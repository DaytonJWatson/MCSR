package com.daytonjwatson.mcsr.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.managers.RunManager;

public class StopRunCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use /stoprun.");
			return true;
		}

		Player player = (Player) sender;

		if (!player.hasPermission("mcsr.debug")) {
			player.sendMessage(Utils.color("&cYou do not have permission."));
			return true;
		}
		
		RunManager.forfeitRun(player, "&cRun stopped and worlds deleted.");
		
		return false;
	}
}
