package dtos;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.util.Base64;

public class SenderVerification {
	
	@com.google.gson.annotations.SerializedName("id")
	public String Id;
	@com.google.gson.annotations.SerializedName("verificationImage")
	public String VerificationImage;
	
	public SenderVerification(Bitmap verificationImage){
		
		if(verificationImage != null){
			byte[] image = getByteArrayFromBitmap(verificationImage);
			this.VerificationImage =  Base64.encodeToString(image, Base64.DEFAULT);
		}
		
	}
	
	public SenderVerification(){
		this.Id = "";
		this.VerificationImage = "";
	}
	
private byte[] getByteArrayFromBitmap(Bitmap verificationImage) {
		
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			verificationImage.compress(Bitmap.CompressFormat.PNG, 40, stream);
			return stream.toByteArray();
			
	}

}
