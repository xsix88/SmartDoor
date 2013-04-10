package itu.dk.masterthesis.smartdoor;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBhelper extends SQLiteOpenHelper {

	Context context;

	public DBhelper(Context context) {
		super(context, "smartdb", null, 1);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE notes (_id integer primary key autoincrement, person TEXT, note TEXT, time DATETIME)");
		db.execSQL("CREATE TABLE statuses (_id integer primary key autoincrement, pic BLOB, status TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE notes");
		db.execSQL("DROP TABLE statuses");
	}

}