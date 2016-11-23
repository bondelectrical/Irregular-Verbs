package net.ucoz.abondarenko;

import net.ucoz.abondarenko.dataBase.DB;
import net.ucoz.abondarenko.dataBase.DBHelper;
import android.app.Application;
import android.util.Log;

public class IrregularVerbs extends Application {
	
	public final static String LOG_TAG = "IrregularVerbs";
	
	protected DB db;
	
	@Override
	public void onCreate() {				
		super.onCreate();		
		db = new DB(getApplicationContext());		
		db.openDB();			
	}
	
	@Override
	public void onLowMemory() {		
		super.onLowMemory();	
		db.closeDB();		
	}
	
	@Override
	public void onTerminate() {		
		super.onTerminate();
		db.closeDB();
	}

}
