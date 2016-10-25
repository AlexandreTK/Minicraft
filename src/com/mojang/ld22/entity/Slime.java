package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;

public class Slime extends Mob {
	private int positionXAbsolute, positionYAbsolute; //moment position of slime
	private int jumpTime = 0; //delay of slime move
	private int lvl;

	public Slime(int lvl) {
		this.lvl = lvl;
		positionX = random.nextInt(64 * 16);
		positionY = random.nextInt(64 * 16);
		health = maxHealth = lvl * lvl * 5;
	}

	public void tick() {
		super.tick();

		int speed = 1;
		if (!move(positionXAbsolute * speed, positionYAbsolute * speed) || random.nextInt(40) == 0) {
			if (jumpTime <= -10) {
				positionXAbsolute = (random.nextInt(3) - 1);
				positionYAbsolute = (random.nextInt(3) - 1);

				if (level.player != null) {
					int positionXWalked = level.player.positionX - positionX;
					int positionYWalked = level.player.positionY - positionY;
					
					if (positionXWalked * positionXWalked + positionYWalked * positionYWalked < 50 * 50) {
						if (positionXWalked < 0){
							positionXAbsolute = -1;}
						
						if (positionXWalked > 0){
							positionXAbsolute = +1;}
						
						if (positionYWalked < 0){
							positionYAbsolute = -1;}
						if (positionYWalked > 0){
							positionYAbsolute = +1;}
					}else{
						//nothing to do
					}

				}else{
					//nothing to do
				}

				if (positionXAbsolute != 0 || positionYAbsolute != 0) jumpTime = 10;
			}else{
				//nothing to do
			}
		}else{
			//nothing to do
		}

		jumpTime--;
		
		if (jumpTime == 0) {
			positionXAbsolute = positionYAbsolute = 0;
		}else{
			//nothing to do
		}
	}

	protected void die() {
		super.die();

		int count = random.nextInt(2) + 1;
		for (int i = 0; i < count; i++) {
			level.add(new ItemEntity(new ResourceItem(Resource.slime), positionX + random.nextInt(11) - 5, positionY + random.nextInt(11) - 5));
		}

		if (level.player != null) {
			level.player.score += 25*lvl;
		}else{
			//nothing to do
		}
		
	}

	public void render(Screen screen) {
		int xt = 0;
		int yt = 18;

		int xo = positionX - 8;
		int yo = positionY - 11;

		if (jumpTime > 0) {
			xt += 2;
			yo -= 4;
		}else{
			//nothing to do
		}

		int col = Color.get(-1, 10, 252, 555);
		switch(lvl){
		case 2:
			col = Color.get(-1, 100, 522, 555);
			break;
		case 3:
			col = Color.get(-1, 111, 444, 555);
			break;
		case 4:
			col = Color.get(-1, 000, 111, 224);
			break;
		}
				

		if (hurtTime > 0) {
			col = Color.get(-1, 555, 555, 555);
		}else{
			//nothing to do
		}

		screen.render(xo + 0, yo + 0, xt + yt * 32, col, 0);
		screen.render(xo + 8, yo + 0, xt + 1 + yt * 32, col, 0);
		screen.render(xo + 0, yo + 8, xt + (yt + 1) * 32, col, 0);
		screen.render(xo + 8, yo + 8, xt + 1 + (yt + 1) * 32, col, 0);
	}

	protected void touchedBy(Entity entity) {
		if (entity instanceof Player) {
			entity.hurt(this, lvl, direction);
		}else{
			//nothing to do
		}
	}
}