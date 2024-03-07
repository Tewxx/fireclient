package com.rooxchicken.fireclient.modules;

import java.util.Scanner;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class RenderWorld extends ModuleBase implements HudRenderCallback
{	
	private ButtonWidget enabledButton;
	private ButtonWidget confirmButton;
	private ButtonWidget denyButton;
	private boolean cheatMenu = false;

	public boolean renderWorld = true;

	@Override
	public void Initialize()
	{
		Name = "RenderWorld";
		Enabled = false;
		KeyName = "key.fireclient_renderworld";
		
		Scale = 0;
		ScaleX = 0;
		ScaleY = 0;
		
		x2Mod = 0;
		y1Mod = 0;
		y2Mod = 0;

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
		UsageKey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, category));
		
	}

	@Override
	public void CheckKey()
	{
		if(UsageKey.wasPressed())
		{
			renderWorld = !renderWorld;
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
		if(!cheatMenu)
			return;

		String text[] = {"Cheater!!! >:(", "", "This module was made to be a way", "to toggle F3+A spamming.", "Because this has now been patched,", "and the fact that this mod also allows you to", "see block entities through walls,", "it is considered a CHEAT!!!", "", "Are you sure you want to enable this?"};
		for(int i = 0; i < text.length; i++)
		{
			context.drawCenteredTextWithShadow(textRenderer, text[i], screen.width/2, screen.height/2 + - 100 + (i*12), 0xFFFFFFFF);
		}
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
		cheatMenu = false;
		int buttonWidth = 135;
		int buttonHeight = 20;

		enabledButton = ButtonWidget.builder(Text.of("RenderWorld: " + Enabled), _button ->
        {
			if(Enabled)
			{
				Enabled = false;
				enabledButton.setMessage(Text.of("RenderWorld: false"));
				enabledButton.setTooltip(Tooltip.of(Text.of("Sets RenderWorld to: " + !Enabled)));
			}
			else
			{
				screen.AddDrawableChild(confirmButton);
				screen.AddDrawableChild(denyButton);

				enabledButton.active = false;
				enabledButton.visible = false;

				confirmButton.active = true;
				confirmButton.visible = true;
				denyButton.active = true;
				denyButton.visible = true;

				cheatMenu = true;
			}

        })
		.dimensions(screen.width / 2 - 50, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets RenderWorld to: " + !Enabled)))
        .build();

		confirmButton = ButtonWidget.builder(Text.of("Yes, I am a dirty cheater"), _button ->
        {
        	Enabled = true;
			cheatMenu = false;

			enabledButton.active = true;
			enabledButton.visible = true;

			confirmButton.active = false;
			confirmButton.visible = false;
			denyButton.active = false;
			denyButton.visible = false;

			enabledButton.setMessage(Text.of("RenderWorld: true"));
			enabledButton.setTooltip(Tooltip.of(Text.of("Sets RenderWorld to: " + !Enabled)));
        })
		.dimensions(screen.width / 2 - (buttonWidth/2) + 5 + 80, screen.height / 2 - (buttonHeight/2) + 80, buttonWidth, buttonHeight)
        .tooltip(Tooltip.of(Text.of("Enables the RenderWorld module")))
        .build();

		denyButton = ButtonWidget.builder(Text.of("No, I am innocent :D"), _button ->
        {
        	Enabled = false;
			cheatMenu = false;

			enabledButton.active = true;
			enabledButton.visible = true;

			confirmButton.active = false;
			confirmButton.visible = false;
			denyButton.active = false;
			denyButton.visible = false;
        })
		.dimensions(screen.width / 2 - (buttonWidth/2) + 5 - 80, screen.height / 2 - (buttonHeight/2) + 80, buttonWidth, buttonHeight)
        .tooltip(Tooltip.of(Text.of("Disables the RenderWorld module")))
        .build();
		
		screen.AddDrawableChild(enabledButton);
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		
	}

	@Override
	public void LoadSettings(Scanner scanner)
	{
		Enabled = Boolean.parseBoolean(scanner.nextLine());
	}

	@Override
	public String SaveSettings()
	{
		String output = "";

		output += Enabled + "\n";

		return output;
	}

}
