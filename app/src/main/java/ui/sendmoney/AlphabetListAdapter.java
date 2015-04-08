package ui.sendmoney;

import java.util.List;

import models.ReceiverModel;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.moneytransfer.R;

public class AlphabetListAdapter extends BaseAdapter {
	
	

    public static abstract class Row {}
    
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
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_item, parent, false); 
                
            }
            
            Item item = (Item) getItem(position);
            TextView textReceiverName = (TextView) view.findViewById(R.id.textReceiverName);
            textReceiverName.setTextColor(Color.parseColor("#455a64"));
            textReceiverName.setText(item.textReceiverName);
            
            
            
        } else { // Section
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = (LinearLayout) inflater.inflate(R.layout.row_section, parent, false);  
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

}