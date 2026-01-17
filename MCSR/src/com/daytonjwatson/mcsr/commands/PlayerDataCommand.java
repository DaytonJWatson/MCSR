package com.daytonjwatson.mcsr.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.gui.PlayerDataGUI;
import com.daytonjwatson.mcsr.managers.PlayerDataManager;

public class PlayerDataCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length == 0) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Only players can open the data GUI.");
				return true;
			}
			Player player = (Player) sender;
			openGui(player, player);
			return true;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("chat")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("Console must specify a player name.");
				return true;
			}
			Player player = (Player) sender;
			sendChatData(sender, player);
			return true;
		}

		OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
		if (args.length >= 2 && args[1].equalsIgnoreCase("chat")) {
			sendChatData(sender, target);
			return true;
		}

		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can open the data GUI.");
			return true;
		}

		Player viewer = (Player) sender;
		openGui(viewer, target);
		return true;
	}

	private void openGui(Player viewer, OfflinePlayer target) {
		viewer.openInventory(PlayerDataGUI.createInventory(target));
	}

	private void sendChatData(CommandSender sender, OfflinePlayer target) {
		String name = target.getName() == null ? "Unknown" : target.getName();
		String header = Utils.color("&6Player Data &7- &e" + name);
		sender.sendMessage(header);

		long best = PlayerDataManager.getPersonalBest(target.getUniqueId());
		sender.sendMessage(Utils.color("&7Personal Best: &a" + PlayerDataManager.formatTime(best)));

		List<Long> recent = PlayerDataManager.getRecentTimes(target.getUniqueId());
		if (recent.isEmpty()) {
			sender.sendMessage(Utils.color("&7Last 10 Runs: &8No runs recorded."));
			return;
		}

		sender.sendMessage(Utils.color("&7Last 10 Runs:"));
		int index = 1;
		for (Long time : recent) {
			sender.sendMessage(ChatColor.GRAY + "#" + index + ": " + ChatColor.GREEN + PlayerDataManager.formatTime(time));
			index++;
		}
	}
}
