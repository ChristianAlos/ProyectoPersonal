package classObjetos;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import alarma.AlarmReceiver;
import classstaic.Skeleton;

import static android.content.ContentValues.TAG;

/**
 * Created by krialo_23 on 8/29/16.
 */

public class AlarmLab {
    //singleton
    private static AlarmLab sAlarmLab;

    private List<String> mAlarmas;
    public static String sAlarm;
    Context con;
    //singleton
    public static AlarmLab get(Context context){
        if (sAlarmLab == null){
            sAlarmLab = new AlarmLab(context);
        }

        return sAlarmLab;
    }

    private AlarmLab(Context context){
        con=context;
        //creamos mAlarms si no existe
        if (mAlarmas ==  null){
            mAlarmas = new ArrayList<>();
        }

    }



    public List<String> getAlarmas(){
        return mAlarmas;
    }

    public List<String> getmAlarmas() {
        return mAlarmas;
    }

    public void setmAlarmas(String mAlarmas) {
        this.mAlarmas.add(mAlarmas);
    }

    public void pararAlarmas(){

        AlarmManager alarmManager = (AlarmManager) con.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(con, AlarmReceiver.class);
        PendingIntent pending = PendingIntent.getService(con, 0, intent, 0);
        pending.getCreatorUid();

        try {
            alarmManager.cancel(pending);
        } catch (Exception e) {
            Log.e(TAG, "No se ha cancelado porque no habia alarmas " + e.toString());
        }
    }

}
