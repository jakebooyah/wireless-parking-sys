/*
 *  Most of this code has been adapted from the following tutorial referenced:
 *  Tamada, R (2013) Android Sliding Menu using Navigation Drawer. Source Code. http://www.androidhive.info/2013/11/android-sliding-menu-using-navigation-drawer/
 * Written by:
 * Michael (Main UI Frame)
*/

package com.example.userinterface.model;

public class NavDrawerItem {
	
	private String title;
    private int icon;
    private String count = "0";
    
    // boolean to set visibility of the counter
    private boolean isCounterVisible = false;
     
    public NavDrawerItem(){}
 
    public NavDrawerItem(String title, int icon){
        this.title = title;
        this.icon = icon;
    }
     
    public NavDrawerItem(String title, int icon, boolean isCounterVisible, String count){
        this.title = title;
        this.icon = icon;
        this.isCounterVisible = isCounterVisible;
        this.count = count;
    }
     
    public String getTitle(){
        return this.title;
    }
     
    public int getIcon(){
        return this.icon;
    }
     
    public String getCount(){
        return this.count;
    }
     
    public boolean getCounterVisibility(){
        return this.isCounterVisible;
    }
     
    public void setTitle(String title){
        this.title = title;
    }
     
    public void setIcon(int icon){
        this.icon = icon;
    }
     
    public void setCount(String count){
        this.count = count;
    }
     
    public void setCounterVisibility(boolean isCounterVisible){
        this.isCounterVisible = isCounterVisible;
    }
	
}
