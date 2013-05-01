package neartweet.neartweetclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
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
	private static ServerSocket serverSocketReceive;
	private static int port;
	
	public static void initServerSocketReceive() {
		try {
			serverSocketReceive = new ServerSocket(6000);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


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
		
		try {
			printwriter = new PrintWriter(clientSend.getOutputStream(),true);
			printwriter.write(params);
			printwriter.close();
			if(cliReceive == 0){
				clientReceive = serverSocketReceive.accept();
				System.out.println("ligado!");
				cliReceive = 1;
			}
			System.out.println("Client closed? " + clientReceive.isClosed());
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
		String[] t1 = null;
		String[] t2 = null;
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
		
		try {
			printwriter = new PrintWriter(clientSend.getOutputStream());
			printwriter.write(params);
			printwriter.flush();      
			printwriter.close();
			
			if(cliReceive == 0){
				clientReceive = serverSocketReceive.accept();
				cliReceive = 1;
			}
			System.out.println("Client closed? " + clientReceive.isClosed());
			
			inputReader = new BufferedReader(new InputStreamReader(clientReceive.getInputStream()));

			if(inputReader != null ){
				System.out.println("inputReader: " + inputReader);
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
							t1 = msgtweet[i].split("=");
							t2 = t1[1].split(" - ");
							t = new Tweet(t2[0],t2[1]);
							tweetlist.add(0,t);
							i++;
						}	
					} else {
						t1 = msgtweet[i].split("=");
						t2 = t1[1].split(" - ");
						t = new Tweet(t2[0],t2[1]);
						tweetlist.add(0,t);
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
