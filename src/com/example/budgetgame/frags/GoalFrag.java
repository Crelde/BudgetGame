package com.example.budgetgame.frags;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;
import com.example.budgetgame.db.DBAdapter;

public class GoalFrag extends ListFragment {

	DBAdapter dbAdapter;

	
	
	public long setNewGoal(String titel, int beloeb, int toSave){
		long id = dbAdapter.setNewGoal(titel, beloeb, toSave);
		initGoals();
		return id;
	}
	
	public void setStandardAlarmForGoal(Context context, long id)
	{
		dbAdapter.setStandardAlarmForGoal(context, id);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initGoals();
		
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		Button goalHistoryButton = (Button) getActivity().findViewById(R.id.goalHistoryButton);
		goalHistoryButton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.addToBackStack(null);
			ft.replace(R.id.FragmentContainer, new GoalHistoryFrag());
			ft.commit();					
		}
	});
	}
	public void initGoals(){
		dbAdapter = new DBAdapter(getActivity());
		dbAdapter.open();
		Cursor c = dbAdapter.getAllGoals();
		getActivity().startManagingCursor(c);
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
				getActivity(), R.layout.goalitem, c, new String[] {
						"titel", "beloebCurrent", "beloebMål" }, new int[] { R.id.goalNameE,
						R.id.currentStatusE, R.id.goalAmountE});
		setListAdapter(cursorAdapter);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {	
		((MainActivity) getActivity()).setActiveFragment(MainActivity.FRAGMENT_GOALS);
		return inflater.inflate(R.layout.goals, container, false);
	}

	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}


}
