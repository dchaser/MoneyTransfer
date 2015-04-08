package fragments;

import ui.navbarLinks.Registration_One;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneytransfer.R;

public class FirstSlide extends Fragment implements OnClickListener {

	ImageButton imgBtnUserProfile;
	LinearLayout outerArea;

	TextView tvEnterMobile;
	TextView tvGetStarted;

	public FirstSlide() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.frag_first_slide, container, false);
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		InitializeControls();
		SetStylingOfTextViews();

		// set text views
	}

	private void SetStylingOfTextViews() {
		// TODO Auto-generated method stub

	}

	private void InitializeControls() {

		// user profile
		imgBtnUserProfile = (ImageButton) getActivity().findViewById(
				R.id.imgBtnUserPic);

		imgBtnUserProfile.setOnClickListener(this);

		// outer area
		outerArea = (LinearLayout) getActivity().findViewById(R.id.outerMain);
		outerArea.setOnClickListener(this);

		// type face NotoSansCJKjp_Light
		Typeface NotoSansCJKjp_Light = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/NotoSans-Regular.ttf");

		// text view for styling of text
		tvEnterMobile = (TextView) getActivity().findViewById(
				R.id.tvEnterMobile);
		tvEnterMobile.setTypeface(NotoSansCJKjp_Light);
		tvEnterMobile.setText("enter your mobile no to");

		// type face NotoSansCJKjp_Light
		Typeface NotoSansCJKjp_BOLD = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/NotoSans-Bold.ttf");

		tvGetStarted = (TextView) getActivity().findViewById(R.id.tvGetStarted);
		tvGetStarted.setTypeface(NotoSansCJKjp_BOLD, Typeface.BOLD);
		tvGetStarted.setText("get started");
		tvGetStarted.setTextColor(Color.parseColor("#607D8B"));

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.outerMain:

			// Mobile two factor authentication for fresh users

			// Input.setRawInputType(Configuration.KEYBOARD_12KEY);
			break;

		case R.id.imgBtnUserPic:

			// Yellow image tap

			Intent toRegistrationPage = new Intent(getActivity(), Registration_One.class);
			startActivity(toRegistrationPage);
			
			break;
		}
	}

}
