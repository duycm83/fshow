package com.hackathon.fshow.module;

import java.util.LinkedList;

public class Layer {
	// Layer1 MINZ/MAXZ
	public static final float LAYER1_MINZ = -2f;
	public static final float LAYER1_MAXZ = -4f;
	// Layer2 MINZ/MAXZ	
	public static final float LAYER2_MINZ = -6f;
	public static final float LAYER2_MAXZ = -8f;
	// Layer3 MINZ/MAXZ
	public static final float LAYER3_MINZ = -10f;
	public static final float LAYER3_MAXZ = -12f;
	
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
