package com.sd.commands;

import java.text.DecimalFormat;
import java.util.UUID;

import com.sd.StatissDisplay;
import com.sd.events.Overlay;
import com.sd.Config;
import com.ss.apireaders.Dungeons;
import com.ss.info.PlayerAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentTranslation;

public class SdisplayThread implements Runnable{

	Thread sdThread;

	boolean console;
	String name;

	public SdisplayThread(boolean console) {
		this.console = console;
	}

	public SdisplayThread(boolean console, String name) {
		this.console = console;
		this.name = name;
	}

	private int getOrder(String pClass) {
		switch(pClass.toLowerCase()) {
		case "archer":
			return 0;
		case "berserk":
			return 1;
		case "mage":
			return 2;
		case "tank":
			return 3;
		case "healer":
			return 4;
		default:
			return -1;
		}
	}

	@Override
	public void run() {
		try {
			
			if(name != null) {
				UUID uuid = MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(name).getId();
				PlayerAPI.setPlayerUUID(uuid);
				Config.writeConfigValue("syncedUuid", uuid);
			} else {

				System.out.println(Minecraft.getMinecraft().thePlayer.getUniqueID());
				PlayerAPI.setPlayerUUID(Minecraft.getMinecraft().thePlayer.getUniqueID());
			}
		} catch(NullPointerException e) {
			if(name != null && !console) {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cInvalid name. Using config value."));
			}
			try {
				PlayerAPI.setPlayerUUID(UUID.fromString(Config.getConfigValue("syncedUuid").getAsString()));
			} catch(NullPointerException e1) {
				if(console) {
					System.out.println("SD: Cannot seem to find your stats. Try again manually using /sd sync.");
				} else {
					Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7Cannot seem to find your stats. Try again manually using /sd sync."));

				}
				return;
			}


		}

		if(console) {
			System.out.println("SD: Syncing stats.");
		} else Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7Syncing stats."));


		if(!PlayerAPI.startAPI()) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cHypixel API is being bad. Try again later."));

			return;
		}

		Dungeons d;
		try {
			d = new Dungeons();
		} catch(NullPointerException e) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cCannot seem to find your stats. Try again manually using /sd sync [name]."));
			return;
		}
		StatissDisplay.setActiveClass(d.getActiveClass());

		int x = getOrder(d.getActiveClass());

		StatissDisplay.setXp(d.getAllClasses()[x]);

		Overlay.update();

		if(console) {
			System.out.println("SD: Stats synced.");
		} else Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7Stats synced."));


		PlayerAPI.shutdownAPI();


	}

	public void start() {

		if(sdThread == null) {
			sdThread = new Thread(this);
			sdThread.start();
		} else {
			System.out.println("SD: Thread already started.");
		}

	}
}
