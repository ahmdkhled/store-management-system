package com.ahmdkhled.storemanagmentsystem.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
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

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CaptureActivity extends AppCompatActivity implements BarcodeTracker.BarcodeDetectorListener {


    CameraSource cameraSource;
    SurfaceView surfaceView;
    @BindView(R.id.done)
    Button doneBtn;
    String source;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        ButterKnife.bind(this);

        surfaceView = findViewById(R.id.surface_view);


        if (getIntent()!=null&&getIntent().hasExtra("source")){
            source =getIntent().getStringExtra("source");
        }

        if (source.equals("AddProductActivity")){
            doneBtn.setVisibility(View.GONE);
        }

        handleBarcode();

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    void handleBarcode(){
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
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {

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
        Log.d("BARCODE_RES", barcode.displayValue);
        if (source.equals("AddProductActivity")){
            Log.d("onBarcodeDetected", "value is "+barcode.displayValue);
            EventBus.getDefault().post(new AddProductActivity.BarcodeDetectedEvent(barcode.displayValue));
            finish();
        }else if (source.equals("OrderActivity")){
            //Log.d("onBarcodeDetected", "value is :) "+barcode.displayValue);
            EventBus.getDefault().post(new OrderActivity.BarcodeDetectionEvent(barcode.displayValue));
        }else if (source.equals("ProductsActivity")){
            EventBus.getDefault().post(new ProductsActivity.OnProductDetected(barcode.displayValue));
            finish();
        }

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
