package com.example.budgetgame;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.budgetgame.db.DBAdapter;
import com.example.budgetgame.frags.AchievementFrag;
import com.example.budgetgame.frags.GoalFrag;
import com.example.budgetgame.frags.OverviewFrag;
import com.example.budgetgame.frags.PostsFrag;
import com.example.budgetgame.frags.SettingFrag;
import com.example.external.ServerController;

public class MainActivity extends Activity {

	DBAdapter dbAdapter;
	// Buttons
	private ImageButton homeButton;
	private ImageButton postsButton;
	private ImageButton goalsButton;
	private ImageButton settingsButton;
	private ImageButton achievementsButton;
	
	// Filter Controls
	private Button filterOK;
	private Button filterCancel;
	private CheckBox checkpos;
	private CheckBox checkneg;
	private Dialog postsDialog;
	
	
	// awardControls
	private Dialog awardDialog;
	private TextView awardTitle;
	private TextView awardDesc;
	private ImageView awardAchievedImage;
	private Button awardOkButton;
	static final String TITLE = "titel";
	static final String DESC = "beskrivelse";
	static final String ACHIEVED = "klaret";
	
	//New goal controls
	private Dialog newGoalDialog;
	private Button acceptGoalButton;
	private Button cancelNewGoalButton;
	private EditText newGoalNameE;
	private EditText newGoalAmountE;
	private EditText newGoalAmountMonthE;
	
	//Edit goal controls
	private Dialog editGoalDialog;
	private TextView goalTitle;
	private TextView dateText;
	private EditText goalSum;
	private EditText savedCurrent;
	private EditText savePerMonth;
	private EditText putAsideSum;
	private ProgressBar progress;
	private Button deleteButton;
	private Button saveButton;
	
	
	// Fragments
	PostsFrag postfrag = new PostsFrag();
	OverviewFrag overfrag = new OverviewFrag();
	GoalFrag goalfrag = new GoalFrag();
	SettingFrag settingfrag = new SettingFrag();
	AchievementFrag achievefrag = new AchievementFrag();
	
	public static final int FRAGMENT_HOME = 1;
	public static final int FRAGMENT_GOALS = 2;
	public static final int FRAGMENT_SETTINGS = 3;
	public static final int FRAGMENT_POSTS = 4;
	public static final int FRAGMENT_ACHIEVEMENTS = 5;
	
	//Layout width
	private int layoutWidth=320;
	ServerController controller;
	onTaskCompleted listener;
	String currentUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = getIntent();
		currentUser = intent.getExtras().getString("userName");
		
		// Button initializers
		homeButton = (ImageButton) findViewById(R.id.homeButton);
		postsButton = (ImageButton) findViewById(R.id.postsButton);
		goalsButton = (ImageButton) findViewById(R.id.goalsButton);
		settingsButton = (ImageButton) findViewById(R.id.settingsButton);
		achievementsButton = (ImageButton) findViewById(R.id.achievementButton);
	
		// Initial adding of the overview fragment.
		if (savedInstanceState == null)
		{
			changeFragment(FRAGMENT_HOME); 	
		}
		controller = new ServerController();
	
		listener = new onTaskCompleted() {
			
			@Override
			public void getSaldoTaskCompleted(double saldo) {
				System.out.println("Saldo: "+saldo);
				
			}
			
			@Override
			public void getLogInTaskCompleted(boolean login) {
				System.out.println("Login: "+login);
				
			}
		};
		//controller.getSaldoForUser(this, "test1", listener);
		//controller.syncPosts(this, "test2");
		//controller.logIn(this, "test1", "646464", listener);
					
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
		
		achievementsButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				changeFragment(FRAGMENT_ACHIEVEMENTS);
				
			}
		});

		
	}
	
	public void makeToast(String toast){ Toast.makeText(this, toast, Toast.LENGTH_SHORT).show(); }
	
	
	public void changeFragment(int fragment){
		
		setActiveFragment(fragment);
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		//getResources().getColor(R.color.idname);
		if (fragment == FRAGMENT_HOME) ft.replace(R.id.FragmentContainer, overfrag);
		
		else if (fragment == FRAGMENT_POSTS) ft.replace(R.id.FragmentContainer, postfrag);
		
		else if (fragment == FRAGMENT_GOALS) ft.replace(R.id.FragmentContainer, goalfrag);
		
		else if (fragment == FRAGMENT_SETTINGS) ft.replace(R.id.FragmentContainer, settingfrag);
		
		else if (fragment == FRAGMENT_ACHIEVEMENTS) ft.replace(R.id.FragmentContainer, achievefrag);
			
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
		
		if (fragment == FRAGMENT_ACHIEVEMENTS) achievementsButton.setBackgroundColor(getResources().getColor(R.color.darkGreen));
		else achievementsButton.setBackgroundColor(getResources().getColor(R.color.lightGreen));
	}

	public void ShowEditGoalDialog(Cursor goal){
		editGoalDialog = new Dialog(MainActivity.this);
		editGoalDialog.setContentView(R.layout.editgoaldialog);
		editGoalDialog.setTitle("Mål");
		editGoalDialog.show();

		// Cursor fields
		goal.moveToFirst();
		String title = goal.getString(1);
		float goalFloat = goal.getFloat(3);
		float current = goal.getFloat(2);
		float perMonth = goal.getFloat(4);
		String dateCreated = goal.getString(5);
		final int goalId = goal.getInt(0);
		
		// Initialize views
		goalTitle = (TextView) editGoalDialog.findViewById(R.id.editGoalDialogTitle);
		goalSum = (EditText) editGoalDialog.findViewById(R.id.editGoalDialogGoal);
		savedCurrent = (EditText) editGoalDialog.findViewById(R.id.editGoalDialogSaved);
		savePerMonth = (EditText) editGoalDialog.findViewById(R.id.editGoalDialogSaving);
		putAsideSum = (EditText) editGoalDialog.findViewById(R.id.editGoalDialogPutAside);
		progress = (ProgressBar) editGoalDialog.findViewById(R.id.editGoalDialogProgress);
		deleteButton = (Button) editGoalDialog.findViewById(R.id.editGoalDialogDeleteButton);
		saveButton = (Button) editGoalDialog.findViewById(R.id.editGoalDialogSaveButton);
		dateText = (TextView) editGoalDialog.findViewById(R.id.editGoalDialogDate);
	
		// Set view Parameters
		goalTitle.setText(title);
		goalSum.setText(""+goalFloat);
		savedCurrent.setText(""+current);
		savedCurrent.setEnabled(false);
		savePerMonth.setText(""+perMonth);
		progress.setMax((int) goalFloat);
		progress.setProgress((int) current);
		dateText.setText("Oprettet d. " + dateCreated);
		
		// OnClickListenes
		deleteButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TEST KEWIN
				dbAdapter.deleteGoal(goalId);
				editGoalDialog.dismiss();
				goalfrag.initGoals();
			}
		});
		
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// DO THIS KEWIN
				//dbAdapter.updateGoal(goal)
			}
		});
		
		
	}
	
	public void ShowAwardDialog(ContentValues award){
		awardDialog = new Dialog(MainActivity.this);
		awardDialog.setContentView(R.layout.achievementdialog);
		awardDialog.setTitle("Medaljebeskrivelse");
		awardDialog.show();
		awardAchievedImage = (ImageView)awardDialog.findViewById(R.id.awardDialogImage);
		awardTitle = (TextView)awardDialog.findViewById(R.id.awardDialogTitleView);
		awardDesc = (TextView)awardDialog.findViewById(R.id.awardDialogDesc);
		awardOkButton = (Button)awardDialog.findViewById(R.id.awardDialogOkButton);
		
		awardTitle.setText(award.getAsString(TITLE));
		awardDesc.setText(award.getAsString(DESC));
		if (award.getAsInteger(ACHIEVED)==1) awardAchievedImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_yes));
		
		awardOkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				awardDialog.dismiss();
				
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
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		return true;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		//dbAdapter.close();
	}
	 
}
