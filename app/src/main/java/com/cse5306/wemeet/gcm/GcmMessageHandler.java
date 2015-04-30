package com.cse5306.wemeet.gcm;

/**
 * Created by Sathvik on 20/03/15.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.activities.MeetingDetailsActivity;
import com.cse5306.wemeet.activities.UserHomeScreenActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GcmMessageHandler extends IntentService {

    public static final int MESSAGE_NOTIFICATION_ID = 435345;
    SimpleDateFormat parserSDF;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    // handel the intent sent by the receiver
    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        Intent intentActStart = null;
        // check the type of data received
        String type = extras.getString("type");

        if(type.equalsIgnoreCase("1")){
            intentActStart = new Intent(this, UserHomeScreenActivity.class);
        }else if(type.equalsIgnoreCase("2")){
            intentActStart = new Intent(this, MeetingDetailsActivity.class);
            intentActStart.putExtra("fragmentInflateType","map");
            intentActStart.putExtra("groupId",extras.getString("group_id"));
            String recDate = extras.getString("meeting_date") +" "+extras.getString("meeting_time");
            addToCalender(recDate,extras.getString("group_id"));
            Log.d("rec gid", String.valueOf(recDate));
        }

        String title = extras.getString("title");
        String body = extras.getString("body");

        createNotification(title, body, intentActStart);
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // Creates notification based on title and body received
    private void createNotification(String title, String body,Intent intent) {

        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification n  = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setStyle(new Notification.BigTextStyle().bigText(body))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .setAutoCancel(true).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    // add event to the calender
    public void addToCalender(String recDate,String groupId){
        parserSDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d=null;
        try{
            d = parserSDF.parse(recDate);
        }catch(Exception e){
            e.getStackTrace();
            return;
        }

        long calID = 3;
        long startMillis = d.getTime();
        long endMillis = startMillis+60*60*1000;

        ContentResolver cr = getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startMillis);
        values.put(CalendarContract.Events.DTEND, endMillis);
        values.put(CalendarContract.Events.TITLE, "Meeting with groupId "+groupId);
        values.put(CalendarContract.Events.DESCRIPTION, "Meeting");
        values.put(CalendarContract.Events.CALENDAR_ID, calID);
        values.put(CalendarContract.Events.EVENT_TIMEZONE, "America/Los_Angeles");
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
    }

}