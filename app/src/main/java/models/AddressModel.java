package models;

import android.os.Parcel;
import android.os.Parcelable;

public class AddressModel implements Parcelable {

	//@com.google.gson.annotations.SerializedName("id")
	private long id;
	@com.google.gson.annotations.SerializedName("steetNumber")
	private String stNo;
	@com.google.gson.annotations.SerializedName("suburb")
	private String suburb;
	@com.google.gson.annotations.SerializedName("postCode")
	private String pCode;
	@com.google.gson.annotations.SerializedName("state")
	private String state;
	private String cloudId;
//	@com.google.gson.annotations.SerializedName("country")
//	private String country;
	
	@Override
	public String toString() {

		return this.stNo +" "+ this.suburb +" "+ this.pCode +" "+ this.state;
	}
	
	

	public String GetCloudId() {
		return cloudId;
	}

	public void SetCloudId(String cloudId) {
		this.cloudId = cloudId;
	}


	public AddressModel() {
		this.id = -1;
	}

	// parcelable contents
	public AddressModel(Parcel in) {
		ReadFromParcel(in);
	}

	public long GetId() {
		return this.id;
	}

	public void SetId(long id) {
		this.id = id;
	}

	public String GetStNo() {
		return this.stNo;
	}

	public void SetStNo(String stNo) {
		this.stNo = stNo;
	}

	public String GetSuburb() {
		return this.suburb;
	}

	public void SetSuburb(String suburb) {
		this.suburb = suburb;
	}

	public String GetPostCode() {
		return this.pCode;
	}

	public void SetPostCode(String pCode) {
		this.pCode = pCode;
	}

	public String GetState() {
		return this.state;
	}

	public void SetState(String state) {
		this.state = state;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	private void ReadFromParcel(Parcel in) {
		
		this.id = in.readLong();
		this.stNo = in.readString();
		this.suburb = in.readString();
		this.pCode = in.readString();
		this.state = in.readString();
		this.cloudId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeLong(this.id);
		dest.writeString(this.stNo);
		dest.writeString(this.suburb);
		dest.writeString(this.pCode);
		dest.writeString(this.state);
		dest.writeString(this.cloudId);
	}

	public static final Parcelable.Creator<AddressModel> CREATOR = new Parcelable.Creator<AddressModel>() {

		@Override
		public AddressModel createFromParcel(Parcel in) {
			return new AddressModel(in);
		}

		@Override
		public AddressModel[] newArray(int size) {
			return new AddressModel[size];
		}

	};

}
