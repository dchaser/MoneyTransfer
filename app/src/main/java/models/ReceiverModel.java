package models;


import android.os.Parcel;
import android.os.Parcelable;

public class ReceiverModel implements Parcelable {

	private long id;
	private String receiverFullName;
	private String receiverMobile;
	private String receiverEmail;
	private String receiverFullAddress;
	private long senderId;
	private String cloudReference;
	
	public ReceiverModel() {
		id = -1;
	}

	public ReceiverModel(Parcel inward) {
		ReadFromParcel(inward);
	}
	
	public String GetCloudId() {
		return this.cloudReference;
	}

	public void SetCloudId(String cloudId) {
		this.cloudReference = cloudId;
	}


	// Getters and Setters
	public long GetId() {
		return this.id;
	}

	public void SetId(long id) {
		this.id = id;
	}



	public String GetReceiverFullName() {
		return this.receiverFullName;
	}

	public void SetReceiverFullName(String receiverName) {
		this.receiverFullName = receiverName;
	}

//	public String getReceiverLastName() {
//		return receiverLastName;
//	}
//
//	public void setReceiverLastName(String receiverLastName) {
//		this.receiverLastName = receiverLastName;
//	}

	public String GetReceiverMobile() {
		return this.receiverMobile;
	}

	public void SetReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String GetReceiverEmail() {
		return this.receiverEmail;
	}

	public long GetSenderId() {
		return this.senderId;
	}

	public void SetSenderId(long senderId) {
		this.senderId = senderId;
	}

	public void SetReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

	public String GetReceiverAddress() {
		return this.receiverFullAddress;
	}

	public void SetReceiverAddress(String receiverFullAddress) {
		this.receiverFullAddress = receiverFullAddress;
	}

	

	@Override
	public int describeContents() {
		return 0;
	}

	private void ReadFromParcel(Parcel inward) {
		
		this.id = inward.readLong();
		this.receiverFullName = inward.readString();
		this.receiverMobile = inward.readString();
		this.receiverEmail = inward.readString();
		this.receiverFullAddress = inward.readString();
		this.senderId = inward.readLong();
		this.cloudReference = inward.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeLong(this.id);
		dest.writeString(this.receiverFullName);
		dest.writeString(this.receiverMobile);
		dest.writeString(this.receiverEmail);
		dest.writeString(this.receiverFullAddress);
		dest.writeLong(this.senderId);
		dest.writeString(this.cloudReference);
	}

	public static final Parcelable.Creator<ReceiverModel> CREATOR = new Parcelable.Creator<ReceiverModel>() {

		@Override
		public ReceiverModel createFromParcel(Parcel source) {
			return new ReceiverModel(source);
		}

		@Override
		public ReceiverModel[] newArray(int size) {
			return new ReceiverModel[size];
		}
	};

}
