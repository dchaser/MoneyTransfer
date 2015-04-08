package dtos;


public class Address {

	@com.google.gson.annotations.SerializedName("id")
	public String Id;
	@com.google.gson.annotations.SerializedName("streetName")
	public String AddressLineOne;
	@com.google.gson.annotations.SerializedName("suburb")
	public String Suburb;
	@com.google.gson.annotations.SerializedName("postCode")
	public String PostCode;
	@com.google.gson.annotations.SerializedName("state")
	public String State;

	public Address(
			String ID,
			String AddressLineOne,
			String suburb,
			String postCode,
			String state
			)
		{
		this.Id = ID;
		this.AddressLineOne = AddressLineOne;
		this.Suburb = suburb;
		this.PostCode = postCode;
		this.State = state;
		}

}
