package gpovallas.services.receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import gpovallas.app.GPOVallasApplication;

public class SchedulerReceiver extends BroadcastReceiver {

    private final static String TAG = SchedulerReceiver.class.getSimpleName();

    public SchedulerReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent i = new Intent(context, StartReceiver.class);
        PendingIntent pending = PendingIntent.getBroadcast(context, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        Calendar cal = Calendar.getInstance();
        // start 30 seconds after boot completed
        cal.add(Calendar.SECOND, 30);
        // fetch every 30 seconds
        // InexactRepeating allows Android to optimize the energy consumption

        GPOVallasApplication.service.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), GPOVallasApplication.SERVICE_REPEAT_TIME, pending);

        Log.e(TAG, "CRON SCHEDULER STARTED");

    }
}
