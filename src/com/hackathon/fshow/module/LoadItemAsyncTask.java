package com.hackathon.fshow.module;

import rajawali.lights.DirectionalLight;
import rajawali.materials.Material;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
import rajawali.primitives.Plane;
import rajawali.util.ObjectColorPicker;
import ImageUtils.ImageUtils;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.hackathon.fshow.ObjectDraggingRenderer;

public class LoadItemAsyncTask extends AsyncTask<String, Integer, Boolean> {

	ObjectDraggingRenderer mRenderer = null; 
	ObjectColorPicker mPicker = null;
	DirectionalLight mLight = null;
	
	public LoadItemAsyncTask(
			ObjectDraggingRenderer renderer, 
			ObjectColorPicker picker,
			DirectionalLight light) {
		this.mRenderer = renderer;
		this.mPicker = picker;
		this.mLight = light;
	}

	@Override
	protected Boolean doInBackground(String... params) {
		boolean result = false;
		int size = params.length;
		for (int i = 0; i < size; i++) {
			String imageString = params[i];
			Bitmap bmp = ImageUtils.decodeToImage(imageString);
			Texture texture = new Texture("texture", bmp);
			Material planesGaloreMaterial = new Material();
			try {
				planesGaloreMaterial.addTexture(texture);
				Plane plane = new Plane();
				plane.setDoubleSided(true);
				plane.setRotY(180);
				plane.setMaterial(planesGaloreMaterial);
				plane.setColor(0x333333 + (int) (Math.random() * 0xcccccc));
				plane.setX(-4 + (float) (Math.random() * 8));
				plane.setY(-4 + (float) (Math.random() * 8));
				plane.setZ(-2 + (float) (Math.random() * -6));
				mPicker.registerObject(plane);
				mRenderer.addChild(plane);
			} catch (TextureException e) {
				e.printStackTrace();
			}
			result = true;
		}
		return result;
	}
	
	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
	}

}
