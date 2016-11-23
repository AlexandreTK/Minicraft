package com.mojang.ld22.entity;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.sound.Sound;

public class AirWizard extends Mob {

	private int positionXAbsolute = 0;
	private int positionYAbsolute = 0;
	private int randomWalkTime = 0; 
	private int attackDelay = 0;  // time that the player need to wait for attack again
	private int attackTime = 0; //time that the person are attacking something
	private int attackType = 0; // the type of attack, or normal ,or with some weapon 

	public AirWizard() {
		positionX = random.nextInt(64 * 16);
		positionY = random.nextInt(64 * 16);
		health = maxHealth = 2000;
	}

	public void tick() {
		super.tick();

		if (attackDelay > 0) {
			direction = (attackDelay - 45) / 4 % 4;
			direction = (direction * 2 % 4) + (direction / 2);

			if (attackDelay < 45) {
				direction = 0;
			} else {
				// nothing to do
			}

			attackDelay--;

			if (attackDelay == 0) {
				attackType = 0;
				if (health < 1000)
					attackType = 1;

				if (health < 200)
					attackType = 2;
				attackTime = 60 * 2;
			} else {
				// nothing to do
			}

		} else {
			// nothing to do
		}

		if (attackTime > 0) {
			attackTime--;
			double direction_angle = attackTime * 0.25;
			double direction_coordinate = (attackTime % 2 * 2 - 1); // will be
																	// +1 or -1
			double direction = direction_angle * direction_coordinate;
			double speed = (0.7) + attackType * 0.2;

			level.add(new Spark(this, Math.cos(direction) * speed, Math.sin(direction) * speed));

		} else {
			// nothing to do
		}

		if (level.player != null && randomWalkTime == 0) {
			int positionXWalked = level.player.positionX - positionX;
			int positionYWalked = level.player.positionY - positionY;

			if (positionXWalked * positionXWalked + positionYWalked * positionYWalked < 32 * 32) {
				positionXAbsolute = 0;
				positionYAbsolute = 0;

				if (positionXWalked < 0) {
					positionXAbsolute = +1;
				}
				if (positionXWalked > 0) {
					positionXAbsolute = -1;

				}
				if (positionYWalked < 0) {
					positionYAbsolute = +1;
				}
				if (positionYWalked > 0) {
					positionYAbsolute = -1;
				}

			} else if (positionXWalked * positionXWalked + positionYWalked * positionYWalked > 80 * 80) {
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

		int speed = 0;
		if ((tickTime % 4) == 0) {
			speed = 0;
		} else {
			speed = 1;
		}

		if (!move(positionXAbsolute * speed, positionYAbsolute * speed) || random.nextInt(100) == 0) {
			randomWalkTime = 30;
			positionXAbsolute = (random.nextInt(3) - 1);
			positionYAbsolute = (random.nextInt(3) - 1);
		} else {
			// nothing to do
		}

		if (randomWalkTime > 0) {
			randomWalkTime--;
			if (level.player != null && randomWalkTime == 0) {
				int positionXWalked = level.player.positionX - positionX;
				int positionYWalked = level.player.positionY - positionY;
				int distanceWalked = positionXWalked * positionXWalked + positionYWalked * positionYWalked;
				if (random.nextInt(4) == 0 && distanceWalked < 50 * 50) {
					if (attackDelay == 0 && attackTime == 0) {
						attackDelay = 60 * 2;
					} else {
						// nothing to do
					}
				} else {
					// nothing to do
				}
			} else {
				// nothing to do
			}
		} else {
			// nothing to do
		}
	}

/**
 * this class is about the effects in the screen
 * like personage movement, distancy , color
 */

	public void render(Screen screen) {
		int xt = 0;
		int yt = 0;
		xt = 8;
		yt = 14;

		if (direction == 1) {
			xt += 2;
		} else {
			// nothing to do
		}

		int flip1 = (walkedDistancy >> 3) & 1;
		int flip2 = (walkedDistancy >> 3) & 1;

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

		int color1 = Color.get(-1, 100, 500, 555);
		int color2 = Color.get(-1, 100, 500, 532);

		if (health < 200) {
			if (tickTime / 3 % 2 == 0) {
				color1 = Color.get(-1, 500, 100, 555);
				color2 = Color.get(-1, 500, 100, 532);
			} else {
				// nothing to do
			}
		} else if (health < 1000) {
			if (tickTime / 5 % 4 == 0) {
				color1 = Color.get(-1, 500, 100, 555);
				color2 = Color.get(-1, 500, 100, 532);
			} else {
				// nothing to do
			}
		} else {
			// nothing to do
		}
		if (hurtTime > 0) {
			color1 = Color.get(-1, 555, 555, 555);
			color2 = Color.get(-1, 555, 555, 555);
		} else {
			// nothing to do
		}

		int positionX0 = 0;
		int positionY0 = 0;
		positionX0 = positionX - 8;
		positionY0 = positionY - 11;

		screen.render(positionX0 + 8 * flip1, positionY0 + 0, xt + yt * 32, color1, flip1);
		screen.render(positionX0 + 8 - 8 * flip1, positionY0 + 0, xt + 1 + yt * 32, color1, flip1);
		screen.render(positionX0 + 8 * flip2, positionY0 + 8, xt + (yt + 1) * 32, color2, flip2);
		screen.render(positionX0 + 8 - 8 * flip2, positionY0 + 8, xt + 1 + (yt + 1) * 32, color2, flip2);
	}

	protected void touchedBy(Entity entity) {
		if (entity instanceof Player) {
			entity.hurt(this, 3, direction);
		} else {
			// nothing to do
		}
	}

	protected void die() {
		super.die();

		if (level.player != null) {
			level.player.score += 1000;
			level.player.gameWon();
		} else {
			// nothing to do
		}

		Sound.bossdeath.play();
	}

	protected void doHurt(int damage, int attackDirection) {
		super.doHurt(damage, attackDirection);
		if (attackDelay == 0 && attackTime == 0) {
			attackDelay = 60 * 2;
		} else {
			// nothing to do
		}
	}
}