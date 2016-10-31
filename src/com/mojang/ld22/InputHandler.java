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
	
	public List<Key> keys = new ArrayList<Key>();
	public Key up = new Key();
	public Key down = new Key();
	public Key left = new Key();
	public Key right = new Key();
	public Key attack = new Key();
	public Key menu = new Key();
	
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

	public void keyTyped(KeyEvent ke) {
	}
	
	private void toggle(KeyEvent keyEvent, boolean pressed) {
		switch( keyEvent.getKeyCode() ) {
		
			case KeyEvent.VK_NUMPAD8://define number 8 in AlphaNumeric as UP
				up.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_NUMPAD2://define number 2 in AlphaNumeric as Down
				down.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_NUMPAD4://define number 4 in AlphaNumeric as left
				left.toggleKey(pressed);
				break;	
				
			case KeyEvent.VK_NUMPAD6://define number 6 in AlphaNumeric as right
				right.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_W://define W as UP
				up.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_S:// define S as down
				down.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_A:// define A as left
				left.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_D://define D as right
				right.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_UP://define 'up' as up
				up.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_DOWN://define 'down' as down
				down.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_LEFT://define 	'left' as left
				left.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_RIGHT:// define right as right
				right.toggleKey(pressed);
				break;
				
			case KeyEvent.VK_TAB://define 'tab' to open menu
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_ALT://define 'alt' to open menu
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_ALT_GRAPH://define 'altGR' to open menu
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_SPACE:// define  'space' as attack
				attack.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_CONTROL:// define  'control' as attack
				attack.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_NUMPAD0:// define  '0' as attack
				attack.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_INSERT:
				attack.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_ENTER://define 'enter' to open menu
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_X:// define'x' to open  menu
				menu.toggleKey(pressed);
				break;		
				
			case KeyEvent.VK_C:// define  'c' as attack
				attack.toggleKey(pressed);
				break;		
				
		}
	}

}
