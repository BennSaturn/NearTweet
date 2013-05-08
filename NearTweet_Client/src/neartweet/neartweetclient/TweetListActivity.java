package neartweet.neartweetclient;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

@SuppressLint("HandlerLeak")
public class TweetListActivity extends ListActivity {

	public final static String USERNAME = "nearTweet.neartweetclient.USERNAME";
	private String userName;
	static TweetAdapter array; 
	static List<Tweet> tweetList = new ArrayList<Tweet>();
	private static Handler tweetHandler;
	private Intent intent;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweetlist);	
		// Show the Up button in the action bar.
		setupActionBar();
		/** vai buscar o extra que passas no intent **/
		Bundle extras = getIntent().getExtras();
		userName = extras.getString(USERNAME);
		//System.out.println("TweetListActivity: "+userName);

		/** Thread que trata das respostas do servidor **/
		tweetHandler=new Handler() {
			@Override
			public void handleMessage(Message msg) {
				System.out.println("Recebeu lista!!");
				List<Tweet> list = msg.getData().getParcelableArrayList("listtweet");
				//System.out.println("list.size: "+list.size()+"tweetlist.isEmpty(): "+(!tweetList.isEmpty()));
				if((!tweetList.isEmpty()) && (list.size()==1)) {
					Tweet tweet = list.get(0);
					if(!tweet.getUsername().equals("NearTweetStaff")) {
						//System.out.println(tweet.getUsername()+"-"+tweet.getMessage());
						tweetList.add(0, tweet);
						int i=0;
						while(tweetList.size()>i){
							System.out.println(tweetList.get(i).username);
							i++;
						}
						array.notifyDataSetChanged();
						ListView listView = (ListView) findViewById(android.R.id.list);
						listView.setLongClickable(true);
						listView.setOnItemLongClickListener(longMessageClickedHandler);
						listView.setOnItemClickListener(quickMessageClickedHandler);
					}
				} else {
					tweetList.clear();
					while(!list.isEmpty()){
						tweetList.add(list.remove(0));
					}
					array = new TweetAdapter(TweetListActivity.this, tweetList);
					setListAdapter(array);
					ListView listView = (ListView) findViewById(android.R.id.list);
					listView.setLongClickable(true);
					listView.setOnItemLongClickListener(longMessageClickedHandler);
					listView.setOnItemClickListener(quickMessageClickedHandler);
				}
			}
		};

		// start the ServerListenerService
		intent = new Intent(this, ServerListenerService.class);  
		intent.putExtra(ServerListenerService.EXTRA_MESSENGER, new Messenger(tweetHandler));  
		startService(intent);
		array = new TweetAdapter(TweetListActivity.this, tweetList);
		setListAdapter(array);
		// pede a lista pela primeira vez
		new GetTweetsTask(this).execute("GETLIST:"+userName);
	}

	public void tweet(View v) {
		stopService(intent);
		Intent intent = new Intent(TweetListActivity.this, TweetActivity.class);
		intent.putExtra(USERNAME, userName);
		startActivity(intent);

	}

	public void refresh(View v) {
		array = new TweetAdapter(TweetListActivity.this, tweetList);
		setListAdapter(array);
		new GetTweetsTask(this).execute("GETLIST:"+userName); 
	}

	private OnItemClickListener quickMessageClickedHandler = new OnItemClickListener() {
		public void onItemClick(AdapterView parent, View v, int position, long id) {
			Intent intent = new Intent(TweetListActivity.this, TweetSelectedActivity.class);
			intent.putExtra(USERNAME, userName);
			startActivity(intent);
		}
	};

	private OnItemLongClickListener longMessageClickedHandler = new OnItemLongClickListener() {
		public boolean onItemLongClick(final AdapterView<?> parent, View v,
				final int position, long id) {

			PopupMenu popup = new PopupMenu(getBaseContext(), v);

			/** Adding menu items to the popumenu */
			popup.getMenuInflater().inflate(R.menu.popuptweetmenu, popup.getMenu());

			/** Defining menu item click listener for the popup menu */
			popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

				@Override
				public boolean onMenuItemClick(MenuItem item) {
					if(item.getTitle().equals("Reply")){

						Intent intent = new Intent(TweetListActivity.this, TweetActivity.class);
						intent.putExtra(USERNAME, userName);
						//condicao para decidir se e pessoal ou publico
						startActivity(intent);

					} else if (item.getTitle().equals("ReTweet")){
						Intent intent = new Intent(TweetListActivity.this, FacebookRetweetActivity.class);
						startActivity(intent);
						//falta agarrar ao facebook para mandar o tweet

					} else if (item.getTitle().equals("SPAM")){
						Object o = parent.getAdapter().getItem(position);		
						String status = o.toString();
						stopService(intent);
						new SpamTask(TweetListActivity.this).execute("SPAM:" + userName + " - " + status);
						//	marcar o tweet como spam para o servidor...

					}
					return true;
				}

			});


			/** Showing the popup menu */
			popup.show();
			return true;
		}
	};


	public void setTweetList(List<Tweet> tweetList){
		if(tweetList != null){
			setListAdapter(new TweetAdapter(this, tweetList));

			ListView listView = (ListView) findViewById(android.R.id.list);
			listView.setLongClickable(true);
			listView.setOnItemLongClickListener(longMessageClickedHandler);
			listView.setOnItemClickListener(quickMessageClickedHandler);



			//listView.setAdapter(new TweetItemAdapter(this, R.layout.listitem, tweets));
		}
		else {
			nearTweetAlert("Servidor em baixo!");
		}
	}
	
	public void setSpamResult(String result){

		System.out.println("SpamActivity: "+ result);

		if(result.equals("OK!")){
			Toast.makeText(getBaseContext(), "Tweet Notified as Spam", Toast.LENGTH_SHORT).show();
			//NavUtils.navigateUpFromSameTask(this);
			startService(intent);
		} else if(result.equals("ERRO!")){
			nearTweetAlert("Spam nao registado");
			startService(intent);
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
