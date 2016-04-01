package gpovallas.services.receivers;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class CronTask extends Service {
    public CronTask() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
