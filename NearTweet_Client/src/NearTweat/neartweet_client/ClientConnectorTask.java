package NearTweat.neartweet_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import android.os.AsyncTask;

public class ClientConnectorTask extends AsyncTask<String, Void, Integer> {
        private Socket client;
        private PrintWriter printwriter;
        
        protected Integer doInBackground(String...strings) {
                // validate input parameters
                if (strings.length <= 0) {
                        return 0;
                }
                // connect to the server and send the message
                try {
                        client = new Socket("10.0.2.2", 4444);
                        printwriter = new PrintWriter(client.getOutputStream(),true);
                        printwriter.write(strings[0]);
                        printwriter.flush();
                        printwriter.close();
                        client.close();
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
        
        protected String serverResponse(){

        	BufferedReader in;
        	
        	try {
        		if(!client.isConnected()){
        			client = new Socket("10.0.2.2", 4444);
        		}
    			
    			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    	        String line;
    	        
    			while (true){
    				if ((line = in.readLine()) != null){
    					in.close();
    					client.close();
    					return line.toString();
    				}
    				//falta introduzir um temporizador por causa da perda de ligacao
    			}
        	} catch (UnknownHostException e) {
        		e.printStackTrace();
        	} catch (IOException e) {
        		e.printStackTrace();
        	}
    		return null;
        }
}
