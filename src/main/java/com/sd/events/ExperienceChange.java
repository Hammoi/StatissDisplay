package com.sd.events;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.sd.StatissDisplay;
import com.ss.apireaders.Dungeons;
import com.ss.apireaders.Skills;
import com.ss.info.Util;

import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ExperienceChange {
	@SubscribeEvent
	public void onReceived(ClientChatReceivedEvent event) {
		System.out.println(event.message.getFormattedText());
		if(event.message.getFormattedText().contains(StatissDisplay.getActiveClass() + " Experience")) {
			System.out.println("received.");
			String msg = event.message.getFormattedText().replace(",", "");
			System.out.println(msg);
			double gainedXp = 0;
			try {
				gainedXp = Double.parseDouble((StringUtils.substringBetween(msg, "§8+§r§3", " " + StatissDisplay.getActiveClass() + " Experience")).replace(",", ""));
			} catch(NullPointerException e) {};
			System.out.println(gainedXp);

			StatissDisplay.setXp(Util.getLevel(Util.dungeonLevelReqs, StatissDisplay.getXp()[2] + gainedXp));

		}
	}
}
