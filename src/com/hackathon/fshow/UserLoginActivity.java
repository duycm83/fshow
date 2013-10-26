package com.hackathon.fshow;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class UserLoginActivity extends Activity implements OnClickListener {

	
	private static final String TAG = "UserLoginActivity";
	private String mLoginActionUrl = "http://133.242.168.69/team_h/fshow/develop/fashion/LoginAction.php";
	
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
			EditText editTextEmail = (EditText) findViewById(R.id.editTextLoginEmail);
			EditText editTextPassword = (EditText) findViewById(R.id.editTextLoginPassword);
			
			String email = editTextEmail.getEditableText().toString();
			String password = editTextPassword.getEditableText().toString();
			new LoginAsyncTask().execute(email, password);
		} else if (id == R.id.buttonSignup) {
	    	Intent intent = new Intent(this, UserRegisterAcitvity.class);
	    	startActivity(intent);
		}
	}
	
	
	private boolean login(String username, String password) {
		boolean result = false;
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(mLoginActionUrl );

		// Request parameters and other properties.
		List<NameValuePair> params = new ArrayList<NameValuePair>(2);
		params.add(new BasicNameValuePair("myemail", username));
		params.add(new BasicNameValuePair("mypassword", password));
		try {
			httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		//Execute and get the response.
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (response != null) {
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				try {
					// do something useful
					if (entity != null) {
				        // EntityUtils to get the reponse content
				        String content =  EntityUtils.toString(entity);
				        ObjectDraggingActivity.sUserId = "0"; //reset
				        Log.v(TAG, "@@@ content:"+content);
				        if ("0".equals(content) == false) {
				        	result = true;
				        	ObjectDraggingActivity.sUserId = content;
				        }
					}
				} catch (ParseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
		return result;
	}

	private class LoginAsyncTask extends AsyncTask<String, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(String... params) {
			String email = params[0];
			String password = params[1];
			boolean result = login(email, password);
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				// Map of item screen back
				Toast.makeText(UserLoginActivity.this, "ログイン成功", Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(UserLoginActivity.this, "ログイン失敗", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(UserLoginActivity.this, UserRegisterAcitvity.class);
		    	startActivity(intent);
				finish();
			}
		}
	}
}
