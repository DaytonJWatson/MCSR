package com.daytonjwatson.mcsr;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.daytonjwatson.mcsr.commands.DebugCommand;
import com.daytonjwatson.mcsr.commands.GenerateHubCommand;
import com.daytonjwatson.mcsr.commands.SetSpawnCommand;
import com.daytonjwatson.mcsr.commands.SpawnCommand;
import com.daytonjwatson.mcsr.commands.StartRunCommand;
import com.daytonjwatson.mcsr.commands.StopRunCommand;
import com.daytonjwatson.mcsr.config.Config;
import com.daytonjwatson.mcsr.events.EndExit;
import com.daytonjwatson.mcsr.events.PlayerJoin;
import com.daytonjwatson.mcsr.events.PlayerPortal;
import com.daytonjwatson.mcsr.events.PlayerQuit;
import com.daytonjwatson.mcsr.events.PlayerRespawn;

public class MCSR extends JavaPlugin {

	public static MCSR instance;
	private Config c = new Config();

	@Override
	public void onEnable() {
		instance = this;
		
		c.initialize();

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
	}
	
	private void initializeEvents() {
		PluginManager pm = MCSR.instance.getServer().getPluginManager();
		
		pm.registerEvents(new EndExit(), instance);
		pm.registerEvents(new PlayerJoin(), instance);
		pm.registerEvents(new PlayerPortal(), instance);
		pm.registerEvents(new PlayerQuit(), instance);
		pm.registerEvents(new PlayerRespawn(), instance);
	}
}
