package com.example.budgetgame.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	DBHelper helper;
	SQLiteDatabase db;
	Context context;
	
	String TABLE_POSTS = "Posteringer";
	String TABLE_GOALS = "Goals";

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
	public Cursor setNewGoal(String titel, int m�l){
		String rawq = "INSERT INTO "+TABLE_GOALS+"(titel, beloebCurrent, beloebM�l) VALUES ("+titel+", 0, "+m�l+");";
		db.execSQL("INSERT INTO " + TABLE_GOALS+ " (titel, beloebCurrent, beloebM�l) VALUES ('"+titel+"', 0, "+m�l+")");
		return getAllGoals();
	}
	
	public void clearPosts() {
		db.delete(TABLE_POSTS, null, null);
	}
}
