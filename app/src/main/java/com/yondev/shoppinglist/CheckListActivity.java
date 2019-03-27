package com.yondev.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.yondev.shoppinglist.adapter.BarangCheckAdapter;
import com.yondev.shoppinglist.entity.Daftar;
import com.yondev.shoppinglist.entity.DetailBarang;
import com.yondev.shoppinglist.utils.Shared;

import java.util.List;

public class CheckListActivity extends AppCompatActivity {
    private Daftar daftar;
    private ListView lv;
    private BarangCheckAdapter adapter;
    private TextView txtNama;
    private TextView txtTanggal;
    private TextView txttotal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        Bundle extras = getIntent().getExtras();

        lv = (ListView)findViewById(R.id.listview);
        txtNama = (TextView)findViewById(R.id.textView2);
        txtTanggal = (TextView)findViewById(R.id.textView);
        txttotal = (TextView)findViewById(R.id.textView8);

        txttotal.setTypeface(Shared.appfontBold);

        adapter = new BarangCheckAdapter(this);

        lv.setAdapter(adapter);
        if(extras != null)
        {
            daftar = Daftar.findById(Daftar.class,extras.getLong("id"));
            txtNama.setText(daftar.nama);
            txtTanggal.setText(Shared.dateformatAdd.format(daftar.tanggal));

            List<DetailBarang> detail = DetailBarang.find(DetailBarang.class, "idDaftar = ?", daftar.getId().toString());
            adapter.set(detail);
        }

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DetailBarang barang = (DetailBarang) adapter.getItem(position);
                adapter.checked(position,barang.status == 1 ? false : true);
                total();
            }
        });

        adapter.setChangeListener(new BarangCheckAdapter.changeListener() {
            @Override
            public void onChange() {
                total();
            }
        });

        total();

        findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<DetailBarang> detail = adapter.getData();

                String text = daftar.nama + " \n" + Shared.dateformatAdd.format(daftar.tanggal)+ "\n";
                double total = 0;
                for(int i = 0; i < detail.size();i++)
                {
                    text += "\n " + detail.get(i).namabarang;
                    text += "\n " + detail.get(i).jumlah + " x " + Shared.decimalformat.format(detail.get(i).harga);
                    total += detail.get(i).jumlah * detail.get(i).harga;
                }

                text +="\n\n "+getString(R.string.totalharga)+": " + Shared.decimalformat.format(total);

                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(intent, getString(R.string.share_via)));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    private void total()
    {
        List<DetailBarang> detail = adapter.getData();
        int qty = 0;
        double total = 0;
        for(int i = 0; i < detail.size();i++)
        {
            if(detail.get(i).status == 1)
            {
                qty += detail.get(i).jumlah;
                total += ( detail.get(i).harga * detail.get(i).jumlah);
            }
        }

        txttotal.setText(getString(R.string.dibeli) + ": " + qty + " " + getString(R.string.item) + " "  + getString(R.string.totalharga) + ": " + Shared.decimalformat.format(total));
    }
}
