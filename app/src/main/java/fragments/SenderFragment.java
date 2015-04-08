package fragments;

import models.AddressModel;
import utilities.GlobalContext;
import utilities.UserInterfaceHelper;
import utilities.sender_registration_communicator;
import viewmodels.SenderViewModel;
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

import com.example.moneytransfer.R;

public class SenderFragment extends Fragment implements OnClickListener {

	sender_registration_communicator comm;

	private EditText EtSenderFullName;
	private EditText EtMobile;
	private EditText EtEmail;
	private EditText EtStreetNo;
	private EditText EtSuburb;
	private EditText EtPostCode;
	private Spinner spnState;

	public SenderViewModel senderVM;
	GlobalContext globalContext;

	private String selectedState = "";

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		comm = (sender_registration_communicator) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		comm = null;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.activity_registration_one, container,
				false);

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		// do normal procedure

		globalContext = (GlobalContext) getActivity().getApplicationContext();
		InitializeControls();

		this.senderVM = new SenderViewModel(
				globalContext.GetDataContext(getActivity()));

		int senderID = (int) this.senderVM.SelectedSender.GetId();

		if (senderID == 2) {
			// senderID == 2 means this sender is coming from Database and
			// has completed registration.

			InitializeControls();
			DisplayInfo();
		}

	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable("senderVM", this.senderVM);
	}

	private void DisplayInfo() {

		// get the first row from all Sender records as global sender
		if (this.senderVM.SelectedSender.GetId() != -1) {

			this.EtSenderFullName.setText(this.senderVM.SelectedSender
					.GetFirstName());
			this.EtMobile.setText(this.senderVM.SelectedSender.GetMobile());
			this.EtEmail.setText(this.senderVM.SelectedSender.GetEmail());

			AddressModel senderCurrentAddress = this.senderVM.SelectedSender
					.GetAddress();

			this.EtStreetNo.setText(senderCurrentAddress.GetStNo());
			// this.EtStreetName.setText(senderCurrentAddress.GetStName());
			this.EtSuburb.setText(senderCurrentAddress.GetSuburb());
			this.EtPostCode.setText(senderCurrentAddress.GetPostCode());

			// set spinner position

						for (int i = 0; i < COUNTRIES.length; i++) {
							if (COUNTRIES[i].equals(this.selectedState)) {
								spnState.setSelection(i);
							}
						}
						
						
		}
	}

	public void AddOrUpdateSender() {
		// create address
		AddressModel senderAddress = this.senderVM.SelectedSender.GetAddress();
		senderAddress.SetStNo(this.EtStreetNo.getText().toString());
		// senderAddress.SetStName(this.EtStreetName.getText().toString());
		senderAddress.SetSuburb(this.EtSuburb.getText().toString());
		senderAddress.SetPostCode(this.EtPostCode.getText().toString());
		senderAddress.SetState(this.selectedState);
		// senderAddress.SetCountry(this.EtCountry.getText().toString());

		// add details to sender
		this.senderVM.SelectedSender.SetFirstName(this.EtSenderFullName
				.getText().toString());
		// this.SenderVM.SelectedSender.SetLastName(this.EtSenderLastName
		// .getText().toString());
		this.senderVM.SelectedSender.SetMobile(this.EtMobile.getText()
				.toString());
		// this.SenderVM.SelectedSender.SetPhoneTwo(this.EtPhoneTwo.getText()
		// .toString());
		this.senderVM.SelectedSender
				.SetEmail(this.EtEmail.getText().toString());
		this.senderVM.SelectedSender.SetAddress(senderAddress);

	}

	public void InitializeControls() {

		this.EtSenderFullName = UserInterfaceHelper.InitializeEditText(
				getActivity(), R.id.etfName, EtSenderFullName);

		this.EtMobile = UserInterfaceHelper.InitializeEditText(getActivity(),
				R.id.etPhoneOne, EtMobile);

		this.EtEmail = UserInterfaceHelper.InitializeEditText(getActivity(),
				R.id.etEmail, EtEmail);

		this.EtStreetNo = UserInterfaceHelper.InitializeEditText(getActivity(),
				R.id.etStreetNo, EtStreetNo);

		this.EtSuburb = UserInterfaceHelper.InitializeEditText(getActivity(),
				R.id.etSuburb, EtSuburb);

		this.EtPostCode = UserInterfaceHelper.InitializeEditText(getActivity(),
				R.id.etPostCode, EtPostCode);

		// setting the state spinner

		spnState = (Spinner) getActivity().findViewById(R.id.spnState);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_spinner_item, COUNTRIES);

		spnState.setAdapter(adapter);

		spnState.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long arg3) {

				SpinnerChanged(parent.getSelectedItem());

			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});
	}

	private void SpinnerChanged(Object selectedItem) {

		this.selectedState = (String) selectedItem;
	}

	private static final String[] COUNTRIES = new String[] { "VICTORIA",
			"NEW SOUTH WALES", "QUEENSLAND", "WESTERN AUSTRALIA",
			"SOUTH AUSTRALIA", "NOTHERN TERRITORY", "TASMANIA", "CANBERRA" };

	@Override
	public void onClick(View v) {

		

	}

}
