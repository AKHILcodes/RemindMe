package com.y.reminder;

/**
 * Created by AKHIL on 10/3/2016.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //fetch extra string from the intent
        String getyourString = intent.getExtras().getString("extra");

        //create an intent to the ringtone service
        Intent service_intent = new Intent(context, RingtonePlayingService.class);

        //pass the extra string from main activity to the ringtone playing service
        service_intent.putExtra("extra_reloaded", getyourString);

        //start the ringtone service
        context.startService(service_intent);
    }
}