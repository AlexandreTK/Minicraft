package com.mojang.ld22.entity;

import java.util.List;

import com.mojang.ld22.Game;
import com.mojang.ld22.InputHandler;
import com.mojang.ld22.entity.particle.TextParticle;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.FurnitureItem;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.PowerGloveItem;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.ToolItem;
import com.mojang.ld22.item.ToolType;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.screen.InventoryMenu;
import com.mojang.ld22.sound.Sound;

public class Player extends Mob {
	private InputHandler input;
	private int attackTime, attackDir;

	public Game game;
	public Inventory inventory = new Inventory(); //load inventory for player
	public Item attackItem;
	public Item activeItem; //item in the hand of the player
	public int stamina;
	public int staminaRecharge; //time to reload the stamina
	public int staminaRechargeDelay;
	public int score; 
	public int maxStamina; //max energy of the player
	private int onStairDelay;
	public int invulnerableTime = 0;

	public Player(Game game, InputHandler input) {
		this.game = game;
		this.input = input;
		positionX = 24;
		positionY = 24;
		stamina = maxStamina;

		inventory.add(new FurnitureItem(new Workbench()));
		inventory.add(new PowerGloveItem());
	}

	public void tick() {
		super.tick();

		if (invulnerableTime > 0) invulnerableTime--;
		Tile onTile = level.getTile(positionX >> 4, positionY >> 4);
		if (onTile == Tile.stairsDown || onTile == Tile.stairsUp) {
			if (onStairDelay == 0) {
				changeLevel((onTile == Tile.stairsUp) ? 1 : -1);
				onStairDelay = 10;
				return;
			}else{
				//nothing to do
			}
			onStairDelay = 10;
		} else {
			if (onStairDelay > 0) onStairDelay--;
		}

		if (stamina <= 0 && staminaRechargeDelay == 0 && staminaRecharge == 0) {
			staminaRechargeDelay = 40;
		}else{
			//nothing to do
		}

		if (staminaRechargeDelay > 0) {
			staminaRechargeDelay--;
		}else{
			//nothing to do
		}

		if (staminaRechargeDelay == 0) {
			staminaRecharge++;
			if (isSwimming()) {
				staminaRecharge = 0;
			}
			while (staminaRecharge > 10) {
				staminaRecharge -= 10;
				if (stamina < maxStamina) stamina++;
			}
		}else{
			//nothing to do
		}

		int xa = 0;
		int ya = 0;
		if (input.up.isKeyDown()) ya--;
		if (input.down.isKeyDown()) ya++;
		if (input.left.isKeyDown()) xa--;
		if (input.right.isKeyDown()) xa++;
		if (isSwimming() && tickTime % 60 == 0) {
			if (stamina > 0) {
				stamina--;
			} else {
				hurt(this, 1, direction ^ 1);
			}
		}else{
			//nothing to do
		}

		if (staminaRechargeDelay % 2 == 0) {
			move(xa, ya);
		}else{
			//nothing to do
		}

		if (input.attack.wasKeyClicked()) {
			if (stamina == 0) {

			} else {
				stamina--;
				staminaRecharge = 0;
				attack();
			}
		}
		if (input.menu.wasKeyClicked()) {
			if (!use()) {
				game.setMenu(new InventoryMenu(this));
			}else{
				//nothing to do
			}
		}
		if (attackTime > 0) attackTime--;

	}

	private boolean use() {
		int yo = -2;
		if (direction == 0 && use(positionX - 8, positionY + 4 + yo, positionX + 8, positionY + 12 + yo)) return true;
		if (direction == 1 && use(positionX - 8, positionY - 12 + yo, positionX + 8, positionY - 4 + yo)) return true;
		if (direction == 3 && use(positionX + 4, positionY - 8 + yo, positionX + 12, positionY + 8 + yo)) return true;
		if (direction == 2 && use(positionX - 12, positionY - 8 + yo, positionX - 4, positionY + 8 + yo)) return true;

		int xt = positionX >> 4;
		int yt = (positionY + yo) >> 4;
		int r = 12;
		if (attackDir == 0) yt = (positionY + r + yo) >> 4;
		if (attackDir == 1) yt = (positionY - r + yo) >> 4;
		if (attackDir == 2) xt = (positionX - r) >> 4;
		if (attackDir == 3) xt = (positionX + r) >> 4;

		if (xt >= 0 && yt >= 0 && xt < level.width && yt < level.height) {
			if (level.getTile(xt, yt).use(level, xt, yt, this, attackDir)) return true;
		}else{
			//nothing to do
		}

		return false;
	}

	private void attack() {
		walkedDistancy += 8;
		attackDir = direction;
		attackItem = activeItem;
		boolean done = false;

		if (activeItem != null) {
			attackTime = 10;
			int yo = -2;
			int range = 12;
			if (direction == 0 && interact(positionX - 8, positionY + 4 + yo, positionX + 8, positionY + range + yo)) done = true;
			if (direction == 1 && interact(positionX - 8, positionY - range + yo, positionX + 8, positionY - 4 + yo)) done = true;
			if (direction == 3 && interact(positionX + 4, positionY - 8 + yo, positionX + range, positionY + 8 + yo)) done = true;
			if (direction == 2 && interact(positionX - range, positionY - 8 + yo, positionX - 4, positionY + 8 + yo)) done = true;
			if (done) return;

			int xt = positionX >> 4;
			int yt = (positionY + yo) >> 4;
			int r = 12;
			if (attackDir == 0) yt = (positionY + r + yo) >> 4;
			if (attackDir == 1) yt = (positionY - r + yo) >> 4;
			if (attackDir == 2) xt = (positionX - r) >> 4;
			if (attackDir == 3) xt = (positionX + r) >> 4;

			if (xt >= 0 && yt >= 0 && xt < level.width && yt < level.height) {
				if (activeItem.interactOn(level.getTile(xt, yt), level, xt, yt, this, attackDir)) {
					done = true;
				} else {
					if (level.getTile(xt, yt).interact(level, xt, yt, this, activeItem, attackDir)) {
						done = true;
					}else{
						//nothing to do
					}
				}
				if (activeItem.isDepleted()) {
					activeItem = null;
				}else{
					//nothing to do
				}
			}
		}

		if (done) return;

		if (activeItem == null || activeItem.canAttack()) {
			attackTime = 5;
			int yo = -2;
			int range = 20;
			if (direction == 0) hurt(positionX - 8, positionY + 4 + yo, positionX + 8, positionY + range + yo);
			if (direction == 1) hurt(positionX - 8, positionY - range + yo, positionX + 8, positionY - 4 + yo);
			if (direction == 3) hurt(positionX + 4, positionY - 8 + yo, positionX + range, positionY + 8 + yo);
			if (direction == 2) hurt(positionX - range, positionY - 8 + yo, positionX - 4, positionY + 8 + yo);

			int xt = positionX >> 4;
			int yt = (positionY + yo) >> 4;
			int r = 12;
			if (attackDir == 0) yt = (positionY + r + yo) >> 4;
			if (attackDir == 1) yt = (positionY - r + yo) >> 4;
			if (attackDir == 2) xt = (positionX - r) >> 4;
			if (attackDir == 3) xt = (positionX + r) >> 4;

			if (xt >= 0 && yt >= 0 && xt < level.width && yt < level.height) {
				level.getTile(xt, yt).hurt(level, xt, yt, this, random.nextInt(3) + 1, attackDir);
			}else{
				//nothing to do
			}
		}

	}

	private boolean use(int x0, int y0, int x1, int y1) {
		List<Entity> entities = level.getEntities(x0, y0, x1, y1);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this) if (e.use(this, attackDir)) return true;
		}
		return false;
	}

	private boolean interact(int x0, int y0, int x1, int y1) {
		List<Entity> entities = level.getEntities(x0, y0, x1, y1);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this) if (e.interact(this, activeItem, attackDir)) return true;
		}
		return false;
	}

	private void hurt(int x0, int y0, int x1, int y1) {
		List<Entity> entities = level.getEntities(x0, y0, x1, y1);
		for (int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			if (e != this) e.hurt(this, getAttackDamage(e), attackDir);
		}
	}

	private int getAttackDamage(Entity e) {
		int dmg = random.nextInt(3) + 1;
		if (attackItem != null) {
			dmg += attackItem.getAttackDamageBonus(e);
		}else{
			//nothing to do
		}
		return dmg;
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
		if (isSwimming()) {
			yo += 4;
			int waterColor = Color.get(-1, -1, 115, 335);
			if (tickTime / 8 % 2 == 0) {
				waterColor = Color.get(-1, 335, 5, 115);
			}else{
				//nothing to do
			}
			screen.render(xo + 0, yo + 3, 5 + 13 * 32, waterColor, 0);
			screen.render(xo + 8, yo + 3, 5 + 13 * 32, waterColor, 1);
		}else{
			//nothing to do
		}
		if (attackTime> 0){
			switch(attackDir){
			case 0:
				screen.render(xo + 0, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 2);
				screen.render(xo + 8, yo + 8 + 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 3);
				if (attackItem != null) {
					attackItem.renderIcon(screen, xo + 4, yo + 8 + 4);
				}else{
					//nothing to do
				}
				
				break;
			case 1:
				screen.render(xo + 0, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
				screen.render(xo + 8, yo - 4, 6 + 13 * 32, Color.get(-1, 555, 555, 555), 1);
				if (attackItem != null) {
					attackItem.renderIcon(screen, xo + 4, yo - 4);
				}else{
					//nothing to do
				}
				
				break;
			case 2:
				screen.render(xo - 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 1);
				screen.render(xo - 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 3);
				if (attackItem != null) {
					attackItem.renderIcon(screen, xo - 4, yo + 4);
				}else{
					//nothing to do
				}
				
				break;
			case 3:
				screen.render(xo + 8 + 4, yo, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 0);
				screen.render(xo + 8 + 4, yo + 8, 7 + 13 * 32, Color.get(-1, 555, 555, 555), 2);
				if (attackItem != null) {
					attackItem.renderIcon(screen, xo + 8 + 4, yo + 4);
				}else{
					//nothing to do
				}
				break;
			}
		}else{
			//nothing to do
		}

		
		int col = Color.get(-1, 100, 220, 532);
		if (hurtTime > 0) {
			col = Color.get(-1, 555, 555, 555);
		}else{
			//nothing to do
		}

		if (activeItem instanceof FurnitureItem) {
			yt += 2;
		}else{
			//nothing to do
		}
		screen.render(xo + 8 * flip1, yo + 0, xt + yt * 32, col, flip1);
		screen.render(xo + 8 - 8 * flip1, yo + 0, xt + 1 + yt * 32, col, flip1);
		if (!isSwimming()) {
			screen.render(xo + 8 * flip2, yo + 8, xt + (yt + 1) * 32, col, flip2);
			screen.render(xo + 8 - 8 * flip2, yo + 8, xt + 1 + (yt + 1) * 32, col, flip2);
		}else{
			//nothing to do
		}


		if (activeItem instanceof FurnitureItem) {
			Furniture furniture = ((FurnitureItem) activeItem).getFurniture();
			furniture.positionX = positionX;
			furniture.positionY = yo;
			furniture.render(screen);

		}else{
			//nothing to do
		}
	}

	public void touchItem(ItemEntity itemEntity) {
		itemEntity.take(this);
		inventory.add(itemEntity.item);
	}

	public boolean canSwim() {
		return true;
	}

	public boolean findStartPos(Level level) {
		while (true) {
			int x = random.nextInt(level.width);
			int y = random.nextInt(level.height);
			if (level.getTile(x, y) == Tile.grass) {
				this.positionX = x * 16 + 8;
				this.positionY = y * 16 + 8;
				return true;
			}else{
				//nothing to do
			}
		}
	}

	public boolean payStamina(int cost) {
		if (cost > stamina) return false;
		stamina -= cost;
		return true;
	}

	public void changeLevel(int dir) {
		game.scheduleLevelChange(dir);
	}

	public int getLightRadius() { //this method put light only on the player radius
		int r = 2; // default radius if have a lantern
		if (activeItem != null) {
			if (activeItem instanceof FurnitureItem) {
				int rr = ((FurnitureItem) activeItem).getFurniture().getLightRadius();
				if (rr > r) r = rr;
			}
		}
		return r;
	}

	protected void die() {
		super.die();
		Sound.playerDeath.play();
	}

	protected void touchedBy(Entity entity) {
		if (!(entity instanceof Player)) {
			entity.touchedBy(this);
		}else{
			//nothing to do
		}
	}

	protected void doHurt(int damage, int attackDir) {
		if (hurtTime > 0 || invulnerableTime > 0) return;

		Sound.playerHurt.play();
		level.add(new TextParticle("" + damage, positionX, positionY, Color.get(-1, 504, 504, 504)));
		health -= damage;
		switch(attackDir){
		case 0:
			positionYKnockback = +6;
			break;
		case 1:
			positionYKnockback = -6;
			break;
		case 2:
			positionXKnockback = -6;
			break;
		case 3:
			positionXKnockback = +6;
			break;
		}
		
		hurtTime = 10;
		invulnerableTime = 30;
	}

	public void gameWon() {
		level.player.invulnerableTime = 60 * 5;
		game.won();
	}
}