package com.sjcp.dossier;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.android.glass.app.Card;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;


public class NotifyService extends Service {
	public static String final_response;
	public String fuckthisresult;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		HashMap<String, String> data = WhoIsActivity.data;

		ImgurUploadTask task = new ImgurUploadTask(WhoIsActivity.uri,this);
		task.execute();
	    //AsyncHttpPost asyncPost = new AsyncHttpPost(this, data);
//	    asyncPost.execute("http://dossier-mhacks.herokuapp.com/recognize");
//	    asyncPost.execute("https://api.imgur.com/3/image");
//	    asyncPost.execute("http://oysterapp.herokuapp.com/6Sv4p37FbOYH7ysXo3Lh0PRxj7Ewu3H4wDMISVACQM8");
//	    asyncPost.execute("http://uploads.im");
	    Log.d("NOTIFY_DEVICE", "ASYNC EXECUTED");

		return START_STICKY;
	}

	


}

