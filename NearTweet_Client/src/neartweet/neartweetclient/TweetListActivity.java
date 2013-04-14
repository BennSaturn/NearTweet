package neartweet.neartweetclient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class TweetListActivity extends ListActivity {

	@SuppressLint("NewApi")
	private Button TweetBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweetlist);	
		// Show the Up button in the action bar.
		new GetTweetsTask(this).execute("GETLIST:");
		setupActionBar();
		
		 TweetBtn = (Button) findViewById(R.id.tweet);
         TweetBtn.setOnClickListener(new View.OnClickListener() {

             public void onClick(View v) {
     			Intent intent = new Intent(TweetListActivity.this, TweetActivity.class);
    			startActivity(intent);
             }
         });
     }
	
	public void setTweetList(Map<String, String> tweetList){
		if(tweetList != null){
			setListAdapter(new TweetAdapter(this, getTweets(tweetList)));
			//ListView listView = (ListView) findViewById(R.id.);
			//listView.setAdapter(new TweetItemAdapter(this, R.layout.listitem, tweets));
		}
	}
	
	public static List<Tweet> getTweets(Map<String,String> list){
		List<Tweet> tweetList = new ArrayList<Tweet>();
		for(Entry<String, String> entry : list.entrySet()){
			Tweet tweet = new Tweet(entry.getKey(), entry.getValue());
			tweetList.add(tweet);
		}
		return tweetList;
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
