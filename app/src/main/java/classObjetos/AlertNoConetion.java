package classObjetos;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Process;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import classfragments.HorariosFragment;
import classmain.MainActivity;
import gdr.tmbmetro.R;

/**
 * Created by krialo_23 on 9/15/16.
 */

public class AlertNoConetion extends DialogFragment {



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View v = LayoutInflater.from(getActivity()).inflate(R.layout.horarios_fragment, null);


        return new AlertDialog.Builder(getActivity())
                .setTitle("REVISA LA CONEXIÓN DE INTERNET, PARA FUNCIONAR NECESITA ESTAR CONECTADO")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                //getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
                                Intent intent = new Intent(getActivity(), MainActivity.class);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.putExtra("LOGOUT", true);
                                startActivity(intent);

                                getActivity().onBackPressed();

                            }
                        }
                ).create();



    }


}