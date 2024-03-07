package com.rooxchicken.fireclient.modules;

import java.util.Scanner;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class Coordinates extends ModuleBase implements HudRenderCallback
{
	private TextRenderer textRenderer;
	
	@Override
	public void Initialize()
	{
		Name = "Coordinates";
		Enabled = true;
		KeyName = "key.fireclient_coordinates";

		PositionX = -318;
		PositionY = -356;
		
		Scale = 1;
		
		x1Mod = -10;
		x2Mod = 100;
		y1Mod = -5;
		y2Mod = 10;

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
	public void OpenSettingsMenu(FireClientMainScreen screen, ButtonWidget button) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		
	}

	@Override
	public void LoadSettings(Scanner scanner)
	{
		Enabled = Boolean.parseBoolean(scanner.nextLine());
		PositionX = Integer.parseInt(scanner.nextLine());
		PositionY = Integer.parseInt(scanner.nextLine());
		Scale = Double.parseDouble(scanner.nextLine());
	}

	@Override
	public String SaveSettings()
	{
		String output = "";

		output += Enabled + "\n";
		output += PositionX + "\n";
		output += PositionY + "\n";
		output += Scale + "\n";

		return output;
	}

}
