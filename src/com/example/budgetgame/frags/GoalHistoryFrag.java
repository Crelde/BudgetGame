package com.example.budgetgame.frags;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;
import com.example.budgetgame.db.DBAdapter;

import android.app.ListFragment;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class GoalHistoryFrag extends ListFragment {

	DBAdapter dbAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] columns = new String[] {"titel", "beskrivelse", "dato", "_id"};
		/*
		MatrixCursor matrixCursor= new MatrixCursor(columns);
		getActivity().startManagingCursor(matrixCursor);

		matrixCursor.addRow(new Object[] {"Ny cykel", "Du sparede 500 kr op!", "07-04-2014", "10" });
		matrixCursor.addRow(new Object[] {"Ferie Mallorca", "Du sparede 400 kr op!", "01-04-2014", "14" });	
		matrixCursor.addRow(new Object[] {"Ferie Mallorca", "Du sparede 400 kr op!", "01-04-2014", "17" });
		matrixCursor.addRow(new Object[] {"Ferie Mallorca", "Du sparede 400 kr op!", "01-03-2014", "18" });
		matrixCursor.addRow(new Object[] {"Ferie Mallorca", "Du sparede 400 kr op!", "01-02-2014", "21" });
*/
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
		dbAdapter.close();
		cursor.close();
	}
	@Override
	public void onResume() {
		super.onResume();
		((MainActivity) getActivity()).setActiveFragment(MainActivity.FRAGMENT_GOALS);
	}
	
}
