package ui.navbarLinks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import models.ReceiverModel;
import redesign.screens.RecieverNBankDetailsScreen;
import ui.sendmoney.AlphabetAdapter_ReceiverDashboard;
import ui.sendmoney.AlphabetAdapter_ReceiverDashboard.Item;
import ui.sendmoney.AlphabetAdapter_ReceiverDashboard.Row;
import utilities.AbstractNavDrawerListActivity;
import utilities.DataContext;
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
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

public class ReceiverDashboard extends AbstractNavDrawerListActivity implements
		OnClickListener {

	private AlphabetAdapter_ReceiverDashboard adapter = new AlphabetAdapter_ReceiverDashboard();
	private GestureDetector mGestureDetector;
	private List<Object[]> alphabet = new ArrayList<Object[]>();
	private HashMap<String, Integer> sections = new HashMap<String, Integer>();
	private int sideIndexHeight;
	private static float sideIndexX;
	private static float sideIndexY;
	private int indexListSize;

	public ReceiverViewModel recVM;
	public SenderViewModel senderVM;
	public BankViewModel bankVM;
	private ImageButton addNewReceiver;
	public ReceiverModel selectedReceiver;
	long selectedReceiverID;
	private ActionBar actionBar;
	private GlobalContext globalcontext;
	private DataContext dContext;
	private ListView receiver_jumpListview;
	private TextView tvReceiverHeading;
	private Boolean refresh = false;
	
	public boolean isDelete = false;
	public boolean isEdit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.globalcontext = (GlobalContext) getApplicationContext();
		this.dContext = this.globalcontext
				.GetDataContext(ReceiverDashboard.this);

		mGestureDetector = new GestureDetector(this,
				new SideIndexGestureListener());

		recVM = new ReceiverViewModel(this.dContext);
		bankVM = new BankViewModel(this.dContext);
		senderVM = new SenderViewModel(this.dContext);

		InitializeControls();
		SetUpOrRefreshListView();

	}

	private void SetUpOrRefreshListView() {
		List<ReceiverModel> receivers = recVM.GetAllReceivers();
		Collections.sort(receivers, new Comparator<ReceiverModel>() {

			@Override
			public int compare(ReceiverModel lhs, ReceiverModel rhs) {
				return lhs.GetReceiverFullName().compareTo(
						rhs.GetReceiverFullName());
			}
		});
		List<Row> rows = new ArrayList<Row>();
		int start = 0;
		int end = 0;
		String previousLetter = null;
		Object[] tmpIndexItem = null;
		Pattern numberPattern = Pattern.compile("[0-9]");

		for (ReceiverModel receiver : receivers) {
			String firstLetter = receiver.GetReceiverFullName().substring(0, 1);
			if (numberPattern.matcher(firstLetter).matches()) {
				firstLetter = "#";
			}
			if (previousLetter != null && !firstLetter.equals(previousLetter)) {
				end = rows.size() - 1;
				tmpIndexItem = new Object[3];
				tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
				tmpIndexItem[1] = start;
				tmpIndexItem[2] = end;
				alphabet.add(tmpIndexItem);
				start = end + 1;
			}
			if (!firstLetter.equals(previousLetter)) {
				sections.put(firstLetter, start);
			}
			rows.add(new Item(receiver));
			previousLetter = firstLetter;
		}

		if (previousLetter != null) {
			// Save the last letter
			tmpIndexItem = new Object[3];
			tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
			tmpIndexItem[1] = start;
			tmpIndexItem[2] = rows.size() - 1;
			alphabet.add(tmpIndexItem);
		}
		

		adapter.setRows(rows);
		this.globalcontext = (GlobalContext) getApplicationContext();
		adapter.setContext(ReceiverDashboard.this);

		receiver_jumpListview = (ListView) findViewById(R.id.receiver_jumpListview);
		receiver_jumpListview.setDivider(null);
		
		receiver_jumpListview.setAdapter(adapter);

		updateList();

		LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		sideIndex.setVisibility(View.GONE);
	}
	

	private void InitializeControls() {

		addNewReceiver = (ImageButton) findViewById(R.id.addNewReceiver);
		addNewReceiver.setOnClickListener(this);

		SetFontStyling();

		globalcontext = (GlobalContext) getApplicationContext();

		UserInterfaceHelper.PrepareReceiverDashboardPageActionBar(this, actionBar,
				globalcontext.IsRegistred());
	}

	private void SetFontStyling() {

		Typeface NotoSans = Typeface.createFromAsset(this.getAssets(),
				"fonts/NotoSans-Regular.ttf");

		tvReceiverHeading = (TextView) findViewById(R.id.tvReceiverHeading);
		tvReceiverHeading.setTypeface(NotoSans);
	}

	public void actionBarClick(View v) {

		switch (v.getId()) {
		case R.id.absendMoney:

			onBackPressed();

			break;

		case R.id.abUserIcon:
			Toast.makeText(this, "Profile Tapped", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mGestureDetector.onTouchEvent(event)) {
			return true;
		} else {
			return false;
		}
	}

	public void updateList() {

		LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		sideIndex.removeAllViews();

		indexListSize = alphabet.size();
		if (indexListSize < 1) {
			return;
		}

		// 1)jump list strip is divided into Alphabetical order(26 letters),atm
		// divided by 20
		// 2)find out what is the max height per one index/item/(header+entry)
		int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
		// total number of headers+entries/indexes to display
		int tmpIndexListSize = indexListSize;
		// while we have more space
		while (tmpIndexListSize > indexMaxSize) {
			tmpIndexListSize = tmpIndexListSize / 2;
		}
		double delta;
		if (tmpIndexListSize > 0) {
			delta = indexListSize / tmpIndexListSize;
		} else {
			delta = 1;
		}

		TextView tmpTV;
		for (double i = 1; i <= indexListSize; i = i + delta) {
			Object[] tmpIndexItem = alphabet.get((int) i - 1);
			String tmpLetter = tmpIndexItem[0].toString();

			tmpTV = new TextView(this);
			tmpTV.setText(tmpLetter);
			tmpTV.setGravity(Gravity.CENTER);
			tmpTV.setTextSize(15);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					ViewGroup.LayoutParams.WRAP_CONTENT,
					ViewGroup.LayoutParams.WRAP_CONTENT, 1);
			tmpTV.setLayoutParams(params);
			sideIndex.addView(tmpTV);
		}

		sideIndexHeight = sideIndex.getHeight();

		sideIndex.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// now you know coordinates of touch
				sideIndexX = event.getX();
				sideIndexY = event.getY();

				// and can display a proper item it country list
				displayListItem();

				return false;
			}
		});
	}

	public void displayListItem() {
		LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		sideIndexHeight = sideIndex.getHeight();
		// compute number of pixels for every side index item
		double pixelPerIndexItem = (double) sideIndexHeight / indexListSize;

		// compute the item index for given event position belongs to
		int itemPosition = (int) (sideIndexY / pixelPerIndexItem);

		// get the item (we can do it since we know item index)
		if (itemPosition < alphabet.size()) {
			Object[] indexItem = alphabet.get(itemPosition);
			int subitemPosition = sections.get(indexItem[0]);

			// ListView listView = (ListView) findViewById(android.R.id.list);
			this.receiver_jumpListview.setSelection(subitemPosition);
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
		navConfig.setMainLayout(R.layout.receiver_dash_windows_style);

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
			Toast.makeText(this, "You are already here", Toast.LENGTH_SHORT)
					.show();
			break;

		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverDashboard.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverDashboard.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverDashboard.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverDashboard.this, FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverDashboard.this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipients", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverDashboard.this, ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.addNewReceiver:

			Intent i = new Intent(ReceiverDashboard.this,
					RecieverNBankDetailsScreen.class);
			i.putExtra("senderVM", this.senderVM);
			i.putExtra("cameFromDashBoard", true);
			startActivity(i);
			break;

		case R.id.absendMoney:

			onBackPressed();
			finish();

			break;

		}
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();

		SetUpOrRefreshListView();
	}

	// Gesture listener class to make up a jump list on right side of screen
	class SideIndexGestureListener extends
			GestureDetector.SimpleOnGestureListener {
		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			// onScroll():Notified when a scroll occurs with the initial on down
			// MotionEvent and the current move MotionEvent.

			/*
			 * e1 - The first down motion event that started the scrolling. e2 -
			 * The move motion event that triggered the current onScroll.
			 * 
			 * distanceX - The distance along the X axis that has been scrolled
			 * since the last call to onScroll. This is NOT the distance between
			 * e1 and e2.
			 * 
			 * distanceY - The distance along the Y axis that has been scrolled
			 * since the last call to onScroll. This is NOT the distance between
			 * e1 and e2.
			 */

			sideIndexX = sideIndexX - distanceX;
			sideIndexY = sideIndexY - distanceY;

			if (sideIndexX >= 0 && sideIndexY >= 0) {
				displayListItem();
			}

			return super.onScroll(e1, e2, distanceX, distanceY);
		}
	}


}
