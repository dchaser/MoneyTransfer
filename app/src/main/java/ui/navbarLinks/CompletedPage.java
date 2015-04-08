package ui.navbarLinks;

import java.util.List;

import ui.pending.SingleTransactionPage;
import utilities.AbstractNavDrawerListActivity;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;
import viewmodels.AmountViewModel;
import viewmodels.SenderViewModel;
import viewmodels.TransactionViewModel;
import models.TransactionModel;

import com.example.moneytransfer.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("InflateParams")
public class CompletedPage extends AbstractNavDrawerListActivity {

	private ListView lvCompleted;
	private TransactionViewModel transVM;
	private OnItemClickListener listener;
	private List<TransactionModel> completedTransactionList;
	private CompletedTransactionsAdapter adapter;

	// action bar
	ActionBar actionBar;
	FrameLayout actionBarContainer;
	int abContainerViewID;
	// ..

	// vms
	SenderViewModel senderVM;
	// ReceiverViewModel recVM;
	AmountViewModel amountVM;
	
	GlobalContext globalContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_completed_page);
		
		globalContext = (GlobalContext) getApplicationContext();

		UserInterfaceHelper
		.PrepareActionBar(this,actionBar, globalContext.IsRegistred());
		
		this.lvCompleted = (ListView) findViewById(R.id.completed_page_list);
		this.completedTransactionList = GetCompletedTransactions();

		this.adapter = new CompletedTransactionsAdapter(this,
				R.layout.activity_completed_sub_layout,
				completedTransactionList);

		initializeListener();
		this.lvCompleted.setAdapter(adapter);
		this.lvCompleted.setOnItemClickListener(listener);

		GlobalContext globalContext = (GlobalContext) getApplicationContext();

		this.senderVM = new SenderViewModel(
				globalContext.GetDataContext(CompletedPage.this));
		this.amountVM = new AmountViewModel(globalContext.GetDataContext(this));
	}

	private List<TransactionModel> GetCompletedTransactions() {

		GlobalContext globalContext = (GlobalContext) getApplicationContext();

		this.transVM = new TransactionViewModel(
				globalContext.GetDataContext(this));

		List<TransactionModel> allCompletedTransacitions = transVM
				.getPendingOrCompletedTransactions("'COMPLETED'", null);

		return allCompletedTransacitions;
	}

	private void initializeListener() {
		listener = new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent i = new Intent(CompletedPage.this,
						SingleTransactionPage.class);

				TransactionModel trans = new TransactionModel();
				trans = completedTransactionList.get(position);

				i.putExtra("trans", trans);

				startActivity(i);
			}

		};
	}

	private void refreshListView() {
		this.adapter.notifyDataSetChanged();
	}

	class CompletedTransactionsAdapter extends ArrayAdapter<TransactionModel> {

		Context context;
		List<TransactionModel> objects;

		public CompletedTransactionsAdapter(Context context, int resource,
				List<TransactionModel> objects) {

			super(context, resource);
			this.context = context;
			this.objects = objects;
		}

		@Override
		public int getCount() {
			return objects.size();
		}

		@Override
		public TransactionModel getItem(int position) {
			return objects.get(position);
		}

		private class ViewHolder {
			TextView textViewReceiverName;
			TextView textViewTransactionDate;
			TextView textViewAmount;
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

				holder.textViewReceiverName = (TextView) row
						.findViewById(R.id.tvReceiverNameToFill);
				holder.textViewTransactionDate = (TextView) row
						.findViewById(R.id.tvDateToFill);
				holder.textViewAmount = (TextView) row
						.findViewById(R.id.tvAmountToFill);
				holder.imgBtnDeleteTransaction = (ImageButton) row
						.findViewById(R.id.imgBtnDeleteTransaction);

				row.setTag(holder);

			} else {
				holder = (ViewHolder) row.getTag();
			}

			final TransactionModel info = objects.get(position);

			if (info != null) {

				holder.textViewReceiverName.setText(senderVM.ReceiverVM
						.GetReceiverName(info.GetReceiverID()));
				holder.textViewTransactionDate.setText(info.GetDate());
				holder.textViewAmount.setText(amountVM.GetAmountByID(
						info.GetAmoutID()).GetSrcCode());
				holder.imgBtnDeleteTransaction
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								if (deleteTransaction(info.GetId())) {
									Toast.makeText(CompletedPage.this,
											"Transaction Deleted",
											Toast.LENGTH_SHORT).show();
									refreshListView();
								}
							}
						});
			}
			return row;
		}
	}

	private boolean deleteTransaction(long rowID) {

		this.transVM.deleteTransaction(rowID);

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
		navConfig.setMainLayout(R.layout.activity_completed_page);

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
			intent = new Intent(CompletedPage.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(CompletedPage.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(CompletedPage.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(CompletedPage.this, FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(CompletedPage.this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipient Manager", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(CompletedPage.this, ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

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

}
