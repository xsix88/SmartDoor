package itu.dk.masterthesis.smartdoor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WriteNoteActivity extends Activity {
	
	DBadapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_writenote);
		
		adapter = new DBadapter(this);
		adapter.open();
		
		final Button addnote_button = (Button) findViewById(R.id.add_note_button);
		final TextView note = (TextView) findViewById(R.id.notetext);
		final TextView person = (TextView) findViewById(R.id.persontext);
		
		addnote_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				adapter.saveNote(person.getText() + "", note.getText() + "");
				Intent intent = new Intent(WriteNoteActivity.this, MainActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				MainActivity.toaster("Note added.");
			 }
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.note, menu);
		return true;
	}
	
	protected void onDestroy() {
		super.onDestroy();
		adapter.close();
	}

}
