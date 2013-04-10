package itu.dk.masterthesis.smartdoor;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AdminActivity extends Activity {
	
	ImageView status_pic;
	byte[] byteArray;
	DBadapter adapter;
	Cursor status;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		
		adapter = new DBadapter(this);
		adapter.open();
		
		final Button change_pic_button = (Button) findViewById(R.id.admin_picture_change_button);
		final Button update_button = (Button) findViewById(R.id.admin_update_button);
		final EditText status_text = (EditText) findViewById(R.id.admin_status_text);
		final Button readNotes_button = (Button) findViewById(R.id.noteButton);
		final Button clearNotes_button = (Button) findViewById(R.id.clearNotesButton);
		
		Intent intent = getIntent();
		if(intent.hasExtra("selectedPicture")) {
			status_pic = (ImageView) findViewById(R.id.admin_picture);
			byteArray = intent.getExtras().getByteArray("selectedPicture");
			Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			status_pic.setImageBitmap(bmp);
			
			status = adapter.getStatus();
			if(status.getCount() > 0) {
				if (status.moveToFirst()){
					do {
					   String text = status.getString(status.getColumnIndex("status"));
					   status_text.setText(text);
					}
					while(status.moveToNext());
				}
				status.close();
			}
		} else {
			status = adapter.getStatus();
			if(status.getCount() > 0) {
				if (status.moveToFirst()){
					do {
					   String text = status.getString(status.getColumnIndex("status"));
					   byteArray =  status.getBlob(status.getColumnIndex("pic"));
					   Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
					   status_pic = (ImageView) findViewById(R.id.admin_picture);
					   status_pic.setImageBitmap(bmp);
					   status_text.setText(text);
					}
					while(status.moveToNext());
				}
				status.close();
			}
		}
		
		change_pic_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AdminActivity.this, FlickrActivity.class);
				startActivity(intent);
			}
		});
		clearNotes_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.clearNotes();
			}
		});
		update_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.saveStatus(byteArray, status_text.getText()+"");
				Intent intent = new Intent(AdminActivity.this, MainActivity.class);
				intent.putExtra("status_text", status_text.getText()+"");
				intent.putExtra("status_pic", byteArray);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
			}
		});
		readNotes_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AdminActivity.this, ReadNoteActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin, menu);
		return true;
	}
	
	protected void onDestroy() {
		super.onDestroy();
		adapter.close();
	}

}
