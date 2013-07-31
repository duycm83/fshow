package com.hackathon.fshow;

import rajawali.RajawaliActivity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends RajawaliActivity {

	private static final String FRAGMENT_TAG = "MainActivity";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Configure the action bar
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		launchFragment(Optimized2000PlanesFragment.class);
	}

	@Override
	protected void onDestroy() {
		try {
			super.onDestroy();
		} catch (Exception e) {
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // メニューの要素を追加
	    menu.add("Normal item");
	 
	    // メニューの要素を追加して取得
	    MenuItem actionItem = menu.add("Action Button");
	 
	    // SHOW_AS_ACTION_IF_ROOM:余裕があれば表示
	    actionItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
	 
	    // アイコンを設定
	    actionItem.setIcon(android.R.drawable.ic_menu_share);
	 
	    return true;
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    Toast.makeText(this, "Selected Item: " + item.getTitle(), Toast.LENGTH_SHORT).show();
	    return true;
	}
	
	/**
	 * Launch a fragment selected from the drawer or at application start.
	 * 
	 * @param fragClass
	 */
	private void launchFragment(Class<? extends AExampleFragment> fragClass) {
		final FragmentManager fragmentManager = getFragmentManager();
		final Fragment fragment;

		final FragmentTransaction transaction = fragmentManager
				.beginTransaction();
		try {
			Fragment oldFrag = fragmentManager.findFragmentByTag(FRAGMENT_TAG);
			if (oldFrag != null)
				transaction.remove(oldFrag);

			fragment = (Fragment) fragClass.getConstructors()[0].newInstance();
			transaction.add(R.id.content_frame, fragment, FRAGMENT_TAG);
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
