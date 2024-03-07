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

public class Nametag extends ModuleBase implements HudRenderCallback
{	
	private TextFieldWidget messageInput;

	public String input = "";

	@Override
	public void Initialize()
	{
		Name = "Nametag";
		Enabled = false;
		KeyName = "key.fireclient_nametag";
	
		Scale = 0;
		ScaleX = 0;
		ScaleY = 0;
		
		x2Mod = 0;
		y1Mod = 0;
		y2Mod = 0;

		HasLines = false;

		input = "";

		FireClient.LOGGER.info("Module: " + Name + " loaded successfully.");
	}

	@Override
	public void ClientInitialization()
	{

	}

	@Override
	public void PostInitialization()
	{
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
		}
		
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void Tick()
	{
		
	}

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("Nametag Config"), screen.width / 2, screen.height/2 - 40, 0xffffff);
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("Custom Username in Nametag"), screen.width / 2, screen.height/2 - 20, 0xffffff);
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
		messageInput = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, screen.width/2 - 150, screen.height/2, 300, 15, Text.of("Username"));
		messageInput.setMaxLength(256);
		messageInput.setText(input);

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
		input = scanner.nextLine();
	}

	@Override
	public String SaveSettings()
	{
		String output = "";

		output += Enabled + "\n";
		output += input + "\n";

		return output;
	}

}
