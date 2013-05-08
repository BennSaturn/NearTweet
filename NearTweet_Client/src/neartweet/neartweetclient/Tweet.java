package neartweet.neartweetclient;

import android.os.Parcel;
import android.os.Parcelable;

public class Tweet implements Parcelable{
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

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(message);
		dest.writeString(username);
	}
	
	public static final Parcelable.Creator<Tweet> CREATOR =
	    	new Parcelable.Creator<Tweet>() {
	            public Tweet createFromParcel(Parcel in) {
	                return new Tweet(in);
	            }
	 
	            public Tweet[] newArray(int size) {
	                return new Tweet[size];
	            }
	        };
	        
	        private Tweet(Parcel in) {
	            message = in.readString();
	            username = in.readString();
	        }        
	        
}
