package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.sound.Sound;

public class ItemEntity extends Entity {
	private int lifeTime;
	protected int walkedDistancy = 0;
	protected int direction = 0;
	public int hurtTime = 0;
	protected int positionXKnockback, positionYKnockback;
	public double positionXAbsolute;
	public double positionYAbsolute;
	public double positionZAbsolute;
	public double xx, yy, zz;
	public Item item;
	private int time = 0;

	public ItemEntity(Item item, int x, int y) {
		this.item = item;
		xx = this.positionX = x;
		yy = this.positionY = y;
		positionXRelative = 3;
		positionYRelative = 3;

		zz = 2;
		positionXAbsolute = random.nextGaussian() * 0.3;
		positionYAbsolute = random.nextGaussian() * 0.2;
		positionZAbsolute = random.nextFloat() * 0.7 + 1;

		lifeTime = 60 * 10 + random.nextInt(60);
	}

	public void tick() {
		time++;
		if (time >= lifeTime) {
			remove();
			return;
		}
		else {
			// nothing to do
		}
		xx += positionXAbsolute;
		yy += positionYAbsolute;
		zz += positionZAbsolute;
		if (zz < 0) {
			zz = 0;
			positionZAbsolute *= -0.5;
			positionXAbsolute *= 0.6;
			positionYAbsolute *= 0.6;
		}
		else {
			// nothing to do
		}
		positionZAbsolute -= 0.15;
		int ox = positionX;
		int oy = positionY;
		int nx = (int) xx;
		int ny = (int) yy;
		int expectedx = nx - positionX;
		int expectedy = ny - positionY;
		move(nx - positionX, ny - positionY);
		int gotx = positionX - ox;
		int goty = positionY - oy;
		xx += gotx - expectedx;
		yy += goty - expectedy;

		if (hurtTime > 0) hurtTime--;
	}

	public boolean isBlockableBy(Mob mob) {
		return false;
	}

	public void render(Screen screen) {
		if (time >= lifeTime - 6 * 20) {
			if (time / 6 % 2 == 0) return;
		}
		else {
			// nothing to do
		}
		screen.render(positionX - 4, positionY - 4, item.getSprite(), Color.get(-1, 0, 0, 0), 0);
		screen.render(positionX - 4, positionY - 4 - (int) (zz), item.getSprite(), item.getColor(), 0);
	}

	protected void touchedBy(Entity entity) {
		if (time > 30) entity.touchItem(this);
	}

	public void take(Player player) {
		Sound.pickup.play();
		player.score++;
		item.onTake(this);
		remove();
	}
}
