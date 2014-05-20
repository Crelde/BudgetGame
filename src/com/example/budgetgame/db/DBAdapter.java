package com.example.budgetgame.db;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.budgetgame.SavingsAlarmReceiver;
import com.example.external.Post;


/**
 * @author Kewin & Christian
 * @summary Database Adapter that holds all databases functions, including 
 * reading, writing, deleting and updating fields.
 * 
 */
public class DBAdapter {
	DBHelper helper;
	SQLiteDatabase db;
	Context context;
	
	// Table constants
	String TABLE_POSTS = "Posteringer";
	String TABLE_GOALS = "Goals";
	String TABLE_GOALS_HISTORY = "History";
	String TABLE_ACHIEVEMENTS = "Achievement";

	// Intialize adapter
	public DBAdapter(Context context) {
		helper = new DBHelper(context);
		this.context = context;
		}

	// Open adapter
	public void open() {
		db = helper.getWritableDatabase();
	}

	// Close adapter
	public void close() {
		db.close();
	}

	// Database function to insert a post, only used by ServerController.
	public void insertPost(Post post){
		ContentValues values = new ContentValues();
		values.put("titel", post.titel);
		values.put("dato", post.dato.substring(0,10));
		values.put("beloeb", post.beloeb);
		db.insert(TABLE_POSTS, null, values);
	}

	// Database function to return all posts.
	public Cursor getAllPosts(){
		Cursor query = db.query(TABLE_POSTS, new String[] { "_id", "titel", "dato", "beloeb" }, null, null, null, null,
				"dato DESC");
		return query;
	}
	// Database function to return all posts with negative values.
	public Cursor getNegPosts(){
		Cursor query = db.query(TABLE_POSTS, new String[] { "_id", "titel", "dato", "beloeb" }, "beloeb < 0", null, null, null,
				"dato DESC");
		return query;
	}
	// Database function to return all posts with positive vaues.
	public Cursor getPosPosts(){
		Cursor query = db.query(TABLE_POSTS, new String[] { "_id", "titel", "dato", "beloeb" }, "beloeb >= 0", null, null, null,
				"dato DESC");
		return query;
	}
	
	// Database function to return a single post with the given id.
	public Cursor getPost(int id){
		Cursor query = db.query(TABLE_POSTS, new String[] { "titel", "dato", "beloeb" }, "_id="+id, null, null, null, null);
		return query;
	}
	
	// Database function to return all goals.
	public Cursor getAllGoals(){
		Cursor query = db.query(TABLE_GOALS, new String[] { "_id", "titel", "beloebCurrent", "beloebMål" }, null, null, null, null,
				"_id DESC");
		return query;
	}
	
	// Database function to return a single goal given the id.
	public Cursor getGoal(long id){
		Cursor query = db.query(TABLE_GOALS, new String[] { "_id", "titel", "beloebCurrent", "beloebMål", "toSavePerMonth", "dateCreated" }, "_id="+id, null, null, null, null);
		return query;
	}
	
	// Database function to return all achievements.
	public Cursor getAchievements(){
		Cursor query = db.query(TABLE_ACHIEVEMENTS, new String[] { "_id", "titel", "beskrivelse", "klaret" }, null, null, null, null,
				"_id DESC");
		return query;			
	}
	
	// Sets new goal and returns id.
	public long setNewGoal(String titel, int mål, int prMonth){
		ContentValues values = new ContentValues();
		values.put("titel", titel);
		values.put("beloebCurrent", 0);
		values.put("beloebMål", mål);
		values.put("toSavePerMonth", prMonth);
		String dateString = getDateString();
		values.put("dateCreated", dateString);
		long id = db.insert(TABLE_GOALS, null, values);
		
		// Initialize achievement
		ContentValues achievement = new ContentValues();
		achievement.put("klaret", 1);
		
		// Check for completion
		Cursor achievement1 = db.query(TABLE_ACHIEVEMENTS, new String[] {"klaret"}, "_id=1",null, null, null, null);
		achievement1.moveToFirst();
		int award1Int = achievement1.getInt(0);
		if (award1Int==0){
			// Achievement 1 completion
			db.update(TABLE_ACHIEVEMENTS, achievement, "_id=1", null);			
			ContentValues historyValues = new ContentValues();
			historyValues.put("titel", "Du opnåede en medalje!");
			historyValues.put("beskrivelse", "Du oprettede et mål, op opnåede dermed en medalje! Godt gået!");
			historyValues.put("dato", getDateString());
			db.insert(TABLE_GOALS_HISTORY, null, historyValues);
		}		
		return id;
	}
	
	// Database function to delete a goal.
	public boolean deleteGoal(int id){
		if (db.delete(TABLE_GOALS, "_id="+id, null)==1) return true;
		else return false;		
	}
	
	// Database function to update a goal.	
	public boolean updateGoal(ContentValues goal, int goalId, int extra){
			
		db.update(TABLE_GOALS, goal, "_id="+goalId, null);
		
		
		if (extra>0)
		{
			putAsideMoney(goalId, extra);
		}
		
		return true;
	}
	
	
	// Database function to return all history items.
	public Cursor getHistory(){
		Cursor query = db.query(TABLE_GOALS_HISTORY, new String[] { "_id", "titel", "beskrivelse", "dato" }, null, null, null, null,
				"_id DESC");
		return query;
	}
	
	// Used when putting aside the standard amount of money as defined by the goal.
	public boolean putAsideMoney(int goalId){
		Cursor query = db.query(TABLE_GOALS, new String[] { "_id", "titel", "beloebCurrent", "beloebMål", "toSavePerMonth" }, "_id="+goalId, null, null, null, null, "1");
		query.moveToFirst();
		String title = query.getString(1);
		float current = query.getFloat(2);
		float goal = query.getFloat(3);
		float monthly = query.getFloat(4);
		
		boolean finished = goalChangeDb(goalId, title, current, goal, monthly);	
			
		int sum = (int) monthly;
		
		if (finished) checkAchievement(goalId, sum);
			
		return finished;
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
			
		if (finished) checkAchievement(goalId, current);
		
		return finished;
	}
	
	// Convenience method to check for completion of achievments 2 and 3
	private void checkAchievement(int goalId, float sum){
		
		// Initialize achievement
		ContentValues achievement = new ContentValues();
		achievement.put("klaret", 1);
		
		// Check for completion
		Cursor achievement2 = db.query(TABLE_ACHIEVEMENTS, new String[] {"klaret"}, "_id=2",null, null, null, null);
		achievement2.moveToFirst();
		int award2Int = achievement2.getInt(0);
		
		if (award2Int==0){
			// Set AchievementValue of 1 (true)
			
			// Achievement 2 completion
			db.update(TABLE_ACHIEVEMENTS, achievement, "_id=2", null);			
			ContentValues historyValues = new ContentValues();
			historyValues.put("titel", "Du opnåede en medalje!");
			historyValues.put("beskrivelse", "Du færdiggjorde et mål, op opnåede dermed en medalje! Godt gået!");
			historyValues.put("dato", getDateString());
			db.insert(TABLE_GOALS_HISTORY, null, historyValues);
				
			}
		// Check for completion
		Cursor achievement3 = db.query(TABLE_ACHIEVEMENTS, new String[] {"klaret"}, "_id=3",null, null, null, null);
		achievement3.moveToFirst();
		int award3Int = achievement3.getInt(0);
		if (award3Int==0 && sum>=500){
			System.out.println("Achieve3 TEST");
			// Achievement 3 completion
			db.update(TABLE_ACHIEVEMENTS, achievement, "_id=3", null);
			ContentValues historyValues = new ContentValues();
			historyValues.put("titel", "Du opnåede en medalje!");
			historyValues.put("dato", getDateString());
			historyValues.put("beskrivelse", "Du færdiggjorde et mål på over 500 kroner, og opnåede dermed en medalje! Godt gået!");
			db.insert(TABLE_GOALS_HISTORY, null, historyValues);
		}		
	}
	
	// Convenience method to get a string representing the current day.
	private String getDateString(){
		Calendar date = Calendar.getInstance();
		int day = date.get(Calendar.DAY_OF_MONTH);
		int month = date.get(Calendar.MONTH)+1;
		int year = date.get(Calendar.YEAR);
		String dateString = day+"-"+month+"-"+year;
		return dateString;
	}
	
	// Method to update a goal as well as creating a history entry.
	public boolean goalChangeDb(long goalId, String goalTitle, float current, float goal, float amount)
	{
		boolean goalFinished = false;
		float newAmount = current+amount;
		ContentValues content = new ContentValues();
		content.put("beloebCurrent", newAmount);
		db.update(TABLE_GOALS, content, "_id="+goalId, null);
		
		String dateString = getDateString();
		//String year = 
		//String dateString = cal.toString();
		db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('"+goalTitle+"', 'Du sparede "+amount+" kroner op!',  '"+dateString+"')");
		if(newAmount>=goal){
			goalFinished = true;
			db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('"+goalTitle+"', 'Du færdigjorde dit mål om at spare "+goal+" op!',  '"+dateString+"')");
		}
		
		return goalFinished;
	}
	
	// Method to create an android alarm on the 1st of the next month, to set aside money as defined by the goal.
	public void setStandardAlarmForGoal(Context context, int goalId){
		// Set startDate to be the 1st of the next month, (if the alarm is created on the 7th of January, the Alarm will start on the 1st of February.
		Calendar startDate = Calendar.getInstance();
		startDate.setTimeInMillis(System.currentTimeMillis());
		startDate.add(Calendar.MONTH, 1);
		startDate.set(Calendar.DATE, 1);

		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		
		Intent intent = new Intent(context, SavingsAlarmReceiver.class);
		intent.putExtra("goalId", goalId);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) goalId, intent, 0);
		
		alarmManager.set(AlarmManager.RTC_WAKEUP,  startDate.getTimeInMillis(), pendingIntent);
	}
	
	
	
	// Convenience method to delete all entries in post table. 
	public void clearPosts() {
		db.delete(TABLE_POSTS, null, null);
	}
}
