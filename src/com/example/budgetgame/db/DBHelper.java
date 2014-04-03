package com.example.budgetgame.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "budget";
	public static final String APP_NAME = "BudgetApp";
	public static final String TABLE_NAME = "Posteringer";
	
	Context context;

	public DBHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Sample data for testing
		db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id integer primary key autoincrement, titel text, dato text, beloeb float)");
		db.execSQL("INSERT INTO " + TABLE_NAME + " (titel, dato, beloeb) VALUES ('FreshFitness', '03-04-2014', -200)");
		db.execSQL("INSERT INTO " + TABLE_NAME + " (titel, dato, beloeb) VALUES ('Netto', '06-04-2014', -350)");
		db.execSQL("INSERT INTO " + TABLE_NAME + " (titel, dato, beloeb) VALUES ('Løn', '01-05-2014', 8948.43)");
		db.execSQL("INSERT INTO " + TABLE_NAME + " (titel, dato, beloeb) VALUES ('SU', '01-05-2014', 2905.29)");
		db.execSQL("INSERT INTO " + TABLE_NAME + " (titel, dato, beloeb) VALUES ('Amazon', '05-05-2014', -350)");
		db.execSQL("INSERT INTO " + TABLE_NAME + " (titel, dato, beloeb) VALUES ('Bilka', '09-05-2014', -1252.20)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		onCreate(db);
	}
	

}
