package neartweet.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class ServerListenerService extends Service {
	
	private static final String TAG = ServerListenerService.class.getSimpleName();
	private ServerSocket serverSocketReceive;
	private Socket clientReceive;
	private static BufferedReader inputReader;
	

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
			
		Log.i(TAG, "Service onStartCommand " + startId);
			
		Runnable connection = new Runnable() {
		     public void run() {
		    	 try {
					clientReceive = serverSocketReceive.accept();
					inputReader = new BufferedReader(new InputStreamReader(clientReceive.getInputStream()));
					String result = inputReader.readLine();			
					clientReceive.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    	
		    	 
		    	 
		    	 /* 
		        for (int i = 0; i < 3; i++)
		        {
		        	long endTime = System.currentTimeMillis() + 10*1000;
		        		
		        	while (System.currentTimeMillis() < endTime) {
		        		synchronized (this) {
		        		 try {
		        			wait(endTime - System.currentTimeMillis());
		        		 } catch (Exception e) {
		        		}
		        	}
		          }		
		          Log.i(TAG, "Service running");
		        }
		        */
		    	 
		        stopSelf();
		       }
		};   
		Thread t = new Thread(connection);
		t.start();
		return Service.START_STICKY;
	}
	
	@Override
	  public void onCreate() {
	    super.onCreate();
	    Log.i(TAG, "Service creating");
	    try {
			serverSocketReceive = new ServerSocket(6000);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	     
	  }
	 
	  @Override
	  public void onDestroy() {
	    super.onDestroy();
	    Log.i(TAG, "Service destroying");
	     
	  }
	
}
