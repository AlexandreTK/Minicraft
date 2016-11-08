/*
* Objective:  Game Management - Game main methods and attributes.
* Last Modification: 04/09/2016.
* Modifier: Alexandre T Kryonidis.
*/


package com.mojang.ld22;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.gfx.SpriteSheet;
import com.mojang.ld22.level.Level;
import com.mojang.ld22.level.tile.Tile;
import com.mojang.ld22.screen.DeadMenu;
import com.mojang.ld22.screen.LevelTransitionMenu;
import com.mojang.ld22.screen.Menu;
import com.mojang.ld22.screen.TitleMenu;
import com.mojang.ld22.screen.WonMenu;
import com.mojang.ld22.TestLog;

public class Game extends Canvas implements Runnable {
	// java uses serialVersionUID as an identifier of the class version
	public static final String NAME = "Minicraft";
	public static final int HEIGHT = 120;
	public static final int WIDTH = 160;
	public int gameTime = 0;
	public Player player = null;
	public TestLog logger = new TestLog();
	public Menu menu = null;
	public boolean hasWon = false;
	
	private static final long serialVersionUID = 1L;
	private Random random = new Random();
	private static final int SCALE = 3;
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
	private boolean running = false;
	private Screen screen = null;
	private Screen lightScreen = null;
	private InputHandler input = new InputHandler(this);
	private final int aSecond = 1000;
	private int[] colors = new int[256];
	private int tickCount = 0;
	private  final int maxLevel = 5;
	private Level level = null;
	private Level[] levels = new Level[5];
	private int currentLevel = 3;
	private final int maxPlayerDeadTime = 60;
	private int playerDeadTime = 0;
	private int pendingLevelChange = 0;
	private int wonTimer = 0;
	

	public static void main(String[] args) {
		Game game = new Game();
		game.setMinimumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setMaximumSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		game.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE));
		TestLog logger = new TestLog();
		JFrame frame = new JFrame(Game.NAME);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(game, BorderLayout.CENTER);
		frame.pack();
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
		
		TestLog.logger.info("Game starting...");
		
		game.start();
		
	}
	
	public void setMenu(Menu menu) {
		this.menu = menu;
		if (menu != null) {
			menu.init(this, input);
		} else {
			// Do nothing
		}
	}

	public void start() {
		running = true;
		new Thread(this).start();
	}

	public void stop() {
		running = false;
	}

	public void resetGame() {
		playerDeadTime = 0;
		wonTimer = 0;
		gameTime = 0;
		hasWon = false;

		levels = new Level[5];
		currentLevel = 3;

		levels[4] = new Level(128, 128, 1, null);
		levels[3] = new Level(128, 128, 0, levels[4]);
		levels[2] = new Level(128, 128, -1, levels[3]);
		levels[1] = new Level(128, 128, -2, levels[2]);
		levels[0] = new Level(128, 128, -3, levels[1]);

		level = levels[currentLevel];
		player = new Player(this, input);
		player.findStartPos(level);

		level.add(player);

		for (int i = 0; i < maxLevel; i++) {
			levels[i].trySpawn(5000);
		}
		
	}

	public void run() {
		long lastTime = System.nanoTime();
		double unprocessed = 0;
		double nsPerTick = 1000000000.0 / 60;
		int frames = 0;
		int ticks = 0;
		long lastTimer1 = System.currentTimeMillis();

		init();

		while (running) {
			long now = System.nanoTime();
			unprocessed += (double)(now - lastTime) / nsPerTick;
			lastTime = now;
			boolean shouldRender = true;
			while (unprocessed >= 1) {
				ticks++;
				tick();
				unprocessed -= 1;
				shouldRender = true;
			}

			try {
				Thread.sleep(2);
			} catch (InterruptedException InterruptedException) {
				InterruptedException.printStackTrace();
			}

			if (shouldRender) {
				frames++;
				render();
			} else {
				// Do nothing
			}

			if (System.currentTimeMillis() - lastTimer1 > aSecond) {
				lastTimer1 += aSecond;
				System.out.println(ticks + " ticks, " + frames + " fps");
				frames = 0;
				ticks = 0;
			} else {
				// Do nothing
			}
		}
	}

	public void tick() {
		tickCount++;
		if (!hasFocus()) {
			input.releaseAllKeys();
		} else {
			if (!player.isRemoved && !hasWon) {
				gameTime++;
			} else {
				// Do nothing
			}
			input.tick();
			if (menu != null) {
				menu.tick();
			} else {
				if (player.isRemoved) {
					playerDeadTime++;
					if (playerDeadTime > maxPlayerDeadTime) {
						setMenu(new DeadMenu());
					} else {
						// Do nothing
					}
				} else {
					if (pendingLevelChange != 0) {
						setMenu(new LevelTransitionMenu(pendingLevelChange));
						pendingLevelChange = 0;
					} else {
						// Do nothing
					}
				}
				if (wonTimer > 0) {
					--wonTimer;
					if (wonTimer == 0) {
						setMenu(new WonMenu());
					} else {
						// Do nothing
					}
				} else {
					// Do nothing
				}
				level.tick();
				Tile.tickCount++;
			}
		}
	}

	public void changeLevel(int dir) {
		level.remove(player);
		currentLevel += dir;
		level = levels[currentLevel];
		final int modifyPositionX = ((player.positionX >> 4) * 16) + 8;
		final int modifyPositionY = ((player.positionY >> 4) * 16) + 8;
		player.positionX = modifyPositionX;
		player.positionY = modifyPositionY;
		level.add(player);
		
		TestLog.logger.info("level changed...");

	}

	public void render() {
		BufferStrategy bs = getBufferStrategy();
		if (bs == null) {
			createBufferStrategy(3);
			requestFocus();
			return;
		} else {
			// Do nothing
		}

		int xScroll = player.positionX - screen.width / 2;
		int yScroll = player.positionY - (screen.height - 8) / 2;
		
		if (xScroll < 16) {
			xScroll = 16;
		} else {
			// Do nothing
		}
		if (yScroll < 16) {
			yScroll = 16;
		} else {
			// Do nothing
		}
		int testCondition = level.width * 16 - screen.width - 16;
		
		if (xScroll > testCondition) {
			xScroll = testCondition;
		} else {
			// Do nothing
		}
		if (yScroll > testCondition) {
			yScroll = testCondition;
		} else {
			// Do nothing
		}
		if (currentLevel > 3) {
			int col = Color.get(20, 20, 121, 121);
			for (int y = 0; y < 14; y++)
				for (int x = 0; x < 24; x++) {
					int positionX = x * 8 - ((xScroll/4) & 7);
					int positionY = y * 8 - ((yScroll/4) & 7);
					screen.render(positionX, positionY, 0, col, 0);
				}
		} else {
			// Do nothing
		}

		level.renderBackground(screen, xScroll, yScroll);
		level.renderSprites(screen, xScroll, yScroll);

		if (currentLevel < 3) {
			lightScreen.clear(0);
			level.renderLight(lightScreen, xScroll, yScroll);
			screen.overlay(lightScreen, xScroll, yScroll);
		} else {
			// Do nothing
		}

		renderGui();

		if (!hasFocus()) {
			renderFocusNagger();
		} else {
			// Do nothing
		}

		for (int y = 0; y < screen.height; y++) {
			for (int x = 0; x < screen.width; x++) {
				int screenPixelsPosition = x + y * screen.width;
				int cc = screen.pixels[screenPixelsPosition];
				if (cc < 255) {
					int width= x + y * WIDTH;
					pixels[width] = colors[cc];
				} else {
					// Do nothing
				}
			}
		}

		Graphics g = bs.getDrawGraphics();
		g.fillRect(0, 0, getWidth(), getHeight());

		int ww = WIDTH * 3;
		int hh = HEIGHT * 3;
		int xo = (getWidth() - ww) / 2;
		int yo = (getHeight() - hh) / 2;
		g.drawImage(image, xo, yo, ww, hh, null);
		g.dispose();
		bs.show();
	}
	
	public void scheduleLevelChange(int dir) {
		
		pendingLevelChange = dir;
	}
	
	
	public void won() {
		wonTimer = 60 * 3;
		hasWon = true;
	}

	private void renderGui() {
		for (int y = 0; y < 2; y++) {
			for (int x = 0; x < 20; x++) {
				int xPosition = x * 8;
				int yPosition = screen.height - 16 + y * 8;
				int tile = 0 + 12 * 32;
				screen.render(xPosition, yPosition, tile, Color.get(000, 000, 000, 000), 0);
			}
		}

		for (int i = 0; i < 10; i++) {
			if (i < player.health) {
				int positionX = i * 8;
				int positionY = screen.height - 16;
				int tile = 0 + 12 * 32;
				
				screen.render(positionX, positionY, tile, Color.get(000, 200, 500, 533), 0);
			} else {
				int positionX = i * 8;
				int positionY = screen.height - 16;
				int tile = 0 + 12 * 32;
				
				screen.render(positionX, positionY, tile, Color.get(000, 100, 000, 000), 0);
			}
			if (player.staminaRechargeDelay > 0) {
				if (player.staminaRechargeDelay / 4 % 2 == 0) {
					int positionX = i * 8;
					int positionY = screen.height - 8;
					int tile = 1 + 12 * 32;
					
					screen.render(positionX, positionY, tile, Color.get(000, 555, 000, 000), 0);
				} else {
					int positionX = i * 8;
					int positionY = screen.height - 8;
					int tile = 1 + 12 * 32;
					
					screen.render(positionX, positionY, tile, Color.get(000, 110, 000, 000), 0);
				}
			} else {
				if (i < player.stamina) {
					int positionX = i * 8;
					int positionY = screen.height - 8;
					int tile = 1 + 12 * 32;
					
					screen.render(positionX, positionY, tile, Color.get(000, 220, 550, 553), 0);
				} else {
					int positionX = i * 8;
					int positionY = screen.height - 8;
					int tile = 1 + 12 * 32;
					
					screen.render(positionX, positionY, tile, Color.get(000, 110, 000, 000), 0);
				}
			}
		}
		if (player.activeItem != null) {
			player.activeItem.renderInventory(screen, 10 * 8, screen.height - 16);
		} else {
			// Do nothing
		}

		if (menu != null) {
			menu.render(screen);
		} else {
			// Do nothing
		}
	}

	private void init() {
		int pp = 0;
		for (int r = 0; r < 6; r++) {
			for (int g = 0; g < 6; g++) {
				for (int b = 0; b < 6; b++) {
					int rr = (r * 255 / 5);
					int gg = (g * 255 / 5);
					int bb = (b * 255 / 5);
					int mid = (rr * 30 + gg * 59 + bb * 11) / 100;
					
					int r1 = ((rr + mid * 1) / 2) * 230 / 255 + 10;
					int g1 = ((gg + mid * 1) / 2) * 230 / 255 + 10;
					int b1 = ((bb + mid * 1) / 2) * 230 / 255 + 10;
					
					colors[pp] = r1 << 16 | g1 << 8 | b1;
					pp++;
				}
			}
		}
		try {
			screen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png"))));
			lightScreen = new Screen(WIDTH, HEIGHT, new SpriteSheet(ImageIO.read(Game.class.getResourceAsStream("/icons.png"))));
		} catch (IOException IOException) {
			IOException.printStackTrace();
		}
		
		resetGame();
		setMenu(new TitleMenu());
	}

	private void renderFocusNagger() {
		String msg = "Click to focus!";
		int xx = (WIDTH - msg.length() * 8) / 2;
		int yy = (HEIGHT - 8) / 2;
		int w = msg.length();
		int h = 1;
		
		screen.render(xx - 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
		screen.render(xx + w * 8, yy - 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
		screen.render(xx - 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
		screen.render(xx + w * 8, yy + 8, 0 + 13 * 32, Color.get(-1, 1, 5, 445), 3);
		for (int x = 0; x < w; x++) {
			screen.render(xx + x * 8, yy - 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
			screen.render(xx + x * 8, yy + 8, 1 + 13 * 32, Color.get(-1, 1, 5, 445), 2);
		}
		for (int y = 0; y < h; y++) {
			screen.render(xx - 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 0);
			screen.render(xx + w * 8, yy + y * 8, 2 + 13 * 32, Color.get(-1, 1, 5, 445), 1);
		}
		
		if ((tickCount / 20) % 2 == 0) {
			Font.draw(msg, screen, xx, yy, Color.get(5, 333, 333, 333));
		} else {
			Font.draw(msg, screen, xx, yy, Color.get(5, 555, 555, 555));
		}
	}
}
