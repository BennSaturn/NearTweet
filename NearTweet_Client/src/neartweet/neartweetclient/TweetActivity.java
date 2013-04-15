package neartweet.neartweetclient;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

public class TweetActivity extends Activity {

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweets);	
		// Show the Up button in the action bar.
		setupActionBar();

	}
	
	/** OnClick Photo button */
	public void photoPopUp(View v) {
		/** Instantiating PopupMenu class */
		PopupMenu popup = new PopupMenu(getBaseContext(), v);

		/** Adding menu items to the popumenu */
		popup.getMenuInflater().inflate(R.menu.popuppicmenu, popup.getMenu());

		/** Defining menu item click listener for the popup menu */
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), 
						Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		/** Showing the popup menu */
		popup.show();

	}
	
	/** OnClick Gps button */
	public void getGpsData(View v) {
		
	}
	
	/** OnClick Send Tweet button */
	public void sendtweet(View v) {
		EditText tweet = (EditText) findViewById(R.id.editText1);
		new TweetTask(this).execute("TWEET: " + tweet.getText().toString());		
		
	}
	
	public void setResult(String result){
		
		System.out.println("TweetActivity: "+ result);
		
		if(result.equals("OK!")){
			NavUtils.navigateUpFromSameTask(this);
		} else if(result.equals("ERRO!")){
			nearTweetAlert("Tweet nao enviado;");
		}
	}
	
	private void nearTweetAlert(String msg){
		new AlertDialog.Builder(this).setTitle("NearTweet Alert!").setMessage(msg)
		.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		}
		}).show();
	}
	
	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tlmenu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
