package com.daytonjwatson.mcsr.config;

import com.daytonjwatson.mcsr.MCSR;

public class Config {
	
	private boolean exists() {
		if(MCSR.instance.getDataFolder().exists())
			return true;
		else 
			return false;
	}
	
	public void initialize() {
		if(!exists())
			create();
		else
			return;
	}
	
	private void create() {
		MCSR.instance.getConfig().options().copyDefaults(true);
		MCSR.instance.saveDefaultConfig();
	}
	
	public static void save() {
		MCSR.instance.saveConfig();
	}
	
	public void reload() {
		MCSR.instance.reloadConfig();
	}	
	
	public static Object get(String path) {
		return MCSR.instance.getConfig().get(path);
	}
	
	public static String getString(String path) {
		return MCSR.instance.getConfig().getString(path);
	}
	
	public static int getInt(String path) {
		return MCSR.instance.getConfig().getInt(path);
	}
	
	public static double getDouble(String path) {
		return MCSR.instance.getConfig().getDouble(path);
	}
	
	public static boolean getBoolean(String path) {
		return MCSR.instance.getConfig().getBoolean(path);
	}
	
	public static void set(String path, Object obj) {
		MCSR.instance.getConfig().set(path, obj);
		save();
	}
	
	public static void setString(String path, String string) {
		MCSR.instance.getConfig().set(path, string);
		save();
	}
	
	public static void setInt(String path, int obj) {
		MCSR.instance.getConfig().set(path, obj);
		save();
	}
	
	public static void setDouble(String path, double obj) {
		MCSR.instance.getConfig().set(path, obj);
		save();
	}
	
	public static void setBoolean(String path, boolean bool) {
		MCSR.instance.getConfig().set(path, bool);
		save();
	}
}