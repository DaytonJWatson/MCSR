package com.daytonjwatson.mcsr.managers;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class InventorySnapshot {
	private final ItemStack[] contents;
	private final ItemStack[] armorContents;
	private final ItemStack[] extraContents;

	public InventorySnapshot(ItemStack[] contents, ItemStack[] armorContents, ItemStack[] extraContents) {
		this.contents = cloneItems(contents);
		this.armorContents = cloneItems(armorContents);
		this.extraContents = cloneItems(extraContents);
	}

	public static InventorySnapshot fromPlayer(Player player) {
		PlayerInventory inventory = player.getInventory();
		return new InventorySnapshot(inventory.getContents(), inventory.getArmorContents(), inventory.getExtraContents());
	}

	public void restore(Player player) {
		PlayerInventory inventory = player.getInventory();
		inventory.setContents(cloneItems(contents));
		inventory.setArmorContents(cloneItems(armorContents));
		inventory.setExtraContents(cloneItems(extraContents));
		player.updateInventory();
	}

	public static void clearPlayerInventory(Player player) {
		PlayerInventory inventory = player.getInventory();
		inventory.clear();
		inventory.setArmorContents(new ItemStack[inventory.getArmorContents().length]);
		inventory.setExtraContents(new ItemStack[inventory.getExtraContents().length]);
		player.updateInventory();
	}

	private static ItemStack[] cloneItems(ItemStack[] items) {
		if (items == null) {
			return new ItemStack[0];
		}
		ItemStack[] clone = new ItemStack[items.length];
		for (int i = 0; i < items.length; i++) {
			ItemStack item = items[i];
			clone[i] = item == null ? null : item.clone();
		}
		return clone;
	}
}
