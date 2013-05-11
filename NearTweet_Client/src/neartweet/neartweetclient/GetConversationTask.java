package neartweet.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class GetConversationTask extends AsyncTask<String, String, String>{

	private ProgressDialog progress;
	private TweetSelectedActivity context;
	private List<Tweet> tweetList = new ArrayList<Tweet>();
	private Socket clientSend;
	private ServerSocket serverSocketReceive;
	private Socket clientReceive;
	private PrintWriter printwriter;
	private BufferedReader inputReader;

	public GetConversationTask(TweetSelectedActivity context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		//Cria novo um ProgressDialogo e exibe
		progress = new ProgressDialog(context);
		progress.setMessage("A carregar tweets...");
		progress.show();
	}

	@Override
	protected String doInBackground(String... params) {

		System.out.println("doInBackground!!!!!!!");
		// validate input parameters
		if (params.length <= 0) {
			return null;
		}

		for(int i = 0; i<2; i++){
			try {
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		System.out.println("params[0]: " + params[0]);
		try {
			clientSend = new Socket("10.0.2.2", 4444);
			serverSocketReceive = new ServerSocket(6000);
			printwriter = new PrintWriter(clientSend.getOutputStream(),true);
			printwriter.write(params[0]);
			printwriter.flush();      
			printwriter.close();
			clientSend.close();
			
			clientReceive = serverSocketReceive.accept();
			inputReader = new BufferedReader(new InputStreamReader(clientReceive.getInputStream()));		

			int time = 0;
			long timeout = 100;
			while (time<timeout){
				time ++;
				String line;
				if ((line = inputReader.readLine()) != null){
					inputReader.close();
					clientReceive.close();
					serverSocketReceive.close();
					//System.out.println("ClientConnecterTask: "+line.toString());
					return line.toString();
				}
			}
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "ERRO!";
	}

	@Override
	protected void onPostExecute(String tList) {
		//Cancela progressDialogo e envia resultado
		System.out.println("onPostExecute!!!!!!!");
		progress.dismiss();
		
		String[] resultSplit = tList.split("\\{");
		System.out.println("r[1]: " + resultSplit[1]);
		String[] usertweet = resultSplit[1].split("\\}");
		System.out.println("u[1]: " + usertweet[0]);
		String[] msgtweet = usertweet[0].split(", ");
		String[] t1;
		String[] t2;
		Tweet t;
		int index = 0;
		tweetList.clear();
		if(msgtweet.length != 0){
			System.out.println("msgtweet length: " + msgtweet.length);
			//tweetList.clear();
			while(msgtweet.length > index){
				t1 = msgtweet[index].split("=");
				t2 = t1[1].split(" - ");
				t = new Tweet(t2[0],t2[1]);
				tweetList.add(0,t);
				index++;
			}
		} else {
			t1 = msgtweet[index].split("=");
			t2 = t1[1].split(" - ");
			t = new Tweet(t2[0],t2[1]);
			tweetList.add(0,t);
		}	
		context.setTweetList(tweetList);
	}

	@Override
	protected void onProgressUpdate(String... values) {
		//Atualiza mensagem
		progress.setMessage(values[0]);
	}

}
