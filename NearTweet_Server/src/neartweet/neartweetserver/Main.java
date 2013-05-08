package neartweet.neartweetserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Main {

	private static ServerSocket serverSocketReceive = null;
	private static Socket serverSocketSend = null;
	private static Socket clientSocket;
	private static Socket sendTweet;
	private static Socket banUser;
	private static InputStreamReader inputStreamReader;
	private static OutputStreamWriter outputStreamWriter;
	private static ObjectOutputStream objOutputStream;
	private static BufferedReader bufferedReader;
	private static String message;
	private static String[] operation;
	private static String userName;
	private static String[] tweet;
	private static String poll;
	private static String sharing;
	private static String spam;
	private static Map<String, Integer> listClients = new HashMap<String, Integer>();
	private static Map<String, Integer> spamTweetList = new HashMap<String, Integer>();
	private static Map<String, String> userTweetList = new TreeMap<String, String>();
	private static int spamValue;
	private static int banValue = 10;
	private static Long tweetTime;
	private static Map<String, String> oneTweetList = new TreeMap<String, String>();

	public static void main(String[] args) {

		try {
			serverSocketReceive = new ServerSocket(4444);
			//serverSocketSend = new ServerSocket(4445);
		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
		}

		System.out.println("Server Receive started. Listening to the port 4444");
		//System.out.println("Server Send started. Listening to the port 4445");
		Long startTime = System.currentTimeMillis();
		userTweetList.put(startTime.toString(),"NearTweetStaff - Welcome to NearTweet,enjoy!");
		while (true) {
			try {
				clientSocket = serverSocketReceive.accept();
				inputStreamReader =
						new InputStreamReader(clientSocket.getInputStream());
				bufferedReader =
						new BufferedReader(inputStreamReader);
				message = bufferedReader.readLine();
				operation = message.split(":");
				//System.out.println(operation.length);
				//System.out.println(operation[0]);
				//System.out.println(operation[1]);
				//System.out.println(operation[2]);

			} catch (IOException ex) {
				System.out.println("Problem in message reading");
			}		
			switch(operation[0]){ 
			case "LOGIN" :
				System.out.println("woohoo?!");
				login(operation[1], Integer.parseInt(operation[2]));
				break;
			case "TWEET" :
				tweet();
				break;
			case "REPLY" :
				reply();
				break;
			case "RETWEET" :
				retweet();
				break;
			case "POLL" :
				poll();
				break;
			case "SPAM" :
				spam(operation[1]);		
				break;
			case "GETLIST" :
				getlist(operation[1]);		
				break;	
			}    

			try {
				inputStreamReader.close();
				clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

	public static void login(String username, int port){
		//userName = (String) message.subSequence(7, message.length());
		System.out.println(username);
		System.out.println(port);


		//Falta avisar o utilizador...
		if(!listClients.containsKey(username)){
			listClients.put(username, port);
		}else if( spamTweetList.containsKey(username)){
			System.out.println("User blocked over spam!");
			return;
		}

		try {
			serverSocketSend = new Socket("127.0.0.1", port);
			outputStreamWriter = new OutputStreamWriter(serverSocketSend.getOutputStream());
			outputStreamWriter.write("OK!");
			outputStreamWriter.flush();
			outputStreamWriter.close();
			serverSocketSend.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void tweet(){
		System.out.println("Server: TWEET");
		tweet = operation[1].split(" - ");
		// ver todos os portos/clientes existentes
		tweetTime = System.currentTimeMillis();
		spamTweetList.put(tweet[0] + " - " + tweet[1], 0);
		userTweetList.put(tweetTime.toString(), tweet[0] + " - " + tweet[1]);
		System.out.println(tweet[0]+"-"+tweet[1]);

		Thread sendTweet = new Thread() {
			public void run() {
				Socket socketSend;
				OutputStreamWriter outputStream;
				for (int port : listClients.values()){
					System.out.println(port);
					if (port != listClients.get(tweet[0])) {
						try {
							socketSend = new Socket("127.0.0.1", port);
							outputStream = new OutputStreamWriter(socketSend.getOutputStream());
							oneTweetList.clear();
							oneTweetList.put(tweetTime.toString(), tweet[0] + " - " + tweet[1]);
							outputStream.write(oneTweetList.toString());
							outputStream.flush();
							outputStream.close();
							socketSend.close();
						} catch (UnknownHostException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}

		};
		sendTweet.start();

		try {
			serverSocketSend = new Socket("127.0.0.1", listClients.get(tweet[0]));
			outputStreamWriter = new OutputStreamWriter(serverSocketSend.getOutputStream());
			outputStreamWriter.write("OK!");
			outputStreamWriter.flush();
			outputStreamWriter.close();
			serverSocketSend.close();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void reply(){
		tweet = operation[1].split(" - ");

		String[] personReply = tweet[1].split(":");
		spamTweetList.put(personReply[1], 0);;
		userTweetList.put(personReply[1], personReply[0]);
		if(listClients.get(personReply[0]) != null){
			/*	String[] portReply = listClients.get(personReply[0]).split("/");
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portReply[2]));
			sendMsg(sendTweet, personReply[1], endpoint);	*/
		} else {
			/*			for (String s :listClients.values()){
				String[] portClient = s.split("/");
				InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
				sendMsg(sendTweet, personReply[1], endpoint);
			} */
		}
		System.out.println(tweet);
	}

	public static void retweet(){
		tweet = operation[1].split(" - ");
		System.out.println(tweet);
		// ver todos os portos/clientes existentes

		/*		for (String s :listClients.values()){
			String[] portClient = s.split("/");
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
			sendMsg(sendTweet, tweet, endpoint);
		} */
	}

	public static void poll(){
		poll = (String) message.subSequence(5, message.length());
		System.out.println(poll);
		spamTweetList.put(poll, 0);
		userTweetList.put(poll, userName);
		/*	for (String s :listClients.values()){
			String[] portClient = s.split("/");
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
			sendMsg(sendTweet, poll, endpoint);
		} */
	}

	public static void spam(String username){
		spam = (String) message.subSequence(4, message.length());
		System.out.println(spam);
		spamValue = spamTweetList.get(spam);
		spamTweetList.remove(spam);
		spamValue += 1;

		if(spamValue == banValue){
			userTweetList.remove(spam);
			/* String bannedUser[] = listClients.get(userName).split("/");
			// informacao para o utilizador a banir
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(bannedUser[2]));
			sendMsg(banUser, "BANNED!", endpoint);
			listClients.remove(userName); */
			return;
		}
		spamTweetList.put(spam, spamValue);
		int port = listClients.get(username);
		try {
			serverSocketSend = new Socket("127.0.0.1", port);
			outputStreamWriter = new OutputStreamWriter(serverSocketSend.getOutputStream());
			outputStreamWriter.write("OK!");
			outputStreamWriter.flush();
			outputStreamWriter.close();
			serverSocketSend.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void getlist(String username){
		//	for (String s :listClients.values()){
		//		String[] portClient = s.split("/");
		//		InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
		//		sendObject(sendTweet, userTweetList, endpoint);
		//	}  	

		int port = listClients.get(username);
		try {
			System.out.println("GETLIST: "+port);
			serverSocketSend = new Socket("127.0.0.1", port);
			System.out.println("Criou Socket!!");
			outputStreamWriter = new OutputStreamWriter(serverSocketSend.getOutputStream());
			outputStreamWriter.write(userTweetList.toString());
			outputStreamWriter.flush();
			outputStreamWriter.close();
			serverSocketSend.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	public static void sendMsg(Socket soc, String msg, InetSocketAddress endpoint){
		try {
			sendTweet.connect(endpoint);
			outputStreamWriter = new OutputStreamWriter(soc.getOutputStream());
			outputStreamWriter.write(msg);
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void sendObject(Socket soc, Object o, InetSocketAddress endpoint){
		try {
			sendTweet.connect(endpoint);
			objOutputStream = new ObjectOutputStream(soc.getOutputStream());
			objOutputStream.writeObject(o);
			objOutputStream.flush();
			objOutputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

