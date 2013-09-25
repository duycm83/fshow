package com.hackathon.fshow.module;

import rajawali.lights.DirectionalLight;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
import rajawali.primitives.Plane;
import rajawali.util.ObjectColorPicker;
import ImageUtils.ImageUtils;
import android.content.Context;
import android.graphics.Bitmap;

import com.hackathon.fshow.ObjectDraggingRenderer;
import com.hackathon.fshow.planes.PlanesGaloreMaterial;

public class AutoGenerateItem {

	public static void addNewObject(Context ctx, ObjectDraggingRenderer renderer, ObjectColorPicker picker, DirectionalLight light) {
		try {
			for (int i = 1; i <= 20; i++) {
				PlanesGaloreMaterial planesGaloreMaterial = new PlanesGaloreMaterial();
				String name = String.format("image%02d", i);
				String str01 = ImageUtils.readRawTextFile(ctx, name);
				Bitmap bmp01 = ImageUtils.decodeToImage(str01);
				if (bmp01 == null) continue;
				planesGaloreMaterial.addTexture(new Texture("duy",bmp01));
				Plane plane = new Plane();
				plane.addLight(light);
				plane.setMaterial(planesGaloreMaterial);
				plane.setColor(0x333333 + (int) (Math.random() * 0xcccccc));
				plane.setX(-4 + (float) (Math.random() * 8));
				plane.setY(-4 + (float) (Math.random() * 8));
				plane.setZ(-2 + (float) (Math.random() * -6));
				picker.registerObject(plane);
				renderer.addChild(plane);
			}
			
			
		} catch (TextureException e) {
			e.printStackTrace();
		}
	}

}
