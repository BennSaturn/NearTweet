package neartweet.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetTweetsTask extends AsyncTask<String, String, Map<String,String>>{

	private ProgressDialog progress;
	private TweetListActivity context;
	private Socket clientSend;
	private Socket clientReceive;
	private PrintWriter printwriter;

	public GetTweetsTask(TweetListActivity context) {
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
	protected Map<String,String> doInBackground(String... params) {

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

		// connect to the server and send the message
		try {
			System.out.println("Inicio dos sockets!!");
			clientSend = new Socket("10.0.2.2", 4444);
			clientReceive = new Socket("10.0.2.2", 4445);
			printwriter = new PrintWriter(clientSend.getOutputStream(),true);
			ObjectInputStream objectInput = new ObjectInputStream(new GZIPInputStream(clientReceive.getInputStream()));
			printwriter.write(params[0]);
			printwriter.flush();      
			printwriter.close();
			clientSend.close();
		
			//int init = (int) System.currentTimeMillis();
			//System.out.println(init);
			int time = 0;
			long timeout = 100;
			Map<String,String> list;
			while (time<timeout){
				//time += (int) (System.currentTimeMillis() - init);
				time ++;
				System.out.println(time);
				if ((list = (Map<String, String>) objectInput.readObject()) != null){
					objectInput.close();
					clientReceive.close();
					System.out.println("ClientConnecterTask: "+list.toString());
					return list;
				}
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Map<String,String> result) {
		//Cancela progressDialogo e envia resultado
		System.out.println("onPostExecute!!!!!!!");
		progress.dismiss();
		context.setTweetList(result);
		
	}

	@Override
	protected void onProgressUpdate(String... values) {
		//Atualiza mensagem
		progress.setMessage(values[0]);
	}

}
