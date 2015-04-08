package viewmodels;

import java.util.ArrayList;
import java.util.List;

import dtos.TransactionDetailsDTO;
import dtos.TransactionsInfoDTO;
import utilities.DataContext;
import utilities.ObjectMapper;
import models.TransactionModel;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("SimpleDateFormat")
public class TransactionViewModel implements Parcelable {

	public static final String LOGTAG = "TransactionVM.java";
	public DataContext dataContext;
	public ObjectMapper objectMapper;
	public TransactionModel currentTransaction;

	public TransactionViewModel(DataContext dataContext) {

		this.dataContext = dataContext;
		this.objectMapper = new ObjectMapper();

	}
	
	public void SetContext(DataContext datacontext) {

		this.dataContext = datacontext;
		this.objectMapper = new ObjectMapper();
	}

	public void UpdateCloudId(String transactionCloudId) {
		// update only the cloudid column of the table for the sender passed.

		ContentValues cloudId_trans = new ContentValues();
		long transactionId = this.currentTransaction.GetId();

		cloudId_trans.put(this.objectMapper.transaction_cloudRef,
				transactionCloudId);


		this.dataContext.mainDB.update(this.objectMapper.tbl_Transaction,
				cloudId_trans, this.objectMapper.transactionID + "="
						+ transactionId, null);
	}

	public long InsertTransaction(TransactionModel trans) {

		ContentValues values = new ContentValues();
		long newTransactionID = 0;

		values.put(this.objectMapper.transaction_senderRef,
				trans.GetSenderID());
		values.put(this.objectMapper.transaction_receiverRef,
				trans.GetReceiverID());
		values.put(this.objectMapper.transaction_amountRef,
				trans.GetAmoutID());
		values.put(this.objectMapper.transaction_transactionDate,
				trans.GetDate());
		values.put(this.objectMapper.transaction_status, trans.GetStatus());
		values.put(this.objectMapper.transaction_bankRef,
				trans.GetReceiverBankID());

		
		
			newTransactionID = this.dataContext.mainDB.insert(
					this.objectMapper.tbl_Transaction, null, values);
	
		

		return newTransactionID;
	}

	// Use the Pascal casing in method names
	public List<TransactionModel> getPendingOrCompletedTransactions(
			String status, String status2) {

		List<TransactionModel> allTransactions = new ArrayList<TransactionModel>();

		String condition = this.objectMapper.transaction_status + "="
				+ status;

		if (status2 != null) {
			condition += " OR " + this.objectMapper.transaction_status
					+ "=" + status2;
		}

		Cursor c = this.dataContext.mainDB.query(
				this.objectMapper.tbl_Transaction,
				objectMapper.AllTransactionTableColumns, condition, null, null,
				null, null);

		if (c.getCount() > 0) {
			while (c.moveToNext()) {

				TransactionModel trans = new TransactionModel();

				trans.SetId(c.getLong(c
						.getColumnIndex(this.objectMapper.transactionID)));

				trans.SetSenderID(c.getLong(c
						.getColumnIndex(this.objectMapper.transaction_senderRef)));
				trans.SetReceiverID(c.getLong(c
						.getColumnIndex(this.objectMapper.transaction_receiverRef)));
				trans.SetAmoutID(c.getLong(c
						.getColumnIndex(this.objectMapper.transaction_amountRef)));
				trans.SetDate(c.getString(c
						.getColumnIndex(this.objectMapper.transaction_transactionDate)));
				trans.SetStatus(c.getString(c
						.getColumnIndex(this.objectMapper.transaction_status)));
				trans.SetReceiverBankID(c.getLong(c
						.getColumnIndex(this.objectMapper.transaction_bankRef)));

				trans.SetCloudId(c.getString(c
						.getColumnIndex(this.objectMapper.transaction_cloudRef)));

				allTransactions.add(trans);

			}

		}
		return allTransactions;
	}

	public void UpdateTransactionStatus(long localID, String status) {

		ContentValues newTransactionValues = new ContentValues();

		newTransactionValues.put(this.objectMapper.transaction_status,
				status);

		this.dataContext.mainDB.update(this.objectMapper.tbl_Transaction,
				newTransactionValues, this.objectMapper.transactionID
						+ "=" + localID, null);

	}

	public List<TransactionDetailsDTO> GetTransactionDetailsForPendingTransactions() {

		List<TransactionDetailsDTO> transactionDetailsList = null;
		// TransactionsInfoDTO customTransactionDTO = null;
		List<TransactionModel> pendingTransactions = GetPendingAndInporcessTransactions();

		if (pendingTransactions.size() > 0) {

			// customTransactionDTO= new TransactionsInfoDTO();
			// customTransactionDTO.TransactionDetailsList = new
			// ArrayList<TransactionDetailsDTO>();
			transactionDetailsList = new ArrayList<TransactionDetailsDTO>();

			for (TransactionModel current_transaction : pendingTransactions) {
				if (current_transaction.GetCloudId() != null) {
					TransactionDetailsDTO transactionDetails = new TransactionDetailsDTO();
					transactionDetails.Id = current_transaction.GetCloudId();
					transactionDetails.Status = current_transaction.GetStatus();
					transactionDetails.ClientTransactionId = current_transaction
							.GetId();
					// customTransactionDTO.TransactionDetailsList.add(transactionDetails);

					transactionDetailsList.add(transactionDetails);
				}
			}
		}

		return transactionDetailsList;
	}

	private List<TransactionModel> GetPendingAndInporcessTransactions() {

		List<TransactionModel> allPendingTransacitions = getPendingOrCompletedTransactions(
				"'PENDING'", "'INPROCESS'");

		return allPendingTransacitions;
	}

	public void ChangeTransactionStatus(TransactionsInfoDTO customTransactionDTO) {

		if (customTransactionDTO != null
				&& customTransactionDTO.TransactionDetailsList.size() > 0) {
			for (TransactionDetailsDTO transactionDetailsDTO : customTransactionDTO.TransactionDetailsList) {
				UpdateTransactionStatus(
						transactionDetailsDTO.ClientTransactionId,
						transactionDetailsDTO.Status);
			}
		}
	}

	public TransactionModel getTransactionWithThisCloudID(String CloudId) {

		TransactionModel transModel = new TransactionModel();

		String[] neededCols = { this.objectMapper.transaction_senderRef,
				this.objectMapper.transaction_receiverRef,
				this.objectMapper.transaction_bankRef,
				this.objectMapper.transaction_amountRef,
				this.objectMapper.transaction_transactionDate,
				this.objectMapper.transaction_status,
				this.objectMapper.transaction_cloudRef };


		Cursor c = this.dataContext.mainDB.query(
				this.objectMapper.tbl_Transaction, neededCols,
				this.objectMapper.transaction_cloudRef + "=" + CloudId,
				null, null, null, null);

		if (c.getCount() > 0) {

			while (c.moveToFirst()) {

				transModel
						.SetSenderID(c.getLong(c
								.getColumnIndex(this.objectMapper.transaction_senderRef)));
				transModel
						.SetReceiverID(c.getLong(c
								.getColumnIndex(this.objectMapper.transaction_receiverRef)));
				transModel
						.SetAmoutID(c.getLong(c
								.getColumnIndex(this.objectMapper.transaction_amountRef)));
				transModel
						.SetDate(c.getString(c
								.getColumnIndex(this.objectMapper.transaction_transactionDate)));
				transModel
						.SetStatus(c.getString(c
								.getColumnIndex(this.objectMapper.transaction_status)));

				transModel
						.SetCloudId(c.getString(c
								.getColumnIndex(this.objectMapper.transaction_cloudRef)));
				transModel
						.SetReceiverBankID(c.getLong(c
								.getColumnIndex(this.objectMapper.transaction_bankRef)));

			}
		}

		return transModel;
	}

	public boolean deleteTransaction(long rowId) {
		return this.dataContext.mainDB.delete(
				this.objectMapper.tbl_Transaction,
				this.objectMapper.transactionID + "=" + rowId, null) > 0;
	}

	// Parceling methods

	public TransactionViewModel(Parcel inward) {
		ReadFromParcel(inward);
	}

	private void ReadFromParcel(Parcel inward) {

	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel outward, int flags) {

	}

	/**
	 * 
	 * This field is needed for Android to be able to create new objects,
	 * individually or as arrays.
	 * 
	 * This also means that you can use use the default constructor to create
	 * the object and use another method to hyrdate it as necessary.
	 */
	@SuppressWarnings({ "rawtypes" })
	public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
		@Override
		public TransactionViewModel createFromParcel(Parcel inward) {
			return new TransactionViewModel(inward);
		}

		@Override
		public TransactionViewModel[] newArray(int size) {
			return new TransactionViewModel[size];
		}
	};
}
