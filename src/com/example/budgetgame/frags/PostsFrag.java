package com.example.budgetgame.frags;

import com.example.budgetgame.R;
import com.example.budgetgame.db.DBAdapter;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class PostsFrag extends ListFragment {


	DBAdapter dbAdapter;

	public void updatePosts(boolean positive, boolean negative){
		if(positive && negative)
			initAllPosts();
		else if(positive && !negative)
			initPosPosts();
		else if(!positive && negative)
			initNegPosts();
	}
	public void initAllPosts(){
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
	public void initPosPosts(){
		dbAdapter = new DBAdapter(getActivity());
		dbAdapter.open();
		Cursor c = dbAdapter.getPosPosts();
		getActivity().startManagingCursor(c);
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
				getActivity(), R.layout.postitem, c, new String[] {
						"titel", "dato", "beloeb" }, new int[] { R.id.titel,
						R.id.dato, R.id.beloeb});
		setListAdapter(cursorAdapter);
	}
	public void initNegPosts(){
			dbAdapter = new DBAdapter(getActivity());
			dbAdapter.open();
			Cursor c = dbAdapter.getNegPosts();
			getActivity().startManagingCursor(c);
			SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
					getActivity(), R.layout.postitem, c, new String[] {
							"titel", "dato", "beloeb" }, new int[] { R.id.titel,
							R.id.dato, R.id.beloeb});
			setListAdapter(cursorAdapter);
		}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
			initAllPosts();

	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.postlist, container, false);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}
}
