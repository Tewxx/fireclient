package com.rooxchicken.fireclient.modules;

import java.util.Scanner;

import org.apache.commons.codec.net.PercentCodec;
import org.lwjgl.glfw.GLFW;

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
		Enabled = true;
		KeyName = "key.hazelgatekept_angledisplay";
		Scale = 1;
		
		HasLines = false;

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
		
	}
	
	@Override
	public void Tick()
	{
		
	}

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("ArmorHud Configuration"), screen.width / 2, screen.height/2 - 35, 0xffffff);
	}

	@Override
	public void BeforeLines(DrawContext context)
	{
		onHudRender(context, 0);
		y1Mod = (int)(-10 * Scale);
	}
	
	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta)
	{
		if(!Enabled)
			return; 	
		
		MinecraftClient client = MinecraftClient.getInstance();
		textRenderer = client.textRenderer;
		
		PositionX = 8;
		PositionY = client.getWindow().getScaledHeight();

		Scale = 2/3.0;

		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();
		matrixStack.scale((float)Scale, (float)Scale, (float)Scale);
		float posX = PositionX/(float)Scale;
		matrixStack.translate(posX, PositionY/(float)Scale, 0);
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
