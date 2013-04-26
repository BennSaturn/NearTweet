package neartweet.neartweetclient;

import java.io.FileNotFoundException;
import java.io.InputStream;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.Location;  
import android.net.Uri;
import android.os.Bundle;  
import android.os.Build;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

public class TweetActivity extends Activity {

	private static final int SELECT_PHOTO = 100;
	private static final int PHOTO_TAKEN = 0;
	private static String url = "";
	private String userName;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweets);	
		// Show the Up button in the action bar.
		setupActionBar();
		
		/** vai buscar o extra que passas no intent **/
		Bundle extras = getIntent().getExtras();
		userName = extras.getString("USERNAME");
		System.out.println("TweetListActivity: "+userName);
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
				//Toast.makeText(getBaseContext(), "You selected the action : " + item.getTitle(), 
				//  		Toast.LENGTH_SHORT).show();
				if(item.getTitle().equals("From Clipboard")){
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					startActivityForResult(photoPickerIntent, SELECT_PHOTO);
				} else if (item.getTitle().equals("From URL")){
					url = "";
					photoURL();
				} else if (item.getTitle().equals("From other application")){
					Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
					intent.setType("image/*");
					startActivityForResult(intent, PHOTO_TAKEN);
				}
				return true;
			}
		});

		/** Showing the popup menu */
		popup.show();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 

		switch(requestCode) { 
		case SELECT_PHOTO:
			if(resultCode == RESULT_OK){  
				Uri selectedImage = imageReturnedIntent.getData();
				InputStream imageStream;
				Bitmap yourSelectedImage;
				try {
					imageStream = getContentResolver().openInputStream(selectedImage);
					yourSelectedImage = BitmapFactory.decodeStream(imageStream);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	            
			}
		case PHOTO_TAKEN:
			if(resultCode == RESULT_OK){  
				Uri selectedImage = imageReturnedIntent.getData();
				InputStream imageStream;
				Bitmap yourSelectedImage;
				try {
					imageStream = getContentResolver().openInputStream(selectedImage);
					yourSelectedImage = BitmapFactory.decodeStream(imageStream);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	            
			}    	
		}
	}

	/** OnClick Gps button */
	public void getGpsData(View v) {
		LocationManager mlocManager=null;  
		LocationListener mlocListener; 
		ProgressDialog alert = new ProgressDialog(v.getContext());
		mlocManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);  
		mlocListener = new MyLocationListener();  
		mlocManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, mlocListener);  

		if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {  
			if(MyLocationListener.latitude>0){  
				Toast.makeText(getBaseContext(), "Latitude: " + MyLocationListener.latitude + '\n' + 
						"Longitude: " + MyLocationListener.longitude + '\n', Toast.LENGTH_SHORT).show(); 
			} else {  
				alert.setTitle("Wait");  
				alert.setMessage("GPS in progress, please wait.");    
				alert.show();  
			}  
		} else {  
			Toast.makeText(getBaseContext(), "GPS is not turned on...", Toast.LENGTH_SHORT).show(); 
		}  


	}

	/** OnClick Send Tweet button */
	public void sendtweet(View v) {
		EditText tweet = (EditText) findViewById(R.id.editText1);
		new TweetTask(this).execute("TWEET:" + userName + " - " + tweet.getText().toString());		

	}

	public void setResult(String result){

		System.out.println("TweetActivity: "+ result);

		if(result.equals("OK!")){
			NavUtils.navigateUpFromSameTask(this);
		} else if(result.equals("ERRO!")){
			nearTweetAlert("Tweet nao enviado;");
		}
	}
	
	public void setResultPhoto(String result){

		System.out.println("TweetActivity - Photo: "+ result);

		if(result.equals("OK!")){
			nearTweetAlert("Photo obtained");
		} else if(result.equals("ERRO!")){
			nearTweetAlert("Couldn't retrieve the photo.");
		}
	}

	private void nearTweetAlert(String msg){
		new AlertDialog.Builder(this).setTitle("NearTweet Alert!").setMessage(msg)
		.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		}).show();
	}

	private void photoURL(){
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this).setTitle("Type the URL!");
		final EditText input = new EditText(this);
		alertDialogBuilder.setView(input);

		// set dialog message
		alertDialogBuilder
		.setCancelable(false)
		.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				// get user input and set it to result
				// edit text
				//url.concat(input.getText().toString());
				url = input.getText().toString();
				new GetImageTask(TweetActivity.this).execute(url);
			}
		})
		.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {
				dialog.cancel();
			}
		});
		
		alertDialogBuilder.show();
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

	public static class MyLocationListener implements LocationListener {  

		public static double latitude;  
		public static double longitude;  

		@Override  
		public void onLocationChanged(Location loc)  
		{  
			loc.getLatitude();  
			loc.getLongitude();  
			latitude=loc.getLatitude();  
			longitude=loc.getLongitude();  
		}  

		@Override  
		public void onProviderDisabled(String provider)  
		{  
			//print "Currently GPS is Disabled";  
		}  
		@Override  
		public void onProviderEnabled(String provider)  
		{  
			//print "GPS got Enabled";  
		}  
		@Override  
		public void onStatusChanged(String provider, int status, Bundle extras)  
		{  
		}  
	}
}
