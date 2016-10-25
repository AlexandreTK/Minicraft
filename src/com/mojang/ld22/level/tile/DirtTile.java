// File: DirtTile.java 
// Objective: take care of Dirt structure use or get.

package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.ItemEntity;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.sound.Sound;

public class DirtTile extends Tile {
	public DirtTile(int id) {
		super(id);
	}

	public void render(Screen screen, Level level, int x, int y) {
		int col = Color.get(level.dirtColor, level.dirtColor, level.dirtColor - 111, level.dirtColor - 111);
		screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
		screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
		screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
		screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
	}

	private boolean haveShovelorHoe(Item item, Player player, Level level, int xt, int yt){
		ToolItem tool = (ToolItem) item;
		if (tool.type == ToolType.shovel) {
			if (player.payStamina(4 - tool.level)) {
				level.setTile(xt, yt, Tile.hole, 0);
				level.add(new ItemEntity(new ResourceItem(Resource.dirt), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
				Sound.monsterHurt.play();
				return true;
			} else{
				//nothing to do
			}
		} else {
			//player don't have a shovel
		}
		if (tool.type == ToolType.hoe) {
			if (player.payStamina(4 - tool.level)) {
				level.setTile(xt, yt, Tile.farmland, 0);
				Sound.monsterHurt.play();
				return true;
			}else {
				//nothing to do
			}
		} else {
			//player don't have a hoe
		}
		return false;
	}
	
	
	
	
	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
		if (item instanceof ToolItem) {
			haveShovelorHoe(item, player, level, xt, yt);
		}
		return false;
	}
}
