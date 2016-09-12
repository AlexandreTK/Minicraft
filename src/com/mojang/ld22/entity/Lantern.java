package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;

public class Lantern extends Furniture {
	public Lantern() {
		super("Lantern");
		color = Color.get(-1, 000, 111, 555);
		sprite = 5;
		positionXRelative = 3;
		positionYRelative = 2;
	}

	public int getLightRadius() {
		return 8;
	}
}