package com.example.tatastrive.remindmetodo;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class ReminderList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{


    AlarmManager am;
    Cursor c1;
    Rdatabase reminder;
    ListView rlist;
    Button addrl,RAddDate,RAddTime,RSave,RCancel,ERAddDate,ERAddTime,ERupdate,ERCancel;
    EditText Rtitle,Rdecrptn,ERtitle,ERdecrptn;
    TextView Rdate,Rtime/*ERdate,ERtime*/;
    Spinner rpeat,Erpeat;
    String stitle,sdecrptn,sdate,settime,srpt="Once",key,utitle,udecrptn,udate,uettime,urpt;
    AlertDialog alert;
    Calendar c;
    Context globalcontext;
    int Syear,Smonth,Sday,Shour,Sminutes,id,c_id1;
    MyCustomadapter customadapter;
    public ReminderList(Context c2)
    {
        globalcontext=c2;
        reminder=new Rdatabase(c2,"Reminders",null,01);
    }
    public  ReminderList(){}



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder_list);
        rlist=(ListView)findViewById(R.id.Rlistview);
        addrl=(Button)findViewById(R.id.btn_addR2);
        addrl.setOnClickListener(this);
        registerForContextMenu(rlist);
        Intent intent=getIntent();
        key=intent.getStringExtra("key1");

        rlist.setOnItemClickListener(this);

        c=Calendar.getInstance();
        reminder=new Rdatabase(this,"Reminders",null,01);
        if(key.equals("Launcher_Page"))
        {
            dailogbox();
        }
        else if(key.equals("broadcast"))
        {
           AlertDialog.Builder abuilder= new AlertDialog.Builder(this);
            abuilder.setIcon(R.drawable.pics);
            abuilder.setTitle("Reminder");
            abuilder.setMessage("IF YOUR TASK IS COMPLETED THEN YOU CAN REMOVE IT FROM LIST");
            AlertDialog dialog=abuilder.create();
            dialog.show();
        }
        //checkalrms();
        refresh(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void refresh(Context context)
    {
        reminder=new Rdatabase(context,"Reminders",null,01);
        c1=reminder.showReminders();
        customadapter=new MyCustomadapter(context,c1);
        rlist.setAdapter(customadapter);
        //reminder.clearall();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        menu.add(1,1,1,"Cancel");
        menu.add(1,2,2,"Edit");
        menu.add(1,3,3,"Send");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        c_id1= c1.getInt(c1.getColumnIndex("_id"));
        switch (item.getItemId())
        {
            case 1:
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent rintents=new Intent(this,Broadcastrcr.class);
                PendingIntent pi=PendingIntent.getBroadcast(this, c_id1,rintents,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pi);
                reminder.delete_by_id(c_id1);
                 refresh(this);
                Toast.makeText(this,"Reminder Cancelled",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                key="edit reminder";
               Cursor ucursor= reminder.read_by_id(c_id1);
                editrdialoguebox(ucursor);
                break;
            case 3:
                Intent sendmail=new Intent(this,Sendmail.class);
                sendmail.putExtra("sendmail_id",c_id1);
                startActivity(sendmail);

                break;
        }
        return super.onContextItemSelected(item);
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId()) {
            case R.id.btn_addR2:
                key = "ReminderList";
                dailogbox();
                break;
            case R.id.Rbtnsave:
                String my_date= Rdate.getText().toString() + " " + Rtime.getText().toString() + ":02";
                if (Rtitle.getText().toString().equals("") || Rdecrptn.getText().toString().equals("") || my_date.equals("") || Rtime.getText().toString().equals("")) {
                    Toast.makeText(this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                } else {

                    c.set(Syear, Smonth, Sday, Shour, Sminutes,02);
                    long stime1=c.getTimeInMillis();
                    if (System.currentTimeMillis() > stime1) {
                        Toast.makeText(this, "Please select valid date and time", Toast.LENGTH_SHORT).show();
                    } else {
                        savereminder();
                    }

                }
                break;
            case R.id.Rbtncancel:
                if (key.equals("Launcher_Page")) {
                    alert.cancel();
                    this.finish();
                } else {
                    alert.cancel();
                }

                break;
            case R.id.Rbtnadddate:
                     Calendar c1=Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Syear = year;
                        Smonth = monthOfYear;
                        Sday = dayOfMonth;
                        String month = "" + (Smonth + 1), day = "" + Sday;
                        if ((month).length() == 1) {
                            month = "0" + month;
                        }
                        if (("" + Sday).length() == 1) {
                            day = "0" + Sday;
                        }
                        Rdate.setText(day + "-" + (month) + "-" + year);
                    }
                }, c1.get(Calendar.YEAR), c1.get(Calendar.MONTH), c1.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
                break;
            case R.id.Rbtnaddtime:
                Calendar c2=Calendar.getInstance();
                TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        Shour = hourOfDay;
                        Sminutes = minute;
                        String hh = "" + Shour, mm = "" + Sminutes;
                        if (("" + Shour).length() == 1) {
                            hh = "0" + Shour;
                        }
                        if (("" + Sminutes).length() == 1) {
                            mm = "0" + Sminutes;
                        }
                        Rtime.setText(hh + ":" + mm);
                    }
                }, c2.get(Calendar.HOUR_OF_DAY), c2.get(Calendar.MINUTE), false);
                timePickerDialog.show();
                break;
            case R.id.ERbtnupdate:
                String my_updated_date=Rdate.getText().toString()+" "+Rtime.getText().toString()+":02";
                if (Rdate.getText().toString().equals("") || Rtime.getText().toString().equals("")) {
                    Toast.makeText(this, "Please Enter All Details..", Toast.LENGTH_SHORT).show();
                } else
                {
                   /*Calendar cupdate=Calendar.getInstance();
                    cupdate.set(Syear, Smonth, Sday, Shour, Sminutes, 02);
                    long stime2=cupdate.getTimeInMillis();
                    if (System.currentTimeMillis() > stime2) {
                        Toast.makeText(this, "Please select valid date and time", Toast.LENGTH_SHORT).show();
                    } else {*/
                        updatereminder(c_id1);
                    //}

                }
                break;
        }
    }
    public void checkalrms()
    {
        System.out.println("******I am in check alarm");

        reminder=new Rdatabase(this,"Reminders",null,01);
        Cursor alarmsc=reminder.showReminders();

        for(int i=0;i<alarmsc.getCount();i++)
        {
            int checkid=alarmsc.getInt(alarmsc.getColumnIndex("_id"));
            System.out.println("********I am in check alarm"+i);

            Intent checkintents=new Intent(this,Broadcastrcr.class);//the same as up
            boolean isWorking = (PendingIntent.getBroadcast(this,checkid, checkintents, PendingIntent.FLAG_NO_CREATE) != null);//just changed the flag
            if(isWorking==false)
            {
                System.out.println("I am in check alarm delete");
                reminder.delete_by_id(checkid);
            }
            alarmsc.moveToNext();
        }
    }

    public void dailogbox()
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.pics);
        builder.setTitle("Reminder");
        View v=getLayoutInflater().inflate(R.layout.addreminder,null);
        Rtitle=(EditText)v.findViewById(R.id.Rettittle);
        Rdecrptn=(EditText)v.findViewById(R.id.Retdescrption);
        Rdate=(TextView)v.findViewById(R.id.Retdate);
        Rtime=(TextView)v.findViewById(R.id.RetTime);
        rpeat=(Spinner)v.findViewById(R.id.RSpinrepeat);
        RAddDate=(Button)v.findViewById(R.id.Rbtnadddate);
        RAddTime=(Button)v.findViewById(R.id.Rbtnaddtime);
        RSave=(Button)v.findViewById(R.id.Rbtnsave);
        RCancel=(Button)v.findViewById(R.id.Rbtncancel);

        RAddDate.setOnClickListener(this);
        RAddTime.setOnClickListener(this);
        RSave.setOnClickListener(this);
        RCancel.setOnClickListener(this);

        builder.setView(v);
        builder.setCancelable(false);
        alert=builder.create();
        alert.show();
    }
    public void savereminder()
    {

        stitle=Rtitle.getText().toString();
        sdecrptn=Rdecrptn.getText().toString();
        sdate=Rdate.getText().toString();
        settime=Rtime.getText().toString();
        srpt=rpeat.getSelectedItem().toString();

        c.set(Syear, Smonth, Sday, Shour, Sminutes,02);
        long stime=c.getTimeInMillis();

        System.out.println("*********************");
        reminder=new Rdatabase(this,"Reminders",null,01);
        long R_id=reminder.addReminder(stitle, sdecrptn, settime, sdate, srpt);
        System.out.println("********** R_id=" + R_id + "***********");


        Intent reminderitent=new Intent(this,Broadcastrcr.class);
        reminderitent.putExtra("db_id", R_id);

        PendingIntent pi=PendingIntent.getBroadcast(this,(int)R_id,reminderitent,PendingIntent.FLAG_UPDATE_CURRENT);
        am=(AlarmManager) getSystemService(this.ALARM_SERVICE);
        if(srpt.equals("Once")) {
            System.out.println("**********" + key + "***********");
            am.set(AlarmManager.RTC_WAKEUP, stime, pi);
        }
        else if(srpt.equals("Repeat Daily"))
        {
            am.setRepeating(AlarmManager.RTC_WAKEUP, stime,AlarmManager.INTERVAL_DAY, pi);
        }
        alert.cancel();
        refresh(this);

    }
    public void editrdialoguebox(Cursor updatec)
    {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.pics);
        builder.setTitle("Reminder");
        View v=getLayoutInflater().inflate(R.layout.editreminder,null);
        ERtitle=(EditText)v.findViewById(R.id.ERettittle);
        ERdecrptn=(EditText)v.findViewById(R.id.ERetdescrption);
        Rdate=(TextView)v.findViewById(R.id.ERetdate);
        Rtime=(TextView)v.findViewById(R.id.ERetTime);
        Erpeat=(Spinner)v.findViewById(R.id.ERSpinrepeat);
        ERAddDate=(Button)v.findViewById(R.id.Rbtnadddate);
        ERAddTime=(Button)v.findViewById(R.id.Rbtnaddtime);
        ERupdate=(Button)v.findViewById(R.id.ERbtnupdate);
        ERCancel=(Button)v.findViewById(R.id.Rbtncancel);

        ERAddDate.setOnClickListener(this);
        ERAddTime.setOnClickListener(this);
        ERupdate.setOnClickListener(this);
        ERCancel.setOnClickListener(this);

        ERtitle.setText(updatec.getString(updatec.getColumnIndex("Title")));
        ERdecrptn.setText(updatec.getString(updatec.getColumnIndex("Description")));
        Rdate.setText(updatec.getString(updatec.getColumnIndex("Date")));
        Rtime.setText(updatec.getString(updatec.getColumnIndex("Time")));

        builder.setView(v);
        builder.setCancelable(false);
        alert=builder.create();
        alert.show();
    }

    public void updatereminder(int u_id)
    {
        Calendar clnder=Calendar.getInstance();
        utitle=ERtitle.getText().toString();
        udecrptn=ERdecrptn.getText().toString();
        udate=Rdate.getText().toString();
        uettime=Rtime.getText().toString();
        urpt=Erpeat.getSelectedItem().toString();
        clnder.set(Syear,Smonth,Sday,Shour,Sminutes,02);
        long lutime=clnder.getTimeInMillis();
        reminder=new Rdatabase(this,"Reminders",null,01);
        int r_id=reminder.update_by_id(u_id, utitle, udecrptn, uettime, udate, urpt);
        refresh(this);

        Intent reminderupdateitent=new Intent(this,Broadcastrcr.class);
        reminderupdateitent.putExtra("db_id", (long)u_id);
        PendingIntent u_pi=PendingIntent.getBroadcast(this,u_id,reminderupdateitent,PendingIntent.FLAG_UPDATE_CURRENT);
        am=(AlarmManager) getSystemService(this.ALARM_SERVICE);
        if(urpt.equals("Once")) {
            am.set(AlarmManager.RTC_WAKEUP,lutime, u_pi);
        }
        else
        {
            am.setRepeating(AlarmManager.RTC_WAKEUP,lutime,AlarmManager.INTERVAL_DAY,u_pi);
        }
        alert.cancel();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
      int details_id =c1.getInt(c1.getColumnIndex("_id"));
        Intent detailspage=new Intent(this,Details.class);
        detailspage.putExtra("id",details_id);
        startActivity(detailspage);
    }
}




class MyCustomadapter extends CursorAdapter
{


    public MyCustomadapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent)
    {
        LayoutInflater inflater=LayoutInflater.from(context);
        View onerow=inflater.inflate(R.layout.rlistrow,parent,false);
        return onerow;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        TextView rtvTitle=(TextView)view.findViewById(R.id.Rtvtitle);
        TextView rtvDate=(TextView)view.findViewById(R.id.Rtvdate);
        TextView rtvTime=(TextView)view.findViewById(R.id.Rtvtime);

        rtvTitle.setText("Title : "+cursor.getString(cursor.getColumnIndex("Title")));
        rtvDate.setText("Date :"+cursor.getString(cursor.getColumnIndex("Date")));
        rtvTime.setText("Time :"+cursor.getString(cursor.getColumnIndex("Time")));
    }
}
