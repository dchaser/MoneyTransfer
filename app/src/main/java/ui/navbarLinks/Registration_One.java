package ui.navbarLinks;

import models.AddressModel;
import models.SenderModel;
import ui.sendmoney.SelectTransactionScreen;
import utilities.AbstractNavDrawerActivity;
import utilities.DataContext;
import utilities.GlobalContext;
import utilities.InputValidator;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;
import viewmodels.SenderViewModel;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

public class Registration_One extends AbstractNavDrawerActivity implements
		OnClickListener {

	private EditText EtSenderFirstName;
	private EditText EtSenderLastName;
	private EditText EtMobile;
	private EditText EtEmail;
	private EditText EtStreetNo;
	private EditText EtSuburb;
	private EditText EtPostCode;
	private Spinner spnState;
	private ActionBar actionBar;
	private ImageButton btnToSecondRegistratioScreen;

	private SenderViewModel senderVM;
	private GlobalContext globalContext;
	private DataContext dContext;
	private String selectedState;

	private SenderModel mainSender;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//background image
		getWindow().setBackgroundDrawableResource(R.drawable.background_image);
		this.globalContext = (GlobalContext) getApplicationContext();
		this.dContext = this.globalContext
				.GetDataContext(Registration_One.this);
		//initialize views
		InitializeControls();

			//create senderVM
			this.senderVM = new SenderViewModel(this.dContext);
			this.mainSender = this.senderVM.SelectedSender;

		
		if (this.mainSender.GetId() != -1) {
			
			DisplayInfo();
		}
	}



	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btnToSecondRegistratioScreen:
			//validate fields
				if (ValidateControls()) {
					//intent
					Intent cameraIntent = new Intent(
							android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
					//fill details for senderVM
					FillSenderVMFromUI();
					startActivityForResult(cameraIntent, CAMERA_REQUEST);

				} else {
					
					Crouton.makeText(this, "Please fill in the required details",
							Style.ALERT).show();
				}

			break;


		case R.id.abRegister:
			// action bar title tap
			onBackPressed();

			break;

		}
	}

	

	private void DisplayInfo() {

		if (this.senderVM.SelectedSender.GetId() != -1) {

			this.EtSenderFirstName.setText(this.mainSender.GetFirstName());
			this.EtSenderLastName.setText(this.mainSender.GetLastName());
			this.EtMobile.setText(this.mainSender.GetMobile());
			this.EtEmail.setText(this.mainSender.GetEmail());

			AddressModel senderCurrentAddress = this.mainSender.GetAddress();

			this.EtStreetNo.setText(senderCurrentAddress.GetStNo());
			this.EtSuburb.setText(senderCurrentAddress.GetSuburb());
			this.EtPostCode.setText(senderCurrentAddress.GetPostCode());
			
			//get state for state spinner
			this.selectedState = senderCurrentAddress.GetState();
			// set spinner position

			for (int i = 0; i < COUNTRIES.length; i++) {
				if (COUNTRIES[i].equals(this.selectedState)) {
					spnState.setSelection(i);
				}
			}
		} else {
			//make the user fill up the user details and get ready for button click
			Crouton.makeText(Registration_One.this, "Fill Up User Details",
					Style.INFO).show();

		}
	}



	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {

			//get photo
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			//attach to VM
			this.senderVM.SenderIdentity = photo;

			Intent i = new Intent(Registration_One.this,
					Registration_Three.class);
			i.putExtra("senderVM", this.senderVM);
			i.putExtra("photoAttached", true);
			startActivity(i);
			finish();
		}

	}

	private boolean ValidateControls() {

		// validation code before sending senderVM to AmountScreen
		/*
		 * 1)Name 2)Mobile Number 3)Email 4)Address Line One 5)Suburb 6)Post
		 * Code 7)State
		 */

		int controlCounter = 7;

		if (EtSenderFirstName.getText().toString().equalsIgnoreCase("")) {

			EtSenderFirstName.setError("Please enter name");
			EtMobile.requestFocus();
			controlCounter--;
		} else {
			EtSenderFirstName.setError(null);
			EtMobile.requestFocus();
		}

		if (EtMobile.getText().toString().equalsIgnoreCase("")) {

			EtMobile.setError("Please enter mobile");
			EtEmail.requestFocus();
			controlCounter--;
		} else {
			EtMobile.setError(null);
			EtEmail.requestFocus();
		}

		if (!InputValidator.isEmailAddress(this.EtEmail, true)) {

			EtEmail.setError("Please Enter a Valid Email Address");
			EtStreetNo.requestFocus();
			controlCounter--;
		} else {

			EtEmail.setError(null);
			EtStreetNo.requestFocus();

		}

		if (EtStreetNo.getText().toString().equalsIgnoreCase("")) {

			EtStreetNo.setError("Please enter Address");
			EtSuburb.requestFocus();
			controlCounter--;
		} else {
			EtStreetNo.setError(null);
			EtSuburb.requestFocus();
		}

		if (EtSuburb.getText().toString().equalsIgnoreCase("")) {

			EtSuburb.setError("Please enter Suburb");
			EtPostCode.requestFocus();
			controlCounter--;
		} else {
			EtSuburb.setError(null);
			EtPostCode.requestFocus();
		}

		if (EtPostCode.getText().toString().equalsIgnoreCase("")) {

			EtPostCode.setError("Please enter Post Code");
			spnState.requestFocus();
			controlCounter--;

		} else {
			EtPostCode.setError("", null);
			spnState.requestFocus();
		}

		if (spnState.getSelectedItemPosition() == 0) {
			Crouton.makeText(this, "Please select State from dropdown",
					Style.ALERT).show();
			controlCounter--;
		}

		if (controlCounter < 7) {
			this.EtPostCode.clearFocus();
			return false;
		} else {
			this.EtPostCode.clearFocus();
			return true;
		}
	}

	private void FillSenderVMFromUI() {

		this.senderVM.SelectedSender.SetFirstName(EtSenderFirstName.getText()
				.toString());
		this.senderVM.SelectedSender.SetLastName(EtSenderLastName.getText()
				.toString());
		this.senderVM.SelectedSender.SetMobile(EtMobile.getText().toString());
		this.senderVM.SelectedSender.SetEmail(EtEmail.getText().toString());

		// prepare address
		AddressModel newSenderAddress = new AddressModel();

		newSenderAddress.SetStNo(EtStreetNo.getText().toString());
		newSenderAddress.SetSuburb(EtSuburb.getText().toString());
		newSenderAddress.SetPostCode(EtPostCode.getText().toString());

		String state = (String) spnState.getSelectedItem();
		// set the state of Sender's Address
		newSenderAddress.SetState(state);

		this.senderVM.SelectedSender.SetAddress(newSenderAddress);

	}

	public void actionBarClick(View v) {

		switch (v.getId()) {
		case R.id.abRegister:

			onBackPressed();

			break;

		case R.id.abUserIcon:
			Crouton.makeText(this, "Settings Tapped", Style.INFO).show();
			break;
		}
	}

	@Override
	protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		NavDrawerItem[] menu;
		if (globalContext.IsRegistred()) {

			// navItems : elements of the menu (section/items)
			menu = new NavDrawerItem[] {
					NavMenuItem.create(99, "Home", "completed_tick_drawer",
							false, this),
					NavMenuSection.create(100, "HISTORY"),
					NavMenuItem.create(101, "Completed",
							"completed_tick_drawer", false, this),
					NavMenuSection.create(200, "HELP"),
					NavMenuItem
							.create(202, "Tutorial", "tutorial", false, this),
					NavMenuItem.create(203, "About", "about_man_drawer", false,
							this),
					NavMenuItem.create(204, "Feedback", "about_man_drawer",
							false, this),
					NavMenuSection.create(300, "My Profile"),
					NavMenuItem.create(301, "My Details", "about_man_drawer",
							false, this),
					NavMenuItem.create(302, "Receivers", "about_man_drawer",
							false, this) };

		} else {

			// navItems : elements of the menu (section/items)
			menu = new NavDrawerItem[] {
					NavMenuItem.create(99, "Home", "completed_tick_drawer",
							false, this),
					NavMenuSection.create(100, "HISTORY"),
					NavMenuItem.create(101, "Completed",
							"completed_tick_drawer", false, this),
					NavMenuSection.create(200, "HELP"),
					NavMenuItem
							.create(202, "Tutorial", "tutorial", false, this),
					NavMenuItem.create(203, "About", "about_man_drawer", false,
							this),
					NavMenuItem.create(204, "Feedback", "about_man_drawer",
							false, this) };

		}

		NavDrawerActivityConfiguration navConfig = new NavDrawerActivityConfiguration();

		// layout of the activity (the one with the DrawerLayout component)
		navConfig.setMainLayout(R.layout.activity_registration_one);

		// id of the DrawerLayout component
		navConfig.setDrawerLayoutId(R.id.drawer_layout);

		// id of the component to slide from the left (ListView)
		navConfig.setLeftDrawerId(R.id.left_drawer);

		// actionMenuItemsToHideWhenDrawerOpen : menu items of the action bar to
		// hide when the drawer is opened.

		// navItems : elements of the menu (section/items)
		navConfig.setNavItems(menu);

		// drawerShadow : drawable for the shadow of the menu
		navConfig.setDrawerShadow(R.drawable.drawer_shadow);

		// drawerOpenDesc : description of opened drawer (accessibility)
		navConfig.setDrawerOpenDesc(R.string.drawer_open);

		// drawerCloseDesc : description of closed drawer (accessibility)
		navConfig.setDrawerCloseDesc(R.string.drawer_close);

		// baseAdapter : adapter
		navConfig.setBaseAdapter(new NavigationDrawerAdapter(this,
				R.layout.navdrawer_item, menu));

		return navConfig;
	}

	@Override
	protected void onNavItemSelected(int id) {
		Intent intent;
		switch ((int) id) {
		case 99:
			Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_One.this,
					SelectTransactionScreen.class);
			startActivity(intent);
			break;
		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_One.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_One.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_One.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_One.this, FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_One.this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipient Manager", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(Registration_One.this, ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

	public void InitializeControls() {

		EtSenderFirstName = (EditText) UserInterfaceHelper.InitializeEditText(
				this, R.id.etfirstName, EtSenderFirstName);
		EtSenderLastName = (EditText) UserInterfaceHelper.InitializeEditText(
				this, R.id.etlastName, EtSenderLastName);
		EtMobile = (EditText) UserInterfaceHelper.InitializeEditText(this,
				R.id.etPhoneOne, EtMobile);
		EtEmail = (EditText) UserInterfaceHelper.InitializeEmailEditText(this,
				R.id.etEmail, EtEmail);
		EtStreetNo = (EditText) UserInterfaceHelper.InitializeEditText(this,
				R.id.etStreetNo, EtStreetNo);
		EtSuburb = (EditText) UserInterfaceHelper.InitializeEditText(this,
				R.id.etSuburb, EtSuburb);
		EtPostCode = (EditText) UserInterfaceHelper.InitializeEditText(this,
				R.id.etPostCode, EtPostCode);
		spnState = (Spinner) findViewById(R.id.spnState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, COUNTRIES);
		spnState.setAdapter(adapter);
		spnState.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long arg3) {
				SpinnerChanged(parent.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

//		// show btnSenderUpdate
//		this.btnsenderUpdate = (Button) findViewById(R.id.btnSenderUpdate);
//		btnsenderUpdate.setOnClickListener(this);

		btnToSecondRegistratioScreen = (ImageButton) findViewById(R.id.btnToSecondRegistratioScreen);
		btnToSecondRegistratioScreen.setOnClickListener(this);

		globalContext = (GlobalContext) getApplicationContext();

		UserInterfaceHelper.PrepareRegistrationPageActionBar(this, actionBar,
				globalContext.IsRegistred());

		SetFontStyling();
	}

	private void SetFontStyling() {

		Typeface NotoSans = Typeface.createFromAsset(this.getAssets(),
				"fonts/NotoSans-Regular.ttf");

		TextView tvFirstSectionHeading = (TextView) findViewById(R.id.tvFirstSectionHeading);
		tvFirstSectionHeading.setTypeface(NotoSans);
		tvFirstSectionHeading.setText("Your details");

		TextView tvfName = (TextView) findViewById(R.id.tvfName);
		tvfName.setTypeface(NotoSans);
		tvfName.setText("first name:");

		TextView tvlName = (TextView) findViewById(R.id.tvlName);
		tvlName.setTypeface(NotoSans);
		tvlName.setText("last name:");

		TextView tvPhoneOne = (TextView) findViewById(R.id.tvPhoneOne);
		tvPhoneOne.setTypeface(NotoSans);
		tvPhoneOne.setText("mobile:");

		TextView tvEmail = (TextView) findViewById(R.id.tvEmail);
		tvEmail.setTypeface(NotoSans);
		tvEmail.setText("email:");

		TextView tvStreetNo = (TextView) findViewById(R.id.tvStreetNo);
		tvStreetNo.setTypeface(NotoSans);
		tvStreetNo.setText("address:");

		TextView tvSuburb = (TextView) findViewById(R.id.tvSuburb);
		tvSuburb.setTypeface(NotoSans);
		tvSuburb.setText("suburb:");

		TextView tvPostCode = (TextView) findViewById(R.id.tvPostCode);
		tvPostCode.setTypeface(NotoSans);
		tvPostCode.setText("postcode:");

		TextView tvSpinnerState = (TextView) findViewById(R.id.tvSpinnerState);
		tvSpinnerState.setTypeface(NotoSans);
		tvSpinnerState.setText("state:");

		TextView tvSecondSectionHeading = (TextView) findViewById(R.id.tvSecondSectionHeading);
		tvSecondSectionHeading.setTypeface(NotoSans);
		tvSecondSectionHeading.setText("Your details");

		TextView tv_take_photo_description = (TextView) findViewById(R.id.tv_take_photo_description);
		tv_take_photo_description.setTypeface(NotoSans);
		tv_take_photo_description
				.setText(Html
						.fromHtml("We need to verify your details against a valid Australian ID. This is a <b>legal requirement</b> and your data will not be shared.Please see our privacy policy here. Please take a clear photo of your passport or drivers license and agree to our privacy policy by clicking the button below."));

	}

	private void SpinnerChanged(Object selectedItem) {

		this.selectedState = (String) selectedItem;
	}

	private static final String[] COUNTRIES = new String[] { "", "VICTORIA",
			"NEW SOUTH WALES", "QUEENSLAND", "WESTERN AUSTRALIA",
			"SOUTH AUSTRALIA", "NOTHERN TERRITORY", "TASMANIA", "CANBERRA" };

	private static final int CAMERA_REQUEST = 1888;

}
