package classJSON;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import classObjetos.DatosTrenes;
import static classstaic.Skeleton.botVia1;
import static classstaic.Skeleton.botVia2;
import static classstaic.Skeleton.infoTrenes;

import static classstaic.Skeleton.internetNoOk;
import static classstaic.Skeleton.listPetitionsTrensData1;

import static classstaic.Skeleton.listPetitionsTrensData2;
import static classstaic.Skeleton.listilla;
import static classstaic.Skeleton.pulsaBotVia;

/**
 * Created by krialo_23 on 8/1/16.
 */

public class GetJSONTrens extends AsyncTask<String, Void, List> {
    private Context context;
    HttpURLConnection con;



    @Override
    protected List doInBackground(String... strings) {
        URL url;
        List arr = new ArrayList();
        HttpURLConnection urlConnection = null;
        System.out.print("Me llamo");
        try {
            url = new URL(strings[0]);

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

            arr.add(resString);
            arr.add(strings[0]);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return arr;
    }

    protected void onPostExecute(List stream) {


        try {
            JSONObject reader = new JSONObject(stream.get(0).toString());
            try {

                listPetitionsTrensData1 = new ArrayList<>();
                listPetitionsTrensData2 = new ArrayList<>();

                LeerJson lector = new LeerJson();
                //lector.leerTrenes(reader, stream.get(0).toString());
                infoTrenes = lector.leerTrenes(reader, stream.get(0).toString());

                listilla = new ArrayList<>();
                listilla.addAll(infoTrenes);

                //obtenemos todos los datos

                //trens de via1
                for (DatosTrenes d1: listilla.get(0)) {
                    listPetitionsTrensData1.add(d1);
                }
                //trens de via2
                for (DatosTrenes d2: listilla.get(1)) {
                    listPetitionsTrensData2.add(d2);
                }

                //preguntamos cual de los dos arrays esta vacio si lo esta
                if (listPetitionsTrensData1.size() != 0){
                    if (pulsaBotVia == false) {
                        botVia1.setEnabled(true);
                    }
                }
                if (listPetitionsTrensData2.size() != 0){
                    if (pulsaBotVia == false) {
                        botVia2.setEnabled(true);
                    }
                }
                pulsaBotVia = true;


            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public GetJSONTrens(Context applicationContext) {

        this.context = applicationContext;
    }



}