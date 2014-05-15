package com.example.budgetgame.frags;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SettingFrag extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.settings, container, false);
		((MainActivity) getActivity()).setActiveFragment(MainActivity.FRAGMENT_SETTINGS);
		return v;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle("Indstillinger");
	}
	

}
