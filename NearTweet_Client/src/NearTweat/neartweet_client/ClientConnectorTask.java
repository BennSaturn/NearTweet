package NearTweat.neartweet_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.AsyncTask;

public class ClientConnectorTask extends AsyncTask<String, Void, Integer> {
	private Socket clientSend;
	private Socket clientReceive;
	private PrintWriter printwriter;
	private BufferedReader inputReader;
	protected Integer doInBackground(String...strings) {
		// validate input parameters
		if (strings.length <= 0) {
			return 0;
		}
		// connect to the server and send the message
		try {
			clientSend = new Socket("10.0.2.2", 4444);
			clientReceive = new Socket("10.0.2.2", 4445);
			printwriter = new PrintWriter(clientSend.getOutputStream(),true);
			printwriter.write(strings[0]);
			printwriter.flush();      
			printwriter.close();
			clientSend.close();
			inputReader = new BufferedReader(new InputStreamReader(clientReceive.getInputStream()));
			System.out.println("Input Reader: " + inputReader.readLine());
			inputReader.close();                       
			clientReceive.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}
	protected void onPostExecute(Long result) {
		return;
	}
}
