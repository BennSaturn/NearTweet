package neartweet.neartweetclient;

public class Tweet {
	public String username;
	public String message;


	public Tweet(String username, String message) {
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
