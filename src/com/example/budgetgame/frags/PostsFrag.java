package com.example.budgetgame.frags;

import android.app.ListFragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;
import com.example.budgetgame.db.DBAdapter;

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
		getActivity().stopManagingCursor(c);
		
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
		getActivity().stopManagingCursor(c);
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
			getActivity().stopManagingCursor(c);
		}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initAllPosts();
		

	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		((MainActivity) getActivity()).setActiveFragment(MainActivity.FRAGMENT_POSTS);
		return inflater.inflate(R.layout.postlist, container, false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle("Posteringer");
		
		//OnItemClickListener//
				ListView list = (ListView) getActivity().findViewById(android.R.id.list);		
				//System.out.println(list.toString());
				list.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
						((MainActivity) getActivity()).showPostDialog((int) id);
						
					                
					            }
					        });
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
	}
}
