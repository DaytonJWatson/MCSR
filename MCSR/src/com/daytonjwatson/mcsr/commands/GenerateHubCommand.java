package com.daytonjwatson.mcsr.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.world.VoidWorldGenerator;

public class GenerateHubCommand implements CommandExecutor {

	private static final String HUB_WORLD_NAME = "hub";

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("Only players can use /generatehub.");
			return true;
		}

		Player player = (Player) sender;

		World world = Bukkit.getWorld(HUB_WORLD_NAME);
		if (world == null) {
			player.sendMessage(Utils.color("&7Generating hub world..."));
			WorldCreator creator = new WorldCreator(HUB_WORLD_NAME);
			creator.generator(new VoidWorldGenerator());
			world = Bukkit.createWorld(creator);
		}

		if (world == null) {
			player.sendMessage(Utils.color("&cFailed to generate hub world."));
			return true;
		}

		world.getBlockAt(0, 64, 0).setType(Material.GRASS_BLOCK);
		player.sendMessage(Utils.color("&aHub world ready with a grass block at 0, 64, 0."));
		player.teleport(new Location(world, 0.5, 65, 0.5));
		return true;
	}
}
