package neartweet.neartweetclient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetTweetsTask extends AsyncTask<String, String, List<Tweet>>{

	private ProgressDialog progress;
	private TweetListActivity context;
	private List<Tweet> objectInput;
	
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
	protected List<Tweet> doInBackground(String... params) {

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
			System.out.println("params[0]: " + params[0]);
			objectInput = CommunicationCS.obtainInfo2(params[0]);
			//int init = (int) System.currentTimeMillis();
			//System.out.println(init);
			int time = 0;
			long timeout = 100;
			Map<String,String> list;
			while (time<timeout){
				//time += (int) (System.currentTimeMillis() - init);
				time ++;
				System.out.println(time);
				if (objectInput != null){
					//clientReceive.close();
					System.out.println("GetTweetsTask: "+objectInput.toString());
					return objectInput;
				}
			}
		return objectInput;
	}

	@Override
	protected void onPostExecute(List<Tweet> result) {
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
