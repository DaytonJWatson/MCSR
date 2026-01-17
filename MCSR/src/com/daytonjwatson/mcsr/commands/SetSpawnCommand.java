package com.daytonjwatson.mcsr.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.config.Config;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use /setspawn.");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("mcsr.setspawn")) {
            player.sendMessage(Utils.color("&cYou do not have permission."));
            return true;
        }

        Location loc = player.getLocation();

        Config.setString("spawn.world", loc.getWorld().getName());
        Config.setDouble("spawn.x", loc.getX());
        Config.setDouble("spawn.y", loc.getY());
        Config.setDouble("spawn.z", loc.getZ());
        Config.setDouble("spawn.yaw", loc.getYaw());
        Config.setDouble("spawn.pitch", loc.getPitch());

        player.sendMessage(Utils.color("&aSpawn set to your current location."));
        return true;
    }
}