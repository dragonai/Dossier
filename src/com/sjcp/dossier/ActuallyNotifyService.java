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
import org.json.JSONObject;

import com.google.android.glass.app.Card;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;


public class ActuallyNotifyService extends Service {
	public static String final_response;
	public String fuckthisresult;
	public static String user_name;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d("ActuallyNotify", "onStart");
//		HashMap<String, String> data = WhoIsActivity.data;
		HashMap<String, String> data = ImgurUploadTask.data;
//
//		ImgurUploadTask task = new ImgurUploadTask(WhoIsActivity.uri,this);
//		task.execute();
	    AsyncHttpPost asyncPost = new AsyncHttpPost(this, data);
	    asyncPost.execute("http://dossier-mhacks.herokuapp.com/recognize");
	    Log.d("NOTIFY_DEVICE", "ASYNC EXECUTED");

		return START_STICKY;
	}

	
	
	private class AsyncHttpPost extends AsyncTask<String, String, String> {
	    private HashMap<String, String> mData = null;// post data
	    private Service service;
	    /**
	     * constructor
	     */
	    public AsyncHttpPost(Service _service, HashMap<String, String> data) {
	        mData = data;
	        service = _service;
	        //activity.makeCard("Created AsyncHttpPost");
	    }

	    /**
	     * background
	     */
	    @Override
	    protected String doInBackground(String... params) {
	        byte[] result = null;
	        String str = "";
	        HttpClient client = new DefaultHttpClient();
	        HttpPost post = new HttpPost(params[0]);// in this case, params[0] is URL
	        Log.d("doInBg", "set header");
	        try {
	            // set up post data
	            ArrayList<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
	            Iterator<String> it = mData.keySet().iterator();
	            while (it.hasNext()) {
	                String key = it.next();
	                nameValuePair.add(new BasicNameValuePair(key, mData.get(key)));
	            }
//
	            post.setEntity(new UrlEncodedFormEntity(nameValuePair, "UTF-8"));
		        Log.d("Post", "Before");

	            HttpResponse response = client.execute(post);
		        Log.d("Post", "After");

	            
	            StatusLine statusLine = response.getStatusLine();
	            if(statusLine.getStatusCode() == HttpURLConnection.HTTP_OK){
	                result = EntityUtils.toByteArray(response.getEntity());
	                str = new String(result, "UTF-8");
	            }
	        }
	        catch (UnsupportedEncodingException e) {
	            e.printStackTrace();
	        }
	        catch (Exception e) {
	        }
	        fuckthisresult = str;
	        return str;
	    }

	    /**
	     * on getting result
	     */
	    @Override
	    protected void onPostExecute(String result) {
	    	//super.onPostExecute(result);
	    	final_response = fuckthisresult;
	    	user_name = "dammit.";
	    	try {
	    		JSONObject root = new JSONObject(final_response);
	    	
	    		String name = root.getString("name");
	    		user_name = name;
	    	
	    	
	    		Log.d("FINAL", name);
	    	}
	    	catch (Exception e) {}
	    		Intent intent = new Intent(ActuallyNotifyService.this, NotifyActivity.class);
	    		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    		ActuallyNotifyService.this.startActivity(intent);
	    		ActuallyNotifyService.this.stopSelf();
	    	
	    }
	    
	    
//	    private class NotifyActivity extends Activity {
//	    	public void onCreate(Bundle b) {
//	    		super.onCreate(b);
//	    		Card card1 = new Card(this);
//	    		card1.setText("HOLA SENOR"); // Main text area
//	    		View card1View = card1.toView();
//	    		setContentView(card1View);
//	    	}
//	    	
//	    	
//	    	
//	    }
	}


}

