package com.example.tatastrive.remindmetodo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.widget.Toast;

/**
 * Created by Tata Strive on 10/23/2017.
 */
public class Broadcastrcr extends BroadcastReceiver
{
    Rdatabase db;
    Context gcontext;
    Cursor c;
    ReminderList reminderList1;
    MediaPlayer mp;
    long row_id;
    Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);


    @Override
    public void onReceive(Context context, Intent intent)
    {
        gcontext=context;
        reminderList1=new ReminderList(context);
        db=new Rdatabase(context,"Reminders",null,01);

        row_id=intent.getExtras().getLong("db_id");
        //Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // Ringtone r = RingtoneManager.getRingtone(context, notification);
        //r.play();


        Toast.makeText(context, "Alarm", Toast.LENGTH_LONG).show();
        System.out.println("*************Alarm************");
        c=db.read_by_id(row_id);

        reminderbox();
        String repeat= c.getString(c.getColumnIndex("Roption"));
        System.out.println("*****........"+repeat+".........**");
        /*if(repeat.equals("Once"))
        {
            long r_id=db.delete_by_id(row_id);

            Intent intentone = new Intent(context.getApplicationContext(), ReminderList.class);
            intentone.putExtra("key1","broadcast");
            intentone.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentone);
        }*/

    }

    public void reminderbox()
    {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Notification.Builder builder = new Notification.Builder(gcontext.getApplicationContext());
        builder.setSmallIcon(R.drawable.reminders);
        builder.setContentTitle("Reminder for" + c.getString(c.getColumnIndex("Title")));
        builder.setContentText("" + c.getString(c.getColumnIndex("Description")) + "\nDate :" + c.getString(c.getColumnIndex("Date")) + "\t Time :" + c.getString(c.getColumnIndex("Time")));
        builder.setSound(soundUri).setSound(soundUri);
        Intent i = new Intent(gcontext, ReminderList.class);
        i.putExtra("key1","broadcast");
        PendingIntent pi = PendingIntent.getActivity(gcontext, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pi);
        NotificationManager manager = (NotificationManager) gcontext.getSystemService(gcontext.NOTIFICATION_SERVICE);
        Notification n = builder.build();
        manager.notify((int)row_id, n);
    }
}
