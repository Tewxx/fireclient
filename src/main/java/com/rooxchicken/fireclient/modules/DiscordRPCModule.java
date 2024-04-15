package com.rooxchicken.fireclient.modules;

import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.glfw.GLFW;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ibm.icu.lang.UCharacter.WordBreak;
import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.client.FireClientside;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import club.minnced.discord.rpc.DiscordRPC;
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

public class DiscordRPCModule extends ModuleBase implements HudRenderCallback
{	
	private TextFieldWidget messageInput;

	public String data = "";
	private int count = 0;

	private LocalDateTime lastSent;

	private DiscordRPC rpc;

	@Override
	public void Initialize()
	{
		Name = "DiscordRPC";
		Description = "WIP :3";
		Enabled = false;
		KeyName = "key.fireclient_discordrpc";
		
		Scale = 0;
		ScaleX = 0;
		ScaleY = 0;
		
		x2Mod = 0;
		y1Mod = 0;
		y2Mod = 0;

		lastSent = LocalDateTime.now();

		HasLines = false;

		data = "";

		FireClient.LOGGER.info("Module: " + Name + " loaded successfully.");
	}

	@Override
	public void ClientInitialization()
	{

	}

	@Override
	public void PostInitialization()
	{
		//rpc = DiscordRPC.INSTANCE;
	}

	@Override
	public void RegisterKeyBinds(String category)
	{
		//UsageKey = KeyBindingHelper.registerKeyBinding(
				//new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, category));
		
	}

	@Override
	public void CheckKey()
	{
		
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void Tick()
	{
		data = messageInput.getText();
	}

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("DiscordRPC Configuration (NOT WORKING! :3 )"), screen.width / 2, screen.height/2 - 35, 0xffffff);
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
		messageInput.setText(data);

		screen.AddDrawableChild(messageInput);
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		
	}

	@Override
	public void LoadSettings(JsonObject file)
	{
		Enabled = file.get("Enabled").getAsBoolean();
		data = file.get("data").getAsString();
	}

	@Override
	public void SaveSettings(JsonObject file)
	{
		HashMap<String, Object> moduleSettings = new HashMap<String, Object>();
		moduleSettings.put("Enabled", Enabled);
		moduleSettings.put("data", data);

		file.addProperty(Name, new Gson().toJson(moduleSettings));
	}

}
