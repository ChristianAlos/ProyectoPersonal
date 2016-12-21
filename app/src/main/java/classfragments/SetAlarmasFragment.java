package classfragments;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import alarma.AlarmReceiver;
import classObjetos.AlarmLab;
import classObjetos.AlertDFragment;
import classObjetos.DatosTrenes;
import classObjetos.Fecha;
import gdr.tmbmetro.R;

import static android.content.Context.ALARM_SERVICE;

import static android.support.v7.widget.StaggeredGridLayoutManager.TAG;
import static classObjetos.AlarmLab.sAlarm;
import static classfragments.HorariosFragment.getAppContext;
import static classstaic.Skeleton.listPetitionsTrensData1;
import static classstaic.Skeleton.listPetitionsTrensData2;



/**
 * Created by krialo_23 on 9/2/16.
 */

public class SetAlarmasFragment extends Fragment {

    private static final String DIALOG_AL = "DIALOG ALARM";

    private Button al1, al2, al3, al4, al5, al6;
    private AlarmManager alarmManager;
    private boolean b1;
    private boolean b2;
    private String numeroTrenSp;
    private boolean mensaje = false;
    private Button butBack, butDel;
    private TextView diaAlarma, horaAlarma;

    private static final int REQUEST_WRITE_STORAGE = 112;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b1 = getArguments().getBoolean("via1");
        b2 = getArguments().getBoolean("via2");
        numeroTrenSp = getArguments().getString("numeroTren");

       // Toast.makeText(getAppContext(), "Sin permisos no se visualizarán las alarmas", Toast.LENGTH_LONG);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frament_set_alarms, container, false);
        int id = 0;

        //alarmManager = lecturaFicheroAlarmasManager();

        al1 = (Button) view.findViewById(R.id.botmas10);
        al2 = (Button) view.findViewById(R.id.botmas15);
        al3 = (Button) view.findViewById(R.id.botmas20);
        al4 = (Button) view.findViewById(R.id.botmas25);
        al5 = (Button) view.findViewById(R.id.botmas30);
        al6 = (Button) view.findViewById(R.id.botmas35);
        butBack = (Button) view.findViewById(R.id.butBackAlarm);
        butDel = (Button) view.findViewById(R.id.butKillAlarm);

        al1.setId(id);
        al2.setId(id + 1);
        al3.setId(id + 2);
        al4.setId(id + 3);
        al5.setId(id + 4);
        al6.setId(id + 5);
        butBack.setId(id + 6);
        butDel.setId(id + 7);

        al1.setOnClickListener(botones);
        al2.setOnClickListener(botones);
        al3.setOnClickListener(botones);
        al4.setOnClickListener(botones);
        al5.setOnClickListener(botones);
        al6.setOnClickListener(botones);
        butBack.setOnClickListener(botones);
        butDel.setOnClickListener(botones);

        diaAlarma = (TextView) view.findViewById(R.id.tvDiaAlarma);
        horaAlarma = (TextView) view.findViewById(R.id.tvHora);
        Fecha f = lecturaFicheroAlarmas();
        if (f != null){
            diaAlarma.setText(f.getDia());
            horaAlarma.setText(f.getHora());
        }

        return view;
    }


    View.OnClickListener botones = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case 0: //boton 1 al1
                    setAlarmasTime(600000);
                    break;
                case 1: //boton 2 al2
                    setAlarmasTime(900000);
                    break;
                case 2:
                    setAlarmasTime(1200000);
                    break;
                case 3:
                    setAlarmasTime(1500000);
                    break;
                case 4:
                    setAlarmasTime(1800000);
                    break;
                case 5:
                    setAlarmasTime(2100000);
                    break;
                //back fragment
                case 6:
                    HorariosFragment fragment2 = new HorariosFragment();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment_container, fragment2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case 7:
                    vaciarContenidoAlarmas();
                    //AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);
                    NotificationManager manager = (NotificationManager) getActivity().getSystemService(getAppContext().NOTIFICATION_SERVICE);

                    alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                    Intent updateServiceIntent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
                    //PendingIntent pendingUpdateIntent = PendingIntent.getService(getActivity().getApplicationContext(), 0, updateServiceIntent, 0);
                    PendingIntent pendingUpdateIntent = PendingIntent.getBroadcast(getActivity(), 0, updateServiceIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    try {
                        alarmManager.cancel(pendingUpdateIntent);
                    } catch (Exception e) {
                        Log.d("ALARM", e.toString());
                    }
                    diaAlarma.setText("Día");
                    horaAlarma.setText("Hora");

                    break;
            }
        }
    };


    private void setAlarmasTime(int tiempo) {
        vaciarContenidoAlarmas();
        if (b1 == true) {
            int posicion1 = obtenerPosicion(listPetitionsTrensData1, numeroTrenSp);
            crearAlarma(listPetitionsTrensData1.get(posicion1).getHorasTren(), tiempo);
        } else if (b2 == true) {
            int posicion2 = obtenerPosicion(listPetitionsTrensData2, numeroTrenSp);
            crearAlarma(listPetitionsTrensData2.get(posicion2).getHorasTren(), tiempo);
        } else {
            Toast.makeText(getAppContext(), "No se puede instanciar una alarma, faltan datos", Toast.LENGTH_SHORT);
        }

        FragmentManager manager = getFragmentManager();
        AlertDFragment al = new AlertDFragment();
        al.show(manager, DIALOG_AL);


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

    public void crearAlarma(String hora, int tiempo) {

        //long llegadaTren = Long.valueOf(hora) - 600000;
        long llegadaTren = Long.valueOf(hora) - tiempo;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(llegadaTren);
        Log.d("HORA ALARMA", "" + calendar.getTime());

        //alarmManager = (AlarmManager) appContext.getSystemService(ALARM_SERVICE);
        alarmManager = (AlarmManager) getAppContext().getSystemService(ALARM_SERVICE);
        Intent myIntent = new Intent(getAppContext(), AlarmReceiver.class);
        PendingIntent penInt = PendingIntent.getBroadcast(getAppContext(), 0, myIntent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), penInt);


        //crearGuardarPedingIntent(penInt);
        //crearGuardarAlarmasManager(alarmManager);
        sAlarm = String.valueOf(new SimpleDateFormat("dd.MM.yy;HH:mm:ss").format(llegadaTren));
        crearGuardarAlarmas(sAlarm);



    }




    private void crearGuardarAlarmas(String alarms) {

        //String r = Environment.getExternalStorageState();

        //File root = Environment.getDataDirectory();
        File root = Environment.getExternalStorageDirectory();

        if (root.canWrite()) {
            //creamos un directorio para guardar las alarmas
            File dir = new File(root + "/alarmasTMB/");
            dir.mkdir();
            File dataFile = new File(dir, "alarmas.txt");
            try (BufferedWriter out = new BufferedWriter(new FileWriter(dataFile, true))) {
                out.write(alarms);
                out.newLine();
                out.flush();

            } catch (IOException e) {
                e.getMessage();
            }

        }
    }


    private void vaciarContenidoAlarmas(){
        String s;
        File root = Environment.getExternalStorageDirectory();
        if (root.canRead()) {
            File dir = new File(root + "/alarmasTMB");
            dir.mkdir();
            File dataFile = new File(dir, "alarmas.txt");
            try (BufferedWriter lector = new BufferedWriter(new FileWriter(dataFile))) {
                lector.write("");
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }
    private Fecha lecturaFicheroAlarmas (){
        AlarmLab al = AlarmLab.get(getActivity());
        Fecha fecha = null;
        String s;
        File root = Environment.getExternalStorageDirectory();
        if (root.canRead()) {
            File dir = new File(root + "/alarmasTMB");
            dir.mkdir();
            File dataFile = new File(dir, "alarmas.txt");
            try (BufferedReader lector = new BufferedReader(new FileReader(dataFile))) {
                while ((s = lector.readLine())!= null){
                    al.setmAlarmas(s);
                    String[] parts = s.split(";");
                    fecha = new Fecha(parts[0], parts[1]);

                }
            } catch (IOException e) {
                e.getMessage();
            }
        }
        return fecha;
    }



}
