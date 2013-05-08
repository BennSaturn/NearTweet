package neartweet.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class SpamTask extends AsyncTask<String, String, String>{

		private ProgressDialog progress;
		private TweetListActivity context;
		private Socket clientSend;
		private ServerSocket serverSocketReceive;
		private Socket clientReceive;
		private PrintWriter printwriter;
		private BufferedReader inputReader;
		
		public SpamTask(TweetListActivity context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			//Cria novo um ProgressDialogo e exibe
			progress = new ProgressDialog(context);
			progress.setMessage("A notificar o servidor de que o Tweet e SPAM....");
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

			// connect to the server and send the message
			try {
				clientSend = new Socket("10.0.2.2", 4444);
				serverSocketReceive = new ServerSocket(6000);
				printwriter = new PrintWriter(clientSend.getOutputStream(),true);
				printwriter.write(params[0]);
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
		protected void onPostExecute(String result) {
			//Cancela progressDialogo e envia resultado
			System.out.println("onPostExecute!!!!!!!");
			progress.dismiss();
			context.setSpamResult(result);
			
		}

		@Override
		protected void onProgressUpdate(String... values) {
			//Atualiza mensagem
			progress.setMessage(values[0]);
		}

}
