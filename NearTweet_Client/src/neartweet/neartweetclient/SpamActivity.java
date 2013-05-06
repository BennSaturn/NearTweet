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
import android.widget.EditText;
import android.widget.Toast;

public class SpamActivity extends Activity{

	public final static String USERNAME = "nearTweet.neartweetclient.USERNAME";
	private String userName;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.id.spam);	
		// Show the Up button in the action bar.
		setupActionBar();

		/** vai buscar o extra que passas no intent **/
		Bundle extras = getIntent().getExtras();
		userName = extras.getString(USERNAME);
		System.out.println("SpamActivity: "+userName);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

		//n sei o que fazer...
	}

	
	public void spamButtonAction(View v) {
		
		Toast.makeText(getBaseContext(), "Tweet Notified as Spam", Toast.LENGTH_SHORT).show();
	}
	
	/** OnClick Spam button */
	public void spamTweet(View v) {
		EditText tweet = (EditText) findViewById(R.id.editText1);
		new SpamTask(this).execute("SPAM:" + userName + " - " + tweet.getText().toString());		
	}

	public void setResult(String result){

		System.out.println("SpamActivity: "+ result);

		if(result.equals("OK!")){
			Intent intent = new Intent(this, SpamActivity.class);
			intent.putExtra(USERNAME, userName);
			startActivity(intent);
			//NavUtils.navigateUpFromSameTask(this);
		} else if(result.equals("ERRO!")){
			nearTweetAlert("Spam nao registado;");
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
		getMenuInflater().inflate(R.menu.popuptweetmenu, menu);
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