package com.hackathon.fshow;

import rajawali.Object3D;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

import com.hackathon.fshow.module.DownloadMapAsyncTask;
import com.hackathon.fshow.module.MyPlane;

public class ObjectDraggingActivity extends RajawaliExampleActivity implements
		OnTouchListener {
	private static final String TAG = "ObjectDraggingActivity";
	private ObjectDraggingRenderer mRenderer;
	private int mScreenWidth = 0;
	private int mScreenHeight = 0;
	private LinearLayout dropArea = null;
	private double objx, objy, objz;
	private LinearLayout dragArea = null;
	private boolean isMoveFirst = false;
	private boolean isInDropArea = false;
	public static String sUserId = "0";
	private int mScreenType = 1; // 1=all; 2=mine

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

		// TextView label = new TextView(this);
		// label.setText("Touch & drag");
		// label.setTextSize(20);
		// label.setGravity(Gravity.CENTER_HORIZONTAL);
		// label.setHeight(100);
		// ll.addView(label);

		LinearLayout dragArea = (LinearLayout) LayoutInflater.from(this)
				.inflate(R.layout.drag_drop_area, null);
		dragArea.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		dragArea.setGravity(Gravity.CENTER_HORIZONTAL);
		ll.addView(dragArea);
		dropArea = (LinearLayout) dragArea.findViewById(R.id.dropArea);
		mLayout.addView(ll);

		new DownloadMapAsyncTask(this, mRenderer).execute();
		initLoader();

		dragArea = (LinearLayout) dragArea.findViewById(R.id.dragArea);
		setDragEvent(dragArea);
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX();
		float y = event.getY();
		Object3D selectedObject = mRenderer.getSelectedObject();
		MyPlane myPlane = null;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mRenderer.getObjectAt(x, y);
			break;
		case MotionEvent.ACTION_MOVE:
			mRenderer.moveSelectedObject(x, y);
			if (selectedObject == null) {
				return true;
			} else if (isMoveFirst == false) {
				myPlane = (MyPlane) selectedObject;
				mRenderer.pauseCamAnim();
				objx = selectedObject.getX();
				objy = selectedObject.getY();
				objz = selectedObject.getZ();
				isMoveFirst = true;
			}
			if (y > (mScreenHeight - 200)) {
				if (isInDropArea == false) {
					vibrate();
					isInDropArea = true;
				}
				myPlane = (MyPlane) selectedObject;
				String name = myPlane.getName();
				Log.v(TAG, "@@@selected " + name);
			} else {
				isInDropArea = false;
			}
			break;
		case MotionEvent.ACTION_UP:
			mRenderer.playCamAnim();
			if (isInDropArea && selectedObject != null) {
				myPlane = (MyPlane) selectedObject;
				Bitmap bm = myPlane.getBitmap();
				ImageView imageView = new ImageView(this);
				imageView.setClickable(true);
				imageView.setOnTouchListener(mOnTouchListener);
				imageView.setImageBitmap(bm);
				imageView.setTag(myPlane.getName());
				dropArea.addView(imageView);
				selectedObject.setX(objx);
				selectedObject.setY(objy);
				selectedObject.setZ(objz);
				isMoveFirst = false;
			}
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
		Toast.makeText(this, "Selected Item: " + item.getTitle(),
				Toast.LENGTH_SHORT).show();
		int id = item.getItemId();
		if (id == R.id.action_user) {
			Intent intent = new Intent(this, UserLoginActivity.class);
			startActivityForResult(intent, 1);
		} else if (id == R.id.action_new) {
			Intent intent = new Intent(this, ItemResgisterActivity.class);
			startActivityForResult(intent, 2);
		} else if (id == R.id.action_myitem) {
			new DownloadMapAsyncTask(this, mRenderer).execute(sUserId);
			mScreenType = 2;
		} else if (id == R.id.action_allitem) {
			mScreenType = 1;
			new DownloadMapAsyncTask(this, mRenderer).execute();
		} else if (id == R.id.action_update) {
			if (mScreenType == 1) {
				new DownloadMapAsyncTask(this, mRenderer).execute();
			} else if (mScreenType == 2) {
				new DownloadMapAsyncTask(this, mRenderer).execute(sUserId);
			}
		}
		return true;
	}

	private void vibrate() {
		Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 50 milliseconds
		v.vibrate(50);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 2) {
			if (mScreenType == 1) {
				new DownloadMapAsyncTask(this, mRenderer).execute();
			} else if (mScreenType == 2) {
				new DownloadMapAsyncTask(this, mRenderer).execute(sUserId);
			}
		}
	}

	long backcount = 0;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (backcount == 0
					|| (System.currentTimeMillis() - backcount > 2000)) {
				Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT)
						.show();
				backcount = System.currentTimeMillis();
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public void coordinate(View v) {
		Toast.makeText(this, "coordinate saved", Toast.LENGTH_LONG).show();
	}

	private OnTouchListener mOnTouchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				ClipData data = ClipData.newPlainText("msg",
						(CharSequence) v.getTag());
				v.startDrag(data, new DragShadowBuilder(v), null, 0);
				return true;
			}
			return false;
		}
	};

	private void setDragEvent(LinearLayout groupOfView) {
		OnDragListener onDragListener = new OnDragListener() {
			@Override
			public boolean onDrag(View v, DragEvent event) {
				// Log.v(TAG, "@@@ onDrag:"+event.getAction());
				if (event.getAction() == DragEvent.ACTION_DROP) {
					Log.v(TAG, "@@@ drop");
					ClipData data = event.getClipData();
					Item item = data.getItemAt(0);
					if (item != null) {
						String objName = (String) item.getText();
						Object3D obj = mRenderer.getChildByName(objName);
						if (obj != null && obj instanceof MyPlane) {
							ImageView imageView = (ImageView) v;
							Bitmap bmp = ((MyPlane) obj).getBitmap();
							imageView.setImageBitmap(bmp);
						}
					}
				} else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
					if (!event.getResult()) {
					}
					Log.v(TAG, "@@@ drag end");
				}
				return true;
			}
		};

		OnTouchListener onTouchListener = new OnTouchListener() {
			long firstTouch = 0;
			View touchView1 = null;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					if (touchView1 == null) {
						touchView1 = v;
					}
					long sub = System.currentTimeMillis() - firstTouch;
					if (sub <= 1000) {
						if (touchView1 == v) {
							((ImageView) v)
									.setImageResource(R.drawable.t_shirt);
							touchView1 = null;
						} else {
							touchView1 = v;
						}

					} else {
						touchView1 = null;
					}
					firstTouch = System.currentTimeMillis();
				}
				return true;
			}
		};

		int count = groupOfView.getChildCount();
		for (int i = 0; i < count; i++) {
			View child = groupOfView.getChildAt(i);
			if (child instanceof LinearLayout) {
				int count2 = ((LinearLayout) child).getChildCount();
				for (int j = 0; j < count2; j++) {
					View child2 = ((LinearLayout) child).getChildAt(j);
					if (child2 instanceof ImageView) {
						child2.setOnDragListener(onDragListener);
						child2.setOnTouchListener(onTouchListener);
					}
				}

			}
		}
	}
}
