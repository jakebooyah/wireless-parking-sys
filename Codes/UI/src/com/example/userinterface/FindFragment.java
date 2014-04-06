package com.example.userinterface;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.Spinner;

public class FindFragment extends Fragment {
	CheckBox cbRed;
	CheckBox cbYellow;
	Spinner spnNearest;
	WebView myWebView;
	String red, yellow, nearest, url;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
  
        View rootView = inflater.inflate(R.layout.fragment_find, container, false);
        
        cbRed = (CheckBox) rootView.findViewById(R.id.checkbox_red);
        cbYellow = (CheckBox) rootView.findViewById(R.id.checkbox_yellow);
        spnNearest = (Spinner) rootView.findViewById(R.id.spinner_nearest);
        myWebView = (WebView) rootView.findViewById(R.id.webview_find);
        myWebView.setWebViewClient(new Callback()); 
        
        cbRed.setOnClickListener(getOnClickListener());
        cbYellow.setOnClickListener(getOnClickListener());
        spnNearest.setOnItemSelectedListener(getOnItemSelectedListener());
        	
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        loadUrl();
               
        return rootView;
      
    }
	
	private class Callback extends WebViewClient{  //HERE IS THE MAIN CHANGE. 

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
	
	private void loadUrl() {
		url = "http://ec2-54-254-255-187.ap-southeast-1.compute.amazonaws.com/grp/find.php?red=" + red + "&yellow=" 
				+ yellow + "&nearest=" + nearest;
		myWebView.loadUrl(url);
	}
	
	AdapterView.OnItemSelectedListener getOnItemSelectedListener() {
		return new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int pos, long id) {
				// TODO Auto-generated method stub
				Object item = parent.getItemAtPosition(pos);
				nearest = item.toString().replaceAll("\\s+","").toLowerCase();
    	        loadUrl();		
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
			}
			
		};
	}
	
	View.OnClickListener getOnClickListener() {
		return new View.OnClickListener() {
        
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(cbRed.isChecked() && cbYellow.isChecked()) {
					red = "true";
					yellow = "true";
				}
				else {
					if(cbRed.isChecked()) {
						red = "true";
						yellow = "false";
					}
					else if(cbYellow.isChecked()) {
						yellow = "true";
						red = "false";
					}
					else {
						red = "false";
						yellow = "false";
					}
				}
				loadUrl();
			}
			
		};
		
	}
}