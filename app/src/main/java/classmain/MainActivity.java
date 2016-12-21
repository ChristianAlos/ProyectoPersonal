package classmain;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import alarma.AlarmReceiver;
import classJSON.LeerJson;
import classObjetos.AlertDFragment;
import classObjetos.DatosTrenes;
import classdatabase.BBDDEstaciones;
import classfragments.AlarmasFragment;
import classfragments.HorariosFragment;
import classstaic.Skeleton;
import gdr.tmbmetro.AutoLog;
import gdr.tmbmetro.R;

import static classfragments.HorariosFragment.getAppContext;
import static classstaic.Skeleton.listPetitionsTrensData1;
import static classstaic.Skeleton.listPetitionsTrensData2;
import static classstaic.Skeleton.numeroDeTrenSpinner;
import static classstaic.Skeleton.pulsaBotVia;
import static classstaic.Skeleton.via1bool;
import static classstaic.Skeleton.via2bool;


public class MainActivity extends AppCompatActivity {
    List<String> list;
    Map<String, Integer> dic;
    private AlarmManager alarmManager;
    private List<PendingIntent> pendingIntent;

    private static final int REQUEST_WRITE_STORAGE = 112;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_actitivity);

        activities.add(this);

        if (getIntent().getBooleanExtra("LOGOUT", false)) {
            this.finishAffinity();
        }
        //permisos de escritura
        boolean hasPermission = (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);

        }

        pendingIntent = new ArrayList<>();

        AutoLog autLog = new AutoLog();
        Skeleton skeleton = new Skeleton();
        skeleton.llenaLineas();

        list = skeleton.getList();
        dic = skeleton.getDic();

        SharedPreferences prefs = getSharedPreferences("prefsApp", Context.MODE_PRIVATE);
        String fechaSinc = prefs.getString("fechaSinc", null);

        if (fechaSinc == null || !autLog.compararFechas(fechaSinc)) {
            ArrayList cosasABajar = new ArrayList<>();
            getApplicationContext().deleteDatabase("BBDD");
            BBDDEstaciones bbddEs = new BBDDEstaciones(getApplicationContext(), "BBDD", null, 1);
            SQLiteDatabase db = bbddEs.getWritableDatabase();
            for (int i = 0; i < list.size(); i++) {
                cosasABajar.add("https://tmbapi.tmb.cat/v1/metro/trains/routes/" +
                        dic.get(list.get(i)) +
                        "/stops?app_id=e8e58d40&app_key=a87e2200fd63a67db4492d0ddde2db6a&timestamp=1469643950043");
            }

            new GetJSON(getApplicationContext()).execute(cosasABajar);
        } else {

            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);

            if (fragment == null) {
                fragment = new HorariosFragment();
                fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.opciones_menu, menu);
        final ActionBar supportAB = getSupportActionBar();
        //supportAB.setDisplayShowTitleEnabled(false);
        return true;
    }
    private static final String DIALOG_AL = "DIALOG ALARM";

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {

            case R.id.home:

                break;
            case R.id.alarma:

                FragmentManager fmmanager = getSupportFragmentManager();

                AlertDFragment al = new AlertDFragment();
                al.show(fmmanager, DIALOG_AL);

                break;
            case R.id.killAlarma:
                NotificationManager manager = (NotificationManager) getAppContext().getSystemService(getAppContext().NOTIFICATION_SERVICE);

                alarmManager = (AlarmManager) getAppContext().getSystemService(ALARM_SERVICE);
                Intent updateServiceIntent = new Intent(getAppContext().getApplicationContext(), AlarmReceiver.class);
                //PendingIntent pendingUpdateIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, updateServiceIntent, 0);
                PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(getAppContext(), 0, updateServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                try {
                    alarmManager.cancel(pendingUpdateIntent);
                } catch (Exception e) {
                    Log.d("ALARM", e.toString());
                }
                break;
            default:
                Toast.makeText(getApplicationContext(), ("No hace nada"), Toast.LENGTH_SHORT).show();
                break;
        }


        return super.onOptionsItemSelected(item);
    }

    public void crearAlarma(String hora) {


        long llegadaTren = Long.valueOf(hora) - 600000;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(llegadaTren);
        Log.d("HORA ALARMA", "" + calendar.getTime());

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(this, AlarmReceiver.class);
        PendingIntent penInt = PendingIntent.getBroadcast(this, 0, myIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), penInt);
        pendingIntent.add(penInt);

    }

    public int obtenerPosicion(List<DatosTrenes> datos, String numeroTren) {
        int contador = 0;
        int numAdevolver = 0;
        for (DatosTrenes pos : datos) {
            if (pos.getNumDeTren().equals(numeroTren)) {
                numAdevolver = contador;
            }
            contador++;
        }
        return numAdevolver;
    }



    public class GetJSON extends AsyncTask<ArrayList, Void, ArrayList> {
        private Context context;
        HttpURLConnection con;

        public GetJSON(Context applicationContext) {
            this.context = applicationContext;
        }

        @Override
        protected ArrayList doInBackground(ArrayList... strings) {
            URL url;
            ArrayList arr = new ArrayList();
            HttpURLConnection urlConnection = null;
            for (int i = 0; i < strings[0].size(); i++)
                try {
                    url = new URL(strings[0].get(i).toString());

                    urlConnection = (HttpURLConnection) url.openConnection();

                    InputStream in = urlConnection.getInputStream();

                    BufferedReader reader = null;
                    try {
                        reader = new BufferedReader(new InputStreamReader(in, "UTF-8"), 8);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) { // Read line by line
                        sb.append(line);
                    }
                    String resString = sb.toString();
                    try {
                        in.close(); // Close the stream
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (resString != null) {
                        arr.add(resString);

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }

            return arr;
        }

        protected void onPostExecute(ArrayList stream) {
            for (int i = 0; i < stream.size(); i++) {
                try {
                    JSONObject reader = new JSONObject(stream.get(i).toString());
                    try {

                        LeerJson lector = new LeerJson();
                        ArrayList<String> inserts = lector.leerEstaciones(reader, dic.get(list.get(i)).toString());
                        BBDDEstaciones bbddEs = new BBDDEstaciones(context, "BBDD", null, 1);
                        SQLiteDatabase db = bbddEs.getWritableDatabase();
                        for (int j = 0; j < inserts.size(); j++) {
                            db.execSQL(inserts.get(j));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            AutoLog au = new AutoLog();
            String fecha = au.obtenerFechaString();
            SharedPreferences prefs = getSharedPreferences("prefsApp", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("fechaSinc", fecha);
            editor.commit();
            FragmentManager fm = getSupportFragmentManager();
            Fragment fragment = fm.findFragmentById(R.id.fragment_container);

            if (fragment == null) {
                fragment = new HorariosFragment();
                fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
            }
        }



    }

    void showDialog(){
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("No podras ver la alarma que pongas, si no das permisos...");
        //final EditText input = new EditText(this);
        //b.setView(input);
        b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                // SHOULD NOW WORK
                //result = input.getText().toString();
            }
        });
        //b.setNegativeButton("CANCEL", null);
        b.create().show();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();

        Log.d("PAUSE", "P");
    }

    public void closeApplication(View view) {
        finish();
        moveTaskToBack(false);
    }

    private static ArrayList<Activity> activities=new ArrayList<Activity>();
    @Override
    protected void onDestroy() {
// closing Entire Application
        activities.remove(this);
        Log.d("DEST", "P");
        super.onDestroy();

    }


    public static void finishAll()
    {
        for(Activity activity:activities)
            activity.finish();
    }
}
