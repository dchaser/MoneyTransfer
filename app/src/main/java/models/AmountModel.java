package models;

import android.os.Parcel;
import android.os.Parcelable;

public class AmountModel implements Parcelable {
	@com.google.gson.annotations.SerializedName("id")
	private long id;
	@com.google.gson.annotations.SerializedName("srcCode")
	private String srcCode;
	@com.google.gson.annotations.SerializedName("destCode")
	private String destCode;
	@com.google.gson.annotations.SerializedName("convertRate")
	private Double convertRate;
	@com.google.gson.annotations.SerializedName("amtSend")
	private Double amtSend;
	@com.google.gson.annotations.SerializedName("amtReceived")
	private Double amtReceived;
	private String cloudId;
	
	public AmountModel() {
		this.id = -1;
	}
	
	public AmountModel(Parcel in) {
		ReadFromParcel(in);
	}

	public String getCloudId() {
		return cloudId;
	}

	public void setCloudId(String cloudId) {
		this.cloudId = cloudId;
	}

	// Getters and Setters
	public long GetId() {
		return this.id;
	}

	public void SetId(long id) {
		this.id = id;
	}

	public String GetSrcCode() {
		return this.srcCode;
	}

	public void SetSrcCode(String srcCode) {
		this.srcCode = srcCode;
	}

	public String GetDestCode() {
		return this.destCode;
	}

	public void SetDestCode(String destCode) {
		this.destCode = destCode;
	}

	public Double GetConvertRate() {
		return this.convertRate;
	}

	public void SetConvertRate(Double convertRate) {
		this.convertRate = convertRate;
	}

	public Double GetAmtSend() {
		return this.amtSend;
	}

	public void SetAmtSend(Double amtSend) {
		this.amtSend = amtSend;
	}

	public Double GetAmtReceived() {
		return this.amtReceived;
	}

	public void SetAmtReceived(Double amtReceived) {
		this.amtReceived = amtReceived;
	}

	private void ReadFromParcel(Parcel in) {
		
		this.id = in.readLong();
		this.srcCode = in.readString();
		this.destCode = in.readString();
		this.convertRate = in.readDouble();
		this.amtSend = in.readDouble();
		this.amtReceived = in.readDouble();
		this.cloudId = in.readString();
	}

	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(this.id);
		dest.writeString(this.srcCode);
		dest.writeString(this.destCode);
		dest.writeDouble(this.convertRate);
		dest.writeDouble(this.amtSend);
		dest.writeDouble(this.amtReceived);
		dest.writeString(this.cloudId);
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source) {
			return new AmountModel(source);
		}

		@Override
		public Object[] newArray(int size) {
			return new AmountModel[size];
		}

	};

}
