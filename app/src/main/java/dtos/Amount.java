package dtos;


public class Amount {
//
	@com.google.gson.annotations.SerializedName("id")	
    public String Id;
	@com.google.gson.annotations.SerializedName("sourceCurrencyCode")
	public String SourceCurrencyCode;
	@com.google.gson.annotations.SerializedName("destinationCurrencyCode")
	public String DestinationCurrencyCode;
	@com.google.gson.annotations.SerializedName("conversionRate")
	public double ConversionRate;
	@com.google.gson.annotations.SerializedName("sentAmount")
	public double SentAmount;
	@com.google.gson.annotations.SerializedName("receivedAmount")
	public double ReceivedAmount;
	//@com.google.gson.annotations.SerializedName("accountNo")
	
//	@com.google.gson.annotations.SerializedName("receiverId")
//	private long receiverId;

	public Amount(String sourceCurrencyCode, String destinationCurrencyCode,double conversionRate,double sentAmount,double receivedAmount){
		this.SourceCurrencyCode = sourceCurrencyCode;
		this.DestinationCurrencyCode = destinationCurrencyCode;
		this.ConversionRate = conversionRate;
		this.SentAmount = sentAmount;
		this.ReceivedAmount = receivedAmount;
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
