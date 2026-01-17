package com.daytonjwatson.mcsr.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.config.Config;

public class SpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use /spawn.");
            return true;
        }

        Player player = (Player) sender;

        String worldName = Config.getString("spawn.world");
        World world = Bukkit.getWorld(worldName);
        if (world == null) {
            player.sendMessage(Utils.color("&cSpawn world not found: &f" + worldName));
            return true;
        }

        double x = Config.getDouble("spawn.x");
        double y = Config.getDouble("spawn.y");
        double z = Config.getDouble("spawn.z");
        float yaw = (float) Config.getDouble("spawn.yaw");
        float pitch = (float) Config.getDouble("spawn.pitch");

        Location spawn = new Location(world, x, y, z, yaw, pitch);
        player.teleport(spawn);
        player.sendMessage(Utils.color("&aTeleported to spawn."));
        return true;
    }
}