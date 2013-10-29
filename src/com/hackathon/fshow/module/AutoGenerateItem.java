package com.hackathon.fshow.module;

import org.json.JSONArray;
import org.json.JSONException;

import rajawali.lights.DirectionalLight;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
import rajawali.util.ObjectColorPicker;
import ImageUtils.ImageUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.hackathon.fshow.ObjectDraggingRenderer;
import com.hackathon.fshow.planes.PlanesGaloreMaterial;

public class AutoGenerateItem {

	private static final String TAG = "AutoGenerateItem";

	public static void addNewItem(Context ctx, ObjectDraggingRenderer renderer,
			ObjectColorPicker picker, DirectionalLight light, int itemId,
			String imageBase64) {
		try {
			PlanesGaloreMaterial planesGaloreMaterial = new PlanesGaloreMaterial();
			String name = String.format("image%04d", itemId);
			Bitmap bmp = ImageUtils.decodeToImage(imageBase64);
			if (bmp == null) {
				Log.e(TAG, "@@@error name:" + name);
				return;
			}
			planesGaloreMaterial.addTexture(new Texture(name, bmp));
			MyPlane plane = new MyPlane(name, bmp);
			plane.addLight(light);
			plane.setMaterial(planesGaloreMaterial);
			plane.setColor(0x333333 + (int) (Math.random() * 0xcccccc));
			plane.setX(-4 + (float) (Math.random() * 8));
			plane.setY(-4 + (float) (Math.random() * 8));
			plane.setZ(-2 + (float) (Math.random() * -6));
			picker.registerObject(plane);
			renderer.addChild(plane);
		} catch (TextureException e) {
			e.printStackTrace();
		}
	}

	public static void showItems(Context context,
			ObjectDraggingRenderer renderer, ObjectColorPicker picker,
			DirectionalLight light, JSONArray mData) {
		if (mData == null) return;
		try {
			int size = mData.length();
			for (int i = 0; i < size; i++) {
				JSONArray item;
				item = mData.getJSONArray(i);
				int itemId = item.getInt(0);
				int userId = item.getInt(1);
				String imageBase64 = item.getString(2);
				Log.v(TAG, "@@@ itemId:" + itemId);
				Log.v(TAG, "@@@ userId:" + userId);
				Log.v(TAG, "@@@ imageBase64:" + imageBase64.substring(0, 20));
				AutoGenerateItem.addNewItem(context, renderer, picker, light,
						itemId, imageBase64);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
