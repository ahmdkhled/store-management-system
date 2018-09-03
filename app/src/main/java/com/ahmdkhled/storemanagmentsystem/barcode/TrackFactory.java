package com.ahmdkhled.storemanagmentsystem.barcode;

import android.util.Log;


import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.barcode.Barcode;

public class TrackFactory implements MultiProcessor.Factory<Barcode> {

    BarcodeTracker.BarcodeDetectorListener listener;

    public TrackFactory(BarcodeTracker.BarcodeDetectorListener listener) {
        this.listener = listener;
    }

    @Override
    public Tracker<Barcode> create(Barcode barcode) {
        Log.d("BARCODE","traker factory");
        return new BarcodeTracker(listener);
    }
}
