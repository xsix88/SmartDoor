package itu.dk.masterthesis.smartdoor;

import android.app.Activity;
import android.content.Intent;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		
		final Button change_pic_button = (Button) findViewById(R.id.admin_picture_change_button);
		final Button update_button = (Button) findViewById(R.id.admin_update_button);
		final EditText status_text = (EditText) findViewById(R.id.admin_status_text);
		
		Intent intent = getIntent();
		if(intent.hasExtra("selectedPicture")) {
			status_pic = (ImageView) findViewById(R.id.admin_picture);
			byteArray = intent.getExtras().getByteArray("selectedPicture");
			Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
			status_pic.setImageBitmap(bmp);
		}
		
		change_pic_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AdminActivity.this, FlickrActivity.class);
				startActivity(intent);
			}
		});
		update_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 Intent intent = new Intent(AdminActivity.this, MainActivity.class);
				 intent.putExtra("status_text", status_text.getText()+"");
				 intent.putExtra("status_pic", byteArray);
				 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

}
