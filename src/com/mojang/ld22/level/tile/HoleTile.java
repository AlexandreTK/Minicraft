package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.Level;

public class HoleTile extends Tile {
	public HoleTile(int id) {
		super(id);
		connectsToSand = true;
		connectsToWater = true;
		connectsToLava= true;
	}

	public void render(Screen screen, Level level, int x, int y) {
		int col = Color.get(111, 111, 110, 110);
		int transitionColor1 = Color.get(3, 111, level.dirtColor - 111, level.dirtColor);
		int transitionColor2 = Color.get(3, 111, level.sandColor - 110, level.sandColor);

		boolean u = !level.getTile(x, y - 1).connectsToLiquid();
		boolean d = !level.getTile(x, y + 1).connectsToLiquid();
		boolean l = !level.getTile(x - 1, y).connectsToLiquid();
		boolean r = !level.getTile(x + 1, y).connectsToLiquid();

		boolean su = u && level.getTile(x, y - 1).connectsToSand;
		boolean sd = d && level.getTile(x, y + 1).connectsToSand;
		boolean sl = l && level.getTile(x - 1, y).connectsToSand;
		boolean sr = r && level.getTile(x + 1, y).connectsToSand;

		if (!u && !l) {
			screen.render(x * 16 + 0, y * 16 + 0, 0, col, 0);
		} else {
			int spriteSheetPosition = 0;
			if(l) {
				spriteSheetPosition+= 14;
			} else {
				spriteSheetPosition+= 15;	
			}
			if(u) {
				spriteSheetPosition+= 0;
			} else {
				spriteSheetPosition+= 1*32;	
			}
			int transitionColor = 0;
			if(su || sl) {
				transitionColor = transitionColor2;
			} else {
				transitionColor = transitionColor1;
			}
			screen.render(x * 16 + 0, y * 16 + 0, spriteSheetPosition, transitionColor, 0);
		}
		if (!u && !r) {
			screen.render(x * 16 + 8, y * 16 + 0, 1, col, 0);
		} else {
			int spriteSheetPosition = 0;
			if(r) {
				spriteSheetPosition+= 16;
			} else {
				spriteSheetPosition+= 15;	
			}
			if(u) {
				spriteSheetPosition+= 0;
			} else {
				spriteSheetPosition+= 1*32;	
			}
			int transitionColor = 0;
			if(su || sr) {
				transitionColor = transitionColor2;
			} else {
				transitionColor = transitionColor1;
			}
			screen.render(x * 16 + 8, y * 16 + 0, spriteSheetPosition, transitionColor, 0);
		}
		if (!d && !l) {
			screen.render(x * 16 + 0, y * 16 + 8, 2, col, 0);
		} else {
			int spriteSheetPosition = 0;
			if(l) {
				spriteSheetPosition+= 14;
			} else {
				spriteSheetPosition+= 15;	
			}
			if(d) {
				spriteSheetPosition+= 2*32;
			} else {
				spriteSheetPosition+= 1*32;	
			}
			int transitionColor = 0;
			if(sd || sl) {
				transitionColor = transitionColor2;
			} else {
				transitionColor = transitionColor1;
			}
			screen.render(x * 16 + 0, y * 16 + 8, spriteSheetPosition, transitionColor, 0);
		}
		if (!d && !r) {
			screen.render(x * 16 + 8, y * 16 + 8, 3, col, 0);
		} else {
			int spriteSheetPosition = 0;
			if(r) {
				spriteSheetPosition+= 16;
			} else {
				spriteSheetPosition+= 15;	
			}
			if(d) {
				spriteSheetPosition+= 2*32;
			} else {
				spriteSheetPosition+= 1*32;	
			}
			int transitionColor = 0;
			if(sd || sr) {
				transitionColor = transitionColor2;
			} else {
				transitionColor = transitionColor1;
			}
			screen.render(x * 16 + 8, y * 16 + 8, spriteSheetPosition, transitionColor, 0);
		}
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		return e.canSwim();
	}

}
