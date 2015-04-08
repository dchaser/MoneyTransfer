package ui.sendmoney;

import java.text.SimpleDateFormat;
import java.util.Date;

import ui.navbarLinks.AboutPage;
import ui.navbarLinks.CompletedPage;
import ui.navbarLinks.ReceiverDashboard;
import ui.navbarLinks.Registration_One;
import ui.navbarLinks.TutorialPage;
import utilities.AbstractNavDrawerFragmentActivity;
import utilities.Communicator;
import utilities.DTOMapper;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;
import viewmodels.AmountViewModel;
import viewmodels.BankViewModel;
import viewmodels.SenderViewModel;
import viewmodels.TransactionViewModel;
import models.AmountModel;
import models.TransactionModel;

import com.example.moneytransfer.R;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import dtos.Transaction;
import fragments.BankFragment;
import fragments.ConfirmationFragment;
import fragments.ReceiverFragment;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("SimpleDateFormat")
public class SenderReceiverConfirmationTabPage extends
		AbstractNavDrawerFragmentActivity implements Communicator,
		OnClickListener {

	private Intent i;
	private TextView tvAmtAud;

	private ActionBar actionBar;
	private FrameLayout actionBarContainer;
	int abContainerViewID;

	private FragmentTabHost mTabHost;
	public Button BtnNext;
	private ProgressDialog dialog;

	private boolean create = false;
	private GlobalContext globalContext;

	public SenderViewModel SenderVM;
	public TransactionViewModel TransactionVM;
	public AmountModel ProcessedAmount;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable("senderVM", this.SenderVM);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {

			InitializeControls();
			ReceiveSenderVM();
			prepareTabHost();

		} else {

			InitializeControls();
			this.SenderVM = savedInstanceState.getParcelable("senderVM");
			prepareTabHost();

		}

	}

	private void ReceiveSenderVM() {

		globalContext = (GlobalContext) getApplicationContext();
		Intent i = getIntent();
		this.SenderVM = i.getParcelableExtra("senderVM");
		this.SenderVM.SetContext(globalContext
				.GetDataContext(SenderReceiverConfirmationTabPage.this));

		SetAmountInTopTextViewOfPage();
	}

	private void prepareTabHost() {

		mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.tabFrameLayout);

		// ReceiverFragment Bundle Preparation
		Bundle fullBucketToReceiverFragment = new Bundle();
		fullBucketToReceiverFragment.putParcelable("senderVM", this.SenderVM);

		// Bank Fragment Bundle Preparation
		Bundle fullBucketToBankFragment = new Bundle();
		fullBucketToBankFragment.putParcelable("senderVM", this.SenderVM);

		// Confirmation Tab Bundle Preparation
		mTabHost.addTab(
				mTabHost.newTabSpec("tab1").setIndicator(
						"1",
						getResources()
								.getDrawable(R.drawable.tab_indicator_gen)),
				ReceiverFragment.class, fullBucketToReceiverFragment);

		mTabHost.addTab(
				mTabHost.newTabSpec("tab2").setIndicator(
						"2",
						getResources()
								.getDrawable(R.drawable.tab_indicator_gen)),
				BankFragment.class, fullBucketToBankFragment);

		mTabHost.addTab(
				mTabHost.newTabSpec("tab3").setIndicator(
						"3",
						getResources()
								.getDrawable(R.drawable.tab_indicator_gen)),
				ConfirmationFragment.class, null);
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

	private void SetAmountInTopTextViewOfPage() {

		String send_money = "<font color='#000000'>SEND MONEY - </font>";
		String amount = "<font color='#ffa500'>"
				+ this.SenderVM.SelectedAmount.GetAmtSend().toString() + " AUD"
				+ "</font>";

		this.tvAmtAud.setText(Html.fromHtml(send_money + amount));
	}

	public void InitializeControls() {

		tvAmtAud = (TextView) findViewById(R.id.tvAmtAud);

		UserInterfaceHelper.InitializeButtonWithOnclickListener(this,
				R.id.btnNextTabReceiver, BtnNext, this);

		globalContext = (GlobalContext) getApplicationContext();
		
		UserInterfaceHelper
		.PrepareActionBar(this,actionBar, globalContext.IsRegistred());
	}

	public boolean IsCreateTrue() {
		return this.create;
	}

	public void SetCreate(boolean create) {
		this.create = create;
	}

	@Override
	public void onClick(View arg0) {
		// first find out from which Tab this was called on

		switch (mTabHost.getCurrentTab()) {

		case 0:
			// receiver tab

			ReceiverFragment fragReceiver = (ReceiverFragment) getSupportFragmentManager()
					.findFragmentByTag("tab1");
			fragReceiver.onClick(arg0);// getSenderVMFromReceiverFragment
			BtnNext = (Button) findViewById(R.id.btnNextTabReceiver);
			BtnNext.setText("Confirm");
			mTabHost.setCurrentTab(1);
			HideVisibilityOfVirtualKeyPad();

			break;

		case 1:
			// bank tab

			BankFragment fragBank = (BankFragment) getSupportFragmentManager()
					.findFragmentByTag("tab2");
			fragBank.onClick(arg0);// getSenderVMFromBankFragment
			BtnNext = (Button) findViewById(R.id.btnNextTabReceiver);
			BtnNext.setText("Next");
			mTabHost.setCurrentTab(2);
			HideVisibilityOfVirtualKeyPad();

			break;

		case 2:
			// confirmation tab

			GlobalContext globalCOntext = (GlobalContext) getApplicationContext();

			this.SenderVM.AddressVM.SetContext(globalCOntext
					.GetDataContext(SenderReceiverConfirmationTabPage.this));

			this.SenderVM.AmountVM.SetContext(globalCOntext
					.GetDataContext(SenderReceiverConfirmationTabPage.this));

			this.SenderVM.ReceiverVM.bankVM.SetContext(globalCOntext
					.GetDataContext(SenderReceiverConfirmationTabPage.this));

			this.SenderVM.UpdateSender();

			ProcessTransaction();

			dialog = new ProgressDialog(this);
			dialog = ProgressDialog.show(
					SenderReceiverConfirmationTabPage.this, "Sending Money..",
					"Please note you Reference Code in a Safe Place");

			AddOrUpdateServiceDataBase();

			// make sender identity bitmap null
			this.SenderVM.SenderIdentity = null;

			break;
		}
	}

	private void HideVisibilityOfVirtualKeyPad() {

		final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mTabHost.getWindowToken(), 0);
	}

	private void AddOrUpdateServiceDataBase() {

		final GlobalContext globalContext = (GlobalContext) getApplicationContext();

		// prepare intent for next screen
		i = new Intent(this, AccountNumberScreen.class);
		i.putExtra("senderVM", this.SenderVM);


		DTOMapper dtoMapper = new DTOMapper(this.SenderVM);

		Transaction transactionDTO = dtoMapper.GetTransactionDTO();
		transactionDTO.Id = SenderVM.SelectedSender.GetCloudRefCode();

		// make senderidentity null as we dont need it anymore in app,
		this.SenderVM.SenderIdentity = null;

		MobileServiceClient mobileClient = globalContext.GetMobileClient();

		mobileClient.getTable(Transaction.class).insert(transactionDTO,
				new TableOperationCallback<Transaction>() {

					public void onCompleted(Transaction entity,
							Exception exception, ServiceFilterResponse response) {
						if (exception == null) {
							// Insert succeeded

							UpdateCloudIds(entity);
							dialog.dismiss();
							i.putExtra("CLOUD_ID", entity.Id);
							startActivity(i);

						} else {
							// Insert failed
							Log.w("Money Transfer",
									"Failed " + exception.getMessage());
							dialog.dismiss();
							exception.printStackTrace();
						}
					}
				});
	}

	private void ProcessTransaction() {

		AmountModel amt = AddNewAmount();

		AddNewTransaction(this.SenderVM.SelectedSender.GetId(),
				this.SenderVM.ReceiverVM.GetSelectedReceiver().GetId(),
				amt.GetId(), this.SenderVM.ReceiverVM.bankVM.GetSelectedBank()
						.GetId());
	}

	private void AddNewTransaction(long senderID, long receiverID,
			long amountID, long receiverBankID) {

		TransactionModel currentTransaction = new TransactionModel();

		currentTransaction.SetSenderID(senderID);
		currentTransaction.SetReceiverID(receiverID);
		currentTransaction.SetAmoutID(amountID);

		// get system data
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				.format(new Date());
		currentTransaction.SetDate(" " + date + " ");

		// status pending
		currentTransaction.SetStatus("PENDING");
		currentTransaction.SetReceiverBankID(receiverBankID);

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		// globalContext.RefreshDataContext();
		this.TransactionVM = new TransactionViewModel(
				globalContext.GetDataContext(this));
		this.TransactionVM.SetContext(globalContext
				.GetDataContext(SenderReceiverConfirmationTabPage.this));

		long transID = TransactionVM.InsertTransaction(currentTransaction);
		currentTransaction.SetId(transID);

		this.TransactionVM.currentTransaction = currentTransaction;
	}

	private void UpdateCloudIds(Transaction transaction) {

		GlobalContext globalContext = (GlobalContext) getApplicationContext();

		this.TransactionVM.UpdateCloudId(transaction.Id);
		this.SenderVM.UpdateSenderCloudId(transaction.Sender.Id);
		this.SenderVM.ReceiverVM.UpdateReceiverCloudID(transaction.Receiver.Id);

		this.SenderVM.SetContext(globalContext.GetDataContext(this));

		this.SenderVM.AmountVM.UpdateAmountCloudID(transaction.Amount.Id,
				this.SenderVM.SelectedAmount.GetId());
		this.SenderVM.ReceiverVM.bankVM.UpdateBankCloudID(transaction.Bank.Id);
	}

	@Override
	public void onAttachFragment(Fragment fragment) {
		super.onAttachFragment(fragment);
		// passing senderVM to Confirmation Tab when attaching the Fragment
		if (fragment.getClass().equals(ConfirmationFragment.class)) {
			ConfirmationFragment fragConfirm = (ConfirmationFragment) fragment;

			Bundle newB = new Bundle();
			newB.putParcelable("senderVM", this.SenderVM);
			fragConfirm.getDetails(newB);
		}
	}

	@Override
	public boolean ProceccAmount(AmountModel amount) {
		// not used
		return false;
	}

	@Override
	public void triggerSenderUpdate(SenderViewModel senderVM, int tab) {

		this.SenderVM = senderVM;
	}

	@Override
	public void getSenderVMFromReceiverFragment(SenderViewModel senderVM) {
		this.SenderVM.ReceiverVM = senderVM.ReceiverVM;

	}

	@Override
	public void getSenderVMFromBankFragment(BankViewModel bankVM) {
		this.SenderVM.ReceiverVM.bankVM = bankVM;

	}

	@Override
	public void getDataToConfirmationFragment(String str) {
		// not used
	}

	// ---------------------------------------

	private AmountModel AddNewAmount() {

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		AmountViewModel amtVM = new AmountViewModel(
				globalContext.GetDataContext(this));
		AmountModel addedAmount = amtVM
				.InsertAmount(this.SenderVM.SelectedAmount);
		// amtVM.Dispose();
		return addedAmount;
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
		navConfig.setMainLayout(R.layout.sender_receiver_confirmation);

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
			intent = new Intent(SenderReceiverConfirmationTabPage.this,
					SelectTransactionScreen.class);
			startActivity(intent);
			break;

		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(SenderReceiverConfirmationTabPage.this,
					CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(SenderReceiverConfirmationTabPage.this,
					TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(SenderReceiverConfirmationTabPage.this,
					AboutPage.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// Receipients
			Toast.makeText(this, "Receipients", Toast.LENGTH_SHORT).show();
			intent = new Intent(this, ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

}
