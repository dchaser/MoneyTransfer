package utilities;

import android.text.InputFilter;
import android.text.Spanned;

public class NullHandler {

	public static boolean isNullOrBlank(String str) {

		if (str == null || str.isEmpty()) {
			return true;
		} else {
			return false;
		}

	}
	
	public static InputFilter filter = new InputFilter() {
		
		@Override
	    public CharSequence filter(CharSequence source, int start, int end,
	            Spanned dest, int dstart, int dend) {
	        for (int i = start; i < end; i++) {
	            if (!Character.isLetterOrDigit(source.charAt(i))) {
	                return "";
	            }
	        }
	        return null;
	    }

		
	};

}
