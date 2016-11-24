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

	/*
	 * As known, the game have a lot of itens, and if the item is a 
	 * resource that can be added in the inventory and used
	 */
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


	public boolean hasResources(Resource resource, int count) {
		ResourceItem resourceItem = findResource(resource);
		boolean returnFlag = false;
		
		if (resourceItem != null) {
			returnFlag = (resourceItem.getCount() >= count);
		} else {
			returnFlag = false;
		}
		
		assert(returnFlag == true ||  returnFlag == false);
		return returnFlag;
	}


	public boolean removeResource(Resource resource, int count) {
		ResourceItem resourceItem = findResource(resource);
		boolean returnFlag = false;

		if (resourceItem != null) {

			if (resourceItem.getCount() < count) {
				returnFlag = false;
			} else {
				resourceItem.lessSetCount(count);
	
				if (resourceItem.getCount() <= 0)
					items.remove(resourceItem);
				returnFlag = true;
			}
		} else {
			returnFlag = false;
		}
		assert(returnFlag == true ||  returnFlag == false);
		return returnFlag;
	}


	public int countItems(Item item) { 
		
		int countItems = 0;
		if (item instanceof ResourceItem) {
			ResourceItem resourceItem = findResource(((ResourceItem) item).getResource());

			if (resourceItem != null)
				countItems = resourceItem.getCount();//item counter on inventory
		} else {

			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).matches(item))
					countItems++;
			}
		}
		assert(countItems >= 0);
		return countItems;
	}	
	
	
	private ResourceItem findResource(Resource resource) {
		ResourceItem itemReturned = null;
		for (int i = 0; i < items.size(); i++) {
			if (items.get(i) instanceof ResourceItem) {
				ResourceItem hasItem = (ResourceItem) items.get(i);

				if (hasItem.getResource() == resource)
					itemReturned = hasItem;
			} else {
				// nothing to do
			}
		}
		assert(itemReturned instanceof ResourceItem || itemReturned == null);
		return itemReturned;
	}
}
