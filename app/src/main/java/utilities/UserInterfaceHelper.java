package utilities;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.moneytransfer.R;

public class UserInterfaceHelper {

	public static EditText InitializeEditText(Activity activity, int id,
			EditText et) {
		et = (EditText) activity.findViewById(id);
		((EditText) et).addTextChangedListener(new ContactValidator(
				(EditText) et, activity));
		((EditText) et).setOnFocusChangeListener(new ContactValidator(
				(EditText) et, activity));

		return et;
	}

	public static EditText InitializeCustomEditText(Activity activity, int id,
			CustomEditText et) {
		et = (CustomEditText) activity.findViewById(id);
		((CustomEditText) et).addTextChangedListener(new ContactValidator(
				(EditText) et, activity));
		((CustomEditText) et).setOnFocusChangeListener(new ContactValidator(
				(EditText) et, activity));

		return et;
	}

	public static EditText InitializeEmailEditText(Activity activity, int id,
			EditText et) {
		et = (EditText) activity.findViewById(id);
		((EditText) et).addTextChangedListener(new EmailValidator(
				(EditText) et, activity));
		((EditText) et).setOnFocusChangeListener(new EmailValidator(
				(EditText) et, activity));

		return et;
	}

	public static EditText InitializeCustomEmailEditText(Activity activity,
			int id, CustomEditText et) {
		et = (CustomEditText) activity.findViewById(id);
		((CustomEditText) et).addTextChangedListener(new EmailValidator(
				(CustomEditText) et, activity));
		((CustomEditText) et).setOnFocusChangeListener(new EmailValidator(
				(CustomEditText) et, activity));

		return et;
	}

	public static Button InitializeButtonWithOnclickListener(Activity activity,
			int id, Button btn, OnClickListener listener) {

		btn = (Button) activity.findViewById(id);
		((Button) btn).setOnClickListener(listener);
		return btn;
	}

	public static void SetTextET(EditText et, String text) {
		et.setText(text);
	}

	public static TextView InitilizeTextView(Activity activity, int id,
			TextView tv) {
		tv = (TextView) activity.findViewById(id);

		return tv;
	}

	public static void PrepareActionBar(ActionBarActivity activity,
			ActionBar actionBar, Boolean isRegistred) {

		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator
				.inflate(R.layout.gloabl_action_barcustom_layout, null);

		// if this is a registered user then we got to change the NavDrawer icon
		// colors and User Profile Icon colors

		if (isRegistred) {
			// view references
			ImageButton abThreeBars = (ImageButton) v
					.findViewById(R.id.abThreeBars);
			abThreeBars.setOnClickListener((OnClickListener) activity);
			
			ImageButton abUserIcon = (ImageButton) v
					.findViewById(R.id.abUserIcon);
			abUserIcon.setOnClickListener((OnClickListener) activity);

			// image changes : three bars
			Drawable highlighted_threeBars = activity.getResources()
					.getDrawable(R.drawable.pride_menu_enabled);
			abThreeBars.setImageDrawable(highlighted_threeBars);

			// image changes : user profile
			Drawable highlighted_userprofile = activity.getResources()
					.getDrawable(R.drawable.pride_profile_enabled);
			abUserIcon.setImageDrawable(highlighted_userprofile);

		}

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public static void PrepareAboutPageActionBar(ActionBarActivity activity,
			ActionBar actionBar, Boolean isRegistred) {

		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbar_aboutpage, null);

		// if this is a registered user then we got to change the NavDrawer icon
		// colors and User Profile Icon colors

		if (isRegistred) {
			// view references
			ImageButton abThreeBars = (ImageButton) v
					.findViewById(R.id.abThreeBars);

			// image changes : three bars
			Drawable highlighted_threeBars = activity.getResources()
					.getDrawable(R.drawable.pride_menu_enabled);
			abThreeBars.setImageDrawable(highlighted_threeBars);

		}

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public static void PrepareRegistrationPageActionBar(
			ActionBarActivity activity, ActionBar actionBar, Boolean isRegistred) {

		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View v = null;
		if (isRegistred) {
			v = inflator.inflate(R.layout.actionbar_registerback_yellow, null);
		} else {
			v = inflator.inflate(R.layout.actionbar_register_back, null);
		}

		TextView abRegister = (TextView) v.findViewById(R.id.abRegister);
		abRegister.setText("YOUR DETAILS");
		abRegister.setOnClickListener((OnClickListener) activity);
		Typeface NotoSans = Typeface.createFromAsset(activity.getAssets(),
				"fonts/NotoSans-Regular.ttf");
		abRegister.setTypeface(NotoSans);

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public static void PrepareReceiverListPageActionBar(
			ActionBarActivity activity, ActionBar actionBar, Boolean isRegistred) {

		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbar_sendmoney_back, null);

		TextView absendMoney = (TextView) v.findViewById(R.id.absendMoney);
		absendMoney.setOnClickListener((OnClickListener) activity);
		Typeface NotoSans = Typeface.createFromAsset(activity.getAssets(),
				"fonts/NotoSans-Regular.ttf");
		absendMoney.setTypeface(NotoSans);

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}
	
	public static void PrepareReceiverDashboardPageActionBar(
			ActionBarActivity activity, ActionBar actionBar, Boolean isRegistred) {

		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbar_sendmoney_back, null);

		TextView absendMoney = (TextView) v.findViewById(R.id.absendMoney);
		absendMoney.setText("RECEIVERS");
		absendMoney.setOnClickListener((OnClickListener) activity);
		Typeface NotoSans = Typeface.createFromAsset(activity.getAssets(),
				"fonts/NotoSans-Regular.ttf");
		absendMoney.setTypeface(NotoSans);

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public static void PrepareAddNewReceiverPageActionBar(
			ActionBarActivity activity, ActionBar actionBar, Boolean isRegistred) {
		// <-Address book

		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbar_back_toaddressbook, null);

		TextView tvAddressBook = (TextView) v.findViewById(R.id.tvAddressBook);
		tvAddressBook.setOnClickListener((OnClickListener) activity);

		Typeface NotoSans = Typeface.createFromAsset(activity.getAssets(),
				"fonts/NotoSans-Regular.ttf");
		tvAddressBook.setTypeface(NotoSans);

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

	}
	
	public static void PrepareAddNewReceiverPageActionBar(
			ActionBarActivity activity, ActionBar actionBar, Boolean isRegistred, String edit_receiver_heading) {
		// <-Address book

		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbar_back_toaddressbook, null);

		TextView tvAddressBook = (TextView) v.findViewById(R.id.tvAddressBook);
		tvAddressBook.setText(edit_receiver_heading);
		
		tvAddressBook.setOnClickListener((OnClickListener) activity);

		Typeface NotoSans = Typeface.createFromAsset(activity.getAssets(),
				"fonts/NotoSans-Regular.ttf");
		tvAddressBook.setTypeface(NotoSans);

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

	}

	public static void PrepareAmountPageActionBar_ComingFromAddressBook(
			ActionBarActivity activity, ActionBar actionBar, Boolean isRegistred) {
		// <-ADDRESS BOOK

		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(
				R.layout.actionbar_amountpage_from_addressbook, null);

		TextView tvfromAddressBook = (TextView) v
				.findViewById(R.id.tvfromAddressBook);
		tvfromAddressBook.setOnClickListener((OnClickListener) activity);
		Typeface NotoSans = Typeface.createFromAsset(activity.getAssets(),
				"fonts/NotoSans-Regular.ttf");
		tvfromAddressBook.setTypeface(NotoSans);

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

	}

	public static void PrepareAmountPageActionBar_ComingFromAddNewReceiver(
			ActionBarActivity activity, ActionBar actionBar, Boolean isRegistred) {
		// <- ADD NEW RECEIVER>
		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(
				R.layout.actionbar_amountpage_from_addnewreceiverpage, null);
		
		

		TextView tvtodNewReceiver = (TextView) v
				.findViewById(R.id.tvtodNewReceiver);
		tvtodNewReceiver.setOnClickListener((OnClickListener) activity);
		Typeface NotoSans = Typeface.createFromAsset(activity.getAssets(),
				"fonts/NotoSans-Regular.ttf");
		tvtodNewReceiver.setTypeface(NotoSans);

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

	}

	public static void PrepareRegistrationPageTwoActionBar(
			ActionBarActivity activity, ActionBar actionBar, Boolean isRegistred) {
		// <- YOUR DETAILS
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setDisplayShowTitleEnabled(true);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator
				.inflate(R.layout.actionbar_registrationpage_two, null);

		TextView tvBack = (TextView) v.findViewById(R.id.tvBack);
		tvBack.setOnClickListener((OnClickListener) activity);
		Typeface NotoSans = Typeface.createFromAsset(activity.getAssets(),
				"fonts/NotoSans-Regular.ttf");
		tvBack.setTypeface(NotoSans);

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	public static void PrepareReceversPageActionBar(ActionBarActivity activity,
			ActionBar actionBar, Boolean isRegistred) {

		// set custom view to action bar
		actionBar = activity.getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.actionbar_receivers_page, null);

		// if this is a registered user then we got to change the NavDrawer icon
		// colors and User Profile Icon colors

		if (isRegistred) {
			// view references
			ImageButton abThreeBars = (ImageButton) v
					.findViewById(R.id.abThreeBars);
			ImageButton abUserIcon = (ImageButton) v
					.findViewById(R.id.abUserIcon);

			// image changes : three bars
			Drawable highlighted_threeBars = activity.getResources()
					.getDrawable(R.drawable.pride_menu_enabled);
			abThreeBars.setImageDrawable(highlighted_threeBars);

			// image changes : user profile
			Drawable highlighted_userprofile = activity.getResources()
					.getDrawable(R.drawable.pride_profile_enabled);
			abUserIcon.setImageDrawable(highlighted_userprofile);

		}

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}
}
