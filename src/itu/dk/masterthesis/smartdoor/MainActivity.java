package itu.dk.masterthesis.smartdoor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	static Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		context = this;
		
		final Button leavenote_button = (Button) findViewById(R.id.leavenote);
		final TextView status_text = (TextView) findViewById(R.id.statustext);
		final ImageView status_pic = (ImageView) findViewById(R.id.statuspic);
		
		final Intent intent = getIntent();
		if(intent.hasExtra("status_text")) {
			status_text.setText(intent.getStringExtra("status_text"));
		}
		if(intent.hasExtra("status_text")) {
			//status_pic.setImageBitmap(intent.getStringExtra("status_pic"));
		}
		
		leavenote_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				status_text.setText("");
				Intent intent = new Intent(MainActivity.this, NoteActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuItem item = menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Admin");
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int id, MenuItem item) {
		// Receipt behavior
		if(item.getItemId() == (Menu.FIRST)) {
			Intent intent = new Intent(MainActivity.this, AdminActivity.class);
			startActivity(intent);
		}
		
		return true;
	}
	
	public static void toaster(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}

}
