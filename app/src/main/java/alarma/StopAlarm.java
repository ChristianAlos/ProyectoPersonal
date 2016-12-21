package alarma;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.NotificationCompat;

import java.io.IOException;


/**
 * Created by krialo_23 on 9/10/16.
 */

public class StopAlarm extends Activity {
    private MediaPlayer mMediaPlayer;
    private static Activity activity;

    private Vibrator vib;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(2000);
        activity = this;
        new android.support.v7.app.AlertDialog.Builder(this)
                .setTitle("Alarma")
                .setMessage("Tu tren esta apunto de llegar...")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete

                        mMediaPlayer.stop();
                        activity.finish();

                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

        playSound(this, getAlarmUri());


    }

    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //Get an alarm sound. Try for an alarm. If none set, try notification,
    //Otherwise, ringtone.
    private Uri getAlarmUri() {
        Uri alert = RingtoneManager
                .getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alert == null) {
            alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (alert == null) {
                alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        return alert;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activity.finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.finish();
    }
}