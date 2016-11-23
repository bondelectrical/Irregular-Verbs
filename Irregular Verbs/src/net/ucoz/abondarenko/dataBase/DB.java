package net.ucoz.abondarenko.dataBase;

import java.util.Random;

import net.ucoz.abondarenko.IrregularVerbs;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;

public class DB {
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "IrregularVerbs";
	public static final String TABLE_NAME = "IrregularVerbs50words";
    public static final String COLUMN_NAME_INFINITIVE = "infinitive";
    public static final String COLUMN_NAME_PAST_SIMPLE = "past_simple";
    public static final String COLUMN_NAME_PAST_PARTICIPLE = "past_participle";
    public static final String COLUMN_NAME_TRANSLATION = "translation";
    public static final String COLUMN_ID = "_id";
	
	private Context context;
	private DBHelper dbHelper;
	private SQLiteDatabase mDB;

	public DB(Context context) {	
		this.context = context;
	}
	
	public void openDB() {
		dbHelper = new DBHelper(context);
		mDB = dbHelper.getWritableDatabase();
	}
	
	public void closeDB() {
		if (dbHelper != null) {
			dbHelper.close();
		}
	}
	
	public Cursor getAllDB() {
		return mDB.query(TABLE_NAME, null, null, null, null, null, null);
	}
	
	public Cursor getLineDB(int id) {
		String where = "_id = " + Integer.toString(id);
		return mDB.query(TABLE_NAME, null, where, null, null, null, null);
	}
	
	public int getEndIdDB() {
		Cursor cursor = mDB.query(TABLE_NAME, new String[] {"_id = ?"}, null, null, null, null, null);
		cursor.moveToLast();
		int id = cursor.getPosition();		
		cursor.close();
		return id;
	}
	
	public String[] getRandomLine() {
		Cursor cursor = mDB.query(TABLE_NAME, new String[] {"_id = ?"}, null, null, null, null, null);
		cursor.moveToLast();
		int id = cursor.getPosition();		
		cursor.close();	
		Random randNumder = new Random();		
		int randId = randNumder.nextInt(id++);			
		String where = "_id = " + Integer.toString(randId);
		Cursor cursorLine = mDB.query(TABLE_NAME, null, where, null, null, null, null);		
		String[] textLine = new String[4];		
		cursorLine.moveToFirst();		
		while (cursorLine.isAfterLast() == false) {			
			textLine[0] = cursorLine.getString(cursorLine.getColumnIndex(DB.COLUMN_NAME_INFINITIVE));			
			textLine[1] = cursorLine.getString(cursorLine.getColumnIndex(DB.COLUMN_NAME_PAST_SIMPLE));
			textLine[2] = cursorLine.getString(cursorLine.getColumnIndex(DB.COLUMN_NAME_PAST_PARTICIPLE));
			textLine[3] = cursorLine.getString(cursorLine.getColumnIndex(DB.COLUMN_NAME_TRANSLATION));			
			cursorLine.moveToNext();
		}
		cursorLine.close();
		return textLine;
	}	
	
	
}
