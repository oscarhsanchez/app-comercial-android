package gpovallas.app;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import gpovallas.app.briefs.BriefFinderActivity;
import gpovallas.app.clientes.ClientFinderActivity;
import gpovallas.services.receivers.StartReceiver;
import gpovallas.utils.Dialogs;
import gpovallas.ws.Sender;
import gpovallas.ws.Updater;

public class ControlPanelActivity extends GPOVallasActivity {

    private ProgressDialog dialog = null;
    private Boolean actualizacionForzadaEjecucion = false;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.controlpanel);
        setBreadCrumb("Panel de control", "");
    }


    public void openClientFinder(View v) {
        Log.v("Control Panel", "cargando ClientFinder");
        startActivity(new Intent(this, ClientFinderActivity.class));
    }

    public void openBrief(View v) {
        Log.v("Control Panel", "cargando BriefFinder");
        startActivity(new Intent(this, BriefFinderActivity.class));
    }

    public void openCatalogo(View v) {

    }

    public void openCircuito(View v) {

    }

    public void openConoce(View v) {

    }

    public void openSettings(View v) {
        openOptionsMenu();
    }

    public void noImplementado(View v) {
        Toast.makeText(this, "Apartado todavía no implementado",
                Toast.LENGTH_LONG).show();
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.arg1) {
                case 0:
                    if (GPOVallasApplication.loginCache) {
                        Dialog alertDialog = Dialogs.newAlertDialog(ControlPanelActivity.this, "Información", "Actualmente est� logeado por cache. Por favor, inicie sesión normalmente.", "OK");
                        alertDialog.show();
                        return;
                    }
                    if (GPOVallasApplication.updaterEnEjecucion || GPOVallasApplication.senderEnEjecucion) {
                        Dialog alertDialog = Dialogs.newAlertDialog(ControlPanelActivity.this, "Información", "En este momento hay un proceso de actualización en marcha. Por favor, espere.", "OK");
                        alertDialog.show();
                        return;
                    }
                    dialog = new ProgressDialog(ControlPanelActivity.this);
                    dialog.setCancelable(false);
                    dialog.setMessage("Actualizacion parcial...");
                    dialog.show();
                    new ActualizacionForzada().start();
                    break;
                case 3:
                    dialog.dismiss();
                    break;
            }
        }
    };

    private class ActualizacionForzada extends Thread {
        public void run() {
            super.run();
            actualizacionForzadaEjecucion = true;
            Log.i("Forzar actualizacion", "Estado de la variable de Sender ocupado:" + GPOVallasApplication.senderEnEjecucion);
            Sender sender = new Sender(getApplicationContext());
            Boolean result = sender.send();
            GPOVallasApplication.senderLastResultado = result ? Updater.ResultadoUpdate.CORRECTA : Updater.ResultadoUpdate.FALLIDA;
            GPOVallasApplication.senderLastDate = Calendar.getInstance().getTime();

            Updater updater = new Updater(getApplicationContext());
            GPOVallasApplication.updaterTipo = Updater.TipoUpdate.PARCIAL;
            updater.partialUpdate(true);
            updater.stockUpdate(true);

            Message mensaje = handler.obtainMessage();
            mensaje.arg1 = 3;
            handler.sendMessage(mensaje);
            actualizacionForzadaEjecucion = false;
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_panel_control, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.update_parcial:
                Message mensaje = handler.obtainMessage();
                mensaje = handler.obtainMessage();
                mensaje.arg1 = 4;

                handler.sendMessage(mensaje);
                break;
        }

        return true;

    }

    @Override
    public void closeApp(View v) {
        Dialogs.newConfirmDialog(this, "Información", "¿Desea realmente volver a la pantalla de inicio?",
                new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((GPOVallasApplication) getApplicationContext()).stopCheckPedidosTimer();
                        finish();
                    }
                }, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public void openOptionsMenu() {
        Configuration config = getResources().getConfiguration();

        if ((config.screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                > Configuration.SCREENLAYOUT_SIZE_LARGE) {

            int originalScreenLayout = config.screenLayout;
            config.screenLayout = Configuration.SCREENLAYOUT_SIZE_LARGE;
            super.openOptionsMenu();
            config.screenLayout = originalScreenLayout;

        } else {
            super.openOptionsMenu();
        }
    }

    @Override
    protected void onResume() {

        //Comprobamos si el servicio esta activo.
        Intent i = new Intent(getApplicationContext(), StartReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp
                || GPOVallasApplication.updaterParcialLastDate == null
                || ((new Date()).getTime() - GPOVallasApplication.updaterParcialLastDate.getTime()) / 60000 > 60) {
            PendingIntent pending = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
            Calendar cal = Calendar.getInstance();

            cal.add(Calendar.SECOND, 30);
            GPOVallasApplication.service.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), GPOVallasApplication.SERVICE_REPEAT_TIME, pending);
        }

        super.onResume();
    }
}
