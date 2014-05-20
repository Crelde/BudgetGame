package com.example.budgetgame;

import com.example.external.ServerController;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


/**
 * @author Kewin & Christian
 * @summary First activity the user will see, this activity holds functionality to contact server through 
 * ServerController. As well as notifying the user if login fails
 */
public class LogInActivity extends Activity {
	
	// Views
	private Button loginButton;
	private ProgressBar loginSpinner;
	private EditText userNameEdit;
	private EditText passWordEdit;
	private Dialog failedLoginDialog;
	private Button loginOK;
	
	// Other ressources
	private Context ctx;
	onTaskCompleted listener;
	ServerController controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		// Intialize ressources.
		ctx = this;
		loginButton = (Button) findViewById(R.id.loginButton);
		loginSpinner = (ProgressBar) findViewById(R.id.loginSpinner);
		userNameEdit = (EditText) findViewById(R.id.userNameEdit);
		passWordEdit = (EditText) findViewById(R.id.userPasswordEdit);		
		controller = new ServerController();
		listener = new onTaskCompleted() {			
			@Override
			public void getSaldoTaskCompleted(double saldo) {
				// Not relevant here				
			}			
			@Override
			public void getLogInTaskCompleted(boolean login) {
				// Allow the user interface to be interacted with once again.
				loginSpinner.setVisibility(4);
				userNameEdit.setEnabled(true);
				passWordEdit.setEnabled(true);
				loginButton.setEnabled(true);
				if(login)
				{
					// If user provided correct information, open MainActivity, and remember which user is now logged on.
					Intent intent = new Intent(ctx, MainActivity.class);
					intent.putExtra("userName", userNameEdit.getText().toString());
					startActivity(intent);
				}
				// If information is invalid, show failed login dialog.
				else{ showFailedLoginDialog(); }				
			}
		};
		loginButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// Disable user interface so the user cannot inject fields that might be nescsary to read from.
				userNameEdit.setEnabled(false);
				passWordEdit.setEnabled(false);
				loginButton.setEnabled(false);
				String uName = userNameEdit.getText().toString();
				String pw = passWordEdit.getText().toString();				
				// Show progress spinner
				loginSpinner.setVisibility(0);
				// Call ServerControllers login.
				controller.logIn(ctx, uName, pw, listener);	
			}
		});
	}

	// Shows failed log in dialog.
	public void showFailedLoginDialog(){		
		failedLoginDialog = new Dialog(ctx);
		failedLoginDialog.setContentView(R.layout.badlogin);
		failedLoginDialog.setTitle("Forkert Login");
		failedLoginDialog.show();	
		loginOK = (Button)failedLoginDialog.findViewById(R.id.loginOK);			
		loginOK.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) { 
				failedLoginDialog.dismiss();
			}
		});
	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
