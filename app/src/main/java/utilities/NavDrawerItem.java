package utilities;

public interface NavDrawerItem {

	// NavDrawerItem is the common interface for element types of the menu. The
	// method isEnabled() tells if the item is touchable or not (a section is
	// not).
	// The method updateActionBarTitle() indicates if the title of the action
	// bar must be updated when the user selects a menu item.

	public int getId();

	public String getLabel();

	public int getType();

	public boolean isEnabled();

	public boolean updateActionBarTitle();
}
