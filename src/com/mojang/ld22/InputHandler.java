/*
* Objective:  Input Management - Handles the keyboard inputs.
* Last Modification: 04/09/2016.
* Modifier: Alexandre T Kryonidis.
*/

package com.mojang.ld22;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InputHandler implements KeyListener {
	public class Key {
		private int presses, absorbs;
		private boolean keyIsDown, keyWasClicked;

		public Key() {
			keys.add(this);
		}

		public void toggleKey(boolean pressed) {
			if (pressed != keyIsDown) {
				keyIsDown = pressed;
			} else {
				// Do nothing
			}
			if (pressed) {
				presses++;
			} else {
				// Do nothing
			}
		}

		public void tick() {
			if (absorbs < presses) {
				absorbs++;
				keyWasClicked = true;
			} else {
				keyWasClicked = false;
			}
		}
		
		public boolean wasKeyClicked() {
			return this.keyWasClicked;
		}
		
		public boolean isKeyDown() {
			return this.keyIsDown;
		}
	}

	public List<Key> keys = new ArrayList<Key>();

	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key attack = new Key();
	public Key menu = new Key();

	public void releaseAllKeys() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).keyIsDown = false;
		}
	}

	public void tick() {
		for (int i = 0; i < keys.size(); i++) {
			keys.get(i).tick();
		}
	}

	public InputHandler(Game game) {
		game.addKeyListener(this);
	}

	public void keyPressed(KeyEvent keyEvent) {
		toggle(keyEvent, true);
	}

	public void keyReleased(KeyEvent keyEvent) {
		toggle(keyEvent, false);
	}

	private void toggle(KeyEvent keyEvent, boolean pressed) {
		switch( keyEvent.getKeyCode() ) {
		
			case KeyEvent.VK_NUMPAD8:
				up.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_NUMPAD2:
				down.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_NUMPAD4:
				left.toggleKey(pressed);
				break;	
				
			case KeyEvent.VK_NUMPAD6:
				right.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_W:
				up.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_S:
				down.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_A:
				left.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_D:
				right.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_UP:
				up.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_DOWN:
				down.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_LEFT:
				left.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_RIGHT:
				right.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_TAB:
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_ALT:
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_ALT_GRAPH:
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_SPACE:
				attack.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_CONTROL:
				attack.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_NUMPAD0:
				attack.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_INSERT:
				attack.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_ENTER:
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_X:
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_C:
				attack.toggleKey(pressed);
				break;		
				
		}
	}

	public void keyTyped(KeyEvent ke) {
	}
}
