package com.rooxchicken.fireclient.event;

import org.lwjgl.glfw.GLFW;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.modules.ModuleBase;
import com.rooxchicken.fireclient.screen.FireClientConfigFailScreen;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

//import me.shedaniel.clothconfig2.api.ConfigBuilder;
//import me.shedaniel.clothconfig2.api.ConfigCategory;
//import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;

public class KeyInputHandler
{
	public static final String KEY_CATEGORY_FIRECLIENT = "key.category.fireclient";
	public static final String KEY_FIRECLIENT_OPENCONFIG = "key.fireclient_openconfig";
	public static KeyBinding openConfigKey;
	
	public static void registerKeyInputs()
	{
		ClientTickEvents.END_CLIENT_TICK.register(client ->
		{
			if(openConfigKey.wasPressed())
			{
				if(!FireClient.FIRECLIENT_CONFIGFAIL || FireClient.FIRECLIENT_IGNOREFAIL)
				{
					FireClientMainScreen screen = new FireClientMainScreen();
					client.setScreen(screen);
				}
				else
				{
					FireClientConfigFailScreen screen = new FireClientConfigFailScreen();
					client.setScreen(screen);
				}
			}
			
			for(ModuleBase module : FireClient.Modules.values())
			{
				module.CheckKey();
			}
		});
			
//			if(openConfigKey.wasPressed())
//			{
//				ConfigBuilder builder = ConfigBuilder.create()
//				        .setTitle(Text.of("FireClient Settings"))
//				        .setTransparentBackground(true);
//				
//				ConfigEntryBuilder entryBuilder = builder.entryBuilder();
//				
//				ConfigCategory mainSettings = builder.getOrCreateCategory(Text.of("Main Settings"));
//				
//				mainSettings.addEntry(entryBuilder.startBooleanToggle(Text.of("Fullbright"), FireClient.Setting_Fullbright)
//				        .setDefaultValue(false) // Recommended: Used when user click "Reset"
//				        .setTooltip(Text.of("Toggles whether Fullbright is enabled")) // Optional: Shown when the user hover over this option
//				        .setSaveConsumer(newValue -> {FireClient.Setting_Fullbright = newValue; if(newValue) client.options.getGamma().setValue(100000.0); else client.options.getGamma().setValue(1.0); FireClient.saveConfiguration(); } ) // Recommended: Called when user save the config
//				        .build()); // Builds the option entry for cloth config
//				
//				mainSettings.addEntry(entryBuilder.startBooleanToggle(Text.of("Render ArmorHud"), FireClient.Setting_RenderArmorHud)
//				        .setDefaultValue(false) // Recommended: Used when user click "Reset"
//				        .setTooltip(Text.of("Toggles whether Fullbright is enabled")) // Optional: Shown when the user hover over this option
//				        .setSaveConsumer(newValue -> {FireClient.Setting_RenderArmorHud = newValue; FireClient.saveConfiguration(); } ) // Recommended: Called when user save the config
//				        .build()); // Builds the option entry for cloth config
//				
//				mainSettings.addEntry(entryBuilder.startBooleanToggle(Text.of("Auto React"), FireClient.Setting_AutoReaction)
//				        .setDefaultValue(false) // Recommended: Used when user click "Reset"
//				        .setTooltip(Text.of("Toggles whether the mod will automatically react to the correct chat message prompts")) // Optional: Shown when the user hover over this option
//				        .setSaveConsumer(newValue -> {FireClient.Setting_AutoReaction = newValue; FireClient.saveConfiguration(); } ) // Recommended: Called when user save the config
//				        .build()); // Builds the option entry for cloth config
//				
//				mainSettings.addEntry(entryBuilder.startBooleanToggle(Text.of("Render Health Difference"), FireClient.Setting_HealthDifferenceDisplay)
//				        .setDefaultValue(false) // Recommended: Used when user click "Reset"
//				        .setTooltip(Text.of("Toggles whether the mod will show the difference between you and your attacker's health")) // Optional: Shown when the user hover over this option
//				        .setSaveConsumer(newValue -> {FireClient.Setting_HealthDifferenceDisplay = newValue; FireClient.saveConfiguration(); } ) // Recommended: Called when user save the config
//				        .build()); // Builds the option entry for cloth config
//				
//				ConfigCategory piechartSettings = builder.getOrCreateCategory(Text.of("Piechart Settings"));
//				
//				piechartSettings.addEntry(entryBuilder.startBooleanToggle(Text.of("Visible"), FireClient.renderPieChart)
//				        .setDefaultValue(false) // Recommended: Used when user click "Reset"
//				        .setTooltip(Text.of("Toggles the PieChart's Visibility")) // Optional: Shown when the user hover over this option
//				        .setSaveConsumer(newValue -> { FireClient.renderPieChart = newValue; FireClient.saveConfiguration(); }) // Recommended: Called when user save the config
//				        .build()); // Builds the option entry for cloth config
//				
//				piechartSettings.addEntry(entryBuilder.startIntField(Text.of("Position X"), FireClient.PieChartPositionX)
//				        .setDefaultValue(1920) // Recommended: Used when user click "Reset"
//				        .setTooltip(Text.of("Changes the PieChart's X value")) // Optional: Shown when the user hover over this option
//				        .setSaveConsumer(newValue -> { FireClient.PieChartPositionX = newValue; FireClient.saveConfiguration(); }) // Recommended: Called when user save the config
//				        .build()); // Builds the option entry for cloth config
//				piechartSettings.addEntry(entryBuilder.startIntField(Text.of("Position Y"), FireClient.PieChartPositionY)
//				        .setDefaultValue(1080) // Recommended: Used when user click "Reset"
//				        .setTooltip(Text.of("Changes the PieChart's Y value")) // Optional: Shown when the user hover over this option
//				        .setSaveConsumer(newValue -> { FireClient.PieChartPositionY = newValue; FireClient.saveConfiguration(); }) // Recommended: Called when user save the config
//				        .build()); // Builds the option entry for cloth config
//				
//				piechartSettings.addEntry(entryBuilder.startFloatField(Text.of("Scale"), FireClient.PieChartScale)
//				        .setDefaultValue(1f) // Recommended: Used when user click "Reset"
//				        .setTooltip(Text.of("Changes the PieChart's Scale value")) // Optional: Shown when the user hover over this option
//				        .setSaveConsumer(newValue -> { FireClient.PieChartScale = newValue; FireClient.saveConfiguration(); }) // Recommended: Called when user save the config
//				        .build()); // Builds the option entry for cloth config
//				
//				
//				
//				Screen screen = builder.build();
//				client.setScreen(screen);
			//}
			
//			if(toggleWorldKey.wasPressed())
//				FireClient.renderWorld = !FireClient.renderWorld;
//			
//			if(toggleArmorKey.wasPressed())
//				FireClient.Setting_RenderArmorHud = !FireClient.Setting_RenderArmorHud;
		//});
	}
	
	public static void register()
	{
		openConfigKey = KeyBindingHelper.registerKeyBinding(
				new KeyBinding(KEY_FIRECLIENT_OPENCONFIG, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, KEY_CATEGORY_FIRECLIENT));
		
		for(ModuleBase module : FireClient.Modules.values())
		{
			module.RegisterKeyBinds(KEY_CATEGORY_FIRECLIENT);
		}
		
//		toggleWorldKey = KeyBindingHelper.registerKeyBinding(
//				new KeyBinding(KEY_FIRECLIENT_TOGGLEWORLD, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_Z, KEY_CATEGORY_FIRECLIENT));
//		
//		toggleArmorKey = KeyBindingHelper.registerKeyBinding(
//				new KeyBinding(KEY_FIRECLIENT_TOGGLEARMOR, InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_K, KEY_CATEGORY_FIRECLIENT));
		
		registerKeyInputs();
	}
}
