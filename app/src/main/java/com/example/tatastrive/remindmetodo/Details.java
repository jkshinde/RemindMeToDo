package com.example.tatastrive.remindmetodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Details extends AppCompatActivity {
Rdatabase rdatabase;
    TextView dtitle,ddscrptn,droption,ddate,dtime;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        dtitle=(TextView)findViewById(R.id.DetailsTvtitle1);
        ddscrptn=(TextView)findViewById(R.id.DetailsTvdscrption1);
        droption=(TextView)findViewById(R.id.DetailsTvroption1);
        ddate=(TextView)findViewById(R.id.DetailsTvDate1);
        dtime=(TextView)findViewById(R.id.DetailsTvTime1);

        Intent getid=getIntent();
        int id=getid.getIntExtra("id", -1);
        rdatabase=new Rdatabase(this,"Reminders",null,01);
       Cursor cr= rdatabase.read_by_id(id);

        dtitle.setText(cr.getString(cr.getColumnIndex("Title")));
        ddscrptn.setText(cr.getString(cr.getColumnIndex("Description")));
        droption.setText(cr.getString(cr.getColumnIndex("Roption")));
        ddate.setText(cr.getString(cr.getColumnIndex("Date")));
        dtime.setText(cr.getString(cr.getColumnIndex("Time")));

    }
}
