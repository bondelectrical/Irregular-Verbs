package net.ucoz.abondarenko;

import java.util.concurrent.ExecutionException;

import net.ucoz.abondarenko.dataBase.DB;
import net.ucoz.abondarenko.fragments.FragmentBottomStudy;
import net.ucoz.abondarenko.fragments.FragmetTopStudy;
import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;


public class StudyVerbs extends Activity {
	
	private FragmetTopStudy fragmentTop;
	private FragmentBottomStudy fragmentBottom;
	private FragmentTransaction transaction;
	private FrameLayout container;	
	private Bundle bundle;	
	
	final public static String KEY_TEXT_INFINITIVER = "infinitiver";
	final public static String KEY_TEXT_PAST_SIMPLE = "pastsimple";
	final public static String KEY_TEXT_PAST_PARTICIPLE = "pastparticiple";
	final public static String KEY_TEXT_TRANSLATION = "translation";
	final public static String KEY_CHANGE_SCREEN = "changeScreenOrientation";
	final public static String KEY_TEXT_LINE = "textLine";
	final public static String TAG_FRAGMENT_TOP = "fragmentTop";
	final public static String TAG_FRAGMENT_BOTTOM = "fragmentBottom";
	
	private String[] textView ;	
	private String whatFragment;
	private MyTask mt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.study_verbs);		
			
		mt = new MyTask();
		
		fragmentTop = new FragmetTopStudy();		
		fragmentBottom = new FragmentBottomStudy();
		
		if(savedInstanceState == null) {					
			bundle = new Bundle();
			mt.execute();
			try {
				textView = mt.get();
			} catch (InterruptedException e) {
				Log.e(IrregularVerbs.LOG_TAG, "Error InterruptedException" + e.getMessage());
				e.printStackTrace();
			} catch (ExecutionException e) {
				Log.e(IrregularVerbs.LOG_TAG, "Error ExecutionException" + e.getMessage());
				e.printStackTrace();
			}	
			bundle.putString(KEY_TEXT_INFINITIVER, textView[0]);				
			fragmentTop.setArguments(bundle);			
			transaction = getFragmentManager().beginTransaction();		
			transaction.add(R.id.flStudy, fragmentTop);		
			whatFragment = TAG_FRAGMENT_TOP;
			transaction.commit();
		} else {			
			whatFragment = savedInstanceState.getString(KEY_CHANGE_SCREEN);
			textView = savedInstanceState.getStringArray(KEY_TEXT_LINE);
			if(whatFragment == TAG_FRAGMENT_TOP) {				
				bundle = new Bundle();
				bundle.putString(KEY_TEXT_INFINITIVER, textView[0]);					
				fragmentTop.setArguments(bundle);					
				transaction = getFragmentManager().beginTransaction();					
				transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);					
				transaction.add(R.id.flStudy, fragmentTop);
				transaction.commit();	
			} else {
				if (whatFragment == TAG_FRAGMENT_BOTTOM) {					
					bundle = new Bundle();
					bundle.putString(KEY_TEXT_INFINITIVER, textView[0]);
					bundle.putString(KEY_TEXT_PAST_SIMPLE, textView[1]);
					bundle.putString(KEY_TEXT_PAST_PARTICIPLE, textView[2]);
					bundle.putString(KEY_TEXT_TRANSLATION, textView[3]);
					fragmentBottom.setArguments(bundle);					
					transaction = getFragmentManager().beginTransaction();
					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);					
					transaction.add(R.id.flStudy, fragmentBottom);
					transaction.commit();	
				}
			}
				
		}		
		container = (FrameLayout) findViewById(R.id.flStudy);
		container.setOnClickListener(new OnClickListener() {				
			
			@Override
			public void onClick(View v) {
				if(whatFragment == TAG_FRAGMENT_TOP) {						
					fragmentBottom = new FragmentBottomStudy();
					bundle = new Bundle();
					bundle.putString(KEY_TEXT_INFINITIVER, textView[0]);
					bundle.putString(KEY_TEXT_PAST_SIMPLE, textView[1]);
					bundle.putString(KEY_TEXT_PAST_PARTICIPLE, textView[2]);
					bundle.putString(KEY_TEXT_TRANSLATION, textView[3]);					
					fragmentBottom.setArguments(bundle);				
					transaction = getFragmentManager().beginTransaction();					
					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);	
					transaction.remove(fragmentTop);
					transaction.add(R.id.flStudy, fragmentBottom);					
//					transaction.replace(R.id.flStudy, fragmentBottom);						
					whatFragment = TAG_FRAGMENT_BOTTOM;
				} else 
				if (whatFragment == TAG_FRAGMENT_BOTTOM){
					fragmentTop = new FragmetTopStudy();					
					mt = new MyTask();
					mt.execute();					
					try {
						textView = mt.get();
					} catch (InterruptedException e) {
						Log.e(IrregularVerbs.LOG_TAG, "Error InterruptedException" + e.getMessage());
						e.printStackTrace();
					} catch (ExecutionException e) {
						Log.e(IrregularVerbs.LOG_TAG, "Error ExecutionException" + e.getMessage());
						e.printStackTrace();
					}				
					bundle = new Bundle();
					bundle.putString(KEY_TEXT_INFINITIVER, textView[0]);					
					fragmentTop.setArguments(bundle);				
					transaction = getFragmentManager().beginTransaction();						
					transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);	
					transaction.remove(fragmentBottom);
					transaction.add(R.id.flStudy, fragmentTop);	
//					transaction.replace(R.id.flStudy, fragmentTop);						
					whatFragment = TAG_FRAGMENT_TOP;					
				}
				transaction.commit();				
			}
		});			
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
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {		
		super.onSaveInstanceState(outState);		
		outState.putString(KEY_CHANGE_SCREEN, whatFragment);		
		outState.putStringArray(KEY_TEXT_LINE, textView);
		
	}		
	
	private class MyTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			DB db = ((IrregularVerbs) getApplicationContext()).db;
			return db.getRandomLine();
		}		
	}	
	
}
