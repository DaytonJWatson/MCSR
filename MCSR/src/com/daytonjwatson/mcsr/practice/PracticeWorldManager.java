package com.daytonjwatson.mcsr.practice;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

import com.daytonjwatson.mcsr.MCSR;
import com.daytonjwatson.mcsr.Utils;
import com.daytonjwatson.mcsr.managers.RunManager;

public class PracticeWorldManager {
	public static final String PRACTICE_FOLDER = "practiceworlds";

	public static boolean startPractice(Player player, PracticeType type) {
		if (player == null || type == null) {
			return false;
		}

		ensurePracticeFolder();
		if (RunManager.isInRun(player.getUniqueId())) {
			RunManager.forfeitRun(player, "&eEnding your current run to start practice.");
		}

		String baseWorldName = player.getName().toLowerCase() + "_" + type.getFolderName();
		File worldContainer = Bukkit.getWorldContainer();
		File templateFolder = new File(new File(MCSR.instance.getDataFolder(), PRACTICE_FOLDER), type.getFolderName());
		File targetFolder = new File(worldContainer, baseWorldName);
		if (!templateFolder.exists()) {
			player.sendMessage(Utils.color("&cPractice world template missing: &e" + type.getFolderName()));
			return false;
		}

		if (targetFolder.exists()) {
			Utils.deleteWorldFolder(targetFolder);
		}

		try {
			Utils.copyWorldFolder(templateFolder, targetFolder);
		} catch (IOException ex) {
			player.sendMessage(Utils.color("&cFailed to copy practice world."));
			ex.printStackTrace();
			return false;
		}

		Bukkit.createWorld(new WorldCreator(baseWorldName).environment(World.Environment.NORMAL));
		World world = Bukkit.getWorld(baseWorldName);
		if (world == null) {
			player.sendMessage(Utils.color("&cUnable to load practice world."));
			return false;
		}

		RunManager.startRun(player, baseWorldName);
		player.teleport(world.getSpawnLocation());
		player.sendMessage(Utils.color("&aLoaded " + type.getDisplayName() + "."));
		return true;
	}

	public static void ensurePracticeFolder() {
		File dataFolder = MCSR.instance.getDataFolder();
		if (!dataFolder.exists()) {
			dataFolder.mkdirs();
		}
		File practiceFolder = new File(dataFolder, PRACTICE_FOLDER);
		if (!practiceFolder.exists()) {
			practiceFolder.mkdirs();
		}
	}
}
