package viewmodels;

import models.AddressModel;
import models.AmountModel;
import models.SenderModel;
import utilities.DataContext;
import utilities.DbHelper;
import utilities.ObjectMapper;
import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;
import dtos.SenderInfoDTO;

public class SenderViewModel implements Parcelable {

	private String LOGTAG = "SenderViewModel";

	public DataContext DataContext;
	public ObjectMapper Mapper;

	public SenderModel SelectedSender;
	public AmountModel SelectedAmount;
	// public byte[] SenderIdentity;
	public Bitmap SenderIdentity;

	public AddressViewModel AddressVM;
	public ReceiverViewModel ReceiverVM;
	public AmountViewModel AmountVM;

	public SenderViewModel() {

	}

	public SenderViewModel(DataContext dataContext) {
		// for data loading and DB access
		LoadSenderViewModel(dataContext, true);
	}

	public SenderViewModel(DataContext dataContext, boolean loadSender) {
		// only for DB access
		LoadSenderViewModel(dataContext, loadSender);
	}

	public void LoadSenderViewModel(DataContext dataContext, boolean loadSender) {

		this.DataContext = dataContext;
		this.Mapper = new ObjectMapper();

		if (loadSender) {
			LoadSender();
		}
	}

	private void LoadSender() {

		// load main sender from Sender table
		this.SelectedSender = GetSenderInfo();
		// load Receivers :- loading banks happen inside Receiver View Model
		this.ReceiverVM = new ReceiverViewModel(this.SelectedSender.GetId(),
				this.DataContext);

		this.AddressVM = new AddressViewModel(this.DataContext);

		this.AmountVM = new AmountViewModel(this.DataContext);

	}

	private SenderModel GetSenderInfo() {

		SenderModel sender = new SenderModel();
		Cursor cursor = null;
		String Query = "SELECT * FROM " + this.Mapper.tbl_Sender;
		cursor = this.DataContext.mainDB.rawQuery(Query, null);
		if (cursor != null && cursor.moveToFirst()) {

			// ORM Stuff

			// Sender id
			sender.SetId(DbHelper.GetLong(cursor, Mapper.senderID));
			// first name
			sender.SetFirstName(DbHelper.GetString(cursor,
					Mapper.sender_first_name));
			// last name
			sender.SetLastName(DbHelper.GetString(cursor,
					Mapper.sender_last_name));
			// mobile
			sender.SetMobile(DbHelper.GetString(cursor, Mapper.sender_mobile));
			// email
			sender.SetEmail(DbHelper.GetString(cursor, Mapper.sender_email));

			if (this.AddressVM == null) {

				this.AddressVM = new AddressViewModel(this.DataContext);
			}
			// address record id from Address table
			long addressID = cursor.getLong(cursor
					.getColumnIndex(this.Mapper.sender_addressRef));

			// get address object and assign
			AddressModel address = AddressVM.GetAddressByID(addressID);
			sender.SetAddress(address);

			// cloud ID
			sender.SetCloudRefCode(DbHelper.GetString(cursor,
					Mapper.sender_cloudRef));
			// verification status
			long s = DbHelper.GetLong(cursor, Mapper.sender_verificationStatus);
			// set to True or False
			if (s == 0) {
				sender.SetVerificationStatus(false);
			} else {
				if (s == 1) {
					sender.SetVerificationStatus(true);
				}
			}

			cursor.close();
		}
		return sender;
	}

	public Boolean IsSenderRegistred(DataContext dataContext) {

		Boolean status = null;

		this.DataContext = dataContext;
		this.Mapper = new ObjectMapper();

		String[] id = { this.Mapper.senderID };

		Cursor c = this.DataContext.mainDB.query(this.Mapper.tbl_Sender, id,
				null, null, null, null, null);

		if (c.getCount() > 0) {

			status = true;

		} else {

			status = false;
		}

		return status;
	}

	// ...................................................................................

	public void SetContext(DataContext dataContext) {
		// helper class to set context externally if
		this.DataContext = dataContext;
		this.Mapper = new ObjectMapper();

		if (this.ReceiverVM == null) {
			this.ReceiverVM = new ReceiverViewModel();
			this.ReceiverVM.SetContext(dataContext);
		}

		if (this.AddressVM == null) {
			this.AddressVM = new AddressViewModel(this.DataContext);
		}

		if (this.AmountVM == null) {
			this.AmountVM = new AmountViewModel(this.DataContext);
		}
	}

	public DataContext GetContext() {
		return this.DataContext;
	}

	public SenderInfoDTO LoadSenderInfoDTO(DataContext dataContext) {
		// gets local Sender ID, Sender's Cloud ID, Verified status into a
		// SenderInfoDTO

		this.DataContext = dataContext;
		this.Mapper = new ObjectMapper();

		SenderInfoDTO senderInfo = null;

		String[] neededColumns = { this.Mapper.senderID,
				this.Mapper.sender_cloudRef,
				this.Mapper.sender_verificationStatus };

		Cursor c = this.DataContext.mainDB.query(this.Mapper.tbl_Sender,
				neededColumns, null, null, null, null, null);

		if (c.getCount() > 0) {

			senderInfo = new SenderInfoDTO();
			c.moveToFirst();

			senderInfo.LocalSenderId = DbHelper.GetLong(c, Mapper.senderID);
			senderInfo.SenderId = DbHelper.GetString(c, Mapper.sender_cloudRef);
			Integer isVerified = DbHelper.GetInt(c,
					Mapper.sender_verificationStatus);
			if (isVerified == 1) {
				senderInfo.Verified = true;
			} else {
				senderInfo.Verified = false;
			}

		}

		return senderInfo;
	}

	public void UpdateSenderCloudId(String senderCloudId) {
		// update only the cloudid column of the table for the sender passed.

		ContentValues cloudId = new ContentValues();
		cloudId.put(this.Mapper.sender_cloudRef, senderCloudId);
		this.DataContext.openDataBase();
		this.DataContext.mainDB.update(this.Mapper.tbl_Sender, cloudId,
				this.Mapper.senderID + "=" + this.SelectedSender.GetId(), null);

	}

	public void UpdateSenderAddressCloudId(String addressCloudPK,
			String senderCloudPK) {
		// update only the address id column of the table for the sender passed.

		ContentValues newVals = new ContentValues();

		newVals.put(this.Mapper.sender_addressRef, addressCloudPK);
		this.DataContext.openDataBase();

		this.DataContext.mainDB.update(this.Mapper.tbl_Sender, newVals,
				this.Mapper.sender_cloudRef + "=" + senderCloudPK, null);

	}

	public void UpdateSender() {

		long insertedSenderID = 0;
		long updatedSenderID = 0;

		if (this.SelectedSender.GetId() != -1) {

			ContentValues updateValues = new ContentValues();

			updateValues.put(this.Mapper.sender_first_name,
					this.SelectedSender.GetFirstName());
			updateValues.put(this.Mapper.sender_last_name,
					this.SelectedSender.GetLastName());
			updateValues.put(this.Mapper.sender_mobile,
					this.SelectedSender.GetMobile());
			updateValues.put(this.Mapper.sender_email,
					this.SelectedSender.GetEmail());

			if (AddressVM == null) {
				AddressVM = new AddressViewModel(this.DataContext);
			}

			if (this.SelectedSender.GetAddress().GetId() != -1) {

				AddressVM.SetContext(this.DataContext);
				AddressVM.UpdateAddress(this.SelectedSender.GetAddress());

			} else {

				AddressVM.SetContext(this.DataContext);
				AddressModel newAddress = AddressVM
						.InsertAddress(this.SelectedSender.GetAddress());

				long insertedAddressID = newAddress.GetId();

				updateValues.put(this.Mapper.sender_addressRef,
						insertedAddressID);
			}

			updateValues.put(this.Mapper.sender_cloudRef,
					this.SelectedSender.GetCloudRefCode());

			Boolean updateSenderStatus = this.SelectedSender.GetVerified();

			if (updateSenderStatus == null) {
				updateValues.put(this.Mapper.sender_verificationStatus, 0);
			} else {
				if (updateSenderStatus) {
					updateValues.put(this.Mapper.sender_verificationStatus, 1);
				} else {
					updateValues.put(this.Mapper.sender_verificationStatus, 0);
				}
			}

			updatedSenderID = this.DataContext.mainDB.update(
					this.Mapper.tbl_Sender, updateValues, this.Mapper.senderID
							+ "=" + this.SelectedSender.GetId(), null);
		}

		else {

			ContentValues newSenderValues = new ContentValues();

			newSenderValues.put(this.Mapper.sender_first_name,
					this.SelectedSender.GetFirstName());
			newSenderValues.put(this.Mapper.sender_last_name,
					this.SelectedSender.GetLastName());
			newSenderValues.put(this.Mapper.sender_mobile,
					this.SelectedSender.GetMobile());
			newSenderValues.put(this.Mapper.sender_email,
					this.SelectedSender.GetEmail());

			// get reference to addresVM and insert a brand new address
			// record
				AddressVM = new AddressViewModel(this.DataContext);

			AddressModel address = AddressVM.InsertAddress(this.SelectedSender
					.GetAddress());

			long insertedAddressID = address.GetId();

			newSenderValues.put(this.Mapper.sender_addressRef,
					insertedAddressID);

			newSenderValues.put(this.Mapper.sender_cloudRef,
					this.SelectedSender.GetCloudRefCode());

			Boolean senderStatus = this.SelectedSender.GetVerified();

			if (senderStatus == null) {
				newSenderValues.put(this.Mapper.sender_verificationStatus, 0);
			} else {
				if (senderStatus) {
					newSenderValues.put(this.Mapper.sender_verificationStatus,
							1);
				} else {
					newSenderValues.put(this.Mapper.sender_verificationStatus,
							0);
				}
			}

			insertedSenderID = this.DataContext.mainDB.insert(
					this.Mapper.tbl_Sender, null, newSenderValues);
			this.SelectedSender.SetId(insertedSenderID);

		}

		ReceiverVM.SetContext(this.DataContext);
		
		if(ReceiverVM.GetSelectedReceiver().GetReceiverFullName() != null){
		ReceiverVM.UpdateReceiver(SelectedSender.GetId());
		}

	}

	public String GetSenderName(long senderID) {

		String senderName = "";

		String[] nameOnly = { this.Mapper.sender_first_name };

		Cursor c = this.DataContext.mainDB.query(this.Mapper.tbl_Sender,
				nameOnly, this.Mapper.senderID + "=" + senderID, null, null,
				null, null);

		if (c.getCount() > 0) {
			c.moveToFirst();

			senderName = DbHelper.GetString(c, this.Mapper.sender_first_name);
		}
		return senderName;
	}

	public String getSenderPhone(long senderID) {

		String getSenderPhone = "";

		String[] getSenderPhoneOnly = { this.Mapper.sender_mobile };

		Cursor c = this.DataContext.mainDB.query(this.Mapper.tbl_Sender,
				getSenderPhoneOnly, this.Mapper.senderID + "=" + senderID,
				null, null, null, null);

		if (c.getCount() > 0) {
			c.moveToFirst();

			getSenderPhone = DbHelper.GetString(c, this.Mapper.sender_mobile);
		}

		return getSenderPhone;
	}

	public void AuthorizeSenderVerification(long senderId) {

		ContentValues verifiedVals = new ContentValues();

		verifiedVals.put(this.Mapper.sender_verificationStatus, 1);

		this.DataContext.mainDB.update(this.Mapper.tbl_Sender, verifiedVals,
				this.Mapper.senderID + "=" + senderId, null);
	}

	public long ModifySender(DataContext context) {
		this.DataContext = context;
		this.Mapper = new ObjectMapper();

		if (this.SelectedSender.GetId() > -1) {
			// update

			ContentValues existingSenderValues = new ContentValues();

			existingSenderValues.put(this.Mapper.sender_first_name,
					this.SelectedSender.GetFirstName());
			existingSenderValues.put(this.Mapper.sender_last_name,
					this.SelectedSender.GetLastName());
			existingSenderValues.put(this.Mapper.sender_mobile,
					this.SelectedSender.GetMobile());
			existingSenderValues.put(this.Mapper.sender_email,
					this.SelectedSender.GetEmail());

			// get reference to addresVM and insert a brand new address record
			if (AddressVM == null) {
				AddressVM = new AddressViewModel(this.DataContext);
			}
			AddressVM.SetContext(context);
			AddressVM.UpdateAddress(this.SelectedSender.GetAddress());

			existingSenderValues.put(this.Mapper.sender_addressRef,
					this.SelectedSender.GetAddress().GetId());

			// cloudReference Code : will be updated after service callback

			// verification status : will be updated later after verification

			this.DataContext.mainDB.update(this.Mapper.tbl_Sender,
					existingSenderValues, this.Mapper.senderID + " = "
							+ this.SelectedSender.GetId(), null);
			
			return this.SelectedSender.GetId();

		} else {
			// insert
			long insertedSenderID = 0;

			ContentValues newSenderValues = new ContentValues();

			newSenderValues.put(this.Mapper.sender_first_name,
					this.SelectedSender.GetFirstName());
			newSenderValues.put(this.Mapper.sender_last_name,
					this.SelectedSender.GetLastName());
			newSenderValues.put(this.Mapper.sender_mobile,
					this.SelectedSender.GetMobile());
			newSenderValues.put(this.Mapper.sender_email,
					this.SelectedSender.GetEmail());

			// get reference to addresVM and insert a brand new address record
			if (AddressVM == null) {
				AddressVM = new AddressViewModel(this.DataContext);
			}

			AddressVM.SetContext(context);

			AddressModel address = AddressVM.InsertAddress(this.SelectedSender
					.GetAddress());

			long insertedAddressID = address.GetId();

			newSenderValues.put(this.Mapper.sender_addressRef,
					insertedAddressID);

			// cloudReference Code : will be updated after service callback

			// verification status : will be updated later after verification

			insertedSenderID = this.DataContext.mainDB.insert(
					this.Mapper.tbl_Sender, null, newSenderValues);
			this.SelectedSender.SetId(insertedSenderID);

			return insertedSenderID;
		}

	}

	public boolean CheckIfUserHasAccount(DataContext context) {

		this.DataContext = context;
		this.Mapper = new ObjectMapper();

		Boolean userHasAccount = false;

		// Cursor c = this.DataContext.mainDB.rawQuery(
		// "SELECT UserHasAccountChecked FROM sender LIMIT 1", null);
		//
		// if (c.getCount() > 0) {
		// int i = c.getInt(c
		// .getColumnIndex(this.Mapper.sender_UserHasAccountChecked));
		// userHasAccount = (i == 0) ? false : true;
		// }

		return userHasAccount;
	}

	// Parceling part
	public SenderViewModel(Parcel inward) {
		ReadFromParcel(inward);
	}

	private void ReadFromParcel(Parcel inward) {

		this.SelectedSender = inward.readParcelable(SenderModel.class
				.getClassLoader());
		this.SelectedAmount = inward.readParcelable(AmountModel.class
				.getClassLoader());

		// this.SenderIdentity = new byte[inward.readInt()];
		// if (this.SenderIdentity != null && this.SenderIdentity.length > 50) {
		// inward.readByteArray(this.SenderIdentity);
		// }
		this.SenderIdentity = (Bitmap) inward.readParcelable(getClass()
				.getClassLoader());

		this.AddressVM = inward.readParcelable(AddressViewModel.class
				.getClassLoader());
		this.ReceiverVM = inward.readParcelable(ReceiverViewModel.class
				.getClassLoader());
		this.AmountVM = inward.readParcelable(AmountViewModel.class
				.getClassLoader());

	}

	public static final Parcelable.Creator<SenderViewModel> CREATOR = new Parcelable.Creator<SenderViewModel>() {

		@Override
		public SenderViewModel createFromParcel(Parcel source) {
			return new SenderViewModel(source);
		}

		@Override
		public SenderViewModel[] newArray(int size) {
			return new SenderViewModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {

		dest.writeParcelable(this.SelectedSender, 0);
		dest.writeParcelable(this.SelectedAmount, 0);
		// if (this.SenderIdentity != null) {
		// dest.writeInt(this.SenderIdentity.length);
		// dest.writeByteArray(this.SenderIdentity);
		// }

		dest.writeParcelable(this.SenderIdentity, 0);
		dest.writeParcelable(this.AddressVM, 0);
		dest.writeParcelable(this.ReceiverVM, 0);
		dest.writeParcelable(this.AmountVM, 0);

	}

}
