package com.yondev.shoppinglist.entity;

import com.orm.SugarRecord;

/**
 * Created by ThinkPad on 7/3/2017.
 */

public class DetailBarang extends SugarRecord<DetailBarang> {
    public Long iddaftar;
    public String namabarang;
    public double harga;
    public int jumlah;
    public int status;
}
