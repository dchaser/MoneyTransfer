package viewmodels;

import java.util.ArrayList;
import java.util.List;

import models.BankModel;
import utilities.DataContext;
import utilities.DbHelper;
import utilities.ObjectMapper;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class BankViewModel implements Parcelable {

	private String LOGTAG = "BankViewModel";

	private DataContext dataContext;
	private ObjectMapper objectMapper;

	private BankModel selectedBank;
	private List<BankModel> banks = new ArrayList<BankModel>();

	// Generic getters ad setters
	public BankModel GetSelectedBank() {
		return this.selectedBank;
	}

	public void SetSelectedBank(BankModel selectedBank) {
		this.selectedBank = selectedBank;
	}

	public List<BankModel> GetBanks() {
		return this.banks;
	}

	public void SetBanks(List<BankModel> banks) {
		this.banks = banks;
	}

	// constructors
	public BankViewModel(DataContext dataContext) {

		this.dataContext = dataContext;
		this.objectMapper = new ObjectMapper();

	}

	public BankViewModel(long receiverID, DataContext dataContext) {

		this.dataContext = dataContext;
		this.objectMapper = new ObjectMapper();

		LoadBanks(receiverID);

	}

	public void LoadBanks(long receiverID) {

		if (receiverID != -1) {

			this.banks = GetBanksByReceiverID(receiverID);
			// make the first bank as selected bank
			if (GetBanks().size() > 0) {
				this.selectedBank = GetBanks().get(0);
			}

		} else {
			// will be an empty object as there are no banks in the database
			this.selectedBank = new BankModel();
		}

		// SetContext(this.dataContext);
	}

	// end of constructor load helper

	public List<BankModel> GetBanksByReceiverID(long ReceiverID) {

		List<BankModel> banks = new ArrayList<BankModel>();

		Cursor cursor = null;
		// String Query =
		// "SELECT * FROM receiverbankdetails WHERE receiverid = " + ReceiverID;

		cursor = this.dataContext.mainDB.query(this.objectMapper.tbl_Banks,
				this.objectMapper.AllReceiverBankDetailsTableColumns,
				this.objectMapper.bank_receiverRef + "=" + ReceiverID, null,
				null, null, null);

		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				BankModel bank = new BankModel();

				bank.SetId(DbHelper.GetLong(cursor, this.objectMapper.bankID));
				bank.SetBankName(DbHelper.GetString(cursor,
						this.objectMapper.bank_name));
				bank.SetAcountName(DbHelper.GetString(cursor,
						this.objectMapper.bank_accountName));
				bank.SetBankCode(DbHelper.GetString(cursor,
						this.objectMapper.bank_code));
				bank.SetAccountID(DbHelper.GetString(cursor,
						this.objectMapper.bank_accountNo));
				bank.SetAcountName(DbHelper.GetString(cursor,
						this.objectMapper.bank_accountName));
				bank.SetBranchName(DbHelper.GetString(cursor,
						this.objectMapper.bank_branchName));

				bank.SetReceiverID(DbHelper.GetLong(cursor,
						this.objectMapper.bank_receiverRef));
				bank.SetCloudId(DbHelper.GetString(cursor,
						this.objectMapper.bank_cloudRef));

				banks.add(bank);
			}

		}

		return banks;
	}
	
	public BankModel GetBankPerReceiverID(long ReceiverID) {

		BankModel bank = new BankModel();

		Cursor cursor = null;
		// String Query =
		// "SELECT * FROM receiverbankdetails WHERE receiverid = " + ReceiverID;

		cursor = this.dataContext.mainDB.query(this.objectMapper.tbl_Banks,
				this.objectMapper.AllReceiverBankDetailsTableColumns,
				this.objectMapper.bank_receiverRef + "=" + ReceiverID, null,
				null, null, null);

		if (cursor.getCount() > 0) {

			while (cursor.moveToFirst()) {


				bank.SetId(DbHelper.GetLong(cursor, this.objectMapper.bankID));
				bank.SetBankName(DbHelper.GetString(cursor,
						this.objectMapper.bank_name));
				bank.SetAcountName(DbHelper.GetString(cursor,
						this.objectMapper.bank_accountName));
				bank.SetBankCode(DbHelper.GetString(cursor,
						this.objectMapper.bank_code));
				bank.SetAccountID(DbHelper.GetString(cursor,
						this.objectMapper.bank_accountNo));
				bank.SetBranchName(DbHelper.GetString(cursor,
						this.objectMapper.bank_branchName));

				bank.SetReceiverID(DbHelper.GetLong(cursor,
						this.objectMapper.bank_receiverRef));
				bank.SetCloudId(DbHelper.GetString(cursor,
						this.objectMapper.bank_cloudRef));

			}

		}

		return bank;
	}

	public void InsertOrUpdateSelectedBank(long receiverID) {

		if (this.selectedBank.GetId() != -1) {

			ContentValues values = new ContentValues();
			values.put(this.objectMapper.bank_name,this.selectedBank.GetBankName());
			values.put(objectMapper.bank_accountName,this.selectedBank.GetAcountName());
			values.put(objectMapper.bank_code, this.selectedBank.GetBankCode());
			values.put(objectMapper.bank_accountNo,this.selectedBank.GetAccountID());
			values.put(objectMapper.bank_branchName,this.selectedBank.GetBranchName());
			values.put(objectMapper.bank_receiverRef, receiverID);
			values.put(objectMapper.bank_cloudRef, this.selectedBank.GetCloudId());

			this.dataContext.mainDB.update(objectMapper.tbl_Banks, values,
					objectMapper.bank_receiverRef + "=" + receiverID, null);

		} else {

			ContentValues values = new ContentValues();

			values.put(this.objectMapper.bank_name,
					this.selectedBank.GetBankName());
			values.put(this.objectMapper.bank_accountName,
					this.selectedBank.GetAcountName());
			values.put(this.objectMapper.bank_code,
					this.selectedBank.GetBankCode());
			values.put(this.objectMapper.bank_accountNo,
					this.selectedBank.GetAccountID());
			values.put(this.objectMapper.bank_branchName,
					this.selectedBank.GetBranchName());
			values.put(this.objectMapper.bank_receiverRef, receiverID);
			values.put(this.objectMapper.bank_cloudRef, this.selectedBank.GetCloudId());

			long insertedBankID = this.dataContext.mainDB.insert(
					this.objectMapper.tbl_Banks, null, values);

			this.selectedBank.SetId(insertedBankID);

		}
	}

	public void SetContext(DataContext datacontext) {

		this.dataContext = datacontext;
		this.objectMapper = new ObjectMapper();
	}

	public void UpdateBankCloudID(String id) {
		// update only the cloudid column

		ContentValues cloudId = new ContentValues();
		cloudId.put(this.objectMapper.bank_cloudRef, id);
		
		this.dataContext.openDataBase();

		this.dataContext.mainDB.update(this.objectMapper.tbl_Banks, cloudId,
				this.objectMapper.bankID + "=" + this.selectedBank.GetId(), null);
	}

	public void DeleteBank(Long l) {

		this.dataContext.mainDB.delete(objectMapper.tbl_Banks,
				this.objectMapper.bank_receiverRef + "=" + l, null);
	}

	// parcelling part
	public BankViewModel(Parcel in) {
		ReadFromParcel(in);
	}

	private void ReadFromParcel(Parcel in) {
		this.selectedBank = in.readParcelable(BankModel.class.getClassLoader());
		in.readList(this.banks, BankModel.class.getClassLoader());
	}

	public static final Parcelable.Creator<BankViewModel> CREATOR = new Parcelable.Creator<BankViewModel>() {

		@Override
		public BankViewModel createFromParcel(Parcel source) {
			return new BankViewModel(source);
		}

		@Override
		public BankViewModel[] newArray(int size) {
			return new BankViewModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.selectedBank, 0);
		dest.writeList(this.banks);
	}

}
