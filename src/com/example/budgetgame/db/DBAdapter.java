package com.example.budgetgame.db;

import com.example.external.Post;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	DBHelper helper;
	SQLiteDatabase db;
	Context context;
	
	String TABLE_POSTS = "Posteringer";
	String TABLE_GOALS = "Goals";
	String TABLE_GOALS_HISTORY = "History";

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
		Cursor query = db.query(TABLE_GOALS, new String[] { "_id", "titel", "beloebCurrent", "beloebM�l" }, null, null, null, null,
				"_id DESC");
		return query;
	}
	
	// Sets new goal and returns updated cursor 
	// Is the return used? Doesnt look like it -Kewin
	public Cursor setNewGoal(String titel, int m�l, int prMonth){
		db.execSQL("INSERT INTO " + TABLE_GOALS+ " (titel, beloebCurrent, beloebM�l, toSavePerMonth, dateCreated ) VALUES ('"+titel+"', 0, "+m�l+","+prMonth+","+"now"+")");
		return getAllGoals();
	}
	
	public Cursor getHistory(){
		Cursor query = db.query(TABLE_GOALS_HISTORY, new String[] { "_id", "titel", "beskrivelse", "dato" }, null, null, null, null,
				"dato DESC");
		return query;
	}
	
	// Used when putting aside the standard amount of money as defined by the goal.
	public void putAsideMoney(int goalId){
		Cursor query = db.query(TABLE_GOALS, new String[] { "_id", "titel", "beloebCurrent", "beloebM�l", "toSavePerMonth" }, "_id="+goalId, null, null, null, null, "1");
		query.moveToFirst();
		String title = query.getString(1);
		float current = query.getFloat(2);
		float goal = query.getFloat(3);
		float monthly = query.getFloat(4);
		
		goalChangeDb(goalId, title, current, goal, monthly);
	}
	
	// Used when putting aside an amount that is not the standard amount.
	public void putAsideMoney(int goalId, int sum){
		Cursor query = db.query(TABLE_GOALS, new String[] { "_id", "titel", "beloebCurrent", "beloebM�l", "toSavePerMonth" }, "_id="+goalId, null, null, null, null, "1");
		query.moveToFirst();
		String title = query.getString(1);
		float current = query.getFloat(2);
		float goal = query.getFloat(3);
		query.close();
		
		goalChangeDb(goalId, title, current, goal, sum);	
	}
	
	public boolean goalChangeDb(int goalId, String goalTitle, float current, float goal, float amount)
	{
		boolean goalFinished = false;
		float newAmount = current+amount;
		ContentValues content = new ContentValues();
		content.put("beloebCurrent", newAmount);
		db.update(TABLE_GOALS, content, "_id="+goalId, null);
		
		db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ("+goalTitle+", 'Du sparede "+amount+" kroner op.',  'now')");
		if(newAmount>=goal){
			goalFinished = true;
			db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ("+goalTitle+", 'Du f�rdigjorde dit m�l om at spare "+goal+" op!',  'now')");
		}
		
		return goalFinished;
	}
	
	
	public void clearPosts() {
		db.delete(TABLE_POSTS, null, null);
	}
}
