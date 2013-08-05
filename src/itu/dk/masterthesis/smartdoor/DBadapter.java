package itu.dk.masterthesis.smartdoor;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class DBadapter {
	DBhelper helper;
	static SQLiteDatabase db;
	static Context context;
	static int count;
	static String status;
	static byte[] picture;
	static long datetime;
	static String from;
	static String note;
	static int x;
	static int y;
	static String app;
	
	public DBadapter(Context context) {
		helper = new DBhelper(context);
		DBadapter.context = context;
	}
	
	public void open() {
		db = helper.getWritableDatabase();
	}
	
	public void close() {
		db.close();
	}
	
	public void syncDefaultsFromServer() {
		clearStatics();
		int cmd = 1;
		new AsyncRest(context, cmd).execute("http://jsnas.dyndns.org/SmartDoorRestAPI/defaults/index.php?owner_id=1");
	}	
	
	public Cursor getNotes() {
		return db.rawQuery("SELECT * FROM notes ORDER BY _id DESC LIMIT ?", new String[]{"10"});
	}
	
	public int[] getPosition(String app) {
		Cursor handlePos = db.rawQuery("SELECT x, y FROM positions WHERE app=? ORDER BY _id DESC LIMIT 1", new String[]{app});
		handlePos.moveToFirst();
		Log.i("DBadapter", "Get Position X: "+handlePos.getInt(handlePos.getColumnIndex("x")) + " Get Position Y: "+handlePos.getInt(handlePos.getColumnIndex("y")));
		int[] result = { handlePos.getInt(handlePos.getColumnIndex("x")), handlePos.getInt(handlePos.getColumnIndex("y")) };
		handlePos.close();
		return result;
	}
	
	public void saveNote(String person, String note) {
		DBadapter.datetime = new Date().getTime();
		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = fmt.format(DBadapter.datetime);
		ContentValues values = new ContentValues();
		values.put("person", person);
		values.put("note", note);
		values.put("time", dateString);
        db.insert("notes", null, values);
		DBadapter.from = person;
		DBadapter.note = note;
		Cursor nCount = db.rawQuery("SELECT MAX(_id) FROM notes", new String[]{});
		nCount.moveToFirst();
		count = nCount.getInt(0);
		nCount.close();
		int cmd = 5;
		new AsyncRest(context, cmd).execute("http://jsnas.dyndns.org/SmartDoorRestAPI/notes/index.php");
	}
	
	public void savePosition(String app, int x, int y) {
		ContentValues values = new ContentValues();
		values.put("app", app);
		values.put("x", x);
		values.put("y", y);
        db.insert("positions", null, values);
        DBadapter.app = app;
        DBadapter.x = x;
        DBadapter.y = y;
        int cmd = 6;
		new AsyncRest(context, cmd).execute("http://jsnas.dyndns.org/SmartDoorRestAPI/apps/index.php");
		Log.i("savePos", "App: " + app + " x: " + x + " y: " + y);
	}
	
	public Cursor getStatus(String amount) {
		return db.rawQuery("SELECT * FROM statuses ORDER BY _id DESC LIMIT ?", new String[] {amount});
	}
	
	public Cursor getStatic(String amount) {
		return db.rawQuery("SELECT * FROM statics ORDER BY _id DESC LIMIT ?", new String[] {amount});
	}
	
	public int getNumberOfNotes() {
		Cursor nCount = db.rawQuery("SELECT count(*) FROM notes", new String[]{});
		nCount.moveToFirst();
		int count = nCount.getInt(0);
		nCount.close();
		return count;
	}
	
	public int getNumberOfStatics() {
		Cursor nCount = db.rawQuery("SELECT count(*) FROM statics", new String[]{});
		nCount.moveToFirst();
		int count = nCount.getInt(0);
		nCount.close();
		return count;
	}
	
	public static void saveStatic(byte[] picture, String status) {
		ContentValues values = new ContentValues();
		values.put("pic", picture);
		values.put("status", status);
        db.insert("statics", null, values);
        Cursor nCount = db.rawQuery("SELECT MAX(_id) FROM statics", new String[]{});
		nCount.moveToFirst();
		count = nCount.getInt(0);
		nCount.close();
		DBadapter.picture = picture;
		DBadapter.status = status;
        int cmd = 2;
		new AsyncRest(context, cmd).execute("http://jsnas.dyndns.org/SmartDoorRestAPI/defaults/index.php");
	}
	
	public void saveStatus(byte[] picture, String status) {
		Date myDate = new Date();
	    long timeMilliseconds = myDate.getTime();
		ContentValues values = new ContentValues();
		values.put("pic", picture);
		values.put("status", status);
		values.put("datetime", timeMilliseconds);
        db.insert("statuses", null, values);
        Cursor nCount = db.rawQuery("SELECT MAX(_id) FROM statuses", new String[]{});
		nCount.moveToFirst();
		count = nCount.getInt(0);
		nCount.close();
		DBadapter.picture = picture;
		DBadapter.status = status;
		DBadapter.datetime = timeMilliseconds;
        int cmd = 2;
		new AsyncRest(context, cmd).execute("http://jsnas.dyndns.org/SmartDoorRestAPI/status/index.php");
	}
	
	public void clearNotes() {
		db.execSQL("DELETE FROM notes");
	}
	
	public void clearStatics() {
		db.execSQL("DELETE FROM statics");
	}

	public void clearPictures() {
		db.execSQL("DELETE FROM statuses");
	}
	public void clearPositions() {
		db.execSQL("DELETE FROM positions");
	}
}

class AsyncRest extends AsyncTask<String, Void, String> {
	Context mContext;
	int cmd;
	byte[] byteArray;
	String status;
	private Exception exception;
	AsyncRest(Context context, int cmd) {
		this.mContext = context;
		this.cmd = cmd;
	}
    
    @Override
    protected String doInBackground(String... urls) {
    	//EditText idnumber = (EditText) ((Activity) mContext).findViewById(R.id.idnumber);
    	try {
            URL url= new URL(urls[0]);
            Log.i("Smartdoor", "URL: "+url);
            RestHandler rest = new RestHandler();
            switch(cmd) {
            	case 1:
            		/*if(idnumber.length() > 0) {
	            		if(Integer.parseInt(idnumber.getText().toString()) > 0) {
	            			JSONArray json = rest.doGet(url+"?id="+idnumber.getText());
	            			JSONObject c = json.getJSONObject(0);
	            			Log.i("Smartdoor json", c.getString("status"));
	            			byteArray = Base64.decode(c.getString("picture"), 0);
	            			return "ID: " + c.getString("id") +" OwnerID: "+ c.getString("owner_id") +" Status: "+ c.getString("status");
	            		}
            		} else {*/
            		//URL url = new URL("http://jsnas.dyndns.org/SmartDoorRestAPI/defaults/index.php?owner_id=1");
        			JSONArray json = rest.doGet(url+"");
        			for(int i = 0; i < json.length(); i++){
        		        JSONObject c = json.getJSONObject(i);
        		        byteArray = Base64.decode(c.getString("picture"), 0);
        		        status = c.getString("status");
        		        DBadapter.saveStatic(byteArray, status);
            		}
        			break;
            	case 2:
            		JSONObject json1 = new JSONObject();
            		json1.put("id", DBadapter.count);
            		json1.put("owner_id", 1);
            		json1.put("status", DBadapter.status);
            		json1.put("picture", Base64.encodeToString(DBadapter.picture, Base64.DEFAULT));
            		SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            		String dateString = fmt.format(DBadapter.datetime);
            		Log.i("SmartDoor", "Date: "+dateString);
            		json1.put("datetime", dateString);
            		return rest.doPost(url+"", json1).toString();
            	/*case 3:
            		JSONObject json1 = new JSONObject();
            		json1.put("id", idnumber.getText());
            		json1.put("owner_id", 3);
            		json1.put("status", "Go away");
            		json1.put("picture", "Bla");
            		return rest.doPut(url+"", json1).toString();*/
            	/*case 4:
            		rest.doDelete(url+"?id="+idnumber.getText());
            		return "Deleted";*/
            	case 5:
            		JSONObject json11 = new JSONObject();
            		json11.put("id", DBadapter.count);
            		json11.put("owner_id", 1);
            		json11.put("note", DBadapter.note);
            		json11.put("fromperson", DBadapter.from);
            		SimpleDateFormat fmt1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            		String dateString1 = fmt1.format(DBadapter.datetime);
            		Log.i("SmartDoor", "Date: "+dateString1);
            		json11.put("datetime", dateString1);
            		return rest.doPost(url+"", json11).toString();
            	case 6:
            		JSONObject json2 = new JSONObject();
            		json2.put("owner_id", 1);
            		json2.put("app", DBadapter.app);
            		json2.put("x", DBadapter.x);
            		json2.put("x", DBadapter.y);
            		return rest.doPut(url+"", json2).toString();
            }
        } catch (Exception e) {
            this.exception = e;
        }
        return null;
    }
    
    @Override
    protected void onPostExecute(String result) {
    	if(exception != null) {
    		Log.i("Smartdoor", "AsyncTask Exception: "+exception);
    	}
    	Log.i("Smartdoor", "Async Result: " + result);
    	/*if(byteArray != null) {
	     	Bitmap bmp = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
	    	ImageView status_pic = (ImageView) ((Activity) mContext).findViewById(R.id.status_pic);
			status_pic.setImageBitmap(bmp);
    	}
    	TextView result_text = (TextView) ((Activity) mContext).findViewById(R.id.result);
    	result_text.setText(result);*/
    }
}
