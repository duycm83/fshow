package com.hackathon.fshow;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class Rajawali2000PlanesActivity extends RajawaliExampleActivity
		implements OnTouchListener {
	private Rajawali2000PlanesRenderer mRenderer;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRenderer = new Rajawali2000PlanesRenderer(this);
		mRenderer.setSurfaceView(mSurfaceView);
		super.setRenderer(mRenderer);
		initLoader();
		mSurfaceView.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		Log.v("Rajawali2000PlanesActivity", "ontouch" + event.getAction());
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();
			mRenderer.getObjectAt(x, y);
//			float glX = ((event.getX() / (float) getDisplaysize().x) * 2.0f - 1.0f);
//			float glY = ((event.getY() / (float) getDisplaysize().y) * -3.0f + 1.5f);

//			Log.v(getLocalClassName(), String.format("x:%f y:%f", glX, glY));
//			mRenderer.getPlanesGalore().getZ(glX, glY);
		}

		return false;
	}
}
