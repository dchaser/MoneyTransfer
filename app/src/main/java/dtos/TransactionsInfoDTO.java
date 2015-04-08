package dtos;

import java.util.List;

public class TransactionsInfoDTO {
	
	@com.google.gson.annotations.SerializedName("transactionDetailsList")
	public List<TransactionDetailsDTO> TransactionDetailsList;
	
	@com.google.gson.annotations.SerializedName("checkVerification")
	public Boolean CheckVerification;

	@com.google.gson.annotations.SerializedName("senderInfoDTO")
	public SenderInfoDTO SenderInfo;

}
