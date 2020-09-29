package com.sd.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sd.StatissDisplay;
import com.ss.apireaders.Dungeons;
import com.ss.apireaders.Skills;
import com.ss.info.Util;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExperienceChange {
	@SubscribeEvent
	public void onReceived(ClientChatReceivedEvent event) {
		System.out.println(event.message.getFormattedText() + ", " + StatissDisplay.getActiveClass() + " Experience");
		if(event.message.getFormattedText().contains(StatissDisplay.getActiveClass() + " Experience")) {
	        Pattern p = Pattern.compile("\\d+");
	        Matcher m = p.matcher(event.message.getFormattedText());
	        System.out.println("received.");
	        while(m.find()) {
	            System.out.println(m.group());
	            StatissDisplay.setXp(Util.getLevel(Util.dungeonLevelReqs, StatissDisplay.getXp()[2] + Double.valueOf(m.group())));
	            break;
	        }
		}
	}
}
