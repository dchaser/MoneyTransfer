package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Environment;

public class DbHelper {
	
	

	public static long GetLong(Cursor c, String columnName ){
		
		 return c.getLong(c
				.getColumnIndex(columnName));
	}
	
	public static String GetString(Cursor c, String columnName ){
		
		 return c.getString(c
				.getColumnIndex(columnName));
	}
	
	public static int GetInt(Cursor c, String columnName ){
		
		 return c.getInt(c
				.getColumnIndex(columnName));
	}
	
	@SuppressWarnings("resource")
	public static void writeToSD(Context context) throws IOException {

		String DB_PATH;

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
			DB_PATH = context.getFilesDir().getAbsolutePath()
					.replace("files", "databases")
					+ File.separator;
		} else {
			DB_PATH = context.getFilesDir().getPath() + context.getPackageName()
					+ "/databases/";
		}

		File sd = Environment.getExternalStorageDirectory();

		if (sd.canWrite()) {
			String currentDBPath = "transfer.db";
			String backupDBPath = "backupname.db";
			File currentDB = new File(DB_PATH, currentDBPath);
			File backupDB = new File(sd, backupDBPath);

			if (currentDB.exists()) {
				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
			}
		}
	}
	
	
}
