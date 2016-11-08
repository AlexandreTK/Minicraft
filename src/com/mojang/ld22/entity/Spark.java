package com.mojang.ld22.entity;

import java.util.List;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class Spark extends Entity {
	private int lifeTime = 0;
	public double positionXAbsolute = 0, positionYAbsolute = 0;
	public double xx = 0, yy = 0;
	private int time = 0;
	private AirWizard owner;

	public Spark(AirWizard owner, double positionXAbsolute, double positionYAbsolute) {
		this.owner = owner;
		xx = this.positionX = owner.positionX;
		yy = this.positionY = owner.positionY;
		positionXRelative = 0;
		positionYRelative = 0;

		this.positionXAbsolute = positionXAbsolute;
		this.positionYAbsolute = positionYAbsolute;

		lifeTime = 60 * 10 + random.nextInt(30);
	}

	public void tick() {
		time++;

		if (time >= lifeTime) {
			remove();
			return;
		} else {
			// nothing to do
		}

		xx += positionXAbsolute;
		yy += positionYAbsolute;
		positionX = (int) xx;
		positionY = (int) yy;

		List<Entity> toHit = level.getEntities(positionX, positionY, positionX, positionY);

		for (int i = 0; i < toHit.size(); i++) {
			Entity entity = toHit.get(i);
			if (entity instanceof Mob && !(entity instanceof AirWizard)) {
				entity.hurt(owner, 1, ((Mob) entity).direction ^ 1);
			} else {
				// nothing to do
			}
		}
	}

	public boolean isBlockableBy(Mob mob) {
		return false;
	}

	public void render(Screen screen) {
		if (time >= lifeTime - 6 * 20) {
			if (time / 6 % 2 == 0)
				return;
		} else {
			// nothing to do
		}

		int xt = 8;
		int yt = 13;

		screen.render(positionX - 4, positionY - 4 - 2, xt + yt * 32, Color.get(-1, 555, 555, 555), random.nextInt(4));
		screen.render(positionX - 4, positionY - 4 + 2, xt + yt * 32, Color.get(-1, 000, 000, 000), random.nextInt(4));
	}
}
