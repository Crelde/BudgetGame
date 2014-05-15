package com.example.budgetgame.db;

import java.util.Calendar;

import com.example.budgetgame.SavingsAlarmReceiver;
import com.example.external.Post;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

public class DBAdapter {
	DBHelper helper;
	SQLiteDatabase db;
	Context context;
	
	String TABLE_POSTS = "Posteringer";
	String TABLE_GOALS = "Goals";
	String TABLE_GOALS_HISTORY = "History";
	String TABLE_ACHIEVEMENTS = "Achievement";

	public DBAdapter(Context context) {
		helper = new DBHelper(context);
		this.context = context;
		}

	public void open() {
		db = helper.getWritableDatabase();
	}

	public void close() {
		db.close();
	}


	public void insertPost(Post post){
		db.execSQL("INSERT INTO " + TABLE_POSTS + " (titel, dato, beloeb) VALUES ("+post.titel+", "+post.dato+", "+post.beloeb+")");
	}

	public Cursor getAllPosts(){
		Cursor query = db.query(TABLE_POSTS, new String[] { "_id", "titel", "dato", "beloeb" }, null, null, null, null,
				"_id DESC");
		return query;
	}
	public Cursor getNegPosts(){
		Cursor query = db.query(TABLE_POSTS, new String[] { "_id", "titel", "dato", "beloeb" }, "beloeb < 0", null, null, null,
				"_id DESC");
		return query;
	}
	
	public Cursor getPosPosts(){
		Cursor query = db.query(TABLE_POSTS, new String[] { "_id", "titel", "dato", "beloeb" }, "beloeb >= 0", null, null, null,
				"_id DESC");
		return query;
	}
	
	public Cursor getAllGoals(){
		Cursor query = db.query(TABLE_GOALS, new String[] { "_id", "titel", "beloebCurrent", "beloebMål" }, null, null, null, null,
				"_id DESC");
		return query;
	}
	
	public Cursor getAchievements(){
		Cursor query = db.query(TABLE_ACHIEVEMENTS, new String[] { "_id", "titel", "beskrivelse", "klaret" }, null, null, null, null,
				"_id DESC");
		return query;			
	}
	
	
	
	// Sets new goal and returns updated cursor 
	// Is the return used? Doesnt look like it -Kewin
	public long setNewGoal(String titel, int mål, int prMonth){
		ContentValues values = new ContentValues();
		values.put("titel", titel);
		values.put("beloebCurrent", 0);
		values.put("beloebMål", mål);
		values.put("toSavePerMonth", prMonth);
		values.put("dateCreated", "now");
		long id = db.insert(TABLE_GOALS, null, values);
		//db.execSQL("INSERT INTO " + TABLE_GOALS+ " (titel, beloebCurrent, beloebMål, toSavePerMonth, dateCreated ) VALUES ('"+titel+"', 0, "+mål+","+prMonth+","+"now"+")");
		return id;
	}
	
	
	
	public Cursor getHistory(){
		Cursor query = db.query(TABLE_GOALS_HISTORY, new String[] { "_id", "titel", "beskrivelse", "dato" }, null, null, null, null,
				"dato DESC");
		return query;
	}
	
	// Used when putting aside the standard amount of money as defined by the goal.
	public boolean putAsideMoney(long goalId){
		Cursor query = db.query(TABLE_GOALS, new String[] { "_id", "titel", "beloebCurrent", "beloebMål", "toSavePerMonth" }, "_id="+goalId, null, null, null, null, "1");
		query.moveToFirst();
		String title = query.getString(1);
		float current = query.getFloat(2);
		float goal = query.getFloat(3);
		float monthly = query.getFloat(4);
		
		return goalChangeDb(goalId, title, current, goal, monthly);
	}
	
	// Used when putting aside an amount that is not the standard amount.
	public boolean putAsideMoney(int goalId, int sum){
		Cursor query = db.query(TABLE_GOALS, new String[] { "_id", "titel", "beloebCurrent", "beloebMål", "toSavePerMonth" }, "_id="+goalId, null, null, null, null, "1");
		query.moveToFirst();
		String title = query.getString(1);
		float current = query.getFloat(2);
		float goal = query.getFloat(3);
		query.close();
		
		boolean finished = goalChangeDb(goalId, title, current, goal, sum);		
		return finished;
	}
	
	public boolean goalChangeDb(long goalId, String goalTitle, float current, float goal, float amount)
	{
		boolean goalFinished = false;
		float newAmount = current+amount;
		ContentValues content = new ContentValues();
		content.put("beloebCurrent", newAmount);
		db.update(TABLE_GOALS, content, "_id="+goalId, null);
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(System.currentTimeMillis());
		//String year = 
		//String dateString = cal.toString();
		db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('"+goalTitle+"', 'Du sparede "+amount+" kroner op!',  'Now')");
		if(newAmount>=goal){
			goalFinished = true;
			db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('"+goalTitle+"', 'Du færdigjorde dit mål om at spare "+goal+" op!',  'now')");
		}
		
		return goalFinished;
	}
	
	public void setStandardAlarmForGoal(Context context, long goalId){
		//Toast.makeText(this, "setStandardAlarmForGoal start", Toast.LENGTH_LONG).show();
		// Set startDate to be the 1st of the next month, (if the alarm is created on the 7th of January, the Alarm will start on the 1st of February.
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(System.currentTimeMillis());
		startDate.add(Calendar.MONTH, 1);
		startDate.set(Calendar.DATE, 1);
		
		//startDate.add(Calendar.SECOND, 10);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(context, SavingsAlarmReceiver.class);
		intent.putExtra("goalId", goalId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) goalId, intent, 0);
		
		alarmManager.set(AlarmManager.RTC_WAKEUP,  startDate.getTimeInMillis(), pendingIntent);
	}
	
	
	
	
	public void clearPosts() {
		db.delete(TABLE_POSTS, null, null);
	}
}
