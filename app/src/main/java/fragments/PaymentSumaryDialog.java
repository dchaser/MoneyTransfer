package fragments;

import java.io.IOException;

import utilities.DbHelper;
import utilities.GlobalContext;
import viewmodels.SenderViewModel;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

public class PaymentSumaryDialog extends DialogFragment {

	SenderViewModel senderVM;

	TextView tvReceiverName;
	TextView tvReceiverBank;
	TextView tvReceiverAccountNumber;

	TextView tvSenderName;
	TextView tvSenderMobile;

	TextView tvAmountInDollars;
	TextView tvOurFee;
	TextView tvTotal;
	TextView tvRate;
	TextView tvAmtLkr;
	TextView tvCurrency;
	
	Typeface NotoSans;

	/**
	 * Create a new instance of PaymentSumaryDialog, providing "SenderViewModel"
	 * as an argument.
	 */
	public static PaymentSumaryDialog newInstance(
			SenderViewModel senderViewModel) {

		PaymentSumaryDialog f = new PaymentSumaryDialog();

		// Supply number input as an argument.
		Bundle args = new Bundle();
		args.putParcelable("senderVM", senderViewModel);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.senderVM = getArguments().getParcelable("senderVM");

		// Pick a style based on the num.
		int style = DialogFragment.STYLE_NORMAL, theme = 0;
		setStyle(style, theme);
		
		NotoSans = Typeface.createFromAsset(getActivity()
				.getAssets(), "fonts/NotoSans-Regular.ttf");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.payment_summary_dialog, container,
				false);

		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		getDialog().setCanceledOnTouchOutside(true);
		
		TextView tvPaymentSummary = (TextView) v.findViewById(R.id.tvPaymentSummary); 
		tvPaymentSummary.setTypeface(NotoSans);
		
		TextView tvPayTo = (TextView) v.findViewById(R.id.tvPayTo);
		tvPayTo.setTypeface(NotoSans);

		this.tvReceiverName = (TextView) v.findViewById(R.id.tvReceiverName);
		tvReceiverName.setTypeface(NotoSans);
		this.tvReceiverBank = (TextView) v.findViewById(R.id.tvReceiverBank);
		tvReceiverBank.setTypeface(NotoSans);
		this.tvReceiverAccountNumber = (TextView) v
				.findViewById(R.id.tvReceiverAccountNumber);
		tvReceiverAccountNumber.setTypeface(NotoSans);
		
		
		TextView tvFrom = (TextView) v.findViewById(R.id.tvFrom);
		tvFrom.setTypeface(NotoSans);

		this.tvSenderName = (TextView) v.findViewById(R.id.tvSenderName);
		tvSenderName.setTypeface(NotoSans);
		this.tvSenderMobile = (TextView) v.findViewById(R.id.tvSenderMobile);
		tvSenderMobile.setTypeface(NotoSans);

		TextView tvAmount = (TextView) v.findViewById(R.id.tvAmount);
		tvAmount.setTypeface(NotoSans);
		
		this.tvAmountInDollars = (TextView) v
				.findViewById(R.id.tvAmountInDollars);
		tvAmountInDollars.setTypeface(NotoSans);
		this.tvOurFee = (TextView) v.findViewById(R.id.tvOurFee);
		tvOurFee.setTypeface(NotoSans);
		this.tvTotal = (TextView) v.findViewById(R.id.tvTotal);
		tvTotal.setTypeface(NotoSans);
		
		TextView tvRateLeft = (TextView) v.findViewById(R.id.tvRateLeft);
		tvRateLeft.setTypeface(NotoSans);

		this.tvRate = (TextView) v.findViewById(R.id.tvRate);
		tvRate.setTypeface(NotoSans);

		this.tvAmtLkr = (TextView) v.findViewById(R.id.tvAmtLkr);
		tvAmtLkr.setTypeface(NotoSans);

		this.tvCurrency = (TextView) v.findViewById(R.id.tvCurrency);
		tvCurrency.setTypeface(NotoSans);

		// set pay to
		this.tvReceiverName.setText(senderVM.ReceiverVM.GetSelectedReceiver()
				.GetReceiverFullName());
		this.tvReceiverBank.setText(senderVM.ReceiverVM.bankVM
				.GetSelectedBank().GetBankName());
		this.tvReceiverAccountNumber.setText(senderVM.ReceiverVM.bankVM
				.GetSelectedBank().GetAccountID());
		// set from
		this.tvSenderName.setText(senderVM.SelectedSender.GetFirstName());
		this.tvSenderMobile.setText(senderVM.SelectedSender.GetMobile());
		// set amount details
		// 1)get Sent amount in Dollars append "AU$"
		double amtSent = senderVM.SelectedAmount.GetAmtSend();
		double fee = 10.00;
		

		this.tvAmountInDollars.setText("AU$ " + Double.toString(amtSent));
		this.tvOurFee.setText("AU$ 10.00(Fee)");
		this.tvTotal.setText("AU$ " + Double.toString(amtSent + fee));
		// 2)get Amount is Receiver currency
		double rate = senderVM.SelectedAmount.GetConvertRate();

		this.tvRate.setText(Double.toString(rate) + " LKR/AUD");
		this.tvAmtLkr.setText("AU$ " + Double.toString(amtSent) + " * "
				+ Double.toString(rate));
		this.tvCurrency.setText("LKR " + Double.toString(amtSent * rate));

		Button btnCancel = (Button) v.findViewById(R.id.btnCancel);
		btnCancel.setTypeface(NotoSans);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Cancel button click

				// pass sender VM to calling activity
				mListener.onCancelClick();

				getDialog().dismiss();
			}
		});

		Button btnConfirm = (Button) v.findViewById(R.id.btnConfirm);
		btnConfirm.setTypeface(NotoSans);
		btnConfirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Confirm button click

				if (isOnline()) {
					// update the sender view model so as soon as user taps 'Pay' button in
					// amount screen, so it will
					// trigger the update of newly added receivers, even when Sender may not done
					// transaction or not
					UpdateSenderVM();
					mListener.onConfirmClick(senderVM);
					getDialog().dismiss();
				} else {
					DisplayOflineToast();
				}

			}
		});

		return v;
	}

	public void UpdateSenderVM() {
		
		GlobalContext globalContext = (GlobalContext) getActivity()
				.getApplicationContext();

		this.senderVM.AddressVM.SetContext(globalContext
				.GetDataContext(getActivity()));

		this.senderVM.AmountVM.SetContext(globalContext
				.GetDataContext(getActivity()));

		this.senderVM.ReceiverVM.bankVM.SetContext(globalContext
				.GetDataContext(getActivity()));

		this.senderVM.ReceiverVM.SetContext(globalContext
				.GetDataContext(getActivity()));

		this.senderVM.UpdateSender();
		
		try {
			DbHelper.writeToSD(getActivity());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public void DisplayOflineToast() {
		Toast.makeText(getActivity(),
				"Please make sure you are connected to internet",
				Toast.LENGTH_SHORT).show();
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

	// .................
	public static interface OnTransactionCancelOrConfirm {

		public void onConfirmClick(SenderViewModel senderVM);

		public void onCancelClick();
	}

	private OnTransactionCancelOrConfirm mListener;

	@Override
	public void onAttach(Activity activity) {
		mListener = (OnTransactionCancelOrConfirm) activity;
		super.onAttach(activity);
	}

	@Override
	public void onDetach() {
		mListener = null;
		super.onDetach();
	}

}
