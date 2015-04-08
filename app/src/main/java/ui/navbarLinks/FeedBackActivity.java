package ui.navbarLinks;

import java.util.Arrays;
import java.util.List;

import utilities.AbstractNavDrawerActivity;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;

import com.example.moneytransfer.R;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

public class FeedBackActivity extends AbstractNavDrawerActivity implements
		android.view.View.OnClickListener {

	EditText etUserName;
	EditText etUserMessage;
	EditText etUserEmail;
	Button btnSubmit;
	OnClickListener listener;

	// action bar
	ActionBar actionBar;
	FrameLayout actionBarContainer;
	int abContainerViewID;
	
	GlobalContext globalContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		InitializeControls();

		this.getWindow().setBackgroundDrawableResource(R.drawable.background_image);

	}

	private void InitializeControls() {

		etUserName = (EditText) findViewById(R.id.etUserName);
		etUserEmail = (EditText) findViewById(R.id.etUserEmail);
		etUserMessage = (EditText) findViewById(R.id.etUserMessage);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);

		btnSubmit.setOnClickListener(this);

		globalContext = (GlobalContext) getApplicationContext();
		UserInterfaceHelper
				.PrepareActionBar(FeedBackActivity.this, this.actionBar,
						globalContext.IsRegistred());
	}

	@SuppressWarnings("static-access")
	private boolean validateControl() {

		String no_name = "Please enter a name";
		String no_message = "Please enter your feedback";
		String no_email = "Please enter an email address";
		String invalid_email = "Please enter a valid email";

		utilities.Validation valid = new utilities.Validation();

		if (valid.hasText(etUserName)) {
			if (valid.hasText(etUserMessage)) {
				if (valid.hasText(etUserEmail)) {
					if (valid.isEmailAddress(etUserEmail, true)) {
						return true;
					} else {
						AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
								FeedBackActivity.this);

						dlgAlert.setMessage(invalid_email);
						dlgAlert.setTitle("Error Message...");
						dlgAlert.setPositiveButton("OK", null);
						dlgAlert.setCancelable(true);
						dlgAlert.create().show();
						return false;

					}
				} else {
					AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
							FeedBackActivity.this);

					dlgAlert.setMessage(no_email);
					dlgAlert.setTitle("Error Message...");
					dlgAlert.setPositiveButton("OK", null);
					dlgAlert.setCancelable(true);
					dlgAlert.create().show();
					return false;
				}
			} else {
				AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
						FeedBackActivity.this);

				dlgAlert.setMessage(no_message);
				dlgAlert.setTitle("Error Message...");
				dlgAlert.setPositiveButton("OK", null);
				dlgAlert.setCancelable(true);
				dlgAlert.create().show();
				return false;
			}
		} else {
			AlertDialog.Builder dlgAlert = new AlertDialog.Builder(
					FeedBackActivity.this);

			dlgAlert.setMessage(no_name);
			dlgAlert.setTitle("Error Message...");
			dlgAlert.setPositiveButton("OK", null);
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onClick(View v) {
		if (validateControl()) {
			try {
				String fromEmail = "dasunchathuradha@gmail.com";
				String fromPassword = "0okmNJI(";
				String toEmails = etUserEmail.getText().toString();
				List<String> toEmailList = Arrays.asList(toEmails
						.split("\\s*,\\s*"));
				Log.i("FeedbackActivity", "To List: " + toEmailList);
				String emailSubject = "hide n seek feedback from "
						+ etUserName.getText().toString() + " via "
						+ etUserEmail.getText().toString();
				String emailBody = etUserMessage.getText().toString();
				new utilities.SendMailTask(FeedBackActivity.this).execute(
						fromEmail, fromPassword, toEmailList, emailSubject,
						emailBody);

				ClearFields();

			} catch (Exception e) {
				Log.e("SendMail", e.getMessage(), e);
			}
		}
	}

	private void ClearFields() {

		etUserName.setText(" ");
		etUserEmail.setText(" ");
		etUserMessage.setText(" ");
	}

	// Action Bar and Nav Drawer

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

	// .......................Nav-Drawer.........

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
		navConfig.setMainLayout(R.layout.activity_feedback);

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
			intent = new Intent( FeedBackActivity.this,
					CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent( FeedBackActivity.this,
					TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent( FeedBackActivity.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent( FeedBackActivity.this,
					FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent( FeedBackActivity.this,
					Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipient Manager", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent( FeedBackActivity.this,
					ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

}
