package com.y.reminder;

/**
 * Created by AKHIL on 10/3/2016.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by AKHIL on 9/21/2016.
 */
public class RingtonePlayingService extends Service {

    MediaPlayer media_song;
    boolean isRunning;
    Notification notification_popup;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int flags, int startId ){

        //fetch the extra string values
        String state = intent.getExtras().getString("extra_reloaded");

        //set up the notification service
        NotificationManager notify_manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        //set up the intent to main activity
        Intent intent_main_activity = new Intent(this.getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntentMainActivity = PendingIntent.getActivity(this, 0, intent_main_activity, 0);

        //this converts the extra strings from myIntent->service_intent->  to startIds, value 0 or 1
        assert state != null;
        switch (state){
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }

        if(!this.isRunning && startId == 1) {

            //create an instance of the media player
            media_song = MediaPlayer.create(this, R.raw.wake_up_alert);
            media_song.start();
            this.isRunning = true;
            startId = 1;


            //make the notification parameter
            if (Build.VERSION.SDK_INT >= 16 ) {
                notification_popup = new Notification.Builder(this)
                        .setContentTitle("You have set a reminder")
                        .setContentText("Click here to view")
                        .setSmallIcon(R.drawable.images_1)
                        .setContentIntent(pendingIntentMainActivity)
                        .setAutoCancel(true)
                        .build();
            }
            else {
                notification_popup = new Notification.Builder(this)
                        .setContentTitle("You have set a reminder")
                        .setContentText("Click here to view")
                        .setSmallIcon(R.drawable.images_1)
                        .setContentIntent(pendingIntentMainActivity)
                        .setAutoCancel(true)
                        .getNotification();
            }

            notify_manager.notify(0, notification_popup);
        }
        else if(this.isRunning && startId == 0){

            media_song.stop();
            media_song.reset();
            this.isRunning = false;
            startId = 0;
        }

        Log.i("LocalService","Received start Id " + startId + ":" + intent);
        return START_NOT_STICKY;
    }

    public void onDestroy(){
        //tell the user we stopped
        Toast.makeText(this,"on Destroy called", Toast.LENGTH_SHORT).show();
    }
}

