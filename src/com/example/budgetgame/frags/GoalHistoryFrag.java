package com.example.budgetgame.frags;

import com.example.budgetgame.R;

import android.app.ListFragment;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;

public class GoalHistoryFrag extends ListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		String[] columns = new String[] {"goalName", "historyText", "historyDate", "_id" };

		MatrixCursor matrixCursor= new MatrixCursor(columns);
		getActivity().startManagingCursor(matrixCursor);

		matrixCursor.addRow(new Object[] {"Ny cykel", "Du sparede 500 kr op!", "07-04-2014", "10" });
		matrixCursor.addRow(new Object[] {"Ferie Mallorca", "Du sparede 400 kr op!", "01-04-2014", "14" });	
		matrixCursor.addRow(new Object[] {"Ferie Mallorca", "Du sparede 400 kr op!", "01-04-2014", "17" });
		matrixCursor.addRow(new Object[] {"Ferie Mallorca", "Du sparede 400 kr op!", "01-03-2014", "18" });
		matrixCursor.addRow(new Object[] {"Ferie Mallorca", "Du sparede 400 kr op!", "01-02-2014", "21" });

		SimpleCursorAdapter adapter = 
		        new SimpleCursorAdapter(
		        		getActivity(), 
		        		R.layout.historyitem, 
		        		matrixCursor, 
		        		columns, 
		        		new int[] { R.id.goalName, R.id.historyText, R.id.historyDate });

		setListAdapter(adapter);
		
		
	}
}
