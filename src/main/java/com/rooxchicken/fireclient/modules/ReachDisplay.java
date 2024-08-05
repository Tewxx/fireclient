package com.rooxchicken.fireclient.modules;

import java.util.HashMap;
import java.util.Scanner;

import org.apache.commons.codec.net.PercentCodec;
import org.lwjgl.glfw.GLFW;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mojang.blaze3d.systems.RenderSystem;
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
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class ReachDisplay extends ModuleBase implements HudRenderCallback
{
	private TextRenderer textRenderer;

	private ButtonWidget enabledButton;
	private double lastReach = -1;
	
	@Override
	public void Initialize()
	{
		Name = "ReachDisplay";
		Description = "Shows how far you are to the\nentity you are looking at!";
		Enabled = true;
		KeyName = "key.fireclient.reachdisplay";

		PositionX = -200;
		PositionY = -100;

		Scale = 1;
		SmallestSize = 1/3.0;
		SnapIncrement = 3;

		x1Mod = -2;
		x2Mod = 24;
		y1Mod = -24;
		y2Mod = -8;
		
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
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("ReachDisplay Configuration"), screen.width / 2, screen.height/2 - 35, 0xffffff);
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

		HitResult result = findCrosshairTarget(client.player, 100, 100, 1.0f);
		String reach = "";
		if(result != null && result.getType().equals(HitResult.Type.ENTITY))
		{
			lastReach = client.player.getEyePos().distanceTo(result.getPos());
			if(lastReach <= 3 && lastReach > 0)
				RenderSystem.setShaderColor(1.0f, 0.0f, 0.0f, 1.0f);
			else
				RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		}
		else
			RenderSystem.setShaderColor(0.6f, 0.6f, 0.6f, 1.0f);

		reach = String.format("%.2f", lastReach);

		drawContext.drawText(textRenderer, reach, 0, -16, 0xFFFFFF, true);
		RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
		matrixStack.pop();
	}

	private HitResult findCrosshairTarget(Entity camera, double blockInteractionRange, double entityInteractionRange, float tickDelta) {
		double d = Math.max(blockInteractionRange, entityInteractionRange);
		double e = MathHelper.square(d);
		Vec3d vec3d = camera.getCameraPosVec(tickDelta);
		HitResult hitResult = camera.raycast(d, tickDelta, false);
		double f = hitResult.getPos().squaredDistanceTo(vec3d);
		if (hitResult.getType() != HitResult.Type.MISS) {
			e = f;
			d = Math.sqrt(f);
		}

		Vec3d vec3d2 = camera.getRotationVec(tickDelta);
		Vec3d vec3d3 = vec3d.add(vec3d2.x * d, vec3d2.y * d, vec3d2.z * d);
		float g = 1.0F;
		Box box = camera.getBoundingBox().stretch(vec3d2.multiply(d)).expand(1.0, 1.0, 1.0);
		EntityHitResult entityHitResult = ProjectileUtil.raycast(camera, vec3d, vec3d3, box, entity -> !entity.isSpectator() && entity.canHit(), e);
		return entityHitResult;
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
