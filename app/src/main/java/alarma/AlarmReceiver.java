package alarma;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;

import classmain.MainActivity;
import classmain.PararAlarma;
import classstaic.Skeleton;


public class AlarmReceiver extends WakefulBroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        Intent service1 = new Intent(context, StopAlarm.class);
        service1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(service1);
    }
}