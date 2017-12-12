package com.example.tatastrive.remindmetodo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tata Strive on 10/23/2017.
 */
public class Rdatabase extends SQLiteOpenHelper
{
    Context c1;
    public Rdatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        c1=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table if not exists Alarms_List (_id integer primary key autoincrement ,Title text,Description text,Time text,Date text,Roption text,Datetime text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public long addReminder(String title,String dscrptn,String time,String date,String roption)
    {
        StringBuilder sb=new StringBuilder(date);
        sb.reverse();
        String adddatetime=sb.toString()+" "+time+":02";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date strDate = null;
            try {
                strDate = sdf.parse(adddatetime);
            } catch (ParseException e) {
                e.printStackTrace();
            }

        ContentValues empValues=new ContentValues();
        empValues.put("Title",title);
        empValues.put("Description",dscrptn);
        empValues.put("Time",time);
        empValues.put("Date",date);
        empValues.put("Roption", roption);
        empValues.put("Datetime",strDate.toString());
        SQLiteDatabase db=getWritableDatabase();
        System.out.println("@@@@@@@@ inserted:**********");
       return (db.insert("Alarms_List",null,empValues));
    }
    public Cursor showReminders()
    {
        SQLiteDatabase db=getReadableDatabase();
       Cursor c= db.query("Alarms_List", null, null, null, null, null, "datetime(Datetime) desc");
        c.moveToFirst();
        return c;
    }
    public void clearall()
    {
        SQLiteDatabase db=getWritableDatabase();
        db.delete("Alarms_List", null, null);
    }

    public Cursor read_by_id(long id)
    {
        SQLiteDatabase db=getReadableDatabase();
        String []id2={id+""};
       // String []column={"Roption"};
        Cursor c =db.query("Alarms_List", null, "_id=?", id2, null, null, null);
        c.moveToFirst();
        return c;
    }
    public long delete_by_id(long id)
    {
        SQLiteDatabase db=getWritableDatabase();
        String []id1={id+""};
        long r_id=db.delete("Alarms_List", "_id=?", id1);
        System.out.println("@@@@@@@@ deleted:" + r_id + "**********");
        return  r_id;
    }

    public int update_by_id(int id,String utitle,String udecrption,String utime,String udate,String urepeat)
    {
        StringBuilder sb=new StringBuilder(udate);
        sb.reverse();
        String adddatetime=sb.toString()+" "+utime+":02";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date strDate = null;
        try {
            strDate = sdf.parse(adddatetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SQLiteDatabase db=getWritableDatabase();
        ContentValues emptyrow=new ContentValues();
        emptyrow.put("Title",utitle);
        emptyrow.put("Description",udecrption);
        emptyrow.put("Time",utime);
        emptyrow.put("Date",udate);
        emptyrow.put("Roption",urepeat);
        emptyrow.put("Datetime",strDate.toString());
        String []u_id={""+id};
       int r_id= db.update("Alarms_List",emptyrow,"_id=?",u_id);
        System.out.println("@@@@@@@@ update:" + r_id + "**********");
       return r_id;
    }

}
