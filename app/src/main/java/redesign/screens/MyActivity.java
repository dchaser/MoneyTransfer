package redesign.screens;

import ui.navbarLinks.AboutPage;
import ui.navbarLinks.CompletedPage;
import ui.navbarLinks.FeedBackActivity;
import ui.navbarLinks.ReceiverDashboard;
import ui.navbarLinks.Registration_One;
import ui.navbarLinks.TutorialPage;
import ui.sendmoney.SelectTransactionScreen;
import utilities.AbstractNavDrawerActivity;
import utilities.DataContext;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import viewmodels.SenderViewModel;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.moneytransfer.R;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import dtos.SenderCheckDTO;
import fragments.FirstSlide;
import fragments.LinkAccountDialog;
import fragments.SecondSlide;
import fragments.ThirdSlide;

public class MyActivity extends AbstractNavDrawerActivity implements
		OnClickListener, OnPageChangeListener {

	private EditText etMobOrEmail;
	private Button btnEnterMobileOrEmail;
	private Button btnMobEmailToggle;
	private ImageView imgViewThreeDots;
	private InputMethodManager inputMethodManager;
	private MobileServiceClient mobileClient;
	private ActionBar actionBar;
	private ViewPager viewPager;

	private GlobalContext globalContext;
	@SuppressWarnings("unused")
	private DataContext dContext;
	public SenderViewModel senderVM;
	private String TAG = "tag";
	private ProgressDialog dial;
	private SenderCheckDTO senderToCheck;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.globalContext = (GlobalContext) getApplicationContext();
		this.dContext = this.globalContext.GetDataContext(MyActivity.this);

		InitializeControls();
		// hide virtual keyboard
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btnMobEmailToggle:
			// enter email text to toggle keyboard

			getCurrentFocus().clearFocus();
			etMobOrEmail
					.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
			showSoftKeyboard(etMobOrEmail);
			break;

		case R.id.etMobOrEmail:
			// edit text

			getCurrentFocus().clearFocus();
			etMobOrEmail.setInputType(InputType.TYPE_CLASS_PHONE);
			ChangeSoftInputType(viewPager.getCurrentItem());
			break;

		case R.id.btnEnterMobileOrEmail:

			// ok button to check accouht via API
			if (this.globalContext == null) {
				this.globalContext = (GlobalContext) getApplicationContext();
			}

			if (this.dContext == null) {
				this.dContext = this.globalContext
						.GetDataContext(MyActivity.this);
			}
			senderVM = new SenderViewModel();
			// check if the Sender is registered locally
			if (senderVM.CheckIfUserHasAccount(this.dContext)) {
				Intent i = new Intent(MyActivity.this,
						MobileCodeEntryScreen.class);
				startActivity(i);
			} else {
				MakeSenderCheckDTO();
				this.mobileClient = globalContext.GetMobileClient();

				if (isOnline()) {
					dial = new ProgressDialog(MyActivity.this);
					dial.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dial.setCanceledOnTouchOutside(false);
					dial.show();
					dial.setContentView(R.layout.progressdialog_view);

					try {
						// call API with senderCheck object to check weather a
						// sender is in the service DB
						mobileClient.invokeApi("CustomTransaction",
								senderToCheck, Boolean.class,
								new ApiOperationCallback<Boolean>() {

									@Override
									public void onCompleted(Boolean result,
											Exception error,
											ServiceFilterResponse response) {
										if (error == null) {
											CheckSender(result);
										} else {
											dial.dismiss();
											Crouton.makeText(
													MyActivity.this,
													"Eror Occured with service",
													Style.ALERT).show();
										}
									}

								});
					} catch (SecurityException e) {
						Log.d(TAG, "CouldNotConnectToSocket", e);
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						Log.d(TAG, "CouldNotConnectToSocket", e);
						e.printStackTrace();
					}

				} else {
					Crouton.makeText(
							MyActivity.this,
							"Check your internet connection and try again please.",
							Style.INFO).show();
				}
			}
			break;
		}
	}

	private void CheckSender(Boolean result) {

		dial.dismiss();
		if (result) {
			
			LinkAccountDialog dialog = LinkAccountDialog
					.newInstance(this.senderToCheck);
			dialog.show(MyActivity.this.getFragmentManager(),
					"linkAccountFragment");
		} else {

			Crouton.makeText(MyActivity.this,
					"No Account found, Register First", Style.ALERT).show();

			Intent toRegistrationPage = new Intent(MyActivity.this,
					Registration_One.class);
			if (this.globalContext == null) {
				this.globalContext = (GlobalContext) getApplicationContext();
			}
			this.globalContext.SetRegistered(false);
			startActivity(toRegistrationPage);

		}
	}

	private SenderCheckDTO MakeSenderCheckDTO() {
		String str = etMobOrEmail.getText().toString();
		senderToCheck = new SenderCheckDTO();

		if (str.contains("@".toLowerCase())) {
			senderToCheck.Email = str;
		} else {
			senderToCheck.Phone = str;
		}

		return senderToCheck;
	}

	public void PrepareActionBar() {

		// set custom view to action bar
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);

		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator
				.inflate(R.layout.gloabl_action_barcustom_layout, null);

		if (this.globalContext == null) {
			this.globalContext = (GlobalContext) getApplicationContext();
		}

		if (globalContext.IsRegistred()) {
			// view references
			ImageButton abThreeBars = (ImageButton) v
					.findViewById(R.id.abThreeBars);
			ImageButton abUserIcon = (ImageButton) v
					.findViewById(R.id.abUserIcon);

			// image changes : three bars
			Drawable highlighted_threeBars = getResources().getDrawable(
					R.drawable.pride_menu_enabled);
			abThreeBars.setImageDrawable(highlighted_threeBars);

			// image changes : user profile
			Drawable highlighted_userprofile = getResources().getDrawable(
					R.drawable.pride_profile_enabled);
			abUserIcon.setImageDrawable(highlighted_userprofile);

		}

		actionBar.setCustomView(v, new ActionBar.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));
	}

	// action bar click handling method
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

			Crouton.makeText(MyActivity.this, "Profile Tapped", Style.INFO)
					.show();

			break;
		}
	}

	public void showSoftKeyboard(View view) {
		if (view.requestFocus()) {
			inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.showSoftInput(view,
					InputMethodManager.SHOW_IMPLICIT);
		}
	}

	// View Pager Adapter
	class CustomPagerAdapter extends FragmentPagerAdapter {

		Context mContext;

		public CustomPagerAdapter(FragmentManager fm, Context context) {
			super(fm);
			mContext = context;
		}

		@Override
		public Fragment getItem(int position) {

			Fragment fragment = null;

			switch (position) {
			case 0:
				fragment = new FirstSlide();
				break;

			case 1:
				fragment = new SecondSlide();
				break;

			case 2:
				fragment = new ThirdSlide();
				break;
			}
			return fragment;
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {

			case 0:

				break;

			case 1:

				break;

			case 2:

				break;

			}
			return "";
		}
	}

	private void ChangeThreeDotsImage(int vpItem) {

		imgViewThreeDots = (ImageView) findViewById(R.id.imgViewThreeDots);

		switch (vpItem) {
		case 0:
			Drawable swipe_1 = getResources().getDrawable(
					R.drawable.pride_swipe_1_enabled);
			imgViewThreeDots.setImageDrawable(swipe_1);
			break;

		case 1:
			Drawable swipe_2 = getResources().getDrawable(
					R.drawable.pride_swipe_2_enabled);
			imgViewThreeDots.setImageDrawable(swipe_2);
			break;

		case 2:
			Drawable swipe_3 = getResources().getDrawable(
					R.drawable.pride_swipe_3_enabled);
			imgViewThreeDots.setImageDrawable(swipe_3);
			break;

		}

	}

	private void ChangeSoftInputType(int vpPosition) {

		switch (vpPosition) {
		case 0:

			// Mobile two factor authentication for fresh users

			etMobOrEmail.setInputType(InputType.TYPE_CLASS_PHONE);
			showSoftKeyboard(etMobOrEmail);
			break;

		case 1:

			// Email two factor authentication for fresh users
			inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					etMobOrEmail.getWindowToken(), 0);
			break;

		case 2:

			// Email two factor authentication for fresh users

			inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(
					etMobOrEmail.getWindowToken(), 0);
			break;
		}
	}

	private void HandleSoftKeyBoardVisibility(int vpPosition) {

		inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

		switch (vpPosition) {
		case 0:

			// Mobile two factor authentication for fresh users

			// inputMethodManager.hideSoftInputFromWindow(
			// etMobOrEmail.getWindowToken(), 0);
			// inputMethodManager.hideSoftInputFromWindow(
			// btnMobEmailToggle.getWindowToken(), 0);
			break;

		case 1:
			// Email two factor authentication for fresh users

			inputMethodManager.hideSoftInputFromWindow(
					etMobOrEmail.getWindowToken(), 0);
			inputMethodManager.hideSoftInputFromWindow(
					btnMobEmailToggle.getWindowToken(), 0);
			break;

		case 2:
			// Email two factor authentication for fresh users

			inputMethodManager.hideSoftInputFromWindow(
					etMobOrEmail.getWindowToken(), 0);
			inputMethodManager.hideSoftInputFromWindow(
					btnMobEmailToggle.getWindowToken(), 0);
			break;
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
		navConfig.setMainLayout(R.layout.redesign_gallery);

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
			intent = new Intent(MyActivity.this, SelectTransactionScreen.class);
			startActivity(intent);
			break;

		case 101:
			// Completed
			intent = new Intent(MyActivity.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			intent = new Intent(MyActivity.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			intent = new Intent(MyActivity.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			intent = new Intent(MyActivity.this, FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			intent = new Intent(MyActivity.this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			intent = new Intent(MyActivity.this, ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

	public void InitializeControls() {

		viewPager = (ViewPager) findViewById(R.id.pager);

		CustomPagerAdapter adapter = new CustomPagerAdapter(
				getSupportFragmentManager(), MyActivity.this);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
		ChangeThreeDotsImage(viewPager.getCurrentItem());

		etMobOrEmail = (EditText) findViewById(R.id.etMobOrEmail);
		btnEnterMobileOrEmail = (Button) findViewById(R.id.btnEnterMobileOrEmail);
		btnEnterMobileOrEmail.setOnClickListener(this);
		btnMobEmailToggle = (Button) findViewById(R.id.btnMobEmailToggle);
		btnMobEmailToggle.setOnClickListener(this);
		btnMobEmailToggle.setText(Html.fromHtml("or <u>enter email</u>"));
		// font styles
		Typeface NotoSans = Typeface.createFromAsset(this.getAssets(),
				"fonts/NotoSans-Regular.ttf");

		btnMobEmailToggle.setTypeface(NotoSans);
		btnMobEmailToggle.setTextColor(Color.parseColor("#455A64"));
		PrepareActionBar();

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int page) {

		ChangeThreeDotsImage(page);
		HandleSoftKeyBoardVisibility(page);
	}

}
