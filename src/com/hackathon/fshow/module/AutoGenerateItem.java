package com.hackathon.fshow.module;

import java.util.LinkedList;

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

	public static void addNewObject(Context ctx, ObjectDraggingRenderer renderer, ObjectColorPicker picker, DirectionalLight light) {
		try {
			for (int i = 0; i < 100; i++) {
				PlanesGaloreMaterial planesGaloreMaterial = new PlanesGaloreMaterial();
				String name = String.format("image%02d", i);
				String str01 = ImageUtils.readRawTextFile(ctx, name);
				Bitmap bmp01 = ImageUtils.decodeToImage(str01);
				if (bmp01 == null) {
					Log.e(TAG, "@@@error name:"+name);
					continue;
				}
				planesGaloreMaterial.addTexture(new Texture("duy",bmp01));
				Plane plane = new Plane();
				plane.addLight(light);
				plane.setMaterial(planesGaloreMaterial);
				plane.setColor(0x333333 + (int) (Math.random() * 0xcccccc));
//				plane.setX(-4 + (float) (Math.random() * 8));
//				plane.setY(-4 + (float) (Math.random() * 8));
//				plane.setZ(-2 + (float) (Math.random() * -6));
				plane.setX(-4 + (float) (Math.random() * 8));
				plane.setY(-4 + (float) (Math.random() * 8));
				if (i < 30) {
					plane.setZ(Layer.LAYER1_MINZ + (float) (Math.random() * -Layer.LAYER1_MAXZ));
				} else if (i < 60) {
					plane.setZ(Layer.LAYER2_MINZ + (float) (Math.random() * -Layer.LAYER2_MAXZ));
				} else {
					plane.setZ(Layer.LAYER3_MINZ + (float) (Math.random() * -Layer.LAYER3_MAXZ));
				}
				picker.registerObject(plane);
				renderer.addChild(plane);
			}
			
			
		} catch (TextureException e) {
			e.printStackTrace();
		}
	}
	
	public static void makeTestDataObject(Context ctx, ObjectDraggingRenderer renderer, ObjectColorPicker picker, DirectionalLight light) {
		try {
			LinkedList list = new LinkedList();
			list.add("foo");
			list.add(new Integer(100));
			list.add(new Double(1000.21));
			list.add(new Boolean(true));
			list.add(null);
			String jsonText = JSONValue.toJSONString(list);
			Log.v(TAG, "@@@"+jsonText);
			
			for (int i = 0; i < 100; i++) {
				PlanesGaloreMaterial planesGaloreMaterial = new PlanesGaloreMaterial();
				String name = String.format("image%02d", i);
				String str01 = ImageUtils.readRawTextFile(ctx, name);
				Bitmap bmp01 = ImageUtils.decodeToImage(str01);
				if (bmp01 == null) {
					Log.e(TAG, "@@@error name:"+name);
					continue;
				}
				planesGaloreMaterial.addTexture(new Texture("duy",bmp01));
				Plane plane = new Plane();
				plane.addLight(light);
				plane.setMaterial(planesGaloreMaterial);
				plane.setColor(0x333333 + (int) (Math.random() * 0xcccccc));
//				plane.setX(-4 + (float) (Math.random() * 8));
//				plane.setY(-4 + (float) (Math.random() * 8));
//				plane.setZ(-2 + (float) (Math.random() * -6));
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

}
