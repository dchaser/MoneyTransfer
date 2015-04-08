package ui.sendmoney;

import java.io.IOException;

import redesign.screens.MainScreen;
import ui.navbarLinks.AboutPage;
import ui.navbarLinks.CompletedPage;
import ui.navbarLinks.FeedBackActivity;
import ui.navbarLinks.ReceiverDashboard;
import ui.navbarLinks.Registration_One;
import ui.navbarLinks.TutorialPage;
import utilities.AbstractNavDrawerActivity;
import utilities.DbHelper;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;
import viewmodels.SenderViewModel;
import com.example.moneytransfer.R;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("InflateParams")
public class AccountNumberScreen extends AbstractNavDrawerActivity implements
		OnClickListener {

	// ActionBar
	ActionBar actionBar;

	// controls
	TextView tvCloudCode;
	TextView tvAmtAudFinal;
	TextView tvPleaseTransfer;

	public Button BtnOK;
	private SenderViewModel senderVM;
	public String cloudID;

	// ///...................
	TextView tvReceiverName;
	TextView tvReceiverBank;
	TextView tvReceiverAccountNumber;

	TextView tvSenderName;
	TextView tvSenderMobile;

	TextView tvAmountInDollars;
	TextView tvOurFee;
	TextView tvTotal;
	TextView tvRate;
	TextView tvAmtLkr;
	TextView tvCurrency;

	Button btnCancel;
	Button btnConfirm;
	
	GlobalContext globaContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// setContentView(R.layout.account_number_screen);
		InitializeControls();
		GetSenderViewModel();
		FillUpTextViews(this.senderVM);

		try {
			DbHelper.writeToSD(this);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void GetSenderViewModel() {

		Intent i = getIntent();
		this.senderVM = i.getParcelableExtra("senderVM");
		this.cloudID = i.getStringExtra("CLOUD_ID");

	}

	private void FillUpTextViews(SenderViewModel senderVM) {
		// set pay to
		this.tvReceiverName.setText(senderVM.ReceiverVM.GetSelectedReceiver()
				.GetReceiverFullName());
		this.tvReceiverBank.setText(senderVM.ReceiverVM.bankVM
				.GetSelectedBank().GetBankName());
		this.tvReceiverAccountNumber.setText(senderVM.ReceiverVM.bankVM
				.GetSelectedBank().GetAccountID());
		// set from
		this.tvSenderName.setText(senderVM.SelectedSender.GetFirstName());
		this.tvSenderMobile.setText(senderVM.SelectedSender.GetMobile());
		// set amount details
		// 1)get Sent amount in Dollars append "AU$"
		double i = senderVM.SelectedAmount.GetAmtSend();
		double fee = 10.00;

		this.tvAmountInDollars.setText("AU$ " + Double.toString(i));
		this.tvOurFee.setText("AU$ 10.00(Fee)");
		this.tvTotal.setText("AU$ " + Double.toString(i + fee));
		// 2)get Amount is Receiver currency
		double amtReceiver = senderVM.SelectedAmount.GetAmtReceived();
		double rate = senderVM.SelectedAmount.GetConvertRate();

		this.tvRate.setText(Double.toString(rate) + " LKR/AUD");
		this.tvAmtLkr.setText("AU$ " + Double.toString(amtReceiver) + " * "
				+ Double.toString(rate));
		this.tvCurrency.setText("LKR " + Double.toString(amtReceiver * rate));

		// // westpak
		// tvWestPacLogo
		// .setText("Account Number: Money Transfer \nBSB No: 123-456\nAccount No: 12345678");
		//
		// // nab
		// tvNabLogo
		// .setText("Account Number: Money Transfer \nBSB No: 456-456\nAccount No: 4568123");

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

	public void InitializeControls() {

		this.tvReceiverName = (TextView) findViewById(R.id.tvReceiverName);
		this.tvReceiverBank = (TextView) findViewById(R.id.tvReceiverBank);
		this.tvReceiverAccountNumber = (TextView) findViewById(R.id.tvReceiverAccountNumber);

		this.tvSenderName = (TextView) findViewById(R.id.tvSenderName);
		this.tvSenderMobile = (TextView) findViewById(R.id.tvSenderMobile);

		this.tvAmountInDollars = (TextView) findViewById(R.id.tvAmountInDollars);
		this.tvOurFee = (TextView) findViewById(R.id.tvOurFee);
		this.tvTotal = (TextView) findViewById(R.id.tvTotal);

		this.tvRate = (TextView) findViewById(R.id.tvRate);

		this.tvAmtLkr = (TextView) findViewById(R.id.tvAmtLkr);

		this.tvCurrency = (TextView) findViewById(R.id.tvCurrency);

		this.btnCancel = (Button) findViewById(R.id.btnCancel);
		this.btnCancel.setOnClickListener(this);
		this.btnConfirm = (Button) findViewById(R.id.btnConfirm);
		this.btnConfirm.setOnClickListener(this);

		globaContext = (GlobalContext) getApplicationContext();
		UserInterfaceHelper.PrepareActionBar(this, actionBar, globaContext.IsRegistred());

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnCancel:

			Intent i = new Intent(AccountNumberScreen.this, MainScreen.class);
			i.putExtra("syncCheck", false);
			startActivity(i);

			break;

		case R.id.btnConfirm:

			Intent a = new Intent(AccountNumberScreen.this, MainScreen.class);
			a.putExtra("senderVM", this.senderVM);
			a.putExtra("syncCheck", false);
			startActivity(a);

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
		navConfig.setMainLayout(R.layout.payment_summary_dialog);

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
			intent = new Intent(AccountNumberScreen.this,
					SelectTransactionScreen.class);
			startActivity(intent);
			break;
		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(AccountNumberScreen.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(AccountNumberScreen.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(AccountNumberScreen.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(AccountNumberScreen.this,
					FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(AccountNumberScreen.this,
					Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipients", Toast.LENGTH_SHORT).show();
			intent = new Intent(AccountNumberScreen.this,
					ReceiverDashboard.class);
			startActivity(intent);
			break;

		}

	}

}
