package com.hackathon.fshow;

import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.json.JSONArray;

import rajawali.BaseObject3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.CatmullRomPath3D;
import rajawali.animation.TranslateAnimation3D;
import rajawali.lights.DirectionalLight;
import rajawali.math.Vector3;
import rajawali.renderer.RajawaliRenderer;
import rajawali.util.ObjectColorPicker;
import rajawali.util.OnObjectPickedListener;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLU;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.hackathon.fshow.module.AutoGenerateItem;
import com.hackathon.fshow.module.MyPlane;

public class ObjectDraggingRenderer extends RajawaliRenderer implements
		OnObjectPickedListener {
	private static final String TAG = "ObjectDraggingRenderer";
	private ObjectColorPicker mPicker;
	private BaseObject3D mSelectedObject;
	private int[] mViewport;
	private float[] mNearPos4;
	private float[] mFarPos4;
	private Vector3 mNearPos;
	private Vector3 mFarPos;
	private Vector3 mNewObjPos;
	private float[] mViewMatrix;
	private float[] mProjectionMatrix;
	private Context mContext;
	private JSONArray mData;
	public boolean isRefresh = false;
	DirectionalLight light = new DirectionalLight(0, 0, 1);

	public ObjectDraggingRenderer(Context context) {
		super(context);
		this.mContext = context;
		setFrameRate(60);
	}

	public ObjectDraggingRenderer(Context context, JSONArray data) {
		super(context);
		this.mContext = context;
		this.mData = data;
		setFrameRate(60);
	}

	int TIMEOUT = 60000;
	private TranslateAnimation3D mCamAnim;

	protected void initScene() {
		Log.v(TAG, "@@@ initScene");
		light.setColor(Color.BLUE);
		light.setPower(12);
		mViewport = new int[] { 0, 0, mViewportWidth, mViewportHeight };
		mNearPos4 = new float[4];
		mFarPos4 = new float[4];
		mNearPos = new Vector3();
		mFarPos = new Vector3();
		mNewObjPos = new Vector3();
		mViewMatrix = getCurrentCamera().getViewMatrix();
		mProjectionMatrix = getCurrentCamera().getProjectionMatrix();

		mPicker = new ObjectColorPicker(this);
		mPicker.setOnObjectPickedListener(this);
		
		
		CatmullRomPath3D path = new CatmullRomPath3D();
		path.addPoint(new Vector3(-4, 0, -2));
		path.addPoint(new Vector3(2, 1, -1));
		path.addPoint(new Vector3(-2, 0, 1));
		path.addPoint(new Vector3(0, -4, 2));
		path.addPoint(new Vector3(5, 10, 3));
		path.addPoint(new Vector3(-2, 5, 4));
		path.addPoint(new Vector3(3, -1, 6));
		path.addPoint(new Vector3(5, -1, 7));
		
		mCamAnim = new TranslateAnimation3D(path);
		mCamAnim.setDuration(20000);
		mCamAnim.setRepeatMode(RepeatMode.REVERSE_INFINITE);
		mCamAnim.setTransformable3D(getCurrentCamera());
		mCamAnim.setInterpolator(new AccelerateDecelerateInterpolator());
		registerAnimation(mCamAnim);
		
		getCurrentCamera().setLookAt(new Vector3(0,0,-2));	
	}
	
	@Override
	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		if (isRefresh) {
			showItems(mData);
			isRefresh = false;
		}
	}

	public void onSurfaceChanged(GL10 gl, int width, int height) {
		super.onSurfaceChanged(gl, width, height);
		mViewport[2] = mViewportWidth;
		mViewport[3] = mViewportHeight;
		mViewMatrix = getCurrentCamera().getViewMatrix();
		mProjectionMatrix = getCurrentCamera().getProjectionMatrix();
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		((RajawaliExampleActivity) mContext).showLoader();
		super.onSurfaceCreated(gl, config);
		((RajawaliExampleActivity) mContext).hideLoader();
		mCamAnim.play();
	}

	public void getObjectAt(float x, float y) {
		mPicker.getObjectAt(x, y);
	}

	public void onObjectPicked(BaseObject3D object) {
		mSelectedObject = object;
	}

	public void moveSelectedObject(float x, float y) {
		if (mSelectedObject == null)
			return;

		//
		// -- unproject the screen coordinate (2D) to the camera's near plane
		//

		int result = GLU.gluUnProject(x, mViewportHeight - y, 0, mViewMatrix,
				0, mProjectionMatrix, 0, mViewport, 0, mNearPos4, 0);

		//
		// -- unproject the screen coordinate (2D) to the camera's far plane
		//

		result = GLU.gluUnProject(x, mViewportHeight - y, 1.f, mViewMatrix, 0,
				mProjectionMatrix, 0, mViewport, 0, mFarPos4, 0);

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

		float factor = (Math.abs(mSelectedObject.getZ()) + mNearPos.z)
				/ (getCurrentCamera().getFarPlane() - getCurrentCamera()
						.getNearPlane());

		mNewObjPos.setAllFrom(mFarPos);
		mNewObjPos.subtract(mNearPos);
		mNewObjPos.multiply(factor);
		mNewObjPos.add(mNearPos);

		mSelectedObject.setX(mNewObjPos.x);
		mSelectedObject.setY(mNewObjPos.y);
	}

	public void stopMovingSelectedObject() {
		mSelectedObject = null;
	}

	public DirectionalLight getLight() {
		return light;
	}

	public ObjectColorPicker getPicker() {
		return mPicker;
	}

	public void setData(JSONArray data) {
		Log.v(TAG, "@@@ setData");
		this.mData = data;
	}
	
	public BaseObject3D getSelectedObject() {
		return mSelectedObject;
	}
	private ArrayList<BaseObject3D> mListChild = new ArrayList<BaseObject3D>();
	@Override
	public boolean addChild(BaseObject3D child) {
		mListChild.add(child);
		return super.addChild(child);
	}
	
	public void removeAllChild() {
		for (BaseObject3D item : mListChild) {
			removeChild(item);
		}
		mListChild.clear();
	}
	
	public BaseObject3D getChildByName(String name) {
		BaseObject3D result = null;
		for (BaseObject3D obj : mListChild) {
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
		mCamAnim.pause();
	}
	
	public void playCamAnim() {
		mCamAnim.play();
	}
	
}
