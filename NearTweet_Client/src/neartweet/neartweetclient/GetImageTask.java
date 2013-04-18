package neartweet.neartweetclient;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GetImageTask extends AsyncTask<String,String,String>{

	private ProgressDialog progress;
	private TweetActivity context;
	private static int numImage = 0;
	private static String result;
	
	public GetImageTask(TweetActivity context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		//Cria novo um ProgressDialogo e exibe
		progress = new ProgressDialog(context);
		progress.setMessage("A carregar imagem...");
		progress.show();
	}

	@Override
	protected String doInBackground(String... params) {

		System.out.println("doInBackground!!!!!!!");
		// validate input parameters
		if (params.length <= 0) {
			result = "ERR0!";
			return result;
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
			ImageDownloader.DownloadFromUrl(params[0], "Image000" + numImage+".jpg");
			
			int time = 0;
			long timeout = 100;
			while (time<timeout){
				//time += (int) (System.currentTimeMillis() - init);
				time ++;
				System.out.println(time);
			}
		result = "OK!";
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		//Cancela progressDialogo e envia resultado
		System.out.println("onPostExecute!!!!!!!");
		progress.dismiss();
		context.setResultPhoto(result);
		
	}
}
