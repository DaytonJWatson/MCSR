package com.daytonjwatson.mcsr.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.daytonjwatson.mcsr.managers.PlayerDataManager;

public class PlayerDataGUI {
	public static final String TITLE_PREFIX = ChatColor.DARK_GRAY + "Player Data: ";

	public static Inventory createInventory(OfflinePlayer target) {
		String name = target.getName() == null ? "Unknown" : target.getName();
		Inventory inventory = Bukkit.createInventory(null, 27, TITLE_PREFIX + name);

		ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta fillerMeta = filler.getItemMeta();
		if (fillerMeta != null) {
			fillerMeta.setDisplayName(" ");
			filler.setItemMeta(fillerMeta);
		}
		for (int i = 0; i < inventory.getSize(); i++) {
			inventory.setItem(i, filler);
		}

		inventory.setItem(11, buildPlayerHead(target, name));
		inventory.setItem(13, buildPersonalBest(target.getUniqueId()));
		inventory.setItem(15, buildRecentRuns(target.getUniqueId()));

		return inventory;
	}

	private static ItemStack buildPlayerHead(OfflinePlayer target, String name) {
		ItemStack head = new ItemStack(Material.PLAYER_HEAD);
		ItemMeta meta = head.getItemMeta();
		if (meta instanceof SkullMeta) {
			SkullMeta skullMeta = (SkullMeta) meta;
			skullMeta.setOwningPlayer(target);
			skullMeta.setDisplayName(ChatColor.GOLD + name);
			List<String> lore = new ArrayList<>();
			lore.add(ChatColor.GRAY + "Speedrun profile");
			skullMeta.setLore(lore);
			head.setItemMeta(skullMeta);
		}
		return head;
	}

	private static ItemStack buildPersonalBest(UUID uuid) {
		ItemStack clock = new ItemStack(Material.CLOCK);
		ItemMeta meta = clock.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(ChatColor.AQUA + "Personal Best");
			List<String> lore = new ArrayList<>();
			long best = PlayerDataManager.getPersonalBest(uuid);
			lore.add(ChatColor.GRAY + "Best Time:");
			lore.add(ChatColor.GREEN + PlayerDataManager.formatTime(best));
			meta.setLore(lore);
			clock.setItemMeta(meta);
		}
		return clock;
	}

	private static ItemStack buildRecentRuns(UUID uuid) {
		ItemStack book = new ItemStack(Material.BOOK);
		ItemMeta meta = book.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(ChatColor.YELLOW + "Last 10 Runs");
			List<String> lore = new ArrayList<>();
			List<Long> recent = PlayerDataManager.getRecentTimes(uuid);
			if (recent.isEmpty()) {
				lore.add(ChatColor.GRAY + "No runs recorded yet.");
			} else {
				int index = 1;
				for (Long time : recent) {
					lore.add(ChatColor.GRAY + "#" + index + ": " + ChatColor.GREEN + PlayerDataManager.formatTime(time));
					index++;
				}
			}
			meta.setLore(lore);
			book.setItemMeta(meta);
		}
		return book;
	}
}
