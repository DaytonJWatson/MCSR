package com.daytonjwatson.mcsr.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.managers.RunManager;

public class ForfeitCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use /forfeit.");
			return true;
		}

		Player player = (Player) sender;

		if (!RunManager.isInRun(player.getUniqueId())) {
			player.sendMessage(Utils.color("&eYou are not currently in a run."));
			return true;
		}

		RunManager.forfeitRun(player, "&cYou forfeited your run.");
		return true;
	}
}
