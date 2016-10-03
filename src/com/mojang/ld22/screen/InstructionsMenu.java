package com.mojang.ld22.screen;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class InstructionsMenu extends Menu {
	private Menu parent;

	public InstructionsMenu(Menu parent) {
		this.parent = parent;
	}

	public void tick() {
		if (input.attack.wasKeyClicked() || input.menu.wasKeyClicked()) {
			game.setMenu(parent);
		}
	}

	public void render(Screen screen) {
		screen.clear(0);
		
		int color = Color.get(0, 333, 333, 333);
		int positionX = 0 * 8 + 4;
		
		Font.draw("HOW TO PLAY", screen, 4 * 8 + 4, 1 * 8, Color.get(0, 555, 555, 555));
		Font.draw("Move your character", screen, positionX, 3 * 8, color);
		Font.draw("with the arrow keys", screen, positionX, 4 * 8, color);
		Font.draw("press C to attack", screen, positionX, 5 * 8, color);
		Font.draw("and X to open the", screen, positionX, 6 * 8, color);
		Font.draw("inventory and to", screen, positionX, 7 * 8, color);
		Font.draw("use items.", screen, positionX, 8 * 8, color);
		Font.draw("Select an item in", screen, positionX, 9 * 8, color);
		Font.draw("the inventory to", screen, positionX, 10 * 8, color);
		Font.draw("equip it.", screen, positionX, 11 * 8, color);
		Font.draw("Kill the air wizard", screen, positionX, 12 * 8, color);
		Font.draw("to win the game!", screen, positionX, 13 * 8, color);
	}
}
