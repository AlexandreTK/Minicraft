package com.mojang.ld22.gfx;

public class Color {

	public static int get(int a, int b, int c, int d) {
		int mixColors = (get(d) << 24) + (get(c) << 16) + (get(b) << 8) + (get(a)); 
		return mixColors;
	}

	public static int get(int d) {
		int red = (int) d / 100 % 10;
		int green = (int) d / 10 % 10;
		int blue = (int) d % 10;
		int colorMixture = ((int) red * 36 + green * 6 + blue);
		
		if (d < 0) {
			return 255;
		} else {
			// Do nothing
		}
		
		return colorMixture;
	}

}