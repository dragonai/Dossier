package com.sjcp.dossier;
import com.google.android.glass.app.Card;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


public class NotifyActivity extends Activity {
	public void onCreate(Bundle b) {
    	Log.d("NotifyActivty", "FUCK YEEEEEAAAAAH()");

		super.onCreate(b);
		Card card1 = new Card(this);
		card1.setText(ActuallyNotifyService.user_name); // Main text area
		card1.setFootnote("Swipe down to dismiss");
		//card1.setImageLayout(Card.ImageLayout.FULL);
		//card1.addImage(android.graphics.Bitmap.createBitmap(src)
		View card1View = card1.toView();
		setContentView(card1View);
	}
	
	
	
}
