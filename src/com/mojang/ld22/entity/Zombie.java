/*********************************************************  
* File: Zombie.java 
* Purpose: Zombie class implementation 
* ********************************************************/

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.TestLog;

/**
 * <h1>Zombie</h1> This class is related to the Zombie entity, which is rendered
 * in screen. It is responsible for the Zombie Creation, Movement, Attack, Death
 * and also to the Items it will drop.
 * 
 * It extends the Class Mob
 * 
 */
public class Zombie extends Mob {
	private int positionXAbsolute = 0, positionYAbsolute = 0;
	private int lvl = 0;
	private int randomWalkTime = 0;

	/**
	 * This is a constructor for the Zombie.
	 *
	 * @param level
	 *            : Indicates the level of the Zombie to be created. It should
	 *            be between 1 and 4 positionX, positionY : indicates the Zombie
	 *            position in the game. health : Indicates the Zombie health.
	 * @return none.
	 */
	public Zombie(int level) {
		
		if(lvl < 0){
			TestLog.logger.severe("Error level is negative - Zombie");
			assert(false);
		}
		
		this.lvl = level;
		// Zombie X position will be located between 0 and (64 * 16)
		positionX = random.nextInt(64 * 16);
		// Zombie Y position will be located between 0 and (64 * 16)
		positionY = random.nextInt(64 * 16); 
		health = maxHealth = level * level * 10;
		// TestLog.logger.info("Zombie was created.");
	}

	/**
	 * This is a method used to control the logic updates of the game. It
	 * basically indicates when each action will take place in the game.
	 * 
	 * It's related to the FPS(Frames Per Second), but while the last indicates
	 * how many times the screen will be refreshed, the Tick indicates how many
	 * times the logic of the game will be updated per second.
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
				TestLog.logger.info("Zombie walked more than 50*50 units.");
			} else {
				// DO NOTHING
			}
		} else {
			// DO NOTHING
		}

		int speed = tickTime & 1;

		if (!move(positionXAbsolute * speed, positionYAbsolute * speed) || random.nextInt(200) == 0) {
			randomWalkTime = 60;
			// Zombie will move between -1(away) and 1 unit from the Player.
			positionXAbsolute = (random.nextInt(3) - 1) * random.nextInt(2); 
			positionYAbsolute = (random.nextInt(3) - 1) * random.nextInt(2);
		} else {
			// DO NOTHING
		}

		if (randomWalkTime > 0)
			randomWalkTime--;
	}

	/**
	 * This method is used to control how the game behaves when a Zombie is
	 * rendered. It indicates what visual element should be rendered and at
	 * which position. It also indicates the Zombie Color.
	 * 
	 * @param Screen
	 *            : Object of the type Screen that indicates where the Zombie
	 *            will be rendered
	 * @return none.
	 */
	public void render(Screen screen) {
		
		if(screen == null){
			TestLog.logger.severe("Error screen is null - Zombie"); 
			assert(false);
		}
		
		// This variables indicate which visual element will be rendered in the
		// screen
		int elementXPositionSpriteSheet = 0;
		int elementYPositionSpriteSheet = 14;

		// These variables are related to the position of each part of the
		// figure of the Zombie.
		int flip1 = (walkedDistancy >> 3) & 1;
		int flip2 = (walkedDistancy >> 3) & 1;
		
		if (direction > 1) {
			flip1 = 0;
			flip2 = ((walkedDistancy >> 4) & 1);
			if (direction == 2) {
				flip1 = 1;
			} else {
				// DO NOTHING
			}
		} else {
			// DO NOTHING
		}
		elementXPositionSpriteSheet = ChangeElementXPositionSpriteSheet(elementXPositionSpriteSheet);

		// This variables indicate the distance from the Player to the element rendered
		int distFromPlayerX = positionX - 8;
		int distFromPlayerY = positionY - 11;

		// Zombie Attributes colors
		int colorAroundEntity = getColorAroundEntity(lvl);
		int borderColor = getBorderColor(lvl);
		int bodyColor = getBodyColor(lvl);
		int headColor = getHeadColor(lvl);
		int col = Color.get(colorAroundEntity, borderColor, bodyColor, headColor);
		
		// If Zombie is hurt, change its colors
		col = getZombieColorIfHurt(hurtTime,col);
		
		renderZombie(distFromPlayerX, distFromPlayerY, flip1, flip2, col,
				screen, elementXPositionSpriteSheet, elementYPositionSpriteSheet);
		TestLog.logger.info("Zombie with level: " + lvl + ", and color " + col + " rendered");
	}

	/**
	 * This method is used to control how the game behaves when a Zombie touches
	 * some other entity.
	 * 
	 * @param Entity
	 *            : Object of the type Entity what was touched by the Zombie
	 * @return none.
	 */
	protected void touchedBy(Entity entity) {
		
		if(entity == null){
			TestLog.logger.severe("Error entity is null - Zombie");
			assert(false);
		}
		
		// Hurts the Player, if it touches the Zombie
		if (entity instanceof Player) {
			entity.hurt(this, lvl + 1, direction);
			TestLog.logger.info("Player was hurt by the Zombie...");
		} else {
			// DO NOTHING
		}
		TestLog.logger.info("Zombie was touched.");
		
		try {
			entity.finalizeObject();
		} catch (Throwable e) {
			TestLog.logger.severe("Error finalizing the entity - Zombie");
			assert(false);
		}
	}

	/**
	 * This method is used to control how the game behaves when a Zombie dies.
	 * That is, when it's attacked by the player and its life is decreased to
	 * zero.
	 * 
	 * Items are dropped and the player score increases.
	 * 
	 * @param none.
	 * @return none.
	 */
	protected void die() {
		super.die();

		// value between 1 and 2, Indicates the number of items will be dropped
		int count = random.nextInt(2) + 1;
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
			// DO NOTHING
		}
		TestLog.logger.info("Zombie died...");
		
		try {
			super.finalize();
		} catch (Throwable e) {
			TestLog.logger.severe("Error finalizing the Zombie");
			assert(false);
		}
	}
	
	private int ChangeElementXPositionSpriteSheet(int elementXPositionSpriteSheet) {
		
		if (direction == 1) {
			// A different picture of the Zombie is rendered
			elementXPositionSpriteSheet += 2;
		} else {
			// DO NOTHING
		}
		
		if (direction > 1) {
			elementXPositionSpriteSheet += 4 + ((walkedDistancy >> 3) & 1) * 2;
		} else {
			// DO NOTHING
		}
		
		return elementXPositionSpriteSheet;
	}
	
	private int getColorAroundEntity(int level) {
		
		if(level > 4 || level < 1){
			TestLog.logger.severe("Error level is invalid - Zombie");
			assert(false);
		}
		
		int colorAroundEntity = 0;
		// Choose the Zombie color according to its level.
		switch (level) {
		case 1:
			colorAroundEntity = -1; // -1 => Background color
			break;
		case 2:
			colorAroundEntity = -1; // -1 => Background color
			break;
		case 3:
			colorAroundEntity = -1; // -1 => Background color
			break;
		case 4:
			colorAroundEntity = -1; // -1 => Background color
			break;
		}
		
		return colorAroundEntity;
	}

	private int getBorderColor(int level) {
		
		if(level > 4 || level < 1){
			TestLog.logger.severe("Error level is invalid - Zombie");
			assert(false);
		}
		
		int borderColor = 0;
		// Choose the Zombie color according to its level.
		switch (lvl) {
		case 1:
			borderColor = 10; // Border color is dark
			break;
		case 2:
			borderColor = 100; // Border color is dark
			break;
		case 3:
			borderColor = 111; // Border color is GRAY
			break;
		case 4:
			borderColor = 000; // Border color is BLACK
			break;
		}
		
		return borderColor;
	}

	private int getBodyColor(int level) {
		
		if(level > 4 || level < 1){
			TestLog.logger.severe("Error level is invalid - Zombie");
			assert(false);
		}
		
		int bodyColor = 0;
		// Choose the Zombie color according to its level.
		switch (lvl) {
		case 1:
			bodyColor = 252; // Body/shirt color is light GREEN
			break;
		case 2:
			bodyColor = 522; // Body color is PINK
			break;
		case 3:
			bodyColor = 444; // Body color is GRAY
			break;
		case 4:
			bodyColor = 111; // Body color is DARK GRAY
			break;
		}
		
		return bodyColor;
	}

	private int getHeadColor(int level) {
		
		if(level > 4 || level < 1){
			TestLog.logger.severe("Error level is invalid - Zombie");
			assert(false);
		}
		
		int headColor = 0;
		// Choose the Zombie color according to its level.
		switch (lvl) {
		case 1:
			headColor = 050; // Head color is Green
			break;
		case 2:
			headColor = 050; // Head color is Green
			break;
		case 3:
			headColor = 555; // Head color is WHITE
			break;
		case 4:
			headColor = 020; // Head color is Dark Green
			break;
		}
		
		return headColor;
	}

	private int getZombieColorIfHurt(int hurtTime, int col) {

		// The Zombie becomes WHITE if it is damaged
		if (hurtTime > 0) {
			final int background_color = -1;
			final int white_color = 555;
			col = Color.get(background_color, white_color, white_color, white_color);
		} else {
			// DO NOTHING
		}		
		return col;
	}
	
	private void renderZombie(int distFromPlayerX, int distFromPlayerY, int flip1, int flip2, int col,
			Screen screen, int elementXPositionSpriteSheet, int elementYPositionSpriteSheet) {

		if(screen == null){
			TestLog.logger.severe("Error screen is null - Zombie");
			assert(false);
		}
		
		// The Zombie is rendered in 4 different pieces, top-left, top-right,
		// bottom-left, bottom-down
		screen.render(distFromPlayerX + 8 * flip1, distFromPlayerY + 0,
				elementXPositionSpriteSheet + elementYPositionSpriteSheet * 32, col, flip1);
		screen.render(distFromPlayerX + 8 - 8 * flip1, distFromPlayerY + 0,
				elementXPositionSpriteSheet + 1 + elementYPositionSpriteSheet * 32, col, flip1);
		screen.render(distFromPlayerX + 8 * flip2, distFromPlayerY + 8,
				elementXPositionSpriteSheet + (elementYPositionSpriteSheet + 1) * 32, col, flip2);
		screen.render(distFromPlayerX + 8 - 8 * flip2, distFromPlayerY + 8,
				elementXPositionSpriteSheet + 1 + (elementYPositionSpriteSheet + 1) * 32, col, flip2);
	}

}