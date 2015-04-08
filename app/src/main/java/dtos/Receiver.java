package dtos;

import java.util.List;


public class Receiver {

	@com.google.gson.annotations.SerializedName("id")
	public String Id;
	@com.google.gson.annotations.SerializedName("firstName")
	public String FullName;
	@com.google.gson.annotations.SerializedName("phoneOne")
	public String Mobile;
	@com.google.gson.annotations.SerializedName("email")
	public String Email;
	public String AddressString;
	public long localReceiverID;
	@com.google.gson.annotations.SerializedName("banks")
	public List<Bank> Banks;
	

	public Receiver(
			String fullName, 
			String  mobile, 
			String email,
			String fulladdress, 
			List<Bank> banks
			)
			{
				this.FullName = fullName;
				this.Mobile = mobile;
				this.Email = email;
				this.AddressString = fulladdress;
				this.Banks = banks;
		}
	
//	public ReceiverDTO(String firstName, String lastName, String  phoneOne, String phoneTwo, 
//			String email,AddressDTO address,List<BankDTO> banks)
//			{
//		this.FirstName = firstName;
//		this.LastName = lastName;
//		this.PhoneOne = phoneOne;
//		this.PhoneTwo = phoneTwo;
//		this.Email = email;
//		this.Address = address;
//		this.Banks = banks;
//				
//		
//		
//			}
//	@Override
//	public String toString() {
//		return GetId() + "," + GetReceiverFirstName() + ","
//				+ GetReceiverLastName() + "," + GetReceiverPhoneOne() + ","
//				+ GetReceiverPhoneTwo() + "," + GetReceiverEmail() + "\n"
//				+ "Address = " + GetReceiverAddress().toString() + "\n"
//				+ "ReceiverBankDetailsList = "
//				// + GetReceiverBankDetails().toString() + "sender ID = "
//				+ GetBank();
//
//	}
//
//	public long GetId() {
//		return id;
//	}
//
//	public void SetId(long id) {
//		this.id = id;
//	}
//
//	public String GetReceiverFirstName() {
//		return receiverFirstName;
//	}
//
//	public void SetReceiverFirstName(String receiverFirstName) {
//		this.receiverFirstName = receiverFirstName;
//	}
//
//	public String GetReceiverLastName() {
//		return receiverLastName;
//	}
//
//	public void SetReceiverLastName(String receiverLastName) {
//		this.receiverLastName = receiverLastName;
//	}
//
//	public String GetReceiverPhoneOne() {
//		return receiverPhoneOne;
//	}
//
//	public void SetReceiverPhoneOne(String receiverPhoneOne) {
//		this.receiverPhoneOne = receiverPhoneOne;
//	}
//
//	public String GetReceiverPhoneTwo() {
//		return receiverPhoneTwo;
//	}
//
//	public void SetReceiverPhoneTwo(String receiverPhoneTwo) {
//		this.receiverPhoneTwo = receiverPhoneTwo;
//	}
//
//	public String GetReceiverEmail() {
//		return receiverEmail;
//	}
//
//	public void SetReceiverEmail(String receiverEmail) {
//		this.receiverEmail = receiverEmail;
//	}
//
//	public Address GetReceiverAddress() {
//		return receiverAddress;
//	}
//
//	public void SetReceiverAddress(Address receiverAddress) {
//		this.receiverAddress = receiverAddress;
//	}
//
//	public Bank GetBank() {
//		return bank;
//	}
//
//	public void SetBank(Bank bank) {
//		this.bank = bank;
//	}

}
