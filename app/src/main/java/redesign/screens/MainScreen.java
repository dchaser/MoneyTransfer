package redesign.screens;

import ui.sendmoney.SelectTransactionScreen;
import utilities.DataContext;
import utilities.GlobalContext;
import utilities.ServiceHelper;
import viewmodels.SenderViewModel;
import viewmodels.TransactionViewModel;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.moneytransfer.R;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import dtos.SenderInfoDTO;
import dtos.TransactionsInfoDTO;

public class MainScreen extends Activity {

	public GlobalContext globalContext;
	private MobileServiceClient mobileClient;

	private TransactionViewModel transVM;
	public SenderViewModel senderVM;

	private long localSenderId = 0;
	private String LOGTAG = "MainScreen";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		if (savedInstanceState == null) 
		{			
			//go to next page after small wait, can display an animation here meanwhile
			GotoNextPageDuringAnimation();
		} 
		else 
		{	
			try 
			{
				this.transVM = savedInstanceState.getParcelable("transVM");
				this.senderVM = savedInstanceState.getParcelable("senderVM");
			} catch (Exception e) 
			{
				e.printStackTrace();
			}
			GotoNextPageDuringAnimation();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		outState.putParcelable("senderVM", this.senderVM);
		outState.putParcelable("transVM", this.transVM);
	}
	
	
	private void CheckChangesFromService() 
	{
		TransactionsInfoDTO transactionsInfoDTO = null;
		// If device is online
		if (ServiceHelper.isOnline(MainScreen.this)) {

			senderVM = new SenderViewModel();

			// get cloudRef and local Verification status into SenderInfoDTO
			SenderInfoDTO senderInfoDTO = senderVM
					.LoadSenderInfoDTO(globalContext.GetDataContext(this));

			if (senderInfoDTO != null) {

				transactionsInfoDTO = new TransactionsInfoDTO();
				localSenderId = senderInfoDTO.LocalSenderId;

				if (!senderInfoDTO.Verified) {

					transactionsInfoDTO.SenderInfo = senderInfoDTO;
				}
				DataContext dContext = globalContext.GetDataContext(MainScreen.this);
				transactionsInfoDTO.TransactionDetailsList = this.transVM
						.GetTransactionDetailsForPendingTransactions();
			}
		} else {
			Crouton.makeText(MainScreen.this, "Please turn on Mobile Data",
					Style.ALERT).show();
			senderVM = new SenderViewModel();
		}

		if (transactionsInfoDTO != null) {

			mobileClient = globalContext.GetMobileClient();

			mobileClient.invokeApi("CustomTransaction", transactionsInfoDTO,
					TransactionsInfoDTO.class,
					new ApiOperationCallback<TransactionsInfoDTO>() {
						public void onCompleted(TransactionsInfoDTO result,
								Exception error, ServiceFilterResponse response) {
							if (error != null) {
								String err = error.getMessage();
								Log.w(LOGTAG, err);

							} else {
								String noErr = "true";
								Log.w(LOGTAG, noErr);
								if (localSenderId > 0
										&& result.SenderInfo != null
										&& result.SenderInfo.Verified != null
										&& result.SenderInfo.Verified) {

									// Verify sender status
									senderVM.AuthorizeSenderVerification(localSenderId);
								}
								// Change transaction status
								transVM.ChangeTransactionStatus(result);
							}
						}

					});
		}

	}


	public void GotoNextPageDuringAnimation() 
	{
		new Handler().postDelayed(new Runnable() 
		{
			@Override
			public void run() 
			{
				GlobalContext globalContext = (GlobalContext) getApplicationContext();
				DataContext dContext = globalContext.GetDataContext(MainScreen.this);
				
				SenderViewModel senderVM = new SenderViewModel(dContext);
				Boolean status = senderVM.IsSenderRegistred(dContext);
				globalContext.SetRegistered(status);
				
				if (globalContext.IsRegistred()) 
				{
					// sender is registered and can send money
					Intent a = new Intent(MainScreen.this,
							SelectTransactionScreen.class);
					
					
					
					a.putExtra("senderVM", senderVM );
					startActivity(a);
					finish();
				} 
				else 
				{
					// sender not registered in the Local DB, maybe in Cloud DB
					// so go to Forst Screen
					Intent b = new Intent(MainScreen.this, MyActivity.class);
					startActivity(b);
					finish();
				}
			}
		}, 1000);

	}

}
