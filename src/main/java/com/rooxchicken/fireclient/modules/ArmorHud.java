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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ArmorHud extends ModuleBase implements HudRenderCallback
{
	public boolean ShowPercentage = false;
	private TextRenderer textRenderer;
	
	private ButtonWidget enabledButton;
	private ButtonWidget percentageButton;
	
	@Override
	public void Initialize()
	{
		Name = "ArmorHud";
		Description = "Displays the armor's durability.\nHas a toggleable percentage mode,\nwhich shows thepercentage of the armor\ninstead of the raw number.";
		Enabled = true;
		KeyName = "key.fireclient_armorhud";
		SmallestSize = 0.6;

		PositionX = 96;
		PositionY = -16;
		
		Scale = 1;
		
		x1Mod = -10;
		x2Mod = 80;
		y1Mod = -15;
		y2Mod = 16;

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
			Enabled = !Enabled;
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
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("ArmorHud Configuration"), screen.width / 2, screen.height/2 - 35, 0xffffff);
	}

	@Override
	public void BeforeLines(DrawContext context)
	{
		onHudRender(context, RenderTickCounter.ZERO);
		y1Mod = (int)(-10 * Scale);
	}
	
	@Override
	public void onHudRender(DrawContext drawContext, RenderTickCounter tickDelta)
	{
		if(!Enabled)
			return; 	
		
		MinecraftClient client = MinecraftClient.getInstance();
		textRenderer = client.textRenderer;
		
		int offset = 20;
		for(int i = 0; i < 4; i++)
		{
			ItemStack item = client.player.getInventory().armor.get(3-i);
			if(item != ItemStack.EMPTY)
			{
				//drawContext.drawTexture(new Identifier("minecraft:textures/item/" + item.getTranslationKey().substring(15) + ".png"), (int)(screenX+offset*(i*screenScale)), screenY, 0, 0, (int)(16*screenScale), (int)(16*screenScale), (int)(16*screenScale), (int)(16*screenScale));
				double percentage = (item.getMaxDamage() - item.getDamage() + 0.0) / item.getMaxDamage();
				
				int color = 0x2BEE00;
				if(item.getDamage() == 0)
					color = 0x099A00;
				
				if(percentage <= 0.5)
					color = 0xFFFC36;
				if(percentage <= 0.15)
					color = 0xD50000;
				
					MatrixStack matrixStack = drawContext.getMatrices();
					matrixStack.push();  
					matrixStack.scale((float)screenScale, (float)screenScale, (float)screenScale);
					matrixStack.translate(screenX/(float)screenScale, screenY/(float)screenScale, 0);
					drawContext.drawItem(item, offset*i, 0);
					if(ShowPercentage)
					{
						String txt = "";
						if(percentage == 1)
							txt = "1.0";
						else
							txt = String.format("%.2f", percentage).substring(1);
						drawContext.drawText(textRenderer, txt, offset*i, -8, color, true);
					}
					else
						drawContext.drawText(textRenderer, item.getMaxDamage() - item.getDamage() + "", offset*i, -8, color, true);
					matrixStack.pop();
			}
		}
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
		.dimensions(screen.width / 2 - 110, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets Enabled to: " + !Enabled)))
        .build();

		percentageButton = ButtonWidget.builder(Text.of("Mode: " + (ShowPercentage ? "Percent" : "Value")), _button ->
        {
			ShowPercentage = !ShowPercentage;
			percentageButton.setMessage(Text.of("Mode: " + (ShowPercentage ? "Percent" : "Value")));
			percentageButton.setTooltip(Tooltip.of(Text.of("Sets the mode to:  " + (!ShowPercentage ? "Percent" : "Value"))));
        })
		.dimensions(screen.width / 2 + 10, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets the mode to:  " + (!ShowPercentage ? "Percent" : "Value"))))
        .build();

		screen.AddDrawableChild(enabledButton);
		screen.AddDrawableChild(percentageButton);
		
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		
	}

	@Override
	public void LoadSettings(JsonObject file)
	{
		Enabled = file.get("Enabled").getAsBoolean();
		PositionX = file.get("PositionX").getAsInt();
		PositionY = file.get("PositionY").getAsInt();
		Scale = file.get("Scale").getAsDouble();
		ShowPercentage = file.get("ShowPercentage").getAsBoolean();
	}

	@Override
	public void SaveSettings(JsonObject file)
	{
		HashMap<String, Object> moduleSettings = new HashMap<String, Object>();
		moduleSettings.put("Enabled", Enabled);
		moduleSettings.put("PositionX", PositionX);
		moduleSettings.put("PositionY", PositionY);
		moduleSettings.put("Scale", Scale);
		moduleSettings.put("ShowPercentage", ShowPercentage);

		file.addProperty(Name, new Gson().toJson(moduleSettings));
	}

}
