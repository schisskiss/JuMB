package jumb.myapplab.pascal.jumb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class CustomListViewAdapterChat extends ArrayAdapter<RowItemChat> {

    Context context;

    public CustomListViewAdapterChat(Context context, int resourceId, List<RowItemChat> items) {
        super(context, resourceId, items);
        this.context = context;
    }
     
    /*private view holder class*/
    private class ViewHolder {
        TextView txtName;
    }
     
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItemChat rowItem = getItem(position);
         
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        	
        	holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listviewchat, null);
            holder.txtName = (TextView) convertView.findViewById(R.id.username);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
                 
        holder.txtName.setText(rowItem.getName());

         
        return convertView;
    }
}
