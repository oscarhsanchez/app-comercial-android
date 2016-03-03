package gpovallas.services.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import gpovallas.services.CronTask;

public class StartReceiver extends BroadcastReceiver {
    public StartReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, CronTask.class);
        context.startService(service);
    }
}
