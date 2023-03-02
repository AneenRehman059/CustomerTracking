package com.zasa.superduper.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.zasa.superduper.R;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerViewActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{
    Context context;
    ProgressDialog progressDialog;
    public static TextView code;
    View view;
    ZXingScannerView zXingScannerView;
    ImageView flashOn, flashOff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner_view);

        context = ScannerViewActivity.this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait...");

        flashOn = findViewById(R.id.flashOn);
        flashOff = findViewById(R.id.flashOff);
        zXingScannerView = findViewById(R.id.zxing);

        requestForCameraPermission();

    }

    @Override
    public void handleResult(Result rawResult) {
        code.setText(rawResult.getText());
//        Toast.makeText(context, "" + code, Toast.LENGTH_SHORT).show();
//
//        Intent intent = new Intent(context, QuestionActivity.class);
//        intent.putExtra("code", code);
//        startActivity(intent);
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        zXingScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zXingScannerView.setResultHandler(this);
        zXingScannerView.setAspectTolerance(0.2f);

        //zXingScannerView.setAutoFocus(true);
        //zXingScannerView.setSoundEffectsEnabled(true);
        requestForCameraPermission();
    }

    private void requestForCameraPermission() {
        Dexter.withContext(getApplicationContext())  //Dexter is used for runtime permission
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        zXingScannerView.setResultHandler(ScannerViewActivity.this);
                        zXingScannerView.startCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                        requestForCameraPermission();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();  //ask continuously for permission utill he grant permission
                    }
                }).check();
    }
    public void flashOn(View view) {

        zXingScannerView.setFlash(true);
        flashOn.setVisibility(View.GONE);
        flashOff.setVisibility(View.VISIBLE);
    }

    public void flashOff(View view) {
        zXingScannerView.setFlash(false);
        flashOn.setVisibility(View.VISIBLE);
        flashOff.setVisibility(View.GONE);
    }

    private void restartScanner() {
        zXingScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        zXingScannerView.resumeCameraPreview(ScannerViewActivity.this);
                    }
                }, 1000);


//                zXingScannerView.setResultHandler(ScannerViewActivity.this);
//                zXingScannerView.startCamera();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}