package com.rooxchicken.fireclient.screen;

import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.modules.ModuleBase;
import com.rooxchicken.fireclient.modules.ScrollClick;

import net.minecraft.client.Mouse;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.text.Text;

public class FireClientMainScreen extends Screen
{	
	private ButtonWidget settingsButton;
	private ButtonWidget modulesButton;
	private ButtonWidget reinitializeButton;

	private ArrayList<ButtonWidget> fireclientSettings;

	private int submenu = 0;
	
	public int mouseStatus = 0;
	
	public boolean ObjectSelected = false;
	
	private ArrayList<ButtonWidget> moduleButtons;
	private ModuleBase activeModule;

	public FireClientMainScreen()
	{
		super(Text.of("FireClient Main Config"));
	}
	@Override
	public void init()
	{	
		moduleButtons = new ArrayList<ButtonWidget>();
		
		int settingsButtonWidth = 120;
		int settingsButtonHeight = 20;
		modulesButton = ButtonWidget.builder(Text.of("Configure Modules"), button ->
		{
			for(ButtonWidget module : moduleButtons)
			{
				module.visible = true;
			}
			
			modulesButton.visible = false;
			settingsButton.visible = false;
			submenu = 1;
		})
			.dimensions(width / 2 - (settingsButtonWidth/2), height / 2 - (settingsButtonHeight/2), settingsButtonWidth, settingsButtonHeight)
			.build();

		settingsButton = ButtonWidget.builder(Text.of("FireClient Settings"), button ->
		{
			
			modulesButton.visible = false;
			settingsButton.visible = false;
			submenu = 3;
			goToSubmenu();
		})
			.dimensions(width / 2 - (settingsButtonWidth/2) + 5, height / 2 - (settingsButtonHeight/2) + 25, settingsButtonWidth-10, settingsButtonHeight)
			.build();

			reinitializeButton = ButtonWidget.builder(Text.of("DEBUG_ReInit"), button ->
			{
				for(ModuleBase module : FireClient.Modules.values())
				{
					module.Initialize();
					module.PostInitialization();
				}
				
			})
				.dimensions(4, height - 26 - settingsButtonHeight/2, settingsButtonWidth-20, settingsButtonHeight)
				.build();
		
		int buttonIndex = 0;
		
		for(ModuleBase module : FireClient.Modules.values())
		{
			if(module.Visible)
			{
				if(submenu != 2)
					module.SettingsOpen = false;
				
				moduleButtons.add(ButtonWidget.builder(Text.of(module.Name), button ->
				{
					submenu = 2;
					module.OpenSettingsMenu(this, button);
					module.SettingsOpen = true;
					activeModule = module;

					for(ButtonWidget _module : moduleButtons)
					{
						_module.visible = false;
					}

					addDrawableChild(ButtonWidget.builder(Text.of("About " + activeModule.Name), abtbutton ->
					{
						activeModule.SettingsOpen = false;
						submenu = 4;
						clearAndInit();
					}
					).dimensions(width - 120, height - 30, 100, 20).tooltip(Tooltip.of(Text.of("Gives the description of the " + activeModule.Name + " module."))).build());
				})
					.dimensions(width/2 + ((buttonIndex%3) * 90)-130, height / 2 + (30 * ((buttonIndex/3)-1)), 80, 20)
					.build());

				moduleButtons.get(buttonIndex).visible = false;
				addDrawableChild(moduleButtons.get(buttonIndex));

				buttonIndex++;
			}

		}
		int buttonWidth = 100;

		fireclientSettings = new ArrayList<ButtonWidget>();

		if(FireClient.Setting_DEBUG)
		{
			fireclientSettings.add(ButtonWidget.builder(Text.of("Debug Settings"), button ->
			{
				FireClient.FIRECLIENT_WHITELISTED = true;
			}).dimensions(0, 0, buttonWidth, 20).build());

			fireclientSettings.add(ButtonWidget.builder(Text.of("Branding: " + FireClient.Setting_Branding), button ->
			{
				FireClient.Setting_Branding = !FireClient.Setting_Branding;
				button.setMessage(Text.of("Branding: " + FireClient.Setting_Branding));
				button.setTooltip(Tooltip.of(Text.of("Sets the branding to: " + !FireClient.Setting_Branding)));
			}).dimensions(0, 0, buttonWidth, 20).tooltip(Tooltip.of(Text.of("Sets the branding to: " + !FireClient.Setting_Branding))).build());
		}

		fireclientSettings.add(ButtonWidget.builder(Text.of("Riptide Shield: " + FireClient.Setting_HideShieldsInRiptide), button ->
		{
			FireClient.Setting_HideShieldsInRiptide = !FireClient.Setting_HideShieldsInRiptide;
			button.setMessage(Text.of("Riptide Shield: " + FireClient.Setting_HideShieldsInRiptide));
		}).dimensions(0, 0, buttonWidth, 20).tooltip(Tooltip.of(Text.of("Hides shields when riptiding with a trident."))).build());
		
		fireclientSettings.add(ButtonWidget.builder(Text.of("Debug Mode: " + FireClient.Setting_DEBUG), button ->
		{
			FireClient.Setting_DEBUG = !FireClient.Setting_DEBUG;
			button.setMessage(Text.of("Debug Mode: " + FireClient.Setting_DEBUG));
			button.setTooltip(Tooltip.of(Text.of("Sets debug mode to: " + !FireClient.Setting_DEBUG)));
		}).dimensions(0, 0, buttonWidth, 20).tooltip(Tooltip.of(Text.of("Sets debug mode to: " + !FireClient.Setting_DEBUG))).build());

		fireclientSettings.add(ButtonWidget.builder(Text.of("Custom Capes: " + FireClient.Setting_CustomCape), button ->
		{
			FireClient.Setting_CustomCape = !FireClient.Setting_CustomCape;
			button.setMessage(Text.of("Custom Capes: " + FireClient.Setting_CustomCape));
			button.setTooltip(Tooltip.of(Text.of("Sets custom capes to: " + !FireClient.Setting_CustomCape)));
		}).dimensions(0, 0, buttonWidth, 20).tooltip(Tooltip.of(Text.of("Sets custom capes to: " + !FireClient.Setting_CustomCape))).build());

		// fireclientSettings.add(ButtonWidget.builder(Text.of(""), button ->
		// {
			
		// }).dimensions(0, 0, buttonWidth, 20).build());

		buttonIndex = 0;
		for(ButtonWidget button : fireclientSettings)
		{
			button.setPosition(width/2 + ((buttonIndex%3) * 110)-160, height / 2 + (30 * ((buttonIndex/3)-1)));
			buttonIndex++;
		}
		
		addDrawableChild(modulesButton);
		addDrawableChild(settingsButton);
		if(FireClient.Setting_DEBUG)
			addDrawableChild(reinitializeButton);
		
		goToSubmenu();
	}
	 
	@Override
	public void close()
	{
		super.close();
	}
	 
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
    	mouseStatus = button;
		FireClient.LOGGER.info("" + button);
    	for(ModuleBase module : FireClient.Modules.values())
		{
			module.MouseStatus = button;
		}
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
    	mouseStatus = -1;
    	for(ModuleBase module : FireClient.Modules.values())
		{
			module.MouseStatus = -1;
		}
    	
    	FireClient.saveConfiguration();
        return super.mouseReleased(mouseX, mouseY, button);
    }
    
    @Override
    public void tick()
    {
    	for(ModuleBase module : FireClient.Modules.values())
    	{
    		if(module.SettingsOpen)
    			module.Tick();
    	}
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE)
        {
        	
            switch(submenu)
            {
            case 0:
            	return super.keyPressed(keyCode, scanCode, modifiers);
            case 1:
            	goToDefaultMenu();
            	this.clearAndInit();
            	break;

			case 4:
				submenu = 2;
				this.clearAndInit();
				break;
            	
			case 3:
				goToDefaultMenu();
				this.clearAndInit();
				break;

            default:
            	goToConfigMenu();
            	this.clearAndInit();
            	break;
            }
            return true; // Consumes the key event
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }
    
    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta)
    {
		this.renderBackground(context, mouseX, mouseY, delta);
		if(FireClient.Setting_DEBUG)
			context.fill(mouseX, mouseY, mouseX+2, mouseY+2, 0xFF0000E4);
    	
    	switch(submenu)
    	{
    	case 0:
    		renderMainScreen(context, mouseX, mouseY, delta);
			context.drawCenteredTextWithShadow(textRenderer, Text.literal("FireClient Config"), width / 2, 10, 0xffffff);
			context.drawCenteredTextWithShadow(textRenderer, Text.literal("Tip: Left click for smooth | Right click for snap"), 120, height - 12, 0xAAAAAA);
    		break;
		case 1:
			context.drawCenteredTextWithShadow(textRenderer, Text.literal("FireClient Config"), width / 2, 10, 0xffffff);
			context.drawCenteredTextWithShadow(textRenderer, Text.literal("Modules"), width / 2, height/2 - 60, 0xffffff);
			break;
    	case 2:
			activeModule.RenderConfiguration(this, context, textRenderer, mouseX, mouseY);
			activeModule.HandleLines(this, context, textRenderer, mouseX, mouseY);
    		break;
		case 3:
			context.drawCenteredTextWithShadow(textRenderer, Text.literal("FireClient Config"), width / 2, 10, 0xffffff);
			context.drawCenteredTextWithShadow(textRenderer, Text.literal("Settings"), width / 2, height/2 - 60, 0xffffff);
			if(FireClient.FIRECLIENT_WHITELISTED)
				context.drawText(textRenderer, Text.literal("You are whitelisted!"), 4, height - 12, 0xAAAAAA, true);
			break;
		
		case 4:
			int i = 0;
			for(String line : activeModule.Description.split("\n"))
			{
				context.drawCenteredTextWithShadow(textRenderer, Text.literal(line), width / 2, height/2 - 60 + (i * 12), 0xffffff);
				i++;
			}
			break;
    	}
    	
    	super.render(context, mouseX, mouseY, delta);
    }
    
    private void renderMainScreen(DrawContext context, int mouseX, int mouseY, float delta)
    {
    	for(ModuleBase module : FireClient.Modules.values())
		{
			module.HandleLines(this, context, textRenderer, mouseX, mouseY);
		}
    }
    
    private void goToDefaultMenu()
    {
    	for(ButtonWidget module : moduleButtons)
		{
			module.visible = false;
		}
    	
    	settingsButton.visible = true;
    	modulesButton.visible = true;
    	submenu = 0;
    }
    
    private void goToConfigMenu()
    {
    	for(ButtonWidget module : moduleButtons)
		{
			module.visible = true;
		}
    	for(ModuleBase module : FireClient.Modules.values())
		{
    		module.CloseSettingsMenu(this);
		}
    	
    	modulesButton.visible = false;
    	settingsButton.visible = false;
    	
    	submenu = 1;
    }
    
    public <T extends Element & Drawable & Selectable> T AddDrawableChild(T drawableChild)
    {
    	addDrawableChild(drawableChild);
    	return drawableChild;
    }
    
//    public <T extends Element & Drawable & Selectable> T RemoveDrawableChild(T drawableChild)
//    {
//    	children().remove(drawableChild);
//    	return null;
//    }
    
    private void goToSubmenu()
    {
    	switch(submenu)
        {
        case 1:
        	goToConfigMenu();
        	break;
        	
        case 2:
			activeModule.OpenSettingsMenu(this, settingsButton);
			for(ButtonWidget _module : moduleButtons)
			{
				_module.visible = false;
			}

			addDrawableChild(ButtonWidget.builder(Text.of("About " + activeModule.Name), abtbutton ->
			{
				activeModule.SettingsOpen = false;
				submenu = 4;
				clearAndInit();
				goToSubmenu();
			}
			).dimensions(width - 120, height - 30, 100, 20).tooltip(Tooltip.of(Text.of("Gives the description of the " + activeModule.Name + " module."))).build());

			modulesButton.visible = false;
			settingsButton.visible = false;
			break;

		case 3:
			modulesButton.visible = false;
			settingsButton.visible = false;
			for(ButtonWidget m : fireclientSettings)
				addDrawableChild(m);

			break;

		case 4:
			modulesButton.visible = false;
			settingsButton.visible = false;
			addDrawableChild(ButtonWidget.builder(Text.of("Back"), abtbutton ->
			{
				submenu = 2;
				clearAndInit();
			}
			).dimensions(width - 100, height - 30, 80, 20).tooltip(Tooltip.of(Text.of("Returns to the module settings."))).build());
			break;
        }
    }

	public void ToModuleMenu()
	{
		submenu = 1;
		goToSubmenu();
	}
}










