package com.daytonjwatson.mcsr.events;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.daytonjwatson.mcsr.MCSR;
import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.config.Config;
import com.daytonjwatson.mcsr.managers.TimerManager;

public class PlayerJoin implements Listener {

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer() == null)
			return;

		Player player = event.getPlayer();

		if (player.hasPlayedBefore())
			returningPlayer(player, event);
		else
			newPlayer(player, event);

		UUID uuid = player.getUniqueId();
		if (TimerManager.autoResume.contains(uuid)) {
			Bukkit.getScheduler().runTaskLater(MCSR.instance, () -> TimerManager.startStopwatch(player), 1L);
		}
	}

	private void newPlayer(Player player, PlayerJoinEvent event) {
		event.setJoinMessage(Utils.color("&6Welcome to MCSR " + "&7" + player.getName() + "&6!"));
		player.teleport(Utils.spawnLocation());
	}

	private void returningPlayer(Player player, PlayerJoinEvent event) {
		if (Config.getBoolean("spawn-settings.teleport-on-join"))
			player.teleport(Utils.spawnLocation());

		event.setJoinMessage(Utils.color("&8[&a+&8] &7" + player.getName()));
	}
}
