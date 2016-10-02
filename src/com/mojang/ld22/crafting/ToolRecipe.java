package com.mojang.ld22.crafting;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;

public class ToolRecipe extends Recipe {
	private ToolType toolType;
	private int toolLevel;

	public ToolRecipe(ToolType toolType, int toolLevel) {
		super(new ToolItem(toolType, toolLevel));
		this.toolType = toolType;
		this.toolLevel = toolLevel;
	}

	public void craft(Player player) {
		player.inventory.add(0, new ToolItem(toolType, toolLevel));
	}
}
