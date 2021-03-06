package com.mojang.ld22.gfx;

import com.mojang.ld22.TestLog;

public class Screen {
	/*
	 * public static final int MAP_WIDTH = 64; // Must be 2^x public static
	 * final int MAP_WIDTH_MASK = MAP_WIDTH - 1;
	 * 
	 * public int[] tiles = new int[MAP_WIDTH * MAP_WIDTH]; public int[] colors
	 * = new int[MAP_WIDTH * MAP_WIDTH]; public int[] databits = new
	 * int[MAP_WIDTH * MAP_WIDTH];
	 */
	public int xOffset = 0;
	public int yOffset = 0;

	public static final int BIT_MIRROR_X = 0x01;
	public static final int BIT_MIRROR_Y = 0x02;

	public final int width, height;
	public int[] pixels;

	private SpriteSheet sheet;

	public Screen(int w, int h, SpriteSheet sheet) {
		this.sheet = sheet;
		this.width = w;
		this.height = h;

		pixels = new int[w * h];

		// Random random = new Random();

		/*
		 * for (int i = 0; i < MAP_WIDTH * MAP_WIDTH; i++) { colors[i] =
		 * Color.get(00, 40, 50, 40); tiles[i] = 0;
		 * 
		 * if (random.nextInt(40) == 0) { tiles[i] = 32; colors[i] =
		 * Color.get(111, 40, 222, 333); databits[i] = random.nextInt(2); } else
		 * if (random.nextInt(40) == 0) { tiles[i] = 33; colors[i] =
		 * Color.get(20, 40, 30, 550); } else { tiles[i] = random.nextInt(4);
		 * databits[i] = random.nextInt(4);
		 * 
		 * } }
		 * 
		 * Font.setMap("Testing the 0341879123", this, 0, 0, Color.get(0, 555,
		 * 555, 555));
		 */
	}

	public void clear(int color) {
		for (int i = 0; i < pixels.length; i++)
			pixels[i] = color;
	}

	/*
	 * public void renderBackground() { for (int yt = yScroll >> 3; yt <=
	 * (yScroll + h) >> 3; yt++) { int yp = yt * 8 - yScroll; for (int xt =
	 * xScroll >> 3; xt <= (xScroll + w) >> 3; xt++) { int xp = xt * 8 -
	 * xScroll; int ti = (xt & (MAP_WIDTH_MASK)) + (yt & (MAP_WIDTH_MASK)) *
	 * MAP_WIDTH; render(xp, yp, tiles[ti], colors[ti], databits[ti]); } }
	 * 
	 * for (int i = 0; i < sprites.size(); i++) { Sprite s = sprites.get(i);
	 * render(s.x, s.y, s.img, s.col, s.bits); } sprites.clear(); }
	 */

	public void render(int xp, int yp, int tile, int colors, int bits) {
		xp -= xOffset;
		yp -= yOffset;

		for (int y = 0; y < 8; y++) {
			int ys = y;
			boolean mirrorY = (bits & BIT_MIRROR_Y) > 0;
			if (mirrorY) {
				ys = 7 - y;
			} else {
				// Do nothing
			}
			if (y + yp < 0 || y + yp >= height) {
				continue;
			} else {
				// Do nothing
			}
			for (int x = 0; x < 8; x++) {
				if (x + xp < 0 || x + xp >= width) {
					continue;
				} else {
					// Do nothing
				}

				int xs = x;
				boolean mirrorX = (bits & BIT_MIRROR_X) > 0;
				if (mirrorX) {
					xs = 7 - x;
				} else {
					// Do nothing
				}
				int xTile = (int) tile % 32;
				int yTile = (int) tile / 32;
				int toffs = (int) xTile * 8 + yTile * 8 * sheet.width;
				int col = (colors >> (sheet.pixels[xs + ys * sheet.width + toffs] * 8)) & 255;
				if (col < 255) {
					pixels[(x + xp) + (y + yp) * width] = col;
				} else {
					// Do nothing
				}
			}
		}
	}

	public void setOffset(int xOffset, int yOffset) {
		this.xOffset = xOffset;
		this.yOffset = yOffset;
	}

	private int[] dither = new int[] { 0, 8, 2, 10, 12, 4, 14, 6, 3, 11, 1, 9, 15, 7, 13, 5, };

	public void overlay(Screen screen2, int xa, int ya) {
		int[] oPixels = screen2.pixels;
		int i = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (oPixels[i] / 10 <= dither[((x + xa) & 3) + ((y + ya) & 3) * 4]) {
					pixels[i] = 0;
				} else {
					// Do nothing
				}
				i++;
			}

		}
	}

	public void renderLight(int x, int y, int r) {
		x -= xOffset;
		y -= yOffset;

		int x0 = x - r;
		if (x0 < 0) {
			x0 = 0;
		} else {
			// Do nothing
		}

		int y0 = y - r;
		if (y0 < 0) {
			y0 = 0;
		} else {
			// Do nothing
		}

		int x1 = x + r;
		if (x1 > width) {
			x1 = width;
		} else {
			// Do nothing
		}

		int y1 = y + r;
		if (y1 > height) {
			y1 = height;
		} else {
			// Do nothing
		}
		// System.out.println(x0 + ", " + x1 + " -> " + y0 + ", " + y1);
		for (int yy = y0; yy < y1; yy++) {
			int yd = yy - y;
			yd = yd * yd;
			for (int xx = x0; xx < x1; xx++) {
				int xd = xx - x;
				int dist = xd * xd + yd;
				// System.out.println(dist);
				if (dist <= r * r) {
					int br = 255 - dist * 255 / (r * r);
					if (pixels[xx + yy * width] < br) {
						pixels[xx + yy * width] = br;
					} else {
						// Do nothing
					}
				} else {
					// Do nothing
				}
			}
		}
		TestLog.logger.info("Renderied Light");
	}
}