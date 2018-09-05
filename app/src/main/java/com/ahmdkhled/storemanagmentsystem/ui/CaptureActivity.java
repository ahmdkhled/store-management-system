package com.ahmdkhled.storemanagmentsystem.ui;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.ahmdkhled.storemanagmentsystem.R;
import com.ahmdkhled.storemanagmentsystem.barcode.BarcodeTracker;
import com.ahmdkhled.storemanagmentsystem.barcode.TrackFactory;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptureActivity extends AppCompatActivity implements BarcodeTracker.BarcodeDetectorListener {


    CameraSource cameraSource;
    SurfaceView surfaceView;
    MediaPlayer mediaPlayer;

    @BindView(R.id.done)
    Button doneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        ButterKnife.bind(this);
        doneBtn.setClickable(false);

        surfaceView = findViewById(R.id.surface_view);



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
    public void onObjectDetected(final Barcode barcode) {
        mediaPlayer.start();
        Log.d("BARCODE_RES", barcode.displayValue);
        doneBtn.setClickable(true);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CaptureActivity.this);
                String[]options = {"Add New Product","View Product Details"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0){
                            // add new product
                            Intent addIntent = new Intent(CaptureActivity.this,AddProductActivity.class);
                            addIntent.putExtra("add_extra",barcode.displayValue);
                            startActivity(addIntent);
                        }else if(i == 1){
                            // view product details
                            Intent detailIntent = new Intent(CaptureActivity.this,ProductDetail.class);
                            detailIntent.putExtra("detail_extra",barcode.displayValue);
                            startActivity(detailIntent);
                        }
                    }
                });
                builder.show();
            }
        });

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