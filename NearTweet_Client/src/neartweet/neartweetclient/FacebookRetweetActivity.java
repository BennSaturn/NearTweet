package neartweet.neartweetclient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;

public class FacebookRetweetActivity extends FragmentActivity {

	private static final String APP_ID = "449619755128925";
	public final static String RETWEET = "nearTweet.neartweetclient.RETWEET";
	public final static String USERNAME = "nearTweet.neartweetclient.USERNAME";
	private static final List<String> PERMISSIONS = new ArrayList<String>(); 
	private SharedPreferences mPrefs;
	private Facebook facebook;
	private AsyncFacebookRunner mAsyncRunner;
	private String messageToPost;
	private FacebookConnector facebookConnector;
	private final Handler mFacebookHandler = new Handler();
	private static final String TAG = "NearTweetGrupo1";
	private static final String MSG = "Message from NearTweetGrupo1";
	private static String user;

	final Runnable mUpdateFacebookNotification = new Runnable() {
		public void run() {
			Toast.makeText(getBaseContext(), "Facebook updated !", Toast.LENGTH_LONG).show();
			finish();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_dialog);
		this.facebookConnector = new FacebookConnector(APP_ID, this, getApplicationContext(), new String[] {"publish_stream", "publish_actions"});
		String facebookMessage = getIntent().getStringExtra(RETWEET);
		String[] userMessage = facebookMessage.split("/");
		user = userMessage[0];
		
		if (userMessage[1] == null){
			userMessage[1] = "Test wall post";
		}
		messageToPost = userMessage[1];
	}

	public void doNotShare(View button){
		finish();
	}
	public void share(View button){
		postMessage();
	}

	private String getFacebookMsg() {
		return MSG + " at " + new Date().toLocaleString();
	}	
	
	public void postMessage() {
		
		if (facebookConnector.getFacebook().isSessionValid()) {
			postMessageInThread();
		} else {
			SessionEvents.AuthListener listener = new SessionEvents.AuthListener() {
				
				@Override
				public void onAuthSucceed() {
					postMessageInThread();
				}
				
				@Override
				public void onAuthFail(String error) {
					
				}
			};
			SessionEvents.addAuthListener(listener);
			facebookConnector.login();
		}
	}

	private void postMessageInThread() {
		Thread t = new Thread() {
			public void run() {
		    	
		    	try {
		    		facebookConnector.postMessageOnWall(messageToPost);
					mFacebookHandler.post(mUpdateFacebookNotification);
				} catch (Exception ex) {
					Log.e(TAG, "Error sending msg",ex);
				}
		    }
		};
		t.start();
	}

	private void showToast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		this.facebookConnector.getFacebook().authorizeCallback(requestCode, resultCode, data);
	}
}


