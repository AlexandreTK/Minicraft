/*********************************************************  
* File: ResourceItem.java 
* Purpose: ResourceItem class implementation, that is 
* items are collected as resource.
* ********************************************************/  

package com.mojang.ld22.item;

import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;

public class ResourceItem extends Item {
	private Resource resource = null;
	private int count = 1;

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	public void lessSetCount(int count) {
		this.count -= count;
	}
	public void plusSetCount(int count) {
		this.count += count;
	}

	public ResourceItem(Resource resource) {
		this.resource = resource;
	}

	public ResourceItem(Resource resource, int count) {
		this.resource = resource;
		this.count = count;
	}

	public int getColor() {
		return resource.color;
	}

	public int getSprite() {
		return resource.sprite;
	}

	public void renderIcon(Screen screen, int x, int y) {
		screen.render(x, y, resource.sprite, resource.color, 0);
	}

	public void renderInventory(Screen screen, int x, int y) {
		screen.render(x, y, resource.sprite, resource.color, 0);
		int positionX = x + 32;
		Font.draw(resource.name, screen, positionX, y, Color.get(-1, 555, 555, 555));
		int cc = count;
		
		if (cc <= 999) {
			// Do nothing
		} else {
			cc = 999;
		}
		positionX = x + 8;
		String msg = "" + cc;
		Font.draw(msg, screen, positionX, y, Color.get(-1, 444, 444, 444));
	}

	public String getName() {
		return resource.name;
	}

	public void onTake(ItemEntity itemEntity) {
	}

	public boolean interactOn(Tile tile, Level level, int xt, int yt, Player player, int attackDir) {
		if (resource.interactOn(tile, level, xt, yt, player, attackDir)) {
			count--;
			return true;
		} else {
			
		}
		return false;
	}

	public boolean isDepleted() {
		return count <= 0;
	}

}