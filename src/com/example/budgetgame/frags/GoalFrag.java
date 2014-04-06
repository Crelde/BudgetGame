package com.example.budgetgame.frags;


import com.example.budgetgame.R;
import com.example.budgetgame.db.DBAdapter;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class GoalFrag extends ListFragment {


	DBAdapter dbAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		return inflater.inflate(R.layout.goals, container, false);
	}
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}


}
