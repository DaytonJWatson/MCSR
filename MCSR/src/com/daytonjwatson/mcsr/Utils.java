package com.daytonjwatson.mcsr;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

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

	public static void copyWorldFolder(File source, File target) throws IOException {
		if (!source.exists()) {
			return;
		}
		if (source.isDirectory()) {
			if (!target.exists()) {
				target.mkdirs();
			}
			File[] files = source.listFiles();
			if (files != null) {
				for (File file : files) {
					if (shouldSkipWorldFile(file)) {
						continue;
					}
					copyWorldFolder(file, new File(target, file.getName()));
				}
			}
		} else {
			Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
		}
	}

	private static boolean shouldSkipWorldFile(File file) {
		if (file == null || file.isDirectory()) {
			return false;
		}
		String name = file.getName();
		return "uid.dat".equalsIgnoreCase(name) || "session.lock".equalsIgnoreCase(name);
	}
	
}
