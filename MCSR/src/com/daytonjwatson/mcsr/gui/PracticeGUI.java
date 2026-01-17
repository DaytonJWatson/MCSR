package com.daytonjwatson.mcsr.gui;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.daytonjwatson.mcsr.MCSR;
import com.daytonjwatson.mcsr.practice.PracticeType;

public class PracticeGUI {
	public static final String TITLE = ChatColor.DARK_GRAY + "Practice Worlds";
	private static final int SIZE = 27;
	private static final NamespacedKey TYPE_KEY = new NamespacedKey(MCSR.instance, "practice_type");

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

		inventory.setItem(10, buildPracticeItem(PracticeType.BLAZE, Material.BLAZE_ROD));
		inventory.setItem(12, buildPracticeItem(PracticeType.CRAFTING, Material.CRAFTING_TABLE));
		inventory.setItem(14, buildPracticeItem(PracticeType.OVERWORLD, Material.GRASS_BLOCK));
		inventory.setItem(16, buildPracticeItem(PracticeType.ZERO, Material.END_STONE));

		return inventory;
	}

	public static PracticeType getType(ItemStack item) {
		if (item == null || !item.hasItemMeta()) {
			return null;
		}
		ItemMeta meta = item.getItemMeta();
		if (meta == null) {
			return null;
		}
		PersistentDataContainer container = meta.getPersistentDataContainer();
		String typeValue = container.get(TYPE_KEY, PersistentDataType.STRING);
		if (typeValue == null) {
			return null;
		}
		try {
			return PracticeType.valueOf(typeValue);
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}

	private static ItemStack buildPracticeItem(PracticeType type, Material material) {
		ItemStack item = new ItemStack(material);
		ItemMeta meta = item.getItemMeta();
		if (meta != null) {
			meta.setDisplayName(ChatColor.GOLD + type.getDisplayName());
			meta.setLore(java.util.Collections.singletonList(ChatColor.GRAY + "Click to load this practice world."));
			meta.getPersistentDataContainer().set(TYPE_KEY, PersistentDataType.STRING, type.name());
			item.setItemMeta(meta);
		}
		return item;
	}
}
