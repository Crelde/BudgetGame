package com.example.budgetgame;

import com.example.budgetgame.db.DBAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class SavingsAlarmReceiver extends BroadcastReceiver {

	DBAdapter dbAdapter;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "OnReceive start", Toast.LENGTH_LONG).show();
		
		Bundle extras = intent.getExtras();
		
		long goalId = extras.getLong("goalId");	
		
		try {
			dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			dbAdapter.putAsideMoney(goalId); 			
			Toast.makeText(context, "Modtog intent og puAsideMoney kaldt", Toast.LENGTH_LONG).show();
		}
		finally {
			dbAdapter.close();
		}
		
		

	}

}
