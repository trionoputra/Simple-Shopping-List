package com.yondev.shoppinglist.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yondev.shoppinglist.R;
import com.yondev.shoppinglist.adapter.BarangAdapter;
import com.yondev.shoppinglist.entity.Barang;

import java.util.List;

/**
 * Created by ThinkPad on 4/20/2017.
 */

public class ListBarangDialog extends Dialog implements   AdapterView.OnItemClickListener {
    private Context context;
    private BarangDialogListener listener;
    private ListView lv;
    private BarangAdapter adapter;
    public ListBarangDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.view_list_barang);

        lv = (ListView) findViewById(R.id.listview);

        List<Barang> barang = Barang.listAll(Barang.class);
        adapter = new BarangAdapter(context,barang);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(this);

        TextView t7 = (TextView)findViewById(R.id.textView7);

        if(adapter.getCount() == 0 )
            t7.setVisibility(View.VISIBLE);
        else
            t7.setVisibility(View.GONE);
    }

    public void setAddBarangDialogListener(BarangDialogListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if(listener != null)
                listener.onFinish((Barang) adapter.getItem(position));

        dismiss();
    }

    public interface BarangDialogListener {
        public void onFinish(Barang data);
    }


}
