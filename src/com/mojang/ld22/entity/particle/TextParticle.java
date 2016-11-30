package com.mojang.ld22.entity.particle;

//File : TextParticle.java
//Objective: Render the thext particles on the game.

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

	/** 
	 * TextParticle constructor. It is used to display a message on the screen.
	 * It's usually used to display the damage one entity took from another.
	 * It's usually a number.
	 * 
	 * @param msg String representing the message to be displayed
	 * @param x The horizontal coordinate where the message will be displayed
	 * @param y The vertical coordinate where the message will be displayed
	 * @param y The color of the message.
	 */
	public TextParticle(String msg, int x, int y, int col) {
		// Secure constructor
		assert msg != null : "Message cannot be null";
		assert x >= 0 : "Position x must be positive";
		assert y >= 0 : "Position y must be positive";
		assert col >= 0 : "Color value must be positive";
		
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

	/**
	 * This is a method  used to control the logic updates of the game.
	 * It basically indicates when each action will take place in the game.
	 * 
	 * It's related to the FPS(Frames Per Second), but while the last indicates how many times the
	 * screen will be refreshed, the Tick indicates how many times the logic of the game will
	 * be updated per second.
	 *
	 * @param none.
	 * @return none.
	 */
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

	/** 
	 * Renders the message on the screen
	 * 
	 * @param screen Represents where the message will be displayed
	 */
	public void render(Screen screen) {
		// Secure method
		assert screen != null : "Screen cannot be null";
		
//		Font.draw(msg, screen, x - msg.length() * 4, y, Color.get(-1, 0, 0, 0));
		Font.draw(msg, screen, positionX - msg.length() * 4 + 1, positionY - (int) (zz) + 1, Color.get(-1, 0, 0, 0));
		Font.draw(msg, screen, positionX - msg.length() * 4, positionY - (int) (zz), col);
	}


	/** 
	 * Frees the object's memory to make it easier for the garbage collector to get it.
	 * 
	 */
	public void finalizeObject() throws Throwable
	{
		this.finalize();
	}
}
