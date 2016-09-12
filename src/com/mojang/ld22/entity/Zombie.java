package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;

public class Zombie extends Mob {
	private int xa, ya;
	private int lvl;
	private int randomWalkTime = 0;

	public Zombie(int lvl) {
		this.lvl = lvl;
		positionX = random.nextInt(64 * 16);
		positionY = random.nextInt(64 * 16);
		health = maxHealth = lvl * lvl * 10;

	}

	public void tick() {
		super.tick();

		if (level.player != null && randomWalkTime == 0) {
			int xd = level.player.positionX - positionX;
			int yd = level.player.positionY - positionY;
			if (xd * xd + yd * yd < 50 * 50) {
				xa = 0;
				ya = 0;
				if (xd < 0) xa = -1;
				if (xd > 0) xa = +1;
				if (yd < 0) ya = -1;
				if (yd > 0) ya = +1;
			}
		}

		int speed = tickTime & 1;
		if (!move(xa * speed, ya * speed) || random.nextInt(200) == 0) {
			randomWalkTime = 60;
			xa = (random.nextInt(3) - 1) * random.nextInt(2);
			ya = (random.nextInt(3) - 1) * random.nextInt(2);
		}
		if (randomWalkTime > 0) randomWalkTime--;
	}

	public void render(Screen screen) {
		int xt = 0;
		int yt = 14;

		int flip1 = (walkedDistancy >> 3) & 1;
		int flip2 = (walkedDistancy >> 3) & 1;

		if (direction == 1) {
			xt += 2;
		}
		if (direction > 1) {

			flip1 = 0;
			flip2 = ((walkedDistancy >> 4) & 1);
			if (direction == 2) {
				flip1 = 1;
			}
			xt += 4 + ((walkedDistancy >> 3) & 1) * 2;
		}

		int xo = positionX - 8;
		int yo = positionY - 11;

		int col = Color.get(-1, 10, 252, 050);
		if (lvl == 2) col = Color.get(-1, 100, 522, 050);
		if (lvl == 3) col = Color.get(-1, 111, 444, 050);
		if (lvl == 4) col = Color.get(-1, 000, 111, 020);
		if (hurtTime > 0) {
			col = Color.get(-1, 555, 555, 555);
		}

		screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
		screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
		screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
		screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);
	}

	protected void touchedBy(Entity entity) {
		if (entity instanceof Player) {
			entity.hurt(this, lvl + 1, direction);
		}
	}

	protected void die() {
		super.die();

		int count = random.nextInt(2) + 1;
		for (int i = 0; i < count; i++) {
			level.add(new ItemEntity(new ResourceItem(Resource.cloth), positionX + random.nextInt(11) - 5, positionY + random.nextInt(11) - 5));
		}

		if (level.player != null) {
			level.player.score += 50 * lvl;
		}

	}

}