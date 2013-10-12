package com.hackathon.fshow;

import android.os.AsyncTask;

public class NetworkAccessAsyncTask extends AsyncTask<String, Integer, Boolean> {

	private NetworkAccessCallbackIF callback;
	
	public NetworkAccessAsyncTask() {
		
	}
	
	public NetworkAccessAsyncTask(NetworkAccessCallbackIF callback) {
		this.callback = callback;
	}

	@Override
	protected Boolean doInBackground(String... args) {
		return null;
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
	}
}
