package com.hackathon.fshow;

import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.hackathon.fshow.module.UserInfo;

public class UserRegisterAcitvity extends Activity {
	private static final String TAG = "UserRegisterAcitvity";
	private static final int DIALOG_REGISTER_SUCCESS = 1;
	private static final int DIALOG_REGISTER_FAILED = 2;
	
	
	
	private UserInfo mUserInfo = null;

	private EditText editTextName = null;
	private EditText editTextEmail = null;
	private EditText editTextPassword = null;
	private String mUrlString ;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_register);
		mUserInfo = new UserInfo();
		editTextName = (EditText) findViewById(R.id.editTextName);
		editTextEmail = (EditText) findViewById(R.id.editTextEmail);
		editTextPassword = (EditText) findViewById(R.id.editTextPassword);
		mUrlString = getString(R.string.domain_user_insert);
		findViewById(R.id.buttonResgister).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mUserInfo.setName(editTextName.getText().toString());
				mUserInfo.setEmail(editTextEmail.getText().toString());
				mUserInfo.setPassword(editTextPassword.getText().toString());
				new UserRegisterAsyncTask().execute(mUserInfo);
			}
		});
	}

	
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		Dialog dialog = null;
		AlertDialog.Builder builder = new AlertDialog.Builder(this);  
		if (id == DIALOG_REGISTER_SUCCESS) {
			builder.setTitle("登録成功");  
	        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(UserRegisterAcitvity.this, UserLoginActivity.class);
					startActivity(intent);
					finish();
				}
			});
		} else {
			builder.setTitle("登録失敗,再入力してください。");  
	        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					clearAllInputed();
				}
			});
	        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			});
		}
		dialog = builder.create();
		return dialog;
	}
	
	

	private class UserRegisterAsyncTask extends AsyncTask<UserInfo, Integer, Boolean> {

		@Override
		protected Boolean doInBackground(UserInfo... params) {
			UserInfo userInfo = params[0];
			boolean result = postFormData(userInfo);
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				showDialog(DIALOG_REGISTER_SUCCESS);
			} else {
				showDialog(DIALOG_REGISTER_FAILED);
			}
		}
		
		private boolean postFormData(UserInfo userInfo) {
			boolean result = false;
			// Create a new HttpClient and Post Header
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(mUrlString);

			// Add your data
			List<NameValuePair> nameValuePairs = userInfo.makePostFormData();
			if (nameValuePairs != null) {
				try {
					httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					// Execute HTTP Post Request
					HttpResponse response = httpclient.execute(httppost);
					int statusCode = response.getStatusLine().getStatusCode();
					if (statusCode == 200) {
						Log.v(TAG, "@@@status code:"+statusCode);
						result = true;
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				Log.e(TAG, "@@@ INPUT ERROR");
			}
			return result;
		}
	}
	
	/**
	 * すべて入力を削除
	 */
	private void clearAllInputed() {
		editTextEmail.getText().clear();
		editTextName.getText().clear();
		editTextPassword.getText().clear();
	}
}
