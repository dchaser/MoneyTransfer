package viewmodels;

import java.util.ArrayList;
import java.util.List;

import models.AddressModel;
import models.TransactionHistoryModel;
import utilities.DataContext;
import utilities.DbHelper;
import utilities.ObjectMapper;
import android.content.ContentValues;
import android.database.Cursor;
import dtos.Transaction;

public class TransactionHistoryViewModel {

	public DataContext DataContext;
	public ObjectMapper Mapper;

	// ...................................................................

	public TransactionHistoryModel InsertTransactionHistoryRecord(
			DataContext context, Transaction transaction) {

		this.DataContext = context;
		this.Mapper = new ObjectMapper();

		long insertedId = 0;

		String cloud_code = transaction.Id;

		// The substring(a), will return characters from ath position till the
		// end.

		String ref_code = cloud_code.substring(cloud_code.length() - 4,
				cloud_code.length());

		ContentValues values = new ContentValues();

		values.put(Mapper.transaction_history_receivername,
				transaction.Receiver.FullName);
		values.put(Mapper.transaction_history_code, ref_code);
		values.put(Mapper.transaction_history_amount,
				transaction.Amount.SentAmount);
		values.put(Mapper.transaction_history_receiverid, transaction.Receiver.localReceiverID);

		insertedId = this.DataContext.mainDB.insert(
				Mapper.tbl_Transaction_History, null, values);

		TransactionHistoryModel trHistory = new TransactionHistoryModel();
		trHistory.id = insertedId;
		trHistory.setReceivername(transaction.Receiver.FullName);
		trHistory.setCode(ref_code);
		trHistory.setAmount_dollars(transaction.Amount.SentAmount);
		trHistory.setReceiverid(transaction.Receiver.localReceiverID);

		return trHistory;

	}

	// ...................................................................

	public List<TransactionHistoryModel> GetAllTransactionsHistory(
			DataContext context) {

		this.DataContext = context;
		this.Mapper = new ObjectMapper();

		List<TransactionHistoryModel> transactionHistoryList = new ArrayList<TransactionHistoryModel>();

		Cursor cursor = null;
		String Query = "SELECT * FROM " + this.Mapper.tbl_Transaction_History;
		cursor = this.DataContext.mainDB.rawQuery(Query, null);

		if (cursor != null && cursor.moveToFirst()) {
			do {
				TransactionHistoryModel trHistory = new TransactionHistoryModel();
				trHistory.setId(DbHelper.GetLong(cursor,
						Mapper.transaction_history_id));
				trHistory.setReceivername(DbHelper.GetString(cursor,
						Mapper.transaction_history_receivername));
				trHistory.setCode(DbHelper.GetString(cursor,
						Mapper.transaction_history_code));
				trHistory.setAmount_dollars(cursor.getDouble(cursor
						.getColumnIndex(Mapper.transaction_history_amount)));
				trHistory.setReceiverid(cursor.getLong(cursor
						.getColumnIndex(Mapper.transaction_history_receiverid)));

				transactionHistoryList.add(trHistory);
			} while (cursor.moveToNext());

			cursor.close();
		}

		return transactionHistoryList;
	}

	// ...................................................................

	public void DeleteHistoryRecord(long id) {

		this.DataContext.mainDB.delete(Mapper.tbl_Transaction_History,
				this.Mapper.transaction_history_id + "=" + id, null);
	}

	// ...................................................................

	public void SetContext(DataContext dContext) {

		this.DataContext = dContext;
		this.Mapper = new ObjectMapper();
	}
	// ...................................................................
	// ...................................................................
	// ...................................................................
	// ...................................................................
	// ...................................................................
}
