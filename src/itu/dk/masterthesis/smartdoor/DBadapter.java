package itu.dk.masterthesis.smartdoor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBadapter {
	DBhelper helper;
	SQLiteDatabase db;
	Context context;
	
	public DBadapter(Context context) {
		helper = new DBhelper(context);
		this.context = context;
	}
	
	public void open() {
		db = helper.getWritableDatabase();
	}
	
	public void close() {
		db.close();
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
		db.execSQL("INSERT INTO notes(person, note, time) VALUES(?, ?, datetime('now'))", new String[]{person, note});
	}
	
	public void savePosition(String app, int x, int y) {
		ContentValues values = new ContentValues();
		values.put("app", app);
		values.put("x", x);
		values.put("y", y);
        db.insert("positions", null, values);
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
	
	public void saveStatic(String status) {
		ContentValues values = new ContentValues();
		values.put("status", status);
        db.insert("statics", null, values);
	}
	
	public void saveStatus(byte[] picture, String status) {
		ContentValues values = new ContentValues();
		values.put("pic", picture);
		values.put("status", status);
        db.insert("statuses", null, values);
	    //db.execSQL("INSERT INTO statuses(pic, status) VALUES(?, ?)", new String[]{picture, status});
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
