package com.yondev.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.yondev.shoppinglist.adapter.BarangAddAdapter;
import com.yondev.shoppinglist.adapter.DaftarAdapter;
import com.yondev.shoppinglist.entity.Daftar;
import com.yondev.shoppinglist.utils.Shared;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher,InterstitialAdListener {
    private ListView lv;
    private TextView txthint;
    private DaftarAdapter adapter;
    private boolean isFirstime = true;
    private com.facebook.ads.InterstitialAd interstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Shared.initialize(getBaseContext());

        setContentView(R.layout.activity_main);
        findViewById(R.id.btnTambah).setOnClickListener(this);

        lv = (ListView)findViewById(R.id.listview);
        txthint = (TextView)findViewById(R.id.textView2);

        List<Daftar> daftar = Daftar.findWithQuery(Daftar.class,"SELECT * FROM DAFTAR order by tanggal desc ");
        adapter = new DaftarAdapter(this,daftar);

        lv.setAdapter(adapter);

        if(daftar.size() == 0 )
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

        EditText search = (EditText)findViewById(R.id.editText);
        search.addTextChangedListener(this);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Daftar data = (Daftar) adapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, CheckListActivity.class);
                intent.putExtra("id",data.getId());
                startActivityForResult(intent,10002);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        TextView title = (TextView)findViewById(R.id.textView);
        title.setTypeface(Shared.appfontTitle);

        Button tambah = (Button)findViewById(R.id.btnTambah);
        tambah.setTypeface(Shared.appfontBold);



    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btnTambah:
                Intent intent = new Intent(MainActivity.this, AddDaftarActivity.class);
                startActivityForResult(intent,10001);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK)
        {
            if(requestCode == 10001 || requestCode == 10002)
            {
                List<Daftar> daftar = Daftar.findWithQuery(Daftar.class,"SELECT * FROM DAFTAR order by tanggal desc ");
                adapter.set(daftar);
                if(daftar.size() == 0 )
                    txthint.setVisibility(View.VISIBLE);
                else
                    txthint.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        List<Daftar> daftar = Daftar.findWithQuery(Daftar.class,"SELECT * FROM DAFTAR WHERE LOWER(nama) like '%"+s.toString().toLowerCase()+"%' order by tanggal desc ");
        adapter.set(daftar);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadInterstitialAd();
    }

    public void displayInterstitial() {
        // If Ads are loaded, show Interstitial else show nothing.
        if (interstitialAd.isAdLoaded()) {
            interstitialAd.show();
        }
    }

    private void loadInterstitialAd() {
        interstitialAd = new com.facebook.ads.InterstitialAd(this, getString(R.string.interstitial_id));
        interstitialAd.setAdListener(this);
        interstitialAd.loadAd();
    }

    @Override
    public void onError(Ad ad, AdError error) {
        // Ad failed to load
    }


    @Override
    public void onAdLoaded(Ad ad) {
        // Ad is loaded and ready to be displayed
        // You can now display the full screen add using this code:
        if(isFirstime)
        {
            displayInterstitial();
            isFirstime = false;
        }
    }

    @Override
    public void onAdClicked(Ad ad) {

    }

    @Override
    public void onLoggingImpression(Ad ad) {

    }

    @Override
    public void onInterstitialDisplayed(Ad ad) {
        loadInterstitialAd();
    }

    @Override
    public void onInterstitialDismissed(Ad ad) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        displayInterstitial();

    }
}
