package com.mojang.ld22.item.resource;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;

public class FoodResource extends Resource {
	private int heal;
	private int staminaCost;

	public FoodResource(String name, int sprite, int colorFood, int heal, int staminaCost) {
		super(name, sprite, colorFood);
		this.heal = heal;
		this.staminaCost = staminaCost;
	}

	public boolean interactOn(Tile tile, Level level, int positionXWalked, int positionYWalked, Player player,
			int attackDirection) {
		if (player.health < player.maxHealth && player.payStamina(staminaCost)) {
			player.heal(heal);
			return true;
		}
		return false;
	}
}
