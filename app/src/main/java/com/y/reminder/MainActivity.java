package com.y.reminder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import java.util.Calendar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    AlarmManager alarm_Manager;
    TimePicker time_picker;
    EditText add_reminder;
    TextView update_text;
    Context context;
    Button alarm_on,alarm_off;
    PendingIntent pending_intent;

    SharedPreferences someData;
    public static String filename = "MySharedString";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.context=this;

        alarm_Manager=(AlarmManager) getSystemService(ALARM_SERVICE);
        time_picker=(TimePicker) findViewById(R.id.timePicker);
        add_reminder=(EditText) findViewById(R.id.etReminder);
        update_text=(TextView) findViewById(R.id.updateText);
        final Calendar calendar=Calendar.getInstance();

        someData = getSharedPreferences(filename, 0);

        final Intent myIntent = new Intent(this.context,AlarmReceiver.class);

        alarm_on = (Button) findViewById(R.id.bOn);
        alarm_on.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                int hour,minute;
                if (Build.VERSION.SDK_INT >= 23 ) {
                    calendar.set(Calendar.HOUR_OF_DAY, time_picker.getHour());
                    calendar.set(Calendar.MINUTE, time_picker.getMinute());
                    hour = time_picker.getHour();
                    minute = time_picker.getMinute();
                }
                else {
                    calendar.set(Calendar.HOUR_OF_DAY, time_picker.getCurrentHour());
                    calendar.set(Calendar.MINUTE, time_picker.getCurrentMinute());
                    hour = time_picker.getCurrentHour();
                    minute = time_picker.getCurrentMinute();
                }

                String hour_string = String.valueOf(hour);
                String minute_string = String.valueOf(minute);

                if(hour > 12){
                    hour_string = String.valueOf(hour - 12);
                }

                if(minute < 10){
                    minute_string = "0" + String.valueOf(minute_string);
                }

                update_text.setText("Alarm set to " + hour_string + ":" + minute_string);

                //put in extra string into myintent to inform clock that alarm_on btn has been pressed
                myIntent.putExtra("extra","alarm on");

                //creates a pending intent that delays the intent until the specified calendar time
                pending_intent = PendingIntent.getBroadcast(MainActivity.this, 0, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                //set the alarm manager
                alarm_Manager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);

                String stringData = add_reminder.getText().toString();
                SharedPreferences.Editor editor = someData.edit();
                editor.putString("aKey", stringData);
                editor.commit();
            }
        });

        alarm_off = (Button) findViewById(R.id.bOff);

        alarm_off.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                //update_text.setText("Alarm OFF!!");

                //cancel the alarm
                alarm_Manager.cancel(pending_intent);

                //put extra string into myIntent to tell the clock that alarm_off btn has been pressed
                myIntent.putExtra("extra","alarm off");

                //stop the ringtone
                sendBroadcast(myIntent);

                someData = getSharedPreferences(filename, 0);
                String dataReturned = someData.getString("aKey", "No Reminder Set!!");
                update_text.setText(dataReturned);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    protected void onRestoreInstanceState(Bundle outState) {
        super.onRestoreInstanceState(outState);
    }

}
