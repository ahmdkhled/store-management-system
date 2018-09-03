package com.ahmdkhled.storemanagmentsystem.barcode;

import android.util.Log;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

public class BarcodeTracker extends Tracker<Barcode> {

    private BarcodeDetectorListener listener;


    public BarcodeTracker(BarcodeDetectorListener listener) {
        this.listener = listener;
    }



    @Override
    public void onNewItem(int i, Barcode barcode) {
        Log.d("BARCODE","onNewItem");
        listener.onObjectDetected(barcode);
    }

    @Override
    public void onUpdate(Detector.Detections<Barcode> detections, Barcode barcode) {

    }

    public interface BarcodeDetectorListener{
        void onObjectDetected(Barcode barcode);
    }
}
