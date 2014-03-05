package com.example.mysqlconnector;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	EditText inputMessage;
	
	private static String url = "http://ec2-54-254-255-187.ap-southeast-1.compute.amazonaws.com/grp/create_record3.php";
	private static final String TAG_SUCCESS = "success";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		inputMessage = (EditText) findViewById(R.id.edit_message);
		
		Button btnAdd = (Button) findViewById(R.id.add);
		
		final String sID = "A10848B210";
		
		// button click event
        btnAdd.setOnClickListener(new View.OnClickListener() {
 
            @Override
            public void onClick(View view) {
                // creating new product in background thread
            	String txtInput = inputMessage.getText().toString();
                new HttpWebService().execute(txtInput);
            }
        });
	}
	
	public class HttpWebService extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub
            // Building Parameters
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("sensor_id", params[0]));
           
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = new JSONParser().makeHttpRequest(url, "POST", param);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
		
	}
}
