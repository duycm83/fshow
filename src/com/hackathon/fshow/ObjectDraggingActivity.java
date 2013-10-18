package com.hackathon.fshow;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ObjectDraggingActivity extends RajawaliExampleActivity implements OnTouchListener {
	private ObjectDraggingRenderer mRenderer;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
        
        mLayout.addView(ll);
		
		initLoader();
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			mRenderer.getObjectAt(event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			mRenderer.moveSelectedObject(event.getX(), event.getY());
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
}
