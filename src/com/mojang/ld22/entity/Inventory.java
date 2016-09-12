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
			ResourceItem toTake = (ResourceItem) item;
			ResourceItem hasItem = findResource(toTake.resource);
			if (hasItem == null) {
				items.add(slot, toTake);
			} else {
				hasItem.count += toTake.count;
			}
		} else {
			items.add(slot, item);
		}
	}

	private ResourceItem findResource(Resource resource) {
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) instanceof ResourceItem) {
				ResourceItem hasItem = (ResourceItem) items.get(i);
				
				if (hasItem.resource == resource)
					return hasItem;
			}
		}
		return null;
	}

	public boolean hasResources(Resource resource, int count) {
		ResourceItem resourceItem = findResource(resource);
		
		if (resourceItem == null)
			return false;
		
		return resourceItem.count >= count;
	}

	public boolean removeResource(Resource resource, int count) {
		ResourceItem resourceItem = findResource(resource);
		
		if (resourceItem == null)
			return false;
		
		if (resourceItem.count < count)
			return false;
		
		resourceItem.count -= count;
		
		if (resourceItem.count <= 0)
			items.remove(resourceItem);
		
		return true;
	}

	public int countItems(Item item) {
		if (item instanceof ResourceItem) {
			ResourceItem resourceItem = findResource(((ResourceItem)item).resource);
			
			if (resourceItem!=null)
				return resourceItem.count;
		} else {
			int countItems = 0;
			
			for (int i=0; i<items.size(); i++) {
				if (items.get(i).matches(item))
					countItems++;
			}
			return countItems;
		}
		return 0;
	}
}
