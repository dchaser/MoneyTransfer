package viewmodels;

import utilities.DataContext;
import utilities.ObjectMapper;
import models.AmountModel;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class AmountViewModel implements Parcelable {
	public final String LogTag = "Amount View Model";

	private DataContext dataContext;
	private ObjectMapper objectMapper;

	public AmountViewModel(DataContext dataContext) {
		this.dataContext = dataContext;
		this.objectMapper = new ObjectMapper();
	}

	// passing var curr doesnt mean anything. Make it meaningfull. call it
	// amount.
	public AmountModel InsertAmount(AmountModel amountModel) {

		ContentValues values = new ContentValues();
		
		values.put(this.objectMapper.amount_senderCurrency,
				amountModel.GetSrcCode());
		values.put(this.objectMapper.amount_receiverCurrency,
				amountModel.GetDestCode());
		values.put(this.objectMapper.amount_convertionRate,
				amountModel.GetConvertRate());
		values.put(this.objectMapper.amount_sent,
				amountModel.GetAmtSend());
		values.put(this.objectMapper.amount_received,
				amountModel.GetAmtReceived());
		values.put(this.objectMapper.amount_cloudRef,
				amountModel.getCloudId());

		long newAmountId = this.dataContext.mainDB.insert(
				this.objectMapper.tbl_Amount, null, values);

		amountModel.SetId(newAmountId);

		//this.dataContext.close();
		return amountModel;
	}

	// small typo. amouID should be amountID
	public AmountModel GetAmountByID(long amounID) {
		AmountModel amount = new AmountModel();

		Cursor c = this.dataContext.mainDB.query(this.objectMapper.tbl_Amount,
				this.objectMapper.AllAmountTableColumns,
				this.objectMapper.amountID + "=" + amounID, null, null,
				null, null);

		if (c.getCount() > 0) {
			c.moveToFirst();

			amount.SetId(c.getLong(c
					.getColumnIndex(this.objectMapper.amountID)));
			amount.SetSrcCode(c.getString(c
					.getColumnIndex(this.objectMapper.amount_senderCurrency)));
			amount.SetDestCode(c.getString(c
					.getColumnIndex(this.objectMapper.amount_receiverCurrency)));
			amount.SetConvertRate(c.getDouble(c
					.getColumnIndex(this.objectMapper.amount_convertionRate)));
			amount.SetAmtSend(c.getDouble(c
					.getColumnIndex(this.objectMapper.amount_sent)));
			amount.SetAmtReceived(c.getDouble(c
					.getColumnIndex(this.objectMapper.amount_received)));
			amount.setCloudId(c.getString(c
					.getColumnIndex(this.objectMapper.amount_cloudRef)));

		}

		return amount;
	}

//	public String GetAmountWithCurrency(long amountID) {
//
//		String AmountWithCode = "";
//
//	String[] amountOnly = { this.objectMapper.tbl_Amount_SENDAMOUT,
//				this.objectMapper.tbl_Amount_SRCCURRCODE };
//		Cursor c = this.dataContext.mainDB.query(this.objectMapper.tbl_Amount,
//				amountOnly, this.objectMapper.tbl_Receiver_PK + "=" + amountID,
//				null, null, null, null);
//
//		if (c.getCount() > 0) {
//			c.moveToFirst();
//
//			AmountWithCode = c.getString(c
//					.getColumnIndex(this.objectMapper.tbl_Amount_SENDAMOUT));
//
//			AmountWithCode = AmountWithCode
//					+ ""
//					+ c.getString(c
//							.getColumnIndex(this.objectMapper.tbl_Amount_SRCCURRCODE));
//		}
//
//		return AmountWithCode;
//	}

	public void UpdateAmountCloudID(String id, long amountRecordID) {
		// update only the cloudid column

		ContentValues cloudId = new ContentValues();
		cloudId.put(this.objectMapper.amount_cloudRef, id);

		this.dataContext.mainDB.update(this.objectMapper.tbl_Amount, cloudId,
				this.objectMapper.amountID + "=" + amountRecordID, null);

	}

	// parcelling part
	// can we give the variable "in" bit more meaning full name. what is
	// actaully being passed here.
	public AmountViewModel(Parcel inward) {
		ReadFromParcel(inward);
	}

	// remove if not used. leave if this is an interface implementation.

	private void ReadFromParcel(Parcel in) {

	}

	public static final Parcelable.Creator<AmountViewModel> CREATOR = new Parcelable.Creator<AmountViewModel>() {

		@Override
		public AmountViewModel createFromParcel(Parcel source) {
			return new AmountViewModel(source);
		}

		@Override
		public AmountViewModel[] newArray(int size) {
			return new AmountViewModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel outward, int flags) {
	}

	public void SetContext(DataContext dataContext) {

		this.dataContext = dataContext;
		this.objectMapper = new ObjectMapper();

	}

}
