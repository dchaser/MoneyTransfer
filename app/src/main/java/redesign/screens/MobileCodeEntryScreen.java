package redesign.screens;

import java.io.IOException;
import java.util.ArrayList;

import models.AddressModel;
import models.BankModel;
import models.ReceiverModel;
import models.SenderModel;
import ui.sendmoney.SelectTransactionScreen;
import utilities.DataContext;
import utilities.DbHelper;
import utilities.GlobalContext;
import viewmodels.SenderViewModel;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.moneytransfer.R;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import dtos.Bank;
import dtos.Receiver;
import dtos.Sender;
import dtos.SenderCheckDTO;

public class MobileCodeEntryScreen extends Activity implements OnClickListener {

	ProgressDialog dial;
	TextView tvEnterCodeDescription;
	EditText etMobileCode;
	Button btnEnterMobileCodeOk;

	SenderCheckDTO senderToCheck;

	SenderViewModel senderVM;

	public String phone;
	public String email;

	private MobileServiceClient mobileClient;
	private GlobalContext globalContext;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mobile_pin_entry);
		getWindow().setBackgroundDrawableResource(R.drawable.background_image);

		if (savedInstanceState == null) {

			InitializeComponents();

			Intent i = getIntent();
			String whatIsPassed = i.getStringExtra("whatIsPassed");

			if (whatIsPassed.contains("@")) {

				this.email = whatIsPassed;
			} else {
				this.phone = whatIsPassed;
			}

			this.senderVM = new SenderViewModel();

		} else {

			InitializeComponents();
			this.senderVM = savedInstanceState.getParcelable("senderVM");
			this.email = savedInstanceState.getString("email");
			this.phone = savedInstanceState.getString("phone");
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable("senderVM", this.senderVM);
		outState.putString("phone", this.phone);
		outState.putString("email", this.email);
	}

	public void InitializeComponents() {

		this.etMobileCode = (EditText) findViewById(R.id.etMobileCode);
		etMobileCode.setInputType(InputType.TYPE_CLASS_NUMBER);

		this.btnEnterMobileCodeOk = (Button) findViewById(R.id.btnEnterMobileCodeOk);
		this.btnEnterMobileCodeOk.setOnClickListener(this);

		SetFontStyling();

	}

	private void SetFontStyling() {

		Typeface NotoSans = Typeface.createFromAsset(this.getAssets(),
				"fonts/NotoSans-Bold.ttf");

		this.tvEnterCodeDescription = (TextView) findViewById(R.id.tvEnterCodeDescription);

		tvEnterCodeDescription.setTypeface(NotoSans);
		tvEnterCodeDescription.setTextColor(Color.parseColor("#455A64"));
		tvEnterCodeDescription
				.setText(Html
						.fromHtml("<h3>We need to verify your number.</h3></br><p>Please enter the SMS code sent to your mobile.</p>"));

	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.btnEnterMobileCodeOk:

			VerifyMobileCode(etMobileCode.getText().toString());

			break;

		default:
			break;
		}

	}

	private void VerifyMobileCode(String code) {
		globalContext = (GlobalContext) getApplicationContext();
		// make DTO
		this.senderToCheck = new SenderCheckDTO();
		this.senderToCheck.Phone = this.phone;
		this.senderToCheck.Email = this.email;

		// access service client
		globalContext = (GlobalContext) getApplicationContext();
		mobileClient = globalContext.GetMobileClient();
		// make sender code as String object
		ArrayList<Pair<String, String>> parameters = new ArrayList<Pair<String, String>>();
		parameters.add(new Pair<String, String>("senderCode", code));

		if (isOnline()) {

			// open progress circle
			dial = new ProgressDialog(MobileCodeEntryScreen.this);
			dial.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dial.setCanceledOnTouchOutside(false);
			dial.show();
			dial.setContentView(R.layout.progressdialog_view);

			mobileClient.invokeApi("CustomTransaction", "GET", parameters,
					Sender.class, new ApiOperationCallback<Sender>() {

						@Override
						public void onCompleted(Sender result, Exception error,
								ServiceFilterResponse response) {

							if (error == null) {

								if (result != null) {
									
									dial.dismiss();
									SetSender(result);
								} else {
									dial.dismiss();
									Crouton.makeText(
											MobileCodeEntryScreen.this,
											"***"
													+ "Code you entered is invalid"
													+ "***", Style.INFO).show();
								}

							} else {
								dial.dismiss();
								Crouton.makeText(MobileCodeEntryScreen.this,
										"***" + error.toString() + "***",
										Style.ALERT).show();
							}

						}
					});

		} else {

			Crouton.makeText(MobileCodeEntryScreen.this,
					"Please check your internet connection and try again.",
					Style.INFO).show();
		}

	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	private void SetSender(Sender result) {

		dial.dismiss();
		if (result == null) {

			Crouton.makeText(MobileCodeEntryScreen.this,
					"Error, please try later.", Style.ALERT).show();
			globalContext = (GlobalContext) getApplicationContext();
			globalContext.SetRegistered(false);
		} else {
			// got sender

			FillSenderVMFromDTO(result, this.senderVM);
			GoToNextPage();
		}

	}

	private void FillSenderVMFromDTO(Sender senderDTO, SenderViewModel senderVM) {

		// set selected sender in Sender View Model
		this.senderVM.SelectedSender = MakeSender(senderDTO);
		FixContexts();

		globalContext = (GlobalContext) getApplicationContext();
		for (Receiver rec : senderDTO.Receivers) {

			ReceiverModel selectedReceiver = MakeReceiver(rec);
			this.senderVM.ReceiverVM.SetSelectedReceiver(selectedReceiver);

			for (Bank bank : rec.Banks) {

				BankModel selectedBank = MakeBank(bank);
				this.senderVM.ReceiverVM.bankVM.SetSelectedBank(selectedBank);

				this.senderVM.UpdateSender();
			}

			try {
				DbHelper.writeToSD(MobileCodeEntryScreen.this);
			} catch (IOException e) {

				e.printStackTrace();
			}

			globalContext = (GlobalContext) getApplicationContext();
			globalContext.SetRegistered(true);
		}

	}


	private void FixContexts() {

		// setup contexts and needed resources to update the DB
		globalContext = (GlobalContext) getApplicationContext();
		DataContext context = globalContext
				.GetDataContext(MobileCodeEntryScreen.this);
		this.senderVM.SetContext(context);
	}

	private void GoToNextPage() {

		Intent i = new Intent(MobileCodeEntryScreen.this,
				SelectTransactionScreen.class);
		i.putExtra("senderVM", this.senderVM);
		startActivity(i);
	}

	private SenderModel MakeSender(Sender senderDTO) {

		SenderModel selectedSender = new SenderModel();

		selectedSender.SetFirstName(senderDTO.FirstName);
		selectedSender.SetLastName(senderDTO.LastName);
		selectedSender.SetMobile(senderDTO.Mobile);
		selectedSender.SetEmail(senderDTO.Email);

		AddressModel senderAddress = new AddressModel();
		senderAddress.SetStNo(senderDTO.Address.AddressLineOne);
		senderAddress.SetSuburb(senderDTO.Address.Suburb);
		senderAddress.SetPostCode(senderDTO.Address.PostCode);
		senderAddress.SetState(senderDTO.Address.State);
		senderAddress.SetCloudId(senderDTO.Address.Id);

		selectedSender.SetAddress(senderAddress);
		selectedSender.SetVerificationStatus(senderDTO.Verified);
		selectedSender.SetCloudRefCode(senderDTO.Id);
		// set sender address id

		return selectedSender;

	}

	private ReceiverModel MakeReceiver(Receiver receiverDTO) {

		ReceiverModel selectedReceiver = new ReceiverModel();

		selectedReceiver.SetReceiverFullName(receiverDTO.FullName);
		selectedReceiver.SetReceiverMobile(receiverDTO.Mobile);
		selectedReceiver.SetReceiverEmail(receiverDTO.Email);
		selectedReceiver.SetReceiverAddress(receiverDTO.AddressString);
		selectedReceiver.SetCloudId(receiverDTO.Id);

		return selectedReceiver;
	}

	private BankModel MakeBank(Bank bankDTO) {

		BankModel selectedBank = new BankModel();

		selectedBank.SetBankName(bankDTO.BankName);
		selectedBank.SetAcountName(bankDTO.AcountName);
		selectedBank.SetBankCode(bankDTO.BankCode);
		selectedBank.SetAccountID(bankDTO.AccountID);
		selectedBank.SetBranchName(bankDTO.BranchName);
		selectedBank.SetCloudId(bankDTO.Id);

		return selectedBank;
	}

}
