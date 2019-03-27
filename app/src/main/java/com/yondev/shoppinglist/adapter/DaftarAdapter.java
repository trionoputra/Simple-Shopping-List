package com.yondev.shoppinglist.adapter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.yondev.shoppinglist.AddDaftarActivity;
import com.yondev.shoppinglist.R;
import com.yondev.shoppinglist.entity.Daftar;
import com.yondev.shoppinglist.entity.DetailBarang;
import com.yondev.shoppinglist.utils.Shared;
import com.yondev.shoppinglist.widget.ConfirmDialog;

import me.piruin.quickaction.ActionItem;
import me.piruin.quickaction.QuickAction;

public class DaftarAdapter extends BaseAdapter {
 
    private List<Daftar> dtList = new ArrayList<Daftar>();
    private Activity context;
    private LayoutInflater inflater;
    public DaftarAdapter(Activity context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    public DaftarAdapter(Activity context, List<Daftar> data) {
     
        this.context = context;
        this.dtList = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    private class ViewHolder {
        TextView title;
        TextView qty;
        TextView tanggal;
        ImageView icon;
        ImageView action;
    }
 
    public int getCount() {
        return dtList.size();
    }
    
    public List<Daftar> getData() {
        return dtList;
    }
  
    public void set(List<Daftar> list) {
    	dtList = list;
        notifyDataSetChanged();
    }
    public void remove(Daftar data) {
    	dtList.remove(data);
        notifyDataSetChanged();
    }
    public void removeAll() {
    	dtList = new ArrayList<Daftar>();
        notifyDataSetChanged();
    }
    
    public void removeByID(long id) {
    	for (int i = 0; i < dtList.size(); i++) {
			if(dtList.get(i).getId() == id)
				dtList.remove(i);
		};
        notifyDataSetChanged();
    }

    public void addMany(List<Daftar>  dt) {
        for (int i = 0; i < dt.size(); i++)
        {
            dtList.add(dt.get(i));
        }

        notifyDataSetChanged();
    }

    public void add(Daftar data) {
    	dtList.add(data);
        notifyDataSetChanged();
    }
    
    public void insert(Daftar data,int index) {
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
        	vi = inflater.inflate(R.layout.daftar_item, null);
            holder = new ViewHolder();

            holder.tanggal = (TextView) vi.findViewById(R.id.textView2);
            holder.title = (TextView) vi.findViewById(R.id.textView4);
            holder.qty = (TextView) vi.findViewById(R.id.textView5);
            holder.icon = (ImageView) vi.findViewById(R.id.imageView2);
            holder.action = (ImageButton) vi.findViewById(R.id.imageButton);

            vi.setTag(holder);
        } else {
        	 holder=(ViewHolder)vi.getTag();
        }

        final Daftar data = (Daftar) getItem(position);

        if(data.nama.length() >= 30)
            holder.title.setText(data.nama.substring(0,29)+"...");
        else
            holder.title.setText(data.nama);

        holder.tanggal.setText(Shared.dateformatAdd.format(data.tanggal));
        holder.qty.setText(context.getString(R.string.total_item) + ": " + data.totalitem + "  "+ context.getString(R.string.totalharga)+": " +  Shared.decimalformat.format(data.totalharga));

        ColorGenerator generator = ColorGenerator.MATERIAL;
        int color = generator.getColor(data.nama.substring(0,1));
        TextDrawable drawable = TextDrawable.builder().buildRound(data.nama.substring(0,1).toUpperCase(), color);
        holder.icon.setImageDrawable(drawable);

        QuickAction.setDefaultColor(ResourcesCompat.getColor(context.getResources(), R.color.coklattua, null));
        ActionItem share = new ActionItem(4, "Share");
        ActionItem copy = new ActionItem(1, "Copy");
        ActionItem edit = new ActionItem(2, "Edit");
        ActionItem delete = new ActionItem(3, "Delete");

        final QuickAction quickAction = new QuickAction(context, QuickAction.HORIZONTAL);
        quickAction.setColorRes(R.color.coklattua);
        quickAction.setTextColorRes(R.color.putih);

        quickAction.addActionItem(share);
        quickAction.addActionItem(copy);
        quickAction.addActionItem(edit);
        quickAction.addActionItem(delete);

        quickAction.setOnActionItemClickListener(new QuickAction.OnActionItemClickListener() {
            @Override public void onItemClick(ActionItem item) {
                if(item.getActionId() == 1)
                {
                    Daftar d = new Daftar();
                    d.nama = data.nama;
                    d.tanggal = new Date();
                    d.status = 0;
                    d.totalitem = data.totalitem;
                    d.totalharga = data.totalharga;
                    d.save();

                    List<DetailBarang> detail = DetailBarang.find(DetailBarang.class, "idDaftar = ?", String.valueOf(data.getId()));
                    for(int i = 0; i < detail.size();i++)
                    {
                        DetailBarang dt = new DetailBarang();
                        dt.harga = detail.get(i).harga;
                        dt.status = 0;
                        dt.jumlah = detail.get(i).jumlah;
                        dt.iddaftar = d.getId();
                        dt.namabarang = detail.get(i).namabarang;
                        dt.save();
                    }

                    dtList.add(d);
                    notifyDataSetChanged();

                }
                else if(item.getActionId() == 2)
                {
                    Intent intent = new Intent(context, AddDaftarActivity.class);
                    intent.putExtra("id",data.getId());
                    context.startActivityForResult(intent,10001);
                    context.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
                else if(item.getActionId() == 3)
                {
                    ConfirmDialog dialog = new ConfirmDialog(context,context.getString(R.string.yakin_apus),context.getString(R.string.ok),context.getString(R.string.cancel));
                    dialog.setConfirmListener(new ConfirmDialog.ConfirmListener() {
                        @Override
                        public void onOK() {
                            DetailBarang.deleteAll(DetailBarang.class, "idDaftar = ?", String.valueOf(data.getId()));
                            data.delete();
                            dtList.remove(position);
                            notifyDataSetChanged();
                            Toast.makeText(context,R.string.daftar_hapus,Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancel() {

                        }
                    });

                    dialog.show();
                }
                else if(item.getActionId() == 4)
                {
                    List<DetailBarang> detail =  DetailBarang.find(DetailBarang.class, "idDaftar = ?", String.valueOf(data.getId()));

                    String text = data.nama + " \n" + Shared.dateformatAdd.format(data.tanggal)+ "\n";
                    double total = 0;
                    for(int i = 0; i < detail.size();i++)
                    {
                        text += "\n " + detail.get(i).namabarang;
                        text += "\n " + detail.get(i).jumlah + " x " + Shared.decimalformat.format(detail.get(i).harga);
                        total += detail.get(i).jumlah * detail.get(i).harga;
                    }

                    text +="\n\n "+context.getString(R.string.totalharga)+": " + Shared.decimalformat.format(total);

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, text);
                    context.startActivity(Intent.createChooser(intent, context.getString(R.string.share_via)));
                }

            }
        });

        holder.action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quickAction.show(v);
            }
        });

        return vi;
    }

    private BarangAddAdapter.changeListener listener;
    public void setChangeListener(BarangAddAdapter.changeListener listener)
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