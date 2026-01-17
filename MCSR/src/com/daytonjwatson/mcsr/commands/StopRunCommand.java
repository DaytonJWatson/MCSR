package com.daytonjwatson.mcsr.commands;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.managers.TimerManager;

public class StopRunCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use /debug.");
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("mcsr.debug")) {
			player.sendMessage(Utils.color("&cYou do not have permission."));
			return true;
		}
		
		player.teleport(Utils.spawnLocation());
		
		String worldName = player.getName() + "_speedrun";
		
		boolean unloaded = Bukkit.unloadWorld(worldName, false);
		if (!unloaded) {
		    // If this fails, something still holds a reference
		    return true;
		}
		
		File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
		deleteWorldFolder(worldFolder);
		TimerManager.stopStopwatch(player);
		player.sendMessage("Speed run deleted");
		
		return false;
	}

	private void deleteWorldFolder(File folder) {
	    if (!folder.exists()) return;

	    File[] files = folder.listFiles();
	    if (files != null) {
	        for (File file : files) {
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
