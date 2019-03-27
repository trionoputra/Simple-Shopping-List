package com.yondev.shoppinglist.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.graphics.Typeface;
import android.view.Display;
import android.view.WindowManager;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

public final class Shared
{
	private static ContextWrapper instance;
	private static SharedPreferences pref;
	public static Typeface appfontTitle;

	public static Typeface appfont;
	public static Typeface appfontBold;


	public static SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static SimpleDateFormat dateformatAdd = new SimpleDateFormat("MM/dd/yyyy");
	public static DecimalFormat decimalformat = new DecimalFormat("#,###.#");
	public static DecimalFormat decimalformat2 = new DecimalFormat("###.#");
	public static void initialize(Context base)
	{
		instance = new ContextWrapper(base);
		pref = instance.getSharedPreferences("com.yondev.shoppinglist", Context.MODE_PRIVATE);
		appfont = Typeface.createFromAsset(instance.getAssets(),"fonts/Roboto-Regular.ttf");
		appfontBold = Typeface.createFromAsset(instance.getAssets(),"fonts/Roboto-Bold.ttf");
		appfontTitle = Typeface.createFromAsset(instance.getAssets(),"fonts/Pacifico-Regular.ttf");
	}
	
	public static void write(String key, String value)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String read(String key)
	{
		return Shared.read(key, null);
	}
	
	public static String read(String key, String defValue)
	{
		return pref.getString(key, defValue);
	}
	
	public static void clear()
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
	
	public static void clear(String key)
	{
		SharedPreferences.Editor editor = pref.edit();
		editor.remove(key);
		editor.commit();
	}
	
	public static Context getContext()
	{
		return instance.getBaseContext();
	}
	
	public static int DipToInt(int value)
	{
		return (int)(instance.getResources().getDisplayMetrics().density * value);
	}
	
	public static int getDisplayHeight()
	{
		
		 WindowManager wm = (WindowManager) instance.getSystemService(Context.WINDOW_SERVICE);
		 Display display = wm.getDefaultDisplay();
		 final int version = android.os.Build.VERSION.SDK_INT;
		 
		 if (version >= 13)
		 {
		     Point size = new Point();
		     display.getSize(size);
		    return size.y;
		 }
		 else
		 {
		     
		     return  display.getHeight();
		 }
	}
	
	public static int getDisplayWidth()
	{
		 WindowManager wm = (WindowManager) instance.getSystemService(Context.WINDOW_SERVICE);
		 Display display = wm.getDefaultDisplay();
		 final int version = android.os.Build.VERSION.SDK_INT;
		 
		 if (version >= 13)
		 {
		     Point size = new Point();
		     display.getSize(size);
		     return size.x;
		 }
		 else
		 {
		     
		     return  display.getWidth();
		 }
	}
}

