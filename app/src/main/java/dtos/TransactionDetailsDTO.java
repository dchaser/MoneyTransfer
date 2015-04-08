package dtos;


public class TransactionDetailsDTO {
	
	@com.google.gson.annotations.SerializedName("clientTransactionId")
	public long ClientTransactionId;
	
	@com.google.gson.annotations.SerializedName("id")
	public String Id;
	@com.google.gson.annotations.SerializedName("status")
	public String Status;

}
