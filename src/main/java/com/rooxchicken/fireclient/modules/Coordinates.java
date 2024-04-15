package com.rooxchicken.fireclient.modules;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Coordinates extends ModuleBase implements HudRenderCallback
{
	private Process p = null;
	private TextRenderer textRenderer;

	private boolean darkMode = false;
	private boolean window = false;
	private int updateInterval = 1;
	private int updateCycle = 0;

	private ButtonWidget enabledButton;
	private ButtonWidget windowButton;
	private ButtonWidget darkmodeButton;
	
	@Override
	public void Initialize()
	{
		Name = "Coordinates";
		Description = "Simple coordinates display.\n\nHas an optional mode that opens a new window with the player's coordinates.\nUseful for streamers";
		Enabled = true;
		KeyName = "key.fireclient_coordinates";

		PositionX = -318;
		PositionY = -356;
		
		Scale = 1;
		
		x1Mod = -10;
		x2Mod = 100;
		y1Mod = -5;
		y2Mod = 10;

		window = false;
		darkMode = false;
		updateInterval = 1;

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
				new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_C, category));
		
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
	public void Update()
	{
		if(!window)
		{
			if(p != null)
				p.destroy();
			return;
		}

		//FireClient.LOGGER.info("" + p.isAlive());

		if(p != null && !p.isAlive())
		{
			p = null;
			window = false;
			if(SettingsOpen)
			{
				windowButton.setMessage(Text.of("Window Mode: " + window));
				windowButton.setTooltip(Tooltip.of(Text.of((window ? "Disables" : "Enables") + " the Window Mode functionality")));
			}
			return;
		}


		updateCycle++;

		if(updateCycle >= updateInterval)
		{
			updateCycle = 0;

			String output = "" + darkMode;
			double x, y, z;

			MinecraftClient client = MinecraftClient.getInstance();
			if(client.player == null)
			{
				if(p != null)
					p.destroy();
				return;
			}
			
			x = client.player.getPos().x;
			y = client.player.getPos().y;
			z = client.player.getPos().z;

			output += String.format("!%1.3f,%2.3f,%3.3f", x, y, z);

			try
			{
				File coordinatesFile = new File("fc_coordinates/fc_coordinates.txt");
				coordinatesFile.createNewFile();
				FileWriter writer = new FileWriter(coordinatesFile);

				writer.write(output);
				writer.close();
			}
			catch(Exception e)
			{
				FireClient.LOGGER.error("Failed to save to coordinates file. " + e.getMessage());
			}
		}
	}
	
	@Override
	public void Tick()
	{
		
	}

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("Coordinates Configuration"), screen.width / 2, screen.height/2 - 35, 0xffffff);
	}

	@Override
	public void BeforeLines(DrawContext context)
	{
		onHudRender(context, 0);
	}
	
	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta)
	{
		if(!Enabled)
			return;
		
		MinecraftClient client = MinecraftClient.getInstance();
		textRenderer = client.textRenderer;

		int x, y, z;
		x = (int)client.player.getPos().x;
		y = (int)client.player.getPos().y;
		z = (int)client.player.getPos().z;

		String coords = "X: " + x + " Y: " + y + " Z: " + z;
		x2Mod = (int)(coords.length()*5.3);

		MatrixStack matrixStack = drawContext.getMatrices();
		matrixStack.push();  
		matrixStack.scale((float)screenScale, (float)screenScale, (float)screenScale);
		matrixStack.translate(screenX/(float)screenScale, screenY/(float)screenScale, 0);
		drawContext.drawText(textRenderer, coords, 0, 0, 0xFFFFFF, true);
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
			enabledButton.setTooltip(Tooltip.of(Text.of("Sets Enabled to: " + !Enabled)));
        })
		.dimensions(screen.width / 2 + 15, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets Enabled to: " + !Enabled)))
        .build();

		windowButton = ButtonWidget.builder(Text.of("Window Mode: " + window), _button ->
        {
			window = !window;
			if(window)
			{
				try
				{
					p = new ProcessBuilder("java","-jar", "fc_coordinates.jar").directory(new File("fc_coordinates")).start();
					//p = Runtime.getRuntime().exec("java -jar fc_coordinates/fc_coordinates.jar", new String[0], new File("fc_coordinates"));
				}
				catch (IOException e)
				{
					FireClient.LOGGER.error("Could not open the fc_coordinates process. Did you install it correctly? (see wiki!)", e);
				}
			}
			windowButton.setMessage(Text.of("Window Mode: " + window));
			windowButton.setTooltip(Tooltip.of(Text.of((window ? "Disables" : "Enables") + " the Window Mode functionality")));
        })
		.dimensions(screen.width / 2 - 115, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of((window ? "Disables" : "Enables") + " the Window Mode functionality")))
        .build();

		darkmodeButton = ButtonWidget.builder(Text.of("Dark Mode: " + darkMode), _button ->
        {
			darkMode = !darkMode;
			darkmodeButton.setMessage(Text.of("Dark Mode: " + darkMode));
			darkmodeButton.setTooltip(Tooltip.of(Text.of("Sets Dark Mode to:  " + !darkMode)));
        })
		.dimensions(screen.width / 2 - 50, screen.height / 2 + 20, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets Dark Mode to: " + !darkMode)))
        .build();

		screen.AddDrawableChild(enabledButton);
		screen.AddDrawableChild(windowButton);
		screen.AddDrawableChild(darkmodeButton);
		
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

		darkMode = file.get("DarkMode").getAsBoolean();
	}

	@Override
	public void SaveSettings(JsonObject file)
	{
		HashMap<String, Object> moduleSettings = new HashMap<String, Object>();
		moduleSettings.put("Enabled", Enabled);
		moduleSettings.put("PositionX", PositionX);
		moduleSettings.put("PositionY", PositionY);
		moduleSettings.put("Scale", Scale);

		moduleSettings.put("DarkMode", darkMode);

		file.addProperty(Name, new Gson().toJson(moduleSettings));
	}

}
