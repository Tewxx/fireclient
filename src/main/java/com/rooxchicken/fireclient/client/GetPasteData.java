package com.rooxchicken.fireclient.client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.rooxchicken.fireclient.FireClient;

public class GetPasteData
{
    public static ArrayList<String> GetPasteDataRaw(String url)
    {
        try
        {
            ArrayList<String> result = new ArrayList<String>();
            URL pasteURL = new URL("https://pastebin.com/raw/kteanMKr");
				URLConnection http = pasteURL.openConnection();
				BufferedReader in = new BufferedReader(
										new InputStreamReader(
											http.getInputStream()));

            String line = "";
            while ((line = in.readLine()) != null) 
                result.add(line);

            in.close();

            return result;
        }
        catch(Exception e)
        {
            FireClient.LOGGER.error("Failed to get paste data from: " + url + ". " + e.getMessage());
            return new ArrayList<String>();
        }
    }
}
