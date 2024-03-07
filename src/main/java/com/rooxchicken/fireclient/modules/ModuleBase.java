package com.rooxchicken.fireclient.modules;

import java.util.Scanner;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;

public abstract class ModuleBase
{
	public String Name;
	public boolean Enabled;
	public boolean Visible = true;
	public boolean HasLines = true;
	public double SmallestSize = 0.2;
	
	public String KeyName;
	public KeyBinding UsageKey;
	
	public boolean SettingsOpen;
	public int MouseStatus;
	public int ManipulationStatus;
	
	public int PositionX;
	public int PositionY;

	public int screenX;
	public int screenY;
	public double screenScale;

	public double Scale;
	public double ScaleX;
	public double ScaleY;
	protected boolean _manualScale = false;
	
	protected int x1Mod;
	protected int x2Mod;
	protected int y1Mod;
	protected int y2Mod;
	
	protected int oldMouseX;
	protected int oldMouseY;
	protected int oldPositionX;
	protected int oldPositionY;
	protected double oldScale;
	protected int length;

	public abstract void Initialize();
	public abstract void ClientInitialization();
	public abstract void PostInitialization();
	public abstract void RegisterKeyBinds(String category);
	
	public abstract void OpenSettingsMenu(FireClientMainScreen screen, ButtonWidget button);
	public abstract void CloseSettingsMenu(FireClientMainScreen screen);
	
	public abstract void CheckKey();
	public abstract void Update();
	public abstract void UpdateScreen(boolean mouseDown, int mouseX, int mouseY);
	public abstract void Tick();

	public abstract void LoadSettings(Scanner scanner);
	public abstract String SaveSettings();

	public void convertPosToScreen()
	{
		MinecraftClient client = MinecraftClient.getInstance();
		double scalingFactor = client.getWindow().getScaleFactor();
		//FireClient.LOGGER.info(Name + ": " + screenX + " | " + screenY);
		screenX = (int)(client.getWindow().getWidth()/(scalingFactor*2)) + PositionX;
		screenY = (int)(client.getWindow().getHeight()/(scalingFactor*3)*3 + PositionY);

		// if(screenX + x1Mod < 0)
		// 	screenX = -x1Mod;

		// if(screenX > client.getWindow().getWidth()/scalingFactor)
		// 	screenX = client.getWindow().getWidth()/(int)scalingFactor;

		if(ManipulationStatus == 2 && MouseStatus == 1)
			screenScale = ((int)(Scale*4))/4.0;
		else
			screenScale = Scale;
		//FireClient.LOGGER.info(scalingFactor + "");

		//FireClient.LOGGER.info(Name + ":" + screenX + " | " + screenY + "   |    " + client.getWindow().getWidth()/scalingFactor);
	}

	public void BeforeLines(DrawContext context) {};
	
	public void HandleLines(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{
		if(!Enabled || !HasLines)
			return;

		BeforeLines(context);
		
		MinecraftClient client = MinecraftClient.getInstance();
		double scalingFactor = client.getWindow().getScaleFactor();

		int x1, x2, y1, y2;
		
		if(!_manualScale)
		{
			x1 = (int)(screenX + x1Mod); //RIGHT
			x2 = (int)(screenX + (x2Mod*screenScale)); //LEFT
			
			y1 = (int)((screenY + y1Mod)); //DOWN
			y2 = (int)(screenY + (y2Mod*screenScale)); //UP
		}
		else
		{
			x2 = (int)(screenX + x1Mod/scalingFactor); //RIGHT
			x1 = (int)(screenX + (x2Mod*screenScale/scalingFactor)); //LEFT
			
			y2 = (int)(screenY + y1Mod/scalingFactor); //DOWN
			y1 = (int)(screenY + (y2Mod*screenScale/scalingFactor)); //UP
		}

		int textXPos = x1;
		int textYPos = -1;

		if(y1 > 10)
			textYPos = y1 - 10;
		else
			textYPos = y2 + 4;

		
		String scale;
		if(Scale % 1 == 0)
			scale = String.format(" %.1f", Scale);
		else
			scale = String.format(" %.2f", Scale);
		context.drawText(textRenderer, Name + scale, textXPos, textYPos, 0xFFFFFFFF, true);
		
		if(ManipulationStatus == 2 && MouseStatus > -1)
			context.fill(x1, y1, x1+8, y1+8, 0xFFFF00E4);
		else
			context.fill(x1, y1, x1+8, y1+8, 0xFFFFFFFF);
		
		context.drawHorizontalLine(x1, x2, y1, 0xFFFFFFFF);
		context.drawHorizontalLine(x1, x2, y2, 0xFFFFFFFF);
		
		context.drawVerticalLine(x1, y1, y2, 0xFFFFFFFF);
		context.drawVerticalLine(x2, y1, y2, 0xFFFFFFFF);
		
		if(!screen.ObjectSelected && MouseStatus > -1 && ManipulationStatus == -1)
		{
			if(AABBCheck(mouseX, mouseY, x1, x2, y1, y2))
			{
				ManipulationStatus = 1;
				if(AABBCheck(mouseX, mouseY, x1, x1+8, y1, y1+8))
				{
					ManipulationStatus = 2;
					oldScale = Scale;
					length = x2-x1;

					oldMouseX = mouseX;
					oldMouseY = mouseY;

					oldPositionX = PositionX;
					oldPositionY = PositionY;

					//FireClient.LOGGER.info("" + oldScale);
				}
				
				FireClient.LOGGER.info("Module: " + Name + " | Passed.");
				screen.ObjectSelected = true;
			}
			else
				ManipulationStatus = 0;
		}
		else if(MouseStatus == -1)
		{
			ManipulationStatus = -1;
			screen.ObjectSelected = false;
		}
		
		if(ManipulationStatus == 1)
		{
			PositionX += ((mouseX - oldMouseX));
			PositionY += ((mouseY - oldMouseY));
			
			//FireClient.LOGGER.info("" + ((mouseX - oldMouseX)));
		}
		
		if(ManipulationStatus == 2)
		{
			if(!_manualScale)
			{
				double mScale = Math.max(mouseX - oldMouseX + 0.0, mouseY - oldMouseY + 0.0);
				if(mScale < 0)
					mScale = Math.min(mouseX - oldMouseX + 0.0, mouseY - oldMouseY + 0.0);

				double val = oldScale * ((length-mScale)/length);


				Scale = val;

				if(Scale < SmallestSize)
				{
					Scale = SmallestSize;
					return;
				}


				PositionX = (int)(oldPositionX + (oldScale-screenScale)*80);
				PositionY = (int)(oldPositionY + (oldScale-screenScale)*15);
			}
			else
			{
				Scale = oldScale * ((length-(Math.min(mouseX - oldMouseX + 0.0, mouseY - oldMouseY + 0.0)))/length);
			}

			if(MouseStatus == 1)
				Scale = ((int)(Scale*4))/4.0;

			if(Scale < SmallestSize)
				Scale = SmallestSize;

			return;
		}
		
		oldMouseX = mouseX;
		oldMouseY = mouseY;
		
		//FireClient.LOGGER.info(PositionX + Name + PositionY);

		//FireClient.LOGGER.info("Module: " + Name + " | (" + x1 + ", " + x2 + ")u(" + y1 + ", " + y2 + "), " + Scale);
		//FireClient.LOGGER.info("Module: " + Name + " | (" + mouseX + ", " + mouseY + ")");
	}
	
	protected boolean AABBCheck(int mouseX, int mouseY, int x1, int x2, int y1, int y2)
	{
		if(!_manualScale)
		{
			return (mouseX > x1 && mouseX < x2 && mouseY < y2 && mouseY > y1);
		}
		else
		{
			return (mouseX > x1 && mouseX < x2 && mouseY < y2 && mouseY > y1);
		}
	}
	
	public abstract void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY);

	public void ResolutionUpdated(double changeX, double changeY)
	{
		//Scale *= change;
		// PositionX += changeX*500;
		// PositionY += changeY*500;
	}
}
