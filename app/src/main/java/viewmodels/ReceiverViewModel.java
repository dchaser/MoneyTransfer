package viewmodels;

import java.util.ArrayList;
import java.util.List;

import models.ReceiverModel;
import utilities.DataContext;
import utilities.DbHelper;
import utilities.ObjectMapper;
import utilities.receiverWithNumber;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class ReceiverViewModel implements Parcelable {

	private DataContext dataContext;
	private ObjectMapper objectMapper;

	public BankViewModel bankVM;

	private ReceiverModel selectedReceiver;
	private List<ReceiverModel> receiverList;

	public ReceiverViewModel() {

	}

	public ReceiverViewModel(long senderID, DataContext datacontext) {

		this.dataContext = datacontext;
		this.objectMapper = new ObjectMapper();

		LoadReceivers(senderID);

	}

	public ReceiverViewModel(DataContext datacontext) {
		this.dataContext = datacontext;
		this.objectMapper = new ObjectMapper();
	}

	public void LoadReceivers(long senderID) {

		this.receiverList = new ArrayList<ReceiverModel>();
		if (senderID != -1) {
			// load all receivers to this sender ID
			this.receiverList = GetReceiversBySenderID(senderID);
			if (this.receiverList.size() > 0) {
				// selected receiver is always the first one of table
				ChangeSelectedReceiver(this.receiverList.get(0),
						this.dataContext);
			} else {
				this.selectedReceiver = new ReceiverModel();
				ChangeSelectedReceiver(this.selectedReceiver, this.dataContext);
			}
		} else {
			// return a Brand New Receiver for SelectedReciever
			ChangeSelectedReceiver(new ReceiverModel(), this.dataContext);
		}

	}

	public void ChangeSelectedReceiver(ReceiverModel receiver,
			DataContext dataContext) {

		this.selectedReceiver = receiver;
		this.bankVM = null;

		if (this.bankVM == null) {
			this.bankVM = new BankViewModel(receiver.GetId(), dataContext);
		}

	}

	private List<ReceiverModel> GetReceiversBySenderID(long senderID) {
		// return all receiver records with given SenderID to this.receivers

		List<ReceiverModel> allReceiversForThisSender = new ArrayList<ReceiverModel>();

		Cursor c = this.dataContext.mainDB.query(
				this.objectMapper.tbl_Receiver,
				this.objectMapper.AllReceiverTableColumns,
				this.objectMapper.receiver_senderRef + "=" + senderID, null,
				null, null, null);

		if (c.getCount() > 0) {
			while (c.moveToNext()) {

				ReceiverModel receiver = new ReceiverModel();

				// id
				receiver.SetId(c.getLong(c
						.getColumnIndex(this.objectMapper.receiverID)));
				// fname
				receiver.SetReceiverFullName(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_fullName)));
				// mobile
				receiver.SetReceiverMobile(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_mobile)));
				// email
				receiver.SetReceiverEmail(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_email)));
				// address
				receiver.SetReceiverAddress(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_address)));
				// sender id of receiver
				receiver.SetSenderId(c.getLong(c
						.getColumnIndex(this.objectMapper.receiver_senderRef)));
				// cloud id
				receiver.SetCloudId(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_cloudRef)));
				// add to collection
				allReceiversForThisSender.add(receiver);

			}
		}

		return allReceiversForThisSender;
	}

	// ....................finishing constructor helpers
	// public List<String> GetFirstNameList() {
	//
	// List<String> allNames = new ArrayList<String>();
	//
	// String[] cols = { this.objectMapper.receiver_fullName };
	//
	// Cursor c = this.dataContext.mainDB.query(
	// this.objectMapper.tbl_Receiver, cols, null, null, null, null,
	// null, null);
	//
	// if (c.getCount() > 0) {
	// while (c.moveToNext()) {
	// String name = c.getString(c
	// .getColumnIndex(this.objectMapper.receiver_fullName));
	// allNames.add(name);
	// }
	// }
	//
	// return allNames;
	// }

	public List<ReceiverModel> GetAllReceivers() {

		List<ReceiverModel> allRecs = new ArrayList<ReceiverModel>();

		Cursor c = this.dataContext.mainDB.query(
				this.objectMapper.tbl_Receiver,
				this.objectMapper.AllReceiverTableColumns, null, null, null,
				null, null);

		if (c.getCount() > 0) {
			while (c.moveToNext()) {
				ReceiverModel receiver = new ReceiverModel();

				receiver.SetId(c.getLong(c
						.getColumnIndex(this.objectMapper.receiverID)));
				receiver.SetReceiverFullName(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_fullName)));
				receiver.SetReceiverMobile(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_mobile)));
				receiver.SetReceiverEmail(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_email)));

				receiver.SetReceiverAddress(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_address)));

				receiver.SetSenderId(c.getLong(c
						.getColumnIndex(this.objectMapper.receiver_senderRef)));

				receiver.SetCloudId(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_cloudRef)));

				allRecs.add(receiver);

			}
		}

		return allRecs;
	}

	public void UpdateReceiver(long senderID) {

		long insertedReceiverID = 0;

		if (this.selectedReceiver.GetId() != -1) {

			ContentValues newReceiverValues = new ContentValues();

			newReceiverValues.put(this.objectMapper.receiver_fullName,
					this.selectedReceiver.GetReceiverFullName());

			newReceiverValues.put(this.objectMapper.receiver_mobile,
					this.selectedReceiver.GetReceiverMobile());

			newReceiverValues.put(this.objectMapper.receiver_email,
					this.selectedReceiver.GetReceiverEmail());

			newReceiverValues.put(this.objectMapper.receiver_address,
					this.selectedReceiver.GetReceiverAddress());

			newReceiverValues.put(this.objectMapper.receiver_senderRef,
					senderID);

			newReceiverValues.put(this.objectMapper.receiver_cloudRef,
					this.selectedReceiver.GetCloudId());

			this.dataContext.mainDB.update(this.objectMapper.tbl_Receiver,
					newReceiverValues, this.objectMapper.receiverID + "="
							+ this.selectedReceiver.GetId(), null);

		} else {

			ContentValues values = new ContentValues();

			values.put(this.objectMapper.receiver_fullName,
					this.selectedReceiver.GetReceiverFullName());
			values.put(this.objectMapper.receiver_mobile,
					this.selectedReceiver.GetReceiverMobile());
			values.put(this.objectMapper.receiver_email,
					this.selectedReceiver.GetReceiverEmail());

			values.put(this.objectMapper.receiver_address,
					this.selectedReceiver.GetReceiverAddress());

			values.put(this.objectMapper.receiver_senderRef, senderID);

			values.put(this.objectMapper.receiver_cloudRef,
					this.selectedReceiver.GetCloudId());

			insertedReceiverID = this.dataContext.mainDB.insert(
					this.objectMapper.tbl_Receiver, null, values);

			this.selectedReceiver.SetId(insertedReceiverID);
		}

		this.bankVM.SetContext(this.dataContext);
		this.bankVM.InsertOrUpdateSelectedBank(selectedReceiver.GetId());

	}

	public void UpdateReceiverCloudID(String id) {

		ContentValues cloudId = new ContentValues();
		cloudId.put(this.objectMapper.receiver_cloudRef, id);

		this.dataContext.mainDB.update(
				this.objectMapper.tbl_Receiver,
				cloudId,
				this.objectMapper.receiverID + "="
						+ this.selectedReceiver.GetId(), null);
	}

	public void UpdateSenderCloudId(String receiverCloudID, String senderCloudID) {

	}

	public ReceiverModel GetReceiverByID(long receiverID) {

		ReceiverModel receiver = new ReceiverModel();

		Cursor c = this.dataContext.mainDB.query(
				this.objectMapper.tbl_Receiver,
				this.objectMapper.AllReceiverTableColumns,
				this.objectMapper.receiverID + "=" + receiverID, null, null,
				null, null);

		if (c.getCount() > 0) {
			while (c.moveToNext()) {

				receiver.SetId(c.getLong(c
						.getColumnIndex(this.objectMapper.receiverID)));
				receiver.SetReceiverFullName(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_fullName)));
				receiver.SetReceiverMobile(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_mobile)));
				receiver.SetReceiverEmail(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_email)));

				receiver.SetReceiverAddress(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_address)));

				receiver.SetSenderId(c.getLong(c
						.getColumnIndex(this.objectMapper.receiver_senderRef)));

				receiver.SetCloudId(c.getString(c
						.getColumnIndex(this.objectMapper.receiver_cloudRef)));

			}
		}

		return receiver;
	}

	public String GetReceiverName(long receiverID) {

		String receiverName = "";

		String[] nameOnly = { this.objectMapper.receiver_fullName };

		Cursor c = this.dataContext.mainDB.query(
				this.objectMapper.tbl_Receiver, nameOnly,
				this.objectMapper.receiverID + "=" + receiverID, null, null,
				null, null);

		if (c.getCount() > 0) {
			c.moveToFirst();

			receiverName = c.getString(c
					.getColumnIndex(this.objectMapper.receiver_fullName));
		}

		return receiverName;

	}

	public void DeleteReceiver(Long l) {

		if (bankVM == null) {

			bankVM = new BankViewModel(this.dataContext);
		}

		// detele bank for this receiver
		bankVM.DeleteBank(l);

		this.dataContext.mainDB.delete(this.objectMapper.tbl_Receiver,
				this.objectMapper.receiverID + "=" + l, null);
	}

	// Supplementary methods
	public void SetContext(DataContext datacontext) {

		this.dataContext = datacontext;
		this.objectMapper = new ObjectMapper();

		if (this.bankVM == null) {
			this.bankVM = new BankViewModel(this.dataContext);
			this.bankVM.SetContext(datacontext);
		}
	}

	public ReceiverModel GetSelectedReceiver() {
		return this.selectedReceiver;
	}

	public ReceiverModel SetSelectedReceiver(ReceiverModel receiver) {
		this.selectedReceiver = receiver;
		// // instantiate bank VM is null
		// if (this.bankVM == null) {
		//
		// this.bankVM = new BankViewModel(this.dataContext);
		// }
		//
		// List<BankModel> banks =
		// this.bankVM.GetBanksByReceiverID(receiver.GetId());
		// this.bankVM.SetSelectedBank(banks.get(0));
		return this.selectedReceiver;

	}

	public List<ReceiverModel> GetReceivers() {
		return this.receiverList;
	}

	public List<ReceiverModel> SetReceivers(List<ReceiverModel> receivers) {
		return this.receiverList = receivers;
	}

	// Parceling part
	public ReceiverViewModel(Parcel in) {
		ReadFromParcel(in);
	}

	private void ReadFromParcel(Parcel in) {

		this.bankVM = in.readParcelable(BankViewModel.class.getClassLoader());
		this.selectedReceiver = in.readParcelable(ReceiverModel.class
				.getClassLoader());
		if (receiverList == null) {
			receiverList = new ArrayList<ReceiverModel>();
		}
		in.readList(receiverList, ReceiverModel.class.getClassLoader());
	}

	@SuppressWarnings("rawtypes")
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

		@Override
		public Object createFromParcel(Parcel source) {
			return new ReceiverViewModel(source);
		}

		@Override
		public Object[] newArray(int size) {
			return new ReceiverViewModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(bankVM, 0);
		dest.writeParcelable(selectedReceiver, 0);
		dest.writeList(receiverList);
	}

	public List<receiverWithNumber> GetReceiverAndAccountNumberList() {

		List<receiverWithNumber> allRecs = new ArrayList<receiverWithNumber>();

		Cursor c = this.dataContext.mainDB
				.rawQuery(
						"SELECT r._id, r.fname, b.accountno from receiver r INNER JOIN receiverbankdetails b ON r._id = b.receiverid",
						null);

		if (c.getCount() > 0) {
			while (c.moveToNext()) {

				long id = DbHelper.GetLong(c, this.objectMapper.receiverID);

				String fullname = DbHelper.GetString(c,
						this.objectMapper.receiver_fullName);

				String accNo = DbHelper.GetString(c,
						this.objectMapper.bank_accountNo);

				receiverWithNumber receiver = new receiverWithNumber(id,
						fullname, accNo);

				allRecs.add(receiver);

			}
		}

		return allRecs;
	}
}