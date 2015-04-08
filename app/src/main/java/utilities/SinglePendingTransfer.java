package utilities;


import android.os.Parcel;
import android.os.Parcelable;

public class SinglePendingTransfer implements Parcelable {

	private String senderName;
	private String senderPhone;
	private String senderEmail;
	private String senderAddress;

	private String receiverName;
	private String receiverPhone;
	private String receiverBank;
	private String receiverAccoutID;
	private String receiverBranchName;

	private String transactionDollarAmount;
	private String transactionFee;
	private String transactionRupeesAmount;
	private String transactionStatus;

	public SinglePendingTransfer(Parcel in) {
		ReadFromParcel(in);
	}

	private void ReadFromParcel(Parcel in) {
		this.senderName = in.readString();
		this.senderPhone = in.readString();
		this.senderEmail = in.readString();
		this.senderAddress = in.readString();
		
		this.receiverName = in.readString();
		this.receiverPhone = in.readString();
		this.receiverBank = in.readString();
		this.receiverAccoutID = in.readString();
		this.receiverBranchName = in.readString();
		
		this.transactionDollarAmount = in.readString();
		this.transactionFee = in.readString();
		this.transactionRupeesAmount = in.readString();
		this.transactionStatus = in.readString();

	}

	public SinglePendingTransfer() {

	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		dest.writeString(this.senderName);
		dest.writeString(this.senderPhone);
		dest.writeString(this.senderEmail);
		dest.writeString(this.senderAddress);
		
		dest.writeString(this.receiverName);
		dest.writeString(this.receiverPhone);
		dest.writeString(this.receiverBank);
		dest.writeString(this.receiverAccoutID);
		dest.writeString(this.receiverBranchName);
		
		dest.writeString(this.transactionDollarAmount);
		dest.writeString(this.transactionFee);
		dest.writeString(this.transactionRupeesAmount);
		dest.writeString(this.transactionStatus);
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderPhone() {
		return senderPhone;
	}

	public void setSenderPhone(String senderPhone) {
		this.senderPhone = senderPhone;
	}

	public String getSenderEmail() {
		return senderEmail;
	}

	public void setSenderEmail(String senderEmail) {
		this.senderEmail = senderEmail;
	}

	public String getSenderAddress() {
		return senderAddress;
	}

	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public String getReceiverBank() {
		return receiverBank;
	}

	public void setReceiverBank(String receiverBank) {
		this.receiverBank = receiverBank;
	}

	public String getReceiverAccoutID() {
		return receiverAccoutID;
	}

	public void setReceiverAccoutID(String receiverAccoutID) {
		this.receiverAccoutID = receiverAccoutID;
	}

	public String getReceiverBranchName() {
		return receiverBranchName;
	}

	public void setReceiverBranchName(String receiverBranchName) {
		this.receiverBranchName = receiverBranchName;
	}

	public String getTransactionDollarAmount() {
		return transactionDollarAmount;
	}

	public void setTransactionDollarAmount(String transactionDollarAmount) {
		this.transactionDollarAmount = transactionDollarAmount;
	}

	public String getTransactionFee() {
		return transactionFee;
	}

	public void setTransactionFee(String transactionFee) {
		this.transactionFee = transactionFee;
	}

	public String getTransactionRupeesAmount() {
		return transactionRupeesAmount;
	}

	public void setTransactionRupeesAmount(String transactionRupeesAmount) {
		this.transactionRupeesAmount = transactionRupeesAmount;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source) {
			return new SinglePendingTransfer(source);
		}

		@Override
		public Object[] newArray(int size) {
			return new SinglePendingTransfer[size];
		}

	};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


}
