package com.example.tatastrive.remindmetodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Launcher_Page extends AppCompatActivity implements View.OnClickListener
{
    Rdatabase reminders;
    Button add;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher__page);
        add=(Button)findViewById(R.id.btn_addR);
        add.setOnClickListener(this);
       // reminder=new Rdatabase(this,"Reminders",null,01);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        menu.add(1,1,1,"About");
        menu.add(1,2,2,"Show reminders");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if(item.getItemId()==1)
        {

        }
        else
        {
            reminders=new Rdatabase(this,"Reminders",null,01);
           Cursor c= reminders.showReminders();
            if(c.getCount()>0) {
                Intent i = new Intent(this, ReminderList.class);
                i.putExtra("key1", "showlist");
                startActivity(i);
            }
            else
            {
                Toast.makeText(this,"No reminders Added",Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_addR:
                Intent i1 = new Intent(this, ReminderList.class);
                i1.putExtra("key1","Launcher_Page");
                startActivity(i1);
                break;
        }
    }

}

