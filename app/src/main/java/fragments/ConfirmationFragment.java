package fragments;

import viewmodels.SenderViewModel;

import com.example.moneytransfer.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.ViewGroup;
import android.widget.TextView;

public class ConfirmationFragment extends Fragment implements OnClickListener {

	SenderViewModel senderVM;
	// internal controls
	// sender
	TextView tv_Sender_Name;
	TextView tv_Sender_Phone;
	TextView tv_Sender_Address;
	TextView tv_Sender_Email;
	// receiver
	TextView tv_Receiver_Name;
	TextView tv_Receiver_Phone;
	TextView tv_Receiver_Bank;
	TextView tv_Receiver_AccountID;
	TextView tv_Receiver_BranchName;
	// transaction
	TextView tv_Transaction_Amount;
	TextView tv_Transaction_Fee;
	TextView tv_Transaction_Sent_Amount;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
	        
	    View v =     inflater.inflate(R.layout.fragment_confirmation_layout,
				container, false);
	    
	    final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
	            Context.INPUT_METHOD_SERVICE);
	        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
		return v;
		
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initializeControls();
		FillUpControls();
	}

	private void FillUpControls() {
		
		  tv_Sender_Name.setText(this.senderVM.SelectedSender.GetFirstName());
		  tv_Sender_Phone.setText(this.senderVM.SelectedSender.GetMobile());
		  tv_Sender_Address.setText(this.senderVM.SelectedSender.GetAddress().toString());
		  tv_Sender_Email.setText(this.senderVM.SelectedSender.GetEmail());
		
		
		  tv_Receiver_Name.setText(this.senderVM.ReceiverVM.GetSelectedReceiver().GetReceiverFullName());
		  tv_Receiver_Phone.setText(this.senderVM.ReceiverVM.GetSelectedReceiver().GetReceiverMobile());
		  tv_Receiver_Bank.setText(this.senderVM.ReceiverVM.bankVM.GetSelectedBank().GetBankName());
		  tv_Receiver_AccountID.setText(this.senderVM.ReceiverVM.bankVM.GetSelectedBank().GetAccountID());
		  tv_Receiver_BranchName.setText(this.senderVM.ReceiverVM.bankVM.GetSelectedBank().GetBranchName());
		

		  tv_Transaction_Amount.setText(this.senderVM.SelectedAmount.GetAmtSend().toString());
		  tv_Transaction_Fee.setText("AUD10");
		  tv_Transaction_Sent_Amount.setText(this.senderVM.SelectedAmount.GetAmtReceived().toString());
	}

	private void initializeControls() {

		tv_Sender_Name = (TextView) getActivity().findViewById(R.id.tv_SD_Name);
		tv_Sender_Phone = (TextView) getActivity().findViewById(
				R.id.tv_SD_Phone);
		tv_Sender_Address = (TextView) getActivity().findViewById(
				R.id.tv_SD_Address);
		tv_Sender_Email = (TextView) getActivity().findViewById(
				R.id.tv_SD_email);

		tv_Receiver_Name = (TextView) getActivity().findViewById(
				R.id.tv_RD_Name);
		tv_Receiver_Phone = (TextView) getActivity().findViewById(
				R.id.tv_RD_Phone);
		tv_Receiver_Bank = (TextView) getActivity().findViewById(
				R.id.tv_RD_bank);
		tv_Receiver_AccountID = (TextView) getActivity().findViewById(
				R.id.tv_RD_Account);
		tv_Receiver_BranchName = (TextView) getActivity().findViewById(
				R.id.tv_RD_Branch);

		tv_Transaction_Amount = (TextView) getActivity().findViewById(
				R.id.tv_trans_amount);
		tv_Transaction_Fee = (TextView) getActivity().findViewById(
				R.id.tv_trans_fee);
		tv_Transaction_Sent_Amount = (TextView) getActivity().findViewById(
				R.id.tv_trans_sent_amount);
	}

	@Override
	public void onClick(View v) {
		
	}

	public void getDetails(Bundle newB) {

		this.senderVM = newB.getParcelable("senderVM");
	}

}
