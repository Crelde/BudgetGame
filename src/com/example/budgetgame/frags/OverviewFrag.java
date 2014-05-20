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
		
		activity.DisableNavigation();
		
		activity.setTitle("Overblik");
		
		adapter.open();
		
		final TextView saldoView = (TextView) activity.findViewById(R.id.currValue);
		final TextView availableView = (TextView) activity.findViewById(R.id.availableValue);
		final TextView avialablePerDayView = (TextView) activity.findViewById(R.id.comingValue);
		final TextView daysUntillPayDayView = (TextView) activity.findViewById(R.id.payDayValue);
		
		
		
		onTaskCompleted task = new onTaskCompleted() {
			
			@Override
			public void getSaldoTaskCompleted(double saldo) {
				activity.findViewById(R.id.overViewProgressBar).setVisibility(8);
				Cursor goals = adapter.getAllGoals();
				
				float totalAmountSavedUp = 0;
				
				
				if (goals.getCount()!=0){
					//goals.moveToFirst();
					//boolean atLastPosition = false;
					while (goals.moveToNext()){
						
						totalAmountSavedUp += goals.getFloat(2);
						
						//goals.
						//if (goals.isLast()) atLastPosition = true;
					}
					//goals.
					//goals.close();
				}				
				
				Calendar date = Calendar.getInstance();
				Calendar payDayDate = Calendar.getInstance();	
				int day = date.get(Calendar.DATE);
				int month = date.get(Calendar.MONTH)+1;
				int year = date.get(Calendar.YEAR);
				
				date.clear();						
				date.set(year, month, day);	
				payDayDate.clear();						
				payDayDate.set(year, month, day);	
								
				payDayDate.add(Calendar.MONTH, 1);
				payDayDate.set(Calendar.DATE, 1);
				
				long dateLong = date.getTimeInMillis();
				long payDayLong = payDayDate.getTimeInMillis();
				
				long difference = payDayLong - dateLong;
				//Calendar
				int daysTillPayDay = (int) difference / (24 * 60 * 60 * 1000);
				
				String saldoString = round((float) saldo, 2).toString()+" kr.";
				
				BigDecimal availableMoneyBD = round((float) saldo-totalAmountSavedUp, 2);
				
				double availableMoney = Double.valueOf(availableMoneyBD.toString());
				
				String availableString = availableMoney +" kr.";

				double moneyPerDayUntilPayDay = availableMoney / daysTillPayDay;
				
				String perDayFutureString= round((float) moneyPerDayUntilPayDay, 2).toString()+" kr.";
					
				
				saldoView.setText(saldoString);
				availableView.setText(availableString);
				avialablePerDayView.setText(perDayFutureString);
				daysUntillPayDayView.setText(daysTillPayDay+ " dage.");
				
				activity.EnableNavigation();
								
			}
			
			@Override
			public void getLogInTaskCompleted(boolean login) {
				// Not relevant				
			}
		};
		activity.controller.getSaldoForUser(activity, activity.currentUser, task);	
		//ServerController controller = new ServerController();
		//controller.getSaldoForUser(getActivity(), username, listener)
		//adapter.close();
	}
	public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);       
        return bd;
    }


}
