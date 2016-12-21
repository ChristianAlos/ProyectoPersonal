package classfragments;

import android.app.AlarmManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Handler;
import android.os.Process;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import classJSON.GetJSONTrens;

import classObjetos.AlertDFragment;
import classObjetos.AlertNoConetion;
import classObjetos.DatosEstaciones;
import classObjetos.DatosTrenes;
import classdatabase.BBDDEstaciones;
import classstaic.Skeleton;
import gdr.tmbmetro.R;

import static classstaic.Skeleton.activarViasBots;
import static classstaic.Skeleton.botCarga;
import static classstaic.Skeleton.botVia1;
import static classstaic.Skeleton.botVia2;
import static classstaic.Skeleton.listPetitionsTrensData1;
import static classstaic.Skeleton.listPetitionsTrensData2;
import static classstaic.Skeleton.numeroDeTrenSpinner;
import static classstaic.Skeleton.numerosObtenidos;
import static classstaic.Skeleton.pulsaBotVia;

import static classstaic.Skeleton.s3;
import static classstaic.Skeleton.via1bool;
import static classstaic.Skeleton.via2bool;

/**
 * Created by krialo_23 on 8/24/16.
 */

public class HorariosFragment extends Fragment {
    private Button conState, selState, trackState, reload;
    private Spinner s1, s2;

    private List<String> list;
    private Map<String, Integer> dic;

    private Integer codEstacio = null;
    private String num = null;
    private TextView lbHora, lbEstacion, lbVia;

    private TextView lbHoraPet, lbEstacionPet, lbViaPet;

    private static Context appContext;
    private final Handler myHandler = new Handler();
    private Timer timer;


    private DatosEstaciones datosEstaciones;
    private static final String DIALOG_AL = "DIALOG CONECTION";

    //NUEVO INTENTO
    private ArrayAdapter<String> adpLin = null;
    private ArrayAdapter<String> adpEst = null;
    private ArrayAdapter<String> adpNum = null;
    private Button botPeticionTren;
    private Button botSetAlarm, botListAlarm;
    private View v;

    //private TextView lbEstacio2;
    private DatosTrenes datosTrenesADevolver;
    private boolean conexion;


    private static final int REQUEST_WRITE_STORAGE = 112;


    public static Context getAppContext() {
        return appContext;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boolean conection = isOnline();
        if (!conection){
            FragmentManager manager = getFragmentManager();
            AlertNoConetion al = new AlertNoConetion();
            al.show(manager, DIALOG_AL);

        }
    }
    public Boolean isOnline() {
        try {
            java.lang.Process p1 = Runtime.getRuntime().exec("ping -c 1 www.google.com");
            int returnVal = p1.waitFor();
            boolean reachable = (returnVal==0);
            return reachable;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.horarios_fragment, container, false);
        //comprobamos si hay internet

        variablesDefecto();
        loadApp(v);

        return v;
    }


    private void loadApp(final View v) {
        variablesDefecto();
        timer = null;
        timer = new Timer();

        //Spinners
        s1 = (Spinner) v.findViewById(R.id.spinner1);
        s2 = (Spinner) v.findViewById(R.id.spinner2);
        s3 = (Spinner) v.findViewById(R.id.spinnernumtren);
        s3.setAdapter(null);
        s3.setEnabled(false);
        s1.setEnabled(true);
        s2.setEnabled(true);

        //Botones
        botCarga = (Button) v.findViewById(R.id.botcarga);
        botVia1 = (Button) v.findViewById(R.id.botonvia1);
        botVia2 = (Button) v.findViewById(R.id.botonvia2);
        botPeticionTren = (Button) v.findViewById(R.id.seleccionCon);
        botListAlarm = (Button) v.findViewById(R.id.buttonListAlarm);
        botSetAlarm = (Button) v.findViewById(R.id.buttonSetAlarma);
        botPeticionTren.setEnabled(false);

        botCarga.setEnabled(true);
        botVia1.setEnabled(false);
        botVia2.setEnabled(false);
        botSetAlarm.setEnabled(false);

        reload = (Button) v.findViewById(R.id.reloadBut);
        conState = (Button) v.findViewById(R.id.conState);
        selState = (Button) v.findViewById(R.id.selState);
        trackState = (Button) v.findViewById(R.id.trackState);
        //colores buttons
        selState.setBackgroundColor(Color.RED);
        conState.setBackgroundColor(Color.RED);
        trackState.setBackgroundColor(Color.RED);
        reload.setBackgroundColor(Color.LTGRAY);
        reload.setEnabled(false);

        //TextViews
        lbEstacion = (TextView) v.findViewById(R.id.lbestacion);
        lbHora = (TextView) v.findViewById(R.id.lbHora);
        lbVia = (TextView) v.findViewById(R.id.selecVia);


        lbEstacionPet = (TextView) v.findViewById(R.id.lbEstacionActual);
        lbHoraPet = (TextView) v.findViewById(R.id.lbHora2);
        lbViaPet = (TextView) v.findViewById(R.id.lbviaactual);

        //El color de fondo se lo podriamos poner directamente al layout
        lbEstacion.setText("ESTACIÓN SELEC");
        lbEstacion.setBackgroundColor(Color.parseColor("#5F4C0B"));
        lbVia.setBackgroundColor(Color.parseColor("#5F4C0B"));
        lbVia.setText("VIA");
        lbHora.setBackgroundColor(Color.parseColor("#5F4C0B"));
        lbHora.setText("HORA");
        lbEstacion.setTextColor(Color.parseColor("#FFFFFF"));
        //lbEstacion.setAllCaps(true);
        lbVia.setTextColor(Color.parseColor("#FFFFFF"));
        lbHora.setTextColor(Color.parseColor("#FFFFFF"));

        lbEstacionPet.setText("ESTACIÓN ACTUAL");
        lbEstacionPet.setBackgroundColor(Color.parseColor("#5F4C0B"));
        lbViaPet.setBackgroundColor(Color.parseColor("#5F4C0B"));
        lbHoraPet.setBackgroundColor(Color.parseColor("#5F4C0B"));
        lbHoraPet.setText("HORA");
        lbViaPet.setText("VIA");
        lbEstacionPet.setTextColor(Color.parseColor("#FFFFFF"));
        lbViaPet.setTextColor(Color.parseColor("#FFFFFF"));
        lbHoraPet.setTextColor(Color.parseColor("#FFFFFF"));

        //contex
        appContext = getContext();

        //cargamos el spinner 1
        final Skeleton skeleton = new Skeleton();
        list = skeleton.getList();
        dic = skeleton.getDic();

        adpLin = new ArrayAdapter<String>(getAppContext(), android.R.layout.simple_list_item_1, list);
        adpLin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s1.setAdapter(adpLin);

        adpNum = null;
        //LISTENERS
        //cargamos el s2
        s1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
                //conexion con la base de datos
                BBDDEstaciones bbddEs = new BBDDEstaciones(getAppContext(), "BBDD", null, 1);
                SQLiteDatabase db = bbddEs.getReadableDatabase();
                String[] codigoEst = new String[]{dic.get(list.get(position)).toString()};
                datosEstaciones = bbddEs.retornoEstaciones(codigoEst);

                //cargamos el spiner 2 dependiendo de la linea seleccionada
                adpEst = new ArrayAdapter<String>(getAppContext(), android.R.layout.simple_list_item_1, datosEstaciones.getEstaciones());
                adpEst.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s2.setAdapter(adpEst);
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }

        });
        //obtenemos el codigo de la estacion
        s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //abrimos la base de datos
                BBDDEstaciones bbddEs = new BBDDEstaciones(getAppContext(), "BBDD", null, 1);
                SQLiteDatabase db = bbddEs.getReadableDatabase();
                String linia = s1.getSelectedItem().toString();
                String[] liniaName = linia.split(" ");
                num = liniaName[1];
                if (db != null) {
                    //codEstacio = bbddEs.retornoNumEstacion(s2.getSelectedItem().toString());
                    codEstacio = bbddEs.retornoNumEstacion(s2.getSelectedItem().toString(), num);
                    db.close();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //mandamos la consulta
        botCarga.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        consulta(num, codEstacio);
                        botCarga.setEnabled(false);
                        reload.setEnabled(true);
                        s1.setEnabled(false);
                        s2.setEnabled(false);
                        s3.setEnabled(true);
                        reload.setBackgroundColor(Color.parseColor("#00BFFF"));
                        selState.setBackgroundColor(Color.GREEN);
                        lbEstacion.setText(s2.getSelectedItem().toString());

                        break;

                    case (MotionEvent.ACTION_UP):


                        break;
                }
                return false;
            }
        });
        //una vez mandada la consulta se activa boton via 1 o 2 o ambos
        botVia1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cargamos el spinner numero 3 's3'
                List<String> trens = new ArrayList<String>();
                for (DatosTrenes d : listPetitionsTrensData1) {
                    trens.add(d.getNumDeTren());
                }
                adpNum = new ArrayAdapter<>(getAppContext(), android.R.layout.simple_list_item_1, trens);
                adpNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s3.setAdapter(adpNum);
                pulsaBotVia = true;
                via1bool = true;
                botVia1.setEnabled(false);
                botVia2.setEnabled(false);
                conState.setBackgroundColor(Color.GREEN);
                trackState.setBackgroundColor(Color.GREEN);
                botPeticionTren.setEnabled(true);
                //seleccion via
                lbVia.setText("VIA 1");
            }
        });

        botVia2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cargamos el spinner numero 3 's3'
                List<String> trens = new ArrayList<String>();
                for (DatosTrenes d : listPetitionsTrensData2) {
                    trens.add(d.getNumDeTren());
                }
                adpNum = new ArrayAdapter<>(getAppContext(), android.R.layout.simple_list_item_1, trens);
                adpNum.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                s3.setAdapter(adpNum);
                pulsaBotVia = true;
                via2bool = true;
                botVia1.setEnabled(false);
                botVia2.setEnabled(false);
                conState.setBackgroundColor(Color.GREEN);
                botPeticionTren.setEnabled(true);
                trackState.setBackgroundColor(Color.GREEN);
                //seleccion via
                lbVia.setText("VIA 2");
            }
        });


        botPeticionTren.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = MotionEventCompat.getActionMasked(event);
                numeroDeTrenSpinner = s3.getSelectedItem().toString();

                switch (action) {
                    case (MotionEvent.ACTION_DOWN):
                        //volvemos a realizar la consulta por si ha cambiado la posicion a la hora de hacerla
                        //consulta(num, codEstacio);
                        break;
                    case (MotionEvent.ACTION_UP):
                        botSetAlarm.setEnabled(true);
                        repetirConsulta();
                        break;
                }
                return false;
            }
        });

        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                terminaRepetirConsulta();
                loadApp(v);
            }
        });

        botListAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                terminaRepetirConsulta();

                AlarmasFragment fragment2 = new AlarmasFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        botSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                terminaRepetirConsulta();

                SetAlarmasFragment fragment3 = new SetAlarmasFragment();
                FragmentManager fragmentManager = getFragmentManager();
                Bundle b = new Bundle();
                b.putBoolean("via1", via1bool);
                b.putBoolean("via2", via2bool);
                b.putString("numeroTren", numeroDeTrenSpinner);
                fragment3.setArguments(b);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment3);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });
    }

    private void variablesDefecto() {
        //constantes al iniciar la activity
        numeroDeTrenSpinner = null;
        numerosObtenidos = false;
        activarViasBots = false;
        pulsaBotVia = false;
        via1bool = false;
        via2bool = false;

    }

    private DatosTrenes seguimientoTrenDatosTextViews() {
        DatosTrenes aux = null;
        DatosTrenes trenV1 = null;
        DatosTrenes trenV2 = null;

        for (DatosTrenes pos : listPetitionsTrensData1) {
            if (pos.getNumDeTren().equals(numeroDeTrenSpinner)) {
                trenV1 = pos;
            }
        }
        for (DatosTrenes pos : listPetitionsTrensData2) {
            if (pos.getNumDeTren().equals(numeroDeTrenSpinner)) {
                trenV2 = pos;
            }
        }

        if (trenV1 != null && trenV2 != null) {
            if (Long.valueOf(trenV1.getHorasTren().toString()) > Long.valueOf(trenV2.getHorasTren().toString())) {
                aux = trenV2;
            } else if (Long.valueOf(trenV1.getHorasTren().toString()) < Long.valueOf(trenV2.getHorasTren().toString())) {
                aux = trenV1;
            } else {
                Log.d("ERROR 01", "error con el retorno de las horas");
            }
        } else if (trenV1 != null && trenV2 == null) {
            aux = trenV1;
        } else if (trenV1 == null && trenV2 != null) {
            aux = trenV2;
        }

        return aux;
    }

    private void actualizarDatosTextViews2(int posicion2) {
        long time2 = Long.valueOf(listPetitionsTrensData2.get(posicion2).getHorasTren());
        lbHora.setText(String.valueOf(new SimpleDateFormat("HH:mm:ss").format(time2)));

    }

    private void actualizarDatosTextViews1(int posicion1) {

        long time1 = Long.valueOf(listPetitionsTrensData1.get(posicion1).getHorasTren());
        lbHora.setText(String.valueOf(new SimpleDateFormat("HH:mm:ss").format(time1)));
    }


    //metodo aplicable a via 1 o 2
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


    //thread consulta
    private void repetirConsulta() {

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                consulta(num, codEstacio);
                updateGUI();
            }
        }, 0, 10000);

    }

    private void updateGUI() {
        myHandler.post(myRunnable);
    }

    private void consulta(String numLin, Integer estacion) {
        String ruta = "https://tmbapi.tmb.cat/v1/metro/trains/arrivals/routes/" + numLin +
                "/stops/" + estacion +
                "?app_id=e8e58d40&app_key=a87e2200fd63a67db4492d0ddde2db6a&timestamp=1469624426082";
        new GetJSONTrens(getAppContext()).execute(ruta);
    }

    final Runnable myRunnable = new Runnable() {
        @Override
        public void run() {

            int posicionArrayVia1 = obtenerPosicion(listPetitionsTrensData1, numeroDeTrenSpinner);
            int posicionArrayVia2 = obtenerPosicion(listPetitionsTrensData2, numeroDeTrenSpinner);
            datosTrenesADevolver = seguimientoTrenDatosTextViews();
            Calendar ca = Calendar.getInstance();
            //long time = Long.valueOf(datosTrenesADevolver.getHorasTren());
            long time = Long.valueOf(Calendar.getInstance().getTimeInMillis());
            if (listPetitionsTrensData1.size() != 0 && via1bool == true) {
                actualizarDatosTextViews1(posicionArrayVia1);
            }

            if (listPetitionsTrensData2.size() != 0 && via2bool == true) {
                actualizarDatosTextViews2(posicionArrayVia2);
            }

            if (datosTrenesADevolver != null) {
                lbEstacionPet.setText(datosTrenesADevolver.getListaDeEstacion().toString());
                lbViaPet.setText("VIA " + datosTrenesADevolver.getViaT().toString());
                lbHoraPet.setText(String.valueOf(new SimpleDateFormat("HH:mm:ss").format(time)));
            }

        }
    };

    public void terminaRepetirConsulta() {
        myHandler.removeCallbacks(myRunnable);
    }


    @Override
    public void onStart() {
        super.onStart();


    }


    @Override
    public void onPause() {
        super.onPause();
        timer.cancel();
        terminaRepetirConsulta();
        loadApp(v);
    }

    @Override
    public void onStop() {
        super.onStop();
        timer.cancel();
        terminaRepetirConsulta();
        loadApp(v);
    }


    private static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }
    public static boolean hasActiveInternetConnection(Context context) {
        if (isNetworkAvailable(context)) {
            try {
                HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
                urlc.setRequestProperty("User-Agent", "Test");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(1500);
                urlc.connect();
                return (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                Log.e("NET", "Error checking internet connection", e);
            }
        } else {
            Log.d("NET", "No network available!");
        }
        return false;
    }


}
