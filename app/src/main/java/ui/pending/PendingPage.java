package ui.pending;

import java.util.List;

import models.TransactionHistoryModel;
import ui.navbarLinks.AboutPage;
import ui.navbarLinks.CompletedPage;
import ui.navbarLinks.FeedBackActivity;
import ui.navbarLinks.ReceiverDashboard;
import ui.navbarLinks.Registration_One;
import ui.navbarLinks.TutorialPage;
import utilities.AbstractNavDrawerListActivity;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;
import viewmodels.AmountViewModel;
import viewmodels.ReceiverViewModel;
import viewmodels.SenderViewModel;
import viewmodels.TransactionHistoryViewModel;
import viewmodels.TransactionViewModel;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

@SuppressLint("InflateParams")
public class PendingPage extends AbstractNavDrawerListActivity implements
		OnClickListener {

	// ...................................
	private ListView lvPending;
	List<TransactionHistoryModel> transactions;
	private OnItemClickListener listener;
	private PendingTransactionsAdapter adapter;
	TransactionHistoryViewModel transactionHistoryVM;
	// .........................
	// action bar
	ActionBar actionBar;
	FrameLayout actionBarContainer;
	int abContainerViewID;
	// ..

	// vms

	SenderViewModel senderVM;
	private TransactionViewModel transVM;
	ReceiverViewModel recVM;
	AmountViewModel amountVM;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_pending_transactions);

		this.lvPending = (ListView) findViewById(R.id.lvPending);
		this.transactionHistoryVM = new TransactionHistoryViewModel();
		GlobalContext global = (GlobalContext) getApplicationContext();
		transactions = transactionHistoryVM.GetAllTransactionsHistory(global
				.GetDataContext(PendingPage.this));

		if (transactions.size() > 0) {

			this.adapter = new PendingTransactionsAdapter(this,
					R.layout.pending_header, transactions);

			// initializeListener();
			this.lvPending.setAdapter(adapter);
			// this.lvPending.setOnItemClickListener(listener);
		}

		GlobalContext globalContext = (GlobalContext) getApplicationContext();

		this.senderVM = new SenderViewModel(globalContext.GetDataContext(this),
				false);
		this.recVM = new ReceiverViewModel(globalContext.GetDataContext(this));
		this.amountVM = new AmountViewModel(globalContext.GetDataContext(this));

		UserInterfaceHelper.PrepareActionBar(PendingPage.this, actionBar,
				globalContext.IsRegistred());

		getWindow().setBackgroundDrawableResource(R.drawable.background_image);

	}

	// private void initializeListener() {
	//
	// listener = new OnItemClickListener() {
	//
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view,
	// int position, long id) {
	//
	// Intent i = new Intent(PendingPage.this,
	// RecieverNBankDetailsScreen.class);
	//
	// TransactionHistoryModel trans = new TransactionHistoryModel();
	// trans = transactions.get(position - 1);
	//
	// i.putExtra("trans", trans);
	//
	//
	// startActivity(i);
	//
	// }
	//
	// };
	// }

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

	class PendingTransactionsAdapter extends
			ArrayAdapter<TransactionHistoryModel> {

		Context context;
		List<TransactionHistoryModel> objects;

		public PendingTransactionsAdapter(Context context, int resource,
				List<TransactionHistoryModel> objects) {

			super(context, resource);
			this.context = context;
			this.objects = objects;
		}

		@Override
		public int getCount() {
			return objects.size();
		}

		@Override
		public TransactionHistoryModel getItem(int position) {
			return objects.get(position);
		}

		private class ViewHolder {
			TextView tvReceiverNameToFill;
			TextView tvCloudCode;
			TextView tvAmount;
			ImageButton imgBtnDeleteTransaction;

		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View row = convertView;
			ViewHolder holder = new ViewHolder();

			if (row == null) {
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				row = inflater.inflate(R.layout.pending_header, parent, false);

				holder.tvReceiverNameToFill = (TextView) row
						.findViewById(R.id.tvReceiverNameToFill);
				holder.tvCloudCode = (TextView) row
						.findViewById(R.id.tvCloudCode);
				holder.tvAmount = (TextView) row.findViewById(R.id.tvAmount);
				holder.imgBtnDeleteTransaction = (ImageButton) row
						.findViewById(R.id.imgBtnDeleteTransaction);

				row.setTag(holder);

			} else {
				holder = (ViewHolder) row.getTag();
			}

			final TransactionHistoryModel info = objects.get(position);

			Typeface face = Typeface.createFromAsset(getAssets(),
					"fonts/NotoSans-Bold.ttf");

			if (info != null) {

				holder.tvReceiverNameToFill.setText(info.getReceivername());
				holder.tvReceiverNameToFill.setTypeface(face);

				holder.tvCloudCode.setText(info.getCode());
				holder.tvCloudCode.setTypeface(face);

				holder.tvAmount.setText(info.getAmount_dollars().toString());
				holder.tvAmount.setTypeface(face);

				holder.imgBtnDeleteTransaction
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (deleteTransaction(info.getId())) {
									Crouton.makeText(PendingPage.this,
											"Transaction Deleted", Style.INFO)
											.show();
								}
							}
						});

			}
			return row;
		}
	}

	private boolean deleteTransaction(long rowID) {

		this.transactionHistoryVM.DeleteHistoryRecord(rowID);

		Intent i = new Intent(PendingPage.this, PendingPage.class);
		startActivity(i);
		this.finish();

		return false;
	}

	@Override
	protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
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
					NavMenuSection.create(300, "Your Details"),
					NavMenuItem.create(301, "Your Details", "about_man_drawer",
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
		navConfig.setMainLayout(R.layout.activity_pending_transactions);

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
		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(PendingPage.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(PendingPage.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(PendingPage.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(PendingPage.this, FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(PendingPage.this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			intent = new Intent(PendingPage.this, ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

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

		default:
			break;
		}
	}

}
