package com.hackathon.fshow.module;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class DownloadMapAsyncTask extends AsyncTask<String, Integer, String> {
	//@see http://techbooster.org/android/application/1645/
	
	@Override
	protected void onPostExecute(String result) {
		if (result != null) {
			try {
				JSONObject rootObject = new JSONObject(result);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		super.onPostExecute(result);
	}
	
	@Override
	protected String doInBackground(String... params) {
		String jsonResult = null;
		String url = params[0];
		HttpClient httpClient = new DefaultHttpClient();
		 
		StringBuilder uri = new StringBuilder(url);
		HttpGet request = new HttpGet(uri.toString());
		HttpResponse httpResponse = null;
		 
		try {
		    httpResponse = httpClient.execute(request);
		} catch (Exception e) {
		    Log.d("JSONSampleActivity", "Error Execute");
		    return jsonResult;
		}
		 
		int status = httpResponse.getStatusLine().getStatusCode();
		 
		if (HttpStatus.SC_OK == status) {
		    try {
		        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		        httpResponse.getEntity().writeTo(outputStream);
		        jsonResult = outputStream.toString(); // JSONデータ
		    } catch (Exception e) {
		          Log.d("JSONSampleActivity", "Error");
		    }
		} else {
		    Log.d("JSONSampleActivity", "Status" + status);
		    return jsonResult;
		}
		return jsonResult;
	}
	
	
	private InputStream getStream(String url) {
	    try {
	        URL link = new URL(url);
	        URLConnection urlConnection = link.openConnection();
	        urlConnection.setConnectTimeout(1000);
	        return urlConnection.getInputStream();
	    } catch (Exception ex) {
	        return null;
	    }
	}

}
