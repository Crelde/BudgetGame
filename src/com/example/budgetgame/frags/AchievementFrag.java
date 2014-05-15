package com.example.budgetgame.frags;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class AchievementFrag extends Fragment {

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		((MainActivity) getActivity()).setActiveFragment(MainActivity.FRAGMENT_ACHIEVEMENTS);
		return inflater.inflate(R.layout.achievements, container, false);
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		RelativeLayout award1 = (RelativeLayout) getActivity().findViewById(R.id.award_1);
	}
}
