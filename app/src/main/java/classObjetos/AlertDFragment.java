package classObjetos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import classfragments.AlarmasFragment;
import classfragments.HorariosFragment;
import gdr.tmbmetro.R;



public class AlertDFragment extends DialogFragment {



	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final View v = LayoutInflater.from(getActivity()).inflate(R.layout.horarios_fragment, null);
		Fecha fecha = null;
		fecha = lecturaFicheroAlarmas();
		if (fecha != null) {

			return new AlertDialog.Builder(getActivity())
					.setTitle("ALARMA GUARDADA: " + fecha.getDia() + " -- " + fecha.getHora())
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									//getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
									HorariosFragment fragment2 = new HorariosFragment();
									FragmentManager fragmentManager = getFragmentManager();
									FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
									fragmentTransaction.replace(R.id.fragment_container, fragment2);
									fragmentTransaction.addToBackStack(null);
									fragmentTransaction.commit();
								}
							}
					).create();
		} else {
			return new AlertDialog.Builder(getActivity())
					.setTitle("NO HAY NINGUNA ALARMA GUARDADA")
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									//getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, getActivity().getIntent());
									HorariosFragment fragment2 = new HorariosFragment();
									FragmentManager fragmentManager = getFragmentManager();
									FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
									fragmentTransaction.replace(R.id.fragment_container, fragment2);
									fragmentTransaction.addToBackStack(null);
									fragmentTransaction.commit();
								}
							}
					).create();
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