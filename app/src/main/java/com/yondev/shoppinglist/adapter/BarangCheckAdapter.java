package com.yondev.shoppinglist.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.yondev.shoppinglist.R;
import com.yondev.shoppinglist.entity.Daftar;
import com.yondev.shoppinglist.entity.DetailBarang;
import com.yondev.shoppinglist.utils.Shared;
import com.yondev.shoppinglist.widget.CekBarangDialog;

import java.util.ArrayList;
import java.util.List;

public class BarangCheckAdapter extends BaseAdapter {

    private List<DetailBarang> dtList = new ArrayList<DetailBarang>();
    private Activity context;
    private LayoutInflater inflater;
    public BarangCheckAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public BarangCheckAdapter(Activity context, List<DetailBarang> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    private class ViewHolder {
        TextView title;
        TextView qty;
        TextView subtotal;
        ImageButton action;
        CheckBox checkBox;
    }

    public void replace(DetailBarang data,int position) {
        dtList.set(position, data);
        notifyDataSetChanged();
    }

    public int getCount() {
        return dtList.size();
    }
    
    public List<DetailBarang> getData() {
        return dtList;
    }
  
    public void set(List<DetailBarang> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    public void remove(DetailBarang data) {
    	dtList.remove(data);
        notifyDataSetChanged();
    }
    public void removeAll() {
    	dtList = new ArrayList<DetailBarang>();
        notifyDataSetChanged();
    }
    
    public void removeByID(long id) {
    	for (int i = 0; i < dtList.size(); i++) {
			if(dtList.get(i).getId() == id)
				dtList.remove(i);
		};
        notifyDataSetChanged();
    }

    public void checked(int position,boolean check) {
        DetailBarang b = dtList.get(position);
        b.status = check ? 1 : 0;
        dtList.set(position,b);
        b.save();
        notifyDataSetChanged();
    }

    public void addMany(List<DetailBarang>  dt) {
        for (int i = 0; i < dt.size(); i++)
        {
            dtList.add(dt.get(i));
        }

        notifyDataSetChanged();
    }

    public void add(DetailBarang data) {
    	dtList.add(data);
        notifyDataSetChanged();
    }
    
    public void insert(DetailBarang data,int index) {
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
        	vi = inflater.inflate(R.layout.barang_check_item, null);
            holder = new ViewHolder();

            holder.title = (TextView) vi.findViewById(R.id.textView4);
            holder.qty = (TextView) vi.findViewById(R.id.textView5);
            holder.subtotal = (TextView) vi.findViewById(R.id.textView6);
            holder.action = (ImageButton) vi.findViewById(R.id.imageButton);
            holder.checkBox = (CheckBox)vi.findViewById(R.id.checkBox);

            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }

        final DetailBarang data = (DetailBarang) getItem(position);

        if(data.namabarang.length() >= 30)
            holder.title.setText(data.namabarang.substring(0,29)+"...");
        else
            holder.title.setText(data.namabarang);

        holder.qty.setText(data.jumlah + " x " + Shared.decimalformat.format(data.harga) );
        holder.subtotal.setText(Shared.decimalformat.format(data.harga * data.jumlah));
        holder.checkBox.setChecked(data.status == 1 ? true  : false);

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CekBarangDialog dialog = new CekBarangDialog(context,data);
                dialog.setCekBarangDialogListener(new CekBarangDialog.CekBarangDialogListener() {
                    @Override
                    public void onFinish(DetailBarang detailBarang) {
                        dtList.set(position,detailBarang);

                        List<DetailBarang> detail = DetailBarang.find(DetailBarang.class, "idDaftar = ?", detailBarang.iddaftar.toString());
                        int qty = 0;
                        double total = 0;
                        for(int i = 0; i < detail.size();i++)
                        {
                            qty += detail.get(i).jumlah;
                            total += ( detail.get(i).harga * detail.get(i).jumlah);
                        }

                        Daftar daftar = Daftar.findById(Daftar.class,detailBarang.iddaftar);
                        daftar.totalitem = qty;
                        daftar.totalharga = total;
                        daftar.save();

                        context.setResult(Activity.RESULT_OK);
                        notifyDataSetChanged();
                    }
                });
                dialog.show();
            }
        });

        holder.checkBox.setClickable(false);

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