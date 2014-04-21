/* Written by:
 * Michael (Main UI Frame)
 * Lim Zhi En (User Guide, Settings Features)
*/

package com.example.userinterface;

import com.example.userinterface.R;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

@SuppressLint("NewApi")
public class SettingsFragment extends PreferenceFragment {
	
	public static final String KEY_PREF_USER_GUIDE = "pref_userGuide";
	Boolean prefUserGuide;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.preferences);
	}
	
	@Override
	public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference    preference) {
	super.onPreferenceTreeClick(preferenceScreen, preference);

	// If the user has clicked on a preference screen, set up the action bar
	if (preference instanceof PreferenceScreen) {
	    initializeActionBar((PreferenceScreen) preference);
	}

	return false;
	}
	
	/** Sets up the action bar for an {@link PreferenceScreen} */
    public static void initializeActionBar(PreferenceScreen preferenceScreen) {
    final Dialog dialog = preferenceScreen.getDialog();

    	if (dialog != null) {
    		// Inialize the action bar
    		dialog.getActionBar().setDisplayHomeAsUpEnabled(true);

    		// Apply custom home button area click listener to close the PreferenceScreen because PreferenceScreens are dialogs which swallow
    		// events instead of passing to the activity
    		View homeBtn = dialog.findViewById(android.R.id.home);

    		if (homeBtn != null) {
    			OnClickListener dismissDialogClickListener = new OnClickListener() {
    				@Override
    				public void onClick(View v) {
    					dialog.dismiss();
    				}
    			};

    			ViewParent homeBtnContainer = homeBtn.getParent();

    			// The home button is an ImageView inside a FrameLayout
    			if (homeBtnContainer instanceof FrameLayout) {
    				ViewGroup containerParent = (ViewGroup) homeBtnContainer.getParent();

                	if (containerParent instanceof LinearLayout) {
                		// This view also contains the title text, set the whole view as clickable
                		((LinearLayout) containerParent).setOnClickListener(dismissDialogClickListener);
                	} else {
                		// Just set it on the home button
                		((FrameLayout) homeBtnContainer).setOnClickListener(dismissDialogClickListener);
                	}
    			} else {
    				// The 'If all else fails' default case
    				homeBtn.setOnClickListener(dismissDialogClickListener);
    			}
    		}    
    	}
    }
    
}

