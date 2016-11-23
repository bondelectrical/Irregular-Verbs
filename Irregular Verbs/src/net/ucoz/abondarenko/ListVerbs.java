package net.ucoz.abondarenko;

import net.ucoz.abondarenko.dataBase.DB;
import android.app.ActionBar;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class ListVerbs extends Activity implements LoaderCallbacks<Cursor>{
	
	private SimpleCursorAdapter scAdapter;
	private ListView lvData;
	private DB db;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_verbs);			
		
		IrregularVerbs mainApplication = (IrregularVerbs) getApplicationContext();
		
		db = mainApplication.db;
		
		String[] from = new String[] {DB.COLUMN_NAME_INFINITIVE, DB.COLUMN_NAME_PAST_SIMPLE,
				DB.COLUMN_NAME_PAST_PARTICIPLE, DB.COLUMN_NAME_TRANSLATION};
		
		int[] to = new int[] {R.id.txtInfinitive, R.id.txtPastSsimple, R.id.txtPastParticiple,
				R.id.txtTranslation};		
		
		scAdapter = new SimpleCursorAdapter(this, R.layout.item_list_verbs, null, from, to, 0);		
		lvData = (ListView) findViewById(R.id.lvVerbs);		
		lvData.setAdapter(scAdapter);		
		getLoaderManager().initLoader(0, null, this);
	
		
		
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {		
		
		return new ListVerbsCursorLoader(getApplicationContext(), db);
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {		
		scAdapter.swapCursor(data);				
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		// TODO Auto-generated method stub
		
	}
	
	
	static class ListVerbsCursorLoader extends CursorLoader {
		
		DB db;

		public ListVerbsCursorLoader(Context context, DB db) {
			super(context);
			this.db = db;			
		}
		
		@Override
		public Cursor loadInBackground() {			
			Cursor cursor = db.getAllDB();			
			return cursor;
		}		
		
	}	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {	
		ActionBar actionBar = (ActionBar) getActionBar();
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayHomeAsUpEnabled(true);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(getApplication(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);			
		}
		return true;
	}

}
