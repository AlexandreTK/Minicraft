package com.mojang.ld22.entity;

import java.util.ArrayList;
import java.util.List;

import com.mojang.ld22.item.Item;
import com.mojang.ld22.item.ResourceItem;
import com.mojang.ld22.item.resource.Resource;

public class Inventory {
	public List<Item> items = new ArrayList<Item>();

	public void add(Item item) {
		add(items.size(), item);
	}

	public void add(int slot, Item item) {
		if (item instanceof ResourceItem) {
			ResourceItem toTake = (ResourceItem) item; //available items
			ResourceItem hasItem = findResource(toTake.getResource()); //items on player inventory
			if (hasItem == null) {
				items.add(slot, toTake);
			} else {
				hasItem.plusSetCount(toTake.getCount()); 
			}
		} else {
			items.add(slot, item);
		}
	}

	private ResourceItem findResource(Resource resource) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) instanceof ResourceItem) {
				ResourceItem hasItem = (ResourceItem) items.get(i);

				if (hasItem.getResource() == resource)
					return hasItem;
			} else {
				// nothing to do
			}
		}
		return null;
	}

	public boolean hasResources(Resource resource, int count) {
		ResourceItem resourceItem = findResource(resource);

		if (resourceItem == null)
			return false;

		return resourceItem.getCount() >= count;
	}

	public boolean removeResource(Resource resource, int count) {
		ResourceItem resourceItem = findResource(resource);

		if (resourceItem == null)
			return false;

		if (resourceItem.getCount() < count)
			return false;

		resourceItem.lessSetCount(count); // 

		if (resourceItem.getCount() <= 0)
			items.remove(resourceItem);

		return true;
	}

	public int countItems(Item item) { 
		if (item instanceof ResourceItem) {
			ResourceItem resourceItem = findResource(((ResourceItem) item).getResource());

			if (resourceItem != null)
				return resourceItem.getCount();//item counter on inventory
		} else {
			int countItems = 0;

			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).matches(item))
					countItems++;
			}
			return countItems;
		}
		return 0;
	}
}
