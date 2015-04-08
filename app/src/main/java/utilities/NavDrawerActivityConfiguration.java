package utilities;

import android.widget.BaseAdapter;


public class NavDrawerActivityConfiguration {
	/*
	 * NavDrawerActivityConfiguration is the bean to configure the sliding menu
	 * and is needed by the AbstractNavDrawerActivity activity. Here’s a short
	 * description of the properties 
	 * 
	 * mainLayout : layout of the activity (the one with the DrawerLayout component) 
	 * drawerShadow : drawable for the shadow of the menu drawer
	 * LayoutId : id of the DrawerLayout component
	 * leftDrawerId : id of the component to slide from the left (ListView)
	 * actionMenuItemsToHideWhenDrawerOpen : menu items of the action bar to hide when the drawer is opened. 
	 * navItems : elements of the menu (section/items) 
	 * drawerOpenDesc : description of opened drawer (accessibility) 
	 * drawerCloseDesc : description of closed drawer (accessibility) 
	 * baseAdapter : adapter for the ListView
	 */

	private int mainLayout;
	private int drawerShadow;
	private int drawerLayoutId;
	private int leftDrawerId;
	private int[] actionMenuItemsToHideWhenDrawerOpen;
	private NavDrawerItem[] navItems;
	private int drawerOpenDesc;
	private int drawerCloseDesc;
	private BaseAdapter baseAdapter;

	public int getMainLayout() {
		return mainLayout;
	}

	public void setMainLayout(int mainLayout) {
		this.mainLayout = mainLayout;
	}

	public int getDrawerShadow() {
		return drawerShadow;
	}

	public void setDrawerShadow(int drawerShadow) {
		this.drawerShadow = drawerShadow;
	}

	public int getDrawerLayoutId() {
		return drawerLayoutId;
	}

	public void setDrawerLayoutId(int drawerLayoutId) {
		this.drawerLayoutId = drawerLayoutId;
	}

	public int getLeftDrawerId() {
		return leftDrawerId;
	}

	public void setLeftDrawerId(int leftDrawerId) {
		this.leftDrawerId = leftDrawerId;
	}

	public int[] getActionMenuItemsToHideWhenDrawerOpen() {
		return actionMenuItemsToHideWhenDrawerOpen;
	}

	public void setActionMenuItemsToHideWhenDrawerOpen(
			int[] actionMenuItemsToHideWhenDrawerOpen) {
		this.actionMenuItemsToHideWhenDrawerOpen = actionMenuItemsToHideWhenDrawerOpen;
	}

	public NavDrawerItem[] getNavItems() {
		return navItems;
	}

	public void setNavItems(NavDrawerItem[] navItems) {
		this.navItems = navItems;
	}

	public int getDrawerOpenDesc() {
		return drawerOpenDesc;
	}

	public void setDrawerOpenDesc(int drawerOpenDesc) {
		this.drawerOpenDesc = drawerOpenDesc;
	}

	public int getDrawerCloseDesc() {
		return drawerCloseDesc;
	}

	public void setDrawerCloseDesc(int drawerCloseDesc) {
		this.drawerCloseDesc = drawerCloseDesc;
	}

	public BaseAdapter getBaseAdapter() {
		return baseAdapter;
	}

	public void setBaseAdapter(BaseAdapter baseAdapter) {
		this.baseAdapter = baseAdapter;
	}
}
