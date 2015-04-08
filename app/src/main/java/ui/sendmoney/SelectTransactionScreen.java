package ui.sendmoney;

import models.SenderModel;
import redesign.screens.MainScreen;
import ui.navbarLinks.AboutPage;
import ui.navbarLinks.CompletedPage;
import ui.navbarLinks.FeedBackActivity;
import ui.navbarLinks.ReceiverDashboard;
import ui.navbarLinks.Registration_One;
import ui.navbarLinks.TutorialPage;
import ui.pending.PendingPage;
import utilities.AbstractNavDrawerActivity;
import utilities.DataContext;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;
import viewmodels.ResetViewModel;
import viewmodels.SenderViewModel;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressLint("InflateParams")
public class SelectTransactionScreen extends AbstractNavDrawerActivity
		implements OnClickListener {

	private TextView tvSenderName;
	@SuppressWarnings("unused")
	private TextView tvRateNumberBigLetters;
	@SuppressWarnings("unused")
	private TextView tvRateCurrencySmallLetters;
	private LinearLayout llSendMoney;
	private LinearLayout llHistory;
	private Button btnSendMoney;
	private Button btnHistory;
	private Button btnReset;
	private ActionBar actionBar;
	
	int counter = 0;
	private SenderViewModel senderVM;	
	private GlobalContext globalContext;
	private DataContext dContext;
	
	private SenderModel mainSender;

	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		this.globalContext = (GlobalContext) getApplicationContext();
		this.dContext = this.globalContext.GetDataContext(SelectTransactionScreen.this);
		InitializeComponents();
		
		Intent i = getIntent();
		
		this.senderVM = i.getParcelableExtra("senderVM");
		this.mainSender = this.senderVM.SelectedSender;

		if (globalContext.IsRegistred()) 
		{
			
			String firstName = this.mainSender.GetFirstName();
			this.tvSenderName.setText(firstName);
		}
	}

	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
		{
		// SendMoney outer space of card
		case R.id.llSendMoney:

			if (this.senderVM == null) 
			{
				Crouton.makeText(SelectTransactionScreen.this,
						"Please Complete Registration Process",
						Style.ALERT).show();
				return;
			}
			//else
			Intent b = new Intent(SelectTransactionScreen.this,
					ReceiverSelectionScreen.class);

			//Sender VM should be prepared at this point
			b.putExtra("senderVM", this.senderVM);
			startActivity(b);
			break;

		case R.id.btnSendMoney:
			// Send Money Button Click

			if (this.senderVM == null) 
			{
				Crouton.makeText(SelectTransactionScreen.this,
						"Please Complete Registration Process",
						Style.ALERT).show();
				return;
			}
			//else
			Intent c = new Intent(SelectTransactionScreen.this,
					ReceiverSelectionScreen.class);

			//Sender VM should be prepared at this point
			c.putExtra("senderVM", this.senderVM);
			startActivity(c);
			break;

		case R.id.btnHistory:
			// history button tap

			Intent d = new Intent(SelectTransactionScreen.this,
					PendingPage.class);

			startActivity(d);
			break;

		// LinearLayout History card outer space
		case R.id.llHistory:
			// history button tap

			Intent da = new Intent(SelectTransactionScreen.this,
					PendingPage.class);

			startActivity(da);

			break;

		case R.id.btnReset:
			// reset button
			globalContext = (GlobalContext) getApplicationContext();
			ResetViewModel resetVM = new ResetViewModel(
					globalContext.GetDataContext(SelectTransactionScreen.this));

			resetVM.DeleteAllRecords();

			Toast.makeText(this, "everything Deleted", Toast.LENGTH_SHORT)
					.show();

			Intent a = new Intent(this, MainScreen.class);
			startActivity(a);

			globalContext.SetRegistered(false);
			finish();

			break;
			
		case R.id.abThreeBars:
			
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
			} else {
				mDrawerLayout.openDrawer(mDrawerList);
			}
			
			break;
			
		case R.id.abUserIcon:
			Crouton.makeText(this, "Settings Tapped", Style.INFO).show();
			break;

		}

	}

	@Override
	protected NavDrawerActivityConfiguration getNavDrawerConfiguration() 
	{
		globalContext = (GlobalContext) getApplicationContext();
		NavDrawerItem[] menu;
		if (globalContext.IsRegistred()) 
		{
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
					NavMenuSection.create(300, "Your Details"),
					NavMenuItem.create(301, "Your Details", "about_man_drawer",
							false, this),
					NavMenuItem.create(302, "Receivers", "about_man_drawer",
							false, this) };
		} 
		else 
		{
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
		navConfig.setMainLayout(R.layout.activity_sendmoney_or_viewhistory);
		navConfig.setDrawerLayoutId(R.id.drawer_layout);
		navConfig.setLeftDrawerId(R.id.left_drawer);
		navConfig.setNavItems(menu);
		navConfig.setDrawerShadow(R.drawable.drawer_shadow);
		navConfig.setDrawerOpenDesc(R.string.drawer_open);
		navConfig.setDrawerCloseDesc(R.string.drawer_close);
		navConfig.setBaseAdapter(new NavigationDrawerAdapter(this,
				R.layout.navdrawer_item, menu));

		return navConfig;
	}

	@Override
	protected void onNavItemSelected(int id) {
		Intent intent;
		switch ((int) id) 
		{
		case 99:
			// Home
			break;

		case 101:
			intent = new Intent(SelectTransactionScreen.this,
					CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			intent = new Intent(SelectTransactionScreen.this,
					TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About us
			intent = new Intent(SelectTransactionScreen.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			intent = new Intent(SelectTransactionScreen.this,
					FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			intent = new Intent(SelectTransactionScreen.this,
					Registration_One.class);
			
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			intent = new Intent(SelectTransactionScreen.this,
					ReceiverDashboard.class);
			startActivity(intent);
			break;
		}
	}

	public void InitializeComponents() {

		this.tvSenderName = (TextView) findViewById(R.id.tvSenderName);
		this.tvRateNumberBigLetters = (TextView) findViewById(R.id.tvRateNumberBigLetters);
		this.tvRateCurrencySmallLetters = (TextView) findViewById(R.id.tvRateCurrencySmallLetters);

		this.btnSendMoney = (Button) findViewById(R.id.btnSendMoney);
		this.btnHistory = (Button) findViewById(R.id.btnHistory);

		llSendMoney = (LinearLayout) findViewById(R.id.llSendMoney);
		llHistory = (LinearLayout) findViewById(R.id.llHistory);

		this.llSendMoney.setOnClickListener(this);
		this.llHistory.setOnClickListener(this);
		this.btnSendMoney.setOnClickListener(this);
		this.btnHistory.setOnClickListener(this);

		btnReset = (Button) findViewById(R.id.btnReset);
		btnReset.setOnClickListener(this);
		
		UserInterfaceHelper.PrepareActionBar(this, actionBar,
				this.globalContext.IsRegistred());

		SetFontStyling();

	}

	private void SetFontStyling() {

		Typeface NotoSans = Typeface.createFromAsset(this.getAssets(),
				"fonts/NotoSans-Bold.ttf");

		btnSendMoney.setTypeface(NotoSans);
		btnSendMoney.setTextColor(Color.parseColor("#FFC107"));
		btnSendMoney.setTextSize((float) 18);
		btnSendMoney.setText("SEND MONEY");

		btnHistory.setTypeface(NotoSans);
		btnHistory.setTextColor(Color.parseColor("#FFC107"));
		btnHistory.setTextSize((float) 18);
		btnHistory.setText("HISTORY");

	}

}
