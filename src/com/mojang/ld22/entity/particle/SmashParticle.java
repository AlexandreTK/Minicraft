package com.mojang.ld22.entity.particle;

//File: SmashParticle.java
//Objective: Render the small particles of any entity.

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.sound.Sound;

public class SmashParticle extends Entity {
	private int time = 0;

	public SmashParticle(int x, int y) {
		// Secure constructor
		assert x >= 0 : "Position x must be positive";
		assert y >= 0 : "Position y must be positive";
		
		this.positionX = x;
		this.positionY = y;
		Sound.monsterHurt.play();
	}

	public void tick() {
		time++;
		if (time > 10) {
			remove();
		} else {
			//Do nothing.
		}
	}

	public void render(Screen screen) {
		// Secure method
		assert screen != null : "Screen cannot be null";
		
		int col = Color.get(-1, 555, 555, 555);
		screen.render(positionX - 8, positionY - 8, 5 + 12 * 32, col, 2);
		screen.render(positionX - 0, positionY - 8, 5 + 12 * 32, col, 3);
		screen.render(positionX - 8, positionY - 0, 5 + 12 * 32, col, 0);
		screen.render(positionX - 0, positionY - 0, 5 + 12 * 32, col, 1);
	}
}
