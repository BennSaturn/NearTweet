package NearTweat.neartweet_client;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {

	private EditText textField;
    private Button button;
    private String message;
    @Override
    public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            textField = (EditText) findViewById(R.id.editText1);
            button = (Button) findViewById(R.id.button1);
            //Button press event listener
            button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                            message = textField.getText().toString();
                            textField.setText("");
                           new ClientConnectorTask().execute(message);
                    }
            });
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
