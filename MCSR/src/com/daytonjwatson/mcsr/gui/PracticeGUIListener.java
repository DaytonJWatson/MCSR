package com.daytonjwatson.mcsr.gui;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;

import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.practice.PracticeType;
import com.daytonjwatson.mcsr.practice.PracticeWorldManager;

public class PracticeGUIListener implements Listener {
	@EventHandler(ignoreCancelled = true)
	public void onInventoryClick(InventoryClickEvent event) {
		if (!isPracticeView(event.getView().getTitle())) {
			return;
		}
		event.setCancelled(true);
		if (!(event.getWhoClicked() instanceof Player)) {
			return;
		}
		PracticeType type = PracticeGUI.getType(event.getCurrentItem());
		if (type == null) {
			return;
		}
		Player player = (Player) event.getWhoClicked();
		if (type != PracticeType.OVERWORLD) {
			player.sendMessage(Utils.color("&eThat practice world is under development."));
			return;
		}
		player.closeInventory();
		PracticeWorldManager.startPractice(player, type);
	}

	@EventHandler(ignoreCancelled = true)
	public void onInventoryDrag(InventoryDragEvent event) {
		if (isPracticeView(event.getView().getTitle())) {
			event.setCancelled(true);
		}
	}

	private boolean isPracticeView(String title) {
		if (title == null) {
			return false;
		}
		return ChatColor.stripColor(title).equals(ChatColor.stripColor(PracticeGUI.TITLE));
	}
}
