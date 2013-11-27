package com.hackathon.fshow.module;

import rajawali.primitives.Plane;
import android.graphics.Bitmap;

public class MyPlane extends Plane {

	public String name = "";
	private Bitmap bitmap = null;
	
	public MyPlane(String name, Bitmap bitmap) {
		super();
		this.name = name;
		this.bitmap = bitmap;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		} else if (o instanceof MyPlane) {
			MyPlane that = ((MyPlane) o);
			return that.getName().equals(this.name);
		} else {
			return false;
		}
	}
	
}
