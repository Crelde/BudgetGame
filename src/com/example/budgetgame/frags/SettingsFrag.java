package com.example.budgetgame.frags;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;

/**
 * @author Kewin & Christian
 * @summary Fragment that displays the available preferences to the user.
 * 
 */
public class SettingsFrag extends PreferenceFragment {
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
       
        // Listener that fires when a shared preference is changed, to immediately change the theme.
        SharedPreferences.OnSharedPreferenceChangeListener spChanged = new
	            SharedPreferences.OnSharedPreferenceChangeListener() {

					@Override
					public void onSharedPreferenceChanged(
							SharedPreferences preference, String key) {
						if (key.compareTo("theme")==0) ((MainActivity) getActivity()).setActiveFragment(3);					
					}
		};       
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());	
		prefs.registerOnSharedPreferenceChangeListener(spChanged);		
    }
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle("Indstillinger");
		MainActivity activity = (MainActivity) getActivity();
		activity.setActiveFragment(3);
	}
}
