package com.example.userinterface;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MapFragment extends Fragment {
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        
        WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        String url = "http://ec2-54-254-255-187.ap-southeast-1.compute.amazonaws.com/grp";

        WebSettings webSettings = myWebView.getSettings();
        myWebView.setInitialScale(1);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE
        myWebView.loadUrl(url);
         
        return rootView;
    }
	
	private class Callback extends WebViewClient{  //HERE IS THE MAIN CHANGE. 

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}


	