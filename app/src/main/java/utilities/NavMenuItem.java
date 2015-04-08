package utilities;

import android.content.Context;

public class NavMenuItem implements NavDrawerItem {
	// The class to describe a section item of the menu :

	public static final int ITEM_TYPE = 1;

	private int id;
	private String label;
	private int icon;
	private boolean updateActionBarTitle;

	private NavMenuItem() {
	}

	public static NavMenuItem create(int id, String label, String icon,
			boolean updateActionBarTitle, Context context) {
		NavMenuItem item = new NavMenuItem();
		item.setId(id);
		item.setLabel(label);
		item.setIcon(context.getResources().getIdentifier(icon, "drawable",
				context.getPackageName()));
		item.setUpdateActionBarTitle(updateActionBarTitle);
		return item;
	}
	
	public static NavMenuItem remove(int id, String label, String icon,
			boolean updateActionBarTitle, Context context) {
		NavMenuItem item = new NavMenuItem();
		item.setId(0);
		item.setLabel(null);
		item.setIcon(0);
		item.setUpdateActionBarTitle(false);
		return item;
	}
	
	

	@Override
	public int getType() {
		return ITEM_TYPE;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getIcon() {
		return icon;
	}

	public void setIcon(int icon) {
		this.icon = icon;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean updateActionBarTitle() {
		return this.updateActionBarTitle;
	}

	public void setUpdateActionBarTitle(boolean updateActionBarTitle) {
		this.updateActionBarTitle = updateActionBarTitle;
	}

	// The class to describe a section item of the menu :

}
