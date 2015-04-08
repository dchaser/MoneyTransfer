package ui.pending;

import ui.navbarLinks.AboutPage;
import ui.navbarLinks.CompletedPage;
import ui.navbarLinks.FeedBackActivity;
import ui.navbarLinks.ReceiverDashboard;
import ui.navbarLinks.Registration_One;
import ui.navbarLinks.TutorialPage;
import ui.sendmoney.SelectTransactionScreen;
import utilities.AbstractNavDrawerActivity;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import viewmodels.AddressViewModel;
import viewmodels.AmountViewModel;
import viewmodels.BankViewModel;
import viewmodels.ReceiverViewModel;
import viewmodels.SenderViewModel;
import models.AmountModel;
import models.BankModel;
import models.ReceiverModel;
import models.TransactionModel;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

@SuppressLint("InflateParams")
public class SingleTransactionPage extends AbstractNavDrawerActivity {

	// action bar
	ActionBar actionBar;
	FrameLayout actionBarContainer;
	int abContainerViewID;

	// VMs
	SenderViewModel senderVM;
	ReceiverViewModel recVM;
	AmountViewModel amountVM;
	AddressViewModel addressVM;
	BankViewModel bankVM;

	// trans
	TransactionModel trans;

	// sender
	TextView tv_Sender_Name;
	TextView tv_Sender_Phone;
	TextView tv_Sender_Address;
	TextView tv_Sender_Email;
	// receiver
	TextView tv_Receiver_Name;
	TextView tv_Receiver_Phone;
	TextView tv_Receiver_Bank;
	TextView tv_Receiver_AccountID;
	TextView tv_Receiver_BranchName;
	// transaction
	TextView tv_Transaction_Amount;
	TextView tv_Transaction_Status;
	TextView tv_Transaction_Sent_Amount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		InitialiseContrlsAndVMs();
		PrepareActionBar();
		LoadUIwithIntentData();
	}

	private void LoadUIwithIntentData() {

		trans = new TransactionModel();

		Intent i = getIntent();
		trans = i.getParcelableExtra("trans");
		GlobalContext globalContext = (GlobalContext) getApplicationContext();

		this.senderVM.SetContext(globalContext.GetDataContext(this));

		// this.senderVM = new SenderViewModel(this);

		// Find the receiver
		ReceiverModel receiver = findReceiver(trans.GetReceiverID());
		GlobalContext context = (GlobalContext) getApplicationContext();

		this.senderVM.ReceiverVM.ChangeSelectedReceiver(receiver,
				context.GetDataContext(SingleTransactionPage.this));

		BankModel bank = findBank(trans.GetReceiverBankID());

		if (trans != null) {

			String fake = " ";

			String sender_name = this.senderVM.SelectedSender.GetFirstName() != null ? this.senderVM.SelectedSender
					.GetFirstName() : fake;
			this.tv_Sender_Name.setText(sender_name);

			String sender_phone = this.senderVM.SelectedSender.GetMobile() != null ? this.senderVM.SelectedSender
					.GetMobile() : fake;
			this.tv_Sender_Phone.setText(sender_phone);

			String sender_address = this.senderVM.SelectedSender.GetAddress()
					.toString() != null ? this.senderVM.SelectedSender
					.GetAddress().toString() : fake;
			this.tv_Sender_Address.setText(sender_address);

			String sender_email = this.senderVM.SelectedSender.GetEmail() != null ? this.senderVM.SelectedSender
					.GetEmail() : fake;
			this.tv_Sender_Email.setText(sender_email);

			String receiver_firstname = receiver.GetReceiverFullName() != null ? receiver
					.GetReceiverFullName() : fake;
			this.tv_Receiver_Name.setText(receiver_firstname);

			String receiver_Phone = receiver.GetReceiverMobile() != null ? receiver
					.GetReceiverMobile() : fake;
			this.tv_Receiver_Phone.setText(receiver_Phone);

			String receiver_bank = bank.GetBankName() != null ? bank
					.GetBankName() : fake;
			this.tv_Receiver_Bank.setText(receiver_bank);

			String receiver_AccountID = bank.GetAccountID() != null ? bank
					.GetAccountID() : fake;
			this.tv_Receiver_AccountID.setText(receiver_AccountID);

			String receiver_BranchName = bank.GetBranchName() != null ? bank
					.GetBranchName() : fake;
			this.tv_Receiver_BranchName.setText(receiver_BranchName);

			this.amountVM = new AmountViewModel(
					globalContext.GetDataContext(this));

			AmountModel amtModel = this.amountVM.GetAmountByID(this.trans
					.GetAmoutID());

			String Transaction_Amount = amtModel.GetAmtReceived().toString() != null ? amtModel
					.GetAmtReceived().toString() : fake;
			this.tv_Transaction_Amount.setText(Transaction_Amount);

			String Transaction_Status = trans.GetStatus() != null ? trans
					.GetStatus() : fake;
			tv_Transaction_Status.setText(Transaction_Status);

			String Transaction_Sent_Amount = amtModel.GetAmtSend().toString() != null ? amtModel
					.GetAmtSend().toString() : fake;
			tv_Transaction_Sent_Amount.setText(Transaction_Sent_Amount);
		}

	}

	private void InitialiseContrlsAndVMs() {

		this.tv_Sender_Name = (TextView) findViewById(R.id.tv_SD_Name);
		this.tv_Sender_Phone = (TextView) findViewById(R.id.tv_SD_Phone);
		this.tv_Sender_Address = (TextView) findViewById(R.id.tv_SD_Address);
		this.tv_Sender_Email = (TextView) findViewById(R.id.tv_SD_email);

		this.tv_Receiver_Name = (TextView) findViewById(R.id.tv_RD_Name);
		this.tv_Receiver_Phone = (TextView) findViewById(R.id.tv_RD_Phone);
		this.tv_Receiver_Bank = (TextView) findViewById(R.id.tv_RD_bank);
		this.tv_Receiver_AccountID = (TextView) findViewById(R.id.tv_RD_Account);
		this.tv_Receiver_BranchName = (TextView) findViewById(R.id.tv_RD_Branch);

		this.tv_Transaction_Amount = (TextView) findViewById(R.id.tv_trans_amount);
		this.tv_Transaction_Status = (TextView) findViewById(R.id.tv_trans_status);
		this.tv_Transaction_Sent_Amount = (TextView) findViewById(R.id.tv_trans_sent_amount);

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		this.senderVM = new SenderViewModel(globalContext.GetDataContext(this));
		this.senderVM.SetContext(globalContext.GetDataContext(this));

		// this.senderVM = new SenderViewModel(this, false);
		this.recVM = new ReceiverViewModel(globalContext.GetDataContext(this));
		this.amountVM = new AmountViewModel(globalContext.GetDataContext(this));
		this.addressVM = new AddressViewModel(
				globalContext.GetDataContext(this));
		this.bankVM = new BankViewModel(globalContext.GetDataContext(this));
	}

	private ReceiverModel findReceiver(long receiverID) {

		for (ReceiverModel recMod : this.senderVM.ReceiverVM.GetReceivers()) {
			if (recMod.GetId() == receiverID) {
				return recMod;
			}
		}
		return null;
	}

	private BankModel findBank(long bankID) {

		for (BankModel bank : this.senderVM.ReceiverVM.bankVM.GetBanks()) {
			if (bank.GetId() == bankID) {
				return bank;
			}
		}
		return null;
	}

	public void PrepareActionBar() {
		// set custom view to action bar
		actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		abContainerViewID = getResources().getIdentifier(
				"action_bar_container", "id", "android");
		LayoutInflater myinflater = getLayoutInflater();
		View customView = myinflater.inflate(
				R.layout.gloabl_action_barcustom_layout, null);
		actionBarContainer = (FrameLayout) findViewById(abContainerViewID);
		actionBarContainer.addView(customView);

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent i = new Intent(this, PendingPage.class);
		startActivity(i);
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
		navConfig.setMainLayout(R.layout.single_pending_transfer);

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
			intent = new Intent(SingleTransactionPage.this,
					SelectTransactionScreen.class);
			startActivity(intent);
			break;
		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(SingleTransactionPage.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(SingleTransactionPage.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(SingleTransactionPage.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(SingleTransactionPage.this,
					FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(SingleTransactionPage.this,
					Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipient Manager", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(SingleTransactionPage.this,
					ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

}
