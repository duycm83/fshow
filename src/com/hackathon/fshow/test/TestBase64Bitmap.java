package com.hackathon.fshow.test;

import java.io.InputStream;
import java.net.URL;

import ImageUtils.ImageUtils;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hackathon.fshow.R;

public class TestBase64Bitmap extends Activity {

	private static final String TAG = "TestBase64Bitmap";
	ImageView image1;
	ImageView image2;
	TextView textView;
	Bitmap bit = null;
	String encoded = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_image_base64);
		textView = (TextView) findViewById(R.id.textView1);
		image1 = (ImageView) findViewById(R.id.imageViewUpload);
		image2 = (ImageView) findViewById(R.id.imageView2);

		Runnable runnable = new Runnable() {

			@Override
			public void run() {
				String url = "http://sns.seraku.com/img.php?filename=m_314_1368577336.jpg&w=180&h=180&m=pc";
				try {
					bit = BitmapFactory
							.decodeStream((InputStream) new URL(url)
									.getContent());
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							image1.setImageBitmap(bit);
						}
					});
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};

		Thread thread = new Thread(runnable);
		thread.start();

		String base64 = ImageUtils.readRawTextFile(this, "image04");
		Bitmap bmp = ImageUtils.decodeToImage(base64);
		image2.setImageBitmap(bmp);
	}

	public void onButtonClick(View view) {
		int id = view.getId();
		if (id == R.id.buttonResgister) {
			encoded = ImageUtils.encodeToString(bit);
			textView.setText(encoded);
			Log.v(TAG, encoded);
		} else {
			Bitmap bmp = ImageUtils.decodeToImage(encoded);
			image2.setImageBitmap(bmp);
		}
	}

	
}
