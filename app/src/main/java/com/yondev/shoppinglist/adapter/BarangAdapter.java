package com.yondev.shoppinglist.adapter;

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
import com.yondev.shoppinglist.utils.Shared;
import com.yondev.shoppinglist.widget.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

public class BarangAdapter extends BaseAdapter {

    private List<Barang> dtList = new ArrayList<Barang>();
    private Context context;
    private LayoutInflater inflater;
    public BarangAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BarangAdapter(Context context, List<Barang> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    private class ViewHolder {
        TextView title;
        TextView harga;
        ImageButton delete;
    }

    public void replace(Barang data,int position) {
        dtList.set(position, data);
        notifyDataSetChanged();
    }

    public int getCount() {
        return dtList.size();
    }
    
    public List<Barang> getData() {
        return dtList;
    }
  
    public void set(List<Barang> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    public void remove(Barang data) {
    	dtList.remove(data);
        notifyDataSetChanged();
    }
    public void removeAll() {
    	dtList = new ArrayList<Barang>();
        notifyDataSetChanged();
    }
    
    public void removeByID(long id) {
    	for (int i = 0; i < dtList.size(); i++) {
			if(dtList.get(i).getId() == id)
				dtList.remove(i);
		};
        notifyDataSetChanged();
    }

    public void addMany(List<Barang>  dt) {
        for (int i = 0; i < dt.size(); i++)
        {
            dtList.add(dt.get(i));
        }

        notifyDataSetChanged();
    }

    public void add(Barang data) {
    	dtList.add(data);
        notifyDataSetChanged();
    }
    
    public void insert(Barang data,int index) {
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
        	vi = inflater.inflate(R.layout.barang_list_item, null);
            holder = new ViewHolder();

            holder.title = (TextView) vi.findViewById(R.id.textView4);
            holder.harga = (TextView) vi.findViewById(R.id.textView5);
            holder.delete = (ImageButton) vi.findViewById(R.id.imageButton);

            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }

        final Barang data = (Barang) getItem(position);

        if(data.nama.length() >= 30)
            holder.title.setText(data.nama.substring(0,29)+"...");
        else
            holder.title.setText(data.nama);

        holder.harga.setText(Shared.decimalformat.format(data.harga) );

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
                        data.delete();
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