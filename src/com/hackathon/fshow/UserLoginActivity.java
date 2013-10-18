package com.hackathon.fshow;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class UserLoginActivity extends Activity implements OnClickListener {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_login);
		findViewById(R.id.buttonLogin).setOnClickListener(this);
		findViewById(R.id.buttonSignup).setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.buttonLogin) {
			
		} else if (id == R.id.buttonSignup) {
	    	Intent intent = new Intent(this, UserRegisterAcitvity.class);
	    	startActivity(intent);
		}
	}
}
