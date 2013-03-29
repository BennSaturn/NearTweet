package NearTweat.neartweetserver;
 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
 
public class Main {
 
    private static ServerSocket serverSocket;
    private static Socket clientSocket;
    private static InputStreamReader inputStreamReader;
    private static BufferedReader bufferedReader;
    private static String message;
    private static String operation;
    private static String userName;
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
                operation = message.split(":").toString();
                System.out.println("Message: " + message + "Operation: " + operation);
                
            switch(operation){ 
            	case "LOGIN" :
            		userName = (String) message.subSequence(7, message.length());
            		listClients.put(userName, clientSocket.getLocalAddress()+"/"+port+"/0");
            		break;
            	case "TWEET" :
            		break;
            	case "REPLY" :
            		break;
            	case "RETWEET" :
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
