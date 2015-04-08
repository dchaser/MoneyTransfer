package utilities;

import java.net.MalformedURLException;


import com.microsoft.windowsazure.mobileservices.MobileServiceClient;

import android.app.Application;
import android.content.Context;

public class GlobalContext extends Application {
	

	private MobileServiceClient mobileClient = null;
	private DataContext dataContext = null;
	
	private Boolean registered;
	
	private Double D_to_R_rate = 110.02; //2014/12/04
	private Double R_to_D_rate = 0.0098;
	

	public MobileServiceClient GetMobileClient() {
		if (this.mobileClient == null) {
			try {
				this.mobileClient = new MobileServiceClient(
						"https://pridemoneytransfer.azure-mobile.net/",
						"zxnRAuEZqEAJVcBgJvmkqIpPGvnCTL36", this);
//				this.mobileClient = new MobileServiceClient(
//						"https://localhost:58969",
//						"zxnRAuEZqEAJVcBgJvmkqIpPGvnCTL36", this);

			} catch (MalformedURLException e) {
				e.printStackTrace();
			}

		}

		return this.mobileClient;
	}

	public DataContext GetDataContext(Context context) {
		this.dataContext = null;
		if (this.dataContext == null) {
			try {
				this.dataContext = new DataContext(context);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		return dataContext;
	}

	public void SetRegistered(boolean registered){
		
		this.registered = registered;
	}
	
	public Boolean IsRegistred() {
		
		return this.registered;
	}

	
	//Conversion Rate getters and setters
	public Double getD_to_R_rate() {
		return D_to_R_rate;
	}

	public void setD_to_R_rate(Double d_to_R_rate) {
		D_to_R_rate = d_to_R_rate;
	}

	public Double getR_to_D_rate() {
		return R_to_D_rate;
	}

	public void setR_to_D_rate(Double r_to_D_rate) {
		R_to_D_rate = r_to_D_rate;
	}
	
}