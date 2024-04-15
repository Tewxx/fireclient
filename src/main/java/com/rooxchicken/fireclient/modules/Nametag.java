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

	private ButtonWidget enabledButon;
	private ButtonWidget renderOwnButton;
	private ButtonWidget renderButton;

	public String input = "";

	public boolean RenderOwnNametag = false;
	public boolean RenderNametags = true;

	@Override
	public void Initialize()
	{
		Name = "Nametag";
		Description = "Allows for the customization of the namtag.";
		Enabled = false;
		KeyName = "key.fireclient_nametag";

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
		// UsageKey = KeyBindingHelper.registerKeyBinding(
		// 		new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, category));
		
	}

	@Override
	public void CheckKey()
	{
		// if(UsageKey.wasPressed())
		// {
		// }
		
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void Tick()
	{
		input = messageInput.getText();
	}

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("Nametag Config"), screen.width / 2, screen.height/2 - 40, 0xffffff);
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("Custom Username in Nametag"), screen.width / 2, screen.height/2 + 25, 0xffffff);
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
	public void OpenSettingsMenu(FireClientMainScreen screen, ButtonWidget _button)
	{
		messageInput = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, screen.width/2 - 102, screen.height/2+40, 204, 15, Text.of("Username"));
		messageInput.setMaxLength(32);
		messageInput.setText(input);

		int buttonWidth = 120;

		enabledButon = ButtonWidget.builder(Text.of("Enabled: " + Enabled), button ->
		{
			Enabled = !Enabled;
			button.setMessage(Text.of("Enabled: " + Enabled	));
			button.setTooltip(Tooltip.of(Text.of("Sets the Nametag module to: " + !Enabled)));

		}).dimensions(screen.width/2 - 50, screen.height/2-25, 100, 20).tooltip(Tooltip.of(Text.of("Sets the Nametag module to: " + !Enabled))).build();

		renderOwnButton = ButtonWidget.builder(Text.of("Self Nametag: " + RenderOwnNametag), button ->
		{
			RenderOwnNametag = !RenderOwnNametag;
			button.setMessage(Text.of("Self Nametag: " + RenderOwnNametag));
			button.setTooltip(Tooltip.of(Text.of("Sets the rendering of your player's nametag to: " + !RenderOwnNametag)));

		}).dimensions(screen.width/2 - buttonWidth/2 - 70, screen.height/2, buttonWidth, 20).tooltip(Tooltip.of(Text.of("Sets the rendering of your player's nametag to: " + !RenderOwnNametag))).build();

		renderButton = ButtonWidget.builder(Text.of("All Nametags: " + RenderNametags), button ->
		{
			RenderNametags = !RenderNametags;
			button.setMessage(Text.of("All Nametags: " + RenderNametags));
			button.setTooltip(Tooltip.of(Text.of("Sets the rendering of the all nametags to: " + !RenderNametags)));

		}).dimensions(screen.width/2 - buttonWidth/2 + 70, screen.height/2, buttonWidth, 20).tooltip(Tooltip.of(Text.of("Sets the rendering of all nametags to: " + !RenderOwnNametag))).build();

		screen.AddDrawableChild(messageInput);
		screen.AddDrawableChild(enabledButon);
		screen.AddDrawableChild(renderOwnButton);
		screen.AddDrawableChild(renderButton);
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		
	}t
	@Override
	public void LoadSettings(JsonObject file)
	{
		Enabled = file.get("Enabled").getAsBoolean();
		input = file.get("input").getAsString();

		RenderNametags = file.get("RenderNametags").getAsBoolean();
		RenderOwnNametag = file.get("Enabled").getAsBoolean();

	}

	@Override
	public void SaveSettings(JsonObject file)
	{
		HashMap<String, Object> moduleSettings = new HashMap<String, Object>();
		moduleSettings.put("Enabled", Enabled);
		moduleSettings.put("input", input);

		moduleSettings.put("RenderNametags", RenderNametags);
		moduleSettings.put("RenderOwnNametag", RenderOwnNametag);

		file.addProperty(Name, new Gson().toJson(moduleSettings));
	}

}
