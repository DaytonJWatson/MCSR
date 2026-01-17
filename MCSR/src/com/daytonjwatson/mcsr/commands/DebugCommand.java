package com.daytonjwatson.mcsr.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.Utils;

public class DebugCommand implements CommandExecutor {
	
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
        
        
        return true;
    }
}
