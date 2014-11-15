package com.sjcp.dossier;

import java.io.ByteArrayOutputStream;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.android.glass.app.Card;
import com.google.android.glass.media.CameraManager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.FileObserver;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WhoIsActivity extends Activity {
	public static HashMap<String, String> data;
	public static Uri uri;
	public void onCreate(Bundle b) {
		super.onCreate(b);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);		
		
		//Uri uriSavedImage = Uri.fromFile(new File());
		//camera.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
		startActivityForResult(camera, 1);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == 1 && resultCode == RESULT_OK) {
			String picturePath = data.getStringExtra(CameraManager.EXTRA_PICTURE_FILE_PATH);
			
			processPictureWhenReady(picturePath);
		}
	    super.onActivityResult(requestCode, resultCode, data);
	}
	
	private void processPicture(File pictureFile)
	{
		if (pictureFile.exists())
		{
			uri = Uri.fromFile(pictureFile);
			Log.d("URI: ", uri.toString());
//			Bitmap bm = BitmapFactory.decodeFile(pictureFile.getPath());
//			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
//			makeCard("Compressing image");
//			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
//			byte[] byteArray = baos.toByteArray();
			makeCard("Analyzing...");
			Intent intent = new Intent(this, NotifyService.class);
			this.startService(intent);
//			String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
//			postData(Arrays.toString(byteArray));
//			makeCard("Sent");
//			makeCard("Suck it");
		}
	}
	
	public void makeCard(String text)
	{
		Card card1 = new Card(this);
		card1.setText(text); // Main text area
		//card1.addImage(android.view.animation.AlphaAnimation)
		View card1View = card1.toView();
		setContentView(card1View);
	}
	
	public void postData(String encodedImage) {
		Log.d("WHO IS ACTIVITY","post()");
		Intent intent = new Intent(this, NotifyService.class);
		
		
		data = new HashMap<String, String>();
		data.put("upload", encodedImage);
//		data.put("type", "file");
	    
	    this.startService(intent);
		Card card1 = new Card(this);
		card1.setText(NotifyService.final_response); // Main text area
		View card1View = card1.toView();
		setContentView(card1View);
	} 
	
	public void uploadImage(File image) {
		
	}
	
	private void processPictureWhenReady(final String picturePath) {
		final File pictureFile = new File(picturePath);
		processPicture(pictureFile);
		if (pictureFile.exists()) {
			
			System.out.print("HURR");
			// The picture is ready; process it.
		} else {
			
			// The file does not exist yet. Before starting the file observer,
			// you can update your UI to let the user know that the application is
			// waiting for the picture (for example, by displaying the thumbnail
			// image and a progress indicator).

			final File parentDirectory = pictureFile.getParentFile();
			FileObserver observer = new FileObserver(parentDirectory.getPath()) {
				
				
				
				// Protect against additional pending events after CLOSE_WRITE
				// is
				// handled.
				private boolean isFileWritten;

				@Override
				public void onEvent(int event, String path) {
					if (!isFileWritten) {
						// For safety, make sure that the file that was created
						// in
						// the directory is actually the one that we're
						// expecting.
						File affectedFile = new File(parentDirectory, path);
						isFileWritten = (event == FileObserver.CLOSE_WRITE && affectedFile
								.equals(pictureFile));

						if (isFileWritten) {
							stopWatching();

							// Now that the file is ready, recursively call
							// processPictureWhenReady again (on the UI thread).
							runOnUiThread(new Runnable() {
								@Override
								public void run() {
									processPictureWhenReady(picturePath);
								}
							});
						}
					}
				}
			};
			observer.startWatching();
		}
	}
	
	
	
	
	
	/*
	public int onStartCommand(Intent intent, int flags, int startId) {
		final Camera cam = Camera.open();
		cam.autoFocus(new Camera.AutoFocusCallback(){
			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				cam.takePicture(null, null, new Camera.PictureCallback() {
					@Override
					public void onPictureTaken(byte[] data, Camera camera) {
						cam.startPreview();
					}
				});
			}
		
		});	
		return 0;
	}
	*/
}
