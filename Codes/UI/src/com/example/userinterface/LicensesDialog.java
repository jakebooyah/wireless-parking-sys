/* Written by:
 * Lim Zhi En 
*/

package com.example.userinterface;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;

//This class is a custom DialogPreference listed in the About subscreen under Settings
public class LicensesDialog extends DialogPreference {
    public LicensesDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
        setDialogLayoutResource(R.layout.dialog_license_settings);
        setPositiveButtonText(android.R.string.ok);
        setNegativeButtonText(null);
    }
   
}
