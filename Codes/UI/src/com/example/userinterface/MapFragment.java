/* Written by:
 * Michael (Main UI Frame)
 * Tang Wei Qi (Webview)
 * Lim Zhi En (User Guide)
*/

package com.example.userinterface;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

@SuppressLint("NewApi")
public class MapFragment extends Fragment {
	SharedPreferences sharedPref;
    Boolean prefUserGuide;
	Toast t1,t2,t3,t4,t5,t6;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        
        //get user guide preference from Settings 
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefUserGuide = sharedPref.getBoolean(SettingsFragment.KEY_PREF_USER_GUIDE, true);
        
        WebView myWebView = (WebView) rootView.findViewById(R.id.webview);
        //Map page url for Map feature
        String url = "http://ec2-54-254-255-187.ap-southeast-1.compute.amazonaws.com/grp";
        
        //configure web settings
        WebSettings webSettings = myWebView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);

        myWebView.setWebViewClient(new Callback());  //HERE IS THE MAIN CHANGE
        //load map page on webview
        myWebView.loadUrl(url);
        
        //user guide Toast messages
        t1 = Toast.makeText(getActivity(),"Hola, welcome to UNMC!",Toast.LENGTH_LONG);
        t2 = Toast.makeText(getActivity(),"MAP shows you the real-time status of the UNMC parking lot.",Toast.LENGTH_LONG);
        t3 = Toast.makeText(getActivity(),"There are red and yellow parking bays.",Toast.LENGTH_LONG);
        t4 = Toast.makeText(getActivity(),"RED represents VACANT red bay.",Toast.LENGTH_LONG);
        t5 = Toast.makeText(getActivity(),"YELLOW represents VACANT yellow bay.",Toast.LENGTH_LONG);
        t6 = Toast.makeText(getActivity(),"GREY represents OCCUPIED bay.",Toast.LENGTH_LONG);
        
        //if user guide enabled in settings, show Toast messages
    	if(prefUserGuide) {
    		t1.show();
    		t2.show();
    		t3.show();
    		t4.show();
    		t5.show();
    		t6.show();
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
		t5.cancel();
		t6.cancel();
	}

	private class Callback extends WebViewClient{   

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }
    }
	
}


	