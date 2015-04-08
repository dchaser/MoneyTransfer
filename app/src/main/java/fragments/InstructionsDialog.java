package fragments;

import redesign.screens.MainScreen;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.example.moneytransfer.R;

public class InstructionsDialog extends DialogFragment {

	String cloudID;
	String referenceCode;
	String sentAmount;

	TextView tvCloudCode;
	TextView tvStepOneInstructionsWithAmount;
	TextView tvStepTwoInstructionsWithCloudCode;

	Typeface NotoSans;

	Button btnOk;

	/**
	 * Create a new instance of PaymentSumaryDialog, providing "SenderViewModel"
	 * as an argument.
	 */
	public static InstructionsDialog newInstance(String cloudID,
			String sentAmount) {

		InstructionsDialog f = new InstructionsDialog();

		// Supply number input as an argument.
		Bundle args = new Bundle();
		args.putString("cloudID", cloudID);
		args.putString("sentAmount", sentAmount);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.cloudID = getArguments().getString("cloudID");
		this.referenceCode = this.cloudID.substring(this.cloudID.length() - 4);

		this.sentAmount = getArguments().getString("sentAmount");

		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		setStyle(style, theme);

		NotoSans = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/NotoSans-Regular.ttf");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View v = inflater
				.inflate(R.layout.instruction_dialog, container, false);

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(true);
		
		TextView tvAckKnowledgement = (TextView) v.findViewById(R.id.tvAckKnowledgement);
		tvAckKnowledgement.setTypeface(NotoSans);

		this.btnOk = (Button) v.findViewById(R.id.btnOk);
		btnOk.setTypeface(NotoSans);
		
		this.tvCloudCode = (TextView) v.findViewById(R.id.tvCloudCode);
		tvCloudCode.setTypeface(NotoSans);
		
		TextView tvInstructions = (TextView) v.findViewById(R.id.tvInstructions);
		tvInstructions.setTypeface(NotoSans);
		
		this.tvStepOneInstructionsWithAmount = (TextView) v
				.findViewById(R.id.tvStepOneInstructions);
		tvStepOneInstructionsWithAmount.setTypeface(NotoSans);
		this.tvStepTwoInstructionsWithCloudCode = (TextView) v
				.findViewById(R.id.tvStepTwoInstructions);
		tvStepTwoInstructionsWithCloudCode.setTypeface(NotoSans);

		/*
		 * $510.00 to your preferred bank account from the list below. For
		 * immediate transfer please use the same account as your netbank.
		 */

		this.tvCloudCode.setText("Code:  " + this.referenceCode);
		this.tvStepOneInstructionsWithAmount
				.setText("Please transfer $"
						+ this.sentAmount
						+ " to your preferred bank account from the list below. For immediate transfer please use the same account as your netbank");

		/* Add ABC1 to your description */
		String stepTwoInstructionWithColoredReferenceCode = "Add <font color='red'>"
				+ this.referenceCode + "</font>. to your description";
		this.tvStepTwoInstructionsWithCloudCode.setText(
				Html.fromHtml(stepTwoInstructionWithColoredReferenceCode),
				TextView.BufferType.SPANNABLE);

		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// OK button click

				getActivity().finish();
				
				Intent i = new Intent(getActivity(), MainScreen.class);
				startActivity(i);
				getDialog().dismiss();
			}
		});

		return v;

	}

	@Override
	public void onStart() {
		super.onStart();

		// safety check
		if (getDialog() == null) {
			return;
		}
		WindowManager.LayoutParams params = getDialog().getWindow()
				.getAttributes();
		params.width = LayoutParams.WRAP_CONTENT;
		params.height = LayoutParams.WRAP_CONTENT;

		getDialog().getWindow().setLayout(params.width, params.height);
	}

}
