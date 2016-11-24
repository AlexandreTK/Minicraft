package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
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

public class CloudTile extends Tile {
	public CloudTile(int id) {
		super(id);
	}

	public void render(Screen screen, Level level, int x, int y) {
		int col = Color.get(444, 444, 555, 555);
		int transitionColor = Color.get(333, 444, 555, -1);

		boolean u = level.getTile(x, y - 1) == Tile.infiniteFall;
		boolean d = level.getTile(x, y + 1) == Tile.infiniteFall;
		boolean l = level.getTile(x - 1, y) == Tile.infiniteFall;
		boolean r = level.getTile(x + 1, y) == Tile.infiniteFall;

		boolean ul = level.getTile(x - 1, y - 1) == Tile.infiniteFall;
		boolean dl = level.getTile(x - 1, y + 1) == Tile.infiniteFall;
		boolean ur = level.getTile(x + 1, y - 1) == Tile.infiniteFall;
		boolean dr = level.getTile(x + 1, y + 1) == Tile.infiniteFall;
		int xAux, yAux;
		xAux = x * 16 + 0;
		yAux = y * 16 + 0;

		if (!u && !l) {
			if (!ul)
				screen.render(xAux, yAux, 17, col, 0);
			else
				screen.render(xAux, yAux, 7 + 0 * 32, transitionColor, 3);
		} else
				
			screen.render(xAux, yAux, lReturn(l) + uReturn(u) * 32, transitionColor, 3);
		

		if (!u && !r) {
			if (!ur)
				screen.render(xAux + 8, yAux, 18, col, 0);
			else
				screen.render(xAux + 8, yAux, 8 + 0 * 32, transitionColor, 3);
		} else
			screen.render(xAux + 8, yAux,rReturn(r) + uReturn(u) * 32, transitionColor, 3);

		if (!d && !l) {
			if (!dl)
				screen.render(xAux, yAux+ 8, 20, col, 0);
			else
				screen.render(xAux, yAux+ 8, 7 + 1 * 32, transitionColor, 3);
		} else
			screen.render(xAux, yAux+ 8, lReturn(l) + dReturn(d) * 32, transitionColor, 3);
		if (!d && !r) {
			if (!dr)
				screen.render(xAux + 8 , yAux+ 8, 19, col, 0);
			else
				screen.render(xAux + 8, yAux+ 8, 8 + 1 * 32, transitionColor, 3);
		} else
			screen.render(xAux + 8, yAux+ 8,rReturn(r) + dReturn(d) * 32, transitionColor, 3);
	} 
	public int lReturn(boolean l){
		if (l = true){
			return 6;
		}
		else {
			return 5;
		}
	}
	public int uReturn(boolean u){
		if (u = true){
			return 2;
		}
		else {
			return 1;
		}
	}
	public int rReturn(boolean r){
		if (r = true){
			return 4;
		}
		else {
			return 5;
		}
	}
	public int dReturn(boolean d){
		if (d = true){
			return 0;
		}
		else {
			return 1;
		}
	}
	
	

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return true;
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) throws Exception {
		if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if(tool == null){
			throw new Exception("Null item");
			}else {
	 				//NOTHINHG TO DO
		 			 }
			if (tool.type == ToolType.shovel) {
				if (player.payStamina(5)) {
					// level.setTile(xt, yt, Tile.infiniteFall, 0);
					int count = random.nextInt(2) + 1;
					for (int i = 0; i < count; i++) {
						level.add(new ItemEntity(new ResourceItem(Resource.cloud), xt * 16 + random.nextInt(10) + 3, yt * 16 + random.nextInt(10) + 3));
					}
					return true;
				}
			}
		}
		return false;
	}

	/*
	 * public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) { if (item instanceof ToolItem) { ToolItem tool = (ToolItem) item; if (tool.type == ToolType.pickaxe) { if (player.payStamina(4 - tool.level)) { hurt(level, xt, yt, random.nextInt(10) + (tool.level) * 5 + 10); return true; } } } return false; }
	 */
}
