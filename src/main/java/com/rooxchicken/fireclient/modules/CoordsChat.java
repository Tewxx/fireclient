package com.rooxchicken.fireclient.modules;

import java.util.Scanner;

import org.lwjgl.glfw.GLFW;

import com.ibm.icu.lang.UCharacter.WordBreak;
import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.client.FireClientside;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.message.v1.ClientSendMessageEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents.Last;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents.CommandMessage;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.enchantment.FireAspectEnchantment;
import net.minecraft.network.message.SentMessage.Chat;
import net.minecraft.text.Text;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class CoordsChat extends ModuleBase implements HudRenderCallback
{	
	private TextFieldWidget messageInput;

	public String Players = "";
	private int count = 0;

	private LocalDateTime lastSent;

	@Override
	public void Initialize()
	{
		Name = "CoordsChat";
		Enabled = false;
		KeyName = "key.fireclient_coordschat";
		
		Scale = 0;
		ScaleX = 0;
		ScaleY = 0;
		
		x2Mod = 0;
		y1Mod = 0;
		y2Mod = 0;

		lastSent = LocalDateTime.now();

		HasLines = false;

		Players = "";

		FireClient.LOGGER.info("Module: " + Name + " loaded successfully.");
	}

	@Override
	public void ClientInitialization()
	{

	}

	@Override
	public void PostInitialization()
	{
		Visible = FireClient.FIRECLIENT_WHITELISTED;
		if(!Visible)
			Enabled = false;
	}

	@Override
	public void RegisterKeyBinds(String category)
	{
		UsageKey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, category));
		
	}

	@Override
	public void CheckKey()
	{
		if(UsageKey.wasPressed())
		{
			MinecraftClient client = MinecraftClient.getInstance();

			if(count > 14)
			{
				if(LocalDateTime.now().toEpochSecond(ZoneOffset.of("Z")) - lastSent.toEpochSecond(ZoneOffset.of("Z")) < 10)
				{
					client.inGameHud.getChatHud().addMessage(Text.of("[WARNING] Blocking messages to prevent rate limiting!!"));
					return;
				}
				else
					count = 0;
			}
			else
			{
				//HazelsGatekeptMods.LOGGER.info("" + (LocalDateTime.now().toEpochSecond(ZoneOffset.of("Z")) - lastSent.toEpochSecond(ZoneOffset.of("Z"))));
				if(LocalDateTime.now().toEpochSecond(ZoneOffset.of("Z")) - lastSent.toEpochSecond(ZoneOffset.of("Z")) > 10)
					count = 0;

				lastSent = LocalDateTime.now();
			}

			int x,y,z;
			
			x = (int)client.player.getPos().x;
			y = (int)client.player.getPos().y;
			z = (int)client.player.getPos().z;
			if(Players == "")
			{
				sendChatMessage(" X: " + x + " | Y: " + y + " | Z: " + z);
				return;
			}

			String[] players = Players.split(",");

			for(String player : players)
			{
				sendChatCommand("msg " + player.trim() + " X: " + x + " | Y: " + y + " | Z: " + z);
			}
		}
		
	}

	public void sendChatCommand(String msg)
	{
		MinecraftClient client = MinecraftClient.getInstance();
    	ClientPlayNetworkHandler handler = client.getNetworkHandler();
    	handler.sendChatCommand(msg);

		count++;
	}

	public void sendChatMessage(String msg)
	{
		MinecraftClient client = MinecraftClient.getInstance();
    	ClientPlayNetworkHandler handler = client.getNetworkHandler();
    	handler.sendChatMessage(msg);

		count++;
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void Tick()
	{
		Players = messageInput.getText();
	}

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("CoordsChat Config"), screen.width / 2, screen.height/2 - 40, 0xffffff);
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("Players (empty for public chat)"), screen.width / 2, screen.height/2 - 20, 0xffffff);
	}
	
	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta)
	{
		if(!Enabled)
			return;
	}
	
	@Override
	public void UpdateScreen(boolean mouseDown, int mouseX, int mouseY)
	{
		
	}
	

	@Override
	public void OpenSettingsMenu(FireClientMainScreen screen, ButtonWidget button)
	{
		messageInput = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, screen.width/2 - 150, screen.height/2, 300, 15, Text.of("Message"));
		messageInput.setMaxLength(256);
		messageInput.setText(Players);

		screen.AddDrawableChild(messageInput);
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		
	}

	@Override
	public void LoadSettings(Scanner scanner)
	{
		Enabled = Boolean.parseBoolean(scanner.nextLine());
		Players = scanner.nextLine();
	}

	@Override
	public String SaveSettings()
	{
		String output = "";

		output += Enabled + "\n";
		output += Players + "\n";

		return output;
	}

}
