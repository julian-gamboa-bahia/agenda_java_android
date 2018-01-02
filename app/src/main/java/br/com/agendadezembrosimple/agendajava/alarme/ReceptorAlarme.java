package br.com.agendadezembrosimple.agendajava.alarme;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ReceptorAlarme extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        /*
                //throw new UnsupportedOperationException("Not yet implemented");

        Intent i = new Intent(context, AlarmeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
        */
        Log.d("Dezembro21","Sistema de Alarme funcionando");
    }
}
