package com.ahmdkhled.storemanagmentsystem.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Button;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.barcode.BarcodeTracker;
import com.ahmdkhled.storemanagmentsystem.barcode.TrackFactory;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

public class CaptureActivity extends AppCompatActivity implements BarcodeTracker.BarcodeDetectorListener {


    CameraSource cameraSource;
    SurfaceView surfaceView;
    Button done;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);


        surfaceView = findViewById(R.id.surface_view);
        done = findViewById(R.id.done);


        mediaPlayer = MediaPlayer.create(this, R.raw.beep);

        TrackFactory trackFactory = new TrackFactory(this);
        BarcodeDetector barcodeDetector = new
                BarcodeDetector.Builder(this).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1600, 1024)
                .setAutoFocusEnabled(true)
                .build();


        barcodeDetector.setProcessor(new MultiProcessor.Builder<>(trackFactory).build());

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    cameraSource.start(surfaceView.getHolder());
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSource.stop();
            }
        });


    }


    @Override
    public void onObjectDetected(Barcode barcode) {
        mediaPlayer.start();
        Log.d("BARCODE_RES", barcode.displayValue);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraSource != null){
            cameraSource.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (cameraSource != null) {
            cameraSource.stop();
        }
    }
}
