package NearTweat.neartweetserver;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
 
public class Main {
 
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static Socket sendTweet;
    private static InputStreamReader inputStreamReader;
    private static OutputStreamWriter outputStreamWriter;
    private static BufferedReader bufferedReader;
    private static String message;
    private static String[] operation;
    private static String userName;
    private static String tweet;
    private static Map<String, String> listClients = new HashMap<String, String>();
    
    public static void main(String[] args) {
 
        try {
            serverSocket = new ServerSocket(4444);
            
        } catch (IOException e) {
            System.out.println("Could not listen on port: 4444");
        }
        
        System.out.println("hello!");
        System.out.println("Server started. Listening to the port 4444");
 
        while (true) {
            try {
                clientSocket = serverSocket.accept();
                
                Integer port = clientSocket.getPort();
               
                inputStreamReader =
                   new InputStreamReader(clientSocket.getInputStream());
                bufferedReader =
                   new BufferedReader(inputStreamReader);
                message = bufferedReader.readLine();
                // verificar a operacao seleccionada
                operation = message.split(":");
                
            switch(operation[0]){ 
            	case "LOGIN" :
            		userName = (String) message.subSequence(7, message.length());
            		System.out.println(userName);
            		listClients.put(userName, clientSocket.getLocalAddress()+"/"+port+"/0");
            		outputStreamWriter = new OutputStreamWriter(clientSocket.getOutputStream());
            		outputStreamWriter.write("OK!");
            		outputStreamWriter.flush();
            		outputStreamWriter.close();
            		break;
            	case "TWEET" :
            		tweet = (String) message.subSequence(7, message.length());
            		System.out.println(tweet);
            		// ver todos os portos/clientes existentes
            		
            		for (String s :listClients.values()){
            			String[] portClient = s.split("/");
               			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
               			sendTweet.connect(endpoint);
               			outputStreamWriter = new OutputStreamWriter(sendTweet.getOutputStream());
               			outputStreamWriter.write(tweet);
               			outputStreamWriter.flush();
               			outputStreamWriter.close();
            		}
            		break;
            	case "REPLY" :
            		tweet = (String) message.subSequence(7, message.length());
            		
            		String[] person = tweet.split(":");
            		
            		if(listClients.get(person[0]) != null){
            			String[] portReply = listClients.get(person[0]).split("/");
            			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portReply[2]));
               			sendTweet.connect(endpoint);
               			outputStreamWriter = new OutputStreamWriter(sendTweet.getOutputStream());
               			outputStreamWriter.write(person[1]);
               			outputStreamWriter.flush();
               			outputStreamWriter.close();
            		} else {
                		for (String s :listClients.values()){
                			String[] portClient = s.split("/");
                   			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
                   			sendTweet.connect(endpoint);
                   			outputStreamWriter = new OutputStreamWriter(sendTweet.getOutputStream());
                   			outputStreamWriter.write(person[1]);
                   			outputStreamWriter.flush();
                   			outputStreamWriter.close();
                		}
            		}
            		System.out.println(tweet);
            		// ver todos os portos/clientes existentes
            		break;
            	case "RETWEET" :
            		tweet = (String) message.subSequence(9, message.length());
            		System.out.println(tweet);
            		// ver todos os portos/clientes existentes
            		
            		for (String s :listClients.values()){
            			String[] portClient = s.split("/");
               			InetSocketAddress endpoint = new InetSocketAddress(Integer.parseInt(portClient[2]));
               			sendTweet.connect(endpoint);
               			outputStreamWriter = new OutputStreamWriter(sendTweet.getOutputStream());
               			outputStreamWriter.write(tweet);
               			outputStreamWriter.flush();
               			outputStreamWriter.close();
            		}
            		break;
            	case "POLL" :
            		break;
            	case "MSHARING" :
            		break;
            	case "SDSHARING" :
            		break;
            	case "SPAM" :
            		break;
            }    
                inputStreamReader.close();
                clientSocket.close();
 
            } catch (IOException ex) {
                System.out.println("Problem in message reading");
            }
        }
    }
}

