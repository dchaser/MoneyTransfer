package ui.navbarLinks;

import java.io.IOException;

import ui.sendmoney.SelectTransactionScreen;
import utilities.AbstractNavDrawerActivity;
import utilities.DTOMapper;
import utilities.DataContext;
import utilities.DbHelper;
import utilities.GlobalContext;
import utilities.NavDrawerActivityConfiguration;
import utilities.NavDrawerItem;
import utilities.NavMenuItem;
import utilities.NavMenuSection;
import utilities.NavigationDrawerAdapter;
import utilities.UserInterfaceHelper;
import viewmodels.BankViewModel;
import viewmodels.ReceiverViewModel;
import viewmodels.SenderViewModel;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.moneytransfer.R;
import com.microsoft.windowsazure.mobileservices.ApiOperationCallback;
import com.microsoft.windowsazure.mobileservices.MobileServiceClient;
import com.microsoft.windowsazure.mobileservices.ServiceFilterResponse;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import dtos.Sender;

public class Registration_Three extends AbstractNavDrawerActivity implements
		OnClickListener {

	private ActionBar actionBar;

	public SenderViewModel senderVM;
	GlobalContext globalContext;
	private boolean photoAttached;
	private ProgressDialog dial;
	private MobileServiceClient mobileClient;
	private ImageView imgVTakenPhoto;
	private Button btnRegister;
	private boolean hasCloudID = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		InitializeControls();

		if (savedInstanceState == null) {

			Intent i = getIntent();
			this.senderVM = i.getParcelableExtra("senderVM");
			this.photoAttached = i.getBooleanExtra("photoAttached", false);

		} else {

			this.senderVM = savedInstanceState.getParcelable("senderVM");
		}

		if (this.senderVM.SelectedSender.GetCloudRefCode() != null) {

			this.hasCloudID = true;
		}

		if (this.photoAttached) {

			SetImageView();

			if (hasCloudID) {

				this.btnRegister.setText("Update");
			}

		} else {
			Toast.makeText(this, "Photo was not attached", Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void SetImageView() {

		if (this.senderVM.SenderIdentity != null) {

			imgVTakenPhoto.setImageBitmap(this.senderVM.SenderIdentity);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		outState.putParcelable("senderVM", this.senderVM);
		
		this.globalContext = (GlobalContext) getApplicationContext();
		this.mobileClient = this.globalContext.GetMobileClient();
	}

	private void InitializeControls() {

		imgVTakenPhoto = (ImageView) findViewById(R.id.imgVTakenPhoto);
		btnRegister = (Button) findViewById(R.id.btnRegister);

		Typeface NotoSans = Typeface.createFromAsset(this.getAssets(),
				"fonts/NotoSans-Regular.ttf");
		btnRegister.setTypeface(NotoSans);
		btnRegister.setOnClickListener(this);

		globalContext = (GlobalContext) getApplicationContext();

		UserInterfaceHelper.PrepareRegistrationPageTwoActionBar(this,
				actionBar, globalContext.IsRegistred());
	}

	// private boolean ValidateSenderViewModel(SenderViewModel senderVM2) {
	//
	// // check is the image is really attached and let continuity
	// if (senderVM2.SenderIdentity != null) {
	// return true;
	// } else {
	// Toast.makeText(Registration_Three.this, "Photo not attached",
	// Toast.LENGTH_SHORT).show();
	// return false;
	//
	// }
	//
	// }

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.btnRegister:
			
			if (photoAttached) {

				if (this.hasCloudID) {
					// button displays update as in its flow
					UpdateServiceSender();
				}
				globalContext = (GlobalContext) getApplicationContext();
				DataContext dContext = globalContext
						.GetDataContext(Registration_Three.this);
				this.senderVM.SetContext(dContext);
				
				if(this.senderVM.ReceiverVM == null){
					
					this.senderVM.ReceiverVM =  new ReceiverViewModel(dContext);
					this.senderVM.ReceiverVM.bankVM = new BankViewModel(dContext);
					
				}
				this.senderVM.UpdateSender();
				
				try {
					DbHelper.writeToSD(Registration_Three.this);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// set status to registered sender inside System,
				this.globalContext = (GlobalContext) getApplicationContext();
				globalContext.SetRegistered(true);

				// first time User
				Intent a = new Intent(Registration_Three.this,
						SelectTransactionScreen.class);

				a.putExtra("senderVM", this.senderVM);
				startActivity(a);

			}

			break;

		case R.id.tvBack:

			onBackPressed();
			break;

		default:
			break;
		}
	}

	// ...............update flow code..........

	private void UpdateServiceSender() {

		// make the sender DTO first
		DTOMapper mapper = new DTOMapper(this.senderVM);
		Sender senderUpdateDTO = mapper.createSenderUpdateDTOwithoutReceivers();

		if (isOnline()) {

			// open progress dialog circling
			dial = new ProgressDialog(Registration_Three.this);
			dial.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dial.setCanceledOnTouchOutside(false);
			dial.show();
			dial.setContentView(R.layout.progressdialog_view);

			// service call
			GlobalContext global = (GlobalContext) getApplicationContext();
			this.mobileClient = global.GetMobileClient();

			mobileClient.invokeApi("SenderOperations", senderUpdateDTO,
					Boolean.class, new ApiOperationCallback<Boolean>() {

						@Override
						public void onCompleted(Boolean result,
								Exception error, ServiceFilterResponse response) {

							if (error == null) {

								ProcessResult(result);

							} else {
								dial.dismiss();
								Crouton.makeText(Registration_Three.this,
										"***" + "Update Failed" + "***",
										Style.INFO).show();
							}

						}

					});

		} else {

			Crouton.makeText(Registration_Three.this,
					"Please check your internet connection and try again.",
					Style.INFO).show();
		}

	}

	private void ProcessResult(Boolean result) {

		if (result) {

			dial.dismiss();

			Intent i = new Intent(Registration_Three.this,
					SelectTransactionScreen.class);

			// senderVM with Verification image
			i.putExtra("senderVM", this.senderVM);
			GlobalContext global = (GlobalContext) getApplicationContext();

			long senderID = this.senderVM.ModifySender(global
					.GetDataContext(Registration_Three.this));

			try {
				DbHelper.writeToSD(Registration_Three.this);
			} catch (IOException e) {
				e.printStackTrace();
			}

			if (senderID > -1) {

				global.SetRegistered(true);
			} else {

				global.SetRegistered(false);
			}

			// still we have not sent the Photo to Cloud so we should
			// keep
			// it in the View Model and pass it to cloud with relevant
			// information letting the user Send Money even at the first
			// time of use
			// When opening the next screen it will be given with
			// SenderVM with attached photo.
			startActivity(i);
			finish();

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

	@Override
	protected NavDrawerActivityConfiguration getNavDrawerConfiguration() {

		GlobalContext globalContext = (GlobalContext) getApplicationContext();
		NavDrawerItem[] menu;
		if (globalContext.IsRegistred()) {

			// navItems : elements of the menu (section/items)
			menu = new NavDrawerItem[] {
					NavMenuItem.create(99, "Home", "completed_tick_drawer",
							false, this),
					NavMenuSection.create(100, "HISTORY"),
					NavMenuItem.create(101, "Completed",
							"completed_tick_drawer", false, this),
					NavMenuSection.create(200, "HELP"),
					NavMenuItem
							.create(202, "Tutorial", "tutorial", false, this),
					NavMenuItem.create(203, "About", "about_man_drawer", false,
							this),
					NavMenuItem.create(204, "Feedback", "about_man_drawer",
							false, this),
					NavMenuSection.create(300, "My Profile"),
					NavMenuItem.create(301, "My Details", "about_man_drawer",
							false, this),
					NavMenuItem.create(302, "Receivers", "about_man_drawer",
							false, this) };

		} else {

			// navItems : elements of the menu (section/items)
			menu = new NavDrawerItem[] {
					NavMenuItem.create(99, "Home", "completed_tick_drawer",
							false, this),
					NavMenuSection.create(100, "HISTORY"),
					NavMenuItem.create(101, "Completed",
							"completed_tick_drawer", false, this),
					NavMenuSection.create(200, "HELP"),
					NavMenuItem
							.create(202, "Tutorial", "tutorial", false, this),
					NavMenuItem.create(203, "About", "about_man_drawer", false,
							this),
					NavMenuItem.create(204, "Feedback", "about_man_drawer",
							false, this) };

		}

		NavDrawerActivityConfiguration navConfig = new NavDrawerActivityConfiguration();
		navConfig.setMainLayout(R.layout.activity_registration_three);
		navConfig.setDrawerLayoutId(R.id.drawer_layout);
		navConfig.setLeftDrawerId(R.id.left_drawer);
		navConfig.setNavItems(menu);
		navConfig.setDrawerShadow(R.drawable.drawer_shadow);
		navConfig.setDrawerOpenDesc(R.string.drawer_open);
		navConfig.setDrawerCloseDesc(R.string.drawer_close);
		// baseAdapter : adapter
		navConfig.setBaseAdapter(new NavigationDrawerAdapter(this,
				R.layout.navdrawer_item, menu));

		return navConfig;
	}

	@Override
	protected void onNavItemSelected(int id) {
		Intent intent;
		switch ((int) id) {
		case 99:
			Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_Three.this,
					SelectTransactionScreen.class);
			startActivity(intent);
			break;
		case 101:
			// Completed
			Toast.makeText(this, "Completed Transactions Page",
					Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_Three.this, CompletedPage.class);
			startActivity(intent);
			break;
		case 202:
			// Tutorial
			Toast.makeText(this, "Tutorial Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_Three.this, TutorialPage.class);
			startActivity(intent);
			break;

		case 203:
			// About
			Toast.makeText(this, "About Page", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_Three.this, AboutPage.class);
			startActivity(intent);
			break;

		case 204:
			// Feedback
			Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_Three.this, FeedBackActivity.class);
			startActivity(intent);
			break;

		case 301:
			// My Profile
			Toast.makeText(this, "My Profile", Toast.LENGTH_SHORT).show();
			intent = new Intent(Registration_Three.this, Registration_One.class);
			startActivity(intent);
			break;

		case 302:
			// My Receivers
			Toast.makeText(this, "Receipient Manager", Toast.LENGTH_SHORT)
					.show();
			intent = new Intent(Registration_Three.this,
					ReceiverDashboard.class);
			startActivity(intent);
			break;
		}

	}

}
