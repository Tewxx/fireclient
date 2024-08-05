package com.rooxchicken.fireclient.modules;

import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.glfw.GLFW;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class ScrollClick extends ModuleBase implements HudRenderCallback
{
	private ButtonWidget enabledButton;

	public int clicks = 0;
	
	@Override
	public void Initialize()
	{
		Name = "FullBright";
		Description = "Allows the scroll wheel to act as a click input";
		Enabled = true;
		KeyName = "key.hazelgatekept_scrollclick";

		HasLines = false;

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
				new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_COMMA, category));
		
	}

	@Override
	public void CheckKey() {
		// TODO Auto-generated method stub
		
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
	public void RenderConfiguration(FireClientMainScreen scren, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		
	}
	
	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickDelta)
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
		enabledButton = ButtonWidget.builder(Text.of("FullBright: " + Enabled), _button ->
        {
        	Enabled = !Enabled;
			enabledButton.setMessage(Text.of("FullBright: " + Enabled));
			enabledButton.setTooltip(Tooltip.of(Text.of("Sets fullbright to: " + !Enabled)));
        })
		.dimensions(screen.width / 2 - 50, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets fullbright to: " + !Enabled)))
        .build();
		
		screen.AddDrawableChild(enabledButton);
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		
	}

	@Override
	public void LoadSettings(JsonObject file)
	{
		Enabled = file.get("Enabled").getAsBoolean();
	}

	@Override
	public void SaveSettings(JsonObject file)
	{
		HashMap<String, Object> moduleSettings = new HashMap<String, Object>();
		moduleSettings.put("Enabled", Enabled);

		file.addProperty(Name, new Gson().toJson(moduleSettings));
	}

}
