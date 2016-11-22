package com.mojang.ld22.crafting;

import java.util.ArrayList;
import java.util.List;

import com.mojang.ld22.entity.Player;
import com.mojang.ld22.gfx.Color;
import com.mojang.ld22.gfx.Font;
import com.mojang.ld22.gfx.Screen;
import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;
import com.mojang.ld22.screen.ListItem;

public abstract class Recipe implements ListItem {

	public boolean canCraft = false;
	
	public List<Item> costs = new ArrayList<Item>();
	
	public Item resultTemplate;

	public Recipe(Item resultTemplate) {
		this.resultTemplate = resultTemplate;
	}

	public Recipe addCost(Resource resource, int count) {
		assert(resource != null): "parameter resource is null";
		costs.add(new ResourceItem(resource, count));
		return this;
	}

	
	public void checkCanCraft(final Player player) {
		assert(player != null): "parameter player is null";
		for (int i = 0; i < costs.size(); i++) {
			Item item = costs.get(i);
			if (item instanceof ResourceItem) {
				ResourceItem resourceItem = (ResourceItem) item;
				if (!player.inventory.hasResources(resourceItem.getResource(), resourceItem.getCount())) {
					canCraft = false;
					return;
				}
			}
		}
		canCraft = true;
	}

	public void renderInventory(final Screen screen, int sizeX, int sizeY) {
		assert(screen != null): "parameter screen is null";
		int whiteScaleColor = 555;
		int grayScaleColor = 222;
		screen.render(sizeX, sizeY, resultTemplate.getSprite(), resultTemplate.getColor(), 0);
		int textColor;
		if (canCraft){
			textColor = Color.get(-1, whiteScaleColor, whiteScaleColor, whiteScaleColor);
		}else {
			textColor = Color.get(-1, grayScaleColor, grayScaleColor, grayScaleColor);
		}
		Font.draw(resultTemplate.getName(), screen, sizeX + 8, sizeY, textColor);
	}

	public abstract void craft(Player player);

	public void deductCost(Player player) {
		for (int i = 0; i < costs.size(); i++) {
			Item item = costs.get(i);
			if (item instanceof ResourceItem) {
				ResourceItem resourceItem = (ResourceItem) item;
				player.inventory.removeResource(resourceItem.getResource(), resourceItem.getCount());
			}
		}
	}
}