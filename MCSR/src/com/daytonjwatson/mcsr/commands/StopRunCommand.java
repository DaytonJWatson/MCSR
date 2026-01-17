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
		Utils.deleteWorldFolder(worldFolder);
		TimerManager.stopStopwatch(player);
		player.sendMessage("Speed run deleted");
		
		return false;
	}
}
