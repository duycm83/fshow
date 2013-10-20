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
import org.json.JSONArray;
import org.json.JSONObject;

import rajawali.lights.DirectionalLight;
import rajawali.util.ObjectColorPicker;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.util.Log;

import com.hackathon.fshow.ObjectDraggingActivity;
import com.hackathon.fshow.ObjectDraggingRenderer;

public class DownloadMapAsyncTask extends AsyncTask<String, Integer, String> {
	private static final String TAG = "DownloadMapAsyncTask";
	private ObjectDraggingActivity mContext;
	private ObjectDraggingRenderer mRenderer;
	private ObjectColorPicker mPicker;
	private DirectionalLight mLight;
	
	//@see http://techbooster.org/android/application/1645/
	private String mMapAccessUrl = "http://133.242.168.69/team_h/fshow/develop/fashion/MapAccess.php";
	private GLSurfaceView mSurfaceView;
	
	public DownloadMapAsyncTask(ObjectDraggingActivity context, ObjectDraggingRenderer renderer, ObjectColorPicker picker, DirectionalLight light) {
		this.mContext = context;
		this.mRenderer = renderer;
		this.mPicker = picker;
		this.mLight = light;
	}
	
	public DownloadMapAsyncTask(ObjectDraggingActivity context, ObjectDraggingRenderer renderer) {
		this.mContext = context;
		this.mRenderer = renderer;
//		this.mPicker = renderer.getPicker();
//		this.mLight = renderer.getLight();
	}
	
	
//	@Override
//	protected void onPostExecute(String result) {
//		this.mPicker = mRenderer.getPicker();
//		this.mLight = mRenderer.getLight();
//		if (result != null) {
//			try {
//				JSONObject rootObject = new JSONObject(result);
//				JSONArray items = rootObject.getJSONArray("items");
//				int size = items.length();
//				for (int i = 0; i < size; i++) {
//					JSONArray item = items.getJSONArray(i);
//					int itemId = item.getInt(0);
//					int userId = item.getInt(1);
//					String imageBase64 = item.getString(2);
//					Log.v(TAG, "@@@ itemId:"+itemId);
//					Log.v(TAG, "@@@ userId:"+userId);
//					Log.v(TAG, "@@@ imageBase64:"+imageBase64.substring(0, 20));
//					AutoGenerateItem.addNewItem(mContext, mRenderer, mPicker, mLight, itemId, imageBase64);
//				}
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
//		}
//		super.onPostExecute(result);
//	}
	
	@Override
	protected String doInBackground(String... params) {
		String jsonResult = null;
		HttpClient httpClient = new DefaultHttpClient();
		 
		StringBuilder uri = new StringBuilder(mMapAccessUrl);
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
		        //Log.v(TAG, "@@@ jsonresult:"+jsonResult);
		        Log.v(TAG, "@@@ jsonresult OK");
		        JSONObject rootObject = new JSONObject(jsonResult);
				JSONArray items = rootObject.getJSONArray("items");
				mRenderer.setData(items);
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
