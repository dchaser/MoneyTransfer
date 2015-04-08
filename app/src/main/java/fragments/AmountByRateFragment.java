package fragments;

import java.text.DecimalFormat;

import utilities.Communicator;
import utilities.EditTextLocker;
import utilities.StringUtils;
import models.AmountModel;

import com.example.moneytransfer.R;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AmountByRateFragment extends Fragment implements OnClickListener {

	public EditText etAmountToSend;
	public TextView tvLkRAmount;

	Communicator comm;
	AmountModel amount;
	public Double convertionRate = 122.38;

	Double amoutToSend = 0.0;
	String amountInLKR = " ";
	Double amoutReceived = 0.0;

	@Override
	public void onAttach(Activity activity) {

		comm = (Communicator) activity;
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		LinearLayout ll = (LinearLayout) inflater.inflate(
				R.layout.amount_by_rate, container, false);

		this.etAmountToSend = (EditText) ll.findViewById(R.id.etAmountToSend);

		return ll;
		// return inflater.inflate(R.layout.amount_by_rate, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		initializeControls();
	}

	public void initializeControls() {

		this.amount = new AmountModel();

		tvLkRAmount = (TextView) getActivity().findViewById(R.id.tvLkRAmount);

		// etAmountToSend = (EditText) getActivity().findViewById(
		// R.id.etAmountToSend);

		EditTextLocker etLocker = new EditTextLocker(etAmountToSend);
		etLocker.limitFractionDigitsinDecimal(2);

		etAmountToSend.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

				try {
					String inserted = etAmountToSend.getText().toString();

					amoutToSend = Double.parseDouble(inserted);

				} catch (Exception e) {
					Toast.makeText(getActivity(), "Invalid Input",
							Toast.LENGTH_SHORT).show();
				}
				if (arg0.toString() == " ") {
					Toast.makeText(getActivity(), "Invalid Input",
							Toast.LENGTH_SHORT).show();
				}

				if (amoutToSend == null) {
					Toast.makeText(getActivity(), "Invalid Input",
							Toast.LENGTH_SHORT).show();
				} else {
					amoutReceived = amoutToSend * convertionRate;
				}

				// update the Dollar label

				DecimalFormat format = new DecimalFormat("##.##");
				String x = format.format(amoutReceived);

				amountInLKR = x + " LKR";
				tvLkRAmount.setText(amountInLKR);
			}
		});

		etAmountToSend.requestFocus();
		tvLkRAmount.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		ProcessAmout();
		comm.ProceccAmount(amount);
	}

	public boolean ProcessAmout() {

		boolean valid = false;

		if (!StringUtils.isBlank(etAmountToSend.getText().toString())) {

			amount.SetSrcCode("AUD");
			amount.SetDestCode("LKR");
			amount.SetConvertRate(convertionRate);
			amount.SetAmtSend(amoutToSend);
			amount.SetAmtReceived(amount.GetAmtSend() * convertionRate);

			valid = true;
			return valid;
		} else {

			amount.SetSrcCode("AUD");
			amount.SetDestCode("LKR");
			amount.SetConvertRate(convertionRate);
			amount.SetAmtSend(amoutToSend);
			amount.SetAmtReceived(amount.GetAmtSend() * convertionRate);

			Toast.makeText(getActivity(),
					"You cannoy proceed without a Valid Amount to send",
					Toast.LENGTH_SHORT).show();
			return valid;
		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

}
