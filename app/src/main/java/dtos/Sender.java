package dtos;

import java.util.List;

public class Sender {
	@com.google.gson.annotations.SerializedName("id")
	public String Id;
	@com.google.gson.annotations.SerializedName("firstName")
	public String FirstName;
	@com.google.gson.annotations.SerializedName("lastName")
	public String LastName;
	@com.google.gson.annotations.SerializedName("phoneOne")
	public String Mobile;
	@com.google.gson.annotations.SerializedName("email")
	public String Email;
	@com.google.gson.annotations.SerializedName("receivers")
	public List<Receiver> Receivers;
	@com.google.gson.annotations.SerializedName("address")
	public Address Address;
	@com.google.gson.annotations.SerializedName("verified")
	public Boolean Verified;
	@com.google.gson.annotations.SerializedName("senderVerification")
	public SenderVerification SenderVerification;

	public Sender(String firstName, String lastName, String mobile, String email,
			List<Receiver> receivers, Address address, Boolean verifeid,
			SenderVerification verification) {
		this.FirstName = firstName;
		this.LastName = lastName;
		this.Mobile = mobile;
		this.Email = email;
		this.Receivers = receivers;
		this.Address = address;
		this.Verified = verifeid;
		this.SenderVerification = verification;

		// //VerificationImage is the Bitmap
		//
		// //calculate how many bytes our image consists of.
		// int bytes = VerificationImage.getByteCount();
		// //or we can calculate bytes this way. Use a different value than 4 if
		// you don't use 32bit images.
		// //int bytes = b.getWidth()*b.getHeight()*4;
		//
		// ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
		// VerificationImage.copyPixelsToBuffer(buffer); //Move the byte data to
		// the buffer
		//
		// this.VerificationImage = buffer.array(); //Get the underlying array
		// containing the data.

	}
	
	public Sender(String firstName, String lastName, String mobile, String email, Address address, Boolean verifeid,
			SenderVerification verification) {
		this.FirstName = firstName;
		this.LastName = lastName;
		this.Mobile = mobile;
		this.Email = email;
		this.Address = address;
		this.Verified = verifeid;
		this.SenderVerification = verification;

		// //VerificationImage is the Bitmap
		//
		// //calculate how many bytes our image consists of.
		// int bytes = VerificationImage.getByteCount();
		// //or we can calculate bytes this way. Use a different value than 4 if
		// you don't use 32bit images.
		// //int bytes = b.getWidth()*b.getHeight()*4;
		//
		// ByteBuffer buffer = ByteBuffer.allocate(bytes); //Create a new buffer
		// VerificationImage.copyPixelsToBuffer(buffer); //Move the byte data to
		// the buffer
		//
		// this.VerificationImage = buffer.array(); //Get the underlying array
		// containing the data.

	}
	
	

}
