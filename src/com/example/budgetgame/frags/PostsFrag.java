package com.example.budgetgame.frags;

import com.example.budgetgame.R;
import com.example.budgetgame.db.DBAdapter;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

public class PostsFrag extends ListFragment {

	DBAdapter dbAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbAdapter = new DBAdapter(getActivity());
		dbAdapter.open();
		Cursor c = dbAdapter.getAllPosts();
		getActivity().startManagingCursor(c);
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
				getActivity(), R.layout.postitem, c, new String[] {
						"titel", "dato", "beloeb" }, new int[] { R.id.titel,
						R.id.dato, R.id.beloeb});
		setListAdapter(cursorAdapter);

	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}
}
