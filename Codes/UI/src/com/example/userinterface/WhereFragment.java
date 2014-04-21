/* Written by:
 * Michael (Main UI Frame)
 * Lim Zhi En (User Guide, Where Features)
*/

package com.example.userinterface;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class WhereFragment extends Fragment {
	
	Spinner spnLocation;
	WebView myWebView;
	TextView txtLocation;
	String location, url, defaultValue; 
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	Boolean prefUserGuide;
	Toast t1,t2,t3,t4;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  	
		View rootView = inflater.inflate(R.layout.fragment_where, container, false);
		
		//get user guide preference from Settings
		sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefUserGuide = sharedPref.getBoolean(SettingsFragment.KEY_PREF_USER_GUIDE, true);

		spnLocation = (Spinner) rootView.findViewById(R.id.spinner_location);
		myWebView = (WebView) rootView.findViewById(R.id.webview_where);
		txtLocation = (TextView) rootView.findViewById(R.id.textview_location);

		myWebView.setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE
		loadUrl();
		
		//configure webview settings
		WebSettings webSettings = myWebView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		
		//retrieve saved SharedPreferences data
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		defaultValue = getResources().getString(R.string.saved_location);
		location = sharedPref.getString(getString(R.string.saved_location), defaultValue);
		txtLocation.setText(location);
		
		setSpinnerValue(spnLocation, location);
		//set onclick listener for spinner
		spnLocation.setOnItemSelectedListener(getOnItemSelectedListener());
		
		//user guide Toast messages
		t1 = Toast.makeText(getActivity(),"Always forget where you parked? WHERE is here to save your day.",Toast.LENGTH_LONG);
		t2 = Toast.makeText(getActivity(),"Just select your parked location...",Toast.LENGTH_LONG);
		t3 = Toast.makeText(getActivity(),"...and simply let WHERE remember it for you.",Toast.LENGTH_LONG);
		t4 = Toast.makeText(getActivity(),"You're welcomed.",Toast.LENGTH_LONG);
		
		//if user guide enabled in settings, show Toast messages
		if(prefUserGuide) {
			t1.show();
			t2.show();
			t3.show();
			t4.show();
		}
		
		return rootView;
    }
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		//cancel all Toast messages when fragment paused
		super.onPause();
		t1.cancel();
		t2.cancel();
		t3.cancel();
		t4.cancel();
	}

	private class Callback extends WebViewClient{  //HERE IS THE MAIN CHANGE. 

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }
    }
	
	private void loadUrl() {
		//Map page url for Where fragment
		url = "http://ec2-54-254-255-187.ap-southeast-1.compute.amazonaws.com/grp/find.php?location=" + location;
		//load map page on webview
		myWebView.loadUrl(url);
		myWebView.reload();
	}
	
	//set spinner value to be the same as SharedPreferences data
	public static void setSpinnerValue(Spinner spin, String value) {
		for (int i = 0; i < spin.getCount(); i++) {
	        if (spin.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
	            spin.setSelection(i);
	            break;
	        }
	    }
	}
	
	//onselect listener for spinner
	AdapterView.OnItemSelectedListener getOnItemSelectedListener() {
		return new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				Object item = parent.getItemAtPosition(pos);
				location = item.toString();
				txtLocation.setText(location);	
				loadUrl();
				
				//save location String data to SharedPreferences 
				sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
				editor = sharedPref.edit();
				editor.putString(getString(R.string.saved_location), location);
				editor.commit();	
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}		
		};
	}
}
