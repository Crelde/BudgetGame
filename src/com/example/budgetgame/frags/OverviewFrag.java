package com.example.budgetgame.frags;

import java.math.BigDecimal;
import java.util.Calendar;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;
import com.example.budgetgame.onTaskCompleted;
import com.example.budgetgame.db.DBAdapter;

/**
 * @author Kewin & Christian
 * @summary Fragment that displays the economical overview to the user.
 * 
 */
public class OverviewFrag extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.overview, container, false);
		
		((MainActivity) getActivity()).setActiveFragment(MainActivity.FRAGMENT_HOME);
		return v;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		final MainActivity activity = (MainActivity) getActivity();
		final DBAdapter adapter = new DBAdapter(activity);
		
		// Disable navigation while fragment is loading, to prevent the user from removing 
		// views that are about to be updated.	
		activity.DisableNavigation();
		
		activity.setTitle("Overblik");
		
		adapter.open();
		
		// Initialze views
		final TextView saldoView = (TextView) activity.findViewById(R.id.currValue);
		final TextView availableView = (TextView) activity.findViewById(R.id.availableValue);
		final TextView avialablePerDayView = (TextView) activity.findViewById(R.id.comingValue);
		final TextView daysUntillPayDayView = (TextView) activity.findViewById(R.id.payDayValue);
		
		
		// Implementation of onTaskCompleted interface
		onTaskCompleted task = new onTaskCompleted() {
			
			@Override
			public void getSaldoTaskCompleted(double saldo) {
				// When we get the saldo from the webservice, we can update the interface.
				activity.findViewById(R.id.overViewProgressBar).setVisibility(8);
				Cursor goals = adapter.getAllGoals();
				
				float totalAmountSavedUp = 0;
							
				// Add together the total amount of money saved up on the users goals.
				if (goals.getCount()!=0){
					while (goals.moveToNext()){ totalAmountSavedUp += goals.getFloat(2); }
				}				
				
				// Get the current date, remember its values.
				Calendar date = Calendar.getInstance();
				Calendar payDayDate = Calendar.getInstance();	
				int day = date.get(Calendar.DATE);
				int month = date.get(Calendar.MONTH)+1;
				int year = date.get(Calendar.YEAR);
				
				// Set the current date, as well as the date for the next pay day
				date.clear();						
				date.set(year, month, day);	
				payDayDate.clear();						
				payDayDate.set(year, month, day);									
				payDayDate.add(Calendar.MONTH, 1);
				payDayDate.set(Calendar.DATE, 1);
				
				// Get dates as longs
				long dateLong = date.getTimeInMillis();
				long payDayLong = payDayDate.getTimeInMillis();
				
				// Calculate difference in milliseconds, and then in days, to find the amount of days
				// until payday
				long difference = payDayLong - dateLong;
				int daysTillPayDay = (int) difference / (24 * 60 * 60 * 1000);
				
				// Get saldo and available money as doubles with two decimals.
				String saldoString = round((float) saldo, 2).toString()+" kr.";				
				BigDecimal availableMoneyBD = round((float) saldo-totalAmountSavedUp, 2);
				
				double availableMoney = Double.valueOf(availableMoneyBD.toString());							
				double moneyPerDayUntilPayDay = availableMoney / daysTillPayDay;
				
				String availableString = availableMoney +" kr.";
				String perDayFutureString= round((float) moneyPerDayUntilPayDay, 2).toString()+" kr.";
					
				// Update views with calculated values.
				saldoView.setText(saldoString);
				availableView.setText(availableString);
				avialablePerDayView.setText(perDayFutureString);
				daysUntillPayDayView.setText(daysTillPayDay+ " dage.");
				
				// Allow the user to user navigation once again.
				activity.EnableNavigation();							
			}
			
			@Override
			public void getLogInTaskCompleted(boolean login) {
				// Not relevant				
			}
		};
		
		// Call ServerController function to retrieve user, with the interface we just implemented as argument. 
		activity.controller.getSaldoForUser(activity, activity.currentUser, task);	
	}
	
	// Convenience method to return BigDecimal with two decimals from a float.
	public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);       
        return bd;
    }


}
