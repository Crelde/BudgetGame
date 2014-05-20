package com.example.budgetgame;

import com.example.budgetgame.db.DBAdapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * @author Kewin & Christian
 * @summary BroadCast Receiver that sets aside money for goals when received.
 */
public class SavingsAlarmReceiver extends BroadcastReceiver {

	DBAdapter dbAdapter;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		Toast.makeText(context, "OnReceive start", Toast.LENGTH_LONG).show();
		
		Bundle extras = intent.getExtras();
		
		int goalId = extras.getInt("goalId");	
		
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
