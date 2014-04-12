package com.example.userinterface;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MapFragment extends PreferenceFragment {
	SharedPreferences sharedPref;
    Boolean prefUserGuide;
	Toast t1;
	Toast t2;
	Toast t3;
	Toast t4;
	Toast t5;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        
        Button btn = (Button) rootView.findViewById(R.id.btnShowToast);
        
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefUserGuide = sharedPref.getBoolean(SettingsFragment.KEY_PREF_USER_GUIDE, true);
        
        
        
        btn.setOnClickListener(new OnClickListener() {
        	

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
        	
        });
        
        WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        String url = "http://ec2-54-254-255-187.ap-southeast-1.compute.amazonaws.com/grp";

        WebSettings webSettings = myWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE
        myWebView.loadUrl(url);
        
        final Toast t1 = Toast.makeText(getActivity(),"Hola, welcome to UNMC!",Toast.LENGTH_LONG);
        final Toast t2 = Toast.makeText(getActivity(),"A few map indicator tips for you...",Toast.LENGTH_LONG);
        final Toast t3 = Toast.makeText(getActivity(),"RED block : VACANT Red parking bay",Toast.LENGTH_LONG);
        final Toast t4 = Toast.makeText(getActivity(),"YELLOW block : VACANT Yellow parking bay",Toast.LENGTH_LONG);
        final Toast t5 = Toast.makeText(getActivity(),"GREY block : OCCUPIED parking bay",Toast.LENGTH_LONG);
        
        Thread th2 = new Thread() {
    		public void run() {
    			prefUserGuide = false;
    		}
            };
            
            th2.start();
            
            
        
        Thread th1 = new Thread() {
    		public void run() {
    			if(prefUserGuide) 
    	        	t1.show();
    	        
    	        if(prefUserGuide) 
    	        	t2.show();
    	        
    	        if(prefUserGuide) 
    	        	t3.show();
    	        
    	        if(prefUserGuide) 
    	        	t4.show();
    	        
    	        if(prefUserGuide) 
    	        	t5.show();
    	         
    		}
    	};
    	
    	
		
		th1.start();
		
		
        
        
        return rootView;
    }
	
	
	
	private class Callback extends WebViewClient{  //HERE IS THE MAIN CHANGE. 

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }

	//@Override
	/*public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		// TODO Auto-generated method stub
		t1.cancel();
		t2.cancel();
		t3.cancel();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
        .unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
        .registerOnSharedPreferenceChangeListener(this);
	}*/
	
	
}


	