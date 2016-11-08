package com.mojang.ld22.entity;

import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.sound.Sound;

public class Mob extends Entity {
	protected int walkedDistancy = 0;
	protected int direction = 0;
	public int hurtTime = 0;
	protected int positionXKnockback, positionYKnockback; // current position of the mobs
	public int maxHealth = 10;
	public int health = maxHealth; //at spawn the all the mobs will get the max of helth
	public int swimTimer = 0; //time allowed in the water
	public int tickTime = 0;

	public Mob() {
		positionX = positionY = 8;
		positionXRelative = 4;
		positionYRelative = 3;
	}

	public void tick() {
		tickTime++;
		if (level.getTile(positionX >> 4, positionY >> 4) == Tile.lava) {
			hurt(this, 4, direction ^ 1);
		}
		else {
			// nothing to do
		}

		if (health <= 0) {
			die();
		}
		else {
			// nothing to do
		}
		if (hurtTime > 0) hurtTime--;
	}

	protected void die() {
		remove();
	}

	/*
	 * As known, the players have some types of moviments in certain structures and this
	 * method deal with each type of moviment
	 */
	public boolean move(int positionXAbsolute, int positionYAbsolute) {
		
		if (isSwimming()) {
			if (swimTimer++ % 2 == 0)
				return true;
		}
		else {
			// nothing to do
		}
		
		if (positionXKnockback < 0) {
			move2(-1, 0);
			positionXKnockback++;
		}
		if (positionXKnockback > 0) {
			move2(1, 0);
			positionXKnockback--;
		}
		
		
		if (positionYKnockback < 0) {
			move2(0, -1);
			positionYKnockback++;
		}
		
		if (positionYKnockback > 0) {
			move2(0, 1);
			positionYKnockback--;
		}
		
		
		
		if (hurtTime > 0)
			return true;
		
		if (positionXAbsolute != 0 || positionYAbsolute != 0) {
			walkedDistancy++;
			if (positionXAbsolute < 0){
				direction = 2;
			}
			if (positionXAbsolute > 0) {
				direction = 3;
			}
			
			if (positionYAbsolute < 0){
				direction = 1;
			}
			if (positionYAbsolute > 0){
				direction = 0;
			}
		}
		return super.move(positionXAbsolute, positionYAbsolute);
	}

	protected boolean isSwimming() {
		Tile tile = level.getTile(positionX >> 4, positionY >> 4);
		return tile == Tile.water || tile == Tile.lava;
	}

	public boolean blocks(Entity entity) {
		return entity.isBlockableBy(this);
	}

	public void hurt(Tile tile, int positionX, int positionY, int damage) {
		int attackDirection = direction ^ 1;
		doHurt(damage, attackDirection);
	}

	public void hurt(Mob mob, int damage, int attackDirection) {
		doHurt(damage, attackDirection);
	}

	public void heal(int heal) {
		if (hurtTime > 0)
			return;

		level.add(new TextParticle("" + heal, positionX, positionY, Color.get(-1, 50, 50, 50)));
		health += heal;
		
		if (health > maxHealth)
			health = maxHealth;
	}

	protected void doHurt(int damage, int attackDirection) {
		
		if (hurtTime > 0)
			 	return;
		
		if (level.player != null) {
			int positionXWalked = level.player.positionX - positionX;
			int positionYWalked = level.player.positionY - positionY;
			
			if (positionXWalked * positionXWalked + positionYWalked * positionYWalked < 80 * 80) {
				Sound.monsterHurt.play();
			}
			else{
				//nothing to do
			}
		}
		else{
			//nothing to do
		
		}
		level.add(new TextParticle("" + damage, positionX, positionY, Color.get(-1, 500, 500, 500)));
		health -= damage;
		
		
		switch(attackDirection){
		case 0:
			positionYKnockback = +6;
			break;
		case 1:
			positionYKnockback = -6;
			break;
		case 2:
			positionXKnockback = -6;
			break;
			
		case 3:
			positionXKnockback = +6;
			break;
		
		}
		hurtTime = 10;
	}

	public boolean findStartPos(Level level) {
		int positionX = random.nextInt(level.width);
		int positionY = random.nextInt(level.height);
		int positionXX = positionX * 16 + 8;
		int positionYY = positionY * 16 + 8;

		if (level.player != null) {
			int positionXWalked = level.player.positionX - positionXX;
			int positionYWalked = level.player.positionY - positionYY;
			
			if (positionXWalked * positionXWalked + positionYWalked * positionYWalked < 80 * 80)
				return false;
		}
		else{
			//nothing to do
		}

		int radius = (int)level.monsterDensity * 16;
		
		if (level.getEntities(positionXX - radius,
							  positionYY - radius,
							  positionXX + radius,
							  positionYY + radius).size() > 0)
			return false;

		if (level.getTile(positionX, positionY).mayPass(level, positionX, positionY, this)) {
			this.positionX = positionXX;
			this.positionY = positionYY;
			return true;
		}

		return false;
	}
}
