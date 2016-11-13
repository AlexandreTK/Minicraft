package com.mojang.ld22.entity;

import java.util.List;
import java.util.Random;

import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;

public class Entity {
	protected final Random random = new Random();

	public void render(Screen screen) {
	}

	public void tick() {
	}

	public boolean isRemoved;

	public void remove() {
		isRemoved = true;
	}

	public Level level;

	public final void init(Level level) {
		this.level = level;
	}

	public int positionX = 0, positionY = 0; // personage position in the plane
												// XY
	public int positionXRelative = 6; // relative personagem postion in X
	public int positionYRelative = 6; // relative personagem postion in Y

	public boolean intersects(int positionX0, int positionY0, int positionX1, int positionY1) {
		return !(positionX + positionXRelative < positionX0 || positionY + positionYRelative < positionY0
				|| positionX - positionXRelative > positionX1 || positionY - positionYRelative > positionY1);
	}

	public boolean blocks(Entity entity) {
		return false;
	}

	public void hurt(Mob mob, int damage, int attackDirection) {
	}

	public void hurt(Tile tile, int positionX, int positionY, int damage) {
	}

	public boolean move(int positionXAbsolute, int positionYAbsolute) {

		if (positionXAbsolute != 0 || positionYAbsolute != 0) {

			boolean isStopped = true;

			if (positionXAbsolute != 0 && move2(positionXAbsolute, 0))
				isStopped = false;

			if (positionYAbsolute != 0 && move2(0, positionYAbsolute))
				isStopped = false;

			if (!isStopped) {
				int positionXWalked = positionX >> 4;
				int positionYWalked = positionY >> 4;
				level.getTile(positionXWalked, positionYWalked).steppedOn(level, positionXWalked, positionYWalked,
						this);
			} else {
				// nothing to do
			}
			return !isStopped;
		} else {
			// nothing to do
		}
		return true;
	}

	public boolean isBlockableBy(Mob mob) {
		return true;
	}

	public void touchItem(ItemEntity itemEntity) {

	}

	public boolean canSwim() {
		return false;
	}

	public boolean interact(Player player, Item item, int attackDir) {
		return item.interact(player, this, attackDir);
	}

	public boolean use(Player player, int attackDir) {
		return false;
	}

	public int getLightRadius() {
		return 0;
	}

	protected boolean move2(int positionXAbsolute, int positionYAbsolute) {

		if (positionXAbsolute != 0 && positionYAbsolute != 0)
			throw new IllegalArgumentException("Move2 can only move along one axis at a time!");

		boolean isBlocked = false;

		int positionXWalked0 = 0;
		int positionYWalked0 = 0;
		int positionXWalked1 = 0;
		int positionYWalked1 = 0;
		positionXWalked0 = ((positionX + positionXAbsolute) - positionXRelative) >> 4;
		positionYWalked0 = ((positionY + positionYAbsolute) - positionYRelative) >> 4;
		positionXWalked1 = ((positionX + positionXAbsolute) + positionXRelative) >> 4;
		positionYWalked1 = ((positionY + positionYAbsolute) + positionYRelative) >> 4;

		int xto0 = 0;
		int yto0 = 0;
		int xto1 = 0;
		int yto1 = 0;
		xto0 = ((positionX) - positionXRelative) >> 4;
		yto0 = ((positionY) - positionYRelative) >> 4;
		xto1 = ((positionX) + positionXRelative) >> 4;
		yto1 = ((positionY) + positionYRelative) >> 4;

		for (int positionYWalked = positionYWalked0; positionYWalked <= positionYWalked1; positionYWalked++)
			for (int positionXWalked = positionXWalked0; positionXWalked <= positionXWalked1; positionXWalked++) {

				if (positionXWalked >= xto0 && positionXWalked <= xto1 && positionYWalked >= yto0
						&& positionYWalked <= yto1)
					continue;

				level.getTile(positionXWalked, positionYWalked).bumpedInto(level, positionXWalked, positionYWalked,
						this);

				if (!level.getTile(positionXWalked, positionYWalked).mayPass(level, positionXWalked, positionYWalked,
						this)) {
					isBlocked = true;
					return false;
				} else {
					// nothing to do
				}
			}
		if (isBlocked)
			return false;

		List<Entity> isInside = level.getEntities(positionX + positionXAbsolute - positionXRelative,
				positionY + positionYAbsolute - positionYRelative, positionX + positionXAbsolute + positionXRelative,
				positionY + positionYAbsolute + positionYRelative);

		for (int i = 0; i < isInside.size(); i++) {
			Entity entity = isInside.get(i);

			if (entity == this)
				continue;

			entity.touchedBy(this);
		}

		List<Entity> wasInside = level.getEntities(positionX - positionXRelative, positionY - positionYRelative,
				positionX + positionXRelative, positionY + positionYRelative);

		isInside.removeAll(wasInside);

		for (int i = 0; i < isInside.size(); i++) {
			Entity entity = isInside.get(i);
			if (entity == this)
				continue;

			if (entity.blocks(this)) {
				return false;
			} else {
				// nothing to do
			}
		}

		positionX += positionXAbsolute;
		positionY += positionYAbsolute;

		return true;
	}

	protected void touchedBy(Entity entity) {

	}
}