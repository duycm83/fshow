package com.hackathon.fshow;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ItemResgisterActivity extends Activity {
	private static final int YOUR_SELECT_PICTURE_REQUEST_CODE = 1;
	private static final String TAG = "ItemResgisterActivity";
	private TextView mTextViewSelectedPath = null;
	private ImageView mImageViewSelectedImage = null;
	private Button mButtonUpload = null;
	private Button mButtonSelectImage = null;
	private int serverResponseCode = 0;
	private ProgressDialog dialog = null;
	private String mUpLoadServerUri = null;
	private Uri outputFileUri;
	private Uri selectedImageUri;
	private String mFileUploadName;
	/********** File Path *************/
	final String uploadFilePath = Environment.getExternalStorageDirectory()
			.getPath();
	private String mUploadFileName = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_upload_to_server);
		mButtonUpload = (Button) findViewById(R.id.uploadButton);
		mButtonSelectImage = (Button) findViewById(R.id.buttonLogin);
		mImageViewSelectedImage = (ImageView) findViewById(R.id.imageViewSelectedImage);
		mTextViewSelectedPath = (TextView) findViewById(R.id.textViewSelectedPath);
		/************* Php script path ****************/
		mUpLoadServerUri = getString(R.string.domain_upload_image);

		mFileUploadName = String.format("%04d%n_%d.jpg", Integer.valueOf(ObjectDraggingActivity.sUserId),
				System.currentTimeMillis());
		mButtonUpload.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mUploadFileName == null) {
					return;
				}
				dialog = ProgressDialog.show(ItemResgisterActivity.this, "",
						"Uploading file...", true);

				new Thread(new Runnable() {
					public void run() {
						runOnUiThread(new Runnable() {
							public void run() {
								Toast.makeText(getBaseContext(),
										"uploading started.....",
										Toast.LENGTH_SHORT).show();
							}
						});
						File file = new File(mUploadFileName);
						if (file.exists()) {
							 uploadFile(file.getAbsolutePath());
//							uploadFile(mUserId, file);
						} else {

							try {
								throw new FileNotFoundException(
										"file not found:"
												+ file.getAbsolutePath());
							} catch (FileNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}).start();
			}
		});

		mButtonSelectImage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				openImageIntent();
			}
		});
	}

	public int uploadFile(String sourceFileUri) {

		String fileName = sourceFileUri;

		HttpURLConnection conn = null;
		DataOutputStream dos = null;
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";
		int bytesRead, bytesAvailable, bufferSize;
		byte[] buffer;
		int maxBufferSize = 1 * 1024 * 1024;
		File sourceFile = new File(sourceFileUri);

		if (!sourceFile.isFile()) {

			dialog.dismiss();

			Log.e("uploadFile", "Source File not exist :" + uploadFilePath + ""
					+ mUploadFileName);

			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(
							getBaseContext(),
							"Source File not exist :" + uploadFilePath + ""
									+ mUploadFileName, Toast.LENGTH_SHORT)
							.show();
				}
			});

			return 0;

		} else {
			try {

				// open a URL connection to the Servlet
				FileInputStream fileInputStream = new FileInputStream(
						sourceFile);
				URL url = new URL(mUpLoadServerUri);

				// Open a HTTP connection to the URL
				conn = (HttpURLConnection) url.openConnection();
				conn.setDoInput(true); // Allow Inputs
				conn.setDoOutput(true); // Allow Outputs
				conn.setUseCaches(false); // Don't use a Cached Copy
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Connection", "Keep-Alive");
				conn.setRequestProperty("ENCTYPE", "multipart/form-data");
				conn.setRequestProperty("Content-Type",
						"multipart/form-data;boundary=" + boundary);
				conn.setRequestProperty("file", fileName);

				dos = new DataOutputStream(conn.getOutputStream());

				// Send parameter #1
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"userid\""
						+ lineEnd);
				dos.writeBytes("Content-Type: text/plain; charset=UTF-8"
						+ lineEnd);
				dos.writeBytes("Content-Transfer-Encoding: 8bit" + lineEnd);
				dos.writeBytes(lineEnd);
				dos.writeBytes(ObjectDraggingActivity.sUserId + lineEnd);

				// Send parameter #2
				dos.writeBytes(twoHyphens + boundary + lineEnd);
				dos.writeBytes("Content-Disposition: form-data; name=\"file\";filename=\""
						+ mFileUploadName + "\"" + lineEnd);

				dos.writeBytes(lineEnd);

				// create a buffer of maximum size
				bytesAvailable = fileInputStream.available();

				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				buffer = new byte[bufferSize];

				// read file and write it into form...
				bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				while (bytesRead > 0) {

					dos.write(buffer, 0, bufferSize);
					bytesAvailable = fileInputStream.available();
					bufferSize = Math.min(bytesAvailable, maxBufferSize);
					bytesRead = fileInputStream.read(buffer, 0, bufferSize);

				}

				// send multipart form data necesssary after file data...
				dos.writeBytes(lineEnd);
				dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

				// Responses from the server (code and message)
				serverResponseCode = conn.getResponseCode();
				String serverResponseMessage = conn.getResponseMessage();

				Log.i("uploadFile", "HTTP Response is : "
						+ serverResponseMessage + ": " + serverResponseCode);

				if (serverResponseCode == 200) {

					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(ItemResgisterActivity.this,
									"File Upload Complete.", Toast.LENGTH_SHORT)
									.show();
						}
					});
				}

				// close the streams //
				fileInputStream.close();
				dos.flush();
				dos.close();

			} catch (MalformedURLException ex) {

				dialog.dismiss();
				ex.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(ItemResgisterActivity.this,
								"MalformedURLException", Toast.LENGTH_SHORT)
								.show();
					}
				});

				Log.e("Upload file to server", "error: " + ex.getMessage(), ex);
			} catch (Exception e) {

				dialog.dismiss();
				e.printStackTrace();

				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(ItemResgisterActivity.this,
								"Got Exception : see logcat ",
								Toast.LENGTH_SHORT).show();
					}
				});
				Log.e("Upload file to server Exception",
						"Exception : " + e.getMessage(), e);
			}
			dialog.dismiss();
			return serverResponseCode;

		} // End else block
	}

	private void openImageIntent() {

		// Determine Uri of camera image to save.
		final File sdImageMainDirectory = Environment
				.getExternalStorageDirectory();
		outputFileUri = Uri.fromFile(sdImageMainDirectory);

		// Camera.
		final List<Intent> cameraIntents = new ArrayList<Intent>();
		final Intent captureIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		final PackageManager packageManager = getPackageManager();
		final List<ResolveInfo> listCam = packageManager.queryIntentActivities(
				captureIntent, 0);
		for (ResolveInfo res : listCam) {
			final String packageName = res.activityInfo.packageName;
			final Intent intent = new Intent(captureIntent);
			intent.setComponent(new ComponentName(res.activityInfo.packageName,
					res.activityInfo.name));
			intent.setPackage(packageName);
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
			cameraIntents.add(intent);
		}

		// Filesystem.
		final Intent galleryIntent = new Intent();
		galleryIntent.setType("image/*");
		galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

		// Chooser of filesystem options.
		final Intent chooserIntent = Intent.createChooser(galleryIntent,
				"Select Source");

		// Add the camera options.
		chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS,
				cameraIntents.toArray(new Parcelable[] {}));

		startActivityForResult(chooserIntent, YOUR_SELECT_PICTURE_REQUEST_CODE);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == YOUR_SELECT_PICTURE_REQUEST_CODE) {
				final boolean isCamera;
				if (data == null) {
					isCamera = true;
				} else {
					final String action = data.getAction();
					if (action == null) {
						isCamera = false;
					} else {
						isCamera = action
								.equals(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					}
				}

				if (isCamera) {
					selectedImageUri = outputFileUri;
				} else {
					selectedImageUri = data == null ? null : data.getData();
				}
				Log.v(TAG, "selected image:" + selectedImageUri.toString());

				mUploadFileName = getRealPathFromURI(selectedImageUri);
				if (mUploadFileName != null) {
					mTextViewSelectedPath.setText(mUploadFileName);

					BitmapDrawable bd = (BitmapDrawable) mImageViewSelectedImage
							.getDrawable();
					bd.getBitmap().recycle();
					mImageViewSelectedImage.setImageBitmap(null);
					BitmapFactory.Options options = new BitmapFactory.Options();
					options.inSampleSize = 2;
					Bitmap myBitmap = BitmapFactory.decodeFile(mUploadFileName,
							options);
					// mImageViewSelectedImage.setImageURI(selectedImageUri);
					mImageViewSelectedImage.setImageBitmap(myBitmap);
				}
			}
		}
	}

	private String getRealPathFromURI(Uri contentUri) {
		String path = null;
		String[] proj = { MediaStore.Images.Media.DATA };
		CursorLoader loader = new CursorLoader(this, contentUri, proj, null,
				null, null);
		Cursor cursor = loader.loadInBackground();
		if (cursor != null && cursor.moveToFirst()) {
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			path = cursor.getString(column_index);
			cursor.close();
		}
		return path;
	}
}