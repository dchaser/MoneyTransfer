package dtos;


public class Bank {
//
	@com.google.gson.annotations.SerializedName("id")	
	public String Id;
	@com.google.gson.annotations.SerializedName("bankName")
	public String BankName;
	@com.google.gson.annotations.SerializedName("acountName")
	public String AcountName;
	@com.google.gson.annotations.SerializedName("bankCode")
	public String BankCode;
	@com.google.gson.annotations.SerializedName("accountNo")
	public String AccountID;
	@com.google.gson.annotations.SerializedName("branchName")
	public String BranchName;

	public Bank(
			String bankName,
			String accountName, 
			String bankCode ,
			String accountNo,
			String branchName
			){
		this.BankName = bankName;
		this.AcountName= accountName;
		this.BankCode= bankCode;
		this.AccountID = accountNo;
	    this.BranchName = branchName;
	    
	}

}
