package com.rooxchicken.fireclient.modules;

import java.util.HashMap;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class Dummy extends ModuleBase implements HudRenderCallback
{
	private ButtonWidget enabledButton;
	
	@Override
	public void Initialize()
	{
		Name = "Dummy";
		Description = "Dummy module for UI testing";
		Enabled = true;
		KeyName = "key.fireclient_dummy";

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
		
	}

	@Override
	public void RegisterKeyBinds(String category)
	{
		//UsageKey = KeyBindingHelper.registerKeyBinding(
				//new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_<KEY>, category));
		
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
		enabledButton = ButtonWidget.builder(Text.of("Enabled: " + Enabled), _button ->
        {
        	Enabled = !Enabled;
			enabledButton.setMessage(Text.of("Enabled: " + Enabled));
			enabledButton.setTooltip(Tooltip.of(Text.of("Sets enabled to: " + !Enabled)));
        })
		.dimensions(screen.width / 2 - 50, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets enabled to: " + !Enabled)))
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
		
	}

	@Override
	public void SaveSettings(JsonObject file)
	{
		
	}

}
