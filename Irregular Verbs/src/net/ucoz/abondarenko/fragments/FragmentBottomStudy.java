package net.ucoz.abondarenko.fragments;

import net.ucoz.abondarenko.IrregularVerbs;
import net.ucoz.abondarenko.R;
import net.ucoz.abondarenko.StudyVerbs;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FragmentBottomStudy extends Fragment {		
	private Bundle bundle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		bundle = getArguments();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {		
		View view =  inflater.inflate(R.layout.fragment_bottom_study, null);		
		
		TextView txtBottomInfinitivet = (TextView) view.findViewById(R.id.txtBottomInfinitivet);
		TextView txtPastSimple = (TextView) view.findViewById(R.id.txtPastSimple);		
		TextView txtPastParticiple = (TextView) view.findViewById(R.id.txtPastParticiple);
		TextView txtTranslation = (TextView) view.findViewById(R.id.txtTranslation);

		if (bundle != null) {
			String txtInfiniver = bundle.getString(StudyVerbs.KEY_TEXT_INFINITIVER);
			String txtSimple = bundle.getString(StudyVerbs.KEY_TEXT_PAST_SIMPLE);
			String txtParticiple = bundle.getString(StudyVerbs.KEY_TEXT_PAST_PARTICIPLE);
			String txtTrnlt = bundle.getString(StudyVerbs.KEY_TEXT_TRANSLATION);			
			
			if(txtInfiniver != null & txtSimple != null & txtParticiple != null & txtTrnlt != null ) {
				txtBottomInfinitivet.setText(txtInfiniver);
				txtPastSimple.setText(txtSimple);
				txtPastParticiple.setText(txtParticiple);				
				txtTranslation.setText(txtTrnlt);				
			}
			
		}		
		return view;
	}	
	
}
