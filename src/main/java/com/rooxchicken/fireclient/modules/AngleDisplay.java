package com.rooxchicken.fireclient.modules;

import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.codec.net.PercentCodec;
import org.lwjgl.glfw.GLFW;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.client.FireClientside;
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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AngleDisplay extends ModuleBase implements HudRenderCallback
{
	private TextRenderer textRenderer;

	private ButtonWidget enabledButton;
	
	@Override
	public void Initialize()
	{
		Name = "AngleDisplay";
		Description = "Shows your up/down angle (pitch)\nin the bottom left of the screen.\n\nWill be movable soon (tm)";
		Enabled = true;
		KeyName = "key.hazelgatekept_angledisplay";

		PositionX = -200;
		PositionY = -100;

		Scale = 1;
		SmallestSize = 1/3.0;
		SnapIncrement = 3;

		x1Mod = -2;
		x2Mod = 34;
		y1Mod = -24;
		y2Mod = -6;
		
		FireClient.LOGGER.info("Module: " + Name + " loaded successfully.");
	}

	@Override
	public void ClientInitialization()
	{
		HudRenderCallback.EVENT.register(this);
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
		//UsageKey = KeyBindingHelper.registerKeyBinding(
				//new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, category));
		
	}

	@Override
	public void CheckKey()
	{
		
	}

	@Override
	public void Update()
	{
		//FireClient.LOGGER.info("" + screenScale);
	}
	
	@Override
	public void Tick()
	{
		
	}

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("AngleDisplay Configuration"), screen.width / 2, screen.height/2 - 35, 0xffffff);
	}

	@Override
	public void BeforeLines(DrawContext context)
	{
		onHudRender(context, RenderTickCounter.ZERO);
		y1Mod = (int)(-24 * Scale);
	}
	
	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickDelta)
	{
		if(!Enabled)
			return; 	
		
		MinecraftClient client = MinecraftClient.getInstance();
		textRenderer = client.textRenderer;

		//FireClient.LOGGER.info(screenX + " | " + screenY);

		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale((float)screenScale, (float)screenScale, (float)screenScale);
		matrixStack.translate(screenX/(float)screenScale, screenY/(float)screenScale, 0);
		drawContext.drawText(textRenderer, String.format("%.2f", -client.player.getPitch()), 0, -16, 0xFFFFFF, true);
		//drawContext.drawText(textRenderer, client.player.getLeaningPitch(0) + "", 0, 0 - 20, 0xFFFFFF, true);
		matrixStack.pop();
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
			enabledButton.setTooltip(Tooltip.of(Text.of("Sets Enabled to:  " + !Enabled)));
        })
		.dimensions(screen.width / 2 - 50, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets Enabled to: " + !Enabled)))
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
