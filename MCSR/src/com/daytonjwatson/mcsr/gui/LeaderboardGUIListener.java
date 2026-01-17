package com.daytonjwatson.mcsr.gui;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class LeaderboardGUIListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if (isLeaderboardView(event.getView().getTitle())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryDrag(InventoryDragEvent event) {
		if (isLeaderboardView(event.getView().getTitle())) {
			event.setCancelled(true);
		}
	}

	private boolean isLeaderboardView(String title) {
		if (title == null) {
			return false;
		}
		return ChatColor.stripColor(title).equals(ChatColor.stripColor(LeaderboardGUI.TITLE));
	}
}
