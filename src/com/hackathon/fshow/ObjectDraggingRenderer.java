package com.hackathon.fshow;

import java.util.ArrayList;

import javax.microedition.khronos.opengles.GL10;

import org.json.JSONArray;

import rajawali.Object3D;
import rajawali.lights.DirectionalLight;
import rajawali.math.Matrix4;
import rajawali.math.vector.Vector3;
import rajawali.renderer.RajawaliRenderer;
import rajawali.util.GLU;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;
import android.content.Context;
import android.util.Log;

import com.hackathon.fshow.module.AutoGenerateItem;
import com.hackathon.fshow.module.MyPlane;

public class ObjectDraggingRenderer extends RajawaliRenderer implements
		OnObjectPickedListener {
	private static final String TAG = "ObjectDraggingRenderer";

	public ObjectDraggingRenderer(Context context) {
		super(context);
		setFrameRate(60);
	}

	private ObjectColorPicker mPicker;
	private Object3D mSelectedObject;
	private int[] mViewport;
	private double[] mNearPos4;
	private double[] mFarPos4;
	private Vector3 mNearPos;
	private Vector3 mFarPos;
	private Vector3 mNewObjPos;
	private Matrix4 mViewMatrix;
	private Matrix4 mProjectionMatrix;
	private JSONArray mData;

	protected void initScene() {
		mViewport = new int[] { 0, 0, mViewportWidth, mViewportHeight };
		mNearPos4 = new double[4];
		mFarPos4 = new double[4];
		mNearPos = new Vector3();
		mFarPos = new Vector3();
		mNewObjPos = new Vector3();
		mViewMatrix = getCurrentCamera().getViewMatrix();
		mProjectionMatrix = getCurrentCamera().getProjectionMatrix();

		mPicker = new ObjectColorPicker(this);
		mPicker.setOnObjectPickedListener(this);

//		try {
//			Material material = new Material();
//			material.enableLighting(true);
//			material.setDiffuseMethod(new DiffuseMethod.Lambert());
//			material.addTexture(new Texture("rajawaliTex",
//					R.drawable.ic_launcher));
//			material.setColorInfluence(0);
//			for (int i = 0; i < 20; i++) {
//				Plane plane = new Plane();
//				plane.setDoubleSided(true);
//				plane.setMaterial(material);
//				plane.setX(-4 + (Math.random() * 8));
//				plane.setY(-4 + (Math.random() * 8));
//				plane.setZ(-2 + (Math.random() * -6));
//				plane.setRotY(180);
//				mPicker.registerObject(plane);
//				addChild(plane);
//
//				// Sphere sphere = new Sphere(.3f, 12, 12);
//				// sphere.setMaterial(material);
//				// sphere.setColor(0x333333 + (int) (Math.random() * 0xcccccc));
//				// sphere.setX(-4 + (Math.random() * 8));
//				// sphere.setY(-4 + (Math.random() * 8));
//				// sphere.setZ(-2 + (Math.random() * -6));
//				// sphere.setDrawingMode(GLES20.GL_LINE_LOOP);
//				// mPicker.registerObject(sphere);
//				// addChild(sphere);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		mViewport[2] = mViewportWidth;
		mViewport[3] = mViewportHeight;
		mViewMatrix = getCurrentCamera().getViewMatrix();
		mProjectionMatrix = getCurrentCamera().getProjectionMatrix();
	}

	@Override
	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		if (isRefresh) {
			showItems(mData);
			isRefresh = false;
		}
	}
	public void getObjectAt(float x, float y) {
		mPicker.getObjectAt(x, y);
	}

	public void onObjectPicked(Object3D object) {
		mSelectedObject = object;
	}

	public void moveSelectedObject(float x, float y) {
		if (mSelectedObject == null)
			return;

		//
		// -- unproject the screen coordinate (2D) to the camera's near plane
		//

		GLU.gluUnProject(x, mViewportHeight - y, 0,
				mViewMatrix.getDoubleValues(), 0,
				mProjectionMatrix.getDoubleValues(), 0, mViewport, 0,
				mNearPos4, 0);

		//
		// -- unproject the screen coordinate (2D) to the camera's far plane
		//

		GLU.gluUnProject(x, mViewportHeight - y, 1.f,
				mViewMatrix.getDoubleValues(), 0,
				mProjectionMatrix.getDoubleValues(), 0, mViewport, 0, mFarPos4,
				0);

		//
		// -- transform 4D coordinates (x, y, z, w) to 3D (x, y, z) by dividing
		// each coordinate (x, y, z) by w.
		//

		mNearPos.setAll(mNearPos4[0] / mNearPos4[3], mNearPos4[1]
				/ mNearPos4[3], mNearPos4[2] / mNearPos4[3]);
		mFarPos.setAll(mFarPos4[0] / mFarPos4[3], mFarPos4[1] / mFarPos4[3],
				mFarPos4[2] / mFarPos4[3]);

		//
		// -- now get the coordinates for the selected object
		//

		double factor = (Math.abs(mSelectedObject.getZ()) + mNearPos.z)
				/ (getCurrentCamera().getFarPlane() - getCurrentCamera()
						.getNearPlane());

		mNewObjPos.setAll(mFarPos);
		mNewObjPos.subtract(mNearPos);
		mNewObjPos.multiply(factor);
		mNewObjPos.add(mNearPos);

		mSelectedObject.setX(mNewObjPos.x);
		mSelectedObject.setY(mNewObjPos.y);
	}

	public void stopMovingSelectedObject() {
		mSelectedObject = null;
	}

//	public DirectionalLight getLight() {
//		return light;
//	}

	public ObjectColorPicker getPicker() {
		return mPicker;
	}

	public void setData(JSONArray data) {
		Log.v(TAG, "@@@ setData");
		this.mData = data;
	}

	public Object3D getSelectedObject() {
		return mSelectedObject;
	}

	private ArrayList<Object3D> mListChild = new ArrayList<Object3D>();
	private DirectionalLight light;
	private boolean isRefresh;
	private Object mCamAnim;

	@Override
	public boolean addChild(Object3D child) {
		mListChild.add(child);
		return super.addChild(child);
	}

	public void removeAllChild() {
		for (Object3D item : mListChild) {
			removeChild(item);
		}
		mListChild.clear();
	}
	public Object3D getChildByName(String name) {
		Object3D result = null;
		for (Object3D obj : mListChild) {
			MyPlane myPlane = ((MyPlane) obj);
			if (myPlane.getName().equals(name)) {
				result = obj;
				break;
			}
		}
		return result;
	}
	public void showItems(JSONArray data) {
		removeAllChild();
		AutoGenerateItem.showItems(mContext, this, mPicker, light, data);
	}

	public void refresh() {
		isRefresh = true;
	}

	public void pauseCamAnim() {
//		mCamAnim.pause();
	}

	public void playCamAnim() {
//		mCamAnim.play();
	}

}
