package com.rooxchicken.fireclient.screen;

import com.rooxchicken.fireclient.FireClient;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public class FireClientConfigFailScreen extends Screen
{	
	private ButtonWidget recreateButton;
	private ButtonWidget ignoreButton;
	private ButtonWidget ignoreFutureButton;

	public FireClientConfigFailScreen()
	{
		super(Text.of("FireClient Config Fail"));
	}
	@Override
	public void init()
	{	
		int buttonWidth = 120;
		int buttonHeight = 20;

		MinecraftClient client = MinecraftClient.getInstance();

		recreateButton = ButtonWidget.builder(Text.of("Remake Settings File"), button ->
		{
			FireClient.FIRECLIENT_CONFIGFAIL = false;
			FireClient.ResetConfigFile();

			FireClientMainScreen screen = new FireClientMainScreen();
			client.setScreen(screen);
		})
		.dimensions(width / 2 - (buttonWidth/2) + 5 - 80, height / 2 - (buttonHeight/2) + 25, buttonWidth, buttonHeight)
		.tooltip(Tooltip.of(Text.of("Remakes the config file\n(WILL DELETE SETTINGS)")))
		.build();

		ignoreButton = ButtonWidget.builder(Text.of("Ignore errors"), button ->
		{
			FireClientMainScreen screen = new FireClientMainScreen();
			client.setScreen(screen);
		})
		.dimensions(width / 2 - (buttonWidth/2) + 5 + 80, height / 2 - (buttonHeight/2) + 25, buttonWidth, buttonHeight)
		.tooltip(Tooltip.of(Text.of("Ignores the error and\nproceeds in a broken state")))
		.build();

		buttonWidth += 10;

		ignoreFutureButton = ButtonWidget.builder(Text.of("Ignore (future) errors"), button ->
		{
			FireClient.FIRECLIENT_IGNOREFAIL = true;

			FireClientMainScreen screen = new FireClientMainScreen();
			client.setScreen(screen);
		})
		.dimensions(width / 2 - (buttonWidth/2) + 5, height / 2 - (buttonHeight/2) + 55, buttonWidth, buttonHeight)
		.tooltip(Tooltip.of(Text.of("Continue to ignore for the remaining session\n(Your settings won't be saved!)")))
		.build();

		addDrawableChild(recreateButton);
		addDrawableChild(ignoreButton);

		

		if(FireClient.FIRECLIENT_CONFIGFAILCOUNT > 4)
			addDrawableChild(ignoreFutureButton);

		FireClient.FIRECLIENT_CONFIGFAILCOUNT++;
	}
	 
	@Override
	public void close()
	{
		super.close();
	}
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
		this.renderBackground(context);
		
		String text[] = {"FireClient Error", "", "There was an error loading/saving the", "FireClient configuration file.", "", "What would you like to do?"};
		for(int i = 0; i < text.length; i++)
		{
			context.drawCenteredTextWithShadow(textRenderer, text[i], width/2, height/2 + - 100 + (i*12), 0xFFFFFFFF);
		}
    	
    	super.render(context, mouseX, mouseY, delta);
	}
}










