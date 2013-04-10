package itu.dk.masterthesis.smartdoor;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ReadNoteActivity extends ListActivity {

	DBadapter adapter;
	SimpleCursorAdapter cursorAdapter;
	Cursor notes;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new DBadapter(this);
		adapter.open();
		notes = adapter.getNotes();
		startManagingCursor(notes);
		cursorAdapter = new SimpleCursorAdapter (this, android.R.layout.simple_list_item_2, notes, new String[] { "person", "note" }, new int [] { android.R.id.text1, android.R.id.text2});
		this.setListAdapter(cursorAdapter);
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Cursor cursor = (Cursor) l.getItemAtPosition(position);
		Intent intent = new Intent(ReadNoteActivity.this, AdminActivity.class).putExtra("SELECTED_NOTE", cursor.getString(cursor.getColumnIndexOrThrow("note")));
		//setResult(RESULT_OK, intent);
		cursor.close();
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
		//finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.read_note, menu);
		return true;
	}

}
