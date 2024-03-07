package com.rooxchicken.fireclient.modules;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;

import java.util.Scanner;
import org.lwjgl.glfw.GLFW;
import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;

public class ToggleablePieChart extends ModuleBase
{
    public int PieChartPositionX = -1;
    public int PieChartPositionY = -1;
    public int oldSizeX = -1;
    public int oldSizeY = -1;
    
    public boolean beingRendered = false;
    public int windowIndex = 0;
	public float oldScale = 1;
	
	private ButtonWidget resetButton;

	@Override
	public void Initialize()
	{
		Name = "PieChart";
		KeyName = "key.fireclient_togglepiechart";
		
		PositionX = 1590;
		PositionY = 760;
		
		Scale = 1;
		ScaleX = 1;
		ScaleY = 1;
		
		x2Mod = -340;
		y1Mod = -60;
		y2Mod = -360;
		
		_manualScale = true;

		FireClient.LOGGER.info("Module: " + Name + " loaded successfully.");
	}
	
	@Override
	public void ClientInitialization()
	{
		// WorldRenderEvents.END.register((context) -> {
		// 	handlePieChart();
		// });
	}

	@Override
	public void PostInitialization()
	{
	}
	
	@Override
	public void OpenSettingsMenu(FireClientMainScreen screen, ButtonWidget button)
	{
		MinecraftClient client = MinecraftClient.getInstance();
		
		resetButton = ButtonWidget.builder(Text.of("Reset PieChart"), _button ->
        {
        	Scale = 1;
        	UpdateValues();
			int scalingFactor = (int)client.getWindow().getScaleFactor()*2;
        	PositionX = client.getWindow().getWidth()/scalingFactor-10;
        	PositionY = client.getWindow().getHeight()/scalingFactor-20;

			//FireClient.LOGGER.info(PositionX + " " + PositionY);
        })
		.dimensions(screen.width / 2 - 50, screen.height-30, 100, 20)
        .tooltip(Tooltip.of(Text.of("Resets the PieChart's Position and Scale")))
        .build();
		
		screen.AddDrawableChild(resetButton);
	}
	
	@Override
	public void CloseSettingsMenu(FireClientMainScreen screen)
	{
		SettingsOpen = false;
		//screen.RemoveDrawableChild(resetButton);
		
	}

	@Override
	public void Update()
	{
		
	}
	
	@Override
	public void Tick()
	{
		if(!SettingsOpen)
			return;
		
		
	}
	
	// public void handlePieChart()
	// {
	// 	MinecraftClient client = MinecraftClient.getInstance();
		
	// 	if(!MouseStatus)
	// 	{
	// 		moveChart = false;
	// 		resetOldClick();
	// 		return;
	// 	}
		
	// 	double scalingFactor = client.getWindow().getScaleFactor();
		
	// 	double mouseX = client.mouse.getX()/scalingFactor;
	// 	double mouseY = client.mouse.getY()/scalingFactor;
	// 	int x1 = (int)((PositionX)/scalingFactor*Scale); //RIGHT
	// 	int x2 = (int)((PositionX - 340)/scalingFactor*Scale); //LEFT
		
	// 	int y1 = (int)((PositionY-120)/scalingFactor*Scale); //DOWN
	// 	int y2 = (int)((PositionY-420)/scalingFactor*Scale); //UP
		
	// 	if(!moveChart)
	// 	{
	// 		if(mouseX > x2 && mouseX < x1 && mouseY < y1 && mouseY > y2)
	// 		{
	// 			moveChart = true;
	// 			scaleChart = false;
	// 			scaleX = mouseX;
	// 			scaleY = mouseY;
	// 			oldScale = Scale;
				
	// 			length = x1-x2;
	// 		}
	// 		else
	// 		{
	// 			moveChart = false;
	// 			MouseStatus = false;
				
	// 			return;
	// 		}
			
	// 		if(mouseX > x2 && mouseX < x2+8 && mouseY > y2 && mouseY < y2+8)
	// 	      {
	// 	    	  scaleChart = true;
	// 	      }
	// 	}
		
	// 	if(moveChart && !scaleChart)
	// 	{
	// 		positionX += ((mouseX - oldMouseX) * 1/Scale) * scalingFactor;
	// 		positionY += ((mouseY - oldMouseY) * 1/Scale) * scalingFactor;
	// 	}
		
	// 	if(scaleChart)
	// 	{
	// 		Scale = (float)oldScale * (float)((length-Math.min(mouseX - scaleX, mouseY - scaleY))/length);
	// 	}

		
	// 	//FireClient.LOGGER.info("" + (float)((length-Math.min(mouseX - scaleX, mouseY - scaleY))/length));
	// 	//FireClient.LOGGER.info(mouseX + " | " + scaleX);
		
	// 	resetOldClick();
	// 	//UpdateValues();
	// }
	
// 	private void resetOldClick()
// 	{
// 		MinecraftClient client = MinecraftClient.getInstance();
// 		double scalingFactor = client.getWindow().getScaleFactor();
		
// 		oldMouseX = client.mouse.getX()/scalingFactor;
// 		oldMouseY = client.mouse.getY()/scalingFactor;
// 	}
	
	public void UpdateValues()
	{
		MinecraftClient client = MinecraftClient.getInstance();

		int scalingFactor = (int)client.getWindow().getScaleFactor();

		ScaleX = client.getWindow().getWidth()/screenScale;//(client.getWindow().getFramebufferWidth() / Scale);
		ScaleY = client.getWindow().getHeight()/screenScale;//(client.getWindow().getFramebufferHeight() / Scale);

		//ScaleX = 1920;

		//FireClient.LOGGER.info("" + ScaleX);

		PieChartPositionX = (int)(screenX * (int)scalingFactor / screenScale);
		PieChartPositionY = (int)(screenY * (int)scalingFactor / screenScale)+60;

		//FireClient.LOGGER.info(PieChartPositionX + " " + PieChartPositionY);

		//FireClient.LOGGER.info("" + screenX);
	}
	
	@Override
	public void UpdateScreen(boolean mouseDown, int mouseX, int mouseY)
	{
		
	}
	

	@Override
	public void RenderConfiguration(FireClientMainScreen screen, DrawContext context, TextRenderer textRenderer, int mouseX, int mouseY)
	{	
		if(SettingsOpen)
		{
			((ToggleablePieChart)FireClient.Modules.get("ToggleablePieChart")).UpdateValues();
			context.drawCenteredTextWithShadow(textRenderer, Text.literal("Toggleable PieChart Config"), screen.width / 2, 10, 0xffffff);
			
		// 	double scalingFactor = client.getWindow().getScaleFactor();
			
		// 	int x1 = (int)((PositionX)/scalingFactor*Scale); //RIGHT
		// 	int x2 = (int)((PositionX - 340)/scalingFactor*Scale); //LEFT
			
		// 	int y1 = (int)((PositionY-120)/scalingFactor*Scale); //DOWN
		// 	int y2 = (int)((PositionY-420)/scalingFactor*Scale); //UP
			
		// 	if(scaleChart && MouseStatus)
		// 		context.fill(x2, y2, x2+8, y2+8, 0xFFFF00E4);
		// 	else
		// 		context.fill(x2, y2, x2+8, y2+8, 0xFFFFFFFF);
			
		// 	context.drawHorizontalLine(x1, x2, y1, 0xFFFFFFFF);
		// 	context.drawHorizontalLine(x1, x2, y2, 0xFFFFFFFF);
			
		// 	context.drawVerticalLine(x1, y1, y2, 0xFFFFFFFF);
		// 	context.drawVerticalLine(x2, y1, y2, 0xFFFFFFFF);
		// }

			HandleLines(screen, context, textRenderer, mouseX, mouseY);
		}
	}

	@Override
	public void RegisterKeyBinds(String category)
	{
		UsageKey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(KeyName, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_P, category));
	}

	@Override
	public void CheckKey()
	{
		if(UsageKey.wasPressed())
		{
			Enabled = !Enabled;
			//FireClient.LOGGER.info("" + Enabled);
		}
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

