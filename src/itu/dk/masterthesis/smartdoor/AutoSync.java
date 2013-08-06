package itu.dk.masterthesis.smartdoor;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;

public class AutoSync implements Runnable {
	Handler handler;
	DBadapter adapter;
	byte[] picture = null;
	String status = null;
	String[] defaults;
	
	public AutoSync(Handler handler, Context context) {
		this.handler = handler;
		adapter = new DBadapter(context);
		adapter.open();
	}

	@Override
	public void run() {
        Looper.prepare();
		while(true) {
			defaults = adapter.checkForNewDefaults().split("BREAK");
			final byte[] pic = Base64.decode(defaults[0], 0);
			final String message = defaults[1];
	        Cursor cursor = adapter.getStatic("1");
	        if(cursor.getCount() > 0) {
				if (cursor.moveToFirst()){
					picture = cursor.getBlob(cursor.getColumnIndex("pic"));
					status = cursor.getString(cursor.getColumnIndex("status"));
				}
			}
	        if(status != message || pic != picture) {
	        	adapter.syncDefaultsFromServer();
	        }
	        
	        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
	        
	        picture = null;
			status = null;
			String[] statuses;
			statuses = adapter.checkForNewStatuses().split("BREAK");
			final byte[] pic2 = Base64.decode(statuses[0], 0);
			final String message2 = statuses[1];
	        Cursor cursor2 = adapter.getStatus("1");
	        if(cursor2.getCount() > 0) {
				if (cursor2.moveToFirst()){
					picture = cursor2.getBlob(cursor2.getColumnIndex("pic"));
					status = cursor2.getString(cursor2.getColumnIndex("status"));
				}
			}
	        if(status != message2 || pic2 != picture) {
	        	handler.post(new Runnable() {
	                @Override
	                public void run() {
	            		MainActivity.setStatusNoRest(message2, pic2);
	                }
	            });
	        }
			try { Thread.sleep(20000); } catch (InterruptedException e) { e.printStackTrace(); }
			
			/*handler.post(new Runnable() {
	            @Override
	            public void run() {
	        		MainActivity.setStatus(message, pic);
	            }
	        });*/
		}
		//Looper.loop();
	}
}
