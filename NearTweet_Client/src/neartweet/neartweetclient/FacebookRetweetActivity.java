package neartweet.neartweetclient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.facebook.LoggingBehavior;
import com.facebook.FacebookRequestError;
//import com.facebook.LoginActivity;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.Session.OpenRequest;
import com.facebook.SessionState;
import com.facebook.Settings;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

public class FacebookRetweetActivity extends Activity {

	private static final String APP_ID = "160929487409513";
	private static final List<String> PERMISSIONS = new ArrayList<String>(); 
	private Session.StatusCallback statusCallback = new SessionStatusCallback();

	private static final String TOKEN = "access_token";
	private static final String EXPIRES = "expires_in";
	private static final String KEY = "facebook-credentials";

	private Facebook facebook;
	private String messageToPost;

	public boolean saveCredentials(Facebook facebook) {
		Editor editor = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
		editor.putString(TOKEN, facebook.getAccessToken());
		editor.putLong(EXPIRES, facebook.getAccessExpires());
		return editor.commit();
	}

	public boolean restoreCredentials(Facebook facebook) {
		SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(KEY, Context.MODE_PRIVATE);
		facebook.setAccessToken(sharedPreferences.getString(TOKEN, null));
		facebook.setAccessExpires(sharedPreferences.getLong(EXPIRES, 0));
		return facebook.isSessionValid();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.facebook_dialog);
		facebook = new Facebook(APP_ID);
		restoreCredentials(facebook);

		String facebookMessage = getIntent().getStringExtra("facebookMessage");
		if (facebookMessage == null){
			facebookMessage = "Test wall post";
		}
		messageToPost = facebookMessage;
	}

	public void doNotShare(View button){
		finish();
	}
	public void share(View button){
		if (!facebook.isSessionValid()) {
			loginAndPostToWall();
		}
		else {
			postToWall(messageToPost);
		}
	}

	public void loginAndPostToWall(){
		facebook = new Facebook(APP_ID);

		Session session = new Session(this);
		Session.setActiveSession(session);
		SessionState state = session.getState();

		Settings.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

		if (!session.isOpened() && !session.isClosed() && session.getState() != SessionState.OPENING) {
			Log.d(ACTIVITY_SERVICE,"open for publish");
			OpenRequest open = new OpenRequest(this).setCallback(statusCallback);
			PERMISSIONS.add("publish_stream");
			PERMISSIONS.add("publish_actions");
			open.setPermissions(PERMISSIONS);
			session.openForPublish(open);
		} else {
			Log.d(ACTIVITY_SERVICE,"open?");
			// start Facebook Login
			Session.openActiveSession(this, true, new Session.StatusCallback() {
				// callback when session changes state
				@Override
				public void call(Session session, SessionState state, Exception exception) {
					if (session.isOpened()) {

						// make request to the /me API
						Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

							// callback after Graph API response with user object
							@Override
							public void onCompleted(GraphUser arg0, Response arg1) {
								// TODO Auto-generated method stub
								Log.d(ACTIVITY_SERVICE,getApplicationContext() + "Welcome " + arg0.getFirstName() +" "+ arg1.toString());
							}
						});
					}
				}
			});
		}
		postToWall(messageToPost);
	}  

	public void postToWall(String message){
		final Bundle parameters = new Bundle();
		parameters.putString("message", message);
		parameters.putString("description", "topic share"); 
		Thread t = new Thread(new Runnable() {
			public void run() {
				try {
					String response = facebook.request("me/feed", parameters, "POST");
					Log.d("Tests", "got response: " + response);
					if (response == null || response.equals("") ||
							response.equals("false")) {
						showToast("Blank response."); 
					}
					else {
						showToast("Message posted to your facebook wall!");
					} 
					finish();
				} catch (Exception e) {
					showToast("Failed to post to wall!");
					e.printStackTrace();
					finish();
				}
			}
		});
		t.start();
	}

	class LoginDialogListener implements DialogListener {
		public void onComplete(Bundle values) {
			saveCredentials(facebook);
			if (messageToPost != null){
				postToWall(messageToPost);
			}
		}
		@Override
		public void onError(DialogError arg0) {
			showToast("Authentication with Facebook failed!");
			finish();

		}
		@Override
		public void onFacebookError(FacebookError arg0) {
			showToast("Authentication with Facebook failed!");
			finish();

		}
		@Override
		public void onCancel() {
			showToast("Authentication with Facebook cancelled!");
			finish();		
		}
	}

	private void showToast(String message){
		Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
	}

	private class SessionStatusCallback implements Session.StatusCallback {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			//updateView();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	}
}


