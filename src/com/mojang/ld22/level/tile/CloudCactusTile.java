package com.mojang.ld22.level.tile;

import com.mojang.ld22.entity.AirWizard;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.particle.SmashParticle;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.level.Level;

public class CloudCactusTile extends Tile {
	public CloudCactusTile(int id) {
		super(id);
	}

	public void render(Screen screen, Level level, int x, int y) {
		int color = Color.get(444, 111, 333, 555);
		int xRender, yRender, colorRender, colorRender2;
		xRender = (x * 16) + 0;
		yRender = (y * 16) + 0;
		colorRender = 17 + (1 * 32);

		screen.render(xRender, yRender, colorRender, color, 0);
		screen.render(xRender + 8, yRender, colorRender + 1, color, 0);

		colorRender2 = 17 + 2 * 32;
		screen.render(xRender, yRender + 8, colorRender2, color, 0);
		screen.render(xRender + 8, yRender + 8, colorRender2 + 1, color, 0);
	}

	public boolean mayPass(Level level, int x, int y, Entity e) {
		if (e instanceof AirWizard)
			return true;
		return false;
	}

	public void hurt(Level level, int x, int y, Mob source, int dmg, int attackDir) {
		
		try {
			hurt(level, x, y, 0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean interact(Level level, int xt, int yt, Player player, Item item, int attackDir) {
			if (item instanceof ToolItem) {
			ToolItem tool = (ToolItem) item;
			if (tool.type == ToolType.pickaxe) {
				if (player.payStamina(6 - tool.level)) {
					try {
						hurt(level, xt, yt, 1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return true;
				} else {
					// nothing to do
				}
			} else {
				// player don't have the pickaxe
			}
		} else {
			// no toolItem on object
		}
		return false;
	}

	public void hurt(Level level, int x, int y, int dmg) throws Exception {
		int damage;
		if(level == null){
			throw new Exception("Null item");
				}else {
							//NOTHINHG TO DO
						 }
		
		damage = level.getData(x, y) + 1;
		int xAux, yAux;
		xAux = (x * 16) + 8;
		yAux = (y * 16) + 8;

		level.add(new SmashParticle(xAux, yAux));
		level.add(new TextParticle("" + dmg, xAux, yAux, Color.get(-1, 500, 500, 500)));
		if (dmg > 0) {
			if (damage >= 10) {
				level.setTile(x, y, Tile.cloud, 0);
			} else {
				level.setData(x, y, damage);
			}
		} else {
			// nothing to do.
		}
	}

	public void bumpedInto(Level level, int x, int y, Entity entity) {
		if (entity instanceof AirWizard) {
			return;
		}
		entity.hurt(this, x, y, 3);
	}
}