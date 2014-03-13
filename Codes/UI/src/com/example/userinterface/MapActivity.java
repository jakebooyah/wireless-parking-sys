package com.example.userinterface;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

public class MapActivity extends FragmentActivity {
	
	//Declaration of resources to be used
	ViewPager Tab;
    TabPagerAdapter TabAdapter;
    ActionBar actionBar;
    
    // Tab titles
    private String[] tabs = { "All", "Zone A", "Zone B" };
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_section);
    
        // Initilization
        Tab = (ViewPager) findViewById(R.id.pager);
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
 
        Tab.setOnPageChangeListener(
        		new ViewPager.SimpleOnPageChangeListener(){
        			@Override
        			public void onPageSelected(int position) {
        				actionBar = getActionBar();
        				actionBar.setSelectedNavigationItem(position);                    }
        		}
        );
     
        Tab.setAdapter(TabAdapter);
        
        //Set the action bar
        actionBar = getActionBar();
        
        //Enable tabs on action Bar and add a listener
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);        
        
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {	
			@Override
			public void onTabUnselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub
				//Return the current position of the tab in the action bar
				Tab.setCurrentItem(tab.getPosition());
			}
			
			@Override
			public void onTabReselected(android.app.ActionBar.Tab tab, FragmentTransaction ft) {
				// TODO Auto-generated method stub		
			}
		};
        // Adding Tabs
         actionBar.addTab(actionBar.newTab().setText(tabs[0]).setTabListener(tabListener)) ;
         actionBar.addTab(actionBar.newTab().setText(tabs[1]).setTabListener(tabListener)) ;    
         actionBar.addTab(actionBar.newTab().setText(tabs[2]).setTabListener(tabListener)) ;
    }
}


	