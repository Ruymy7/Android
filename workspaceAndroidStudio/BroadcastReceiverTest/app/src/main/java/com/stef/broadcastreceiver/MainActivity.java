package com.stef.broadcastreceiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Register mMessageReceiver to receive messages.
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("my-event"));
    }

    //
    // Definition of local broadcaster
    //   handler for received Intents for the "my-event" event
    //
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Extract data included in the Intent
            String message = intent.getStringExtra("message");
            Log.d("receiver", "Got message: " + message);

        }
    };

    @Override
    protected void onPause() {
        // Unregister since the activity is not visible
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }


    //
    // Schedule alart from statically defined alert and receiver
    // Here event is not identified
    //
    public void scheduleAlert(View view) {
        EditText text = (EditText) findViewById(R.id.time);
        int i = Integer.parseInt(text.getText().toString());

        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 234324243, intent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                + (i * 1000), pendingIntent);
        Toast.makeText(this, "Alarm set in " + i + " seconds",
                Toast.LENGTH_LONG).show();
    }

    //
    // Start application event
    //
    public void launchApplicationEvent(View view){

        Intent intent = new Intent();
        intent.setAction("com.stef.tabuenca.wakeup");
        Toast.makeText(this, "Started application event "+intent.getAction(),
                Toast.LENGTH_LONG).show();

        sendBroadcast(intent);
    }


    //
    // Send an Intent with an action named "my-event"
    //
    // The LocalBroadcastManager class is used to register for and send broadcasts of Intents to
    // local objects within your process.
    // This is faster and more secure as your events don't leave your application.
    //
    public void launchLocalApplicationEvent(View view) {

        Intent intent = new Intent("my-event");
        intent.putExtra("message", "You should be expecting this message bla bla");
        Toast.makeText(this, "Started local application event "+intent.getAction(),
                Toast.LENGTH_LONG).show();

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}
