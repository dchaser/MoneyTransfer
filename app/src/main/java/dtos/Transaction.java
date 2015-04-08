package dtos;


public class Transaction {
//
	@com.google.gson.annotations.SerializedName("id")	
	public String Id;
//    @com.google.gson.annotations.SerializedName("senderID")
//	public String SenderId;
	@com.google.gson.annotations.SerializedName("sender")
	public Sender Sender;
	@com.google.gson.annotations.SerializedName("receiver")
	public Receiver Receiver;
	@com.google.gson.annotations.SerializedName("amount")
	public Amount Amount;
	
	@com.google.gson.annotations.SerializedName("bank")
	public Bank Bank;
	//@com.google.gson.annotations.SerializedName("branchPhone")
	
	@com.google.gson.annotations.SerializedName("status")
	public String Status;
	
	//public String clientID;
	//@com.google.gson.annotations.SerializedName("acountName")
//	public String AcountName;
//	//@com.google.gson.annotations.SerializedName("swiftCode")
//	public String SwiftCode;
//	//@com.google.gson.annotations.SerializedName("bankCode")
//	public String BankCode;
//	//@com.google.gson.annotations.SerializedName("branchCode")
//	public String BranchCode;
////	@com.google.gson.annotations.SerializedName("receiverId")
////	private long receiverId;

	public Transaction(Sender sender,Receiver receiver, Amount amount,Bank bank,String status){
		this.Sender = sender;
		this.Receiver = receiver;
		this.Amount = amount;
		this.Bank = bank;
	    this.Status = status;
	  }
//	
//	@Override
//	public String toString() {
//		return GetStNo() + GetStName() + GetStName() + GetSuburb() + GetpCode() + GetState() + GetCountry();
//	}
//	
//	public AddressDTO(String country, String state, String postcode, String suburb, String streetname, String streetnumber, String id){
//		this.SetCountry(country);
//		this.SetState(state);
//		this.SetpCode(postcode);
//		this.SetSuburb(suburb);
//		this.SetStName(streetname);
//		this.SetStNo(streetnumber);
//		this.setId(id);
//	}
//	
//	public String getId() {
//		return id;
//	}
//
//	public void setId(String id) {
//		this.id = id;
//	}
//	
//	public String GetStNo() {
//		return stNo;
//	}
//
//	public void SetStNo(String stNo) {
//		this.stNo = stNo;
//	}
//
//	public String GetStName() {
//		return stName;
//	}
//
//	public void SetStName(String stName) {
//		this.stName = stName;
//	}
//
//	public String GetSuburb() {
//		return suburb;
//	}
//
//	public void SetSuburb(String suburb) {
//		this.suburb = suburb;
//	}
//
//	public String GetpCode() {
//		return pCode;
//	}
//
//	public void SetpCode(String pCode) {
//		this.pCode = pCode;
//	}
//
//	public String GetState() {
//		return state;
//	}
//
//	public void SetState(String state) {
//		this.state = state;
//	}
//
//	public String GetCountry() {
//		return country;
//	}
//
//	public void SetCountry(String country) {
//		this.country = country;
//	}
//	
//	@Override
//	public boolean equals(Object o) {
//		return o instanceof AddressDTO && ((AddressDTO) o).id == id;
//	}

}
