package ui.navbarLinks;

import models.BankModel;
import models.ReceiverModel;
import ui.sendmoney.AmountScreen;
import ui.sendmoney.SelectTransactionScreen;
import utilities.AbstractNavDrawerActivity;
import utilities.GlobalContext;
import utilities.InputValidator;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.ReceiverDashboardCommunicator;
import utilities.UserInterfaceHelper;
import viewmodels.SenderViewModel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

import fragments.AddNewBank;
import fragments.AddNewReceiver;

public class AddReceipients extends AbstractNavDrawerActivity implements
		OnClickListener, ReceiverDashboardCommunicator {
	// ActionBar
	
	FrameLayout actionBarContainer;
	int abContainerViewID;
	private FragmentTabHost mTabHost;
	

	ActionBar actionBar;
	SenderViewModel SenderVM;
	public Button BtnNext;

	ReceiverModel newReceiver;
	BankModel newBank;

	GlobalContext globalContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		this.SenderVM = new SenderViewModel(
				globalContext.GetDataContext(AddReceipients.this));

		InitializeControls();
		prepareTabHost();

		mTabHost.getTabWidget().getChildTabViewAt(0)
				.setBackgroundColor(Color.parseColor("#FFC107"));
		mTabHost.getTabWidget().getChildTabViewAt(1)
				.setBackgroundColor(Color.parseColor("#455A64"));
	}

	private void InitializeControls() {

		this.BtnNext = (Button) findViewById(R.id.btnOK);
		SetFontStyling();
		this.BtnNext.setOnClickListener(this);

		globalContext = (GlobalContext) getApplicationContext();
		UserInterfaceHelper.PrepareReceversPageActionBar(AddReceipients.this,
				this.actionBar, globalContext.IsRegistred());
	}

	private void SetFontStyling() {

		Typeface NotoSans = Typeface.createFromAsset(this.getAssets(),
				"fonts/NotoSans-Regular.ttf");

		this.BtnNext.setTypeface(NotoSans);
		this.BtnNext.setText("Next");
	}

	public void actionBarClick(View v) {

		switch (v.getId()) {
		case R.id.abThreeBars:

			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			break;

		case R.id.abUserIcon:
			Toast.makeText(this, "Settings Tapped", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void prepareTabHost() {

		mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);

		final LayoutInflater inflator = getLayoutInflater();

		View receiver_tab = inflator.inflate(
				R.layout.receiver_tab_header_indicator, null);

		TextView tvReceiverTabHeader = (TextView) receiver_tab
				.findViewById(R.id.tvReceiverTabHeader);
		Typeface NotoSans = Typeface.createFromAsset(this.getAssets(),
				"fonts/NotoSans-Bold.ttf");
		tvReceiverTabHeader.setTypeface(NotoSans);
		tvReceiverTabHeader.setText("Receiver");

		View bank_tab = inflator.inflate(R.layout.banks_tab_header_indicator,
				null);

		TextView tvBankHeader = (TextView) bank_tab
				.findViewById(R.id.tvBankHeader);
		tvBankHeader.setTypeface(NotoSans);
		tvBankHeader.setText("Bank Details");

		// Confirmation Tab Bundle Preparation
		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator(receiver_tab),
				AddNewReceiver.class, null);

		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator(bank_tab),
				AddNewBank.class, null);

		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String tabId) {

				switch (tabId) {
				case "tab1":

					mTabHost.getTabWidget().getChildTabViewAt(0)
							.setBackgroundColor(Color.parseColor("#FFC107"));
					mTabHost.getTabWidget().getChildTabViewAt(1)
							.setBackgroundColor(Color.parseColor("#455A64"));
					break;

				case "tab2":

					mTabHost.getTabWidget().getChildTabViewAt(1)
							.setBackgroundColor(Color.parseColor("#FFC107"));
					mTabHost.getTabWidget().getChildTabViewAt(0)
							.setBackgroundColor(Color.parseColor("#455A64"));
					break;

				}

			}

		});

	}

	private void HideVisibilityOfVirtualKeyPad() {

		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mTabHost.getWindowToken(), 0);
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
		navConfig.setMainLayout(R.layout.add_receipients);

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
			intent = new Intent(AddReceipients.this,
					SelectTransactionScreen.class);
			startActivity(intent);
			break;
		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(AddReceipients.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(AddReceipients.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(AddReceipients.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(AddReceipients.this, FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(AddReceipients.this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipient Manager", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(AddReceipients.this, ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

	private void ShowToast() {
		Toast.makeText(this, "Please enter required information",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onClick(View v) {

		Intent e = getIntent();
		boolean comingFromJumpList = e.getBooleanExtra("comingFromJumpList",
				false);

		switch (mTabHost.getCurrentTab()) {

		case 0:
			// receiver tab
			BtnNext = (Button) findViewById(R.id.btnOK);
			BtnNext.setText("Add");

			mTabHost.getTabWidget().getChildTabViewAt(0)
					.setBackgroundColor(Color.parseColor("#FFC107"));
			mTabHost.getTabWidget().getChildTabViewAt(1)
					.setBackgroundColor(Color.parseColor("#455A64"));

			if (ValidationSuccess()) {
				mTabHost.setCurrentTab(1);
				HideVisibilityOfVirtualKeyPad();

			} else {
				mTabHost.setCurrentTab(0);
				ShowToast();
			}

			break;

		case 1:
			// bank tab

			mTabHost.getTabWidget().getChildTabViewAt(1)
					.setBackgroundColor(Color.parseColor("#FFC107"));
			mTabHost.getTabWidget().getChildTabViewAt(0)
					.setBackgroundColor(Color.parseColor("#455A64"));

			if (ValidateBankControls()) {

				HideVisibilityOfVirtualKeyPad();
				if (comingFromJumpList) {
					Intent a = new Intent(AddReceipients.this,
							AmountScreen.class);
					a.putExtra("senderVM", this.SenderVM);
					startActivity(a);
				} else {
					Intent a = new Intent(AddReceipients.this,
							ReceiverDashboard.class);
					startActivity(a);
				}

			} else {
				ShowToast();
			}

			break;
		}
	}

	private boolean ValidationSuccess() {

		AddNewReceiver fragReceiver = (AddNewReceiver) getSupportFragmentManager()
				.findFragmentByTag("tab1");

		int controlCounter = 4;

		// validation code before settin the Tab Host to Bank from receiver
		/*
		 * 1)Receiver Name 2)Receiver Mobile 3)Receiver Email 4)Receiver Full
		 * Address
		 */

		if (fragReceiver.EtReceiverFirstName.getText().toString()
				.equalsIgnoreCase("")) {
			fragReceiver.EtReceiverFirstName
					.setError("Please enter Receiver's full name");
			fragReceiver.EtReceiverPhoneOne.requestFocus();
			controlCounter--;
		} else {
			fragReceiver.EtReceiverFirstName.setError(null);
			fragReceiver.EtReceiverPhoneOne.requestFocus();
		}

		if (fragReceiver.EtReceiverPhoneOne.getText().toString()
				.equalsIgnoreCase("")) {
			fragReceiver.EtReceiverPhoneOne
					.setError("Please enter receiver's mobile number");
			fragReceiver.EtReceiverEmail.requestFocus();
			controlCounter--;
		} else {
			fragReceiver.EtReceiverPhoneOne.setError(null);
			fragReceiver.EtReceiverEmail.requestFocus();
		}

		if (!InputValidator.isEmailAddress(fragReceiver.EtReceiverEmail, true)) {
			fragReceiver.EtReceiverEmail.setError("Receiver email is invalid");
			fragReceiver.EtRecieverStreetNo.requestFocus();
			controlCounter--;
		} else {
			fragReceiver.EtReceiverEmail.setError(null);
			fragReceiver.EtRecieverStreetNo.requestFocus();
		}

		if (fragReceiver.EtRecieverStreetNo.getText().toString()
				.equalsIgnoreCase("")) {
			fragReceiver.EtRecieverStreetNo
					.setError("Receiver address is empty");
			fragReceiver.EtRecieverStreetNo.clearFocus();
			controlCounter--;
		} else {
			fragReceiver.EtRecieverStreetNo.setError(null);
			fragReceiver.EtRecieverStreetNo.clearFocus();
		}

		if (controlCounter < 4) {
			return false;
		} else {
			// firing fragment's fake onclick event
			fragReceiver.onClick(this.BtnNext);
			return true;
		}

	}

	private boolean ValidateBankControls() {

		// validation code before sending senderVM to AmountScreen
		/*
		 * 1)Receiver Name 2)Receiver Mobile Number 3)Receiver Email 4)Receiver
		 * Address Line One
		 */AddNewBank fragBank = (AddNewBank) getSupportFragmentManager()
				.findFragmentByTag("tab2");

		int controlCounter = 5;

		if (fragBank.EtRecieverBankName.getText().toString()
				.equalsIgnoreCase("")) {

			fragBank.EtRecieverBankName.setError("Please enter bank name");
			fragBank.EtRecieverBranchName.requestFocus();
			controlCounter--;
		} else {
			fragBank.EtRecieverBankName.setError(null);
			fragBank.EtRecieverBranchName.requestFocus();
		}

		if (fragBank.EtRecieverBranchName.getText().toString()
				.equalsIgnoreCase("")) {

			fragBank.EtRecieverBranchName.setError("Please enter branch name");
			fragBank.EtRecieverAccountNo.requestFocus();
			controlCounter--;
		} else {
			fragBank.EtRecieverBranchName.setError(null);
			fragBank.EtRecieverAccountNo.requestFocus();
		}

		if (fragBank.EtRecieverAccountNo.getText().toString()
				.equalsIgnoreCase("")) {

			fragBank.EtRecieverAccountNo
					.setError("Please enter valid account number");
			fragBank.EtRecieverAccountName.requestFocus();
			controlCounter--;
		} else {
			fragBank.EtRecieverAccountNo.setError(null);
			fragBank.EtRecieverAccountName.requestFocus();
		}

		if (fragBank.EtRecieverAccountName.getText().toString()
				.equalsIgnoreCase("")) {

			fragBank.EtRecieverAccountName
					.setError("Please enter valid account name");
			fragBank.EtRecieverBankCode.requestFocus();
			controlCounter--;
		} else {
			fragBank.EtRecieverAccountName.setError(null);
			fragBank.EtRecieverBankCode.requestFocus();
		}

		if (fragBank.EtRecieverBankCode.getText().toString()
				.equalsIgnoreCase("")) {

			fragBank.EtRecieverBankCode.setError("Please enter Bank code");
			fragBank.EtRecieverBankCode.clearFocus();
			controlCounter--;
		} else {
			fragBank.EtRecieverBankCode.setError(null);
			fragBank.EtRecieverBankCode.clearFocus();
		}

		if (controlCounter < 4) {
			return false;
		} else {
			fragBank.onClick(BtnNext);
			return true;
		}
	}

	@Override
	public void GetNewReceiver(ReceiverModel receiverToAdd) {

		// receiver from Interface
		this.newReceiver = receiverToAdd;
		// set Sender ID to global sender
		this.newReceiver.SetSenderId(this.SenderVM.SelectedSender.GetId());
		// make new one selected receiver
		this.SenderVM.ReceiverVM.SetSelectedReceiver(receiverToAdd);

	}

	@Override
	public void GetNewBank(BankModel bankToAdd) {

		// receiver
		this.newBank = bankToAdd;
		
		// set Receiver ID for new Bank
		this.newBank.SetReceiverID(this.SenderVM.ReceiverVM
				.GetSelectedReceiver().GetId());

		// attach
		this.SenderVM.ReceiverVM.bankVM.SetSelectedBank(bankToAdd);
	}

}
