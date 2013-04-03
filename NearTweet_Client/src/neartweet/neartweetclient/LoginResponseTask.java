package neartweet.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class LoginResponseTask extends AsyncTask<String, String, String>  {
	private ProgressDialog progress;
	private MainActivity context;
	private Socket clientSend;
	private Socket clientReceive;
	private PrintWriter printwriter;
	private BufferedReader inputReader;

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
		try {
			System.out.println("Inicio dos sockets!!");
			clientSend = new Socket("10.0.2.2", 4444);
			clientReceive = new Socket("10.0.2.2", 4445);
			printwriter = new PrintWriter(clientSend.getOutputStream(),true);
			inputReader = new BufferedReader(new InputStreamReader(clientReceive.getInputStream()));
			printwriter.write(params[0]);
			printwriter.flush();      
			printwriter.close();
			clientSend.close();
		
			//int init = (int) System.currentTimeMillis();
			//System.out.println(init);
			int time = 0;
			long timeout = 100;
			String line;
			while (time<timeout){
				//time += (int) (System.currentTimeMillis() - init);
				time ++;
				System.out.println(time);
				if ((line = inputReader.readLine()) != null){
					inputReader.close();
					clientReceive.close();
					System.out.println("ClientConnecterTask: "+line.toString());
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
