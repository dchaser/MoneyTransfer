package fragments;

import java.util.ArrayList;

import utilities.Communicator;
import utilities.GlobalContext;
import utilities.UserInterfaceHelper;
import viewmodels.SenderViewModel;
import models.BankModel;
import models.ReceiverModel;

import com.example.moneytransfer.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ReceiverFragment extends Fragment implements
		OnItemSelectedListener, OnClickListener {
	Communicator comm;
	// controls
	public TextView tvselectPrev;
	public Spinner SpnFirstName;
	public int CheckSpinner = 0;
	// ..........
	public EditText EtReceiverFirstName;
	public EditText EtReceiverPhoneOne;
	public EditText EtReceiverEmail;
	public EditText EtRecieverStreetNo;

	public SenderViewModel SenderVM = null;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comm = (Communicator) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_receiver_layout, container,
				false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		InitializeControls();
		Bundle fullBucketToReceiverFragment = new Bundle();
		fullBucketToReceiverFragment = getArguments();

		this.SenderVM = fullBucketToReceiverFragment.getParcelable("senderVM");
		GlobalContext global = (GlobalContext) getActivity()
				.getApplicationContext();
		this.SenderVM.SetContext(global.GetDataContext(getActivity()));
		// LoadViewModels();
		LoadUI();
	}

	// private void LoadViewModels() {
	//
	// GlobalContext globalContext = (GlobalContext)
	// getActivity().getApplicationContext();
	//
	// this.SenderVM.SetContext(globalContext.GetDataContext(getActivity()));
	// this.SenderVM.addressVM.SetContext(globalContext.GetDataContext(getActivity()));
	// this.SenderVM.receiverVM.addressVM.SetContext(globalContext.GetDataContext(getActivity()));
	//
	// }

	private void LoadUI() {

		if (this.SenderVM.ReceiverVM.GetSelectedReceiver().GetId() == -1) {
			// no receivers from back then

			HideSpinnersAndButtons();

			Toast.makeText(getActivity(), "Enter Details and press next",
					Toast.LENGTH_LONG).show();
			;
		} else {
			LoadSelectedReceiverToUI();
			LoadReceiverSpinner();
		}
	}

	private void HideSpinnersAndButtons() {

		this.tvselectPrev.setVisibility(View.GONE);
		SpnFirstName.setVisibility(View.GONE);
	}

	private void LoadSelectedReceiverToUI() {

		ReceiverModel receiver = this.SenderVM.ReceiverVM.GetSelectedReceiver();

		this.EtReceiverFirstName.setText(receiver.GetReceiverFullName());
		// this.EtReceiverLastName.setText(receiver.GetReceiverLastName());
		this.EtReceiverPhoneOne.setText(receiver.GetReceiverMobile());
		// this.EtReceiverPhonEtwo.setText(receiver.GetReceiverPhoneTwo());
		this.EtReceiverEmail.setText(receiver.GetReceiverEmail());
		this.EtRecieverStreetNo.setText(receiver.GetReceiverAddress());
		// this.EtRecieverStreetName.setText(receiver.GetReceiverAddress().GetStName());
		// this.EtRecieverSuburb.setText(receiver.GetReceiverAddress().GetSuburb());
		// this.EtRecieverPostCode.setText(receiver.GetReceiverAddress().GetPostCode());
		// this.EtRecieverState.setText(receiver.GetReceiverAddress().GetState());
		// this.EtRecieverCountry.setText(receiver.GetReceiverAddress().GetCountry());
	}

	private void LoadReceiverSpinner() {

		ArrayList<String> firstNames = new ArrayList<String>();

		for (ReceiverModel rec : this.SenderVM.ReceiverVM.GetReceivers()) {
			firstNames.add(rec.GetReceiverFullName());
		}
		firstNames.add("Add New Reciepient!");
		ArrayAdapter<String> firstNamesAdapter = new ArrayAdapter<String>(
				getActivity(), android.R.layout.simple_spinner_item, firstNames);
		firstNamesAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		SpnFirstName.setAdapter(firstNamesAdapter);
		SpnFirstName.setOnItemSelectedListener(this);
	}

	private void AddOrUpdateReceiver() {
		UpdateSelectedReceiverFromUI();
	}

	private void UpdateSelectedReceiverFromUI() {

		ReceiverModel receiver = this.SenderVM.ReceiverVM.GetSelectedReceiver();

		receiver.SetReceiverFullName(this.EtReceiverFirstName.getText()
				.toString());
		receiver.SetReceiverMobile(this.EtReceiverPhoneOne.getText().toString());
		receiver.SetReceiverEmail(this.EtReceiverEmail.getText().toString());
		receiver.SetSenderId(this.SenderVM.SelectedSender.GetId());
		receiver.SetReceiverAddress((this.EtRecieverStreetNo.getText()
				.toString()));

		// SenderVM.receiverVM.UpdateReceiver(receiver.GetId());

	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {

		CheckSpinner++;

		switch (parent.getId()) {

		case R.id.spnRecieverfName:

			if (CheckSpinner > 0) {
				GlobalContext context = (GlobalContext) getActivity()
						.getApplicationContext();
				if ((this.SenderVM.ReceiverVM.GetReceivers().size()) < position + 1) {
					// Add new Receiver Tapped
					this.SenderVM.ReceiverVM.bankVM = null;

					SenderVM.ReceiverVM.ChangeSelectedReceiver(
							new ReceiverModel(), context
									.GetDataContext(getActivity()
											.getApplicationContext()));

					CleareReceiverAndBankFields();

					// should clear bank fragment fields here

					// BankFragment fragBank = (BankFragment)
					// getActivity().getSupportFragmentManager()
					// .findFragmentByTag("tab2");
					//
					// fragBank.ClearBankFields();

					LoadSelectedReceiverToUI();
				} else {

					ReceiverModel receiver = this.SenderVM.ReceiverVM
							.GetReceivers().get(position);
					this.SenderVM.ReceiverVM.bankVM = null;

					this.SenderVM.ReceiverVM.ChangeSelectedReceiver(receiver,
							context.GetDataContext(getActivity()));

					LoadSelectedReceiverToUI();
				}
			}
			break;

		case R.id.spnRecieverBankName:
			if (CheckSpinner > 0) {

				BankModel bank = this.SenderVM.ReceiverVM.bankVM.GetBanks()
						.get(position);
				this.SenderVM.ReceiverVM.bankVM.SetSelectedBank(bank);
			}
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	private void CleareReceiverAndBankFields() {

		EtReceiverFirstName.setText("");
		// EtReceiverLastName.setText("");
		EtReceiverPhoneOne.setText("");
		// EtReceiverPhonEtwo.setText("");
		EtReceiverEmail.setText("");
		EtRecieverStreetNo.setText("");
		// EtRecieverStreetName.setText("");
		// EtRecieverSuburb.setText("");
		// EtRecieverPostCode.setText("");
		// EtRecieverState.setText("");
		// EtRecieverCountry.setText("");

	}

	public void InitializeControls() {

		this.EtReceiverFirstName = UserInterfaceHelper.InitializeEditText(
				getActivity(), R.id.etRecieverFullName,
				this.EtReceiverFirstName);

		this.EtReceiverPhoneOne = UserInterfaceHelper
				.InitializeEditText(getActivity(), R.id.etRecieverPhoneOne,
						this.EtReceiverPhoneOne);

		this.EtReceiverEmail = UserInterfaceHelper.InitializeEditText(
				getActivity(), R.id.etRecieverEmail, this.EtReceiverEmail);
		this.EtRecieverStreetNo = UserInterfaceHelper
				.InitializeEditText(getActivity(), R.id.etRecieverStreetNo,
						this.EtRecieverStreetNo);

		this.tvselectPrev = UserInterfaceHelper.InitilizeTextView(
				getActivity(), R.id.selectPrev, this.tvselectPrev);

		/*
		 * Removed Stuff
		 * 
		 * this.EtReceiverLastName = UserInterfaceHelper.InitializeEditText(
		 * getActivity(), R.id.etRecieverlName, this.EtReceiverLastName);
		 * 
		 * this.EtReceiverPhonEtwo = UserInterfaceHelper
		 * .InitializeEditText(getActivity(), R.id.etRecieverPhoneTwo,
		 * this.EtReceiverPhonEtwo);
		 * 
		 * this.EtRecieverBranchCode = UserInterfaceHelper.InitializeEditText(
		 * getActivity(), R.id.etRecieverBranchCode, this.EtRecieverBranchCode);
		 * 
		 * this.EtRecieverSwiftCode = UserInterfaceHelper.InitializeEditText(
		 * getActivity(), R.id.etRecieverSwiftCode, this.EtRecieverSwiftCode);
		 * 
		 * this.EtRecieverBranchPhone = UserInterfaceHelper.InitializeEditText(
		 * getActivity(), R.id.etRecieverBranchPhone,
		 * this.EtRecieverBranchPhone);
		 * 
		 * this.EtRecieverBankAddress = UserInterfaceHelper.InitializeEditText(
		 * getActivity(), R.id.etRecieverBankAddress,
		 * this.EtRecieverBankAddress);
		 * 
		 * this.EtRecieverStreetName = UserInterfaceHelper.InitializeEditText(
		 * getActivity(), R.id.etRecieverStreetName, this.EtRecieverStreetName);
		 * this.EtRecieverSuburb = UserInterfaceHelper.InitializeEditText(
		 * getActivity(), R.id.etRecieverSuburb, this.EtRecieverSuburb);
		 * this.EtRecieverPostCode = UserInterfaceHelper
		 * .InitializeEditText(getActivity(), R.id.etRecieverPostCode,
		 * this.EtRecieverPostCode); this.EtRecieverState =
		 * UserInterfaceHelper.InitializeEditText( getActivity(),
		 * R.id.etRecieverState, this.EtRecieverState); this.EtRecieverCountry =
		 * UserInterfaceHelper.InitializeEditText( getActivity(),
		 * R.id.etRecieverCountry, this.EtRecieverCountry);
		 */

		// TVAdd new Bank

		// Spinners
		this.SpnFirstName = (Spinner) getActivity().findViewById(
				R.id.spnRecieverfName);
		SpnFirstName.setOnItemSelectedListener(this);

	}

	@Override
	public void onClick(View v) {
		AddOrUpdateReceiver();
		comm.getSenderVMFromReceiverFragment(this.SenderVM);

	}

}
