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

public class LogInActivity extends Activity {
	

	private Button loginButton;
	private ProgressBar loginSpinner;
	private EditText userNameEdit;
	private EditText passWordEdit;
	private Context ctx;
	private Dialog failedLoginDialog;
	private Button loginOK;
	onTaskCompleted listener;
	ServerController controller;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		ctx = this;
		loginButton = (Button) findViewById(R.id.loginButton);
		loginSpinner = (ProgressBar) findViewById(R.id.loginSpinner);
		userNameEdit = (EditText) findViewById(R.id.userNameEdit);
		passWordEdit = (EditText) findViewById(R.id.userPasswordEdit);
		
		controller = new ServerController();
		listener = new onTaskCompleted() {
			
			@Override
			public void getSaldoTaskCompleted(double saldo) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void getLogInTaskCompleted(boolean login) {
				loginSpinner.setVisibility(8);
				userNameEdit.setEnabled(true);
				passWordEdit.setEnabled(true);
				loginButton.setEnabled(true);
				if(login)
				{
					Intent intent = new Intent(ctx, MainActivity.class);
					intent.putExtra("userName", userNameEdit.getText().toString());
					startActivity(intent);
				}
				else{
					
					showFailedLoginDialog();
				}
				
			}
		};
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String uName = userNameEdit.getText().toString();
				String pw = passWordEdit.getText().toString();
				userNameEdit.setEnabled(false);
				passWordEdit.setEnabled(false);
				loginButton.setEnabled(false);
				
				loginSpinner.setVisibility(0);
				controller.logIn(ctx, uName, pw, listener);
				
				
			
			}
		});
	}


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
			}});

	}
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
