package com.rooxchicken.fireclient;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rooxchicken.fireclient.modules.AngleDisplay;
import com.rooxchicken.fireclient.modules.ArmorHud;
import com.rooxchicken.fireclient.modules.AutoMessage;
import com.rooxchicken.fireclient.modules.Coordinates;
import com.rooxchicken.fireclient.modules.CoordsChat;
import com.rooxchicken.fireclient.modules.FullBright;
import com.rooxchicken.fireclient.modules.ModuleBase;
import com.rooxchicken.fireclient.modules.RenderWorld;
import com.rooxchicken.fireclient.modules.ToggleablePieChart;

public class FireClient implements ModInitializer
{
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("fireclient");
    
    public static String FIRECLIENT_VERSION = "0.1.6";
    public static boolean FIRECLIENT_CONFIGFAIL = false;
    public static boolean FIRECLIENT_IGNOREFAIL = false;
	public static boolean FIRECLIENT_WHITELISTED = false;
    public static int FIRECLIENT_CONFIGFAILCOUNT = 0;

	public static HashMap<String, String> FIRECLIENT_CUSTOMCAPES = new HashMap<String, String>();
    
    public static boolean Setting_Branding = true;
    public static boolean Setting_DEBUG = false;
	public static boolean Setting_CustomCape = true;

    public static boolean Setting_HideShieldsInRiptide = true;

    public static HashMap<String, ModuleBase> Modules;

	@Override
	public void onInitialize()
	{
		LOGGER.info("FireClient V" + FIRECLIENT_VERSION + " (1987)");

		Modules = new HashMap<String, ModuleBase>();
		Modules.put("ToggleablePieChart", new ToggleablePieChart());
		Modules.put("RenderWorld", new RenderWorld());
		Modules.put("FullBright", new FullBright());
		Modules.put("CoordsChat", new CoordsChat());
		Modules.put("Coordinates", new Coordinates());
		Modules.put("AutoMessage", new AutoMessage());
		Modules.put("ArmorHud", new ArmorHud());
		Modules.put("AngleDisplay", new AngleDisplay());

		for(ModuleBase module : Modules.values())
		{
			module.Initialize();
		}
		
		try
		{
			File settingsFile = new File("fireclient.txt");
			if(settingsFile.createNewFile())
			{
				LOGGER.info("Created new FireClient configuration file.");
				
				FileWriter writer = new FileWriter(settingsFile);
				writer.write(settingsToConfig().toCharArray());
				
				writer.close();
			}
			
			Scanner scanner = new Scanner(settingsFile);
			
			String version = scanner.nextLine().substring(19);
			if(version == FIRECLIENT_VERSION)
				LOGGER.warn("Fireclient version mismatch! Running V" + FIRECLIENT_VERSION + " but config is V" + version + "!");

			for(ModuleBase module : Modules.values())
			{
				module.LoadSettings(scanner);
			}

			Setting_Branding = Boolean.parseBoolean(scanner.nextLine());
			Setting_DEBUG = Boolean.parseBoolean(scanner.nextLine());
			
			scanner.close();
		}
		catch(Exception e)
		{
			LOGGER.error("Failed to open FireClient configuration. " + e.getMessage());

			FIRECLIENT_CONFIGFAIL = true;
			FIRECLIENT_CONFIGFAILCOUNT++;
		}
	}
	
	public static void saveConfiguration()
	{
		if(FIRECLIENT_CONFIGFAIL)
			return;

		try
		{
			File settingsFile = new File("fireclient.txt");
			if(settingsFile.createNewFile())
			{
				LOGGER.info("Created new FireClient configuration file.");
			}
			
			FileWriter writer = new FileWriter(settingsFile);
			writer.write(settingsToConfig().toCharArray());
			
			writer.close();
			
			LOGGER.info("Saved FireClient configuration file.");
		}
		catch(Exception e)
		{
			LOGGER.error("Failed to save FireClient configuration. " + e.getMessage());

			FIRECLIENT_CONFIGFAIL = true;
			FIRECLIENT_CONFIGFAILCOUNT++;
		}
	}
	
	public static String settingsToConfig()
	{
		String output = "FIRECLIENT CONFIG V" + FIRECLIENT_VERSION + "\n";

		for(ModuleBase module : Modules.values())
		{
			output += module.SaveSettings();
		}

		output += Setting_Branding + "\n";
		output += Setting_DEBUG + "\n";
		
		return output;
	}

	public static void ResetConfigFile()
	{
		for(ModuleBase module : Modules.values())
		{
			module.Initialize();
		}

		saveConfiguration();
	}
}
