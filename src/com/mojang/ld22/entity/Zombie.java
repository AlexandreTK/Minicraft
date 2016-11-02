// File: Zombie.java

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;

/**
 * <h1> Zombie </h1>
 * This class is related to the Zombie entity, which is rendered in screen.
 * It is responsible for the Zombie Creation, Movement, Attack, Death and
 * also to the Items it will drop.
 * 
 * It extends the Class Mob
 * 
 */
public class Zombie extends Mob {
	private int positionXAbsolute = 0, positionYAbsolute = 0;
	private int lvl;
	private int randomWalkTime = 0;

	/**
	 * This is a constructor for the Zombie.
	 *
	 * @param level : Indicates the level of the Zombie to be created. It should 
	 * be between 1 and 4
	 * positionX, positionY : indicates the Zombie position in the game.
	 * health : Indicates the Zombie health.
	 * @return none.
	 */
	public Zombie(int level) {
		this.lvl = level;
		positionX = random.nextInt(64 * 16); // Zombie X position will be located between 0 and (64 * 16)
		positionY = random.nextInt(64 * 16); // Zombie Y position will be located between 0 and (64 * 16)
		health = maxHealth = level * level * 10;
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
		
		// Walk after 'randomWalkTime' ticks and decrease this variable.
		if (level.player != null && randomWalkTime == 0) {
			
			// Distance Walked from the player
			int positionXWalked = level.player.positionX - positionX;
			int positionYWalked = level.player.positionY - positionY;

			// After following the player for 50*50 units, act different
			final int distanceWalked = positionXWalked * positionXWalked + positionYWalked * positionYWalked;
			if (distanceWalked < 50 * 50) {
				if (positionXWalked < 0) {
					positionXAbsolute = -1;
				}
				if (positionXWalked > 0) {
					positionXAbsolute = +1;
				}
				if (positionYWalked < 0) {
					positionYAbsolute = -1;
				}
				if (positionYWalked > 0) {
					positionYAbsolute = +1;
				}
				
			} else {
				// nothing to do
			}
		} else {
			// nothing to do
		}

		int speed = tickTime & 1;

		if (!move(positionXAbsolute * speed, positionYAbsolute * speed) || random.nextInt(200) == 0) {
			randomWalkTime = 60;
			positionXAbsolute = (random.nextInt(3) - 1) * random.nextInt(2); // Zombie will move between -1(away) and 1 unit from the Player
			positionYAbsolute = (random.nextInt(3) - 1) * random.nextInt(2); // Zombie will move between -1(away) and 1 unit from the Player
		} else {
			// nothing to do
		}

		if (randomWalkTime > 0)
			randomWalkTime--;
	}

	/**
	 * This method is used to control how the game behaves when a Zombie is rendered.
	 * It indicates what visual element should be rendered and at which position.
	 * It also indicates the Zombie Color.
	 * 
	 * @param Screen : Object of the type Screen that indicates where the Zombie will be
	 * rendered
	 * @return none.
	 */
	public void render(Screen screen) {

		// This variables indicate which visual element will be rendered in the screen 
		int elementXPositionSpriteSheet = 0;
		int elementYPositionSpriteSheet = 14;

		// These variables are related to the position of each part of the figure of the Zombie.
		int flip1 = (walkedDistancy >> 3) & 1;
		int flip2 = (walkedDistancy >> 3) & 1;

		
		if (direction == 1) {
			elementXPositionSpriteSheet += 2; // A different picture of the Zombie is rendered
		} else {
			// nothing to do
		}
		if (direction > 1) {

			flip1 = 0;
			flip2 = ((walkedDistancy >> 4) & 1);

			if (direction == 2) {
				flip1 = 1;
			}

			elementXPositionSpriteSheet += 4 + ((walkedDistancy >> 3) & 1) * 2;
		} else {
			// nothing to do
		}
		
		// This variables indicate the distance from the Player to the element rendered
		int distFromPlayerX = positionX - 8;
		int distFromPlayerY = positionY - 11;

		// Zombie Attributes colors
		int colorAroundEntity = 0;
		int borderColor = 0;
		int bodyColor = 0;
		int headColor = 0;
		int col = 0;
		
		// Choose the Zombie color according to its level.
		switch (lvl) {
		case 1:
			colorAroundEntity = -1; // -1 => Background color
			borderColor = 10;		// Border color is dark
			bodyColor = 252;		// Body/shirt color is light GREEN
			headColor = 050; 		// Head color is Green
			col = Color.get(colorAroundEntity, borderColor, bodyColor, headColor);
			break;
		case 2:
			colorAroundEntity = -1; // -1 => Background color
			borderColor = 100;		// Border color is dark
			bodyColor = 522;		// Body color is PINK
			headColor = 050; 		// Head color is Green
			col = Color.get(colorAroundEntity, borderColor, bodyColor, headColor);
			break;
		case 3:
			colorAroundEntity = -1; // -1 => Background color
			borderColor = 111;		// Border color is GRAY
			bodyColor = 444;		// Body color is GRAY
			headColor = 555; 		// Head color is WHITE
			col = Color.get(colorAroundEntity, borderColor, bodyColor, headColor);
			break;
		case 4:
			colorAroundEntity = -1; // -1 => Background color
			borderColor = 000;		// Border color is BLACK
			bodyColor = 111;		// Body color is DARK GRAY
			headColor = 020;			// Head color is Dark Green
			col = Color.get(colorAroundEntity, borderColor, bodyColor, headColor);
			break;
		}
		// The Zombie becomes WHITE if it is damaged
		if (hurtTime > 0) {
			final int background_color = -1;
			final int white_color = 555;
			col = Color.get(background_color, white_color, white_color, white_color);
		} else {
			// nothing to do
		}

		// The Zombie is rendered in 4 different pieces, top-left, top-right, bottom-left, bottom-down
		screen.render(distFromPlayerX + 8 * flip1, distFromPlayerY + 0, elementXPositionSpriteSheet + elementYPositionSpriteSheet * 32, col, flip1);
		screen.render(distFromPlayerX + 8 - 8 * flip1, distFromPlayerY + 0, elementXPositionSpriteSheet + 1 + elementYPositionSpriteSheet * 32, col, flip1);
		screen.render(distFromPlayerX + 8 * flip2, distFromPlayerY + 8, elementXPositionSpriteSheet + (elementYPositionSpriteSheet + 1) * 32, col, flip2);
		screen.render(distFromPlayerX + 8 - 8 * flip2, distFromPlayerY + 8, elementXPositionSpriteSheet + 1 + (elementYPositionSpriteSheet + 1) * 32, col, flip2);
	}

	/**
	 * This method is used to control how the game behaves when a Zombie touches some other entity.
	 * 
	 * @param Entity : Object of the type Entity what was touched by the Zombie
	 * @return none.
	 */
	protected void touchedBy(Entity entity) {
		// Hurts the Player, if it touches the Zombie
		if (entity instanceof Player) {
			entity.hurt(this, lvl + 1, direction);
		} else {
			// nothing to do
		}
	}

	/**
	 * This method is used to control how the game behaves when a Zombie dies. That is, 
	 * when it's attacked by the player and its life is decreased to zero.
	 * 
	 * Items are dropped and the player score increases.
	 * 
	 * @param none.
	 * @return none.
	 */
	protected void die() {
		super.die();
		
		int count = random.nextInt(2) + 1; // value between 1 and 2, Indicates the number of items will be dropped
		// When the Zombie dies new items are created and dropped.
		for (int i = 0; i < count; i++) {
			ResourceItem resourceItem = new ResourceItem(Resource.cloth);
			int resourcePositionX = positionX + random.nextInt(11) - 5;
			int resourcePositionY = positionY + random.nextInt(11) - 5;
			level.add(new ItemEntity(resourceItem, resourcePositionX, resourcePositionY));
		}

		// Increase player score
		if (level.player != null) {
			level.player.score += 50 * lvl;
		} else {
			// nothing to do
		}

	}

}