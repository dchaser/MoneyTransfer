package fragments;

import java.util.ArrayList;

import com.example.moneytransfer.R;

import models.BankModel;
import utilities.Communicator;
import utilities.GlobalContext;
import utilities.TabMesenger;
import utilities.UserInterfaceHelper;
import viewmodels.SenderViewModel;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class BankFragment extends Fragment implements OnClickListener,
		OnItemSelectedListener, TabMesenger {
	Communicator comm;
	
	public TextView tvAddNewBank;
	public Spinner SpnRecieverBankName;
	public int CheckSpinner = 0;
	public EditText EtRecieverBankName;
	public EditText EtRecieverBranchName;
	public EditText EtRecieverAccountNo;
	public EditText EtRecieverAccountName;
	public EditText EtRecieverBankCode;
	
	private SenderViewModel SenderVM;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comm = (Communicator) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater
				.inflate(R.layout.fragment_bank_layout, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		InitializeControls();
		Bundle fullBucketToBankFragment = new Bundle();
		fullBucketToBankFragment = getArguments();

		this.SenderVM = fullBucketToBankFragment.getParcelable("senderVM");
		GlobalContext global = (GlobalContext) getActivity()
				.getApplicationContext();
		this.SenderVM.SetContext(global.GetDataContext(getActivity()));
		this.SenderVM.ReceiverVM.SetContext(global.GetDataContext(getActivity()));
		
		// LoadViewModels();
		LoadUI();
	}


	private void LoadUI() {

		if (this.SenderVM.ReceiverVM.bankVM.GetSelectedBank().GetId() == -1) {

			HideBankSpinner();
		} else {
			LoadSelectedBankToUI();
			LoadBankSpinner();
		}
	}

	private void HideBankSpinner() {
		
		this.tvAddNewBank.setCompoundDrawables(null, null, null, null);
		SpnRecieverBankName.setVisibility(View.GONE);
	}

	private void LoadSelectedBankToUI() {
		BankModel selectedBank = this.SenderVM.ReceiverVM.bankVM
				.GetSelectedBank();

		this.EtRecieverBankName.setText(selectedBank.GetBankName());
		this.EtRecieverBranchName.setText(selectedBank.GetBranchName());
		this.EtRecieverAccountNo.setText(selectedBank.GetAccountID());
		this.EtRecieverAccountName.setText(selectedBank.GetAcountName());
		this.EtRecieverBankCode.setText(selectedBank.GetBankCode());
	}

	private void LoadBankSpinner() {
		ArrayList<String> banknames = new ArrayList<String>();

		for (BankModel bank : SenderVM.ReceiverVM.bankVM.GetBanks()) {

			banknames.add(bank.GetBankName());
		}

		ArrayAdapter<String> banksAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, banknames);
		banksAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpnRecieverBankName.setAdapter(banksAdapter);
		SpnRecieverBankName.setOnItemSelectedListener(this);
	}

	private void AddOrUpdateBank() {
		UpdateSelectedBankFromUI();
	}

	public void ClearBankFields() {
		
		EtRecieverBankName.setText("");
		EtRecieverBranchName.setText("");
		EtRecieverAccountNo.setText("");
		EtRecieverAccountName.setText("");
		EtRecieverBankCode.setText("");
	}

	private void UpdateSelectedBankFromUI() {

		BankModel receiverBankDetails = this.SenderVM.ReceiverVM.bankVM
				.GetSelectedBank();

		receiverBankDetails.SetBankName(this.EtRecieverBankName.getText()
				.toString());
		receiverBankDetails.SetBranchName(this.EtRecieverBranchName.getText()
				.toString());
		receiverBankDetails.SetAccountID(this.EtRecieverAccountNo.getText()
				.toString());
		receiverBankDetails.SetAcountName(this.EtRecieverAccountName.getText()
				.toString());
		receiverBankDetails
				.SetBankCode(EtRecieverBankCode.getText().toString());
		this.SenderVM.ReceiverVM.bankVM.SetSelectedBank(receiverBankDetails);
	}

	private void InitializeControls() {

		this.EtRecieverBankName = UserInterfaceHelper
				.InitializeEditText(getActivity(), R.id.etRecieverBankName,
						this.EtRecieverBankName);

		this.EtRecieverBranchName = UserInterfaceHelper.InitializeEditText(
				getActivity(), R.id.etRecieverBranchName,
				this.EtRecieverBranchName);

		this.EtRecieverAccountNo = UserInterfaceHelper.InitializeEditText(
				getActivity(), R.id.etRecieverAccountNo,
				this.EtRecieverAccountNo);
		this.EtRecieverAccountName = UserInterfaceHelper.InitializeEditText(
				getActivity(), R.id.etRecieverAccountName,
				this.EtRecieverAccountName);

		this.EtRecieverBankCode = UserInterfaceHelper
				.InitializeEditText(getActivity(), R.id.etRecieverBankCode,
						this.EtRecieverBankCode);

		tvAddNewBank = (TextView) getActivity().findViewById(R.id.tvAddNewBank);
		tvAddNewBank.setOnClickListener(this);

		this.SpnRecieverBankName = (Spinner) getActivity().findViewById(
				R.id.spnRecieverBankName);
		this.SpnRecieverBankName.setOnItemSelectedListener(this);

	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.tvAddNewBank) {

			ClearBankFields();
			SenderVM.ReceiverVM.bankVM.SetSelectedBank(new BankModel());
			LoadSelectedBankToUI();

		} else {
			AddOrUpdateBank();
			comm.getSenderVMFromBankFragment(this.SenderVM.ReceiverVM.bankVM);
		}

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		CheckSpinner++;
		switch (parent.getId()) {

		case R.id.spnRecieverBankName:
			if (CheckSpinner > 0) {

				BankModel bank = this.SenderVM.ReceiverVM.bankVM.GetBanks()
						.get(position);
				this.SenderVM.ReceiverVM.bankVM.SetSelectedBank(bank);
				LoadSelectedBankToUI();
			}
			break;
		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
	

}
