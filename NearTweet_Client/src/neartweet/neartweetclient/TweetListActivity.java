package neartweet.neartweetclient;

import java.util.List;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class TweetListActivity extends ListActivity {

	public final static String USERNAME = "nearTweet.neartweetclient.USERNAME";
	private String userName;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweetlist);	
		// Show the Up button in the action bar.
		setupActionBar();
		System.out.println(userName);
		/** vai buscar o extra que passas no intent **/
		Bundle extras = getIntent().getExtras();
		userName = extras.getString(USERNAME);
		//System.out.println("TweetListActivity: "+userName);
		new GetTweetsTask(this).execute("GETLIST:"+userName);

		// start the ServerListenerService
		//startService(new Intent(this, ServerListenerService.class));
	}

	public void tweet(View v) {
		Intent intent = new Intent(TweetListActivity.this, TweetActivity.class);
		intent.putExtra(USERNAME, userName);
		startActivity(intent);

	}

	public void refresh(View v) {
		new GetTweetsTask(this).execute("GETLIST:"+userName); 
	}

	public void setTweetList(List<Tweet> tweetList){
		if(tweetList != null){
			setListAdapter(new TweetAdapter(this, tweetList));
			//ListView listView = (ListView) findViewById(R.id.);
			//listView.setAdapte(new TweetItemAdapter(this, R.layout.listitem, tweets));
		}
		else {
			nearTweetAlert("Servidor em baixo!");
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
