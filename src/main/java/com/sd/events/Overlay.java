package com.sd.events;

import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.colorchooser.ColorSelectionModel;

import org.fusesource.jansi.Ansi.Color;
import org.lwjgl.opengl.GL11;

import com.sd.Config;
import com.sd.StatissDisplay;
import com.ss.apireaders.Dungeons;
import com.ss.info.Util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;

public class Overlay {

	public static String[] text = new String[2];

	public static void update() {
		
		
		DecimalFormat nf = new DecimalFormat("#.#");
		nf.setGroupingUsed(true);
		nf.setGroupingSize(3);

		StringBuilder progressBar = new StringBuilder("§2--------------------");
		progressBar.insert(2 + (int) Math.ceil((20 * StatissDisplay.getXp()[1]) / Util.dungeonLevelReqs[(int) StatissDisplay.getXp()[0]]), "§f");

		
		text = new String[] {"§7Progress to §a" + StatissDisplay.getActiveClass() + " §7Level " + (int) Math.ceil(StatissDisplay.getXp()[0]) + ": §e" + new DecimalFormat("#.##").format(100 * (StatissDisplay.getXp()[1] / Util.dungeonLevelReqs[(int) StatissDisplay.getXp()[0]])) + "%", 
				progressBar + " §e" + nf.format(StatissDisplay.getXp()[1]) + "§6/§e" + nf.format(Util.dungeonLevelReqs[(int) StatissDisplay.getXp()[0]])};

	}
	
	public static int x = Config.getConfigValue("x").getAsInt();
	public static int y = Config.getConfigValue("y").getAsInt();
	
	@SubscribeEvent
	public void onRender(RenderGameOverlayEvent event) {
		if (event.type == ElementType.TEXT && Config.getConfigValue("toggle").getAsBoolean()) {
			
			
			
			int y1 = y;
			for(String t : text) {
				Minecraft.getMinecraft().fontRendererObj.drawString(t, x, y1, 0);
				y1 += 10;
			}
			

			
		}
	}
}
