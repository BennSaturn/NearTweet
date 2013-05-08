package neartweet.neartweetclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetTweetsTask extends AsyncTask<String, String, Integer>{

	private ProgressDialog progress;
	private TweetListActivity context;
	private Socket clientSend;
	private PrintWriter printwriter;
	//private List<Tweet> objectInput = new ArrayList<Tweet>();

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
	protected Integer doInBackground(String... params) {

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

		//connect to the server and send the message
		System.out.println("params[0]: " + params[0]);

		//if(objectInput.size() > 0) {
		//	System.out.println(objectInput.size());
		//	objectInput.clear();
		//}
		//objectInput = CommunicationCS.obtainInfo2(params[0]);
		//int init = (int) System.currentTimeMillis();
		//System.out.println(init);

		try {
			clientSend = new Socket("10.0.2.2", 4444);

			printwriter = new PrintWriter(clientSend.getOutputStream(),true);
			printwriter.write(params[0]);
			printwriter.flush();      
			printwriter.close();
			clientSend.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
	}

	@Override
	protected void onPostExecute(Integer i) {
		//Cancela progressDialogo e envia resultado
		System.out.println("onPostExecute!!!!!!!");
		progress.dismiss();
		//context.setTweetList(result);

	}

	@Override
	protected void onProgressUpdate(String... values) {
		//Atualiza mensagem
		progress.setMessage(values[0]);
	}

}
