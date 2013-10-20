package com.hackathon.fshow;

import org.json.JSONArray;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.hackathon.fshow.module.DownloadMapAsyncTask;
import com.hackathon.fshow.module.MyPlane;

public class ObjectDraggingActivity extends RajawaliExampleActivity implements OnTouchListener {
	private static final String TAG = "ObjectDraggingActivity";
	private ObjectDraggingRenderer mRenderer;
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		mScreenWidth = size.x;
		mScreenHeight = size.y;
		mRenderer = new ObjectDraggingRenderer(this);
		mRenderer.setSurfaceView(mSurfaceView);
		super.setRenderer(mRenderer);
		mSurfaceView.setOnTouchListener(this);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
        ll.setGravity(Gravity.BOTTOM);

//        TextView label = new TextView(this);
//        label.setText("Touch & drag");
//        label.setTextSize(20);
//        label.setGravity(Gravity.CENTER_HORIZONTAL);
//        label.setHeight(100);
//        ll.addView(label);
        
//        LinearLayout dropArea = new LinearLayout(this);
//        dropArea.setBackgroundColor(Color.TRANSPARENT);
//        dropArea.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 130));
//        dropArea.setGravity(Gravity.CENTER_HORIZONTAL);
       
        LinearLayout dropArea = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.drag_area, null);
      dropArea.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 130));
      dropArea.setGravity(Gravity.CENTER_HORIZONTAL);
        ll.addView(dropArea);
        
        mLayout.addView(ll);
		
        new DownloadMapAsyncTask(this, mRenderer).execute();
		initLoader();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
	}
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		switch(event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mRenderer.getObjectAt(x, y);
				break;
			case MotionEvent.ACTION_MOVE:
				mRenderer.moveSelectedObject(x, y);
				if (y > (mScreenHeight -200)) {
					String name = ((MyPlane)mRenderer.getSelectedObject()).getName();
					Log.v(TAG, "@@@selected "+name);
				}
				break;
			case MotionEvent.ACTION_UP:
				mRenderer.stopMovingSelectedObject();
				break;
		}
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.action_menu, menu);
	    return true;
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
	    int id = item.getItemId();
	    if (id == R.id.action_user) {
	    	Intent intent = new Intent(this, UserLoginActivity.class);
	    	startActivity(intent);
	    } else if (id == R.id.action_new) {
	    	Intent intent = new Intent(this, ItemResgisterActivity.class);
	    	startActivity(intent);
	    }
	    return true;
	}

	public void duy(JSONArray items) {
//		mRenderer = new ObjectDraggingRenderer(this, items);
//		mRenderer.setSurfaceView(mSurfaceView);
//		super.setRenderer(mRenderer);
//		mSurfaceView.setOnTouchListener(this);
		
	}
}
