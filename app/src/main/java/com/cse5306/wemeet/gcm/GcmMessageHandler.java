package com.cse5306.wemeet.gcm;

/**
 * Created by Sathvik on 20/03/15.
 */

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.cse5306.wemeet.R;
import com.cse5306.wemeet.activities.MeetingDetailsActivity;
import com.cse5306.wemeet.activities.UserHomeScreenActivity;
import com.google.android.gms.gcm.GoogleCloudMessaging;

public class GcmMessageHandler extends IntentService {
    public static final int MESSAGE_NOTIFICATION_ID = 435345;

    public GcmMessageHandler() {
        super("GcmMessageHandler");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);

        Intent intentActStart = null;

        String type = extras.getString("type");


        if(type.equalsIgnoreCase("1")){
            intentActStart = new Intent(this, UserHomeScreenActivity.class);
        }else if(type.equalsIgnoreCase("2")){
            intentActStart = new Intent(this, MeetingDetailsActivity.class);
            intentActStart.putExtra("fragmentInflateType","map");
            intentActStart.putExtra("groupId",extras.getString("group_id"));
            //Log.d("rec gid", String.valueOf(extras.getString("group_id")));
        }

        String title = extras.getString("title");
        String body = extras.getString("body");

        createNotification(title, body, intentActStart);
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("IntentService","intent");
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

}