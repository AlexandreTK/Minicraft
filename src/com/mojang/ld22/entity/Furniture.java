package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.FurnitureItem;
import com.mojang.ld22.item.PowerGloveItem;

public class Furniture extends Entity {
	private int pushTime = 0;
	private int pushDirection = -1;
	public int color;
	public int sprite;
	public String name;
	private Player shouldTake;

	public Furniture(String name) {
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
			}
			else {
				// nothing to do
			}
			
			shouldTake = null;
		}
		
		switch(pushDirection){
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
			
		pushDirection = -1;
		
		if (pushTime > 0)
			pushTime--;
	}

	public void render(Screen screen) {
		screen.render(positionX - 8, positionY - 8 - 4, sprite * 2 + 8 * 32, color, 0);
		screen.render(positionX - 0, positionY - 8 - 4, sprite * 2 + 8 * 32 + 1, color, 0);
		screen.render(positionX - 8, positionY - 0 - 4, sprite * 2 + 8 * 32 + 32, color, 0);
		screen.render(positionX - 0, positionY - 0 - 4, sprite * 2 + 8 * 32 + 33, color, 0);
	}

	public boolean blocks(Entity entity) {
		return true;
	}

	protected void touchedBy(Entity entity) {
		if (entity instanceof Player && pushTime == 0) {
			pushDirection = ((Player) entity).direction;
			pushTime = 10;
		}
		else {
			// nothing to do
		}
	}

	public void take(Player player) {
		shouldTake = player;
	}
}