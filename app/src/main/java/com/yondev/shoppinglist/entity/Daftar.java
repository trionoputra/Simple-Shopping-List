package com.yondev.shoppinglist.entity;

import com.orm.SugarRecord;

import java.util.Date;

/**
 * Created by ThinkPad on 7/3/2017.
 */

public class Daftar  extends SugarRecord<Daftar> {
    public  String nama;
    public Date tanggal;
    public int status;
    public int totalitem;
    public double totalharga;
}
