package ui.sendmoney;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import models.BankModel;
import models.ReceiverModel;
import redesign.screens.RecieverNBankDetailsScreen;
import ui.navbarLinks.AboutPage;
import ui.navbarLinks.CompletedPage;
import ui.navbarLinks.FeedBackActivity;
import ui.navbarLinks.ReceiverDashboard;
import ui.navbarLinks.Registration_One;
import ui.navbarLinks.TutorialPage;
import ui.sendmoney.AlphabetListAdapter.Item;
import ui.sendmoney.AlphabetListAdapter.Row;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

public class ReceiverSelectionScreen extends AbstractNavDrawerListActivity
		implements OnClickListener {

	private ui.sendmoney.AlphabetListAdapter adapter = new ui.sendmoney.AlphabetListAdapter();
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
	private TextView tvReceiverHeading;
	public ReceiverModel selectedReceiver;
	long selectedReceiverID;
	private ActionBar actionBar;
	private GlobalContext globalcontext;
	private DataContext dContext;
	private ListView receiver_jumpListview;
	

	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);


			mGestureDetector = new GestureDetector(this,
					new SideIndexGestureListener());

			this.globalcontext = (GlobalContext) getApplicationContext();
			this.dContext = this.globalcontext.GetDataContext(ReceiverSelectionScreen.this);

			recVM = new ReceiverViewModel(this.dContext);
			bankVM = new BankViewModel(this.dContext);

			Intent i = getIntent();
			this.senderVM = i.getParcelableExtra("senderVM");

			InitializeControls();

		SetUpOrRefreshListView();

	}

	private void SetUpOrRefreshListView() {

		//get
		List<ReceiverModel> receivers = recVM.GetAllReceivers();
		//sort
		Collections.sort(receivers, new Comparator<ReceiverModel>() {

			@Override
			public int compare(ReceiverModel lhs, ReceiverModel rhs) {
				// sort by Receiver name
				return lhs.GetReceiverFullName().compareTo(
						rhs.GetReceiverFullName());
			}
		});

		//Adapter input collection
		List<Row> rows = new ArrayList<Row>();
		int start = 0;
		int end = 0;
		String previousLetter = null;
		Object[] tmpIndexItem = null;
		Pattern numberPattern = Pattern.compile("[0-9]");

		if(receivers.size() > 0){
		
		for (ReceiverModel receiver : receivers) {
			// get first letter of
			String firstLetter = receiver.GetReceiverFullName().substring(0, 1);

			// Group numbers together in the scroller
			if (numberPattern.matcher(firstLetter).matches()) {
				firstLetter = "#";
			}

			// If we've changed to a new letter, add the previous letter to the
			// alphabet scroller
			if (previousLetter != null && !firstLetter.equals(previousLetter)) {
				end = rows.size() - 1;
				tmpIndexItem = new Object[3];
				tmpIndexItem[0] = previousLetter.toUpperCase(Locale.UK);
				tmpIndexItem[1] = start;
				tmpIndexItem[2] = end;
				alphabet.add(tmpIndexItem);

				start = end + 1;
			}

			// Check if we need to add a header row
			if (!firstLetter.equals(previousLetter)) {
				//rows.add(new Section(firstLetter));
				sections.put(firstLetter, start);
			}

			// Add the receiver to the list
			rows.add(new Item(receiver));
			previousLetter = firstLetter;
		}
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
		receiver_jumpListview = (ListView) findViewById(R.id.receiver_jumpListview);
		receiver_jumpListview.setDivider(null);
		receiver_jumpListview.setAdapter(adapter);
		InitialiseTapListener();
		updateList();

		LinearLayout sideIndex = (LinearLayout) findViewById(R.id.sideIndex);
		sideIndex.setVisibility(View.GONE);
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
				 * e1 - The first down motion event that started the scrolling.
				 * e2 - The move motion event that triggered the current onScroll. 
				 * 
				 * distanceX - The distance along the X axis that has been scrolled since the
				 * last call to onScroll. This is NOT the distance between e1 and
				 * e2. 
				 * 
				 * distanceY - The distance along the Y axis that has been
				 * scrolled since the last call to onScroll. This is NOT the
				 * distance between e1 and e2.
				 */

				sideIndexX = sideIndexX - distanceX;
				sideIndexY = sideIndexY - distanceY;

				if (sideIndexX >= 0 && sideIndexY >= 0) {
					displayListItem();
				}

				return super.onScroll(e1, e2, distanceX, distanceY);
			}
		}

	private void InitialiseTapListener() {

		this.receiver_jumpListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {

				Item r = (Item) parent.getItemAtPosition(position);
				selectedReceiverID = r.receiverID;

				StartIntent();
			}

		});
	}

	private void StartIntent() {

		// get receiver from receiver ID and set it to VM flow
		selectedReceiver = this.recVM.GetReceiverByID(this.selectedReceiverID);
		this.senderVM.ReceiverVM.SetSelectedReceiver(selectedReceiver);

		List<BankModel> banks = this.bankVM
				.GetBanksByReceiverID(this.selectedReceiverID);
		this.senderVM.ReceiverVM.bankVM.SetBanks(banks);
		this.senderVM.ReceiverVM.bankVM.SetSelectedBank(banks.get(0));

		Intent i = new Intent(ReceiverSelectionScreen.this, AmountScreen.class);

		i.putExtra("senderVM", this.senderVM);
		startActivity(i);
	}

	private void InitializeControls() {

		addNewReceiver = (ImageButton) findViewById(R.id.addNewReceiver);
		addNewReceiver.setOnClickListener(this);
		
		SetFontStyling();
		
		globalcontext = (GlobalContext) getApplicationContext();

		UserInterfaceHelper
		.PrepareReceiverListPageActionBar(this,actionBar, globalcontext.IsRegistred());
	}
	
	private void SetFontStyling() {
		
		Typeface NotoSans = Typeface.createFromAsset(this
				.getAssets(), "fonts/NotoSans-Regular.ttf");
		
		tvReceiverHeading = (TextView) findViewById(R.id.tvReceiverHeading);
		tvReceiverHeading.setTypeface(NotoSans);
	}

	public void actionBarClick(View v) {

		switch (v.getId()) {
		case R.id.absendMoney:

			onBackPressed();

			break;

		case R.id.abUserIcon:
			Toast.makeText(this, "Settings Tapped", Toast.LENGTH_SHORT).show();
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

		//1)jump list strip is divided into Alphabetical order(26 letters),atm divided by 20
		//2)find out what is the max height per one index/item/(header+entry)
		int indexMaxSize = (int) Math.floor(sideIndex.getHeight() / 20);
		//total number of headers+entries/indexes to display
		int tmpIndexListSize = indexListSize;
		//while we have more space 
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
			intent = new Intent(ReceiverSelectionScreen.this,
					SelectTransactionScreen.class);
			startActivity(intent);
			break;

		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverSelectionScreen.this,
					CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverSelectionScreen.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverSelectionScreen.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverSelectionScreen.this,
					FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverSelectionScreen.this,
					Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipients", Toast.LENGTH_SHORT).show();
			intent = new Intent(ReceiverSelectionScreen.this,
					ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.addNewReceiver:

			Intent i = new Intent(ReceiverSelectionScreen.this,
					RecieverNBankDetailsScreen.class);
			i.putExtra("senderVM", this.senderVM);
			startActivity(i);
			finish();
			break;
			
		case R.id.absendMoney:

			onBackPressed();

			break;

		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		SetUpOrRefreshListView();
	}
}
