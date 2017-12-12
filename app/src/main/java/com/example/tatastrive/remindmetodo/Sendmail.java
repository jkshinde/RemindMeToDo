package com.example.tatastrive.remindmetodo;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sendmail extends AppCompatActivity implements View.OnClickListener {
Rdatabase rmail;
    EditText mailaddr,mailtitle,maildscrptn,mailRdate,mailRtime;
    Button mailcancel,mailsend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmail);
        mailaddr=(EditText)findViewById(R.id.EmailRetemailadd);
        mailtitle=(EditText)findViewById(R.id.EmailRetemailTitle);
        maildscrptn=(EditText)findViewById(R.id.EmailRetemailDscrptn);
        mailRdate=(EditText)findViewById(R.id.EmailRetemailDate);
        mailRtime=(EditText)findViewById(R.id.EmailRetemailTime);

        mailcancel=(Button)findViewById(R.id.EmailRbtnCancel);
        mailsend=(Button)findViewById(R.id.EmailRbtnSend);

        mailcancel.setOnClickListener(this);
        mailsend.setOnClickListener(this);
        setfile();


    }

    private void setfile()
    {
        Intent getmailintent=getIntent();
        int id =  getmailintent.getIntExtra("sendmail_id",-1);
        rmail=new Rdatabase(this,"Reminders",null,01);
        Cursor c=rmail.read_by_id(id);
        mailtitle.setText(c.getString(c.getColumnIndex("Title")));
        maildscrptn.setText(c.getString(c.getColumnIndex("Description")));
        mailRdate.setText(c.getString(c.getColumnIndex("Date")));
        mailRtime.setText(c.getString(c.getColumnIndex("Time")));
    }

    @Override
    public void onClick(View v)
    {
        if(v.getId()==R.id.EmailRbtnSend) {

            String emailTo = mailaddr.getText().toString();
            String emailSubject = " Reminder ";
            String emailContent = "Reminder Title:" + mailtitle.getText().toString() + "\n" + maildscrptn.getText().toString() +
                    "\nDate :" + mailRdate.getText().toString() + "\nTime :" + mailRtime.getText().toString();
            final String EMAIL_PATTERN =
                    "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
            final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
            final Matcher matcher = pattern.matcher(emailTo);
            if(matcher.matches()==true) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{emailTo});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
                emailIntent.putExtra(Intent.EXTRA_TEXT, emailContent);

                emailIntent.setType("message/rfc822");
                startActivity(Intent.createChooser(emailIntent, "Select an Email Client:"));
                this.finish();
            }
            else
            {
                Toast.makeText(this,"Enter Valid Email Address",Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            this.finish();
        }
    }
}

