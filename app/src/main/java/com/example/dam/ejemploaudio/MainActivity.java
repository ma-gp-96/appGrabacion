package com.example.dam.ejemploaudio;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.media.MediaPlayer.OnCompletionListener;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements OnCompletionListener{
    TextView tv1;
    MediaRecorder recorder;
    MediaPlayer player;
    File archivo;
    Button b1, b2, b3;
    private final int PHOTO_CODE = 200;
    private AppCompatActivity yo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        yo = this;
        tv1 = (TextView) this.findViewById(R.id.tv1);
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button1);
        b3 = (Button) findViewById(R.id.button2);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    public void grabar(View v) {
        if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                adb.setMessage("El permiso es para accceder al microfono");
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(yo,
                                new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                123);
                    }
                });
            }
            else {
                ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},123);
            }

        }
        else {
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)
            {
                if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.RECORD_AUDIO)){
                    AlertDialog.Builder adb = new AlertDialog.Builder(this);
                    adb.setMessage("El permiso es para guardar el audio");
                    adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(yo,
                                    new String[]{android.Manifest.permission.RECORD_AUDIO},124);
                        }
                    });
                }
                else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{android.Manifest.permission.RECORD_AUDIO},124);
                }
            }
            else{
                recorder = new MediaRecorder();
                recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                File path = new File(Environment.getExternalStorageDirectory()
                        .getPath());
                try {
                    archivo = File.createTempFile("temporal", ".3gp", path);
                } catch (IOException e) {
                }
                recorder.setOutputFile(archivo.getAbsolutePath());
                try {
                    recorder.prepare();
                } catch (IOException e) {
                }
                recorder.start();
                tv1.setText("Grabando");
                b1.setEnabled(false);
                b2.setEnabled(true);
            }
        }

    }

    public void detener(View v) {
        recorder.stop();
        recorder.release();
        player = new MediaPlayer();
        player.setOnCompletionListener(this);
        try {
            player.setDataSource(archivo.getAbsolutePath());
        } catch (IOException e) {
        }
        try {
            player.prepare();
        } catch (IOException e) {
        }
        b1.setEnabled(true);
        b2.setEnabled(false);
        b3.setEnabled(true);
        tv1.setText("Listo para reproducir");
    }

    public void reproducir(View v) {
        player.start();
        b1.setEnabled(false);
        b2.setEnabled(false);
        b3.setEnabled(false);
        tv1.setText("Reproduciendo");
    }

    public void onCompletion(MediaPlayer mp) {
        b1.setEnabled(true);
        b2.setEnabled(true);
        b3.setEnabled(true);
        tv1.setText("Listo");
    }
}


