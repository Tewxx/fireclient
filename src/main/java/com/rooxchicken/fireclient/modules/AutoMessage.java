package com.rooxchicken.fireclient.modules;

import java.util.HashMap;
import java.util.Scanner;

import org.lwjgl.glfw.GLFW;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ibm.icu.lang.UCharacter.WordBreak;
import com.rooxchicken.fireclient.FireClient;
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
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.network.message.SentMessage.Chat;
import net.minecraft.text.Text;

public class AutoMessage extends ModuleBase implements HudRenderCallback
{	
	private ButtonWidget automaticButton;
	private TextFieldWidget messageInput;

	public String LastMessage = "";
	public boolean Automatic = true;

	private String message = "";

	@Override
	public void Initialize()
	{
		Name = "AutoMessage";
		Description = "Stores the last sent message command,\nand inputs it into the chat box when the key is pressed.\nPrevents the repetitive typing of /r and /msg <PLAYER>\n\nAutomatic mode grabs the last sent command and\nwill input it into the chat box.\nManual mode has the user manually type in the filled in command.";
		Enabled = false;
		KeyName = "key.fireclient_automessage";
		
		Scale = 0;
		ScaleX = 0;
		ScaleY = 0;
		
		x2Mod = 0;
		y1Mod = 0;
		y2Mod = 0;

		HasLines = false;

		LastMessage = "";
		message = "";
		Automatic = true;

		FireClient.LOGGER.info("Module: " + Name + " loaded successfully.");
	}

	@Override
	public void ClientInitialization()
	{
		ClientSendMessageEvents.COMMAND.register((message) ->
		{
			if(!Automatic)
				return;
				
			LastMessage = "/" + message;
		});
	}

	@Override
	public void PostInitialization()
	{
		
	}

	@Override
	public void RegisterKeyBinds(String category)
	{
		UsageKey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Y, category));
		
	}

	@Override
	public void CheckKey()
	{
		if(UsageKey.wasPressed())
		{
			//ClientInitialization();
			MinecraftClient client = MinecraftClient.getInstance();

			ChatScreen screen;// = new ChatScreen(message);

			if(Automatic && LastMessage != "")
			{
				String[] split = LastMessage.split(" ");
				if(split.length < 2)
					screen = new ChatScreen("");
				else
				{
					if(split[0].contains("/r"))
						screen = new ChatScreen("/r ");
					else if(split[0].charAt(0) == '/')
						screen = new ChatScreen(split[0] + " " + split[1] + " ");
					else
						screen = new ChatScreen("");
				}
			}
			else if(message != "")
				screen = new ChatScreen(message.trim() + " ");
			else
			screen = new ChatScreen("");

			client.setScreen(screen);
			
		}
		
	}

	@Override
	public void Update() {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void Tick()
	{
		if(!Automatic)
			message = messageInput.getText();
	}

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		context.drawCenteredTextWithShadow(textRenderer, Text.literal("Custom Message"), screen.width / 2, screen.height/2 + 15, 0xffffff);
	}
	
	@Override
	public void onHudRender(DrawContext drawContext, float tickDelta)
	{

	}
	
	@Override
	public void UpdateScreen(boolean mouseDown, int mouseX, int mouseY)
	{
		
	}
	

	@Override
	public void OpenSettingsMenu(FireClientMainScreen screen, ButtonWidget button)
	{
		// cheatMenu = false;
		// int buttonWidth = 135;
		// int buttonHeight = 20;

		automaticButton = ButtonWidget.builder(Text.of("Automatic: " + Automatic), _button ->
        {
			Automatic = !Automatic;
			automaticButton.setMessage(Text.of("Automatic: " + Automatic));
			automaticButton.setTooltip(Tooltip.of(Text.of("Sets Automatic mode to: " + !Automatic)));
        })
		.dimensions(screen.width / 2 - 50, screen.height / 2 - 10, 100, 20)
        .tooltip(Tooltip.of(Text.of("Sets Automatic mode to: " + !Automatic)))
        .build();

		messageInput = new TextFieldWidget(MinecraftClient.getInstance().textRenderer, screen.width/2 - 75, screen.height/2 + 30, 150, 15, Text.of("Message"));
		messageInput.setText(message);

		screen.AddDrawableChild(automaticButton);
		screen.AddDrawableChild(messageInput);
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		
	}

	@Override
	public void LoadSettings(JsonObject file)
	{
		Enabled = file.get("Enabled").getAsBoolean();
		Automatic = file.get("Automatic").getAsBoolean();
		message = file.get("message").getAsString();
	}

	@Override
	public void SaveSettings(JsonObject file)
	{
		HashMap<String, Object> moduleSettings = new HashMap<String, Object>();
		moduleSettings.put("Enabled", Enabled);
		moduleSettings.put("Automatic", Automatic);
		moduleSettings.put("message", message);

		file.addProperty(Name, new Gson().toJson(moduleSettings));
	}

}
