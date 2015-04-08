package utilities;

import android.app.Activity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.TextView;

public class ContactValidator extends TextValidator implements
		OnFocusChangeListener {
	
	private Activity activity;

	public ContactValidator(TextView textView, Activity activity) {
		super(textView, activity);
		
		this.activity = activity;
	}


	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		if (!hasFocus) {
			validate((TextView) v, this.activity);
		}
	}

	@Override
	public boolean validate(TextView textView, Activity activity) {

		if (StringUtils.isBlank(textView.getText().toString())) {
			// reddish color
			textView.setError("This field is required!");
			// textView.setBackgroundColor(Color.parseColor("#8D2029"));
			return false;
		} else {
			// greenish color
			textView.setError(null);
			// textView.setBackgroundColor(Color.parseColor("#005641"));
			return true;
		}
	}


	@Override
	public boolean validateEmail(TextView textView, Activity activity) {
		// TODO Auto-generated method stub
		return false;
	}

	

}
