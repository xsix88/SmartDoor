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
import android.widget.AbsoluteLayout;
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
	private ImageView movePostit;
	private ImageView moveAboutme;
	private ImageView moveCoffee;
	private static ImageView moveLinkOne;
	private static ImageView moveLinkTwo;
	private static ImageView moveLinkThree;
	private static ImageView moveLinkFour;
	private static ImageView moveLinkFive;
	private AbsoluteLayout aLayout;
	static boolean startThread = false;
	static boolean startThread2 = false;
	final static int maxPictureSize = 400;
	final static int minPictureSize = 319;

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
		moveLinkTwo.requestLayout();
		
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
		setContentView(R.layout.activity_main);
		
		adapter = new DBadapter(this);
		adapter.open();
		context = this;
		
		//adapter.syncAppsFromServer();
		//Populate the static statuses
		/*adapter.saveStatic("I am out for lunch.");
		adapter.saveStatic("I am out for a meeting.");
		adapter.saveStatic("I am sick today.");
		adapter.saveStatic("I am on holiday.");
		adapter.saveStatic("I am at a conference.");*/
		//adapter.clearStatics();
		
		
		
		
		
		//Populate X and Y positions
		/*adapter.insertNewPosition("postit", 223, 2);
		adapter.insertNewPosition("aboutme", 223, 2);
		adapter.insertNewPosition("coffee", 223, 2);
		adapter.insertNewPosition("linkOne", 223, 2);
		adapter.insertNewPosition("linkTwo", 223, 2);
		adapter.insertNewPosition("linkThree", 223, 2);
		adapter.insertNewPosition("linkFour", 223, 2);
		adapter.insertNewPosition("linkFive", 223, 2);
		//Log.i("mytag", "Height: " + adapter.getPosition("postit")[0] + " - Width: " + adapter.getPosition("postit")[1]);
		adapter.savePosition("linkOne", 258, 65);
		adapter.savePosition("linkTwo", 258, 65);
		adapter.savePosition("linkThree", 258, 65);
		adapter.savePosition("linkFour", 258, 65);
		adapter.savePosition("linkFive", 258, 65);*/
		
		
		
		
		status_text = (TextView) findViewById(R.id.statustext);
		status_pic = (ImageView) findViewById(R.id.statuspic);
		movePostit = (ImageView) findViewById(R.id.postit);
		moveAboutme = (ImageView) findViewById(R.id.aboutme);
		moveCoffee = (ImageView) findViewById(R.id.coffee);
		aLayout = (AbsoluteLayout) findViewById(R.id.alayout);
		moveLinkOne = (ImageView) findViewById(R.id.linkOne);
		moveLinkTwo = (ImageView) findViewById(R.id.linkTwo);
		moveLinkThree = (ImageView) findViewById(R.id.linkThree);
		moveLinkFour = (ImageView) findViewById(R.id.linkFour);
		moveLinkFive = (ImageView) findViewById(R.id.linkFive);
		ImageView ituLogo = (ImageView) findViewById(R.id.itulogo);
		
		if(!startThread) {
			Handler handler = new Handler();
			new Thread((Runnable) new TcpServer(handler)).start();
			startThread = true;
			Log.i("test", "I started the TCP Server");
		}
		if(!startThread2) {
			Handler handler2 = new Handler();
			Thread sync = new Thread((Runnable) new AutoSync(handler2, this));
			sync.start();
			startThread2 = true;
			Log.i("test", "I started the AutoSync");
		}
		
		final Intent intent = getIntent();
		if(intent.hasExtra("status_text")) {
			status_text.setText(intent.getStringExtra("status_text"));
		}
		if(intent.hasExtra("status_pic")) {
			byte[] byteArray = intent.getExtras().getByteArray("status_pic");
			BitmapFactory.Options options=new BitmapFactory.Options();
			options.inSampleSize = 3;
			Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length, options);
			while(bmp.getHeight() > maxPictureSize || bmp.getHeight() < minPictureSize) {
				if(bmp.getHeight() > maxPictureSize) {
					bmp = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*0.8), (int)(bmp.getHeight()*0.8), true);
				}
				if(bmp.getHeight() < minPictureSize) {
					bmp = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*1.2), (int)(bmp.getHeight()*1.2), true);
				}
			}
			status_pic.setImageBitmap(bmp);
			if (bmp != null) {
				bmp = null;
			}
			System.gc();
		}
		
		ituLogo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, AdminActivity.class);
				startActivity(intent);
			}
		});
		
		movePostit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//Log.i("mytag", "Height: " + adapter.getPosition("postit")[0] + " - Width: " + adapter.getPosition("postit")[1]);
				Intent intent = new Intent(MainActivity.this, WriteNoteActivity.class);
				startActivity(intent);
			}
		});
		
		moveAboutme.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WebActivity.class);
				String link = adapter.getLink("aboutme");
				intent.putExtra("web", link);
				startActivity(intent);
			}
		});
		moveLinkOne.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WebActivity.class);
				String link = adapter.getLink("linkOne");
				intent.putExtra("web", link);
				startActivity(intent);
			}
		});
		moveLinkTwo.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WebActivity.class);
				String link = adapter.getLink("linkTwo");
				intent.putExtra("web", link);
				startActivity(intent);
			}
		});
		moveLinkThree.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WebActivity.class);
				String link = adapter.getLink("linkThree");
				intent.putExtra("web", link);
				startActivity(intent);
			}
		});
		moveLinkFour.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WebActivity.class);
				String link = adapter.getLink("linkFour");
				intent.putExtra("web", link);
				startActivity(intent);
			}
		});
		moveLinkFive.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this, WebActivity.class);
				String link = adapter.getLink("linkFive");
				intent.putExtra("web", link);
				startActivity(intent);
			}
		});
		
		status = adapter.getStatus("1");
		if(status.getCount() > 0) {
			if (status.moveToFirst()){
				do {
				   String text = status.getString(status.getColumnIndex("status"));
				   byteArray =  status.getBlob(status.getColumnIndex("pic"));
				   BitmapFactory.Options options=new BitmapFactory.Options();
				   options.inSampleSize = 3;
				   Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length,options);
				   while(bmp.getHeight() > maxPictureSize || bmp.getHeight() < minPictureSize) {
						if(bmp.getHeight() > maxPictureSize) {
							bmp = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*0.8), (int)(bmp.getHeight()*0.8), true);
						}
						if(bmp.getHeight() < minPictureSize) {
							bmp = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*1.2), (int)(bmp.getHeight()*1.2), true);
						}
					}
				   status_pic.setImageBitmap(bmp);
				   status_text.setText(text);
				   if (bmp != null) {
						bmp = null;
						byteArray = null;
					}
				   System.gc();
				}
				while(status.moveToNext());
			}
			status.close();
		}
	}
	public static void updateAppIcon(String app, byte[] icon) {
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 3;
		Bitmap bmp = BitmapFactory.decodeByteArray(icon, 0, icon.length,options);
		bmp = Bitmap.createScaledBitmap(bmp, 150, 150, true);
		if(app.equalsIgnoreCase("linkOne")) {
			moveLinkOne.setImageBitmap(bmp);
		}
		if(app.equalsIgnoreCase("linkTwo")) {
			moveLinkTwo.setImageBitmap(bmp);
		}
		if(app.equalsIgnoreCase("linkThree")) {
			moveLinkThree.setImageBitmap(bmp);
		}
		if(app.equalsIgnoreCase("linkFour")) {
			moveLinkFour.setImageBitmap(bmp);
		}
		if(app.equalsIgnoreCase("linkFive")) {
			moveLinkFive.setImageBitmap(bmp);
		}
		if (bmp != null) {
			bmp = null;
		}
		System.gc();
	}
	
	public static void setStatus(String status, byte[] pic) {
		if(pic.length <= 0) {
			Cursor cursor = adapter.getStatus("1");
			if(cursor.getCount() > 0) {
				if (cursor.moveToFirst()){
					pic = cursor.getBlob(cursor.getColumnIndex("pic"));
					cursor.close();
				}
			}
		}
		adapter.saveStatus(pic, status);
		status_text.setText(status);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 3;
		Bitmap bmp = BitmapFactory.decodeByteArray(pic, 0, pic.length,options);
		while(bmp.getHeight() > maxPictureSize || bmp.getHeight() < minPictureSize) {
			if(bmp.getHeight() > maxPictureSize) {
				bmp = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*0.8), (int)(bmp.getHeight()*0.8), true);
			}
			if(bmp.getHeight() < minPictureSize) {
				bmp = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*1.2), (int)(bmp.getHeight()*1.2), true);
			}
		}
		status_pic.setImageBitmap(bmp);
		if (bmp != null) {
			bmp = null;
		}
		System.gc();
	}
	
	public static void setStatusNoRest(String status, byte[] pic) {
		adapter.saveStatusNoRest(pic, status);
		status_text.setText(status);
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 3;
		Bitmap bmp = BitmapFactory.decodeByteArray(pic, 0, pic.length,options);
		while(bmp.getHeight() > maxPictureSize || bmp.getHeight() < minPictureSize) {
			if(bmp.getHeight() > maxPictureSize) {
				bmp = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*0.8), (int)(bmp.getHeight()*0.8), true);
			}
			if(bmp.getHeight() < minPictureSize) {
				bmp = Bitmap.createScaledBitmap(bmp,(int)(bmp.getWidth()*1.2), (int)(bmp.getHeight()*1.2), true);
			}
		}
		status_pic.setImageBitmap(bmp);
		if (bmp != null) {
			bmp = null;
		}
		System.gc();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//MenuItem item = menu.add(Menu.NONE, Menu.FIRST, Menu.NONE, "Admin");
		//item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int id, MenuItem item) {
		// Receipt behavior
		if(item.getItemId() == (Menu.FIRST)) {
		}
		return true;
	}
	
	public static void toaster(String text) {
		Toast.makeText(context, text, Toast.LENGTH_LONG).show();
	}
	
	protected void onDestroy() {
		super.onDestroy();
		//adapter.close();
	}

}
