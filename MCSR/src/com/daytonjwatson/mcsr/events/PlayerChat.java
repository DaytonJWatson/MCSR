package com.daytonjwatson.mcsr.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.daytonjwatson.mcsr.Utils;

public class PlayerChat implements Listener {
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent event) {
		event.setFormat(Utils.color("&7" + event.getPlayer().getName() + " &8> &f" + event.getMessage()));
	}
}
