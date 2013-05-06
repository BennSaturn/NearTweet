package neartweet.neartweetclient;

public class Spam {
	
	public String username;
	public String message;


	public Spam(String username, String message) {
		this.username = username;
		this.message = message;
	}
	
	public String getUsername(){
		return this.username;
	}
	
	public String getMessage(){
		return this.message;
	}
	
}
