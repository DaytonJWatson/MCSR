package com.daytonjwatson.mcsr;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import com.daytonjwatson.mcsr.config.Config;

public class Utils {
	
	public static String color(String string) {
		return ChatColor.translateAlternateColorCodes('&', string);
	}
	
	public static Location spawnLocation() {
		World world = Bukkit.getWorld(Config.getString("spawn.world"));
		double x = Config.getDouble("spawn.x");
		double y = Config.getDouble("spawn.y");
		double z = Config.getDouble("spawn.z");
		float yaw = (float) Config.getDouble("spawn.yaw");
		float pitch = (float) Config.getDouble("spawn.pitch");
		
		return new Location(world, x, y, z, yaw, pitch);
	}

	public static void deleteWorldFolder(java.io.File folder) {
	    if (!folder.exists()) {
	        return;
	    }

	    java.io.File[] files = folder.listFiles();
	    if (files != null) {
	        for (java.io.File file : files) {
	            if (file.isDirectory()) {
	                deleteWorldFolder(file);
	            } else {
	                file.delete();
	            }
	        }
	    }
	    folder.delete();
	}
	
}
