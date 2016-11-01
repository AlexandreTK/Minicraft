package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;

/**
 * <h1> Slime </h1>
 * This class is related to the Slime entity that is rendered in screen.
 * It is responsible for the Slime Creation, Movement, Attack, Death and
 * also to the Items it will drop.
 * 
 * It extends the Class Mob
 * 
 */
public class Slime extends Mob {
	private int positionXAbsolute, positionYAbsolute; // Position of slime
	private int jumpTime = 0; //delay of slime movement
	private int lvl;

	/**
	 * This is a constructor for the slime.
	 *
	 * @param lvl : Indicates the level of the Slime to be created. It should 
	 * be between 1 and 4
	 * positionX, positionY : indicates the Slime position in the game.
	 * health : Indicates the Slime health.
	 * @return none.
	 */
	public Slime(int lvl) {
		this.lvl = lvl;
		positionX = random.nextInt(64 * 16); // Slime X position will be located between 0 and (64 * 16)
		positionY = random.nextInt(64 * 16); // Slime Y position will be located between 0 and (64 * 16)
		health = maxHealth = lvl * lvl * 5; 
	}

	/**
	 * This is a method  used to control the logic updates of the game.
	 * It basically indicates when each action will take place in the game.
	 * 
	 * It's related to the FPS(Frames Per Second), but while the last indicates how many times the
	 * screen will be refreshed, the Tick indicates how many times the logic of the game will
	 * be updated per second.
	 *
	 * @param none.
	 * @return none.
	 */
	public void tick() {
		super.tick();

		int speed = 1;
		if (!move(positionXAbsolute * speed, positionYAbsolute * speed) || random.nextInt(40) == 0) {
			if (jumpTime <= -10) {
				
				// Slime movement when it's not following the player
				positionXAbsolute = (random.nextInt(3) - 1); // Slime will move between -1(away) and 1 unit from the Player
				positionYAbsolute = (random.nextInt(3) - 1); // Slime will move between -1(away) and 1 unit from the Player

				if (level.player != null) {
					//Distance Walked from the player
					int positionXWalked = level.player.positionX - positionX;
					int positionYWalked = level.player.positionY - positionY;
					
					// After following the player for 50*50 units, act different
					int distanceWalked = positionXWalked * positionXWalked + positionYWalked * positionYWalked;
					if (distanceWalked < 50 * 50) {
						if (positionXWalked < 0){
							positionXAbsolute = -1;}
						if (positionXWalked > 0){
							positionXAbsolute = +1;}
						if (positionYWalked < 0){
							positionYAbsolute = -1;}
						if (positionYWalked > 0){
							positionYAbsolute = +1;}
					}else{
						//nothing to do
					}

				}else{
					//nothing to do
				}

				if (positionXAbsolute != 0 || positionYAbsolute != 0) {
					jumpTime = 10;
				}
			}else{
				//nothing to do
			}
		}else{
			//nothing to do
		}

		jumpTime--;
		
		if (jumpTime == 0) {
			positionXAbsolute = positionYAbsolute = 0;
		}else{
			//nothing to do
		}
	}
	/**
	 * This method is used to control how the game behaves when a Slime dies. That is, 
	 * when it's attacked by the player and its life is decreased to zero.
	 * 
	 * Items are dropped and the player score increases.
	 * 
	 * @param none.
	 * @return none.
	 */
	protected void die() {
		super.die();

		// Indicates the number of items will be dropped
		int count = random.nextInt(2) + 1; // value between 1 and 2
		for (int i = 0; i < count; i++) {
			// When the Slime dies new items are created and dropped.
			ResourceItem resourceItem = new ResourceItem(Resource.slime);
			int resourcePositionX = positionX + random.nextInt(11) - 5;// Item dropped at the position X of the player +- 5
			int resourcePositionY = positionY + random.nextInt(11) - 5;// Item dropped at the position Y of the player +- 5
			level.add(new ItemEntity(resourceItem, resourcePositionX, resourcePositionY));
		}

		// Increase player score
		if (level.player != null) {
			level.player.score += 25*lvl;
		}else{
			//nothing to do
		}
	}

	public void render(Screen screen) {
		int xt = 0;
		int yt = 18;

		int xo = positionX - 8;
		int yo = positionY - 11;

		if (jumpTime > 0) {
			xt += 2;
			yo -= 4;
		}else{
			//nothing to do
		}

		int col = Color.get(-1, 10, 252, 555);
		switch(lvl){
		case 2:
			col = Color.get(-1, 100, 522, 555);
			break;
		case 3:
			col = Color.get(-1, 111, 444, 555);
			break;
		case 4:
			col = Color.get(-1, 000, 111, 224);
			break;
		}
				

		if (hurtTime > 0) {
			col = Color.get(-1, 555, 555, 555);
		}else{
			//nothing to do
		}

		screen.render(xo + 0, yo + 0, xt + yt * 32, col, 0);
		screen.render(xo + 8, yo + 0, xt + 1 + yt * 32, col, 0);
		screen.render(xo + 0, yo + 8, xt + (yt + 1) * 32, col, 0);
		screen.render(xo + 8, yo + 8, xt + 1 + (yt + 1) * 32, col, 0);
	}

	protected void touchedBy(Entity entity) {
		if (entity instanceof Player) {
			entity.hurt(this, lvl, direction);
		}else{
			//nothing to do
		}
	}
}