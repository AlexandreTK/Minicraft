package com.mojang.ld22.entity;

import com.mojang.ld22.TestLog;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.FurnitureItem;
import com.mojang.ld22.item.PowerGloveItem;

public class Furniture extends Entity {

	public String name = null;
	private Player shouldTake = null;
	private int pushDirection = -1;
	private int pushTime = 0;
	public int color = 0;
	public int sprite = 0;
	

	public Furniture(String name) {
		if(name == null){	
			TestLog.logger.severe("Error String is null - Furniture");
			assert(false);
		}
		
		this.name = name;
		positionXRelative = 3;
		positionYRelative = 3;
	}

	public void tick() {
		if (shouldTake != null) {
			if (shouldTake.activeItem instanceof PowerGloveItem) {
				remove();
				shouldTake.inventory.add(0, shouldTake.activeItem);
				shouldTake.activeItem = new FurnitureItem(this);
			} else {
				// DO NOTHING
			}

			shouldTake = null;
		}

		pushFurniture(pushDirection);

		pushDirection = -1;

		if (pushTime > 0)
			pushTime--;
	}
	
	/**
	 * Render the furniture on the screen.
	 * 
	 * @param screen - object Screen, where the furniture will be rendered.
	 * @return nothing (void)
	 */
	public void render(Screen screen) {

		if(screen == null){
			TestLog.logger.severe("Error Screen is null - Furniture");
			assert(false);
		}
		
		// Renders the Furniture in the screen at the (positionX, positionY) coordinate
		// and the sprite rendered is spriteBasePosition.
		final int leftPosition = -8;
		final int downPosition = -8;
		final int spriteBasePosition = sprite * 2 + 8 * 32;
		screen.render(positionX + leftPosition, positionY + downPosition- 4,
				spriteBasePosition, color, 0); 
		screen.render(positionX, positionY + downPosition - 4,
				spriteBasePosition + 1, color, 0);
		screen.render(positionX + leftPosition, positionY - 4,
				spriteBasePosition + 32, color, 0);
		screen.render(positionX, positionY - 4,
				spriteBasePosition + 33, color, 0);
	}

	/**
	 * This function only returns true.
	 * 
	 * @param entity - object Entity.
	 * @return true
	 */
	public boolean blocks(Entity entity) {
		return true;
	}

	/**
	 * This method only sets the variable shouldTake to the player.
	 * 
	 * @param player - object Player.
	 */
	public void take(Player player) {
		if(player == null){
			TestLog.logger.severe("Error Player is null - Furniture");
			// Since this class do not receive user inputs, any wrong argument came from 
			// the game, so it's better to use assert instead of throwing exceptions. 
			assert(false);
		}
		
		shouldTake = player;
	}
	
	/**
	 * This method verifies whether a player touched the furniture or not.
	 * If so, the furniture will be pushed towards the players direction.
	 * 
	 * @param entity - indicates the entity which touched the furniture.
	 */
	protected void touchedBy(Entity entity) {
		
		if(entity == null){
			TestLog.logger.severe("Error Entity is null - Furniture");
			// Since this class do not receive user inputs, any wrong argument came from 
			// the game, so it's better to use assert instead of throwing exceptions. 
			assert(false);
		}
		
		if (entity instanceof Player && pushTime == 0) {
			pushDirection = ((Player) entity).direction;
			pushTime = 10;
		} else {
			// DO NOTHING
		}
	}

	private void pushFurniture(int pushDirection) {
		switch (pushDirection) {
		case 0:
			move(0, +1);
			break;
		case 1:
			move(0, -1);
			break;
		case 2:
			move(-1, 0);
			break;
		case 3:
			move(+1, 0);
			break;
		}
	}
}