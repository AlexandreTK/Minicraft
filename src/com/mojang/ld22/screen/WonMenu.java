package com.mojang.ld22.screen;

import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class WonMenu extends Menu {
	private int inputDelay = 60;

	public WonMenu() {
	}

	public void tick() {
		if (inputDelay > 0)
			inputDelay--;
		
		else if (input.attack.wasKeyClicked() || input.menu.wasKeyClicked()) {
			game.setMenu(new TitleMenu());
		}
	}

	public void render(Screen screen) {
		Font.renderFrame(screen, "", 1, 3, 18, 9);
		Font.draw("You won! Yay!", screen, 2 * 8, 4 * 8, Color.get(-1, 555, 555, 555));

		int seconds = game.gameTime / 60;
		int minutes = seconds / 60;
		int hours = minutes / 60;
		minutes %= 60;
		seconds %= 60;

		String timeString = "";
		if (hours > 0) {
			if(minutes < 10){
				timeString = Integer.toString(hours) + "h" + "0" + Integer.toString(minutes) + "m";	
			}else{
				timeString = Integer.toString(hours) + "h" + "" + Integer.toString(minutes) + "m";
			}
		} else {
			if(seconds < 10){
				timeString = Integer.toString(minutes) + "m " + "0" + Integer.toString(seconds) + "s";				
			}else{
				timeString = Integer.toString(minutes) + "m " + "" + Integer.toString(seconds) + "s";
			}
		}
		
		Font.draw("Time:", screen, 2 * 8, 5 * 8, Color.get(-1, 555, 555, 555));
		Font.draw(timeString, screen, (2 + 5) * 8, 5 * 8, Color.get(-1, 550, 550, 550));
		Font.draw("Score:", screen, 2 * 8, 6 * 8, Color.get(-1, 555, 555, 555));
		Font.draw(Integer.toString(game.player.score), screen, (2 + 6) * 8, 6 * 8, Color.get(-1, 550, 550, 550));
		Font.draw("Press C to win", screen, 2 * 8, 8 * 8, Color.get(-1, 333, 333, 333));
	}
}
