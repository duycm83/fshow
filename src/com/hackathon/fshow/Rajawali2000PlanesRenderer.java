package com.hackathon.fshow;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import rajawali.BaseObject3D;
import rajawali.animation.Animation3D.RepeatMode;
import rajawali.animation.TranslateAnimation3D;
import rajawali.curves.CatmullRomCurve3D;
import rajawali.lights.DirectionalLight;
import rajawali.materials.textures.ATexture.TextureException;
import rajawali.materials.textures.Texture;
import rajawali.math.Vector3;
import rajawali.renderer.RajawaliRenderer;
import android.content.Context;
import android.opengl.GLU;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.example.fshow.planes.PlanesGalore;
import com.example.fshow.planes.PlanesGaloreMaterial;

/**
 * This example shows how you can create a large number of textured planes efficiently.
 * The slow way is creating 2000 Plane objects and 16 separate textures. The optimized way
 * is to create one BaseObject3D with the vertex data for 2000 planes in one buffer (and
 * the same for tex coord data, normal data, etc). Each single plane is given the same position
 * at (0, 0, 0). Extra buffers are created for each plane's position and rotation.
 * 
 * Only one texture is used. It's a 1024*1024 bitmap containing 16 256*256 images. This is
 * called a 'texture atlas'. Each plane is assigned a specific portion of this texture.
 * 
 * This is much faster than creating separate object and textures because the shader program 
 * needs to be created once, only one texture has to be uploaded, matrix transformations need
 * to be done only once on the cpu, etc.
 * 
 * @author dennis.ippel
 *
 */
public class Rajawali2000PlanesRenderer extends RajawaliRenderer {
	private static final String TAG = "Rajawali2000PlanesRenderer";
	private PlanesGalore mPlanes;
	private PlanesGaloreMaterial mMaterial;
	private long mStartTime;
	private TranslateAnimation3D mCamAnim;

	public Rajawali2000PlanesRenderer(Context context) {
		super(context);
		setFrameRate(60);
	}

	protected void initScene() {
		DirectionalLight light = new DirectionalLight(0, 0, 1);
		//		getCurrentCamera().setPosition(0, 0, -16);
		getCurrentCamera().setPosition(0, 0, 10);

		mPlanes = new PlanesGalore();
		try {
			mMaterial = (PlanesGaloreMaterial) mPlanes.getMaterial();
			mMaterial.addTexture(new Texture(R.drawable.flickrpics));
		} catch (TextureException e) {
			e.printStackTrace();
		}
		mPlanes.addLight(light);
		mPlanes.setDoubleSided(true);
		mPlanes.setZ(4);
		addChild(mPlanes);

		BaseObject3D empty = new BaseObject3D();
		addChild(empty);

		CatmullRomCurve3D path = new CatmullRomCurve3D();
		path.addPoint(new Vector3(-4, 0, -20));
		path.addPoint(new Vector3(2, 1, -10));
		path.addPoint(new Vector3(-2, 0, 10));
		path.addPoint(new Vector3(0, -4, 20));
		path.addPoint(new Vector3(5, 10, 30));
		path.addPoint(new Vector3(-2, 5, 40));
		path.addPoint(new Vector3(3, -1, 60));
		path.addPoint(new Vector3(5, -1, 70));

		mCamAnim = new TranslateAnimation3D(path);
		mCamAnim.setDuration(20000);
		mCamAnim.setRepeatMode(RepeatMode.REVERSE_INFINITE);
		mCamAnim.setTransformable3D(getCurrentCamera());
		mCamAnim.setInterpolator(new AccelerateDecelerateInterpolator());
		registerAnimation(mCamAnim);

		getCurrentCamera().setLookAt(new Vector3(0, 0, 30));
	}

	public void onSurfaceCreated(GL10 gl, EGLConfig config) {
		((RajawaliExampleActivity) mContext).showLoader();
		super.onSurfaceCreated(gl, config);
		((RajawaliExampleActivity) mContext).hideLoader();
		mStartTime = System.currentTimeMillis();
		//		mCamAnim.play();
	}

	public void onDrawFrame(GL10 glUnused) {
		super.onDrawFrame(glUnused);
		mMaterial.setTime((System.currentTimeMillis() - mStartTime) / 1000f);
	}

	public PlanesGalore getPlanesGalore() {
		return mPlanes;
	}

	public void getObjectAt(float x, float y) {
		float[] mViewMatrix = getCurrentCamera().getViewMatrix();
		float[] mProjectionMatrix = getCurrentCamera().getProjectionMatrix();
		float[] vector = new float[4];
		int[] mViewport = new int[] { 0, 0, mViewportWidth, mViewportHeight };
		;
		float[] mNearPos4 = new float[4];
		;
		// x and y come from the x/y you get from the OnTouchListener. 
		// OpenGL works from the bottom left corner, so flip the y
		//		GLU.gluUnProject(
		//				x,
		//				mSurfaceView.getHeight() - y,
		//				0.9f,
		//				mViewMatrix,
		//				0,
		//				mProjectionMatrix,
		//				0,
		//				new int[] { mSurfaceView.getTop(), mSurfaceView.getLeft(), mSurfaceView.getWidth(),
		//						mSurfaceView.getHeight() }, 0,
		//				vector, 0);

		GLU.gluUnProject(x, mViewportHeight - y, 0, mViewMatrix, 0,
				mProjectionMatrix, 0, mViewport, 0, mNearPos4, 0);

		Log.v(TAG, String.format("%f, %f, %f, %f",
				mNearPos4[0], mNearPos4[1], mNearPos4[2], mNearPos4[3]));

		mPlanes.getZ(mNearPos4[0], mNearPos4[1]);
	}

}
