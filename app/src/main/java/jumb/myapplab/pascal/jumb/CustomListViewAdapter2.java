package jumb.myapplab.pascal.jumb;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


public class CustomListViewAdapter2 extends ArrayAdapter<RowItemSearch> {
 
    Context context;
 
    public CustomListViewAdapter2(Context context, int resourceId, List<RowItemSearch> items) {
        super(context, resourceId, items);
        this.context = context;
    }
     
    /*private view holder class*/
    private class ViewHolder {
        TextView txtVorname;
        TextView txtNachname;
        TextView txtStrasse;
        TextView txtPlz;
        TextView txtOrt;
        TextView txtFestnetz;
        TextView txtMobil;
        TextView txtEmail;
        TextView txtGeburtsdatum;
    }
     
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        RowItemSearch rowItem = getItem(position);
         
        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
        	
        	holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.listviewsearch, null);
            holder.txtVorname = (TextView) convertView.findViewById(R.id.VVorname);
            holder.txtNachname = (TextView) convertView.findViewById(R.id.VNachname);
            holder.txtStrasse = (TextView) convertView.findViewById(R.id.VStrasse);
            holder.txtPlz = (TextView) convertView.findViewById(R.id.VPlz);
            holder.txtOrt = (TextView) convertView.findViewById(R.id.VOrt);
            holder.txtFestnetz = (TextView) convertView.findViewById(R.id.VFestnetz);
            holder.txtMobil = (TextView) convertView.findViewById(R.id.VMobil);
            holder.txtEmail = (TextView) convertView.findViewById(R.id.VEmail);
            holder.txtGeburtsdatum = (TextView) convertView.findViewById(R.id.VGeburtsdatum);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
                 
        holder.txtVorname.setText(rowItem.getVorname());
        holder.txtNachname.setText(rowItem.getNachname());
        holder.txtStrasse.setText(rowItem.getStrasse());
        holder.txtPlz.setText(rowItem.getPlz());
        holder.txtOrt.setText(rowItem.getOrt());
        holder.txtFestnetz.setText(rowItem.getFestnetz());
        holder.txtMobil.setText(rowItem.getMobil());
        holder.txtEmail.setText(rowItem.getEmail());
        holder.txtGeburtsdatum.setText(rowItem.getGeburtsdatum());
         
        return convertView;
    }
}
