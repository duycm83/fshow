package com.hackathon.fshow.module;

import rajawali.lights.DirectionalLight;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
import rajawali.primitives.Plane;
import rajawali.util.ObjectColorPicker;

import com.hackathon.fshow.ObjectDraggingRenderer;
import com.hackathon.fshow.R;
import com.hackathon.fshow.planes.PlanesGaloreMaterial;

public class AutoGenerateItem {

	public static void addNewObject(ObjectDraggingRenderer renderer, ObjectColorPicker picker, DirectionalLight light) {
		PlanesGaloreMaterial[] planesGaloreMaterials = new PlanesGaloreMaterial[20];
		planesGaloreMaterials[0] = new PlanesGaloreMaterial();
		try {
			planesGaloreMaterials[0].addTexture(new Texture(R.drawable.item_01));
			planesGaloreMaterials[1] = new PlanesGaloreMaterial();
			planesGaloreMaterials[1].addTexture(new Texture(R.drawable.item_02));
			planesGaloreMaterials[2] = new PlanesGaloreMaterial();
			planesGaloreMaterials[2].addTexture(new Texture(R.drawable.item_03));
			planesGaloreMaterials[3] = new PlanesGaloreMaterial();
			planesGaloreMaterials[3].addTexture(new Texture(R.drawable.item_04));
			planesGaloreMaterials[4] = new PlanesGaloreMaterial();
			planesGaloreMaterials[4].addTexture(new Texture(R.drawable.item_05));
			planesGaloreMaterials[5] = new PlanesGaloreMaterial();
			planesGaloreMaterials[5].addTexture(new Texture(R.drawable.item_06));
			planesGaloreMaterials[6] = new PlanesGaloreMaterial();
			planesGaloreMaterials[6].addTexture(new Texture(R.drawable.item_07));
			planesGaloreMaterials[7] = new PlanesGaloreMaterial();
			planesGaloreMaterials[7].addTexture(new Texture(R.drawable.item_08));
			planesGaloreMaterials[8] = new PlanesGaloreMaterial();
			planesGaloreMaterials[8].addTexture(new Texture(R.drawable.item_09));
			planesGaloreMaterials[9] = new PlanesGaloreMaterial();
			planesGaloreMaterials[9].addTexture(new Texture(R.drawable.item_10));
			planesGaloreMaterials[10] = new PlanesGaloreMaterial();
			planesGaloreMaterials[10].addTexture(new Texture(R.drawable.item_11));
			planesGaloreMaterials[11] = new PlanesGaloreMaterial();
			planesGaloreMaterials[11].addTexture(new Texture(R.drawable.item_12));
			planesGaloreMaterials[12] = new PlanesGaloreMaterial();
			planesGaloreMaterials[12].addTexture(new Texture(R.drawable.item_13));
			planesGaloreMaterials[13] = new PlanesGaloreMaterial();
			planesGaloreMaterials[13].addTexture(new Texture(R.drawable.item_14));
			planesGaloreMaterials[14] = new PlanesGaloreMaterial();
			planesGaloreMaterials[14].addTexture(new Texture(R.drawable.item_15));
			planesGaloreMaterials[15] = new PlanesGaloreMaterial();
			planesGaloreMaterials[15].addTexture(new Texture(R.drawable.item_16));
			planesGaloreMaterials[16] = new PlanesGaloreMaterial();
			planesGaloreMaterials[16].addTexture(new Texture(R.drawable.item_17));
			planesGaloreMaterials[17] = new PlanesGaloreMaterial();
			planesGaloreMaterials[17].addTexture(new Texture(R.drawable.item_18));
			planesGaloreMaterials[18] = new PlanesGaloreMaterial();
			planesGaloreMaterials[18].addTexture(new Texture(R.drawable.item_19));
			planesGaloreMaterials[19] = new PlanesGaloreMaterial();
			planesGaloreMaterials[19].addTexture(new Texture(R.drawable.item_20));
		} catch (TextureException e) {
			e.printStackTrace();
		}
		for (int i = 0; i < 20; i++) {
			Plane plane = new Plane();
			plane.addLight(light);
			plane.setMaterial(planesGaloreMaterials[i]);
			plane.setColor(0x333333 + (int) (Math.random() * 0xcccccc));
			plane.setX(-4 + (float) (Math.random() * 8));
			plane.setY(-4 + (float) (Math.random() * 8));
			plane.setZ(-2 + (float) (Math.random() * -6));
			picker.registerObject(plane);
			renderer.addChild(plane);
		}
	}

}
