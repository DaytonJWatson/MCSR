package com.daytonjwatson.mcsr.gui;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

public class PlayerDataGUIListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if (isPlayerDataView(event.getView().getTitle())) {
			event.setCancelled(true);
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryDrag(InventoryDragEvent event) {
		if (isPlayerDataView(event.getView().getTitle())) {
			event.setCancelled(true);
		}
	}

	private boolean isPlayerDataView(String title) {
		if (title == null) {
			return false;
		}
		return ChatColor.stripColor(title).startsWith(ChatColor.stripColor(PlayerDataGUI.TITLE_PREFIX));
	}
}
