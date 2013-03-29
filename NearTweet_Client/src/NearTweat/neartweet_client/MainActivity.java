package NearTweat.neartweet_client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText enter_usernameTx;
    private Button loginBt;
    private String message;
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            enter_usernameTx = (EditText) findViewById(R.id.enter_usernameTx);
            loginBt = (Button) findViewById(R.id.loginBt);
            
            //Button press event listener
            loginBt.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                    	message = enter_usernameTx.getText().toString();
                    	enter_usernameTx.setText("");
                    	new ClientConnectorTask().execute("LOGIN: "+message);
                    	waitLogin();
                    }
            });
    }
    
    public void waitLogin(){
    	setContentView(R.layout.wait);
    	
    	
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
