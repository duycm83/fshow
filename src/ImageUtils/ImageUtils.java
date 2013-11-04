package ImageUtils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.util.Base64;
import android.util.Log;

public class ImageUtils {

	/**
	 * Decode string to image
	 * @param imageString The string to decode
	 * @return decoded image
	 */
	public static Bitmap decodeToImage(String imageString) {
		Bitmap image = null;
		byte[] imageByte;
		try {
			imageByte = Base64.decode(imageString, Base64.DEFAULT);
			ByteArrayInputStream bis = new ByteArrayInputStream(imageByte);
			Options imageOptions = new BitmapFactory.Options();
	        imageOptions.inSampleSize = 1;
	        imageOptions.inPurgeable = true;
	        imageOptions.inInputShareable = true;
			image = BitmapFactory.decodeStream(bis, null, imageOptions);
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return image;
	}

	/**
	 * Encode image to string
	 * @param image The image to encode
	 * @return encoded string
	 */
	public static String encodeToString(Bitmap image) {
		String imageString = null;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		try {
			image.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object 
			byte[] b = baos.toByteArray();
			imageString = Base64.encodeToString(b, Base64.DEFAULT);
			baos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageString;
	}

	public static String readRawTextFile(Context ctx, String fileName) {
		Log.v("ImageUtils", fileName);
		AssetManager as = ctx.getResources().getAssets();
		InputStream inputStream = null;
		try {
			inputStream = as.open(fileName);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		InputStreamReader inputreader = new InputStreamReader(inputStream);
		BufferedReader buffreader = new BufferedReader(inputreader);
		String line;
		StringBuilder text = new StringBuilder();

		try {
			while ((line = buffreader.readLine()) != null) {
				text.append(line);
			}
		} catch (IOException e) {
			return null;
		}
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return text.toString();
	}
}