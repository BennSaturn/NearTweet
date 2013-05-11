package neartweet.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.Parcelable;
import android.util.Log;

public class ServerListenerService extends Service {

	public static final String EXTRA_MESSENGER="nearTweet.neartweetclient.EXTRA_MESSENGER";
	private static final String TAG = ServerListenerService.class.getSimpleName();
	private ServerSocket serverSocketReceive;
	private Socket clientReceive;
	private BufferedReader inputReader;
	private ArrayList<Tweet> tweetlist = new ArrayList<Tweet>();
	private Intent i;
	private Thread t;
	private boolean run = true; 
	//private Message msg;
	//private Bundle data;
	//private Messenger messenger;


	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i(TAG, "Service onStartCommand " + startId);
		this.i=intent;
		Runnable connection = new Runnable() {
			public void run() {
				String resultRead = null;
				String[] resultSplit = null;
				String[] usertweet = null;
				String[] msgtweet = null;
				String[] t1 = null;
				String[] t2 = null;
				int index;
				Tweet t;
				int flag = 0;

				//serverSocketReceive = new ServerSocket(6000);
				while(run) {
					try{
						clientReceive = serverSocketReceive.accept();
						Log.i(TAG, "Service onStartCommand " + "Recebeu dados do servidor!");
						inputReader = new BufferedReader(new InputStreamReader(clientReceive.getInputStream()));
						index = 0;
						if(inputReader != null ){
							resultRead = inputReader.readLine();
							System.out.println(resultRead.toString());
							if (resultRead.toString().equals("REPLY")) {
								Bundle extras=i.getExtras();
								if (extras!=null) {
									Messenger messenger=(Messenger)extras.get(EXTRA_MESSENGER);
									Message msg = Message.obtain();
									Bundle data = new Bundle();
									data.putString("reply", resultRead);
									msg.setData(data);
									try {
										Log.i(TAG, "Service onStartCommand: send data !");
										messenger.send(msg);	
									}
									catch (android.os.RemoteException e1) {
										Log.w(getClass().getName(), "Exception sending message", e1);
									}
								}
							}
							else{
								Log.i(TAG, "Service onStartCommand " + "O que recebeu: " + resultRead);
								resultSplit = resultRead.split("\\{");
								System.out.println("r[1]: " + resultSplit[1]);
								usertweet = resultSplit[1].split("\\}");
								if(usertweet.length == 1){
									System.out.println("u[0]: " + usertweet[0]);
									msgtweet = usertweet[0].split(", ");
									if(msgtweet.length != 0){
										System.out.println("msgtweet length: " + msgtweet.length);
										System.out.println(msgtweet[0]);
										tweetlist.clear();
										while(msgtweet.length > index){
											t1 = msgtweet[index].split("=");
											t2 = t1[1].split(" - ");
											t = new Tweet(t2[0],t2[1]);
											tweetlist.add(0,t);
											index++;
										}	
									} else {
										t1 = msgtweet[index].split("=");
										t2 = t1[1].split(" - ");
										t = new Tweet(t2[0],t2[1]);
										tweetlist.add(0,t);
									}
								}
								Bundle extras=i.getExtras();
								if (extras!=null) {
									Messenger messenger=(Messenger)extras.get(EXTRA_MESSENGER);
									Message msg = Message.obtain();
									Bundle data = new Bundle();
									data.putParcelableArrayList("listtweet", tweetlist);
									msg.setData(data);
									try {
										Log.i(TAG, "Service onStartCommand: send data !");
										messenger.send(msg);	
									}
									catch (android.os.RemoteException e1) {
										Log.w(getClass().getName(), "Exception sending message", e1);
									}
								}
							}
						}
						clientReceive.close();


					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};   
		t = new Thread(connection);
		t.start();
		return Service.START_STICKY;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service creating");
		try {
			serverSocketReceive = new ServerSocket(6000);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "Service destroying");
		//t.interrupt();
		run=false;
		try {
			serverSocketReceive.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
