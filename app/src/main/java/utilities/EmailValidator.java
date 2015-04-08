package utilities;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class EmailValidator extends TextValidator implements OnFocusChangeListener {
	
	private Activity activity;

	public EmailValidator(TextView textView, Activity activity) {
		super(textView, activity);

		this.activity = activity;
	}

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			validateEmail((TextView) v, this.activity);
		}
	}

	@Override
	public boolean validate(TextView textView, Activity act) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean validateEmail(TextView textView, Activity activity) {
		if (StringUtils.isBlank(textView.getText().toString())) {
			// reddish color
//			textView.setError("Invalid Email");
			Crouton.makeText(activity, "email is empty", Style.INFO).show();
			 textView.setBackgroundColor(Color.parseColor("#455A64"));
			return false;
		} else if (!InputValidator.isEmailAddress((EditText) textView, true)) {

			textView.setError("Invalid Email");
			return false;
		} else {

			textView.setError(null);
			return true;
		}
	}
}
