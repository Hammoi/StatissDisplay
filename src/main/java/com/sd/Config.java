package com.sd;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentTranslation;

public class Config {
	private static JsonObject config;
	private static File configFile;
	private static FileReader fr;
	private static Boolean toggle;
	private static Integer x;
	private static Integer y;

	public static void setUpConfig(File cFile) {

		configFile = cFile;
		try {
			if(configFile.createNewFile()) {
				System.out.println("SD: Config doesn't exist, writing new config.");
			} else {
				System.out.println("SD: Config exists.");
			}

			fr = new FileReader(configFile);
		} catch (IOException e) {
			System.out.println("SD: Something went wrong while setting up config.");
		}

		StringBuilder c = new StringBuilder();

		Scanner s = new Scanner(fr);
		while (s.hasNextLine()) {
			c.append(s.nextLine());
		}
		s.close();

		if(c.length() != 0) {
			try {
				config = new Gson().fromJson(c.toString(), JsonObject.class);
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
		writeConfig("toggle", true);
		writeConfig("x", "5");
		writeConfig("y", "5");
	}


	public static boolean writeConfig(String key, String value) {
		if(config.has(key)) {
			config.remove(key);

		}
		config.addProperty(key, value);
		try {
			FileWriter fw = new FileWriter(configFile);
			fw.write(config.toString());
			fw.flush();
			fw.close();
			return true;
		} catch (IOException e) {
			System.out.println("SD: Something went wrong while writing to config.");
			return false;
		}

	}
	
	public static boolean writeConfig(String key, Object value) {
		return writeConfig(key, String.valueOf(value));
	}


	public JsonObject getConfig() {

		return config;
	}

	
	public static Boolean isToggled() {
		if(toggle == null) {
			try {
				toggle = config.get("toggle").getAsBoolean();
			} catch(NullPointerException e) {
				System.out.println("SD: No toggle mode. Setting to false.");
				toggle(false);
				return isToggled();
			
			} catch(IllegalArgumentException e) {
				System.out.println("SD: IAE.");
				return null;
			}

		}
		return toggle;
	}


	public static void toggle(boolean mode) {
		if(writeConfig("toggle", mode)) {
			if(mode) {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7StatissDisplay is toggled §aon§7."));
			} else {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7StatissDisplay is toggled §coff§7."));
			}
			toggle = mode;
		} else {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cSomething went wrong."));
		}
	}
	
	public static Integer x() {
		
		if(x == null) {
			try {
				x = config.get("x").getAsInt();
			} catch(NullPointerException e) {
				System.out.println("SD: No x. Setting to 5.");
				setX(5);
				return x();
			
			} catch(IllegalArgumentException e) {
				System.out.println("SD: IAE.");
				return null;
			}
		}
		
		return x;
	}
	
	public static void setX(int newX) {
		if(!writeConfig("x", newX)) Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cSomething went wrong."));
		x = newX;
	}
	
	public static Integer y() {
		if(y == null) {
			try {
				y = config.get("y").getAsInt();
			} catch(NullPointerException e) {
				System.out.println("SD: No y. Setting to 5.");
				setY(5);
				return y();
			
			} catch(IllegalArgumentException e) {
				System.out.println("SD: IAE.");
				return null;
			}
		}
		
		return y;
	}
	
	public static void setY(int newY) {
		if(!writeConfig("y", newY)) Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cSomething went wrong."));
	
		y = newY;
	}

}
