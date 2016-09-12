package com.mojang.ld22.entity;

import java.util.List;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;

public class Spark extends Entity {
	private int lifeTime;
	public double xa, ya;
	public double xx, yy;
	private int time;
	private AirWizard owner;

	public Spark(AirWizard owner, double xa, double ya) {
		this.owner = owner;
		xx = this.positionX = owner.positionX;
		yy = this.positionY = owner.positionY;
		positionXRelative = 0;
		positionYRelative = 0;

		this.xa = xa;
		this.ya = ya;

		lifeTime = 60 * 10 + random.nextInt(30);
	}

	public void tick() {
		time++;
		if (time >= lifeTime) {
			remove();
			return;
		}
		xx += xa;
		yy += ya;
		positionX = (int) xx;
		positionY = (int) yy;
		List<Entity> toHit = level.getEntities(positionX, positionY, positionX, positionY);
		for (int i = 0; i < toHit.size(); i++) {
			Entity e = toHit.get(i);
			if (e instanceof Mob && !(e instanceof AirWizard)) {
				e.hurt(owner, 1, ((Mob) e).dir ^ 1);
			}
		}
	}

	public boolean isBlockableBy(Mob mob) {
		return false;
	}

	public void render(Screen screen) {
		if (time >= lifeTime - 6 * 20) {
			if (time / 6 % 2 == 0) return;
		}

		int xt = 8;
		int yt = 13;

		screen.render(positionX - 4, positionY - 4 - 2, xt + yt * 32, Color.get(-1, 555, 555, 555), random.nextInt(4));
		screen.render(positionX - 4, positionY - 4 + 2, xt + yt * 32, Color.get(-1, 000, 000, 000), random.nextInt(4));
	}
}
