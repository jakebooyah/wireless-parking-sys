/* Written by:
 * Lim Zhi En
*/

package com.example.userinterface;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.DialogPreference;
import android.util.AttributeSet;

//This class is a custom DialogPreference listed in Settings
public class ClearDataDialog extends DialogPreference {
	
	SharedPreferences sharedPref;
	SharedPreferences.Editor editor;
	Context mContext;
	public static final int BUTTON_POSITIVE = -1;
	public static final int BUTTON_NEGATIVE = -2;
	
	public ClearDataDialog(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		mContext = context;
		this.setDialogTitle(null);		
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		// TODO Auto-generated method stub
		super.onClick(dialog, which);
		
		switch(which) {
		//do nothing if user selects Cancel
		case BUTTON_NEGATIVE:
			break;
		//delete saved SharedPreferences data if user selects OK
		case BUTTON_POSITIVE:
			sharedPref = ((Activity)mContext).getPreferences(Context.MODE_PRIVATE);
			editor = sharedPref.edit();
			editor.clear();
			editor.commit();
			break;
		}
	}
	
}
