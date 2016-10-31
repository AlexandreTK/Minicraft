/*
* Objective:  Applet Management - Handles the game initialization, start, stop.
* Last Modification: 04/09/2016.
* Modifier: Alexandre T Kryonidis.
*/

package com.mojang.ld22;

import java.applet.Applet;
import java.awt.BorderLayout;

public class GameApplet extends Applet {
	// java uses serialVersionUID as an identifier of the class version
	private static final long serialVersionUID = 1L;
	private Game game = new Game();
	public TestLog logger = new TestLog();

	public void initApplet() {
		setLayout(new BorderLayout());
		add(game, BorderLayout.CENTER);
		TestLog logger = new TestLog();
		logger.logger.info("init applet...");
	}

	public void startApplet() {// Start  The game
		game.start();
	}

	public void stopApplet() {// Stop the Game
		game.stop();
	}
}