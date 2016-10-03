package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;

public class Zombie extends Mob {
	private int positionXAbsolute, positionYAbsolute;
	private int lvl;
	private int randomWalkTime = 0;

	public Zombie(int level) {
		this.lvl = level;
		positionX = random.nextInt(64 * 16);
		positionY = random.nextInt(64 * 16);
		health = maxHealth = level * level * 10;

	}

	public void tick() {
		super.tick();

		if (level.player != null && randomWalkTime == 0) {
			int positionXWalked = level.player.positionX - positionX;
			int positionYWalked = level.player.positionY - positionY;

			if (positionXWalked * positionXWalked + positionYWalked * positionYWalked < 50 * 50) {
				positionXAbsolute = 0;
				positionYAbsolute = 0;

				if (positionXWalked < 0) {
					positionXAbsolute = -1;
				}

				if (positionXWalked > 0) {
					positionXAbsolute = +1;
				}

				if (positionYWalked < 0) {
					positionYAbsolute = -1;
				}

				if (positionYWalked > 0) {
					positionYAbsolute = +1;
				}
			} else {
				// nothing to do
			}
		} else {
			// nothing to do
		}

		int speed = tickTime & 1;

		if (!move(positionXAbsolute * speed, positionYAbsolute * speed) || random.nextInt(200) == 0) {
			randomWalkTime = 60;
			positionXAbsolute = (random.nextInt(3) - 1) * random.nextInt(2);
			positionYAbsolute = (random.nextInt(3) - 1) * random.nextInt(2);
		} else {
			// nothing to do
		}

		if (randomWalkTime > 0)
			randomWalkTime--;
	}

	public void render(Screen screen) {
		int xt = 0;
		int yt = 14;

		int flip1 = (walkedDistancy >> 3) & 1;
		int flip2 = (walkedDistancy >> 3) & 1;

		if (direction == 1) {
			xt += 2;
		} else {
			// nothing to do
		}
		if (direction > 1) {

			flip1 = 0;
			flip2 = ((walkedDistancy >> 4) & 1);

			if (direction == 2) {
				flip1 = 1;
			}

			xt += 4 + ((walkedDistancy >> 3) & 1) * 2;
		} else {
			// nothing to do
		}

		int xo = positionX - 8;
		int yo = positionY - 11;

		int col = Color.get(-1, 10, 252, 050);

		switch (lvl) {
		case 2:
			col = Color.get(-1, 100, 522, 050);
			break;
		case 3:
			col = Color.get(-1, 111, 444, 050);
			break;
		case 4:
			col = Color.get(-1, 000, 111, 020);
			break;
		}

		if (hurtTime > 0) {
			col = Color.get(-1, 555, 555, 555);
		} else {
			// nothing to do
		}

		screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
		screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
		screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
		screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);
	}

	protected void touchedBy(Entity entity) {
		if (entity instanceof Player) {
			entity.hurt(this, lvl + 1, direction);
		} else {
			// nothing to do
		}
	}

	protected void die() {
		super.die();

		int count = random.nextInt(2) + 1;

		for (int i = 0; i < count; i++) {
			level.add(new ItemEntity(new ResourceItem(Resource.cloth), positionX + random.nextInt(11) - 5,
					positionY + random.nextInt(11) - 5));
		}

		if (level.player != null) {
			level.player.score += 50 * lvl;
		} else {
			// nothing to do
		}

	}

}