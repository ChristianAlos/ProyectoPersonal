package classfragments;

import android.app.AlarmManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import classObjetos.AlarmLab;
import gdr.tmbmetro.R;

/**
 * Created by krialo_23 on 8/24/16.
 */

public class AlarmasFragment extends Fragment {

    private Button butBack, butDel;
    private RecyclerView mAlarmaReciclewView;
    private AlarmAdapter mAdapter;
    private AlarmManager alarmManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alarmas_lista, container, false);
        mAlarmaReciclewView = (RecyclerView) view.findViewById(R.id.alarma_recycler_view);
        butBack = (Button) view.findViewById(R.id.butBack);
        butDel = (Button) view.findViewById(R.id.butDel);
        mAlarmaReciclewView.setLayoutManager(new LinearLayoutManager(getActivity()));
        butDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaciarContenidoAlarmas();
                AlarmLab.get(getContext()).pararAlarmas();
                Log.d("Parar", "por aqui paso");
                updateUI();
            }
        });
        butBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cargar fragment Horarios
                HorariosFragment fragment2 = new HorariosFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, fragment2);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        updateUI();
        return view;
    }
    private void updateUI(){
        AlarmLab al = AlarmLab.get(getActivity());
        List<String> alarmas = al.getAlarmas();
        alarmas.clear();

        if (mAdapter == null){
            mAdapter = new AlarmAdapter(alarmas);
            lecturaFicheroAlarmas();
            mAlarmaReciclewView.setAdapter(mAdapter);
        } else {
            lecturaFicheroAlarmas();
            mAdapter.notifyDataSetChanged();
        }

        //mAdapter = new AlarmAdapter(alarmas);
        //mAlarmaReciclewView.setAdapter(mAdapter);

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
    private void lecturaFicheroAlarmas (){
        AlarmLab al = AlarmLab.get(getActivity());

        String s;
        File root = Environment.getExternalStorageDirectory();
        if (root.canRead()) {
            File dir = new File(root + "/alarmasTMB");
            dir.mkdir();
            File dataFile = new File(dir, "alarmas.txt");
            try (BufferedReader lector = new BufferedReader(new FileReader(dataFile))) {
                while ((s = lector.readLine())!= null){
                    al.setmAlarmas(s);
                }
            } catch (IOException e) {
                e.getMessage();
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }


    private class AlarmHolder extends RecyclerView.ViewHolder{

        public TextView mTituloAlarma;

        public AlarmHolder(View itemView) {
            super(itemView);

            mTituloAlarma = (TextView) itemView;
        }
    }

    private class AlarmAdapter extends RecyclerView.Adapter<AlarmHolder> {

        private List<String> mAlarmas;

        public AlarmAdapter (List<String> alrm){
            mAlarmas = alrm;
        }

        @Override
        public AlarmHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(android.R.layout.simple_list_item_1, parent, false);

            return new AlarmHolder(view);
        }

        @Override
        public void onBindViewHolder(AlarmHolder holder, int position) {
            String alm = mAlarmas.get(position);
            holder.mTituloAlarma.setText(alm);
        }

        @Override
        public int getItemCount() {
            return mAlarmas.size();
        }
    }
}
