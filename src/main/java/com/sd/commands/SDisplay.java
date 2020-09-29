package com.sd.commands;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;

import com.sd.StatissDisplay;
import com.sd.events.Overlay;
import com.sd.Config;
import com.ss.apireaders.Dungeons;
import com.ss.apireaders.Profiles;
import com.ss.info.PlayerAPI;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;

public class SDisplay implements ICommand{

	@Override
	public int compareTo(ICommand o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		// TODO Auto-generated method stub
		return "ssdisplay";
	}

	@Override
	public String getCommandUsage(ICommandSender sender) {
		// TODO Auto-generated method stub
		return "StatissDisplay main command.";
	}

	@Override
	public List<String> getCommandAliases() {
		// TODO Auto-generated method stub
		return Arrays.asList(new String[] {"ssd", "sd"});
	}




	@Override
	public void processCommand(ICommandSender sender, String[] args) throws CommandException {

		if(args.length == 0) {
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7/sd move [x] [y] - Move the info overlay."));
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7/sd sync [name] - Sync your stats. Require Statiss. ([name] is optional)"));
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7/sd toggle - Turn the mod on or off."));
			return;
		}

		switch(args[0]) {
		case "toggle":
			if(Config.isToggled()) {
				Config.toggle(false);
			} else {
				Config.toggle(true);
			}
			break;
		case "sync":
			try {
				SdisplayThread sdThread;
				if(args.length > 1) {
					sdThread = new SdisplayThread(false, MinecraftServer.getServer().getPlayerProfileCache().getGameProfileForUsername(args[1]).getId());
				} else {
					sdThread = new SdisplayThread(false);
				}
				
				sdThread.start();

			} catch(NullPointerException e)  {
				e.printStackTrace();
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cNo valid API key! Set one using /ss setApiKey [key]"));
			}
			break;
		case "move":
			if(args.length < 3) {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7/sd move [x] [y] - Move the info overlay. Your current location is at (" + Overlay.x + ", " + Overlay.y + "). Your Minecraft's dimension is " + Minecraft.getMinecraft().displayWidth + " x " + Minecraft.getMinecraft().displayHeight));
				break;
			} 
			if(!StringUtils.isNumeric(args[1]) || !StringUtils.isNumeric(args[2]) || Integer.valueOf(args[1]) > Minecraft.getMinecraft().displayWidth || Integer.valueOf(args[2]) > Minecraft.getMinecraft().displayHeight) {
				Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§cInvalid values!"));
				break;
			}
			Config.setX(Integer.valueOf(args[1]));
			Config.setY(Integer.valueOf(args[2]));
			Overlay.x = Integer.valueOf(args[1]);
			Overlay.y = Integer.valueOf(args[2]);
			break;
		default:

			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7/sd move [x] [y] - Move the info overlay."));
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7/sd sync - Sync your stats. Require Statiss."));
			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentTranslation("§7/sd toggle - Turn the mod on or off."));

		}
	}

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] args, int index) {
		// TODO Auto-generated method stub
		return false;
	}

}
