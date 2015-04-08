package redesign.screens;

import java.io.IOException;
import java.util.List;

import models.BankModel;
import models.ReceiverModel;
import ui.navbarLinks.AboutPage;
import ui.navbarLinks.CompletedPage;
import ui.navbarLinks.FeedBackActivity;
import ui.navbarLinks.ReceiverDashboard;
import ui.navbarLinks.Registration_One;
import ui.navbarLinks.TutorialPage;
import ui.sendmoney.AmountScreen;
import ui.sendmoney.SelectTransactionScreen;
import utilities.AbstractNavDrawerActivity;
import utilities.DataContext;
import utilities.DbHelper;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;
import viewmodels.BankViewModel;
import viewmodels.ReceiverViewModel;
import viewmodels.SenderViewModel;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

public class RecieverNBankDetailsScreen extends AbstractNavDrawerActivity
		implements OnClickListener {

	// Receiver Controls
	public EditText EtRecieverBankName;
	public EditText EtRecieverBranchName;
	public EditText EtRecieverAccountNo;
	public EditText EtRecieverAccountName;
	public EditText EtRecieverBankCode;

	// Bank Controls
	public EditText EtReceiverFirstName;
	public EditText EtReceiverPhoneOne;
	public EditText EtReceiverEmail;
	public EditText EtRecieverStreetNo;

	// Ok Button
	private Button btnToAmountScreen;

	// Heading
	private TextView tvAddReceiverHeading;
	private TextView tvAddBankHeading;

	private ActionBar actionBar;
	private SenderViewModel SenderVM;
	public ReceiverViewModel recVM;
	public BankViewModel bankVM;

	private ReceiverModel newReceiver;
	private BankModel newBank;

	private GlobalContext globalContext;
	private DataContext dContext;
	public Boolean cameFromDashBoard = false;
	public long receiverIDToEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Window v = getWindow();
		v.setBackgroundDrawableResource(R.drawable.background_image);

		this.globalContext = (GlobalContext) getApplicationContext();
		this.dContext = this.globalContext
				.GetDataContext(RecieverNBankDetailsScreen.this);

		InitialiseControls();

		Intent i = getIntent();
		this.SenderVM = i.getParcelableExtra("senderVM");
		this.cameFromDashBoard = i.getBooleanExtra("cameFromDashBoard", false);
		this.receiverIDToEdit = i.getLongExtra("receiverIDToEdit", -1);

		if (this.cameFromDashBoard) {

			if (this.receiverIDToEdit == -1) {

				// user tryna create new Receiver from Dashboard
			} else {
				// editing for real

				this.tvAddReceiverHeading.setText("Edit receiver details");
				this.tvAddBankHeading.setText("Edit bank details");

				// set action bar text again with
				UserInterfaceHelper.PrepareAddNewReceiverPageActionBar(this,
						actionBar, this.globalContext.IsRegistred(),
						"Edit Receiver");

				this.btnToAmountScreen.setText("UPDATE");

				LoadReceiverToEdit();
			}

		}
	}

	private void LoadReceiverToEdit() {

		this.SenderVM = new SenderViewModel(this.dContext, true);
		this.recVM = new ReceiverViewModel(this.dContext);
		this.bankVM = new BankViewModel(this.dContext);

		ReceiverModel receiverToEdit = this.recVM
				.GetReceiverByID(this.receiverIDToEdit);
		
		this.SenderVM.ReceiverVM.SetSelectedReceiver(receiverToEdit);
		
		List<BankModel> banks = this.bankVM
				.GetBanksByReceiverID(receiverToEdit.GetId());
		this.SenderVM.ReceiverVM.bankVM.SetBanks(banks);
		this.SenderVM.ReceiverVM.bankVM.SetSelectedBank(banks.get(0));
		
		FillUIFromReceiverAndBank();
	}

	private void FillUIFromReceiverAndBank() {

		ReceiverModel receiverToFIll = new ReceiverModel();
		receiverToFIll = this.SenderVM.ReceiverVM.GetSelectedReceiver();

		// receiver details
		if (receiverToFIll.GetReceiverFullName() != null) {

			EtReceiverFirstName.setText(receiverToFIll.GetReceiverFullName());
		}
		
		if(receiverToFIll.GetReceiverMobile() != null){
			EtReceiverPhoneOne.setText(receiverToFIll.GetReceiverMobile());	
		}

		if(receiverToFIll.GetReceiverEmail() != null){
			EtReceiverEmail.setText(receiverToFIll.GetReceiverEmail());	
		}
	
		if(receiverToFIll.GetReceiverAddress() != null){
			EtRecieverStreetNo.setText(receiverToFIll.GetReceiverAddress());
		}
		
		BankModel bankToFill = new BankModel();
		bankToFill = this.SenderVM.ReceiverVM.bankVM.GetSelectedBank();
		
		// bank details
		if(bankToFill.GetBankName() != null){
			EtRecieverBankName.setText(bankToFill.GetBankName());
		}

		if(bankToFill.GetAcountName() != null){
			EtRecieverAccountName.setText(bankToFill.GetAcountName());
		}
		
		if(bankToFill.GetBankCode() != null){
			EtRecieverBankCode.setText(bankToFill.GetBankCode());
		}
		
		if(bankToFill.GetAccountID() != null){
			EtRecieverAccountNo.setText(bankToFill.GetAccountID());
		}
		
		if(bankToFill.GetBranchName() != null){
			EtRecieverBranchName.setText(bankToFill.GetBranchName());
		}

	}

	private void InitialiseControls() {

		UserInterfaceHelper.PrepareAddNewReceiverPageActionBar(this, actionBar,
				this.globalContext.IsRegistred());

		this.btnToAmountScreen = (Button) findViewById(R.id.btnToAmountScreen);

		this.btnToAmountScreen.setOnClickListener(this);

		// Receiver Controls...

		this.EtReceiverFirstName = (EditText) UserInterfaceHelper
				.InitializeEditText(RecieverNBankDetailsScreen.this,
						R.id.etRecieverFullName, this.EtReceiverFirstName);

		this.EtReceiverPhoneOne = (EditText) UserInterfaceHelper
				.InitializeEditText(RecieverNBankDetailsScreen.this,
						R.id.etRecieverPhoneOne, this.EtReceiverPhoneOne);

		this.EtReceiverEmail = (EditText) UserInterfaceHelper
				.InitializeEmailEditText(RecieverNBankDetailsScreen.this,
						R.id.etRecieverEmail, this.EtReceiverEmail);
		this.EtRecieverStreetNo = (EditText) UserInterfaceHelper
				.InitializeEditText(RecieverNBankDetailsScreen.this,
						R.id.etRecieverStreetNo, this.EtRecieverStreetNo);

		// Bank Controls...

		this.EtRecieverBankName = (EditText) UserInterfaceHelper
				.InitializeEditText(RecieverNBankDetailsScreen.this,
						R.id.etRecieverBankName, this.EtRecieverBankName);

		this.EtRecieverBranchName = (EditText) UserInterfaceHelper
				.InitializeEditText(RecieverNBankDetailsScreen.this,
						R.id.etRecieverBranchName, this.EtRecieverBranchName);

		this.EtRecieverAccountNo = (EditText) UserInterfaceHelper
				.InitializeEditText(RecieverNBankDetailsScreen.this,
						R.id.etRecieverAccountNo, this.EtRecieverAccountNo);
		this.EtRecieverAccountName = (EditText) UserInterfaceHelper
				.InitializeEditText(RecieverNBankDetailsScreen.this,
						R.id.etRecieverAccountName, this.EtRecieverAccountName);

		this.EtRecieverBankCode = (EditText) UserInterfaceHelper
				.InitializeEditText(RecieverNBankDetailsScreen.this,
						R.id.etRecieverBankCode, this.EtRecieverBankCode);

		SetFontStyling();
	}

	private void SetFontStyling() {

		Typeface NotoSans = Typeface.createFromAsset(
				RecieverNBankDetailsScreen.this.getAssets(),
				"fonts/NotoSans-Regular.ttf");

		this.tvAddReceiverHeading = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.addNewReceiverFragment);
		tvAddReceiverHeading.setTypeface(NotoSans);

		this.tvAddBankHeading = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvAddBankHeading);

		TextView tvReceiverName = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvReceiverName);
		tvReceiverName.setTypeface(NotoSans);

		TextView tvReceiverPhone = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvReceiverPhone);
		tvReceiverPhone.setTypeface(NotoSans);

		TextView tvReceiverEmail = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvReceiverEmail);
		tvReceiverEmail.setTypeface(NotoSans);

		TextView receiverFullAddress = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.receiverFullAddress);
		receiverFullAddress.setTypeface(NotoSans);

		TextView tvAddNewBankHeading = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvAddBankHeading);
		tvAddNewBankHeading.setTypeface(NotoSans);

		TextView tvRecieverBankName = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvRecieverBankName);
		tvRecieverBankName.setTypeface(NotoSans);

		TextView tvRecieverAccountName = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvRecieverAccountName);
		tvRecieverAccountName.setTypeface(NotoSans);

		TextView tvRecieverBankCode = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvRecieverBankCode);
		tvRecieverBankCode.setTypeface(NotoSans);

		TextView tvRecieverAccountNo = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvRecieverAccountNo);
		tvRecieverAccountNo.setTypeface(NotoSans);

		TextView tvRecieverBranchName = (TextView) RecieverNBankDetailsScreen.this
				.findViewById(R.id.tvRecieverBranchName);
		tvRecieverBranchName.setTypeface(NotoSans);

		this.btnToAmountScreen.setTypeface(NotoSans);
	}

	private void FillViewModelsFromUI() {

		this.newReceiver = new ReceiverModel();
		this.newBank = new BankModel();

		newReceiver.SetReceiverFullName(this.EtReceiverFirstName.getText()
				.toString());
		newReceiver.SetReceiverMobile(this.EtReceiverPhoneOne.getText()
				.toString());
		newReceiver.SetReceiverEmail(this.EtReceiverEmail.getText().toString());
		newReceiver.SetReceiverAddress((this.EtRecieverStreetNo.getText()
				.toString()));
		newReceiver.SetId(this.receiverIDToEdit);
		// set Sender ID to global sender
		this.newReceiver.SetSenderId(this.SenderVM.SelectedSender.GetId());
		// make new one selected receiver
		this.SenderVM.ReceiverVM.SetSelectedReceiver(newReceiver);

		// bank Name
		this.newBank.SetBankName(this.EtRecieverBankName.getText().toString());
		// account name
		this.newBank.SetAcountName(this.EtRecieverAccountName.getText()
				.toString());
		// bank code
		this.newBank.SetBankCode(EtRecieverBankCode.getText().toString());
		// account number/id
		this.newBank.SetAccountID(EtRecieverAccountNo.getText().toString());
		// branch name
		this.newBank.SetBranchName(this.EtRecieverBranchName.getText()
				.toString());
		if(this.receiverIDToEdit != -1){
			this.newBank.SetId(this.SenderVM.ReceiverVM.bankVM.GetSelectedBank()
					.GetId());	
		}
		
		

		// set Receiver ID for new Bank
		this.newBank.SetReceiverID(this.receiverIDToEdit);
		// attach
		this.SenderVM.ReceiverVM.bankVM.SetSelectedBank(newBank);

		this.globalContext = (GlobalContext) getApplicationContext();
		this.SenderVM.SetContext(globalContext
				.GetDataContext(RecieverNBankDetailsScreen.this));
		this.SenderVM.ReceiverVM.SetContext(globalContext
				.GetDataContext(RecieverNBankDetailsScreen.this));
		this.SenderVM.ReceiverVM.bankVM.SetContext(globalContext
				.GetDataContext(RecieverNBankDetailsScreen.this));
		this.SenderVM.UpdateSender();

		try {
			DbHelper.writeToSD(RecieverNBankDetailsScreen.this);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private boolean ValidateReceiverControls() {

		// validation code before sending senderVM to AmountScreen
		/*
		 * 1)Receiver Name 2)Receiver Mobile Number 3)Receiver Email 4)Receiver
		 * Address Line One
		 */

		int controlCounter = 4;

		if (EtReceiverFirstName.getText().toString().equalsIgnoreCase("")) {

			EtReceiverFirstName.setError("Please enter receiver's name");
			EtReceiverPhoneOne.requestFocus();
			controlCounter--;
		} else {
			EtReceiverFirstName.setError(null);
			EtReceiverPhoneOne.requestFocus();
		}

		if (EtReceiverPhoneOne.getText().toString().equalsIgnoreCase("")) {

			EtReceiverPhoneOne.setError("Please enter receiver's mobile");
			EtReceiverEmail.requestFocus();
			controlCounter--;
		} else {
			EtReceiverPhoneOne.setError(null);
			EtReceiverEmail.requestFocus();
		}

		if (EtReceiverEmail.getText().toString().equalsIgnoreCase("")) {

			EtReceiverEmail.setError("Please enter receiver's email");
			EtRecieverStreetNo.requestFocus();
			controlCounter--;
		} else {
			EtReceiverEmail.setError(null);
			EtRecieverStreetNo.requestFocus();
		}

		if (EtRecieverStreetNo.getText().toString().equalsIgnoreCase("")) {

			EtRecieverStreetNo.setError("Please enter receiver's Address");
			EtRecieverStreetNo.clearFocus();
			controlCounter--;
		} else {
			EtRecieverStreetNo.setError(null);
			EtRecieverStreetNo.clearFocus();
		}

		if (controlCounter < 4) {
			return false;
		} else {
			return true;
		}
	}

	private boolean ValidateBankControls() {

		// validation code before sending senderVM to AmountScreen
		/*
		 * 1)Receiver Name 2)Receiver Mobile Number 3)Receiver Email 4)Receiver
		 * Address Line One
		 */

		int controlCounter = 5;

		if (EtRecieverBankName.getText().toString().equalsIgnoreCase("")) {

			EtRecieverBankName.setError("Please enter bank name");
			EtRecieverBranchName.requestFocus();
			controlCounter--;
		} else {
			EtRecieverBankName.setError(null);
			EtRecieverBranchName.requestFocus();
		}

		if (EtRecieverBranchName.getText().toString().equalsIgnoreCase("")) {

			EtRecieverBranchName.setError("Please enter branch name");
			EtRecieverAccountNo.requestFocus();
			controlCounter--;
		} else {
			EtRecieverBranchName.setError(null);
			EtRecieverAccountNo.requestFocus();
		}

		if (EtRecieverAccountNo.getText().toString().equalsIgnoreCase("")) {

			EtRecieverAccountNo.setError("Please enter valid account number");
			EtRecieverAccountName.requestFocus();
			controlCounter--;
		} else {
			EtRecieverAccountNo.setError(null);
			EtRecieverAccountName.requestFocus();
		}

		if (EtRecieverAccountName.getText().toString().equalsIgnoreCase("")) {

			EtRecieverAccountName.setError("Please enter valid account name");
			EtRecieverBankCode.requestFocus();
			controlCounter--;
		} else {
			EtRecieverAccountName.setError(null);
			EtRecieverBankCode.requestFocus();
		}

		if (EtRecieverBankCode.getText().toString().equalsIgnoreCase("")) {

			EtRecieverBankCode.setError("Please enter Bank code");
			EtRecieverBankCode.clearFocus();
			controlCounter--;
		} else {
			EtRecieverBankCode.setError(null);
			EtRecieverBankCode.clearFocus();
		}

		if (controlCounter < 4) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		// globalContext.SetRegistered(true);
		NavDrawerItem[] menu;
		if (globalContext.IsRegistred()) {

			// navItems : elements of the menu (section/items)
			menu = new NavDrawerItem[] {
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
		navConfig.setMainLayout(R.layout.receiver_and_bank_details_screen);

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
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onNavItemSelected(int id) {
		Intent intent;
		switch ((int) id) {
		case 99:
			intent = new Intent(RecieverNBankDetailsScreen.this,
					SelectTransactionScreen.class);
			startActivity(intent);
			break;

		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(RecieverNBankDetailsScreen.this,
					CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(RecieverNBankDetailsScreen.this,
					TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(RecieverNBankDetailsScreen.this,
					AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(RecieverNBankDetailsScreen.this,
					FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(RecieverNBankDetailsScreen.this,
					Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipients", Toast.LENGTH_SHORT).show();
			intent = new Intent(RecieverNBankDetailsScreen.this,
					ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

	private void HideVisibilityOfVirtualKeyPad() {

		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnToAmountScreen:

			HideVisibilityOfVirtualKeyPad();
			if (ValidateBankControls() && ValidateReceiverControls()) {

				FillViewModelsFromUI();

				if (this.cameFromDashBoard == true) {

					Intent b = new Intent(RecieverNBankDetailsScreen.this,
							ReceiverDashboard.class);
					startActivity(b);
					finish();
				} else if (this.cameFromDashBoard == false) {

					Intent a = new Intent(RecieverNBankDetailsScreen.this,
							AmountScreen.class);

					a.putExtra("senderVM", this.SenderVM);
					a.putExtra("brandnewreceiver", true);
					startActivity(a);
					finish();
				} else {
					ShowToast();
				}
			}

			break;

		case R.id.tvAddressBook:

			onBackPressed();
			break;

		default:
			break;
		}
	}

	private void ShowToast() {
		Toast.makeText(this, "Please enter required information",
				Toast.LENGTH_SHORT).show();
	}

}
