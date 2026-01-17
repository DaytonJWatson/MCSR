package com.daytonjwatson.mcsr.gui;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.daytonjwatson.mcsr.managers.LeaderboardEntry;
import com.daytonjwatson.mcsr.managers.PlayerDataManager;

public class LeaderboardGUI {
	public static final String TITLE = ChatColor.DARK_GRAY + "Server Leaderboard";
	private static final int SIZE = 36;

	public static Inventory createInventory() {
		Inventory inventory = Bukkit.createInventory(null, SIZE, TITLE);
		ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta fillerMeta = filler.getItemMeta();
		if (fillerMeta != null) {
			fillerMeta.setDisplayName(" ");
			filler.setItemMeta(fillerMeta);
		}
		for (int i = 0; i < inventory.getSize(); i++) {
			inventory.setItem(i, filler);
		}

		List<LeaderboardEntry> entries = PlayerDataManager.getTopRuns(10);
		if (entries.isEmpty()) {
			inventory.setItem(13, emptyState());
			return inventory;
		}

		int slot = 10;
		for (int i = 0; i < entries.size(); i++) {
			LeaderboardEntry entry = entries.get(i);
			inventory.setItem(slot, buildEntry(entry, i + 1));
			slot = nextSlot(slot);
			if (slot == -1) {
				break;
			}
		}
		return inventory;
	}

	private static int nextSlot(int current) {
		int next = current + 1;
		if (next == 17) {
			return 19;
		}
		if (next >= 26) {
			return -1;
		}
		return next;
	}

	private static ItemStack emptyState() {
		ItemStack paper = new ItemStack(Material.PAPER);
		ItemMeta meta = paper.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(ChatColor.YELLOW + "No runs yet");
			meta.setLore(java.util.Collections.singletonList(ChatColor.GRAY + "Complete a run to appear here."));
			paper.setItemMeta(meta);
		}
		return paper;
	}

	private static ItemStack buildEntry(LeaderboardEntry entry, int rank) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(entry.getUuid());
		String name = player.getName() == null ? "Unknown" : player.getName();
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = head.getItemMeta();
		if (meta instanceof SkullMeta) {
			SkullMeta skullMeta = (SkullMeta) meta;
			skullMeta.setOwningPlayer(player);
			skullMeta.setDisplayName(ChatColor.GOLD + "#" + rank + " " + name);
			skullMeta.setLore(java.util.Arrays.asList(
				ChatColor.GRAY + "Best Time:",
				ChatColor.GREEN + PlayerDataManager.formatTime(entry.getTimeMs())));
			head.setItemMeta(skullMeta);
		}
		return head;
	}
}
