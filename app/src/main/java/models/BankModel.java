package models;

import android.os.Parcel;
import android.os.Parcelable;

public class BankModel implements Parcelable {

	private long id;
	private String bankName;
	private String acountName;
	private String bankCode;
	private String accountID;
	private String branchName;	
	private long receiverID;
	private String cloudReference;
	
	
	public BankModel() {
		this.id = -1;
	}

	public BankModel(Parcel inward) {
		ReadFromParcel(inward);
	}

	public String GetCloudId() {
		return this.cloudReference;
	}

	public void SetCloudId(String cloudId) {
		this.cloudReference = cloudId;
	}
	
	public long GetId() {
		return id;
	}

	public void SetId(long id) {
		this.id = id;
	}

	public String GetBankName() {
		return bankName;
	}

	public void SetBankName(String bankName) {
		this.bankName = bankName;
	}

	public String GetBranchName() {
		return branchName;
	}

	public void SetBranchName(String branchName) {
		this.branchName = branchName;
	}

	public String GetAccountID() {
		return accountID;
	}

	public void SetAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String GetAcountName() {
		return acountName;
	}

	public void SetAcountName(String acountName) {
		this.acountName = acountName;
	}

	public String GetBankCode() {
		return bankCode;
	}

	public void SetBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public long GetReceiverID() {
		return receiverID;
	}

	public void SetReceiverID(long receiverID) {
		this.receiverID = receiverID;
	}

	
	@Override
	public int describeContents() {
		return 0;
	}

	private void ReadFromParcel(Parcel inward) {
		
		this.id = inward.readLong();
		this.bankName = inward.readString();
		this.acountName = inward.readString();
		this.bankCode = inward.readString();
		this.accountID = inward.readString();
		this.branchName = inward.readString();		
		this.receiverID = inward.readLong();
		this.cloudReference = inward.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeLong(this.id);
		dest.writeString(this.bankName);
		dest.writeString(this.acountName);
		dest.writeString(this.bankCode);
		dest.writeString(this.accountID);
		dest.writeString(this.branchName);
		dest.writeLong(this.receiverID);
		dest.writeString(this.cloudReference);
	}


	public static final Parcelable.Creator<BankModel> CREATOR = new Parcelable.Creator<BankModel>() {

		@Override
		public BankModel createFromParcel(Parcel source) {
			return new BankModel(source);
		}

		@Override
		public BankModel[] newArray(int size) {
			return new BankModel[size];
		}

	};

}
