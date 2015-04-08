package viewmodels;

import utilities.DataContext;
import utilities.ObjectMapper;

public class ResetViewModel {

	private DataContext dataContext;
	private ObjectMapper objectMapper;

	public ResetViewModel(DataContext dataContext) {
		this.dataContext = dataContext;
		this.objectMapper = new ObjectMapper();
	}
	
	public void DeleteAllRecords(){
		
		this.dataContext.mainDB.execSQL("DELETE FROM "+this.objectMapper.tbl_Address+";");
		this.dataContext.mainDB.execSQL("DELETE FROM "+this.objectMapper.tbl_Amount+";");
		this.dataContext.mainDB.execSQL("DELETE FROM "+this.objectMapper.tbl_Receiver+";");
		this.dataContext.mainDB.execSQL("DELETE FROM "+this.objectMapper.tbl_Banks+";");
		this.dataContext.mainDB.execSQL("DELETE FROM "+this.objectMapper.tbl_Sender+";");
		this.dataContext.mainDB.execSQL("DELETE FROM "+this.objectMapper.tbl_Transaction+";");
		this.dataContext.mainDB.execSQL("DELETE FROM "+this.objectMapper.tbl_Transaction_History+";");
		this.dataContext.mainDB.execSQL("VACUUM;");
	}
}
