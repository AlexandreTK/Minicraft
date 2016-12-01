/*********************************************************  
* File: Slime.java 
* Purpose: Slime class implementation 
* ********************************************************/  

package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.TestLog;

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
		
		if(lvl < 0){	
			TestLog.logger.severe("Error lvl is negative - Slime");
			// Since this class do not receive user inputs, any wrong argument came from 
			// the game, so it's better to use assert instead of throwing exceptions. 
			assert(false);
		}
		
		this.lvl = lvl;
		positionX = random.nextInt(64 * 16); // Slime X position will be located between 0 and (64 * 16)
		positionY = random.nextInt(64 * 16); // Slime Y position will be located between 0 and (64 * 16)
		health = maxHealth = lvl * lvl * 5; 
		//TestLog.logger.info("Slime was created.");
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
				// Slime will move between -1(away) and 1 unit from the Player
				positionXAbsolute = (random.nextInt(3) - 1); 
				// Slime will move between -1(away) and 1 unit from the Player
				positionYAbsolute = (random.nextInt(3) - 1); 
 
				//TestLog.logger.info("Slime movement in X: " + positionXAbsolute + ",
				// and movement in Y" + positionYAbsolute);
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
						TestLog.logger.info("Slime walked more than 50*50 units.");
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
	 * This method is used to control how the game behaves when a Slime is rendered.
	 * It indicates what visual element should be rendered and at which position.
	 * It also indicates the Slime Color.
	 * 
	 * @param Screen : Object of the type Screen that indicates where the Slime will be
	 * rendered
	 * @return none.
	 */
	public void render(Screen screen) {

		if(screen == null){
			TestLog.logger.severe("Error screen is null - Slime");
			// Since this class do not receive user inputs, any wrong argument came from 
			// the game, so it's better to use assert instead of throwing exceptions. 
			assert(false);
		}
		
		// This variables indicate which visual element will be rendered in the screen 
		int elementXPositionSpriteSheet = 0;
		int elementYPositionSpriteSheet = 18;

		// This variables indicate the distance from the Player to the element rendered
		int distFromPlayerX = positionX - 8;
		int distFromPlayerY = positionY - 11;

		// If the Slime is jumping, do these actions
		if (jumpTime > 0) {
			elementXPositionSpriteSheet += 2; // A different picture of the slim is rendered
			distFromPlayerY -= 4; // Gets 4 units closer to the player
		}else{
			//nothing to do
		}
		
		int colorAroundEntity = getColorAroundEntity(lvl);
		int borderColor = getBorderColor(lvl);
		int bodyColor = getBodyColor(lvl);
		int eyeColor = getEyeColor(lvl);
		int col = Color.get(colorAroundEntity, borderColor, bodyColor, eyeColor);

		col = getSlimeColorIfHurt(hurtTime, col);
		
		renderSlime(distFromPlayerX, distFromPlayerY, col, screen, 
				elementXPositionSpriteSheet, elementYPositionSpriteSheet);

		TestLog.logger.info("Slime with level: " + level + ", and color " + col + " rendered");
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
			// Item dropped at the position X of the player +- 5
			int resourcePositionX = positionX + random.nextInt(11) - 5;
			// Item dropped at the position Y of the player +- 5
			int resourcePositionY = positionY + random.nextInt(11) - 5;
			level.add(new ItemEntity(resourceItem, resourcePositionX, resourcePositionY));
		}

		// Increase player score
		if (level.player != null) {
			level.player.score += 25*lvl;
		}else{
			//nothing to do
		}
		TestLog.logger.info("Slime died...");

		try {
			super.finalize();
		} catch (Throwable e) {
			TestLog.logger.severe("Error finalizing the slime");
			assert(false);
		}
		
	}


	/**
	 * This method is used to control how the game behaves when a Slime touches some other entity.
	 * 
	 * @param Entity : Object of the type Entity what was touched by the Slime
	 * @return none.
	 */
	protected void touchedBy(Entity entity) {

		if(entity == null){
			TestLog.logger.severe("Error entity is null - Slime");
			// Since this class do not receive user inputs, any wrong argument came from 
			// the game, so it's better to use assert instead of throwing exceptions. 
			assert(false);
		}
		
		// Hurts the Player, if it touches the Slime
		if (entity instanceof Player) {
			entity.hurt(this, lvl, direction);
			TestLog.logger.info("Player was hurt by the Slime...");
		}else{
			//nothing to do
		}
		TestLog.logger.info("Slime was touched.");

		try {
			entity.finalizeObject();
		} catch (Throwable e) {
			TestLog.logger.severe("Error finalizing the entity - Slime");
			assert(false);
		}
		
	}

	private void renderSlime(int distFromPlayerX, int distFromPlayerY, int col, Screen screen, 
			int elementXPositionSpriteSheet, int elementYPositionSpriteSheet) {

		if(screen == null){
			TestLog.logger.severe("Error screen is null - Slime");
			assert(false);
		}
		// The slime is rendered in 4 different pieces, top-left, top-right, bottom-left, bottom-down
		screen.render(distFromPlayerX + 0, distFromPlayerY + 0,
				elementXPositionSpriteSheet + elementYPositionSpriteSheet * 32, col, 0);
		screen.render(distFromPlayerX + 8, distFromPlayerY + 0,
				elementXPositionSpriteSheet + 1 + elementYPositionSpriteSheet * 32, col, 0);
		screen.render(distFromPlayerX + 0, distFromPlayerY + 8,
				elementXPositionSpriteSheet + (elementYPositionSpriteSheet + 1) * 32, col, 0);
		screen.render(distFromPlayerX + 8, distFromPlayerY + 8,
				elementXPositionSpriteSheet + 1 + (elementYPositionSpriteSheet + 1) * 32, col, 0);
	}
	
	private int getSlimeColorIfHurt(int hurtTime, int col) {

		// The slime becomes WHITE if it is damaged
		if (hurtTime > 0) {
			final int background_color = -1;
			final int white_color = 555;
			col = Color.get(background_color, white_color, white_color, white_color);
		}else{
			//nothing to do
		}	
		return col;
	}
	
	private int getColorAroundEntity(int level) {
		
		if(level > 4 || level < 1){
			TestLog.logger.severe("Error level is invalid - Slime");
			assert(false);
		}
		
		int colorAroundEntity = 0;
		// Choose the Slime color according to its level.
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
			TestLog.logger.severe("Error level is invalid - Slime");
			assert(false);
		}
		
		int borderColor = 0;
		// Choose the Slime color according to its level.
		switch (lvl) {
		case 1:
			borderColor = 10;		// Border color is dark
			break;
		case 2:
			borderColor = 100;		// Border color is dark
			break;
		case 3:
			borderColor = 111;		// Border color is GRAY
			break;
		case 4:
			borderColor = 000;		// Border color is BLACK
			break;
		}
		
		return borderColor;
	}

	private int getBodyColor(int level) {
		
		if(level > 4 || level < 1){
			TestLog.logger.severe("Error level is invalid - Slime");
			assert(false);
		}
		
		int bodyColor = 0;
		// Choose the Slime color according to its level.
		switch (lvl) {
		case 1:
			bodyColor = 252;		// Body color is GREEN
			break;
		case 2:
			bodyColor = 522;		// Body color is PINK
			break;
		case 3:
			bodyColor = 444;		// Body color is GRAY
			break;
		case 4:
			bodyColor = 111;		// Body color is DARK GRAY
			break;
		}
		
		return bodyColor;
	}

	private int getEyeColor(int level) {
		
		if(level > 4 || level < 1){
			TestLog.logger.severe("Error level is invalid - Slime");
			assert(false);
		}
		
		int eyeColor = 0;
		// Choose the Slime color according to its level.
		switch (lvl) {
		case 1:
			eyeColor = 555; 		// Eye color is WHITE
			break;
		case 2:
			eyeColor = 555; 		// Eye color is WHITE
			break;
		case 3:
			eyeColor = 555; 		// Eye color is WHITE
			break;
		case 4:
			eyeColor = 224;			// Border color is PURPLE
			break;
		}
		
		return eyeColor;
	}
	
}