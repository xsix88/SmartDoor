package itu.dk.masterthesis.smartdoor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class AdminActivity extends Activity {
	
	ImageView status_pic;
	byte[] byteArray;
	DBadapter adapter;
	Cursor status;
	EditText status_text;
	public static final String EXTRA_SEARCHTEXT = "extraSearch";
	public static final int GET_PICTURE = 13;
	private ImageView movePostit;
	private ImageView moveAboutme;
	private ImageView moveCoffee;
	private ImageView moveLinkOne;
	private ImageView moveLinkTwo;
	private ImageView moveLinkThree;
	private ImageView moveLinkFour;
	private ImageView moveLinkFive;
	private float oldXvalue;
	private float oldYvalue;
	BitmapFactory.Options options = new BitmapFactory.Options();
	
	public void onResume(){
		super.onResume();
		
		int[] postitPosition = adapter.getPosition("postit");
		AbsoluteLayout.LayoutParams postParams = (AbsoluteLayout.LayoutParams)movePostit.getLayoutParams();
		postParams.x = postitPosition[0];
		postParams.y = postitPosition[1];
		movePostit.requestLayout();
		
		int[] aboutmePosition = adapter.getPosition("aboutme");
		AbsoluteLayout.LayoutParams aboutParams = (AbsoluteLayout.LayoutParams)moveAboutme.getLayoutParams();
		aboutParams.x = aboutmePosition[0];
		aboutParams.y = aboutmePosition[1];
		moveAboutme.requestLayout();
		
		int[] coffeePosition = adapter.getPosition("coffee");
		AbsoluteLayout.LayoutParams coffeeParams = (AbsoluteLayout.LayoutParams)moveCoffee.getLayoutParams();
		coffeeParams.x = coffeePosition[0];
		coffeeParams.y = coffeePosition[1];
		moveCoffee.requestLayout();
		
		int[] linkOnePosition = adapter.getPosition("linkOne");
		AbsoluteLayout.LayoutParams linkOneParams = (AbsoluteLayout.LayoutParams)moveLinkOne.getLayoutParams();
		linkOneParams.x = linkOnePosition[0];
		linkOneParams.y = linkOnePosition[1];
		moveLinkOne.requestLayout();
		
		int[] linkTwoPosition = adapter.getPosition("linkTwo");
		AbsoluteLayout.LayoutParams linkTwoParams = (AbsoluteLayout.LayoutParams)moveLinkTwo.getLayoutParams();
		linkTwoParams.x = linkTwoPosition[0];
		linkTwoParams.y = linkTwoPosition[1];
		moveLinkTwo.requestLayout();
		
		int[] linkThreePosition = adapter.getPosition("linkThree");
		AbsoluteLayout.LayoutParams linkThreeParams = (AbsoluteLayout.LayoutParams)moveLinkThree.getLayoutParams();
		linkThreeParams.x = linkThreePosition[0];
		linkThreeParams.y = linkThreePosition[1];
		moveLinkThree.requestLayout();
		
		int[] linkFourPosition = adapter.getPosition("linkFour");
		AbsoluteLayout.LayoutParams linkFourParams = (AbsoluteLayout.LayoutParams)moveLinkFour.getLayoutParams();
		linkFourParams.x = linkFourPosition[0];
		linkFourParams.y = linkFourPosition[1];
		moveLinkFour.requestLayout();
		
		int[] linkFivePosition = adapter.getPosition("linkFive");
		AbsoluteLayout.LayoutParams linkFiveParams = (AbsoluteLayout.LayoutParams)moveLinkFive.getLayoutParams();
		linkFiveParams.x = linkFivePosition[0];
		linkFiveParams.y = linkFivePosition[1];
		moveLinkFive.requestLayout();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		
		adapter = new DBadapter(this);
		adapter.open();
		
		options.inSampleSize = 3;
		
		final Button change_pic_button = (Button) findViewById(R.id.admin_picture_change_button);
		final Button update_button = (Button) findViewById(R.id.admin_update_button);
		status_text = (EditText) findViewById(R.id.admin_status_text);
		final EditText picture_text = (EditText) findViewById(R.id.pictureTxt);
		final Button readNotes_button = (Button) findViewById(R.id.noteButton);
		final Button clearNotes_button = (Button) findViewById(R.id.clearNotesButton);
		final Button selectStatus_button = (Button) findViewById(R.id.select_static_status);
		movePostit = (ImageView) findViewById(R.id.postit);
		moveAboutme = (ImageView) findViewById(R.id.aboutme);
		moveCoffee = (ImageView) findViewById(R.id.coffee);
		moveLinkOne = (ImageView) findViewById(R.id.linkOne);
		moveLinkTwo = (ImageView) findViewById(R.id.linkTwo);
		moveLinkThree = (ImageView) findViewById(R.id.linkThree);
		moveLinkFour = (ImageView) findViewById(R.id.linkFour);
		moveLinkFive = (ImageView) findViewById(R.id.linkFive);
		byte[] icon1 = adapter.getAppPic("linkOne");
		Bitmap bmp1 = BitmapFactory.decodeByteArray(icon1, 0, icon1.length,options);
		bmp1 = Bitmap.createScaledBitmap(bmp1, 150, 150, true);
		moveLinkOne.setImageBitmap(bmp1);
		if (bmp1 != null) {
			bmp1 = null;
		}
		byte[] icon2 = adapter.getAppPic("linkTwo");
		Bitmap bmp2 = BitmapFactory.decodeByteArray(icon2, 0, icon2.length,options);
		bmp2 = Bitmap.createScaledBitmap(bmp2, 150, 150, true);
		moveLinkTwo.setImageBitmap(bmp2);
		if (bmp2 != null) {
			bmp2 = null;
		}
		byte[] icon3 = adapter.getAppPic("linkThree");
		Bitmap bmp3 = BitmapFactory.decodeByteArray(icon3, 0, icon3.length,options);
		bmp3 = Bitmap.createScaledBitmap(bmp3, 150, 150, true);
		moveLinkThree.setImageBitmap(bmp3);
		if (bmp3 != null) {
			bmp3 = null;
		}
		byte[] icon4 = adapter.getAppPic("linkFour");
		Bitmap bmp4 = BitmapFactory.decodeByteArray(icon4, 0, icon4.length,options);
		bmp4 = Bitmap.createScaledBitmap(bmp4, 150, 150, true);
		moveLinkFour.setImageBitmap(bmp4);
		if (bmp4 != null) {
			bmp4 = null;
		}
		byte[] icon5 = adapter.getAppPic("linkFive");
		Bitmap bmp5 = BitmapFactory.decodeByteArray(icon5, 0, icon5.length,options);
		bmp5 = Bitmap.createScaledBitmap(bmp5, 150, 150, true);
		moveLinkFive.setImageBitmap(bmp5);
		if (bmp5 != null) {
			bmp5 = null;
		}
		/*
		int[] postitPosition = adapter.getPosition("postit");
		movePostit.setLayoutParams(new LayoutParams(movePostit.getWidth(), movePostit.getHeight(), postitPosition[0], postitPosition[1]));
		int[] aboutmePosition = adapter.getPosition("aboutme");
		moveAboutme.setLayoutParams(new LayoutParams(moveAboutme.getWidth(), moveAboutme.getHeight(), aboutmePosition[0], aboutmePosition[1]));
		int[] coffeePosition = adapter.getPosition("coffee");
		moveCoffee.setLayoutParams(new LayoutParams(moveCoffee.getWidth(), moveCoffee.getHeight(), coffeePosition[0], coffeePosition[1]));
		*/
		
		OnTouchListener otimg = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent me){
		        if (me.getAction() == MotionEvent.ACTION_DOWN){
		            oldXvalue = me.getX();
		            oldYvalue = me.getY();
		        } else if (me.getAction() == MotionEvent.ACTION_MOVE  ){
		        	LayoutParams params = new LayoutParams(v.getWidth(), v.getHeight(),(int)((me.getRawX() - (v.getWidth() / 2))*1), (int)(me.getRawY() - (v.getHeight() * 3)));
		        	v.setLayoutParams(params);
		        } else if (me.getAction() == MotionEvent.ACTION_UP) {
		        	adapter.savePosition(v.getContentDescription()+"", (int)Math.round((me.getRawX() - (v.getWidth() / 2))*1), (int)Math.round(me.getRawY() - (v.getHeight() * 3)));
		        }
		        return true;
		    }
		};
		
		movePostit.setOnTouchListener(otimg);
		moveAboutme.setOnTouchListener(otimg);
		moveCoffee.setOnTouchListener(otimg);
		moveLinkOne.setOnTouchListener(otimg);
		moveLinkTwo.setOnTouchListener(otimg);
		moveLinkThree.setOnTouchListener(otimg);
		moveLinkFour.setOnTouchListener(otimg);
		moveLinkFive.setOnTouchListener(otimg);
		
		if(adapter.getNumberOfNotes() > 0) {
			readNotes_button.setText("Read notes ("+adapter.getNumberOfNotes()+")");
			readNotes_button.setTypeface(null,Typeface.BOLD);
		}
		
		status = adapter.getStatus("1");
		if(status.getCount() > 0) {
			if (status.moveToFirst()){
				do {
				   String text = status.getString(status.getColumnIndex("status"));
				   byteArray =  status.getBlob(status.getColumnIndex("pic"));
				   Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
				   status_pic = (ImageView) findViewById(R.id.admin_picture);
				   status_pic.setImageBitmap(bmp);
				   status_text.setText(text);
				   if (bmp != null) {
						bmp = null;
					}
				   System.gc();
				}
				while(status.moveToNext());
			}
			status.close();
		}
		
		change_pic_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//adapter.syncDefaultsFromServer();
				if (picture_text.getText().length() > 0) {
					// create an intent to start the ViewThumbnailsActivity
			    	Intent startThumbnailsActIntent = new Intent(AdminActivity.this, ViewThumbnailsActivity.class);
			    	// put in the intent the search string too
			    	startThumbnailsActIntent.putExtra(EXTRA_SEARCHTEXT, picture_text.getText().toString());		
			    	startThumbnailsActIntent.putExtra("status_text", status_text.getText().toString());		
			    	// fire the intent
			    	startActivityForResult(startThumbnailsActIntent, GET_PICTURE);
				} else {
					MainActivity.toaster("Enter text to search for a picture.");
				}
			}
		});
		
		selectStatus_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final CharSequence[] items = new CharSequence[adapter.getNumberOfStatics()];
				status = adapter.getStatic(adapter.getNumberOfStatics()+"");
				if(status.getCount() > 0) {
					int i = 0;
					if (status.moveToFirst()){
						do {
						   String text = status.getString(status.getColumnIndex("status"));
						   items[i] = text;
						   i++;
						   }
						while(status.moveToNext());
					}
					status.close();
				}
				//final CharSequence[] items = {"Status 1", "Status 2","Status 3", "Status 4", "Status 5"};
				AlertDialog.Builder builder = new AlertDialog.Builder(AdminActivity.this);
				builder.setTitle("Select Status");
				builder.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						status_text.setText(items[item]);
						byte[] pic = adapter.getStaticPic(items[item]+"");
						Bitmap bmp = BitmapFactory.decodeByteArray(pic, 0, pic.length, options);
						status_pic.setImageBitmap(bmp);
						byteArray = pic;						}
				});
				builder.show();
				System.gc();
			}
		});
		clearNotes_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.clearNotes();
				readNotes_button.setText("Read notes");
				readNotes_button.setTypeface(null,Typeface.NORMAL);
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Check which request we're responding to
	    if (requestCode == GET_PICTURE) {
	        // Make sure the request was successful
	        if (resultCode == RESULT_OK) {
	        	status_pic = (ImageView) findViewById(R.id.admin_picture);
				byteArray = data.getByteArrayExtra("selectedPicture");
				Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
				status_pic.setImageBitmap(bmp);
				if (bmp != null) {
					bmp = null;
				}
				System.gc();
				status_text.setText(data.getStringExtra("status_text"));
	        }
	    }
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString("status_text", status_text.getText().toString());
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		status_text.setText(savedInstanceState.getString("status_text"));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin, menu);
		return true;
	}
	
	protected void onDestroy() {
		super.onDestroy();
	}

}
