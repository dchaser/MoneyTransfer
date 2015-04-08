package models;

import android.os.Parcel;
import android.os.Parcelable;

public class TransactionHistoryModel implements Parcelable {

	// @com.google.gson.annotations.SerializedName("id")
	public long id;
	@com.google.gson.annotations.SerializedName("receiverName")
	private String receivername;
	@com.google.gson.annotations.SerializedName("code")
	private String code;
	@com.google.gson.annotations.SerializedName("amount_dollars")
	private Double amount_dollars;
	@com.google.gson.annotations.SerializedName("receiverid")
	private long receiverid;

	@Override
	public String toString() {

		return this.receivername + " " + this.code + " " + this.amount_dollars;
	}

	public TransactionHistoryModel() {
		this.id = -1;
	}

	public String getReceivername() {
		return receivername;
	}

	public void setReceivername(String receivername) {
		this.receivername = receivername;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getAmount_dollars() {
		return amount_dollars;
	}

	public void setAmount_dollars(Double amount_dollars) {
		this.amount_dollars = amount_dollars;
	}
	

	public long getReceiverid() {
		return receiverid;
	}

	public void setReceiverid(long receiverid) {
		this.receiverid = receiverid;
	}

	// parcelable contents
	public TransactionHistoryModel(Parcel in) {
		ReadFromParcel(in);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	private void ReadFromParcel(Parcel in) {

		this.id = in.readLong();
		this.receivername = in.readString();
		this.code = in.readString();
		this.amount_dollars = in.readDouble();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeLong(this.id);
		dest.writeString(this.receivername);
		dest.writeString(this.code);
		dest.writeDouble(this.amount_dollars);
	}

	public static final Parcelable.Creator<TransactionHistoryModel> CREATOR = new Parcelable.Creator<TransactionHistoryModel>() {

		@Override
		public TransactionHistoryModel createFromParcel(Parcel in) {
			return new TransactionHistoryModel(in);
		}

		@Override
		public TransactionHistoryModel[] newArray(int size) {
			return new TransactionHistoryModel[size];
		}

	};

	public void setId(long id) {
		this.id = id;;
	}
	
	public long getId() {

		return this.id;
	}

}
