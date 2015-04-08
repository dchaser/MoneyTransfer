package ui.sendmoney;

import java.io.IOException;
import java.util.List;

import models.ReceiverModel;
import redesign.screens.RecieverNBankDetailsScreen;
import ui.navbarLinks.ReceiverDashboard;
import utilities.DataContext;
import utilities.DbHelper;
import viewmodels.ReceiverViewModel;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.moneytransfer.R;

public class AlphabetAdapter_ReceiverDashboard extends BaseAdapter implements
		OnClickListener {

	public Context activityContext;
	public long receiverNoToEditOrDelete;

	public static abstract class Row {
	}

	public static final class Section extends Row {
		public final String text;

		public Section(String text) {
			this.text = text;
		}
	}

	public static final class Item extends Row {

		public final long receiverID;
		public final String textReceiverName;
		public final String textReceiverMobileNumber;

		public Item(ReceiverModel receiver) {

			this.receiverID = receiver.GetId();
			this.textReceiverName = receiver.GetReceiverFullName();
			this.textReceiverMobileNumber = receiver.GetReceiverMobile();
		}
	}

	private List<Row> rows;

	public void setRows(List<Row> rows) {
		this.rows = rows;
	}

	public void setContext(Context dContext) {

		this.activityContext = dContext;
	}

	@Override
	public int getCount() {
		return rows.size();
	}

	@Override
	public Row getItem(int position) {
		return rows.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}

	@Override
	public int getItemViewType(int position) {
		if (getItem(position) instanceof Section) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;

		if (getItemViewType(position) == 0) { // Item
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = (LinearLayout) inflater.inflate(R.layout.row_item_nav,
						parent, false);

			}

			Item item = (Item) getItem(position);

			TextView textReceiverName = (TextView) view
					.findViewById(R.id.textReceiverName);
			textReceiverName.setTextColor(Color.parseColor("#455a64"));
			textReceiverName.setText(item.textReceiverName);

			ImageButton imgBtnEditReceiver = (ImageButton) view
					.findViewById(R.id.imgBtnEditReceiver);
			// key = 0 for edit
			imgBtnEditReceiver.setTag(R.id.imgBtnEditReceiver, item.receiverID);

			ImageButton imgBtnDeleteReceiver = (ImageButton) view
					.findViewById(R.id.imgBtnDeleteReceiver);
			// key = 1 for delete
			imgBtnDeleteReceiver.setTag(R.id.imgBtnDeleteReceiver,
					item.receiverID);

			imgBtnEditReceiver.setOnClickListener(this);
			imgBtnDeleteReceiver.setOnClickListener(this);

		} else { // Section
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) parent.getContext()
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = (LinearLayout) inflater.inflate(R.layout.row_section,
						parent, false);
				view.setOnClickListener(null);
				view.setOnLongClickListener(null);
				view.setLongClickable(false);
			}

			Section section = (Section) getItem(position);
			TextView textView = (TextView) view.findViewById(R.id.textView1);
			textView.setText(section.text);
		}

		return view;
	}

	@Override
	public void onClick(View v) {

		if (v.getId() == R.id.imgBtnDeleteReceiver) {

			try {
				DataContext dContext = new DataContext(activityContext);
				ReceiverViewModel recVM = new ReceiverViewModel(dContext);
				recVM.SetContext(dContext);

				// get Receiver ID to Delete from Button's Tag Key
				long deleteRid = (long) v.getTag(R.id.imgBtnDeleteReceiver);
				recVM.DeleteReceiver(deleteRid);
				
				

				try {
					DbHelper.writeToSD(activityContext);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				Intent i = new Intent(activityContext, ReceiverDashboard.class);
				activityContext.startActivity(i);
				
				if(activityContext instanceof Activity){
					((Activity) activityContext).finish();
				}

			} catch (Exception e) {

				Toast.makeText(activityContext, " " + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}
		}

		if (v.getId() == R.id.imgBtnEditReceiver) {

			Intent g = new Intent(activityContext,
					RecieverNBankDetailsScreen.class);

			// get Receiver ID to Edit from Button's Tag Key
			long editRid = (long) v.getTag(R.id.imgBtnEditReceiver);

			g.putExtra("receiverIDToEdit", editRid);
			g.putExtra("cameFromDashBoard", true);
			activityContext.startActivity(g);

			if (activityContext instanceof Activity) {
				((Activity) activityContext).finish();
			}

		}
	}

}
