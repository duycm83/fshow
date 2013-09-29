package com.hackathon.fshow.module;

import java.util.LinkedList;

public class Layer {
	public enum TYPE {layer1, layer2, layer3};
	private LinkedList<Item> itemList = new LinkedList<Item>();
	private float z= 0;
	public LinkedList<Item> getItemList() {
		return itemList;
	}
	public void setItemList(LinkedList<Item> itemList) {
		this.itemList = itemList;
	}
	public float getZ() {
		return z;
	}
	public void setZ(float z) {
		this.z = z;
	}

	
}
