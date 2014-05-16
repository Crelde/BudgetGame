package com.example.budgetgame.frags;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;
import com.example.budgetgame.db.DBAdapter;

import android.app.Activity;
import android.app.Fragment;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AchievementFrag extends Fragment {

	static final String TITLE = "titel";
	static final String DESC = "beskrivelse";
	static final String ACHIEVED = "klaret";
	
	MainActivity mainActivity;
	
	// Contents of used achievements
	ContentValues awardValue_1;
	ContentValues awardValue_2;
	ContentValues awardValue_3;
	ContentValues awardValue_none;
	
	//TextViews for achievements
	TextView awardText_1;
	TextView awardText_2;
	TextView awardText_3;
	TextView awardText_4;
	TextView awardText_5;
	TextView awardText_6;
	TextView awardText_7;
	TextView awardText_8;
	TextView awardText_9;
	
	//Layouts for achievements
	RelativeLayout awardLayout_1;
	RelativeLayout awardLayout_2;
	RelativeLayout awardLayout_3;
	RelativeLayout awardLayout_4;
	RelativeLayout awardLayout_5;
	RelativeLayout awardLayout_6;
	RelativeLayout awardLayout_7;
	RelativeLayout awardLayout_8;
	RelativeLayout awardLayout_9;
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		((MainActivity) getActivity()).setActiveFragment(MainActivity.FRAGMENT_ACHIEVEMENTS);
		return inflater.inflate(R.layout.achievements, container, false);
	}
	
	private void checkAchievementStatus(){
		boolean awardReached_1 = false;
		boolean awardReached_2 = false;
		boolean awardReached_3 = false;
		
		if (awardValue_1.getAsInteger(ACHIEVED)==1) awardReached_1 = true;
		if (awardValue_2.getAsInteger(ACHIEVED)==1) awardReached_2 = true;
		if (awardValue_3.getAsInteger(ACHIEVED)==1) awardReached_3 = true;
		
		if (awardReached_1) getActivity().findViewById(R.id.imageView1).setAlpha(1);
		if (awardReached_2) getActivity().findViewById(R.id.imageView2).setAlpha(1);
		if (awardReached_3) getActivity().findViewById(R.id.imageView2).setAlpha(1);
		
		
		
		
	}
	
	
	private void initAchievementValues(){
		DBAdapter dbAdapter = new DBAdapter(getActivity());
		dbAdapter.open();
		
		Cursor achievements = dbAdapter.getAchievements();
		

		
		
		achievements.moveToFirst();
		
		Log.i("achievement", "achivements cursor has this many entries: " + achievements.getCount());
		
		Log.i("achievement", "achivements[0] cursor has this many columns: " + achievements.getColumnCount());
		
		Log.i("achievement", "TITLE: " + achievements.getString(1));
		Log.i("achievement", "DESC: " + achievements.getString(2));
		Log.i("achievement", "KLARET: " + achievements.getInt(3));
		
		awardValue_1 = new ContentValues();
		awardValue_2 = new ContentValues();
		awardValue_3 = new ContentValues();
		awardValue_none = new ContentValues();
		
		awardValue_3.put(TITLE, achievements.getString(1));
		awardValue_3.put(DESC, achievements.getString(2));
		awardValue_3.put(ACHIEVED, achievements.getInt(3));
		
		achievements.moveToNext();
		
		awardValue_2.put(TITLE, achievements.getString(1));
		awardValue_2.put(DESC, achievements.getString(2));
		awardValue_2.put(ACHIEVED, achievements.getInt(3));
		
		achievements.moveToNext();
		
		awardValue_1.put(TITLE, achievements.getString(1));
		awardValue_1.put(DESC, achievements.getString(2));
		awardValue_1.put(ACHIEVED, achievements.getInt(3));
		
		achievements.close();
		
		dbAdapter.close();
		
		awardValue_none.put(TITLE, "Findes ikke");
		awardValue_none.put(DESC, "Medaljen kan ikke opnås.");
		awardValue_none.put(ACHIEVED, 0);
		
		

	}
	
	private void initAchievementViews(){
		
		Activity a = getActivity();
		
		awardText_1 = (TextView) a.findViewById(R.id.textview_award1);
		awardText_2 = (TextView) a.findViewById(R.id.textview_award2);
		awardText_3 = (TextView) a.findViewById(R.id.textview_award3);
		awardText_4 = (TextView) a.findViewById(R.id.textview_award4);
		awardText_5 = (TextView) a.findViewById(R.id.textview_award5);
		awardText_6 = (TextView) a.findViewById(R.id.textview_award6);
		awardText_7 = (TextView) a.findViewById(R.id.textview_award7);
		awardText_8 = (TextView) a.findViewById(R.id.textview_award8);
		awardText_9 = (TextView) a.findViewById(R.id.textview_award9);
		
		
		awardLayout_1 = (RelativeLayout) a.findViewById(R.id.award_1);
		awardLayout_2 = (RelativeLayout) a.findViewById(R.id.award_2);
		awardLayout_3 = (RelativeLayout) a.findViewById(R.id.award_3);
		awardLayout_4 = (RelativeLayout) a.findViewById(R.id.award_4);
		awardLayout_5 = (RelativeLayout) a.findViewById(R.id.award_5);
		awardLayout_6 = (RelativeLayout) a.findViewById(R.id.award_6);
		awardLayout_7 = (RelativeLayout) a.findViewById(R.id.award_7);
		awardLayout_8 = (RelativeLayout) a.findViewById(R.id.award_8);
		awardLayout_9 = (RelativeLayout) a.findViewById(R.id.award_9);
		
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initAchievementValues();
		
		
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		getActivity().setTitle("Medaljer");
		
		initAchievementViews();
		checkAchievementStatus();
		
		mainActivity = (MainActivity) getActivity();
				
		
		awardText_1.setText(awardValue_1.getAsString(TITLE));
		awardText_2.setText(awardValue_2.getAsString(TITLE));
		awardText_3.setText(awardValue_3.getAsString(TITLE));
		
		
		// OnClickListeners for awards
		awardLayout_1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainActivity.ShowAwardDialog(awardValue_1);
			}
		});
		
		awardLayout_2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainActivity.ShowAwardDialog(awardValue_2);				
			}
		});
		
		awardLayout_3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainActivity.ShowAwardDialog(awardValue_3);				
			}
		});
		
		OnClickListener noAwardClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainActivity.ShowAwardDialog(awardValue_none);	
				
			}
		};
		
		awardLayout_4.setOnClickListener(noAwardClickListener);
		awardLayout_5.setOnClickListener(noAwardClickListener);
		awardLayout_6.setOnClickListener(noAwardClickListener);
		awardLayout_7.setOnClickListener(noAwardClickListener);
		awardLayout_8.setOnClickListener(noAwardClickListener);
		awardLayout_9.setOnClickListener(noAwardClickListener);
			
		
	
				
				
				//awardLayout_1.setBackground(awardedBackground);
		
	
		//award1.setOnClickListener(l) // Comment for fun
	}
}
