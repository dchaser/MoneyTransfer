package fragments;

import utilities.GlobalContext;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

public class CurrencyRateFragment extends Fragment implements OnClickListener {

	TextView tvRate;
	LinearLayout llBottom;
	EditText etCurrencyRate;
	TextView tvLkr;
	private int counter = 0;
	
	public String sendCurrency = "AUD"; 
	
	GlobalContext global;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.network_currency_rate_fragment,
				container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);

		tvRate = (TextView) getActivity().findViewById(R.id.tvRate);
		tvRate.setOnClickListener(this);

		llBottom = (LinearLayout) getActivity().findViewById(R.id.llBottom);

		if (isOnline()) {
			MakeUI(this.sendCurrency);
			Toast.makeText(getActivity(), "Online", Toast.LENGTH_SHORT).show();
		} else {
			AlterUI();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		if (counter > 0) {

			if (isOnline()) {
				this.sendCurrency = (this.sendCurrency == "AUD") ? "LKR" : "AUD";
				MakeUI(this.sendCurrency);
				// Toast.makeText(getActivity(), "Online Now",
				// Toast.LENGTH_SHORT)
				// .show();
			}
		}
		counter++;
	}

	@Override
	public void onStop() {

		super.onStop();
	}

	@Override
	public void onDestroy() {

		super.onDestroy();
	}

	public void MakeUI(String sendingCurrency) {

		llBottom = (LinearLayout) getActivity().findViewById(R.id.llBottom);
		llBottom.removeAllViewsInLayout();
		llBottom.setBackgroundResource(0);

		LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		

		llBottom.setLayoutParams(lp);
		llBottom.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
		llBottom.setBackgroundResource(R.drawable.linearlayout_border);
		llBottom.setOrientation(LinearLayout.HORIZONTAL);
		llBottom.setPadding(5, 5, 5, 0);

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			TextView tvCurrencyRate = new TextView(getActivity());

			tvCurrencyRate.setId(R.id.etCurrencyRate);
			LayoutParams lpOne = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.WRAP_CONTENT);

			tvCurrencyRate.setLayoutParams(lpOne);
			if(sendingCurrency == "AUD"){
				tvCurrencyRate.setText("1 "+sendingCurrency+" = ");
			}else{
				tvCurrencyRate.setText("1 "+sendingCurrency+" = ");
			}
			
			tvCurrencyRate.setTextColor(Color.BLACK);
			tvCurrencyRate.setTextSize(30);
			//tvCurrencyRate.setPadding(0, 20, 0, 0);
			tvCurrencyRate.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);

			
			//...........
			TextView tvSecondLkr = new TextView(getActivity());
			tvSecondLkr.setId(R.id.tvLkr);

			LayoutParams lpSecond = new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT,
					LinearLayout.LayoutParams.MATCH_PARENT);

			tvSecondLkr.setLayoutParams(lpSecond);
			tvSecondLkr.setPadding(10, 0, 0, 0);
			tvSecondLkr.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
			
			global = (GlobalContext) getActivity().getApplicationContext();
			
			if(sendingCurrency == "AUD"){
				tvSecondLkr.setText("LKR "+global.getD_to_R_rate());
			}else{
				tvSecondLkr.setText("AUD "+global.getR_to_D_rate());
			}
			
			tvSecondLkr.setTextColor(Color.BLACK);
			tvSecondLkr.setTextSize(30);

			llBottom.addView(tvCurrencyRate,0);
			llBottom.addView(tvSecondLkr,1);
			llBottom.invalidate();
		}
	}

	public void AlterUI() {
		llBottom = (LinearLayout) getActivity().findViewById(R.id.llBottom);
		llBottom.removeAllViewsInLayout();
		llBottom.setBackgroundResource(0);
		LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		llBottom.setLayoutParams(lp);
		//llBottom.setGravity(Gravity.START);

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			ImageView img = new ImageView(getActivity().getBaseContext());
			img.setImageDrawable(getActivity().getResources().getDrawable(
					R.drawable.connection_error));
			llBottom.addView(img);
			img.invalidate();
			llBottom.invalidate();
		}
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		if (isOnline()) {
			Toast.makeText(getActivity(), "Online", Toast.LENGTH_SHORT).show();
			this.sendCurrency = (this.sendCurrency == "AUD") ? "LKR" : "AUD";
			MakeUI(sendCurrency);
			
		} else {
			AlterUI();
		}
	}
}
