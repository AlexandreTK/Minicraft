package com.mojang.ld22.entity.particle;

import com.mojang.ld22.entity.Entity;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class TextParticle extends Entity {
	private String msg;
	private int col = 0;
	private int time = 0;
	public double xa = 0;
	public double ya = 0;
	public double za = 0;
	public double xx = 0;
	public double yy = 0;
	public double zz = 0;

	public TextParticle(String msg, int x, int y, int col) {
		this.msg = msg;
		this.positionX = x;
		this.positionY = y;
		this.col = col;
		xx = x;
		yy = y;
		zz = 2;
		xa = random.nextGaussian() * 0.3;
		ya = random.nextGaussian() * 0.2;
		za = random.nextFloat() * 0.7 + 2;
	}

	public void tick() {
		time++;
		if (time > 60) {
			remove();
		} else {
			//Do nothing.
		}
		
		xx += xa;
		yy += ya;
		zz += za;
		if (zz < 0) {
			zz = 0;
			za *= -0.5;
			xa *= 0.6;
			ya *= 0.6;
		}else{
			//Do nothing;
		}
		za -= 0.15;
		try{
		positionX = (int) xx;
		positionY = (int) yy;
		}catch(ArithmeticException erroArithmetic){
			System.out.println("erro in TextParticle");
			
		}
		
	}

	public void render(Screen screen) {
//		Font.draw(msg, screen, x - msg.length() * 4, y, Color.get(-1, 0, 0, 0));
		Font.draw(msg, screen, positionX - msg.length() * 4 + 1, positionY - (int) (zz) + 1, Color.get(-1, 0, 0, 0));
		Font.draw(msg, screen, positionX - msg.length() * 4, positionY - (int) (zz), col);
	}

}
