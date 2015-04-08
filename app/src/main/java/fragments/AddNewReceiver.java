package fragments;

import models.ReceiverModel;
import utilities.CustomEditText;
import utilities.NullHandler;
import utilities.ReceiverDashboardCommunicator;
import utilities.UserInterfaceHelper;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

public class AddNewReceiver extends Fragment implements OnClickListener {

	ReceiverDashboardCommunicator comm;

	public CustomEditText EtReceiverFirstName;
	public CustomEditText EtReceiverPhoneOne;
	public CustomEditText EtReceiverEmail;
	public CustomEditText EtRecieverStreetNo;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comm = (ReceiverDashboardCommunicator) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.addnew_receipinet, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		InitializeControls();

	}

	private ReceiverModel UpdateSelectedReceiverFromUI() {

		ReceiverModel receiver = new ReceiverModel();

		// validate Receiver Full Name
		if (NullHandler.isNullOrBlank(this.EtReceiverFirstName.getText()
				.toString())) {
			Toast.makeText(getActivity(), "Receiver's Full Name is Blank",
					Toast.LENGTH_SHORT).show();
			this.EtReceiverFirstName.clearFocus();
		} else {
			receiver.SetReceiverFullName(this.EtReceiverFirstName.getText()
					.toString());
		}

		// validate Receiver Mobile Numbmer
		if (NullHandler.isNullOrBlank(this.EtReceiverPhoneOne.getText()
				.toString())) {
			Toast.makeText(getActivity(), "Receiver's Contact Number is Blank",
					Toast.LENGTH_SHORT).show();
			this.EtReceiverPhoneOne.clearFocus();
		} else {
			receiver.SetReceiverMobile(this.EtReceiverPhoneOne.getText()
					.toString());
		}

		receiver.SetReceiverEmail(this.EtReceiverEmail.getText().toString());
		receiver.SetReceiverAddress((this.EtRecieverStreetNo.getText()
				.toString()));

		return receiver;
	}

	public void InitializeControls() {

		this.EtReceiverFirstName = (CustomEditText) UserInterfaceHelper.InitializeEditText(
				getActivity(), R.id.etRecieverFullName,
				this.EtReceiverFirstName);

		this.EtReceiverPhoneOne = (CustomEditText) UserInterfaceHelper
				.InitializeEditText(getActivity(), R.id.etRecieverPhoneOne,
						this.EtReceiverPhoneOne);

		this.EtReceiverEmail = (CustomEditText) UserInterfaceHelper.InitializeEmailEditText(
				getActivity(), R.id.etRecieverEmail, this.EtReceiverEmail);
		this.EtRecieverStreetNo = (CustomEditText) UserInterfaceHelper
				.InitializeEditText(getActivity(), R.id.etRecieverStreetNo,
						this.EtRecieverStreetNo);
		
		SetFontStyling();
	}
	
	private void SetFontStyling() {

		Typeface NotoSans = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/NotoSans-Regular.ttf");

		TextView tvAddReceiverHeading = (TextView) getActivity().findViewById(R.id.addNewReceiverFragment);
		tvAddReceiverHeading.setTypeface(NotoSans);
		
		TextView tvReceiverName = (TextView) getActivity().findViewById(R.id.tvReceiverName);
		tvReceiverName.setTypeface(NotoSans);
		
		
		TextView tvReceiverPhone = (TextView) getActivity().findViewById(R.id.tvReceiverPhone);
		tvReceiverPhone.setTypeface(NotoSans);
		
		TextView tvReceiverEmail = (TextView) getActivity().findViewById(R.id.tvReceiverEmail);
		tvReceiverEmail.setTypeface(NotoSans);
		
		TextView receiverFullAddress = (TextView) getActivity().findViewById(R.id.receiverFullAddress);
		receiverFullAddress.setTypeface(NotoSans);
		
	}

	private boolean ValidateControls() {

		// validation code before sending senderVM to AmountScreen
		/*
		 * 1)Receiver Name 2)Receiver Mobile Number 3)Receiver Email 4)Receiver
		 * Address Line One
		 */

		int controlCounter = 4;

		if (EtReceiverFirstName.getText().toString().equalsIgnoreCase("")) {

			EtReceiverFirstName.setError("Please enter receiver's name");
			EtReceiverPhoneOne.requestFocus();
			controlCounter--;
		} else {
			EtReceiverFirstName.setError(null);
			EtReceiverPhoneOne.requestFocus();
		}

		if (EtReceiverPhoneOne.getText().toString().equalsIgnoreCase("")) {

			EtReceiverPhoneOne.setError("Please enter receiver's mobile");
			EtReceiverEmail.requestFocus();
			controlCounter--;
		} else {
			EtReceiverPhoneOne.setError(null);
			EtReceiverEmail.requestFocus();
		}

		if (EtReceiverEmail.getText().toString().equalsIgnoreCase("")) {

			EtReceiverEmail.setError("Please enter receiver's email");
			EtRecieverStreetNo.requestFocus();
			controlCounter--;
		} else {
			EtReceiverEmail.setError(null);
			EtRecieverStreetNo.requestFocus();
		}

		if (EtRecieverStreetNo.getText().toString().equalsIgnoreCase("")) {

			EtRecieverStreetNo.setError("Please enter receiver's Address");
			EtRecieverStreetNo.clearFocus();
			controlCounter--;
		} else {
			EtRecieverStreetNo.setError(null);
			EtRecieverStreetNo.clearFocus();
		}

		if (controlCounter < 4) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void onClick(View v) {

		if (ValidateControls()) {
			comm.GetNewReceiver(UpdateSelectedReceiverFromUI());
		} else {
			Toast.makeText(getActivity(), "Please fill required details", Toast.LENGTH_SHORT).show();
		}
		

	}

}
