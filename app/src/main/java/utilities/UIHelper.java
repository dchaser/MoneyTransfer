package utilities;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class UIHelper {

	public static EditText InitializeEditText(Activity activity, int id,
			EditText et) {
		et = (EditText) activity.findViewById(id);
		((EditText) et).addTextChangedListener(new ContactValidator(
				(EditText) et, activity));
		((EditText) et).setOnFocusChangeListener(new ContactValidator(
				(EditText) et, activity));

		return et;
	}

	public static Button InitializeButtonWithOnclickListener(Activity activity,
			int id, Button btn, OnClickListener listener) {
		btn = (Button) activity.findViewById(id);
		((Button) btn).setOnClickListener(listener);
		return btn;
	}

	public static void SetTextET(EditText et, String text) {
		et.setText(text);
	}

}
