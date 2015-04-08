package models;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionModel implements Parcelable {
	
	@com.google.gson.annotations.SerializedName("id")
	private long id;
	@com.google.gson.annotations.SerializedName("senderID")
	private long senderID;
	@com.google.gson.annotations.SerializedName("receiverID")
	private long receiverID;
	@com.google.gson.annotations.SerializedName("amountID")
	private long amountID;
	@com.google.gson.annotations.SerializedName("date")
	private String date;
	@com.google.gson.annotations.SerializedName("status")
	private String status;
	@com.google.gson.annotations.SerializedName("receiverBankID")
	private long receiverBankID;	
	
	private String cloudId;

	public String GetCloudId() {
		return this.cloudId;
	}

	public void SetCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	@Override
	public String toString() {
		return "Transaction ID: " + GetId() + "\n" + "Sender ID: "
				+ GetSenderID() + "\n" + "Receiver ID: " + GetReceiverID()
				+ "\n" + " Amout ID: " + GetAmoutID() + "Date :" + GetDate()
				+ " Status: " + GetStatus();
	}

	public long GetId() {
		return this.id;
	}

	public void SetId(long id) {
		this.id = id;
	}

	public long GetSenderID() {
		return this.senderID;
	}

	public void SetSenderID(long senderID) {
		this.senderID = senderID;
	}

	public long GetReceiverID() {
		return this.receiverID;
	}

	public void SetReceiverID(long receiverID) {
		this.receiverID = receiverID;
	}

	public long GetAmoutID() {
		return this.amountID;
	}

	public void SetAmoutID(long amount) {
		this.amountID = amount;
	}

	public String GetDate() {
		return this.date;
	}

	public void SetDate(String date) {
		this.date = date;
	}

	public String GetStatus() {
		return this.status;
	}

	public void SetStatus(String status) {
		this.status = status;
	}

	public TransactionModel() {
		id = -1;
	}

	public TransactionModel(Parcel inward) {
		ReadFromParcel(inward);
	}

	

	public long GetReceiverBankID() {
		return receiverBankID;
	}

	public void SetReceiverBankID(long receiverBankID) {
		this.receiverBankID = receiverBankID;
	}

	//Parcel Methods
	
	public static final Parcelable.Creator<TransactionModel> CREATOR = new Parcelable.Creator<TransactionModel>() {

		@Override
		public TransactionModel createFromParcel(Parcel source) {
			return new TransactionModel(source);
		}

		@Override
		public TransactionModel[] newArray(int size) {
			return new TransactionModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(this.id);
		dest.writeLong(this.senderID);
		dest.writeLong(this.receiverID);
		dest.writeLong(this.amountID);
		dest.writeString(this.date);
		dest.writeString(this.status);
		dest.writeLong(this.receiverBankID);

	}
	
	

	private void ReadFromParcel(Parcel inward) {
		this.id = inward.readLong();
		this.senderID = inward.readLong();
		this.receiverID = inward.readLong();
		this.amountID = inward.readLong();
		this.date = inward.readString();
		this.status = inward.readString();
		this.receiverBankID = inward.readLong();
		this.cloudId = inward.readString();

	}

}
