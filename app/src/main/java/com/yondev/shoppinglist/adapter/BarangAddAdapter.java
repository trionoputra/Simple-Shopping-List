package com.yondev.shoppinglist.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yondev.shoppinglist.R;
import com.yondev.shoppinglist.entity.Barang;
import com.yondev.shoppinglist.entity.BarangAdd;
import com.yondev.shoppinglist.utils.Shared;
import com.yondev.shoppinglist.widget.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

public class BarangAddAdapter extends BaseAdapter {

    private List<BarangAdd> dtList = new ArrayList<BarangAdd>();
    private Activity context;
    private LayoutInflater inflater;
    public BarangAddAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BarangAddAdapter(Activity context, List<BarangAdd> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    private class ViewHolder {
        TextView title;
        TextView qty;
        TextView subtotal;
        ImageButton delete;
    }

    public void replace(BarangAdd data,int position) {
        dtList.set(position, data);
        notifyDataSetChanged();
    }

    public int getCount() {
        return dtList.size();
    }
    
    public List<BarangAdd> getData() {
        return dtList;
    }
  
    public void set(List<BarangAdd> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    public void remove(Barang data) {
    	dtList.remove(data);
        notifyDataSetChanged();
    }
    public void removeAll() {
    	dtList = new ArrayList<BarangAdd>();
        notifyDataSetChanged();
    }
    
    public void removeByID(long id) {
    	for (int i = 0; i < dtList.size(); i++) {
			if(dtList.get(i).getId() == id)
				dtList.remove(i);
		};
        notifyDataSetChanged();
    }

    public void addMany(List<BarangAdd>  dt) {
        for (int i = 0; i < dt.size(); i++)
        {
            dtList.add(dt.get(i));
        }

        notifyDataSetChanged();
    }

    public void add(BarangAdd data) {
    	dtList.add(data);
        notifyDataSetChanged();
    }
    
    public void insert(BarangAdd data,int index) {
    	dtList.add(index, data);
        notifyDataSetChanged();
    }
 
    public Object getItem(int position) {
        return dtList.get(position);
    }
 
    public long getItemId(int position) {
        return 0;
    }
    
 
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        ViewHolder holder;
        
        if (convertView == null) {
        	vi = inflater.inflate(R.layout.barang_add_item, null);
            holder = new ViewHolder();

            holder.title = (TextView) vi.findViewById(R.id.textView4);
            holder.qty = (TextView) vi.findViewById(R.id.textView5);
            holder.subtotal = (TextView) vi.findViewById(R.id.textView6);
            holder.delete = (ImageButton) vi.findViewById(R.id.imageButton);

            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }

        final BarangAdd data = (BarangAdd) getItem(position);

        if(data.nama.length() >= 30)
            holder.title.setText(data.nama.substring(0,29)+"...");
        else
            holder.title.setText(data.nama);

        holder.qty.setText(data.qty + " x " + Shared.decimalformat.format(data.harga) );
        holder.subtotal.setText(Shared.decimalformat.format(data.harga * data.qty));

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConfirmDialog dialog = new ConfirmDialog(context,context.getString(R.string.yakin_apus),context.getString(R.string.ok),context.getString(R.string.cancel));
                dialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
                    @Override
                    public void onOK() {
                        dtList.remove(position);
                        notifyDataSetChanged();
                        Toast.makeText(context,R.string.berhasil_dihapus,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancel() {

                    }
                });

                dialog.show();
            }
        });

        return vi;
    }

    private changeListener listener;
    public void setChangeListener(changeListener listener)
    {
        this.listener = listener;
    }

    public interface changeListener {
        public void onChange();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if(listener != null)
            listener.onChange();
    }
}