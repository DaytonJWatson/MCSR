package com.daytonjwatson.mcsr.commands;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.managers.RunAnnouncementManager;
import com.daytonjwatson.mcsr.managers.RunManager;
import com.daytonjwatson.mcsr.managers.TimerManager;

public class StartRunCommand implements CommandExecutor {

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

		String name = player.getName().toLowerCase() + "_speedrun";

		if (Bukkit.getWorld(name) != null) {
			player.sendMessage(Utils.color("&eTaking you to existing world..."));
			player.teleport(Bukkit.getWorld(name).getSpawnLocation());
			if (!RunManager.isInRun(player.getUniqueId())) {
				healForRun(player);
				RunManager.startRun(player, name);
				RunAnnouncementManager.reset(player.getUniqueId());
				TimerManager.startStopwatch(player);
			}
			return true;
		}

		player.sendMessage(Utils.color("&7Preparing world..."));

		final long startNanos = System.nanoTime();

		Bukkit.createWorld(new WorldCreator(name).environment(World.Environment.NORMAL));
		Bukkit.createWorld(new WorldCreator(name+"_nether").environment(World.Environment.NETHER));
		Bukkit.createWorld(new WorldCreator(name+"_the_end").environment(World.Environment.THE_END));

		double seconds = (System.nanoTime() - startNanos) / 1_000_000_000.0;
		player.sendMessage(String.format("§aWorld loaded successfully in §e%.2f §aseconds.", seconds));
		player.teleport(Bukkit.getWorld(name).getSpawnLocation());
		healForRun(player);
		RunManager.startRun(player, name);
		RunAnnouncementManager.reset(player.getUniqueId());
		TimerManager.startStopwatch(player);
		return true;
	}

	private void healForRun(Player player) {
		player.setHealth(player.getMaxHealth());
		player.setFoodLevel(20);
		player.setSaturation(20.0f);
		player.setFireTicks(0);
	}
}
