package dtos;

public class SenderInfoDTO {
	
	//class to recognize a sender initially when a device is connected to service via the app.
	
	//this should always be one (1) in the local DB of device
	public long LocalSenderId;
	
	//Cloud Identification record of the sender, this helps the cloud DB to identify a user globally
	@com.google.gson.annotations.SerializedName("senderId")
	public String SenderId;
	
	//verified status of local db 
	@com.google.gson.annotations.SerializedName("verified")
	public Boolean Verified;
}
