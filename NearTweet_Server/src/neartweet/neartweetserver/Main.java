package neartweet.neartweetserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class Main {

	private static ServerSocket serverSocketReceive = null;
	private static ServerSocket serverSocketSend = null;
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
	private static String tweet;
	private static String poll;
	private static String sharing;
	private static String spam;
	private static Map<String, String> listClients = new HashMap<String, String>();
	private static Map<String, Integer> spamTweetList = new HashMap<String, Integer>();
	private static Map<String, String> userTweetList = new HashMap<String, String>();
	private static int spamValue;
	private static int banValue = 10;
	private static Integer port = null;

	public static void main(String[] args) {

		try {
			serverSocketReceive = new ServerSocket(4444);
			serverSocketSend = new ServerSocket(4445);
		} catch (IOException e) {
			System.out.println("Could not listen on port: 4444");
		}

		System.out.println("Server Receive started. Listening to the port 4444");
		System.out.println("Server Send started. Listening to the port 4445");
		userTweetList.put("Welcome to NearTweet,enjoy!","NearTweetStaff");
		while (true) {
			try {
				clientSocket = serverSocketReceive.accept();
				port = clientSocket.getPort();

				inputStreamReader =
						new InputStreamReader(clientSocket.getInputStream());
				bufferedReader =
						new BufferedReader(inputStreamReader);
				message = bufferedReader.readLine();
				operation = message.split(":");
			} catch (IOException ex) {
				System.out.println("Problem in message reading");
			}		
				switch(operation[0]){ 
				case "LOGIN" :
					login(port);
					break;
				case "TWEET" :
					System.out.println("Tweet tweet");
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
				case "MSHARING" :
					mdsharing();
					break;
				case "SDSHARING" :
					sdsharing();
					break;
				case "SPAM" :
					spam();		
					break;
				case "GETLIST" :
					getlist();		
					break;	
				}    

			try {
				inputStreamReader.close();
				//clientSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
	}

	public static void login(Integer port){
		userName = (String) message.subSequence(7, message.length());
		System.out.println(userName+port+clientSocket.getLocalAddress());
		listClients.put(clientSocket.getLocalAddress().toString(), userName);
		 /*+"/"+port+"/0" */
		
		try {
			clientSocket = serverSocketSend.accept();
			outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
			outputStreamWriter.write("OK!");
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void tweet(){
		String user = listClients.get(clientSocket.getLocalAddress().toString());
		System.out.println("user:" + user);
		tweet = (String) message.subSequence(7, message.length());
		System.out.println(tweet);
		// ver todos os portos/clientes existentes
		spamTweetList.put(tweet, 0);;
		userTweetList.put(tweet, user);
		try {
			clientSocket = serverSocketSend.accept();
			outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
			outputStreamWriter.write("OK!");
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	/*	for (String s :listClients.values()){
			String[] portClient = s.split("/");
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
			sendMsg(sendTweet, personTweet[1], endpoint);
		}	*/
	}

	public static void reply(){
		tweet = (String) message.subSequence(7, message.length());

		String[] personReply = tweet.split(":");
		spamTweetList.put(personReply[1], 0);;
		userTweetList.put(personReply[1], personReply[0]);
		if(listClients.get(personReply[0]) != null){
			String[] portReply = listClients.get(personReply[0]).split("/");
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portReply[2]));
			sendMsg(sendTweet, personReply[1], endpoint);	
		} else {
			for (String s :listClients.values()){
				String[] portClient = s.split("/");
				InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
				sendMsg(sendTweet, personReply[1], endpoint);
			}
		}
		System.out.println(tweet);
	}

	public static void retweet(){
		tweet = (String) message.subSequence(9, message.length());
		System.out.println(tweet);
		// ver todos os portos/clientes existentes

		for (String s :listClients.values()){
			String[] portClient = s.split("/");
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
			sendMsg(sendTweet, tweet, endpoint);
		}
	}

	public static void poll(){
		poll = (String) message.subSequence(5, message.length());
		System.out.println(poll);
		spamTweetList.put(poll, 0);
		userTweetList.put(poll, userName);
		for (String s :listClients.values()){
			String[] portClient = s.split("/");
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
			sendMsg(sendTweet, poll, endpoint);
		}
	}

	public static void mdsharing(){
		sharing = (String) message.subSequence(10, message.length());
		System.out.println(sharing);
		spamTweetList.put(sharing, 0);
		userTweetList.put(sharing, userName);
		for (String s :listClients.values()){
			String[] portClient = s.split("/");
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
			sendMsg(sendTweet, sharing, endpoint);
		}
	}

	public static void sdsharing(){
		sharing = (String) message.subSequence(11, message.length());
		System.out.println(sharing);
		spamTweetList.put(sharing, 0);
		userTweetList.put(sharing, userName);
		for (String s :listClients.values()){
			String[] portClient = s.split("/");
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
			sendMsg(sendTweet, sharing, endpoint);
		}
	}

	public static void spam(){
		spam = (String) message.subSequence(4, message.length());
		System.out.println(spam);
		spamValue = spamTweetList.get(spam);
		spamTweetList.remove(spam);
		spamValue += 1;

		if(spamValue == banValue){
			userTweetList.remove(spam);
			String bannedUser[] = listClients.get(userName).split("/");
			// informacao para o utilizador a banir
			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(bannedUser[2]));
			sendMsg(banUser, "BANNED!", endpoint);
			listClients.remove(userName);
			return;
		}
		spamTweetList.put(spam, spamValue);
		try {
			clientSocket = serverSocketSend.accept();
			outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
			outputStreamWriter.write("OK!");
			outputStreamWriter.flush();
			outputStreamWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void getlist(){
	//	for (String s :listClients.values()){
	//		String[] portClient = s.split("/");
	//		InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
	//		sendObject(sendTweet, userTweetList, endpoint);
	//	}  	
		try {
			clientSocket = serverSocketSend.accept();
			outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
			outputStreamWriter.write(userTweetList.toString());
			outputStreamWriter.flush();
			outputStreamWriter.close();
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

