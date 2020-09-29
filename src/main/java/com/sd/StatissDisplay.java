package com.sd;

import com.sd.commands.SDisplay;
import com.sd.commands.SdisplayThread;
import com.sd.events.ExperienceChange;
import com.sd.events.Overlay;
import com.sd.Config;
import com.ss.Statiss;

import net.minecraft.init.Blocks;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = StatissDisplay.MODID, name = StatissDisplay.NAME, version = StatissDisplay.VERSION, dependencies = "required-after:statiss@[0.9.7,)")
public class StatissDisplay {
	
    public static final String MODID = "statissDisplay";
    public static final String NAME = "StatissDisplay";
    public static final String VERSION = "0.9.0";
    
    private static String activeClass;
    private static double[] xp;
    
    

	public static String getActiveClass() {
		return activeClass;
	}

	public static void setActiveClass(String activeClass) {
		StatissDisplay.activeClass = activeClass;
		if(xp != null) Overlay.update();
	}

	public static double[] getXp() {
		return xp;
	}

	public static void setXp(double[] xp) {
		StatissDisplay.xp = xp;
		if(activeClass != null) Overlay.update();
	}

	@EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	
    	Config.setUpConfig(event.getSuggestedConfigurationFile());
    	
    	MinecraftForge.EVENT_BUS.register(new Overlay());
    	MinecraftForge.EVENT_BUS.register(new ExperienceChange());
        ClientCommandHandler.instance.registerCommand(new SDisplay());
        

        
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event) {

    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	
		SdisplayThread sdThread = new SdisplayThread(true);
		
		sdThread.start();
    }
}
