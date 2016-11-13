package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.sound.Sound;

public class ItemEntity extends Entity {
	private int lifeTime = 0; // time that player are alive
	protected int walkedDistancy = 0; //distance that player walked
	protected int direction = 0; //direction that player walked
	public int hurtTime = 0; // time that player need for recover of some type of hurt
	protected int positionXKnockback = 0, positionYKnockback = 0;
	public double positionXAbsolute = 0.0;
	public double positionYAbsolute = 0.0;
	public double positionZAbsolute = 0.0;
	public double xx = 0.0, yy = 0.0, zz = 0.0;
	public Item item;
	private int time = 0; //time

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
