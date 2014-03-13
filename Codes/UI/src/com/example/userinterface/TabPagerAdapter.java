package com.example.userinterface;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Fragment getItem(int index) {
		switch (index){
		case 0:
			//Fragment for all tab
			return new allFragment();
		case 1:
			//Fragment for zoneA tab
			return new zoneAFragment();
		case 2:
			//Fragment for zoneB tab
			return new zoneBFragment();
		}
		return null;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 3;//No of tabs that exist in the TabPagerAdapter
	}

}
