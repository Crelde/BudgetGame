package com.example.budgetgame;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
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
import com.example.budgetgame.frags.SettingsFrag;
import com.example.external.ServerController;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;

/**
 * @author Kewin & Christian
 * @summary Main Activity that holds all content that the user sees and interacts with while logged in.
 */
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
	
	// Award Controls
	private Dialog awardDialog;
	private TextView awardTitle;
	private TextView awardDesc;
	private ImageView awardAchievedImage;
	private Button awardOkButton;
	static final String TITLE = "titel";
	static final String DESC = "beskrivelse";
	static final String ACHIEVED = "klaret";
	
	// New goal controls
	private Dialog newGoalDialog;
	private Button acceptGoalButton;
	private Button cancelNewGoalButton;
	private EditText newGoalNameE;
	private EditText newGoalAmountE;
	private EditText newGoalAmountMonthE;
	
	// Edit goal controls
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
	
	//Show post controls
	private Dialog showPostDialog;
	private TextView postDesc;
	private TextView postAmount;
	private TextView postDate;
	private Button facebookSharePost;
	private Button closePostDialog;
	
	// Fragments
	PostsFrag postfrag = new PostsFrag();
	OverviewFrag overfrag = new OverviewFrag();
	GoalFrag goalfrag = new GoalFrag();
	SettingsFrag settingfrag = new SettingsFrag(); 
	AchievementFrag achievefrag = new AchievementFrag();
	
	MainActivity self = this;
	
	// Interger vaues representing each fragment
	public static final int FRAGMENT_HOME = 1;
	public static final int FRAGMENT_GOALS = 2;
	public static final int FRAGMENT_SETTINGS = 3;
	public static final int FRAGMENT_POSTS = 4;
	public static final int FRAGMENT_ACHIEVEMENTS = 5;
	
	// Server communication
	public ServerController controller;
	public String currentUser;
	
	// Facebook stuff
	public UiLifecycleHelper uiHelper;	
	private Session.StatusCallback callback = new Session.StatusCallback() {
	    @Override
	    public void call(Session session, SessionState state, Exception exception) {
	        onSessionStateChange(session, state, exception);
	    }
	};
	
	@Override
	public void onResume() {
	    super.onResume();
	    // Facebook
	    // For scenarios where the main activity is launched and user
	    // session is not null, the session state change notification
	    // may not be triggered. Trigger it if it's open/closed.
	    Session session = Session.getActiveSession();
	    if (session != null &&
	           (session.isOpened() || session.isClosed()) ) {
	        onSessionStateChange(session, session.getState(), null);
	    }

	    uiHelper.onResume();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		// Facebook
		 uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
		        @Override
		        public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
		            Log.e("Activity", String.format("Error: %s", error.toString()));
		        }

		        @Override
		        public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
		            Log.i("Activity", "Success!");
		        }
		    });
	}
	
	@Override
	public void onPause() {
	    super.onPause();
	    // Facebook
	    uiHelper.onPause();
	}
	
	// Disables navigation buttons
	public void DisableNavigation(){
		homeButton.setEnabled(false);
		postsButton.setEnabled(false);
		goalsButton.setEnabled(false);
		settingsButton.setEnabled(false);
		achievementsButton.setEnabled(false);
	}
	
	// Enables navigation buttons
	public void EnableNavigation(){
		homeButton.setEnabled(true);
		postsButton.setEnabled(true);
		goalsButton.setEnabled(true);
		settingsButton.setEnabled(true);
		achievementsButton.setEnabled(true);
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		// Intiliaze view
		setContentView(R.layout.activity_main);
		// Get user from LogIn Activity
		Intent intent = getIntent();
		currentUser = intent.getExtras().getString("userName");
		
		// Intiliaze database adapter
		dbAdapter = new DBAdapter(this);
		dbAdapter.open();
		
		// Initialize server controller
		controller = new ServerController();
		
		// Facebook 
		uiHelper = new UiLifecycleHelper(this, callback);
	    uiHelper.onCreate(savedInstanceState);
		
		
		// Button initializers
		homeButton = (ImageButton) findViewById(R.id.homeButton);
		postsButton = (ImageButton) findViewById(R.id.postsButton);
		goalsButton = (ImageButton) findViewById(R.id.goalsButton);
		settingsButton = (ImageButton) findViewById(R.id.settingsButton);
		achievementsButton = (ImageButton) findViewById(R.id.achievementButton);
		
		
		
		
		// Initial adding of the overview fragment, if no other fragment was present.
		if (savedInstanceState == null)
		{
			changeFragment(FRAGMENT_HOME); 	
		}
		
		
					
		// Implementations of navigation onclicks so they change between the fragments
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
	
	// Facebook debugging
	private void onSessionStateChange(Session session, SessionState state, Exception exception) {
	    if (state.isOpened()) {
	        Log.i("Facebook", "Logged in...");
	    } else if (state.isClosed()) {
	        Log.i("Facebook", "Logged out...");
	    }
	}
	
	// Convenience method for making toasts.
	public void makeToast(String toast){ Toast.makeText(this, toast, Toast.LENGTH_SHORT).show(); }
		
	// Changes fragment when a navigation button is pressed.
	public void changeFragment(int fragment){
		
		setActiveFragment(fragment);
		
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		
		if (fragment == FRAGMENT_HOME) ft.replace(R.id.FragmentContainer, overfrag);
		
		else if (fragment == FRAGMENT_POSTS) ft.replace(R.id.FragmentContainer, postfrag);
		
		else if (fragment == FRAGMENT_GOALS) ft.replace(R.id.FragmentContainer, goalfrag);
		
		else if (fragment == FRAGMENT_SETTINGS) ft.replace(R.id.FragmentContainer, settingfrag);
		
		else if (fragment == FRAGMENT_ACHIEVEMENTS) ft.replace(R.id.FragmentContainer, achievefrag);
			
		else { /* Error situation*/ }
		
		
		ft.commit();
	}
	
	// Handles showing which fragment is currently active on the navigation buttons.
	public void setActiveFragment(int fragment){
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		
		String themeColor = prefs.getString("theme", "Green");
		
		int activeColor = getResources().getColor(R.color.darkGreen);
		int inactiveColor = getResources().getColor(R.color.lightGreen);
				
		if (themeColor.compareTo("Green")!=0){
			if (themeColor.compareTo("Blue")==0){
				activeColor = getResources().getColor(R.color.darkBlue);
				inactiveColor = getResources().getColor(R.color.blue_mine);
			}
			else if (themeColor.compareTo("Red")==0){
				activeColor = getResources().getColor(R.color.darkRed);
				inactiveColor = getResources().getColor(R.color.red_mine);
			}						
		}
		
		
		
		if (fragment == FRAGMENT_HOME) homeButton.setBackgroundColor(activeColor);
		else homeButton.setBackgroundColor(inactiveColor);
		
		if (fragment == FRAGMENT_POSTS) postsButton.setBackgroundColor(activeColor);
		else postsButton.setBackgroundColor(inactiveColor);
		
		if (fragment == FRAGMENT_GOALS) goalsButton.setBackgroundColor(activeColor);
		else goalsButton.setBackgroundColor(inactiveColor);
		
		if (fragment == FRAGMENT_SETTINGS) settingsButton.setBackgroundColor(activeColor);
		else settingsButton.setBackgroundColor(inactiveColor);
		
		if (fragment == FRAGMENT_ACHIEVEMENTS) achievementsButton.setBackgroundColor(activeColor);
		else achievementsButton.setBackgroundColor(inactiveColor);
	}

	// Shows the dialog for viewing and editting a goal.
	public void ShowEditGoalDialog(final Cursor goal){
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
				GoalFrag g = (GoalFrag) getFragmentManager().findFragmentById(R.id.FragmentContainer);
				dbAdapter.deleteGoal(goalId);
				editGoalDialog.dismiss();
				goal.close();
				goalfrag.initGoals();
				g.setStandardAlarmForGoal(getApplicationContext(),goalId);
			}
		});
				
		saveButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Editable goalSumE = goalSum.getText();
				String goalValueString = goalSumE.toString();
				
				Editable savePerMonthE = savePerMonth.getText();
				String saveMonthlyString = savePerMonthE.toString();
				
				Editable putAsideE = putAsideSum.getText();
				String putAsideString = putAsideE.toString();
				boolean success = false;
				if (goalValueString.compareTo("")!=0 && saveMonthlyString.compareTo("")!=0 && !(Float.valueOf(goalValueString)==0))
				{					
					
					final ContentValues newGoal = new ContentValues();
											
					newGoal.put("beloebMål", Float.valueOf(goalValueString));
					newGoal.put("toSavePerMonth", Float.valueOf(saveMonthlyString));
					
					int extra = 0;
					
					try{
						extra = Integer.valueOf(putAsideString);
					}
					catch(NumberFormatException e){
						// expected
						System.out.println("exception");
					}
			
					dbAdapter.updateGoal(newGoal, goalId, extra);
					editGoalDialog.dismiss();
					goal.close();
					goalfrag.initGoals();
					success = true;
					
				}
				if (!success){
					boolean zeroGoal = false;
					if (Float.valueOf(goalValueString)==0) zeroGoal=true;
					if (!zeroGoal) makeToast("Du skal udfylde både samlet mål og beløb der skal sættes til side!");
					else makeToast("Målet må ikke være 0 kr!");
				}
							
			}
		});
		
		
	}
	// Shows the dialog for viewing a post.
	public void ShowPostDialog(int postId){
		showPostDialog = new Dialog(MainActivity.this);
		showPostDialog.setContentView(R.layout.showpostdialog);
		showPostDialog.setTitle("Postering");
		showPostDialog.show();
		
		// get Post
		Cursor post = dbAdapter.getPost(postId);
		
		//Initialize views
		postDesc = (TextView) showPostDialog.findViewById(R.id.showPostDialogDesc);
		postAmount = (TextView) showPostDialog.findViewById(R.id.showPostDialogAmount);
		postDate = (TextView) showPostDialog.findViewById(R.id.showPostDialogDate);
		facebookSharePost = (Button) showPostDialog.findViewById(R.id.showPostDialogFacebookButton);
		closePostDialog = (Button) showPostDialog.findViewById(R.id.showPostDialogCloseButton);
		
		// Cursor fields
		post.moveToFirst();		
		final String desc = post.getString(0);
		final String date = post.getString(1);
		final float amount = post.getFloat(2);
		
		// Set fields in dialog
		postDesc.setText(desc);
		postDate.setText(date);
		postAmount.setText(""+amount);
		
		// onClick listeners
		closePostDialog.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				showPostDialog.dismiss();
			}
		});
		
		// Facebook share button
		facebookSharePost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showPostDialog.dismiss();
				// Creates a facebook shareDialog which will share a link with title and subtitle defined by us.
				String title;
				if (amount<0) title = "Jeg har d. " + date + " brugt " + -amount + " kr.!";
				else title = "Jeg har d. " + date + " fået " + amount + " kr. ind på kontoen!";
				FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(self)
					.setLink("https://developers.facebook.com/android")
			        .setCaption("Jeg bruger BudgetHelper til at hjælpe med at holde styr på økonomien, du skulle prøve!")
					.setName(title)
			        .build();
				uiHelper.trackPendingDialogCall(shareDialog.present());
				
			}
		});
	
	}
	
	// Shows the dialog for viewing an award.
	public void ShowAwardDialog(ContentValues award){
		awardDialog = new Dialog(MainActivity.this);
		awardDialog.setContentView(R.layout.achievementdialog);
		awardDialog.setTitle("Medaljebeskrivelse");
		awardDialog.show();
		
		// Intialize views
		awardAchievedImage = (ImageView)awardDialog.findViewById(R.id.awardDialogImage);
		awardTitle = (TextView)awardDialog.findViewById(R.id.awardDialogTitleView);
		awardDesc = (TextView)awardDialog.findViewById(R.id.awardDialogDesc);
		awardOkButton = (Button)awardDialog.findViewById(R.id.awardDialogOkButton);
		
		// Set view fields
		awardTitle.setText(award.getAsString(TITLE));
		awardDesc.setText(award.getAsString(DESC));
		if (award.getAsInteger(ACHIEVED)==1) awardAchievedImage.setImageDrawable(getResources().getDrawable(R.drawable.icon_yes));
		
		// onClick Listener
		awardOkButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				awardDialog.dismiss();
				
			}
		});
	}

	// Shows the dialog to define a filter for viewing posts
	public void ShowFilterDialog(View v){		
		postsDialog = new Dialog(MainActivity.this);
		postsDialog.setContentView(R.layout.filterdialog);
		postsDialog.setTitle("Vælg Filtre");
		postsDialog.show();
		
		// Intialize views
		checkpos = (CheckBox)postsDialog.findViewById(R.id.checkpos);
		checkneg = (CheckBox)postsDialog.findViewById(R.id.checkneg);
		filterOK = (Button)postsDialog.findViewById(R.id.filterOK);
		filterCancel = (Button)postsDialog.findViewById(R.id.filterCancel);
		
		// onClick Listeners, 
		filterOK.setOnClickListener(new View.OnClickListener() {
			
			// Shows the user the posts the user wishes to see, as defined by their filter.
			@Override
			public void onClick(View v) {
				PostsFrag f = (PostsFrag) getFragmentManager().findFragmentById(R.id.FragmentContainer);
				// If the user doesn't check anything we'll show everything (as if they pressed both)
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

	// Shows the dialog for creating a new goal.
	public void ShowNewGoalDialog(View v){	
		newGoalDialog = new Dialog(MainActivity.this);
		newGoalDialog.setContentView(R.layout.newgoaldialog);
		newGoalDialog.setTitle("Opret nyt mål");
		newGoalDialog.show();
		
		// Intialize views
		newGoalNameE = (EditText)newGoalDialog.findViewById(R.id.newGoalDialogNameE);
		newGoalAmountE= (EditText)newGoalDialog.findViewById(R.id.newGoalDialogAmountE);
		newGoalAmountMonthE = (EditText)newGoalDialog.findViewById(R.id.newGoalDialogAmountMonthE);
		acceptGoalButton = (Button)newGoalDialog.findViewById(R.id.acceptnewGoalDialogButton);
		cancelNewGoalButton= (Button)newGoalDialog.findViewById(R.id.cancelnewGoalDialogButton);
		//newGoalDialog.setCancelable(false);
		
		// onClick Listeners
		acceptGoalButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				GoalFrag g = (GoalFrag) getFragmentManager().findFragmentById(R.id.FragmentContainer);
				
				// Make sure fields are all filled.
				if(newGoalNameE.getText().toString().trim().length() <= 0 ||
						newGoalAmountE.getText().toString().trim().length() <= 0 || 
							newGoalAmountMonthE.getText().toString().trim().length() <= 0){		
					makeToast("Udfyld venligst alle felter!");		
				}
				
				 
				// Make sure input is not over max int.
				try{
					// Make sure goal isnt 0
					if(Integer.valueOf(newGoalAmountE.getText().toString())==0) makeToast("Målet kan ikke være 0!");
					else {
						int id = (int) g.setNewGoal(newGoalNameE.getText().toString(), Integer.parseInt(newGoalAmountE.getText().toString()), Integer.parseInt(newGoalAmountMonthE.getText().toString()));
						g.setStandardAlarmForGoal(getApplicationContext(),id);
						newGoalDialog.dismiss();
					}
				}
					catch(NumberFormatException e){
						makeToast("Tallet er for højt!");
					}	
			}});
		
		cancelNewGoalButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				newGoalDialog.cancel();
			}});		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// Facebook
		uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		dbAdapter.close();
		// Facebook
		uiHelper.onDestroy();
	}
	 
}

