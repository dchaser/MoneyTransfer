package models;

import android.os.Parcel;
import android.os.Parcelable;

public class SenderModel implements Parcelable {

	private long id;
	private String senderFirstName;
	private String senderLastName;
	private String senderMobile;
	private String senderEmail;
	private AddressModel senderAddress;
	private String cloudReference;
	private Boolean verificationStatus = false;

	public SenderModel() {
		this.id = -1;
		this.senderAddress = new AddressModel();
	}

	public SenderModel(Parcel inward) {
		ReadFromParcel(inward);
	}

	public Boolean GetVerified() {
		return this.verificationStatus;
	}

	public void SetVerificationStatus(Boolean isVerified) {
		this.verificationStatus = isVerified;
	}

	// Getters and Setters
	public long GetId() {
		return this.id;
	}

	public void SetId(long id) {
		this.id = id;
	}

	public String GetCloudRefCode() {
		return this.cloudReference;
	}

	public void SetCloudRefCode(String cloudId) {
		this.cloudReference = cloudId;
	}

	public String GetFirstName() {
		return this.senderFirstName;
	}

	public String GetLastName() {
		return this.senderLastName;
	}

	public void SetFirstName(String firstName) {
		this.senderFirstName = firstName;
	}

	public void SetLastName(String lastName) {
		this.senderLastName = lastName;
	}

	public String GetMobile() {
		return this.senderMobile;
	}

	public void SetMobile(String mobile) {
		this.senderMobile = mobile;
	}

	public String GetEmail() {
		return this.senderEmail;
	}

	public void SetEmail(String email) {
		this.senderEmail = email;
	}

	public AddressModel GetAddress() {
		return this.senderAddress;
	}

	public void SetAddress(AddressModel address) {
		this.senderAddress = address;
	}

	// Parcel methods

	private void ReadFromParcel(Parcel in) {
		this.id = in.readLong();
		this.senderFirstName = in.readString();
		this.senderLastName = in.readString();
		this.senderMobile = in.readString();
		this.senderEmail = in.readString();
		this.senderAddress = in.readParcelable(AddressModel.class
				.getClassLoader());
		this.cloudReference = in.readString();
		this.verificationStatus = in.readByte() != 0; // myBoolean == true if
														// byte != 0

	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.senderFirstName);
		dest.writeString(this.senderLastName);
		dest.writeString(this.senderMobile);
		dest.writeString(this.senderEmail);
		dest.writeParcelable(this.senderAddress, 0);
		dest.writeString(this.cloudReference);

		if (this.verificationStatus == null) {
			dest.writeByte((byte) (0));
		} else {
			dest.writeByte((byte) (this.verificationStatus ? 1 : 0)); // if
																		// myBoolean
																		// ==
																		// true,
																		// byte
																		// == 1
		}

	}

	public static final Parcelable.Creator<SenderModel> CREATOR = new Parcelable.Creator<SenderModel>() {

		@Override
		public SenderModel createFromParcel(Parcel source) {
			return new SenderModel(source);
		}

		@Override
		public SenderModel[] newArray(int size) {
			return new SenderModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

}
