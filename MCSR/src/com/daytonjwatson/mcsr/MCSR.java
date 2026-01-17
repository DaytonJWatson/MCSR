package com.daytonjwatson.mcsr;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.daytonjwatson.mcsr.commands.DebugCommand;
import com.daytonjwatson.mcsr.commands.ForfeitCommand;
import com.daytonjwatson.mcsr.commands.GenerateHubCommand;
import com.daytonjwatson.mcsr.commands.LeaderboardCommand;
import com.daytonjwatson.mcsr.commands.PlayerDataCommand;
import com.daytonjwatson.mcsr.commands.PracticeCommand;
import com.daytonjwatson.mcsr.commands.SetSpawnCommand;
import com.daytonjwatson.mcsr.commands.SpawnCommand;
import com.daytonjwatson.mcsr.commands.StartRunCommand;
import com.daytonjwatson.mcsr.commands.StopRunCommand;
import com.daytonjwatson.mcsr.config.Config;
import com.daytonjwatson.mcsr.events.EndExit;
import com.daytonjwatson.mcsr.events.PlayerChat;
import com.daytonjwatson.mcsr.events.PlayerJoin;
import com.daytonjwatson.mcsr.events.PlayerPortal;
import com.daytonjwatson.mcsr.events.PlayerQuit;
import com.daytonjwatson.mcsr.events.PlayerRespawn;
import com.daytonjwatson.mcsr.events.PlayerWorldChange;
import com.daytonjwatson.mcsr.events.StrongholdAdvancement;
import com.daytonjwatson.mcsr.gui.LeaderboardGUIListener;
import com.daytonjwatson.mcsr.gui.PlayerDataGUIListener;
import com.daytonjwatson.mcsr.gui.PracticeGUIListener;
import com.daytonjwatson.mcsr.managers.PlayerDataManager;
import com.daytonjwatson.mcsr.practice.PracticeWorldManager;

public class MCSR extends JavaPlugin {

	public static MCSR instance;
	private Config c = new Config();

	@Override
	public void onEnable() {
		instance = this;
		
		c.initialize();
		PlayerDataManager.initialize(this);
		PracticeWorldManager.ensurePracticeFolder();

		initializeCommands();
		initializeEvents();
	}
	
	private void initializeCommands() {
		getCommand("debug").setExecutor(new DebugCommand());
		getCommand("generatehub").setExecutor(new GenerateHubCommand());
		getCommand("spawn").setExecutor(new SpawnCommand());
	    getCommand("setspawn").setExecutor(new SetSpawnCommand());
	    getCommand("startrun").setExecutor(new StartRunCommand());
	    getCommand("stoprun").setExecutor(new StopRunCommand());
	    getCommand("forfeit").setExecutor(new ForfeitCommand());
	    getCommand("playerdata").setExecutor(new PlayerDataCommand());
	    getCommand("leaderboard").setExecutor(new LeaderboardCommand());
	    getCommand("practice").setExecutor(new PracticeCommand());
	}
	
	private void initializeEvents() {
		PluginManager pm = MCSR.instance.getServer().getPluginManager();
		
		pm.registerEvents(new EndExit(), instance);
		pm.registerEvents(new PlayerChat(), instance);
		pm.registerEvents(new PlayerJoin(), instance);
		pm.registerEvents(new PlayerPortal(), instance);
		pm.registerEvents(new PlayerQuit(), instance);
		pm.registerEvents(new PlayerRespawn(), instance);
		pm.registerEvents(new PlayerWorldChange(), instance);
		pm.registerEvents(new PlayerDataGUIListener(), instance);
		pm.registerEvents(new LeaderboardGUIListener(), instance);
		pm.registerEvents(new PracticeGUIListener(), instance);
		pm.registerEvents(new StrongholdAdvancement(), instance);
	}
}
