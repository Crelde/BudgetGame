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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;
import com.example.budgetgame.db.DBAdapter;
/**
 * @author Kewin & Christian
 * @summary Fragment that displays goals to the user, 
 * as well as allowing the user to create, update and delete existing ones
 * 
 * 
 */
public class GoalFrag extends ListFragment {

	DBAdapter dbAdapter;

	
	// Create a new goal in the database.
	public long setNewGoal(String titel, int beloeb, int toSave){
		long id = dbAdapter.setNewGoal(titel, beloeb, toSave);
		initGoals();
		return id;
	}
	
	// Set alarm for goal that set aside money each month.
	public void setStandardAlarmForGoal(Context context, int id)
	{
		dbAdapter.setStandardAlarmForGoal(context, id);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dbAdapter = new DBAdapter(getActivity());
		dbAdapter.open();
		initGoals();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		getActivity().setTitle("M�l");
		
		//OnItemClickListener
		ListView list = (ListView) getActivity().findViewById(android.R.id.list);		
		System.out.println(list.toString());
		list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				//((MainActivity) getActivity()).makeToast("you clicked id:"+id);
				Cursor goal = dbAdapter.getGoal(id);
				((MainActivity) getActivity()).ShowEditGoalDialog(goal);
				
			                
			            }
			        });
		// Intiilaze history button, and set its onCliclListener to change the content fragment.
		Button goalHistoryButton = (Button) getActivity().findViewById(R.id.goalHistoryButton);
		goalHistoryButton.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.FragmentContainer, new GoalHistoryFrag());
			ft.commit();					
		}
	});
	}
	
	// Intialize goals from the database.
	public void initGoals(){
		Cursor c = dbAdapter.getAllGoals();
		//getActivity().startManagingCursor(c);
		SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
				getActivity(), R.layout.goalitem, c, new String[] {
						"titel", "beloebCurrent", "beloebM�l" }, new int[] { R.id.goalNameE,
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
