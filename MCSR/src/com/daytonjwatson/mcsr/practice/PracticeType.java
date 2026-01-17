package com.daytonjwatson.mcsr.practice;

public enum PracticeType {
	BLAZE("blazepractice", "Blaze Practice"),
	CRAFTING("craftingpractice", "Crafting Practice"),
	OVERWORLD("overworldpractice", "Overworld Practice"),
	ZERO("zeropractice", "Zero Practice");

	private final String folderName;
	private final String displayName;

	PracticeType(String folderName, String displayName) {
		this.folderName = folderName;
		this.displayName = displayName;
	}

	public String getFolderName() {
		return folderName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
