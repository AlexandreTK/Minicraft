package com.mojang.ld22.screen;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;

public class InventoryMenu extends Menu {
	private Player player = null;
	private int selected = 0;

	public InventoryMenu(Player player) {
		this.player = player;

		if (player.activeItem != null) {
			player.inventory.items.add(0, player.activeItem);
			player.activeItem = null;
		}
	}

	public void tick() {
		if (input.menu.wasKeyClicked()) game.setMenu(null);

		if (input.up.wasKeyClicked())
			selected--;
		
		if (input.down.wasKeyClicked())
			selected++;

		int len = player.inventory.items.size();
		
		if (len == 0)
			selected = 0;
		
		if (selected < 0)
			selected += len;
		
		if (selected >= len)
			selected -= len;

		if (input.attack.wasKeyClicked() && len > 0) {
			Item item = player.inventory.items.remove(selected);
			player.activeItem = item;
			game.setMenu(null);
		}
	}

	public void render(Screen screen) {
		Font.renderFrame(screen, "inventory", 1, 1, 12, 11);
		renderItemList(screen, 1, 1, 12, 11, player.inventory.items, selected);
	}
}