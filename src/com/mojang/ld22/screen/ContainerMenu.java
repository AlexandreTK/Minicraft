package com.mojang.ld22.screen;

import com.mojang.ld22.entity.Inventory;
import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;

public class ContainerMenu extends Menu {
	private Player player = null;
	private Inventory container = null;
	private int selected = 0;
	private String title = "";
	private int oSelected = 0;
	private int window = 0;

	public ContainerMenu(Player player, String title, Inventory container) {
		this.player = player;
		this.title = title;
		this.container = container;
	}

	public void tick() {
		if (input.menu.wasKeyClicked()){
			game.setMenu(null);
		}
		
		if (input.left.wasKeyClicked()) {
			window = 0;
			int tmp = selected;
			selected = oSelected;
			oSelected = tmp;
		}
		
		if (input.right.wasKeyClicked()) {
			window = 1;
			int tmp = selected;
			selected = oSelected;
			oSelected = tmp;
		}

		Inventory i = null;
		Inventory i2 = null;
		
		if(window == 1){
			i = player.inventory;
		}else{
			i = container;	
		};
		

		if(window == 0){
			i2 = player.inventory;
		}else{
			i2 = container;	
		};

		int len = i.items.size();
		
		if (selected < 0)
			selected = 0;
		
		if (selected >= len)
			selected = len - 1;

		if (input.up.wasKeyClicked())
			selected--;
		
		if (input.down.wasKeyClicked())
			selected++;

		if (len == 0)
			selected = 0;
		
		if (selected < 0)
			selected += len;
		
		if (selected >= len)
			selected -= len;

		if (input.attack.wasKeyClicked() && len > 0) {
			i2.add(oSelected, i.items.remove(selected));
			
			if (selected >= i.items.size())
				selected = i.items.size() - 1;
		}
	}

	public void render(Screen screen) {
		if (window == 1)
			screen.setOffset(6 * 8, 0);
		
		Font.renderFrame(screen, title, 1, 1, 12, 11);
		
		if(window == 0){
			renderItemList(screen, 1, 1, 12, 11, container.items, selected);			
		}else{
			renderItemList(screen, 1, 1, 12, 11, container.items, -oSelected - 1);	
		}

		Font.renderFrame(screen, "inventory", 13, 1, 13 + 11, 11);
		
		if(window==1){
			renderItemList(screen, 13, 1, 13 + 11, 11, player.inventory.items,selected);
		}else{
			 renderItemList(screen, 13, 1, 13 + 11, 11, player.inventory.items,-oSelected - 1);
		}
		
		screen.setOffset(0, 0);
	}
}