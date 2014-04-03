package com.example.budgetgame.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBAdapter {
	DBHelper helper;
	SQLiteDatabase db;
	Context context;
	
	String TABLE_NAME = "Posteringer";

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
		Cursor query = db.query(TABLE_NAME, new String[] { "_id", "titel", "dato", "beloeb" }, null, null, null, null,
				"_id DESC");
		return query;
	}
	
	public void clearPosts() {
		db.delete(TABLE_NAME, null, null);
	}
}
