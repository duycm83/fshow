package com.hackathon.fshow.module;

import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.simple.JSONValue;

import rajawali.lights.DirectionalLight;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
import rajawali.primitives.Plane;
import rajawali.util.ObjectColorPicker;
import ImageUtils.ImageUtils;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.hackathon.fshow.ObjectDraggingRenderer;
import com.hackathon.fshow.planes.PlanesGaloreMaterial;

public class AutoGenerateItem {

	private static final String TAG = "AutoGenerateItem";

	public static void addNewObject(Context ctx,
			ObjectDraggingRenderer renderer, ObjectColorPicker picker,
			DirectionalLight light) {
		try {
			for (int i = 0; i < 100; i++) {
				PlanesGaloreMaterial planesGaloreMaterial = new PlanesGaloreMaterial();
				String name = String.format("image%02d", i);
				String str01 = ImageUtils.readRawTextFile(ctx, name);
				Bitmap bmp01 = ImageUtils.decodeToImage(str01);
				if (bmp01 == null) {
					Log.e(TAG, "@@@error name:" + name);
					continue;
				}
				planesGaloreMaterial.addTexture(new Texture("duy", bmp01));
				Plane plane = new Plane();
				plane.addLight(light);
				plane.setMaterial(planesGaloreMaterial);
				plane.setColor(0x333333 + (int) (Math.random() * 0xcccccc));
				// plane.setX(-4 + (float) (Math.random() * 8));
				// plane.setY(-4 + (float) (Math.random() * 8));
				// plane.setZ(-2 + (float) (Math.random() * -6));
				plane.setX(-4 + (float) (Math.random() * 8));
				plane.setY(-4 + (float) (Math.random() * 8));
				if (i < 30) {
					plane.setZ(Layer.LAYER1_MINZ
							+ (float) (Math.random() * -Layer.LAYER1_MAXZ));
				} else if (i < 60) {
					plane.setZ(Layer.LAYER2_MINZ
							+ (float) (Math.random() * -Layer.LAYER2_MAXZ));
				} else {
					plane.setZ(Layer.LAYER3_MINZ
							+ (float) (Math.random() * -Layer.LAYER3_MAXZ));
				}
				picker.registerObject(plane);
				renderer.addChild(plane);
			}

		} catch (TextureException e) {
			e.printStackTrace();
		}
	}

	public static void addNewItem(Context ctx, ObjectDraggingRenderer renderer,
			ObjectColorPicker picker, DirectionalLight light, int itemId,
			String imageBase64) {
		try {
			PlanesGaloreMaterial planesGaloreMaterial = new PlanesGaloreMaterial();
			String name = String.format("image%04d", itemId);
			Bitmap bmp01 = ImageUtils.decodeToImage(imageBase64);
			if (bmp01 == null) {
				Log.e(TAG, "@@@error name:" + name);
				return;
			}
			planesGaloreMaterial.addTexture(new Texture("duy", bmp01));
			Plane plane = new Plane();
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

	public static void makeTestDataObject(Context ctx,
			ObjectDraggingRenderer renderer, ObjectColorPicker picker,
			DirectionalLight light) {
		try {
			LinkedList list = new LinkedList();
			list.add("foo");
			list.add(new Integer(100));
			list.add(new Double(1000.21));
			list.add(new Boolean(true));
			list.add(null);
			String jsonText = JSONValue.toJSONString(list);
			Log.v(TAG, "@@@" + jsonText);

			for (int i = 0; i < 100; i++) {
				PlanesGaloreMaterial planesGaloreMaterial = new PlanesGaloreMaterial();
				String name = String.format("image%02d", i);
				String str01 = ImageUtils.readRawTextFile(ctx, name);
				Bitmap bmp01 = ImageUtils.decodeToImage(str01);
				if (bmp01 == null) {
					Log.e(TAG, "@@@error name:" + name);
					continue;
				}
				planesGaloreMaterial.addTexture(new Texture("duy", bmp01));
				Plane plane = new Plane();
				plane.addLight(light);
				plane.setMaterial(planesGaloreMaterial);
				plane.setColor(0x333333 + (int) (Math.random() * 0xcccccc));
				// plane.setX(-4 + (float) (Math.random() * 8));
				// plane.setY(-4 + (float) (Math.random() * 8));
				// plane.setZ(-2 + (float) (Math.random() * -6));
				plane.setX(-4 + (float) (Math.random() * 8));
				plane.setY(-4 + (float) (Math.random() * 8));
				plane.setZ(-6 + (float) (Math.random() * -12));
				picker.registerObject(plane);
				renderer.addChild(plane);
			}

		} catch (TextureException e) {
			e.printStackTrace();
		}
	}

	public static void showItems(Context context,
			ObjectDraggingRenderer renderer, ObjectColorPicker picker,
			DirectionalLight light, JSONArray mData) {

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
