package neartweet.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class LoginResponseTask extends AsyncTask<String, String, String> {
	private ProgressDialog progress;
	private MainActivity context;
	private String result;

	public LoginResponseTask(MainActivity context) {
		this.context = context;
	}

	@Override
	protected void onPreExecute() {
		//Cria novo um ProgressDialogo e exibe
		progress = new ProgressDialog(context);
		progress.setMessage("Aguarde...");
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
				System.out.println("ciclo for!!");
				Thread.sleep(500);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}

		// connect to the server and send the message
		System.out.println("params[0]: " + params[0]);
		result = CommunicationCS.obtainInfo(params[0]);

		//int init = (int) System.currentTimeMillis();
		//System.out.println(init);
		int time = 0;
		long timeout = 100;
		while (time<timeout){
			//time += (int) (System.currentTimeMillis() - init);
			time ++;
			System.out.println(time);

			if (result != null){
				System.out.println("ClientConnecterTask: "+result);
				return result;
			}
		}
		return "ERRO!";
	}

	@Override
	protected void onPostExecute(String result) {
		//Cancela progressDialogo e envia resultado
		System.out.println("onPostExecute!!!!!!!");
		progress.dismiss();
		context.setResult(result);

	}

	@Override
	protected void onProgressUpdate(String... values) {
		//Atualiza mensagem
		progress.setMessage(values[0]);
	}
}
