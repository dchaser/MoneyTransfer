package viewmodels;

import utilities.DataContext;
import utilities.ObjectMapper;
import models.AddressModel;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

public class AddressViewModel implements Parcelable {


	// These can be private if not accessed outside the class.
	// hence can start from a lower case letter. should follow this in all
	// classes

	private DataContext dataContext;
	private ObjectMapper objectMapper;

	public AddressViewModel(DataContext dataContext) {
		
		this.dataContext = dataContext;
		this.objectMapper = new ObjectMapper();
	}

	// the variable AddressID change it to addressID.
	// the scope of this variable is only within this method. not a public
	// variable

	public AddressModel GetAddressByID(long addressID) {
		// Get a record from Address table where Primary Key = parameter
		
		AddressModel address = new AddressModel();
		
		Cursor c = this.dataContext.mainDB.query(this.objectMapper.tbl_Address,
				this.objectMapper.AllAddressTableColumns,
				this.objectMapper.addressID + "=" + addressID, null, null,
				null, null);
		
		if (c.getCount() > 0) {
			c.moveToFirst();

			address.SetId(c.getLong(c
					.getColumnIndex(this.objectMapper.addressID)));
			address.SetStNo(c.getString(c
					.getColumnIndex(this.objectMapper.address_lineOne)));
			address.SetSuburb(c.getString(c
					.getColumnIndex(this.objectMapper.address_suburb)));
			address.SetPostCode(c.getString(c
					.getColumnIndex(this.objectMapper.address_postCode)));
			address.SetState(c.getString(c
					.getColumnIndex(this.objectMapper.address_state)));
			address.SetCloudId(c.getString(c
					.getColumnIndex(this.objectMapper.address_cloudRef)));
		}
		
		return address;
	}

	public AddressModel InsertAddress(AddressModel address) {
		//inserts brand new address
		
		ContentValues values = new ContentValues();

		values.put(this.objectMapper.address_lineOne, address.GetStNo());
		values.put(this.objectMapper.address_suburb, address.GetSuburb());
		values.put(this.objectMapper.address_postCode, address.GetPostCode());
		values.put(this.objectMapper.address_state, address.GetState());
		values.put(this.objectMapper.address_cloudRef, address.GetCloudId());

		
		long returned = this.dataContext.mainDB.insert(
				this.objectMapper.tbl_Address, null, values);

		address.SetId(returned);
		
		return address;

	}

	public void SetContext(DataContext datacontext) {

		this.dataContext = datacontext;
		this.objectMapper = new ObjectMapper();
	}
	

	public void UpdateAddress(AddressModel address) {
		// to use this method, parameter MUST never have ID = -1;
		
		ContentValues addressValues = new ContentValues();

		addressValues
				.put(this.objectMapper.address_lineOne, address.GetStNo());
		addressValues.put(this.objectMapper.address_suburb,
				address.GetSuburb());
		addressValues.put(this.objectMapper.address_postCode,
				address.GetPostCode());
		addressValues.put(this.objectMapper.address_state,
				address.GetState());
		addressValues.put(this.objectMapper.address_cloudRef,
				address.GetCloudId());
		
		
		long addID = address.GetId();
		
		this.dataContext.mainDB.update(this.objectMapper.tbl_Address, addressValues, this.objectMapper.addressID +" = "+addID, null);
		

	}

	// Parceling part : 
	public AddressViewModel(Parcel inward) {
		ReadFromParcel(inward);
	}

	// remove unused methods.
	private void ReadFromParcel(Parcel inward) {
		
	}

	public static final Parcelable.Creator<AddressViewModel> CREATOR = new Parcelable.Creator<AddressViewModel>() {

		@Override
		public AddressViewModel createFromParcel(Parcel source) {
			return new AddressViewModel(source);
		}

		@Override
		public AddressViewModel[] newArray(int size) {
			return new AddressViewModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel outward, int flags) {

	}

	public void UpdateAddressCloudID(String id, AddressModel getAddress) {

		ContentValues cloudId = new ContentValues();
		cloudId.put(this.objectMapper.address_cloudRef, id);
		
		this.dataContext.openDataBase();

		this.dataContext.mainDB.update(this.objectMapper.tbl_Address, cloudId,
				this.objectMapper.addressID + "=" + getAddress.GetId(), null);
	}

}
