package utilities;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

@SuppressLint("SdCardPath")
public class DataContext extends SQLiteOpenHelper {

	// private static String DB_PATH = "";


	private static String DB_PATH_WITH_NAME = "";
	
//	private static String DB_PATH = "";

	private static String DB_NAME = "transfer.db";

	public SQLiteDatabase mainDB;

	private Context myContext;

	public DataContext(Context context) {

		super(context, DB_NAME, null, 1);
//
//		if (android.os.Build.VERSION.SDK_INT >= 4.2) {
//			DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
//		} else {
//			DB_PATH = context.getFilesDir().getPath()
//					+ context.getPackageName() + "/databases/";
//		}
		
		DB_PATH_WITH_NAME = context.getDatabasePath(DB_NAME).toString();
		this.myContext = context;
		OpenExistingDatabase();

	}

	private void OpenExistingDatabase() {
		//this.getWritableDatabase();
		try {
			//this.close();
			boolean checkDB = checkDataBase();
			if (checkDB == false) {
				Log.d("DB Class", "checkDB is false");
				createDataBase();
				
				openDataBase();
				return;
			}
			openDataBase();
		} catch (IOException e) {
			throw new Error("Error copying or Opening database");
		}
	}

	/**
	 * Creates a empty database on the system and rewrites it with your own
	 * database.
	 * */
	public void createDataBase() throws IOException {

		boolean dbExist = checkDataBase();
		//boolean dbExist = false;

		if (!dbExist) {
			this.getWritableDatabase();
			try {
				this.close();
				copyDataBase();
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}

	/**
	 * Check if the database already exist to avoid re-copying the file each
	 * time you open the application.
	 * 
	 * @return true if it exists, false if it doesn't
	 */
	private boolean checkDataBase() {
		SQLiteDatabase checkDB = null;

		try {
			//String myPath = DB_PATH + DB_NAME;
			String myPath = DB_PATH_WITH_NAME;
			checkDB = SQLiteDatabase.openDatabase(myPath, null,
					SQLiteDatabase.OPEN_READWRITE);
		} catch (SQLiteException e) {
			// if the db does not exist it will throw an exception. we are using
			// this as to indicate that
			// db has not been copied to the internal data folder. No need to
			// output the error.
			Log.d("DB Class", "DB is not copied");
			 e.printStackTrace();
			 checkDB = null;
		}

		if (checkDB != null) {
			checkDB.close();
		}
		return checkDB != null ? true : false;
	}

	/**
	 * Copies your database from your local assets-folder to the just created
	 * empty database in the system folder, from where it can be accessed and
	 * handled. This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException {

		// Open your local db as the input stream
		InputStream myInput = myContext.getAssets().open(DB_NAME);

		// Path to the just created empty db
		//String outFileName = DB_PATH + DB_NAME;
		String outFileName = DB_PATH_WITH_NAME;
		// Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		// transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer)) > 0) {
			myOutput.write(buffer, 0, length);
		}

		// Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException {

		// Open the database
//		String myPath = DB_PATH + DB_NAME;
		String myPath = DB_PATH_WITH_NAME;
		mainDB = SQLiteDatabase.openDatabase(myPath, null,
				SQLiteDatabase.OPEN_READWRITE);

	}

	@Override
	public synchronized void close() {
		if (mainDB != null)
			mainDB.close();

		super.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	

}
