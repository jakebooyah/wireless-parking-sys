package com.example.userinterface;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class WhereFragment extends Fragment {
	
	Spinner spnLocation;
	WebView myWebView;
	TextView txtLocation;
	String location, url, defaultValue; 
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  	
		View rootView = inflater.inflate(R.layout.fragment_where, container, false);

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

		return rootView;
    }
	
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
