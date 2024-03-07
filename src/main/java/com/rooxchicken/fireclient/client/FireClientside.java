package com.rooxchicken.fireclient.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;

import com.rooxchicken.fireclient.FireClient;
import com.rooxchicken.fireclient.event.KeyInputHandler;
import com.rooxchicken.fireclient.modules.ModuleBase;
import com.rooxchicken.fireclient.screen.FireClientMainScreen;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.util.Identifier;

public class FireClientside implements ClientModInitializer
{	
	@Override
	public void onInitializeClient()
	{
		KeyInputHandler.register();
		ClientTickEvents.END_CLIENT_TICK.register(client ->
		{
			for(ModuleBase module : FireClient.Modules.values())
			{
				module.Update();
			}
		});

		ClientLifecycleEvents.CLIENT_STARTED.register(client ->
		{
			try
			{
				ArrayList<String> playerData = GetPasteData.GetPasteDataRaw("https://pastebin.com/raw/kteanMKr");

				String username = client.getSession().getUsername();
				ArrayList<String> players = new ArrayList<String>();
				HashMap<String, String> capes = new HashMap<String, String>();

				for(String player : playerData.get(0).split("=")[1].split(","))
				{
					players.add(player);
				}

				if(players.contains(username))
				{
					FireClient.FIRECLIENT_WHITELISTED = true;
					FireClient.LOGGER.info("You can access whitelisted modules! Username: " + username);
				}


				for(String cape : playerData.get(1).split("=")[1].split(","))
				{
					String[] data = cape.split("-");
					capes.put(data[0], data[1]);
				}

				FireClient.FIRECLIENT_CUSTOMCAPES = capes;
			}
			catch(Exception e)
			{
				FireClient.LOGGER.error("Failed to player data! " + e.getMessage());
			}
			
			for(ModuleBase module : FireClient.Modules.values())
			{
				module.PostInitialization();
			}

		});

		WorldRenderEvents.START.register(client ->
		{
			for(ModuleBase module : FireClient.Modules.values())
			{
				module.convertPosToScreen();
			}
		});
		
		for(ModuleBase module : FireClient.Modules.values())
		{
			module.ClientInitialization();
		}

		HudRenderCallback.EVENT.register((context, delta) ->
		{
			//FireClient.LOGGER.info("hi");
			MinecraftClient client = MinecraftClient.getInstance();
			if(FireClient.Setting_Branding)
			{
				if(client.currentScreen != null && client.currentScreen.getClass() != FireClientMainScreen.class && client.currentScreen.getClass() != ChatScreen.class)
				context.drawText(client.textRenderer, "alpha_FireClient V" + FireClient.FIRECLIENT_VERSION, 2, client.getWindow().getScaledHeight()-10, 0xFFFFFF, false);
			}
		});

		
	}
	
	public void sendChatMessage(String msg)
	{
		MinecraftClient client = MinecraftClient.getInstance();
    	ClientPlayNetworkHandler handler = client.getNetworkHandler();
    	handler.sendChatMessage(msg.substring(0, msg.length() - 22));
	}
}