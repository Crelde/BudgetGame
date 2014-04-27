package com.example.budgetgame;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.budgetgame.db.DBAdapter;
import com.example.budgetgame.frags.GoalFrag;
import com.example.budgetgame.frags.OverviewFrag;
import com.example.budgetgame.frags.PostsFrag;
import com.example.budgetgame.frags.SettingFrag;

public class MainActivity extends Activity {

	private String posttag = "POST_TAG";
	DBAdapter dbAdapter;
	// Buttons
	private ImageButton homeButton;
	private ImageButton postsButton;
	private ImageButton goalsButton;
	private ImageButton settingsButton;
	
	// Filter Controls
	private Button filterOK;
	private Button filterCancel;
	private CheckBox checkpos;
	private CheckBox checkneg;
	private Dialog postsDialog;
	private Dialog newGoalDialog;
	
	//New goal controls
	private Button acceptGoalButton;
	private Button cancelNewGoalButton;
	private EditText newGoalNameE;
	private EditText newGoalAmountE;
	private EditText newGoalAmountMonthE;
	
	
	// Fragments
	PostsFrag postfrag = new PostsFrag();
	OverviewFrag overfrag = new OverviewFrag();
	GoalFrag goalfrag = new GoalFrag();
	SettingFrag settingfrag = new SettingFrag();
	
	public static final int FRAGMENT_HOME = 1;
	public static final int FRAGMENT_GOALS = 2;
	public static final int FRAGMENT_SETTINGS = 3;
	public static final int FRAGMENT_POSTS = 4;
	
	//Layout width
	private int layoutWidth=320;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Button initializers
		homeButton = (ImageButton) findViewById(R.id.homeButton);
		postsButton = (ImageButton) findViewById(R.id.postsButton);
		goalsButton = (ImageButton) findViewById(R.id.goalsButton);
		settingsButton = (ImageButton) findViewById(R.id.settingsButton);
	
		// Initial adding of the overview fragment.
		if (savedInstanceState == null)
		{
			changeFragment(FRAGMENT_HOME); 	
		}
		
		
					
		// Implementations of button onclicks so they change between the fragments
		homeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeFragment(FRAGMENT_HOME);
			}
		});
		
		postsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeFragment(FRAGMENT_POSTS);
			}
		});
		
		goalsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeFragment(FRAGMENT_GOALS);	
			}
		});
		
		settingsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				changeFragment(FRAGMENT_SETTINGS);	
			}
		});

		
	}
	
	public void makeToast(String toast){ Toast.makeText(this, toast, Toast.LENGTH_SHORT).show(); }
	
	private void defineLayoutWidth(int width){
		layoutWidth = width;
	}
	
	
	public void changeFragment(int fragment){
		
		setActiveFragment(fragment);
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		//getResources().getColor(R.color.idname);
		if (fragment == FRAGMENT_HOME) ft.replace(R.id.FragmentContainer, overfrag);
		
		else if (fragment == FRAGMENT_POSTS) ft.replace(R.id.FragmentContainer, postfrag);
		
		else if (fragment == FRAGMENT_GOALS) ft.replace(R.id.FragmentContainer, goalfrag);
		
		else if (fragment == FRAGMENT_SETTINGS) ft.replace(R.id.FragmentContainer, settingfrag);
			
		else { /* Error situation*/ }
		
		
		ft.commit();
	}
	
	public void setActiveFragment(int fragment){
		
		if (fragment == FRAGMENT_HOME) homeButton.setBackgroundColor(getResources().getColor(R.color.darkGreen));
		else homeButton.setBackgroundColor(getResources().getColor(R.color.lightGreen));
		
		if (fragment == FRAGMENT_POSTS) postsButton.setBackgroundColor(getResources().getColor(R.color.darkGreen));
		else postsButton.setBackgroundColor(getResources().getColor(R.color.lightGreen));
		
		if (fragment == FRAGMENT_GOALS) goalsButton.setBackgroundColor(getResources().getColor(R.color.darkGreen));
		else goalsButton.setBackgroundColor(getResources().getColor(R.color.lightGreen));
		
		if (fragment == FRAGMENT_SETTINGS) settingsButton.setBackgroundColor(getResources().getColor(R.color.darkGreen));
		else settingsButton.setBackgroundColor(getResources().getColor(R.color.lightGreen));
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
		acceptGoalButton = (Button)newGoalDialog.findViewById(R.id.acceptNewGoalButton);
		cancelNewGoalButton= (Button)newGoalDialog.findViewById(R.id.cancelNewGoalButton);
		newGoalDialog.setCancelable(false);
		acceptGoalButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				GoalFrag g = (GoalFrag) getFragmentManager().findFragmentById(R.id.FragmentContainer);
				// if the user doesn't check anything we'll show everything (as if they pressed both)
			
				if(newGoalNameE.getText().toString().trim().length() <= 0 ||
						newGoalAmountE.getText().toString().trim().length() <= 0 || 
							newGoalAmountMonthE.getText().toString().trim().length() <= 0){		
					Toast.makeText(getApplicationContext(), "Udfyld venligst alle felter!", Toast.LENGTH_SHORT).show();		
				}
				else {				
					long id = g.setNewGoal(newGoalNameE.getText().toString(), Integer.parseInt(newGoalAmountE.getText().toString()), Integer.parseInt(newGoalAmountMonthE.getText().toString()));
					g.setStandardAlarmForGoal(getApplicationContext(),id);
					newGoalDialog.dismiss();
				}	
			}});
		
		cancelNewGoalButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				newGoalDialog.cancel();
			}});
	
		
	}
	
	
	
	/* 
	 * 		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		Intent intent = new Intent(this, AlarmReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
		
	}

	public void setAlarm(View view) {
		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + FIVE_SECONDS, pendingIntent);
	}*/
	
	
	/* We are not, as of yet, using the options menu.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	 */
}
