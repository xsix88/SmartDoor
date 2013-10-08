package itu.dk.masterthesis.smartdoor;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

public class AutoSync implements Runnable {
	Handler handler;
	DBadapter adapter;
	byte[] picture = null;
	String status = null;
	String[] defaults;
	String[] apps;
	String[] appdata;
	
	public AutoSync(Handler handler, Context context) {
		this.handler = handler;
		adapter = new DBadapter(context);
		adapter.open();
	}

	@Override
	public void run() {
        Looper.prepare();
		while(true) {
			
			apps = null;
			appdata = null;
			apps = adapter.getNewApps().split("NEWLINE");
			for(int i = 0; i < apps.length; i++) {
				appdata = apps[i].split("BREAK");
				if(appdata[0].contains("null")) {
					appdata[0] = appdata[0].replace("null", "");
				}
				if(appdata.length == 1) {
				}else if(appdata.length == 2){
					if(adapter.getLink(appdata[0]) != appdata[1]) {
						adapter.updatePosition(appdata[0], appdata[1], new byte[]{});
					}
				}else if(appdata.length == 3) {
					if(adapter.getLink(appdata[0]) != appdata[1]) {
						if(DBadapter.getAppPic(appdata[0]) != Base64.decode(appdata[2], 0)) {
							final byte[] icon = Base64.decode(appdata[2], 0);
							final String app = appdata[0];
							adapter.updatePosition(appdata[0], appdata[1], icon);
							handler.post(new Runnable() {
				                @Override
				                public void run() {
				                	MainActivity.updateAppIcon(app, icon);
				                }
				            });
						}
					}
				}
			}
			apps = null;
			appdata = null;
			System.gc();
	        
	        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
	        
	        defaults = adapter.checkForNewDefaults().split("BREAK");
			final byte[] pic = Base64.decode(defaults[0], 0);
			final String message = defaults[1];
			defaults = null;
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
	        System.gc();
	        try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
	        
	        picture = null;
			status = null;
			String[] statuses;
			statuses = adapter.checkForNewStatuses().split("BREAK");
			final byte[] pic2 = Base64.decode(statuses[0], 0);
			final String message2 = statuses[1];
			statuses = null;
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
	        System.gc();
			try { Thread.sleep(10000); } catch (InterruptedException e) { e.printStackTrace(); }
			
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
