package itu.dk.masterthesis.smartdoor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FlickrActivity extends Activity {
	
	public static final String EXTRA_SEARCHTEXT = "extraSearch";
	public static final int GET_PICTURE = 13;
	
	private Button pictureButton;
	private EditText pictureTxt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flickr);
		
		pictureTxt = (EditText) findViewById(R.id.pictureTxt);
		pictureButton = (Button) findViewById(R.id.pictureB);
				
		pictureButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (pictureTxt.getText().length() > 0) {					
					updatePicture();
				} else {
					displayToast("Enter text to search for a picture.");
				}
			}	
		});		
	}
	
	private void updatePicture() {
		// create an intent to start the ViewThumbnailsActivity
    	Intent startThumbnailsActIntent = new Intent(this, ViewThumbnailsActivity.class);
    	// put in the intent the search string too
    	startThumbnailsActIntent.putExtra(EXTRA_SEARCHTEXT, pictureTxt.getText().toString());		    	
    	// fire the intent
    	startActivityForResult(startThumbnailsActIntent, GET_PICTURE);
	}	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
		Log.i("Test", "hi");
	    if (requestCode == GET_PICTURE) {
	        // Make sure the request was successful
	    	Log.i("Test", "Yes");
	        if (resultCode == RESULT_OK) {
	        	Log.i("Test", "Seriously?!?!??!");
	        	Intent intent = new Intent(FlickrActivity.this, AdminActivity.class);
	        	intent.putExtra("selectedPicture", data.getByteArrayExtra("selectedPicture"));
	        	startActivity(intent);
	        	Log.i("Test", "Seriously?!?!??! 2.0");
	        }
	    }
	}
	
	
	private void displayToast(String txt) {
		Toast.makeText(this, txt, Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
