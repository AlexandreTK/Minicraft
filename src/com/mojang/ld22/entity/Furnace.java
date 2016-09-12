package com.mojang.ld22.entity;

import com.mojang.ld22.crafting.Crafting;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.screen.CraftingMenu;

public class Furnace extends Furniture {
	public Furnace() {
		super("Furnace");
		color = Color.get(-1, 000, 222, 333);
		sprite = 3;
		positionXRelative = 3;
		positionYRelative = 2;
	}

	public boolean use(Player player, int attackDirection) {
		player.game.setMenu(new CraftingMenu(Crafting.furnaceRecipes, player));
		return true;
	}
}