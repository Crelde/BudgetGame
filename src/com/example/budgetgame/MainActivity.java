package com.example.budgetgame;

import com.example.budgetgame.db.DBAdapter;
import com.example.budgetgame.frags.GoalFrag;
import com.example.budgetgame.frags.OverviewFrag;
import com.example.budgetgame.frags.PostsFrag;
import com.example.budgetgame.frags.SettingFrag;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private String posttag = "POST_TAG";
	DBAdapter dbAdapter;
	// Buttons
	private Button homeButton;
	private Button postsButton;
	private Button goalsButton;
	private Button settingsButton;

	// Filter Controls
	private Button filterOK;
	private Button filterCancel;
	private CheckBox checkpos;
	private CheckBox checkneg;
	private Dialog postsDialog;
	private Dialog newGoalDialog;
	
	//New goal controls
	private Button opretMålButton;
	private Button cancelNytMålButton;
	private EditText newGoalNameE;
	private EditText newGoalAmountE;
	private EditText newGoalAmountMonthE;
	
	
	// Fragments
	PostsFrag postfrag = new PostsFrag();
	OverviewFrag overfrag = new OverviewFrag();
	GoalFrag goalfrag = new GoalFrag();
	SettingFrag settingfrag = new SettingFrag();
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Button initializers
		homeButton = (Button) findViewById(R.id.homeButton);
		postsButton = (Button) findViewById(R.id.postsButton);
		goalsButton = (Button) findViewById(R.id.goalsButton);
		settingsButton = (Button) findViewById(R.id.settingsButton);
		
	
		// Initial adding of the overview fragment
			FragmentManager fm = getFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.FragmentContainer, overfrag);
			ft.commit();
			
		

		// Implementations of button onclicks so they change between the fragments
		homeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.FragmentContainer, overfrag);
				ft.commit();;	
			}
		});
		
		postsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.FragmentContainer, postfrag, posttag);
				ft.commit();;	

			}
		});
		
		goalsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.FragmentContainer, goalfrag);
				ft.commit();;	
			}
		});
		
		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.FragmentContainer, settingfrag);
				ft.commit();	
			}
		});

		
	}


	public void ShowPostsDialog(View v){
		
		postsDialog = new Dialog(MainActivity.this);
		postsDialog.setContentView(R.layout.filterdialog);
		postsDialog.setTitle("Vælg Filtre");
		postsDialog.show();
		checkpos = (CheckBox)postsDialog.findViewById(R.id.checkpos);
		checkneg = (CheckBox)postsDialog.findViewById(R.id.checkneg);
		filterOK = (Button)postsDialog.findViewById(R.id.filterOK);
		filterCancel = (Button)postsDialog.findViewById(R.id.filterCancel);
		
		filterOK.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				PostsFrag f = (PostsFrag) getFragmentManager().findFragmentById(R.id.FragmentContainer);
				// if the user doesn't check anything we'll show everything (as if they pressed both)
				if (checkpos.isChecked() && !checkneg.isChecked())
					f.updatePosts(true, false);
				else if (!checkpos.isChecked() && checkneg.isChecked())
					f.updatePosts(false, true);
				else
					f.updatePosts(true, true);
				
				postsDialog.dismiss();
			}});
		
		filterCancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				postsDialog.cancel();
			}});
	
		
	}

	public void ShowNewGoalDialog(View v){
		
		newGoalDialog = new Dialog(MainActivity.this);
		newGoalDialog.setContentView(R.layout.newgoaldialog);
		newGoalDialog.setTitle("Opret nyt mål");
		newGoalDialog.show();
		newGoalNameE = (EditText)newGoalDialog.findViewById(R.id.newGoalNameE);
		newGoalAmountE= (EditText)newGoalDialog.findViewById(R.id.newGoalAmountE);
		newGoalAmountMonthE = (EditText)newGoalDialog.findViewById(R.id.newGoalAmountMonthE);
		opretMålButton = (Button)newGoalDialog.findViewById(R.id.opretMÃ¥lButton);
		cancelNytMålButton= (Button)newGoalDialog.findViewById(R.id.cancelNytMÃ¥lButton);
		
		opretMålButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GoalFrag g = (GoalFrag) getFragmentManager().findFragmentById(R.id.FragmentContainer);
				// if the user doesn't check anything we'll show everything (as if they pressed both)
				g.setNewGoal(newGoalNameE.getText().toString(), Integer.parseInt(newGoalAmountE.getText().toString()));
				
				newGoalDialog.dismiss();
			}});
		
		cancelNytMålButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				newGoalDialog.cancel();
			}});
	
		
	}
	
	
	
	/* We are not, as of yet, using the options menu.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 */
}
