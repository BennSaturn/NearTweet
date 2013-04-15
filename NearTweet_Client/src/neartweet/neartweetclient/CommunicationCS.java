package neartweet.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class CommunicationCS {

	/**
	 * @param args
	 */

	private static Socket clientSend;
	private static Socket clientReceive;
	private static PrintWriter printwriter;
	private static BufferedReader inputReader;
	private static int cliSend = 0;
	private static int cliReceive = 0;
	private static String result;
	private static List<Tweet> tweetlist = new ArrayList<Tweet>();

	public static String obtainInfo(String params) {
		System.out.println("Inicio dos sockets!!");
		if(cliSend == 0){
			try {
				clientSend = new Socket("10.0.2.2", 4444);
				cliSend = 1;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Client closed? " + clientSend.isClosed());
		if(cliReceive == 0){
			try {
				clientReceive = new Socket("10.0.2.2", 4445);
				cliReceive = 1;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Client closed? " + clientReceive.isClosed());
		try {
			printwriter = new PrintWriter(clientSend.getOutputStream(),true);
			printwriter.write(params);
			printwriter.close();
			inputReader = new BufferedReader(new InputStreamReader(clientReceive.getInputStream()));
			result = inputReader.readLine();			
			clientSend.close();
			clientReceive.close();
			cliSend = 0;
			cliReceive = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	public static List<Tweet> obtainInfo2(String params) {
		String[] resultSplit = null;
		String[] usertweet = null;
		String[] msgtweet = null;
		String[] tweet = null;
		int i = 0;
		Tweet t;
		System.out.println("Inicio dos sockets!!");
		if(cliSend == 0){
			try {
				clientSend = new Socket("10.0.2.2", 4444);
				cliSend = 1;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Client closed? " + clientSend.isClosed());
		if(cliReceive == 0){
			try {
				clientReceive = new Socket("10.0.2.2", 4445);
				cliReceive = 1;
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println("Client closed? " + clientReceive.isClosed());
		try {
			printwriter = new PrintWriter(clientSend.getOutputStream());
			printwriter.write(params);
			printwriter.flush();      
			printwriter.close();
			inputReader = new BufferedReader(new InputStreamReader(clientReceive.getInputStream()));
			System.out.println(params);

			if(inputReader != null ){
				resultSplit = inputReader.readLine().split("\\{");
				System.out.println("r[1]: " + resultSplit[1]);
				usertweet = resultSplit[1].split("\\}");
				if(usertweet.length == 1){
					System.out.println("u[0]: " + usertweet[0]);
					msgtweet = usertweet[0].split(", ");
					if(msgtweet.length != 0){
						System.out.println("msgtweet length: " + msgtweet.length);
						System.out.println(msgtweet[0]);
						tweetlist.clear();
						while(msgtweet.length > i){
							tweet = msgtweet[i].split("=");
							t = new Tweet(tweet[1],tweet[0]);
							tweetlist.add(t);
							i++;
						}	
					} else {
						tweet = usertweet[0].split("=");
						t = new Tweet(tweet[1],tweet[0]);
						tweetlist.add(t);
					}

				}
			}

			clientSend.close();
			clientReceive.close();
			cliSend = 0;
			cliReceive = 0;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tweetlist;
	}

}
