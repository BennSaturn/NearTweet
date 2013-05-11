package neartweet.neartweetclient;

import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class TweetSelectedActivity extends ListActivity{
	
	public final static String CONVERSATION = "nearTweet.neartweetclient.CONVERSATION";
	public final static String USERNAME = "nearTweet.neartweetclient.USERNAME";
	private String tweet;
	private String userName;
	//alterar para ver qual o tweet completo e as respostas a este...
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweetlistselected);	
		// Show the Up button in the action bar.
		setupActionBar();
		/** vai buscar o extra que passas no intent **/
		Bundle extras = getIntent().getExtras();
		userName = extras.getString(USERNAME);
		tweet = extras.getString(CONVERSATION);
		new GetConversationTask(this).execute("GETCONVERSATION:"+ userName +":"+ tweet); 

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

	public void setTweetList(List<Tweet> tweetList){
		if(tweetList != null){
			setListAdapter(new TweetAdapter(this, tweetList));
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if((keyCode == KeyEvent.KEYCODE_BACK)){
			Intent intent = new Intent(this, TweetListActivity.class);
			intent.putExtra(USERNAME, userName);
			startActivity(intent);
		}
		return super.onKeyDown(keyCode, event);
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
