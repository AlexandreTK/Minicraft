package com.mojang.ld22.gfx;

public class Color {

	public static int get(int a, int b, int c, int d) {
		int mixColors = (get(d) << 24) + (get(c) << 16) + (get(b) << 8) + (get(a)); 
		return mixColors;
	}

	public static int get(int d) {
		if (d < 0) {
			return 255;
		} else {
			// Do nothing
		}
		int red = d / 100 % 10;
		int green = d / 10 % 10;
		int blue = d % 10;
		int colorMixture = red * 36 + green * 6 + blue;
		return colorMixture;
	}

}