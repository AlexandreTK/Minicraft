/*********************************************************  
* File: Item.java 
* Purpose: FurnitureItem class implementation, that is 
* items that cannot be attacked.
* ********************************************************/  

package com.mojang.ld22.item;

import com.mojang.ld22.TestLog;
import com.mojang.ld22.entity.Furniture;
import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;

public class FurnitureItem extends Item {
	private Furniture furniture = null;
	private boolean placed = false;
	TestLog logger = new TestLog();
	
	public FurnitureItem(Furniture furniture) {
		this.furniture = furniture;
	}

	public int getColor() {
		return furniture.color;
	}

	public int getSprite() {
		return furniture.sprite + 10 * 32;
	}

	public void renderIcon(Screen screen, int x, int y) {
		screen.render(x, y, getSprite(), getColor(), 0);
	}

	public void renderInventory(Screen screen, int x, int y){
		screen.render(x, y, getSprite(), getColor(), 0);
		Font.draw(furniture.name, screen, x + 8, y, Color.get(-1, 555, 555, 555));
		TestLog.logger.info("The inventory was rendered !");
	}

	public void onTake(ItemEntity itemEntity) {
	}

	public boolean canAttack() {
		TestLog.logger.info("The player can attack ! ");
		return false;
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir){
		if (tile.mayPass(level, xt, yt, furniture)) {
			furniture.positionX = (int)xt * 16 + 8;
			furniture.positionY = (int)yt * 16 + 8;
			level.add(furniture);
			placed = true;
			return true;
		}
		return false;
	}

	public boolean isDepleted() {
		return placed;
	}
	
	public String getName() {
		return furniture.name;
	}

	public Furniture getFurniture() {
		return furniture;
	}

	public void setFurniture(Furniture furniture) {
		this.furniture = furniture;
	}

	public boolean isPlaced() {
		return placed;
	}

	public void setPlaced(boolean placed) {
		this.placed = placed;
	}
}