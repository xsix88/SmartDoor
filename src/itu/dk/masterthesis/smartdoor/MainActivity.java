package itu.dk.masterthesis.smartdoor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	static Context context;
	byte[] byteArray;
	static DBadapter adapter;
	Cursor status;
	static TextView status_text;
	static ImageView status_pic;
	static boolean startThread = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.i("test", "Am I called?");
		
		adapter = new DBadapter(this);
		adapter.open();
		context = this;
		
		final Button leavenote_button = (Button) findViewById(R.id.leavenote);
		status_text = (TextView) findViewById(R.id.statustext);
		status_pic = (ImageView) findViewById(R.id.statuspic);
		
		if(!startThread) {
			Log.i("test", "Am I called 2.0?");
			Handler handler = new Handler();
			new Thread((Runnable) new TcpServer(handler)).start();
			startThread = true;
		}
		
		final Intent intent = getIntent();
		if(intent.hasExtra("status_text")) {
			status_text.setText(intent.getStringExtra("status_text"));
		}
		if(intent.hasExtra("status_pic")) {
			byte[] byteArray = intent.getExtras().getByteArray("status_pic");
			Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			status_pic.setImageBitmap(bmp);
		}
		
		leavenote_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WriteNoteActivity.class);
				startActivity(intent);
			}
		});
		
		status = adapter.getStatus();
		if(status.getCount() > 0) {
			if (status.moveToFirst()){
				do {
				   String text = status.getString(status.getColumnIndex("status"));
				   byteArray =  status.getBlob(status.getColumnIndex("pic"));
				   Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
				   status_pic.setImageBitmap(bmp);
				   status_text.setText(text);
				}
				while(status.moveToNext());
			}
			status.close();
		}
	}
	
	public static void setStatus(String status, byte[] pic) {
		if(pic.length <= 0) {
			Cursor cursor = adapter.getStatus();
			if(cursor.getCount() > 0) {
				if (cursor.moveToFirst()){
					pic = cursor.getBlob(cursor.getColumnIndex("pic"));
				}
			}
		}
		adapter.saveStatus(pic, status);
		status_text.setText(status);
		Bitmap bmp = BitmapFactory.decodeByteArray(pic, 0, pic.length);
		status_pic.setImageBitmap(bmp);
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
	
	protected void onDestroy() {
		super.onDestroy();
		adapter.close();
	}

}
