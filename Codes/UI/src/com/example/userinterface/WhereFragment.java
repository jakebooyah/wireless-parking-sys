/* Written by:
 * Michael (Main UI Frame)
 * Lim Zhi En (User Guide, Where Features)
*/

package com.example.userinterface;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
		
		sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefUserGuide = sharedPref.getBoolean(SettingsFragment.KEY_PREF_USER_GUIDE, true);

		spnLocation = (Spinner) rootView.findViewById(R.id.spinner_location);
		myWebView = (WebView) rootView.findViewById(R.id.webview_where);
		txtLocation = (TextView) rootView.findViewById(R.id.textview_location);

		myWebView.setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE
		loadUrl();
		
		WebSettings webSettings = myWebView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
		webSettings.setBuiltInZoomControls(true);
		webSettings.setJavaScriptEnabled(true);
		
		sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
		defaultValue = getResources().getString(R.string.saved_location);
		location = sharedPref.getString(getString(R.string.saved_location), defaultValue);
		txtLocation.setText(location);
		
		setSpinnerValue(spnLocation, location);
		spnLocation.setOnItemSelectedListener(getOnItemSelectedListener());
		
		t1 = Toast.makeText(getActivity(),"Always forget where you parked? WHERE is here to save your day.",Toast.LENGTH_LONG);
		t2 = Toast.makeText(getActivity(),"Just select your parked location...",Toast.LENGTH_LONG);
		t3 = Toast.makeText(getActivity(),"...and simply let WHERE remember it for you.",Toast.LENGTH_LONG);
		t4 = Toast.makeText(getActivity(),"You're welcomed.",Toast.LENGTH_LONG);
		
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
		super.onPause();
		t1.cancel();
		t2.cancel();
		t3.cancel();
		t4.cancel();
	}

	/*@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.fragment_where);
        }
        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.login);         
        }
	}*/



	private class Callback extends WebViewClient{  //HERE IS THE MAIN CHANGE. 

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }
    }
	
	private void loadUrl() {
		url = "http://ec2-54-254-255-187.ap-southeast-1.compute.amazonaws.com/grp/find.php?location=" + location;
		myWebView.loadUrl(url);
		myWebView.reload();
	}
	
	public static void setSpinnerValue(Spinner spin, String value) {
		for (int i = 0; i < spin.getCount(); i++) {
	        if (spin.getItemAtPosition(i).toString().equalsIgnoreCase(value)) {
	            spin.setSelection(i);
	            break;
	        }
	    }
	}
	
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
