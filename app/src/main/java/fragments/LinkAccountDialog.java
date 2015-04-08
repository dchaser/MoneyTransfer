package fragments;

import redesign.screens.MobileCodeEntryScreen;
import ui.navbarLinks.Registration_One;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.moneytransfer.R;

import dtos.SenderCheckDTO;

public class LinkAccountDialog extends DialogFragment {

	public String phone;
	public String email;

	Typeface NotoSans;

	public static LinkAccountDialog newInstance(SenderCheckDTO senderToCheck) {

		LinkAccountDialog f = new LinkAccountDialog();
		
		SenderCheckDTO checkDTO = senderToCheck;

		Bundle args = new Bundle();
		args.putString("phone", checkDTO.Phone);
		args.putString("email", checkDTO.Email);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//access SenderCheckDTo's stuff
		this.phone = getArguments().getString("phone");
		this.email = getArguments().getString("email");

		// Pick a style based on the number.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		setStyle(style, theme);

		NotoSans = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/NotoSans-Regular.ttf");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.found_existing_user_dialog,
				container, false);

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(true);

		TextView tvAccFound = (TextView) v.findViewById(R.id.tvAccFound);
		tvAccFound.setTypeface(NotoSans);

		TextView tvAccDialog = (TextView) v.findViewById(R.id.tvAccDialog);
		tvAccDialog.setTypeface(NotoSans);

		Button btnlinkAcc = (Button) v.findViewById(R.id.btnlinkAcc);
		btnlinkAcc.setTypeface(NotoSans);
		btnlinkAcc.setOnClickListener(new OnClickListener() {

			// Link Account button click
			@Override
			public void onClick(View v) {

				// send user to sms code page
				Intent a = new Intent(getActivity(),
						MobileCodeEntryScreen.class);
				String pass = WhatToPAss();
				a.putExtra("whatIsPassed", pass);
				startActivity(a);

			}
		});

		Button btnresetAcc = (Button) v.findViewById(R.id.btnresetAcc);
		btnresetAcc.setTypeface(NotoSans);
		btnresetAcc.setOnClickListener(new OnClickListener() {

			// Reset and start new Account button click
			@Override
			public void onClick(View v) {

				// send to registration page

				Intent b = new Intent(getActivity(), Registration_One.class);
				startActivity(b);
			}
		});

		return v;

	}

	private String WhatToPAss() {

		if (this.email == null) {
			return this.phone;
		} else {
			return this.email;
		}
	}

}