package com.sd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

public class Config {
	private static JsonObject config;
	private static File configFile;

	private static String defaultConfig = "{\"toggle\":\"true\","
			+ "\"synceduuid\":\"\","
			+ "\"x\":\"5\","
			+ "\"y\":\"5\"}";
	
	public static void setUpConfig(File cFile) {

		configFile = cFile;
		try {
			if(configFile.createNewFile()) {
				System.out.println("SD: Config doesn't exist, writing new config.");
			} else {
				System.out.println("SD: Config exists.");
			}
		} catch (IOException e) {
			System.out.println("SD: Something went wrong while setting up config.");
		}
		//Check if config is valid
		String c = readFile(configFile);
		
		if(c.length() != 0) {
			try {
				config = new Gson().fromJson(c, JsonObject.class);
			} catch(JsonSyntaxException e) {
				config = new JsonObject();
				createConfig();
				System.out.println("SD: Corrupt config file. Writing new one.");
			}
		} else {
			config = new JsonObject();
			createConfig();
		}
	}

	public static void createConfig() {
		overwriteConfigFile(defaultConfig);

	}
	
	public static String readFile(File file) {
		FileReader fr;
		
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		
		StringBuilder c = new StringBuilder();

		Scanner s = new Scanner(fr);
		while (s.hasNextLine()) {
			c.append(s.nextLine());
		}
		s.close();
		
		System.out.println(c);
		
		return c.toString();
	}
	
	public static boolean overwriteConfigFile(String newConfig) {
		System.out.println(newConfig);
		try {
			FileWriter fw = new FileWriter(configFile);
			fw.write(newConfig);
			fw.flush();
			fw.close();
			config = new Gson().fromJson(newConfig, JsonObject.class);
			System.out.println(config);
			return true;
		} catch (IOException e) {
			System.out.println("SD: Something went wrong while writing to config.");
			return false;
		}
	}

	public static boolean writeConfigValue(String key, Object value) {
		if(config.has(key)) {
			config.remove(key);
		}
		
		config.addProperty(key, String.valueOf(value));
		return overwriteConfigFile(new Gson().toJson(config));

	}

	public static JsonElement getConfigValue(String key) {
		//System.out.println(config + ", " + key);
		JsonElement value = null;
		try {
			value = config.get(key);
		} catch(NullPointerException e) {
			
		}
		
		return value;
	}

}
