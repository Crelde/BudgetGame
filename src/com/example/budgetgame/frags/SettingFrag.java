package com.example.budgetgame.frags;

import com.example.budgetgame.MainActivity;
import com.example.budgetgame.R;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.LoginButton;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class SettingFrag extends Fragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.settings, container, false);
		((MainActivity) getActivity()).setActiveFragment(MainActivity.FRAGMENT_SETTINGS);
		//LoginButton authButton = (LoginButton) v.findViewById(R.id.authButton);
		//android.support.v4.app.Fragment self = (this);
		//authButton.setFragment(self);
		return v;
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		getActivity().setTitle("Indstillinger");
		
		final MainActivity activity = (MainActivity) getActivity();
		
		Button test = (Button) activity.findViewById(R.id.facebookTestButton);
		
		test.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(activity)
		        .setLink("https://developers.facebook.com/android")
		        .setCaption("Min custom subtitle!")
				.setName("Min Custom Title!")
		        .build();
		activity.uiHelper.trackPendingDialogCall(shareDialog.present());
				
			}
		});
	}
	

}
