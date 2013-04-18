package neartweet.neartweetclient;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	public final static String USERNAME = "nearTweet.neartweetclient.USERNAME";
	private EditText enter_usernameTx;
	private static String message;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		enter_usernameTx = (EditText) findViewById(R.id.enter_usernameTx);

	}

	public void loginResponse(View view){
		enter_usernameTx = (EditText) findViewById(R.id.enter_usernameTx);
		if (enter_usernameTx.getText().length() == 0) {
			Toast.makeText(MainActivity.this, "Please enter a valid username",
					Toast.LENGTH_LONG).show();
			return;
		}
		message = enter_usernameTx.getText().toString();
		enter_usernameTx.setText("");
		new LoginResponseTask(this).execute("LOGIN: "+ message);
		//new ClientConnectorTask().execute("LOGIN:"+ message);
	}

	public void setResult(String result){
		
		System.out.println("MainActivity: "+ result);
		
		if(result.equals("OK!")){
			Intent intent = new Intent(this, TweetListActivity.class);
			intent.putExtra(USERNAME, message);
			startActivity(intent);
		} else if(result.equals("EXISTE!")){
			nearTweetAlert("Utilizador ja existe!");
		} else if(result.equals("ERRO!")){
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static String getUsername(){
		return message;
		
	}
}
