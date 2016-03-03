package gpovallas.services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Calendar;

import gpovallas.app.GPOVallasApplication;
import gpovallas.obj.DbParameters;
import gpovallas.obj.DbParameters.Tipo;
import gpovallas.ws.Sender;
import gpovallas.ws.Updater;

public class CronTask extends Service {

    private final IBinder mBinder = new MyBinder();
    private String lastUpdate = "";
    private String lastUpdateOk = "";
    private final static String TAG = CronTask.class.getSimpleName();

    @Override
    public void onCreate() {
        Log.i(TAG, "Started");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        DbParameters dbParameters = new DbParameters(getApplicationContext());
        Calendar c = Calendar.getInstance();
        long current = 0;
        if (GPOVallasApplication.token != null) {

            long freqActualizacionParcial = 7200000; //2 horas
            long freqActualizacionEnvio = 300000; //5 Minutos
            long lastExecutionParcial = 0;
            long lastExecutionEnvio = 0;

            if (dbParameters.hasParameter("partialfreq", Tipo.REMOTO))
                freqActualizacionParcial = Long.valueOf(dbParameters.getValue("partialfreq", Tipo.REMOTO));
            if (dbParameters.hasParameter("senderfreq", Tipo.REMOTO))
                freqActualizacionEnvio = Long.valueOf(dbParameters.getValue("senderfreq", Tipo.REMOTO));

            if (dbParameters.hasParameter("lastParcialExecution", Tipo.LOCAL))
                lastExecutionParcial = Long.valueOf(dbParameters.getValue("lastParcialExecution", Tipo.LOCAL));
            if (dbParameters.hasParameter("lastEnvioExecution", Tipo.LOCAL))
                lastExecutionEnvio = Long.valueOf(dbParameters.getValue("lastEnvioExecution", Tipo.LOCAL));


            current = c.getTimeInMillis();

            //ACTUALIZACION PARCIAL
            if ((current - lastExecutionParcial >= freqActualizacionParcial) || lastExecutionParcial == 0) {

                new Thread(new Runnable() {
                    public void run() {

                        Updater updater = new Updater(getApplicationContext());
                        updater.partialUpdate();

                    }
                }).start();

                dbParameters.setValue("lastParcialExecution", Tipo.LOCAL, String.valueOf(current));

            }
            //ENVIO
            if ((current - lastExecutionEnvio >= freqActualizacionEnvio) || lastExecutionEnvio == 0) {
                new Thread(new Runnable() {
                    public void run() {

                        Sender sender = new Sender(getApplicationContext());
                        Boolean result = sender.send();

                        GPOVallasApplication.senderLastResultado = result ? Updater.ResultadoUpdate.CORRECTA : Updater.ResultadoUpdate.FALLIDA;
                        GPOVallasApplication.senderLastDate = Calendar.getInstance().getTime();


                    }
                }).start();

                dbParameters.setValue("lastEnvioExecution", Tipo.LOCAL, String.valueOf(current));
            }

        }

        return Service.START_NOT_STICKY;

    }

    @Override
    public IBinder onBind(Intent arg0) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        public CronTask getService() {
            return CronTask.this;
        }
    }

}
