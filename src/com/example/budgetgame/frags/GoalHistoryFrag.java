package com.example.budgetgame.frags;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;
import com.example.budgetgame.db.DBAdapter;

/**
 * @author Kewin & Christian
 * @summary Fragment that displays history entries to the user.
 * 
 */
public class GoalHistoryFrag extends ListFragment {

	DBAdapter dbAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Get history entries from database,´.
		String[] columns = new String[] {"titel", "beskrivelse", "dato", "_id"};
		dbAdapter = new DBAdapter(getActivity());
		dbAdapter.open();
		Cursor cursor = dbAdapter.getHistory();
		
		SimpleCursorAdapter adapter = 
		        new SimpleCursorAdapter(
		        		getActivity(), 
		        		R.layout.historyitem, 
		        		cursor, 
		        		columns, 
		        		new int[] { R.id.goalName, R.id.historyText, R.id.historyDate });

		setListAdapter(adapter);
	}
	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) getActivity()).setActiveFragment(MainActivity.FRAGMENT_GOALS);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}
	
}

