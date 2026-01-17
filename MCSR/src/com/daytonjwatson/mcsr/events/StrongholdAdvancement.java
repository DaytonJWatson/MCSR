package com.daytonjwatson.mcsr.events;

import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

import com.daytonjwatson.mcsr.managers.RunAnnouncementManager;
import com.daytonjwatson.mcsr.managers.RunAnnouncementManager.Stage;

public class StrongholdAdvancement implements Listener {
	private static final NamespacedKey EYE_SPY = NamespacedKey.minecraft("story/follow_ender_eye");

	@EventHandler(ignoreCancelled = true)
	public void onAdvancement(PlayerAdvancementDoneEvent event) {
		if (event.getAdvancement() == null
				|| event.getAdvancement().getKey() == null
				|| !event.getAdvancement().getKey().equals(EYE_SPY)) {
			return;
		}

		RunAnnouncementManager.announceStage(event.getPlayer(), Stage.STRONGHOLD);
	}
}
