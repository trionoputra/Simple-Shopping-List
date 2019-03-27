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
import com.yondev.shoppinglist.entity.BarangAdd;
import com.yondev.shoppinglist.utils.Shared;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ThinkPad on 4/20/2017.
 */

public class AddBarangDialog extends Dialog implements  View.OnClickListener {
    private Context context;
    private AddBarangDialogListener listener;
    private EditText txtNama;
    private EditText txtHarga;
    private TextView txtqty;
    private ImageButton btnPlus;
    private ImageButton btnMinus;
    private BarangAdd barangAdd;
    private int position = -1;

    private Button btnTambah;
    public AddBarangDialog(@NonNull Context context) {
        super(context);
        this.context = context;


        barangAdd = new BarangAdd();
        barangAdd.qty = 1;
    }
    public AddBarangDialog(Context context,BarangAdd barangAdd,int pos) {
        super(context);
        this.context = context;
        this.position = pos;

        this.barangAdd = barangAdd;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.view_add_barang);

        findViewById(R.id.btnTambah).setOnClickListener(this);
        findViewById(R.id.btnKeluar).setOnClickListener(this);
        findViewById(R.id.btnList).setOnClickListener(this);

        Button button = (Button)findViewById(R.id.btnTambah);
        Button button2 = (Button)findViewById(R.id.btnKeluar);
        button.setTypeface(Shared.appfontBold);
        button2.setTypeface(Shared.appfontBold);

        txtNama = (EditText)findViewById(R.id.editText);
        txtHarga = (EditText)findViewById(R.id.editText2);
        txtqty = (TextView) findViewById(R.id.textView4);
        btnPlus = (ImageButton) findViewById(R.id.imageButton1);
        btnMinus = (ImageButton) findViewById(R.id.imageButton2);

        btnPlus.setOnClickListener(this);
        btnMinus.setOnClickListener(this);

        btnMinus.setEnabled(false);

        btnTambah = (Button)findViewById(R.id.btnTambah);

        if(barangAdd.nama != null)
        {
            txtNama.setText(barangAdd.nama);
            txtHarga.setText(Shared.decimalformat2.format(barangAdd.harga));
            txtqty.setText(String.valueOf(barangAdd.qty));
            if(barangAdd.qty <= 1)
                btnMinus.setEnabled(false);

            btnTambah.setText(R.string.ubah);
        }
        else
            btnMinus.setEnabled(false);

    }

    public void setAddBarangDialogListener(AddBarangDialogListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnTambah:
                if(txtNama.getText().toString().equals(""))
                {
                    Toast.makeText(context, R.string.nama_kosong, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtHarga.getText().toString().equals(""))
                {
                    Toast.makeText(context, R.string.harga_kosong, Toast.LENGTH_SHORT).show();
                    return;
                }

                barangAdd.nama = txtNama.getText().toString().toLowerCase();
                barangAdd.qty = Integer.valueOf(txtqty.getText().toString());
                barangAdd.harga = Double.valueOf(txtHarga.getText().toString());

                if(listener != null)
                    listener.onFinish(barangAdd,position);

                List<Barang> barang = new ArrayList<Barang>();


                try
                {
                    barang = Barang.find(Barang.class, "nama = ?", barangAdd.nama.toLowerCase());
                }
                catch (Exception e){}


                if(barang.size() == 0)
                {
                    Barang b = new Barang();
                    b.nama = barangAdd.nama;
                    b.harga = barangAdd.harga;
                    b.save();
                }
                else
                {
                    Barang b =  barang.get(0);
                    b.harga = barangAdd.harga;
                    b.save();
                }

                if(position != -1)
                {
                    dismiss();
                    Toast.makeText(context,R.string.berhasil_diubah,Toast.LENGTH_SHORT).show();
                }
                else
                {
                    barangAdd = new BarangAdd();
                    Toast.makeText(context,R.string.berhasil_ditambah,Toast.LENGTH_SHORT).show();
                    txtqty.setText("1");
                    txtHarga.setText("");
                    txtNama.setText("");
                    btnMinus.setEnabled(false);
                    txtNama.requestFocus();

                }


                break;
            case R.id.btnKeluar:
                dismiss();
                break;
            case R.id.imageButton1:
                barangAdd.qty += 1;
                if(barangAdd.qty > 1)
                    btnMinus.setEnabled(true);

                txtqty.setText(String.valueOf(barangAdd.qty));
                break;
            case R.id.imageButton2:

                barangAdd.qty -= 1;
                txtqty.setText(String.valueOf(barangAdd.qty));

                if(barangAdd.qty == 1)
                    btnMinus.setEnabled(false);

                break;
            case R.id.btnList:
                ListBarangDialog dialog = new ListBarangDialog(context);
                dialog.setAddBarangDialogListener(new ListBarangDialog.BarangDialogListener() {
                    @Override
                    public void onFinish(Barang data) {
                        txtNama.setText(data.nama);
                        txtHarga.setText(Shared.decimalformat2.format(data.harga));
                    }
                });
                dialog.show();
                break;
        }
    }

    public interface AddBarangDialogListener {
        public void onFinish(BarangAdd data,int pos);
    }


}
