package com.yondev.shoppinglist.entity;

import com.orm.SugarRecord;

/**
 * Created by ThinkPad on 7/3/2017.
 */

public class Barang extends SugarRecord<Barang> {
    public String nama;
    public Double harga;

    public Barang(){
    }
    public Barang(String nama, Double harga){
        this.nama = nama;
        this.harga = harga;
    }

}
