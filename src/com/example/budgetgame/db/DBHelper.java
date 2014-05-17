package com.example.budgetgame.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
	
	public static final int DB_VERSION = 14;
	public static final String DB_POSTS = "budget";
	public static final String APP_POSTS = "BudgetApp";
	public static final String TABLE_POSTS = "Posteringer";
	public static final String TABLE_GOALS = "Goals";
	public static final String TABLE_GOALS_HISTORY = "History";
	public static final String TABLE_ACHIEVEMENTS = "Achievement";
	
	Context context;

	public DBHelper(Context context) {
		super(context, DB_POSTS, null, DB_VERSION);
		this.context = context;
	}

	
	@Override
	public void onCreate(SQLiteDatabase db) {
		// Sample data for testing
		db.execSQL("CREATE TABLE " + TABLE_POSTS + " (_id integer primary key autoincrement, titel text, dato text, beloeb float)");
		db.execSQL("INSERT INTO " + TABLE_POSTS + " (titel, dato, beloeb) VALUES ('FreshFitness', '03-04-2014', -200)");
		db.execSQL("INSERT INTO " + TABLE_POSTS + " (titel, dato, beloeb) VALUES ('Netto', '06-04-2014', -350)");
		db.execSQL("INSERT INTO " + TABLE_POSTS + " (titel, dato, beloeb) VALUES ('L�n', '01-05-2014', 8948.43)");
		db.execSQL("INSERT INTO " + TABLE_POSTS + " (titel, dato, beloeb) VALUES ('SU', '01-05-2014', 2905.29)");
		db.execSQL("INSERT INTO " + TABLE_POSTS + " (titel, dato, beloeb) VALUES ('Amazon', '05-05-2014', -350)");
		db.execSQL("INSERT INTO " + TABLE_POSTS + " (titel, dato, beloeb) VALUES ('Bilka', '09-05-2014', -1252.20)");
		
		db.execSQL("CREATE TABLE " + TABLE_GOALS + " (_id integer primary key autoincrement, titel text, beloebCurrent float, beloebM�l float, toSavePerMonth float, dateCreated date )");
		db.execSQL("INSERT INTO " + TABLE_GOALS + " (titel, beloebCurrent, beloebM�l, toSavePerMonth, dateCreated) VALUES ('Ny Cykel', 500,  3500, 500, '2014-03-17')");
		db.execSQL("INSERT INTO " + TABLE_GOALS + " (titel, beloebCurrent, beloebM�l, toSavePerMonth, dateCreated) VALUES ('Ferie Mallorca', 1600 , 8400, 400, '2014-01-07')");
		
		db.execSQL("CREATE TABLE " + TABLE_GOALS_HISTORY + " (_id integer primary key autoincrement, titel text, beskrivelse text, dato date)");
		db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('Julegaver', 'Du sparede 750 kroner op.',  '2013-10-31')");
		db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('Julegaver', 'Du sparede 750 kroner op, og opn�ede dit m�l om at spare 1500 til Julegaver!',  '2013-11-30')");
		db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('Ferie Mallorca', 'Du sparede 400 kroner op.',  '2014-02-01')");
		db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('Ferie Mallorca', 'Du sparede 400 kroner op.',  '2014-03-01')");
		db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('Ferie Mallorca', 'Du sparede 400 kroner op.',  '2014-04-01')");
		db.execSQL("INSERT INTO " + TABLE_GOALS_HISTORY + " (titel, beskrivelse, dato) VALUES ('Ferie Mallorca', 'Du sparede 400 kroner op.',  '2014-05-01')");
		
		db.execSQL("CREATE TABLE " + TABLE_ACHIEVEMENTS + " (_id integer primary key autoincrement, titel text, beskrivelse text, klaret integer)");
		ContentValues achievement1 = new ContentValues();
		achievement1.put("titel", "Opret m�l");
		achievement1.put("beskrivelse", "For at opn� denne medalje, skal du oprette et m�l i app'en.");
		achievement1.put("klaret", 0);
		
		ContentValues achievement2 = new ContentValues();
		achievement2.put("titel", "F�rdigg�r m�l");
		achievement2.put("beskrivelse", "For at opn� denne medalje, skal du f�rdigg�re et m�l, du har sat for dig selv.");
		achievement2.put("klaret", 0);
		
		ContentValues achievement3 = new ContentValues();
		achievement3.put("titel", "F�rdigg�r st�rre m�l");
		achievement3.put("beskrivelse", "For at opn� denne medalje, skal du f�rdigg�re et m�l p� over 500 kr, du har sat for dig selv.");
		achievement3.put("klaret", 0);
		
		db.insert(TABLE_ACHIEVEMENTS, null, achievement1);
		db.insert(TABLE_ACHIEVEMENTS, null, achievement2);
		db.insert(TABLE_ACHIEVEMENTS, null, achievement3);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE " + TABLE_POSTS);
		db.execSQL("DROP TABLE " + TABLE_GOALS);
		db.execSQL("DROP TABLE " + TABLE_GOALS_HISTORY);
		
		if (oldVersion>=10) db.execSQL("DROP TABLE " + TABLE_ACHIEVEMENTS);
		
		
		onCreate(db);
	}
	

}
