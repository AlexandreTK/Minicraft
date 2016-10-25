// File : Level.java
// Objective: this class is responsable for a random map generate.

package com.mojang.ld22.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import com.mojang.ld22.entity.AirWizard;
import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.entity.Mob;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.entity.Slime;
import com.mojang.ld22.entity.Zombie;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.level.levelgen.LevelGen;
import com.mojang.ld22.level.tile.Tile;

public class Level {
	private Random random = new Random();

	public int width, height;

	public byte[] tiles;
	public byte[] data;
	public List<Entity>[] entitiesInTiles;

	public int grassColor = 141;
	public int dirtColor = 322;
	public int sandColor = 550;
	private int depth;
	public byte monsterDensity = 8;

	public List<Entity> entities = new ArrayList<Entity>();
	
	private Comparator<Entity> spriteSorter = new Comparator<Entity>() {
		public int compare(Entity firstEntity, Entity secondEntity) {
			if (secondEntity.positionY < firstEntity.positionY)
				return +1;
			
			if (secondEntity.positionY > firstEntity.positionY)
				return -1;
			
			return 0;
		}

	};

	@SuppressWarnings("unchecked")
	public Level(int width, int height, int level, Level parentLevel) {
		if (level < 0) {
			dirtColor = 222;
		}
		
		this.depth = level;
		this.width = width;
		this.height = height;
		byte[][] maps;

		if (level == 1) {
			dirtColor = 444;
		}
		
		if (level == 0)
			maps = LevelGen.createAndValidateTopMap(width, height);
		else if (level < 0) {
			maps = LevelGen.createAndValidateUndergroundMap(width, height, -level);
			monsterDensity = 4;
		} else {
			maps = LevelGen.createAndValidateSkyMap(width, height); // Sky level
			monsterDensity = 4;
		}

		tiles = maps[0];
		data = maps[1];

		if (parentLevel != null) {
			for (int positionY = 0; positionY < height; positionY++)
				for (int positionX = 0; positionX < width; positionX++) {
					if (parentLevel.getTile(positionX, positionY) == Tile.stairsDown) {

						setTile(positionX, positionY, Tile.stairsUp, 0);
						
						if (level == 0) {
							setTile(positionX - 1, positionY, Tile.hardRock, 0);
							setTile(positionX + 1, positionY, Tile.hardRock, 0);
							setTile(positionX, positionY - 1, Tile.hardRock, 0);
							setTile(positionX, positionY + 1, Tile.hardRock, 0);
							setTile(positionX - 1, positionY - 1, Tile.hardRock, 0);
							setTile(positionX - 1, positionY + 1, Tile.hardRock, 0);
							setTile(positionX + 1, positionY - 1, Tile.hardRock, 0);
							setTile(positionX + 1, positionY + 1, Tile.hardRock, 0);
						} else {
							setTile(positionX - 1, positionY, Tile.dirt, 0);
							setTile(positionX + 1, positionY, Tile.dirt, 0);
							setTile(positionX, positionY - 1, Tile.dirt, 0);
							setTile(positionX, positionY + 1, Tile.dirt, 0);
							setTile(positionX - 1, positionY - 1, Tile.dirt, 0);
							setTile(positionX - 1, positionY + 1, Tile.dirt, 0);
							setTile(positionX + 1, positionY - 1, Tile.dirt, 0);
							setTile(positionX + 1, positionY + 1, Tile.dirt, 0);
						}
					}

				}
		}

		entitiesInTiles = new ArrayList[width * height];
		
		for (int i = 0; i < width * height; i++) {
			entitiesInTiles[i] = new ArrayList<Entity>();
		}
		
		if (level==1) {
			AirWizard airWizard = new AirWizard();
			airWizard.positionX = width*8;
			airWizard.positionY = height*8;
			add(airWizard);
		}
	}

	public void renderBackground(Screen screen, int xScroll, int yScroll) {
		int xo = xScroll >> 4;
		int yo = yScroll >> 4;
		int width = (screen.width + 15) >> 4;
		int height = (screen.height + 15) >> 4;
		
		screen.setOffset(xScroll, yScroll);
		
		for (int positionY = yo; positionY <= height + yo; positionY++) {
			for (int positionX = xo; positionX <= width + xo; positionX++) {
				getTile(positionX, positionY).render(screen, this, positionX, positionY);
			}
		}
		screen.setOffset(0, 0);
	}

	private List<Entity> rowSprites = new ArrayList<Entity>();

	public Player player;

	public void renderSprites(Screen screen, int xScroll, int yScroll) {
		int xo = xScroll >> 4;
		int yo = yScroll >> 4;
		int width = (screen.width + 15) >> 4;
		int height = (screen.height + 15) >> 4;

		screen.setOffset(xScroll, yScroll);
		for (int y = yo; y <= height + yo; y++) {
			for (int x = xo; x <= width + xo; x++) {
				if (x < 0 || y < 0 || x >= this.width || y >= this.height)
					continue;
				
				rowSprites.addAll(entitiesInTiles[x + y * this.width]);
			}
			
			if (rowSprites.size() > 0) {
				sortAndRender(screen, rowSprites);
			}
			rowSprites.clear();
		}
		screen.setOffset(0, 0);
	}

	public void renderLight(Screen screen, int xScroll, int yScroll) {
		int xo = xScroll >> 4;
		int yo = yScroll >> 4;
		int width = (screen.width + 15) >> 4;
		int height = (screen.height + 15) >> 4;

		screen.setOffset(xScroll, yScroll);
		int radius = 4;
		for (int y = yo - radius; y <= height + yo + radius; y++) {
			for (int x = xo - radius; x <= width + xo + radius; x++) {
				
				if (x < 0 || y < 0 || x >= this.width || y >= this.height)
					continue;
				
				List<Entity> entities = entitiesInTiles[x + y * this.width];
				
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = entities.get(i);
					// e.render(screen);
					int lightRadius = entity.getLightRadius();
					
					if (lightRadius > 0)
						screen.renderLight(entity.positionX - 1, entity.positionY - 4, lightRadius * 8);
				}
				
				int lightRadius = getTile(x, y).getLightRadius(this, x, y);
				
				if (lightRadius > 0)
					screen.renderLight(x * 16 + 8, y * 16 + 8, lightRadius * 8);
			}
		}
		screen.setOffset(0, 0);
	}

	// private void renderLight(Screen screen, int x, int y, int r) {
	// screen.renderLight(x, y, r);
	// }

	private void sortAndRender(Screen screen, List<Entity> list) {
		Collections.sort(list, spriteSorter);
		
		for (int i = 0; i < list.size(); i++) {
			list.get(i).render(screen);
		}
	}

	public Tile getTile(int positionX, int positionY) {
		if (positionX < 0 || positionY < 0 || positionX >= width || positionY >= height)
			return Tile.rock;
		
		return Tile.tiles[tiles[positionX + positionY * width]];
	}

	public void setTile(int positionX, int positionY, Tile t, int dataVal) {
		if (positionX < 0 || positionY < 0 || positionX >= width || positionY >= height)
			return;
		tiles[positionX + positionY * width] = t.id;
		data[positionX + positionY * width] = (byte) dataVal;
	}

	public int getData(int positionX, int positionY) {
		if (positionX < 0 || positionY < 0 || positionX >= width || positionY >= height)
			return 0;
		
		return data[positionX + positionY * width] & 0xff;
	}

	public void setData(int positionX, int positionY, int value) {
		if (positionX < 0 || positionY < 0 || positionX >= width || positionY >= height)
			return;
		
		data[positionX + positionY * width] = (byte) value;
	}

	public void add(Entity entity) {
		if (entity instanceof Player) {
			player = (Player) entity;
		}
		
		entity.isRemoved = false;
		entities.add(entity);
		entity.init(this);

		insertEntity(entity.positionX >> 4, entity.positionY >> 4, entity); // insert a player on this position
	}

	public void remove(Entity entity) {
		entities.remove(entity);
		
		int xto = entity.positionX >> 4;
		int yto = entity.positionY >> 4;
		removeEntity(xto, yto, entity);
	}

	private void insertEntity(int positionX, int positionY, Entity entity) {
		if (positionX < 0 || positionY < 0 || positionX >= width || positionY >= height)
			return;
		
		entitiesInTiles[positionX + positionY * width].add(entity);
	}

	private void removeEntity(int positionX, int positionY, Entity entity) {
		if (positionX < 0 || positionY < 0 || positionX >= width || positionY >= height)
			return;
		
		entitiesInTiles[positionX + positionY * width].remove(entity);
	}

	public void trySpawn(int count) {
		for (int i = 0; i < count; i++) {
			Mob mob;

			int minLevel = 1;
			int maxLevel = 1;
		
			if (depth < 0) {
				maxLevel = (-depth) + 1;
			}
			
			if (depth > 0) {
				minLevel = maxLevel = 4;
			}

			int level = random.nextInt(maxLevel - minLevel + 1) + minLevel;
			
			if (random.nextInt(2) == 0)
				mob = new Slime(level); //spawning slimes randomly
			else
				mob = new Zombie(level); //spawning zombies randomly

			if (mob.findStartPos(this)) {
				this.add(mob);
			}
		}
	}

	public void tick() {
		trySpawn(1);

		for (int i = 0; i < width * height / 50; i++) {
			int positionXWalked = random.nextInt(width);
			int positionYWalked = random.nextInt(width);
			getTile(positionXWalked, positionYWalked).tick(this, positionXWalked, positionYWalked);
		}
		
		for (int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			
			int xto = entity.positionX >> 4;
			int yto = entity.positionY >> 4;

			entity.tick();

			if (entity.isRemoved) {
				entities.remove(i--);
				removeEntity(xto, yto, entity);
			} else {
				int positionXWalked = entity.positionX >> 4;
				int positionYWalked = entity.positionY >> 4;

				if (xto != positionXWalked || yto != positionYWalked) {
					removeEntity(xto, yto, entity);
					insertEntity(positionXWalked, positionYWalked, entity);
				}
			}
		}
	}

	public List<Entity> getEntities(int positionX0, int positionY0, int positionX1, int positionY1) {
		List<Entity> result = new ArrayList<Entity>();
		
		int positionXWalked0 = (positionX0 >> 4) - 1;
		int positionYWalked0 = (positionY0 >> 4) - 1;
		int positionXWalked1 = (positionX1 >> 4) + 1;
		int positionYWalked1 = (positionY1 >> 4) + 1;
		
		for (int positionY = positionYWalked0; positionY <= positionYWalked1; positionY++) {
			for (int positionX = positionXWalked0; positionX <= positionXWalked1; positionX++) {
				
				if (positionX < 0 || positionY < 0 || positionX >= width || positionY >= height)
					continue;
				
				List<Entity> entities = entitiesInTiles[positionX + positionY * this.width];
				
				for (int i = 0; i < entities.size(); i++) {
					Entity entity = entities.get(i);
					if (entity.intersects(positionX0, positionY0, positionX1, positionY1))
						result.add(entity);
				}
			}
		}
		return result;
	}
}