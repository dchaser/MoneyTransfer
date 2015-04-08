package ui.sendmoney;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import redesign.screens.MobileCodeEntryScreen;
import redesign.screens.MyActivity;

import models.AmountModel;
import models.TransactionHistoryModel;
import models.TransactionModel;
import ui.navbarLinks.AboutPage;
import ui.navbarLinks.CompletedPage;
import ui.navbarLinks.FeedBackActivity;
import ui.navbarLinks.ReceiverDashboard;
import ui.navbarLinks.Registration_One;
import ui.navbarLinks.TutorialPage;
import utilities.AbstractNavDrawerActivity;
import utilities.CustomEditText;
import utilities.DTOMapper;
import utilities.DbHelper;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;
import viewmodels.AmountViewModel;
import viewmodels.SenderViewModel;
import viewmodels.TransactionHistoryViewModel;
import viewmodels.TransactionViewModel;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;
import com.microsoft.windowsazure.mobileservices.TableOperationCallback;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import dtos.Transaction;
import fragments.InstructionsDialog;
import fragments.PaymentSumaryDialog;
import fragments.PaymentSumaryDialog.OnTransactionCancelOrConfirm;

public class AmountScreen extends AbstractNavDrawerActivity implements
		OnClickListener, OnTransactionCancelOrConfirm {

	public Button BtnPay;
	public CustomEditText etAussie;
	public CustomEditText etSriLanka;
	ProgressDialog dial;
	TextView textPersonName;
	TextView textAccountNumber;

	TextView tvPayTo;
	TextView tvDetails;

	public AmountModel Amout;
	public Double sentAmount;

	public TransactionViewModel TransactionVM;
	public TransactionHistoryViewModel trHistoryVM;

	Intent i;

	Double dollars;
	Double rupees;

	Double dTOr_rate = null;
	Double rTOd_rate = null;

	Boolean noError = false;
	Boolean brandnewreceiver = false;

	Typeface NotoSans;

	private ActionBar actionBar;

	private GlobalContext globalcontext;
	private SenderViewModel senderVM;
	
	public long localreceiverID;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable("senderVM", this.senderVM);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			this.globalcontext = (GlobalContext) getApplicationContext();

			// set rate attributes
			dTOr_rate = this.globalcontext.getD_to_R_rate();
			rTOd_rate = this.globalcontext.getR_to_D_rate();

			Intent i = getIntent();
			this.senderVM = i.getParcelableExtra("senderVM");
			this.brandnewreceiver = i
					.getBooleanExtra("brandnewreceiver", false);

			this.senderVM.SetContext(globalcontext
					.GetDataContext(AmountScreen.this));

			InitializeControls();

		} else {

			globalcontext = (GlobalContext) getApplicationContext();
			this.senderVM = savedInstanceState.getParcelable("senderVM");
			this.senderVM.SetContext(globalcontext
					.GetDataContext(AmountScreen.this));

			InitializeControls();
		}

		NotoSans = Typeface.createFromAsset(this.getAssets(),
				"fonts/NotoSans-Regular.ttf");

	}

	public void InitializeControls() {

		this.Amout = new AmountModel();

		UserInterfaceHelper.InitializeButtonWithOnclickListener(this,
				R.id.btnPay, BtnPay, this);

		this.BtnPay = (Button) findViewById(R.id.btnPay);

		globalcontext = (GlobalContext) getApplicationContext();

		if (this.brandnewreceiver == true) {
			// user coming from Add new receiver page
			UserInterfaceHelper
					.PrepareAmountPageActionBar_ComingFromAddNewReceiver(
							AmountScreen.this, actionBar,
							globalcontext.IsRegistred());
		} else {
			// coming from Jump list selection
			UserInterfaceHelper
					.PrepareAmountPageActionBar_ComingFromAddressBook(
							AmountScreen.this, actionBar,
							globalcontext.IsRegistred());
		}

		textPersonName = (TextView) findViewById(R.id.textPersonName);
		textAccountNumber = (TextView) findViewById(R.id.textAccountNumber);

		if (this.senderVM != null) {
			textPersonName.setTypeface(NotoSans);
			textPersonName.setText(this.senderVM.ReceiverVM
					.GetSelectedReceiver().GetReceiverFullName());

			textAccountNumber.setTypeface(NotoSans);
			textAccountNumber.setText(this.senderVM.ReceiverVM.bankVM
					.GetSelectedBank().GetAccountID());
		}

		etAussie = (CustomEditText) UserInterfaceHelper
				.InitializeCustomEditText(this, R.id.etAussie, etAussie);
		etSriLanka = (CustomEditText) UserInterfaceHelper
				.InitializeCustomEditText(this, R.id.etSriLanka, etSriLanka);

		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(etAussie, InputMethodManager.SHOW_IMPLICIT);
		imm.showSoftInput(etSriLanka, InputMethodManager.SHOW_IMPLICIT);

		etAussie.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				try {
					// convert inserted Dollar Amount to Double

					dollars = Double.parseDouble(s.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}
				rupees = dollars * dTOr_rate;
				// sending dollars

				// update the Dollar label

				DecimalFormat format = new DecimalFormat("##.##");
				String x = format.format(rupees);

				if (etAussie.isFocused()) {

					etSriLanka.setTypeface(NotoSans);
					etSriLanka.setText(x);
				}

			}

		});

		etSriLanka.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				try {
					Editable inserted = s;

					rupees = Double.parseDouble(inserted.toString());

				} catch (Exception e) {
					e.printStackTrace();
				}

				dollars = rupees * rTOd_rate;

				// update the Dollar label

				DecimalFormat format = new DecimalFormat("##.##");
				String x = format.format(dollars);

				if (etSriLanka.isFocused()) {
					etAussie.setTypeface(NotoSans);
					etAussie.setText(x);
				}

			}

		});

		SetFontStyling();
	}

	@Override
	public void onClick(View v) {

		if (MakeAmountObjectToPass(etAussie.getText().toString(), etSriLanka
				.getText().toString())) {

			switch (v.getId()) {

			case R.id.btnPay:
				
				if (Amout.GetAmtSend() > 0) {
					this.senderVM.SelectedAmount = this.Amout;

					PaymentSumaryDialog dialog = PaymentSumaryDialog
							.newInstance(this.senderVM);
					dialog.show(AmountScreen.this.getFragmentManager(),
							"summaryFrg");
				} else {

					Toast.makeText(this, "Amount not validated ",
							Toast.LENGTH_SHORT).show();
				}
				break;

			case R.id.tvtodNewReceiver:
				onBackPressed();
				break;

			case R.id.tvfromAddressBook:
				onBackPressed();
				break;

			}
		} else {
			Toast.makeText(this, "Invalid Amount", Toast.LENGTH_SHORT).show();
		}
	}

	private boolean MakeAmountObjectToPass(String Dollars, String Rupees) {

		Boolean isAmountAllGood = false;

		this.Amout = new AmountModel();
		// sending dollars, to sri lanka

		this.Amout.SetSrcCode("AUD");
		this.Amout.SetDestCode("LKR");
		this.Amout.SetConvertRate(dTOr_rate);

		if (Dollars != null) {

			// if text fields are not empty
			if (Dollars != "") {

				try {

					this.Amout.SetAmtSend(Double.parseDouble(Dollars));
					isAmountAllGood = true;
				} catch (Exception e) {
					Toast.makeText(this, "please check the amount",
							Toast.LENGTH_SHORT).show();
				}

			} else {

				Toast.makeText(this, "invalid amount", Toast.LENGTH_SHORT)
						.show();
				isAmountAllGood = false;
			}
		}

		if (Rupees != null) {

			if (Rupees != "") {
				// if etSriLanka edit text isn't empty
				try {
					this.Amout.SetAmtReceived(Double.parseDouble(Rupees));
					isAmountAllGood = true;
				} catch (Exception e) {
					Toast.makeText(this, "please check the amount",
							Toast.LENGTH_SHORT).show();
					isAmountAllGood = false;
				}

			}
		} else {

			Toast.makeText(this, "invalid amount", Toast.LENGTH_SHORT).show();
			isAmountAllGood = false;

		}

		Log.d("Amount", this.Amout.toString());
		return isAmountAllGood;
	}

	private void SetFontStyling() {

		TextView tvPayTo = (TextView) findViewById(R.id.tvPayTo);
		TextView tvDetails = (TextView) findViewById(R.id.tvDetails);

		tvPayTo.setTypeface(NotoSans);
		tvPayTo.setText("Pay to");

		tvDetails.setTypeface(NotoSans);
		tvDetails.setText("Details");

	}

	private void ProcessTransaction() {

		AmountModel amt = AddNewAmount();

		AddNewTransaction(this.senderVM.SelectedSender.GetId(),
				this.senderVM.ReceiverVM.GetSelectedReceiver().GetId(),
				amt.GetId(), this.senderVM.ReceiverVM.bankVM.GetSelectedBank()
						.GetId());
	}

	private AmountModel AddNewAmount() {

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		AmountViewModel amtVM = new AmountViewModel(
				globalContext.GetDataContext(this));
		AmountModel addedAmount = amtVM
				.InsertAmount(this.senderVM.SelectedAmount);
		// amtVM.Dispose();
		return addedAmount;
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
				.GetDataContext(AmountScreen.this));

		long transID = TransactionVM.InsertTransaction(currentTransaction);
		currentTransaction.SetId(transID);

		this.TransactionVM.currentTransaction = currentTransaction;
	}

	private void AddOrUpdateServiceDataBase() {

		final GlobalContext globalContext = (GlobalContext) getApplicationContext();

		// prepare intent for next screen
		DTOMapper dtoMapper = new DTOMapper(this.senderVM);

		Transaction transactionDTO = dtoMapper.GetTransactionDTO();
		// transactionDTO.Id = senderVM.SelectedSender.GetCloudRefCode();

		// make sender identity null as we don't need it anymore in app,
		this.senderVM.SenderIdentity = null;
		
		 localreceiverID = this.senderVM.ReceiverVM.GetSelectedReceiver().GetId();

		if (isOnline()) {

			MobileServiceClient mobileClient = globalContext.GetMobileClient();

			mobileClient.getTable(Transaction.class).insert(transactionDTO,
					new TableOperationCallback<Transaction>() {

						public void onCompleted(Transaction entity,
								Exception exception,
								ServiceFilterResponse response) {
							if (exception == null) {

								dial.dismiss();
								InstructionsDialog dialog = InstructionsDialog.newInstance(
										entity.Id,
										Double.toString(entity.Amount.SentAmount));
								dialog.show(
										AmountScreen.this.getFragmentManager(),
										"");
								// Add transaction to local DB
								ProcessTransaction();
								// Update cloud ids
								UpdateCloudIds(entity);

								try {
									DbHelper.writeToSD(AmountScreen.this);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							} else {
								// Insert failed
								dial.dismiss();
								Crouton.makeText(AmountScreen.this,
										"***" + exception.toString() + "***",
										Style.ALERT).show();
							}
						}
					});

		} else {

			dial.dismiss();
			Crouton.makeText(AmountScreen.this,
					"Please check your internet connection and try again.",
					Style.INFO).show();
		}
	}

	private void UpdateCloudIds(Transaction transaction) {

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		this.senderVM.SetContext(globalContext.GetDataContext(this));

		// this.TransactionVM.UpdateCloudId(transaction.Id);
		this.senderVM.UpdateSenderCloudId(transaction.Sender.Id);
		this.senderVM.ReceiverVM.UpdateReceiverCloudID(transaction.Receiver.Id);

		// this.senderVM.AmountVM.UpdateAmountCloudID(transaction.Amount.Id,
		// this.senderVM.SelectedAmount.GetId());
		this.senderVM.ReceiverVM.bankVM.UpdateBankCloudID(transaction.Bank.Id);
		this.senderVM.AddressVM.UpdateAddressCloudID(
				transaction.Sender.Address.Id,
				this.senderVM.SelectedSender.GetAddress());
		
		//enter details to Transaction History Table
		this.trHistoryVM = new TransactionHistoryViewModel();
		globalContext = (GlobalContext) getApplicationContext();
		
		transaction.Receiver.localReceiverID = this.localreceiverID;
		
		this.trHistoryVM.InsertTransactionHistoryRecord(globalContext.GetDataContext(AmountScreen.this), transaction);
		
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
		navConfig.setMainLayout(R.layout.activity_send_money);

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
			intent = new Intent(AmountScreen.this,
					SelectTransactionScreen.class);
			startActivity(intent);
			break;

		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(AmountScreen.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(AmountScreen.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(AmountScreen.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(AmountScreen.this, FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(AmountScreen.this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipients", Toast.LENGTH_SHORT).show();
			intent = new Intent(AmountScreen.this, ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}
	
	public  boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	@Override
	public void onConfirmClick(SenderViewModel senderVM) {

		this.senderVM = senderVM;

		dial = new ProgressDialog(AmountScreen.this);
		dial.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dial.setCanceledOnTouchOutside(false);
		dial.show();
		dial.setContentView(R.layout.progressdialog_view);


		if (isOnline()) {
			AddOrUpdateServiceDataBase();
		}
	}

	

	@Override
	public void onCancelClick() {

	}

}
