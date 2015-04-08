package utilities;

public class receiverWithNumber {

	public long receiverID;
	public String receiverName;
	public String accountNumber;

	public receiverWithNumber(long receiverID, String name, String accountNumber) {
		this.receiverID = receiverID;
		this.receiverName = name;
		this.accountNumber = accountNumber;
	}
	
	public receiverWithNumber(){}
}
