package com.yondev.shoppinglist.widget;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.yondev.shoppinglist.R;
import com.yondev.shoppinglist.entity.Barang;
import com.yondev.shoppinglist.entity.DetailBarang;
import com.yondev.shoppinglist.utils.Shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 4/20/2017.
 */

public class CekBarangDialog extends Dialog implements  View.OnClickListener {
    private Context context;
    private CekBarangDialogListener listener;
    private EditText txtHarga;
    private TextView txtqty;
    private ImageButton btnPlus;
    private ImageButton btnMinus;
    private DetailBarang detailBarang;
    private int position = -1;

    private Button btnTambah;
    public CekBarangDialog(@NonNull Context context,DetailBarang detailBarang) {
        super(context);
        this.context = context;
        this.detailBarang = detailBarang;
    }
    public CekBarangDialog(Context context, DetailBarang detailBarang, int pos) {
        super(context);
        this.context = context;
        this.position = pos;

        this.detailBarang = detailBarang;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.view_cek_barang);

        findViewById(R.id.btnTambah).setOnClickListener(this);
        findViewById(R.id.btnKeluar).setOnClickListener(this);

        Button button = (Button)findViewById(R.id.btnTambah);
        Button button2 = (Button)findViewById(R.id.btnKeluar);
        button.setTypeface(Shared.appfontBold);
        button2.setTypeface(Shared.appfontBold);

        txtHarga = (EditText)findViewById(R.id.editText2);
        txtqty = (TextView) findViewById(R.id.textView4);
        btnPlus = (ImageButton) findViewById(R.id.imageButton1);
        btnMinus = (ImageButton) findViewById(R.id.imageButton2);

        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);

        btnMinus.setEnabled(true);

        btnTambah = (Button)findViewById(R.id.btnTambah);
        if(detailBarang != null)
        {
            txtHarga.setText(Shared.decimalformat2.format(detailBarang.harga));
            txtqty.setText(String.valueOf(detailBarang.jumlah));
            if(detailBarang.jumlah <= 1)
                btnMinus.setEnabled(false);
        }
        else
            btnMinus.setEnabled(false);

    }

    public void setCekBarangDialogListener(CekBarangDialogListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnTambah:

                if(txtHarga.getText().toString().equals(""))
                {
                    Toast.makeText(context, R.string.harga_kosong, Toast.LENGTH_SHORT).show();
                    return;
                }

                detailBarang.jumlah = Integer.valueOf(txtqty.getText().toString());
                detailBarang.harga = Double.valueOf(txtHarga.getText().toString());
                detailBarang.save();

                if(listener != null)
                    listener.onFinish(detailBarang);

                List<Barang> barang = new ArrayList<Barang>();
                try
                {
                    barang = Barang.find(Barang.class, "nama = ?", detailBarang.namabarang.toLowerCase());
                }
                catch (Exception e){}

                if(barang.size() == 0)
                {
                    Barang b = new Barang();
                    b.nama = detailBarang.namabarang;
                    b.harga = detailBarang.harga;
                    b.save();
                }
                else
                {
                    Barang b =  barang.get(0);
                    b.harga = detailBarang.harga;
                    b.save();
                }

                Toast.makeText(context,R.string.berhasil_diubah,Toast.LENGTH_SHORT).show();
                dismiss();
                break;
            case R.id.btnKeluar:
                dismiss();
                break;
            case R.id.imageButton1:
                detailBarang.jumlah += 1;
                if(detailBarang.jumlah > 1)
                    btnMinus.setEnabled(true);

                txtqty.setText(String.valueOf(detailBarang.jumlah));
                break;
            case R.id.imageButton2:

                detailBarang.jumlah -= 1;
                txtqty.setText(String.valueOf(detailBarang.jumlah));

                if(detailBarang.jumlah == 1)
                    btnMinus.setEnabled(false);

                break;
        }
    }

    public interface CekBarangDialogListener {
        public void onFinish(DetailBarang detailBarang);
    }


}
