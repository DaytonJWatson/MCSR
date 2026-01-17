package com.daytonjwatson.mcsr.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.gui.PracticeGUI;

public class PracticeCommand implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use /practice.");
			return true;
		}

		Player player = (Player) sender;
		player.openInventory(PracticeGUI.createInventory());
		return true;
	}
}
