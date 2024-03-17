package com.rooxchicken.fireclient;

import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.include.com.google.gson.JsonElement;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import com.rooxchicken.fireclient.modules.AngleDisplay;
import com.rooxchicken.fireclient.modules.ArmorHud;
import com.rooxchicken.fireclient.modules.AutoMessage;
import com.rooxchicken.fireclient.modules.Coordinates;
import com.rooxchicken.fireclient.modules.CoordsChat;
import com.rooxchicken.fireclient.modules.DiscordRPCModule;
import com.rooxchicken.fireclient.modules.FullBright;
import com.rooxchicken.fireclient.modules.ModuleBase;
import com.rooxchicken.fireclient.modules.Nametag;
import com.rooxchicken.fireclient.modules.RenderWorld;
import com.rooxchicken.fireclient.modules.ToggleablePieChart;

import it.unimi.dsi.fastutil.Hash;

public class FireClient implements ModInitializer
{
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("fireclient");
    
    public static String FIRECLIENT_VERSION = "0.1.9";
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
		Modules.put("Nametag", new Nametag());
		Modules.put("FullBright", new FullBright());
		Modules.put("DiscordRPC", new DiscordRPCModule());
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
				
				ResetConfigFile();
			}
			
			Scanner scanner = new Scanner(settingsFile);
			Gson gson = new Gson();
			JsonObject file = new Gson().fromJson(scanner.nextLine(), JsonObject.class);
			
			JsonObject fireClientSettings = gson.fromJson(gson.fromJson(file.get("FireClient Settings"), JsonPrimitive.class).getAsString(), JsonObject.class);
			
			String version = file.get("FIRECLIENT_CONFIG_VERSION").getAsString();

			if(version == FIRECLIENT_VERSION)
				LOGGER.warn("Fireclient version mismatch! Running V" + FIRECLIENT_VERSION + " but config is V" + version + "! Will attempt to update!!");

			for(ModuleBase module : Modules.values())
			{
				try
				{
					module.LoadSettings(gson.fromJson(gson.fromJson(file.get(module.Name), JsonPrimitive.class).getAsString(), JsonObject.class));
				}
				catch(Exception e)
				{
					LOGGER.error("Module " + module.Name + " has a *missing or broken* config! If you are updating FireClient versions, this is safe to ignore! (error regardless) " + e.getMessage());
				}
			}

			Setting_Branding = fireClientSettings.get("Branding").getAsBoolean();
			Setting_CustomCape = fireClientSettings.get("Custom Capes").getAsBoolean();
			Setting_HideShieldsInRiptide = fireClientSettings.get("Hide Shields in Riptide").getAsBoolean();
			Setting_DEBUG = fireClientSettings.get("Debug Mode").getAsBoolean();
			
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
			writer.write(settingsToConfig().toString());
			
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
	
	public static JsonObject settingsToConfig()
	{
		Gson gson = new Gson();
		JsonObject file = new JsonObject();

		HashMap<String, Object> fireClientSettings = new HashMap<String, Object>();
		fireClientSettings.put("Branding", Setting_Branding);
		fireClientSettings.put("Custom Capes", Setting_CustomCape);
		fireClientSettings.put("Hide Shields in Riptide", Setting_HideShieldsInRiptide);
		fireClientSettings.put("Debug Mode", Setting_DEBUG);

		file.addProperty("FIRECLIENT_CONFIG_VERSION", FIRECLIENT_VERSION);
		file.addProperty("FireClient Settings", gson.toJson(fireClientSettings));

		for(ModuleBase module : Modules.values())
		{
			module.SaveSettings(file);
		}
		
		return file;
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
