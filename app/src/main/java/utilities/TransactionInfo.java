 package utilities;

public class TransactionInfo {
	
	@com.google.gson.annotations.SerializedName("senderName")	
	private String senderName;
	@com.google.gson.annotations.SerializedName("receiverName")	
	private String receiverName;
	@com.google.gson.annotations.SerializedName("transactionDate")	
	private String transactionDate;
	@com.google.gson.annotations.SerializedName("status")	
	private String status;
	@com.google.gson.annotations.SerializedName("amount")	
	private Double amount;
	@com.google.gson.annotations.SerializedName("sourceCurrency")	
	private String sourceCurrency;
	@com.google.gson.annotations.SerializedName("destinaitonCurrency")	
	private String destinaitonCurrency;
	
	
	public String getSourceCurrency() {
		return sourceCurrency;
	}
	public void setSourceCurrency(String sourceCurrency) {
		this.sourceCurrency = sourceCurrency;
	}
	public String getDestinaitonCurrency() {
		return destinaitonCurrency;
	}
	public void setDestinaitonCurrency(String destinaitonCurrency) {
		this.destinaitonCurrency = destinaitonCurrency;
	}
	public Double GetAmount() {
		return amount;
	}
	public void SetAmount(Double amount) {
		this.amount = amount;
	}
	public String GetSenderName() {
		return senderName;
	}
	public void SetSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String GetReceiverName() {
		return receiverName;
	}
	public void SetReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String GetTransactionDate() {
		return transactionDate;
	}
	public void SetTransactionDate(String transactionDate) {
		this.transactionDate = transactionDate;
	}
	public String GetStatus() {
		return status;
	}
	public void SetStatus(String status) {
		this.status = status;
	}
	
	

}
