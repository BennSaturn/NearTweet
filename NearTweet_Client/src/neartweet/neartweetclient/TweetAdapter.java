package neartweet.neartweetclient;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TweetAdapter extends BaseAdapter {
	
	private List<Tweet> tweetList;
	private Context context;	

	public TweetAdapter(Context context, List<Tweet> tweets){
		super();
		this.tweetList=tweets;
		this.context=context;
	}
	
	@Override
	public int getCount() {
		return tweetList.size();
	}

	@Override
	public Object getItem(int position) {
		return tweetList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
        if ( v == null )
        {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.tweet_item, null);
        }

        Tweet tweet = (Tweet) getItem(position);
        
        if(tweet!=null){
        	TextView profileName = (TextView)v.findViewById(R.id.tweetUsername);
        	profileName.setText(tweet.getUsername());

        	TextView twitMessage = (TextView)v.findViewById(R.id.tweetMessage);
        	twitMessage.setText(tweet.getMessage());
        }
        return v;
	}

}
