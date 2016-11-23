package net.ucoz.abondarenko;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

import net.ucoz.abondarenko.dataBase.DB;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Exam extends Activity {
	
	private ProgressBar prBarExam;	
	private TextView txtExamInfinitive;
	private TextView txtExamTranslation;
	private TextView txtFormVerbs;
	private EditText edTxtInputRespons;
	private Button btnOk;
	private TextView txtAnsver;
	private TextView txtCorrectAnsver;
	private TextView txtPrBar;
	
	private String infinitive;
	private String pastSsimple;
	private String pastParticiple;
	private String translation;
	
	private int numberVerbs = 2;
	private int randVerb;
	
	private SharedPreferences sPref;
	private int valuesProgress;
	
	final private  static  int KEY_NUMBER_PAST_SIMPLE = 0;
	final private  static  int KEY_NUMBER_PAST_PARTICIPLE = 1;
	final private static String SAVED_PROGRESS = "saved_progress";
	final private static int DIALOG_EXIT = 1;
	
	private Timer timer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exam);
		
		prBarExam = (ProgressBar) findViewById(R.id.prBarExam);
		txtExamInfinitive = (TextView) findViewById(R.id.txtExamInfinitive);
		txtExamTranslation = (TextView) findViewById(R.id.txtExamTranslation);
		txtFormVerbs = (TextView) findViewById(R.id.txtFormVerbs);
		edTxtInputRespons = (EditText) findViewById(R.id.edTxtInputRespons);
		txtAnsver = (TextView) findViewById(R.id.txtAnsver);
		btnOk = (Button) findViewById(R.id.btnOk);
		txtCorrectAnsver = (TextView) findViewById(R.id.txtCorrectAnsver);
		txtPrBar = (TextView) findViewById(R.id.txtPrBar);
		
		txtCorrectAnsver.setText("");
		txtAnsver.setText("");	
		sPref = getPreferences(MODE_PRIVATE);
		if(sPref.contains(SAVED_PROGRESS)) {
			valuesProgress = getValueProgressBar();		
			setProgressBar(valuesProgress);
			checkProgress(valuesProgress);
		} else {
			valuesProgress = 0;
			setProgressBar(valuesProgress);
		}		
		Log.e(IrregularVerbs.LOG_TAG, Integer.toString(valuesProgress));
		setRandVerbs();		
		edTxtInputRespons.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {
					onClickOk();
					return true;
				}
				return false;
			}
		});;
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				onClickOk();
			}
		});
		
	}
	
	private void onClickOk() {
		String readLine = edTxtInputRespons.getText().toString();
		switch (randVerb) {
		case KEY_NUMBER_PAST_SIMPLE:
			if(readLine.compareToIgnoreCase(pastSsimple) == 0){
				txtAnsver.setText(getResources().getString(R.string.true_answer));
				txtCorrectAnsver.setText("");
				++valuesProgress;	
				setGreenColorButtom();
				checkProgress(valuesProgress);
				setProgressBar(valuesProgress);					
			} else {
				txtAnsver.setText(getResources().getString(R.string.false_answer));
				txtCorrectAnsver.setText(infinitive + " " + pastSsimple + " "
						+ pastParticiple + " " + translation);
				if (valuesProgress == 0) {
					setProgressBar(valuesProgress);
				} else {
					--valuesProgress;
					setProgressBar(valuesProgress);							
				}						
				downKeyboard();
				setRedColorButtom();						
			}
			break;
		case KEY_NUMBER_PAST_PARTICIPLE:
			if(readLine.compareToIgnoreCase(pastParticiple) == 0){
				txtAnsver.setText(getResources().getString(R.string.true_answer));
				txtCorrectAnsver.setText("");
				++valuesProgress;	
				setGreenColorButtom();
				checkProgress(valuesProgress);
				setProgressBar(valuesProgress);	
			} else {
				txtAnsver.setText(getResources().getString(R.string.false_answer));
				txtCorrectAnsver.setText(infinitive + " " + pastSsimple + " "
						+ pastParticiple + " " + translation);
				if (valuesProgress == 0) {
					setProgressBar(valuesProgress);
				} else {
					--valuesProgress;
					setProgressBar(valuesProgress);								
				}													
				downKeyboard();
				setRedColorButtom();
			}
			break;
		default:
			break;
		}
		setRandVerbs();		
		edTxtInputRespons.setText("");
	}
	
	private void downKeyboard() {
		InputMethodManager imm = (InputMethodManager) getSystemService(getApplication().INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(edTxtInputRespons.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);	

	}
	
	private void saveDataProgress(int progress) {
		sPref = getPreferences(MODE_PRIVATE);
		Editor editor = sPref.edit();
		editor.putInt(SAVED_PROGRESS, progress);
		editor.commit();
	}
	
	private void setProgressBar(int values) {		
		prBarExam.setProgress(values);
		float valuesFloat = (float)(values/10.0);
		txtPrBar.setText(Float.toString(valuesFloat) + "%");
	}
	
	private int getValueProgressBar() {
		sPref = getPreferences(MODE_PRIVATE);
		return sPref.getInt(SAVED_PROGRESS, 0);
	}
	
	private void checkProgress(int values) {
		if( values >= 1000) {
			showDialog(DIALOG_EXIT);
			valuesProgress = 1000;
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
		if(item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(getApplication(), MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		}
		return true;
	}
	
	@Override
	protected void onStart() {		
		super.onStart();
		txtCorrectAnsver.setText("");
		txtAnsver.setText("");
		checkProgress(valuesProgress);		
	}
	
	@Override
	protected void onStop() {		
		super.onStop();
		saveDataProgress(valuesProgress);		
	}
	
	@Override
	protected void onDestroy() {		
		super.onDestroy();
		saveDataProgress(valuesProgress);		
	}
	
	private void setRandVerbs() {
		MyTask mt = new MyTask();
		String[] textLineDB = null;
		mt.execute();
		try {
			textLineDB = mt.get();
		} catch (InterruptedException e) {
			Log.e(IrregularVerbs.LOG_TAG, "Error InterruptedException" + e.getMessage());
			e.printStackTrace();
		} catch (ExecutionException e) {
			Log.e(IrregularVerbs.LOG_TAG, "Error ExecutionException" + e.getMessage());
			e.printStackTrace();
		}				
		txtExamInfinitive.setText(textLineDB[0]);
		txtExamTranslation.setText(textLineDB[3]);	
		infinitive = textLineDB[0];
		pastSsimple = textLineDB[1];
		pastParticiple = textLineDB[2];		
		translation = textLineDB[3];
		Random randNumder = new Random();	
		randVerb = randNumder.nextInt(numberVerbs);	
		switch (randVerb) {
		case KEY_NUMBER_PAST_SIMPLE:
			txtFormVerbs.setText(this.getResources().getString(R.string.enter_past_simple));		
			break;
		case KEY_NUMBER_PAST_PARTICIPLE:
			txtFormVerbs.setText(this.getResources().getString(R.string.enter_past_participle));			
			break;
		default:			
			break;
		}
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		if(id == DIALOG_EXIT) {
			AlertDialog.Builder adb = new AlertDialog.Builder(this);
			adb.setIcon(android.R.drawable.ic_dialog_alert);
			adb.setTitle(R.string.dialog_title);
			adb.setMessage(R.string.dialig_message);
			adb.setPositiveButton(R.string.yes, dialogButtomlistener);
			adb.setNegativeButton(R.string.no, dialogButtomlistener);	
			adb.setCancelable(false);
			return adb.create();
		}
		return super.onCreateDialog(id);
	}
	
	android.content.DialogInterface.OnClickListener dialogButtomlistener = new android.content.DialogInterface.OnClickListener() {
		
		@Override
		public void onClick(DialogInterface dialog, int which) {
			switch (which) {
			case Dialog.BUTTON_POSITIVE:
				valuesProgress = 0;
				setProgressBar(valuesProgress);
				saveDataProgress(valuesProgress);		
				break;
			case Dialog.BUTTON_NEGATIVE:
					
				break;			
			}			
		}
	};
	
	private void setGreenColorButtom() {
		btnOk.setBackground(getResources().getDrawable(R.drawable.rounded_button_green));		
		timer = new Timer();
		DelayTimerTask delayTimer = new DelayTimerTask(R.drawable.rounded_button_gray);
		timer.schedule(delayTimer, 1000);		
	}
	
	private void setRedColorButtom() {
		btnOk.setBackground(getResources().getDrawable(R.drawable.rounded_button_red));		
		timer = new Timer();
		DelayTimerTask delayTimer = new DelayTimerTask(R.drawable.rounded_button_gray);
		timer.schedule(delayTimer, 1000);		
	}
	
	private class MyTask extends AsyncTask<Void, Void, String[]> {

		@Override
		protected String[] doInBackground(Void... params) {
			DB db = ((IrregularVerbs) getApplicationContext()).db;
			return db.getRandomLine();			
		}
		
	}
	
	private class DelayTimerTask extends TimerTask {
		private int drawable;
		DelayTimerTask(int drawable) {
			this.drawable = drawable;
		}

		@Override
		public void run() {
			
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					btnOk.setBackground(getResources().getDrawable(drawable));	
					edTxtInputRespons.setCursorVisible(true);
				}
			});			
		}
		
	}

}
