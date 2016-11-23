package net.ucoz.abondarenko.dataBase;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import net.ucoz.abondarenko.IrregularVerbs;
import net.ucoz.abondarenko.R;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
	
	private final Context context;
	
	public static final int DATABASE_VERSION = 1;
	public static final String DATABASE_NAME = "IrregularVerbs";
	private static final String TEXT_TYPE = " TEXT";
	private static final String COMMA_SEP = ", ";
	private static final String SQL_CREATE_ENTRIES =
	    "CREATE TABLE " + DB.TABLE_NAME + " (" +
	    DB.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
	    DB.COLUMN_NAME_INFINITIVE + TEXT_TYPE + COMMA_SEP +
	    DB.COLUMN_NAME_PAST_SIMPLE + TEXT_TYPE + COMMA_SEP +	   
	    DB.COLUMN_NAME_PAST_PARTICIPLE + TEXT_TYPE + COMMA_SEP +	   
	    DB.COLUMN_NAME_TRANSLATION + TEXT_TYPE + 	   
	    ");";
	



	public DBHelper(Context context) {
		super(context, DB.DATABASE_NAME, null, DB.DATABASE_VERSION);
		this.context = context;	
	}

	@Override
	public void onCreate(SQLiteDatabase db) {		
		db.execSQL(SQL_CREATE_ENTRIES);			
		ContentValues cv = new ContentValues();			
		Resources res = context.getResources();				
		XmlResourceParser xmlParser = res.getXml(R.xml.irregular_verbs);	
		
		 try {
			 
			 int eventType = xmlParser.getEventType();
			 while (eventType != XmlPullParser.END_DOCUMENT) {
				 
				 if (eventType == XmlPullParser.START_TAG && (xmlParser.getName().equals("record"))) {
					 String infinitive = xmlParser.getAttributeValue(0);
					 String past_simple = xmlParser.getAttributeValue(1);
					 String past_participle = xmlParser.getAttributeValue(2);
					 String translation = xmlParser.getAttributeValue(3);
					 
					 cv.put(DB.COLUMN_NAME_INFINITIVE, infinitive);
					 cv.put(DB.COLUMN_NAME_PAST_SIMPLE, past_simple);
					 cv.put(DB.COLUMN_NAME_PAST_PARTICIPLE, past_participle);
					 cv.put(DB.COLUMN_NAME_TRANSLATION, translation);
					 
					 db.insert(DB.TABLE_NAME, null, cv);					 
				 }
				 
				 eventType = xmlParser.next();
			 }
			
		} catch (XmlPullParserException e) {
			Log.e(IrregularVerbs.LOG_TAG, "Error XmlPullParserException" + e.getMessage());
		} catch (IOException e) {
			Log.e(IrregularVerbs.LOG_TAG, "Error write DataBase " + e.getMessage());
			
		} finally {
			xmlParser.close();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
