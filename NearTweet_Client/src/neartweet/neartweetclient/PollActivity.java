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
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class PollActivity extends Activity {

	public final static String USERNAME = "nearTweet.neartweetclient.USERNAME";
	private String userName;
	private String pgt;
	private String op1;
	private String op2;
	private String op3;
	private String res1;
	private String res2;
	private String res3;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pollinit_activity);	
		// Show the Up button in the action bar.
		setupActionBar();

		/** vai buscar o extra que passas no intent **/
		Bundle extras = getIntent().getExtras();
		userName = extras.getString(USERNAME);
		System.out.println("PollActivity: "+userName);
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
	
	public void getQuestion(View v){
		EditText texto =(EditText)findViewById(R.id.editText1);
		pgt = texto.getText().toString();
	}	

	public void getOption1(View v){
		EditText texto =(EditText)findViewById(R.id.editText2);
		op1 = texto.getText().toString();
	}

	public void getOption2(View v){
		EditText texto =(EditText)findViewById(R.id.editText3);
		op2 = texto.getText().toString();
	}

	public void getOption3(View v){
		EditText texto =(EditText)findViewById(R.id.editText4);
		op3 = texto.getText().toString();
	}

	public void pollView(View v) {
		//abrir a poll selecionada na lista de polls
		//abrir a pollView_activity
	}

	public void finalizePoll(View v){
		//vai xamar e preencher o pollres_activity
		setContentView(R.layout.pollres_activity);
		if(pgt == null){
			//ERRROOO nao e possivel criar
			Toast.makeText(getBaseContext(), "Nao pode criar uma poll sem a pergunta", Toast.LENGTH_LONG);
		}else{

			TextView tv = (TextView)findViewById(R.id.textView1);
			tv.setText(pgt);

			if(op1 != null && op2!=null && op3!= null){
				RadioButton rb1 = (RadioButton) findViewById(R.id.option1);
				rb1.setText(op1);

				RadioButton rb2 = (RadioButton) findViewById(R.id.option2);
				rb2.setText(op2);

				RadioButton rb3 = (RadioButton) findViewById(R.id.option3);
				rb3.setText(op3);
			}else{
				//ERRROOO need at least 3 options
				Toast.makeText(getBaseContext(), "Precisa de pelo menos tres opcoes!", Toast.LENGTH_LONG);
			}

		}
	}

	public void viewContentPoll(View v){
		//vai xamar e preencher o pollView_activity
		setContentView(R.layout.pollview_activity);

		if(pgt == null || op1 == null || res1 == null || op2 == null || res2 == null || op3 == null || res3 == null)
			return;

		TextView tv = (TextView)findViewById(R.id.textView1);
		tv.setText(pgt);

		RadioButton rb1 = (RadioButton) findViewById(R.id.option1);
		rb1.setText(op1);

		RadioButton rb2 = (RadioButton) findViewById(R.id.option2);
		rb2.setText(op2);

		RadioButton rb3 = (RadioButton) findViewById(R.id.option3);
		rb3.setText(op3);

		TextView tres1 = (TextView)findViewById(R.id.result1);
		tres1.setText(res1);

		TextView tres2 = (TextView)findViewById(R.id.result2);
		tres2.setText(res2);

		TextView tres3 = (TextView)findViewById(R.id.result3);
		tres3.setText(res3);

	}

	public void onRadioButtonClicked(View view) {
		// Is the button now checked?
		boolean checked = ((RadioButton) view).isChecked();

		// Check which radio button was clicked
		switch(view.getId()) {
		case R.id.option1:
			if (checked)
				// somar 1 ao contador da primeira opcao
				viewContentPoll(view);
				break;
		case R.id.option2:
			if (checked)
				// somar 1 ao contador da segunda opcao
				viewContentPoll(view);
				break;
		case R.id.option3:
			if (checked)
				// somar 1 ao contador da terceira opcao
				viewContentPoll(view);
				break;
		}
	}

	public void setResult(String result){

		System.out.println("PollActivity: "+ result);

		if(result.equals("OK!")){
			Intent intent = new Intent(this, TweetListActivity.class);
			intent.putExtra(USERNAME, userName);
			startActivity(intent);

		} else if(result.equals("ERRO!")){

		}
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
		case R.id.createpoll:
			Intent intentCP = new Intent(this, PollActivity.class);
			startActivity(intentCP);
			return true;
		case R.id.viewpoll:
			Intent intentVP = new Intent(this, PollActivity.class);
			startActivity(intentVP);
			return true;
		case R.id.logout:
			Intent intentLO = new Intent(this, MainActivity.class);
			startActivity(intentLO);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
