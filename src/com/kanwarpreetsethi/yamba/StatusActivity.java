package com.kanwarpreetsethi.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends BaseActivity implements OnClickListener, TextWatcher {
	
	
	EditText editText;
	Button updateButton;
	TextView textCount;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.status);
        
        editText = (EditText) findViewById(R.id.statusText);
        updateButton = (Button) findViewById(R.id.buttonUpdate);
        updateButton.setOnClickListener(this);
        
        textCount = (TextView) findViewById(R.id.textCount);
        textCount.setText(Integer.toString(140));
        textCount.setTextColor(Color.GREEN);
        
        editText.addTextChangedListener(this);  

    }
    
    class PostToTwitter extends AsyncTask<String, Integer, String> {

		@Override
		protected String doInBackground(String... statuses) {
			
			try {
				Twitter.Status status = yamba.getTwitter().updateStatus(statuses[0]);
				return status.text;
			}	catch (TwitterException e) {
				e.printStackTrace();
				return "Failed To Post";
			}
			
		}
		
		@Override
		protected void onProgressUpdate(Integer...values) {
			super.onProgressUpdate(values);
		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
    	
    }
    
	@Override
	public void onClick(View view) {
		String status = editText.getText().toString();
		new PostToTwitter().execute(status);
	}
	
	
	@Override
	public void afterTextChanged(Editable statusText) {
		int count = 140 - statusText.length();
		textCount.setText(Integer.toString(count));
		textCount.setTextColor(Color.GREEN);
		if (count < 20)
			textCount.setTextColor(Color.YELLOW);
		if (count < 0)
			textCount.setTextColor(Color.RED);
	}

	@Override
	public void beforeTextChanged(CharSequence text, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence text, int start, int before, int count) {
		
	}
	
}