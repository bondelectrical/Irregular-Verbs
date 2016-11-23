package net.ucoz.abondarenko.fragments;

import net.ucoz.abondarenko.R;
import net.ucoz.abondarenko.StudyVerbs;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmetTopStudy extends Fragment {	
	private Bundle bundle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		bundle = getArguments();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view =  inflater.inflate(R.layout.fragment_top_study, null);	
		TextView txtTopInfinitivet = (TextView) view.findViewById(R.id.txtTopInfinitivet);	
		if (bundle != null) {
			String txt = bundle.getString(StudyVerbs.KEY_TEXT_INFINITIVER);			
			if(txt != null) {
				txtTopInfinitivet.setText(txt);			
			}			
		}		
		return view;
	}
	
}
