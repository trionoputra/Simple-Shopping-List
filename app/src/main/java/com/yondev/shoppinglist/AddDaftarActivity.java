package com.yondev.shoppinglist;

import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.AdSize;
import com.yondev.shoppinglist.adapter.BarangAddAdapter;
import com.yondev.shoppinglist.entity.BarangAdd;
import com.yondev.shoppinglist.entity.Daftar;
import com.yondev.shoppinglist.entity.DetailBarang;
import com.yondev.shoppinglist.utils.Shared;
import com.yondev.shoppinglist.widget.AddBarangDialog;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddDaftarActivity extends AppCompatActivity implements View.OnClickListener {
    private DatePickerDialog datePickerDialog;
    private EditText txtNama;
    private EditText txtTanggal;

    private TextView txthint;
    private ListView lv;
    private BarangAddAdapter adapter;

    private Date selectedDate;

    private boolean isEditMode = false;
    private Long id = -1l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_daftar);

        txtNama = (EditText)findViewById(R.id.editText) ;
        txtTanggal = (EditText) findViewById(R.id.editText2);

        txtTanggal.setOnClickListener(this);

        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button2).setOnClickListener(this);


        Button button = (Button)findViewById(R.id.button);
        Button button2 = (Button)findViewById(R.id.button2);
        button.setTypeface(Shared.appfontBold);
        button2.setTypeface(Shared.appfontBold);

        Calendar c = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(this, dateListener, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        selectedDate  = c.getTime();

        lv = (ListView)findViewById(R.id.listview);
        adapter = new BarangAddAdapter(this);

        lv.setAdapter(adapter);

        txthint = (TextView)findViewById(R.id.textView3);

        if(adapter.getCount() == 0 )
            txthint.setVisibility(View.VISIBLE);
        else
            txthint.setVisibility(View.GONE);

        adapter.setChangeListener(new BarangAddAdapter.changeListener() {
            @Override
            public void onChange() {
                if(adapter.getCount() == 0 )
                    txthint.setVisibility(View.VISIBLE);
                else
                    txthint.setVisibility(View.GONE);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                AddBarangDialog dialog = new AddBarangDialog(AddDaftarActivity.this, (BarangAdd) adapter.getItem(position),position);
                dialog.setAddBarangDialogListener(new AddBarangDialog.AddBarangDialogListener() {
                    @Override
                    public void onFinish(BarangAdd data, int pos) {
                        if(pos == -1)
                            adapter.add(data);
                        else
                            adapter.replace(data,pos);

                        txthint.setVisibility(View.GONE);
                    }
                });

                dialog.show();
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras != null)
        {
            id = extras.getLong("id");
            Daftar d = Daftar.findById(Daftar.class,id);
            txtNama.setText(d.nama);
            txtTanggal.setText(Shared.dateformatAdd.format(d.tanggal));
            txthint.setVisibility(View.GONE);
            isEditMode  = true;

            List<DetailBarang> detail = DetailBarang.find(DetailBarang.class, "idDaftar = ?", String.valueOf(id));
            for(int i = 0; i < detail.size();i++)
            {
                BarangAdd b = new BarangAdd();
                b.nama = detail.get(i).namabarang;
                b.harga =  detail.get(i).harga;
                b.qty =  detail.get(i).jumlah;
                adapter.add(b);
            }

            selectedDate = d.tanggal;
        }

        RelativeLayout adViewContainer = (RelativeLayout) findViewById(R.id.adViewContainer);
        com.facebook.ads.AdView adView = new com.facebook.ads.AdView(this, getString(R.string.banner_ad_unit_id), AdSize.BANNER_HEIGHT_50);
        adViewContainer.addView(adView);
        adView.loadAd();

    }
    private DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            // TODO Auto-generated method stub
            Calendar calendar = Calendar.getInstance();
            calendar.set(year, monthOfYear, dayOfMonth);
            datePickerDialog.updateDate(year, monthOfYear, dayOfMonth);
            txtTanggal.setText(Shared.dateformatAdd.format(calendar.getTime()));
            selectedDate  = calendar.getTime();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.editText2:
                datePickerDialog.show();
                break;
            case R.id.button:
                AddBarangDialog dialog = new AddBarangDialog(this);
                dialog.setAddBarangDialogListener(new AddBarangDialog.AddBarangDialogListener() {
                    @Override
                    public void onFinish(BarangAdd data, int pos) {
                        if(pos == -1)
                            adapter.add(data);
                        else
                            adapter.replace(data,pos);

                        txthint.setVisibility(View.GONE);
                    }
                });
                dialog.show();
                break;
            case R.id.button2:
                save();
                break;
        }
    }

    private void save()
    {
        if(txtNama.getText().toString().equals(""))
        {
            Toast.makeText(this, R.string.nama_daftar_kosong, Toast.LENGTH_SHORT).show();
            return;
        }
        if(txtTanggal.getText().toString().equals(""))
        {
            Toast.makeText(this, R.string.tanggal_kosong, Toast.LENGTH_SHORT).show();
            return;
        }
        if(adapter.getCount() == 0)
        {
            Toast.makeText(this, R.string.barang_kosong, Toast.LENGTH_SHORT).show();
            return;
        }

        Daftar daftar = new Daftar();
        if(isEditMode)
            daftar = Daftar.findById(Daftar.class,id);

        daftar.nama = txtNama.getText().toString();
        daftar.tanggal = selectedDate;
        daftar.status = 0;
        daftar.save();

        if(isEditMode)
            DetailBarang.deleteAll(DetailBarang.class, "idDaftar = ?", String.valueOf(id));

        List<BarangAdd> barang = adapter.getData();
        int qty = 0;
        double total = 0;
        for(int i = 0; i < barang.size();i++)
        {
            BarangAdd ba = barang.get(i);
            DetailBarang d = new DetailBarang();
            d.namabarang = ba.nama;
            d.harga = ba.harga;
            d.jumlah = ba.qty;
            d.status = 0;
            d.iddaftar = daftar.getId();
            d.save();

            qty += ba.qty;
            total += ( ba.harga * ba.qty);
        }

        daftar.totalitem = qty;
        daftar.totalharga = total;
        daftar.save();

        setResult(RESULT_OK);
        Toast.makeText(this, R.string.daftar_sukses, Toast.LENGTH_SHORT).show();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
