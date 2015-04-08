package ui.navbarLinks;

import utilities.AbstractNavDrawerActivity;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;

import com.example.moneytransfer.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class AboutPage extends AbstractNavDrawerActivity {

	// action bar
	android.support.v7.app.ActionBar actionBar;
	FrameLayout actionBarContainer;
	int abContainerViewID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		GlobalContext global = (GlobalContext) getApplicationContext();
		UserInterfaceHelper.PrepareActionBar(this, this.actionBar, global.IsRegistred());

		this.getWindow().setBackgroundDrawableResource(R.drawable.background_image);

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

	// ......Nav-Drawer..........

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
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
		navConfig.setMainLayout(R.layout.about_us);

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
			intent = new Intent(AboutPage.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(AboutPage.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(AboutPage.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(AboutPage.this, FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(AboutPage.this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipient Manager", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(AboutPage.this, ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}
}
