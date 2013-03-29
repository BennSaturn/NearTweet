package NearTweat.neartweet_client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
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
    private String username;
    
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
                    	waitLogin(message);
                    }
            });
    }
    
    public void waitLogin(String message){
    	AsyncTask<String,Void,Integer> msg;
    	setContentView(R.layout.wait);
    	System.out.println("entrou");
    	msg = new ClientConnectorTask().execute();
    	
    	if(msg.equals("OK!")){
    		System.out.println("entrou");
    		menuView(message);
    	} else if(msg.equals("EXISTE!")){
    		// pop-up como o erro
    	} else if(msg.equals("ERRO")){
    		// pop-up como o erro
    	}
  
    }
    
    void menuView(String user){
    	username = user;
    	setContentView(R.layout.menu);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
