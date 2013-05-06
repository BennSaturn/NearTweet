package neartweet.neartweetclient;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class SpamTask extends AsyncTask<String, String, String>{

		private ProgressDialog progress;
		private SpamActivity context;
		private String tweetResult;
		
		public SpamTask(SpamActivity context) {
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
				System.out.println("params[0]: " + params[0]);
				tweetResult = CommunicationCS.obtainInfo(params[0]);
				//int init = (int) System.currentTimeMillis();
				//System.out.println(init);
				int time = 0;
				long timeout = 100;
				while (time<timeout){
					//time += (int) (System.currentTimeMillis() - init);
					time ++;
					System.out.println(time);
					if (tweetResult != null){
						//clientReceive.close();
						System.out.println("SpamTask: "+ tweetResult);
						return tweetResult;
					}
				}
			return tweetResult;
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
