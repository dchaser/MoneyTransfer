package utilities;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

public abstract class TextValidator implements TextWatcher {
	private final TextView textView;
	private final Activity activity;

	public TextValidator(TextView textView, Activity activity) {
		this.textView = textView;
		this.activity = activity;
	}

	public abstract boolean validate(TextView textView, Activity activity);

	public abstract boolean validateEmail(TextView textView, Activity activity);

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {

	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {

	}

	@Override
	public void afterTextChanged(Editable s) {

		validate(textView, activity);
	}

}
