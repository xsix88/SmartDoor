package itu.dk.masterthesis.smartdoor;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.os.Handler;
import android.util.Log;

public class TcpServer implements Runnable {
	
	private Handler handler;
	private Socket socket;
	private InputStream is;
	private ServerSocket serverSocket;
	private int serverPort;
	
	TcpServer(Handler h) {
		handler = h;
		serverPort = 7896;
		socket = null;
		serverSocket = null;
		try {
			serverSocket = new ServerSocket(serverPort);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	@Override
	public void run() {
		byte buffer[] = new byte[1024];
		
		while(true) {
			try{
				socket = serverSocket.accept(); // blocking call
				is = socket.getInputStream();
				DataInputStream dis = new DataInputStream( is );
				final String message = dis.readUTF();
				
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				for(int s; (s=is.read(buffer)) != -1; )
				{
				  baos.write(buffer, 0, s);
				}
				final byte pic[] = baos.toByteArray();
				
				handler.post(new Runnable() {
	                @Override
	                public void run() {
                		MainActivity.setStatus(message, pic);
	                }
	            });
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
