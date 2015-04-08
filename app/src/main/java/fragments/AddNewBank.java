package fragments;

import models.BankModel;
import utilities.CustomEditText;
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

import com.example.moneytransfer.R;

public class AddNewBank extends Fragment implements OnClickListener {
	ReceiverDashboardCommunicator comm;

	public CustomEditText EtRecieverBankName;
	public CustomEditText EtRecieverBranchName;
	public CustomEditText EtRecieverAccountNo;
	public CustomEditText EtRecieverAccountName;
	public CustomEditText EtRecieverBankCode;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comm = (ReceiverDashboardCommunicator) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_add_new_bank, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		InitializeControls();

	}

	private BankModel UpdateSelectedBankFromUI() {

		BankModel selectedBank = new BankModel();

		// bank Name
		selectedBank.SetBankName(this.EtRecieverBankName.getText().toString());

		// account name
		selectedBank.SetAcountName(this.EtRecieverAccountName.getText()
				.toString());

		// bank code
		selectedBank.SetBankCode(EtRecieverBankCode.getText().toString());

		// account number/id
		selectedBank.SetAccountID(EtRecieverAccountNo.getText().toString());

		// branch name
		selectedBank.SetBranchName(this.EtRecieverBranchName.getText()
				.toString());

		return selectedBank;

	}

	private void InitializeControls() {

		this.EtRecieverBankName = (CustomEditText) UserInterfaceHelper
				.InitializeCustomEditText(getActivity(),
						R.id.etRecieverBankName, this.EtRecieverBankName);

		this.EtRecieverBranchName = (CustomEditText) UserInterfaceHelper
				.InitializeCustomEditText(getActivity(),
						R.id.etRecieverBranchName, this.EtRecieverBranchName);

		this.EtRecieverAccountNo = (CustomEditText) UserInterfaceHelper
				.InitializeCustomEditText(getActivity(),
						R.id.etRecieverAccountNo, this.EtRecieverAccountNo);
		this.EtRecieverAccountName = (CustomEditText) UserInterfaceHelper
				.InitializeCustomEditText(getActivity(),
						R.id.etRecieverAccountName, this.EtRecieverAccountName);

		this.EtRecieverBankCode = (CustomEditText) UserInterfaceHelper
				.InitializeCustomEditText(getActivity(),
						R.id.etRecieverBankCode, this.EtRecieverBankCode);
		
		SetFontStyling();

	}

	private void SetFontStyling() {

		Typeface NotoSans = Typeface.createFromAsset(getActivity().getAssets(),
				"fonts/NotoSans-Regular.ttf");

		TextView tvAddReceiverHeading = (TextView) getActivity().findViewById(R.id.tvAddBankHeading);
		tvAddReceiverHeading.setTypeface(NotoSans);
		
		TextView tvRecieverBankName = (TextView) getActivity().findViewById(R.id.tvRecieverBankName);
		tvRecieverBankName.setTypeface(NotoSans);
		
		
		TextView tvRecieverAccountName = (TextView) getActivity().findViewById(R.id.tvRecieverAccountName);
		tvRecieverAccountName.setTypeface(NotoSans);
		
		TextView tvRecieverBankCode = (TextView) getActivity().findViewById(R.id.tvRecieverBankCode);
		tvRecieverBankCode.setTypeface(NotoSans);
		
		TextView tvRecieverAccountNo = (TextView) getActivity().findViewById(R.id.tvRecieverAccountNo);
		tvRecieverAccountNo.setTypeface(NotoSans);
		
		TextView tvRecieverBranchName = (TextView) getActivity().findViewById(R.id.tvRecieverBranchName);
		tvRecieverBranchName.setTypeface(NotoSans);
	}

	@Override
	public void onClick(View v) {

		comm.GetNewBank(UpdateSelectedBankFromUI());

	}

}
