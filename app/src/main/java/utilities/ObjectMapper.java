package utilities;


public class ObjectMapper  {


	public ObjectMapper() {
		
	}

	// SENDER TABLE
	public final String tbl_Sender = "sender";

	public final String senderID = "_id";
	public final String sender_first_name = "fname";
	public final String sender_last_name = "lname";
	public final String sender_mobile = "pone";
	public final String sender_email = "email";
	public final String sender_addressRef = "addressid";
	public final String sender_cloudRef = "cloudid";
	public final String sender_verificationStatus = "verified";
	public final String sender_UserHasAccountChecked = "userhasaccountchecked";
	

	public final String[] AllSenderTableColumns = { 
			senderID,
			sender_first_name, 
			sender_last_name,
			sender_mobile,
			sender_email, 
			sender_addressRef, 
			sender_cloudRef,
			sender_verificationStatus };
	// =====================================================================================================================
	// ADDRESS Table
	public final String tbl_Address = "address";

	public final String addressID = "_id";
	public final String address_lineOne = "stNo";
	public final String address_suburb = "suburb";
	public final String address_postCode = "pcode";
	public final String address_state = "state";
	public final String address_cloudRef = "cloudid";

	public final String[] AllAddressTableColumns = { 
			addressID,
			address_lineOne, 
			address_suburb, 
			address_postCode,
			address_state, 
			address_cloudRef };
	// =====================================================================================================================
	// AMOUNT Table
	public final String tbl_Amount = "amount";

	public final String amountID = "_id";
	public final String amount_senderCurrency = "srccurrcode";
	public final String amount_receiverCurrency = "destcurrcode";
	public final String amount_convertionRate = "conversionrate";
	public final String amount_sent = "sendamount";
	public final String amount_received = "receiveamount";
	public final String amount_cloudRef = "cloudid";

	public final String[] AllAmountTableColumns = { 
			amountID,
			amount_senderCurrency, 
			amount_receiverCurrency,
			amount_convertionRate, 
			amount_sent,
			amount_received,
			amount_cloudRef 
			};

	// =====================================================================================================================
	// RECEIVERBANKDETAILS Table
	public final String tbl_Banks = "receiverbankdetails";

	public final String bankID = "_id";
	public final String bank_name = "bankname";
	public final String bank_accountName = "accountname";
	public final String bank_code = "bankcode";
	public final String bank_accountNo = "accountno";
	public final String bank_branchName = "branchname";
	public final String bank_receiverRef = "receiverid";
	public final String bank_cloudRef = "cloudid";
	
	

	public final String[] AllReceiverBankDetailsTableColumns = { 
			bankID,
			bank_name, 
			bank_accountName, 
			bank_code,
			bank_accountNo, 
			bank_branchName, 
			bank_receiverRef,
			bank_cloudRef
		};
	// =====================================================================================================================
	// RECEIVER Table
	public final String tbl_Receiver = "receiver";

	public final String receiverID = "_id";
	public final String receiver_fullName = "fname";
	public final String receiver_mobile = "pone";
	public final String receiver_email = "email";
	public final String receiver_address = "fulladdress";
	public final String receiver_senderRef = "senderid";
	public final String receiver_cloudRef = "cloudid";

	public final String[] AllReceiverTableColumns = { 
			receiverID,
			receiver_fullName,
			receiver_mobile,
			receiver_email, 
			receiver_address, 
			receiver_senderRef,
			receiver_cloudRef
		};

	// =====================================================================================================================
	// TRANSACTION Table

	public final String tbl_Transaction = "transactions";

	public final String transactionID = "_id";
	public final String transaction_senderRef = "senderid";
	public final String transaction_receiverRef = "receiverid";
	public final String transaction_amountRef = "amountid";
	public final String transaction_transactionDate = "transactiondate";
	public final String transaction_status = "status";
	public final String transaction_cloudRef = "cloudid";
	public final String transaction_bankRef = "receiverbankid";

	// Arrays for Querying rows
	public final String[] AllTransactionTableColumns = { 
			transactionID,
			transaction_senderRef, 
			transaction_receiverRef,
			transaction_amountRef, 
			transaction_transactionDate,
			transaction_status, 
			transaction_cloudRef,
			transaction_bankRef 
			};
	
	//===========================================================================================
	//TRANSACTIONS HISTORY
	
	public final String tbl_Transaction_History = "transactionhistory";

	public final String transaction_history_id = "_id";
	public final String transaction_history_receivername = "receivername";
	public final String transaction_history_code = "code";
	public final String transaction_history_amount = "amount";
	public final String transaction_history_receiverid = "receiverid";
	

	// Arrays for Querying rows
	public final String[] AllTransactionHistoryTableColumns = { 
			transaction_history_id,
			transaction_history_receivername, 
			transaction_history_code,
			transaction_history_amount 
			};
	
	


}
