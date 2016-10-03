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
	public List<Item> costs = new ArrayList<Item>();
	
	public Item resultTemplate;

	public Recipe(Item resultTemplate) {
		this.resultTemplate = resultTemplate;
	}

	public Recipe addCost(Resource resource, int count) {
		costs.add(new ResourceItem(resource, count));
		return this;
	}

	public boolean canCraft = false;
	
	public void checkCanCraft(Player player) {
		for (int i = 0; i < costs.size(); i++) {
			Item item = costs.get(i);
			if (item instanceof ResourceItem) {
				ResourceItem resourceItem = (ResourceItem) item;
				if (!player.inventory.hasResources(resourceItem.resource, resourceItem.count)) {
					canCraft = false;
					return;
				}
			}
		}
		canCraft = true;
	}

	public void renderInventory(Screen screen, int sizeX, int sizeY) {
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
				player.inventory.removeResource(resourceItem.resource, resourceItem.count);
			}
		}
	}
}