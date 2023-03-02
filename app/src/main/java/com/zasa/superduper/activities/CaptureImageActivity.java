package com.zasa.superduper.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.github.florent37.runtimepermission.RuntimePermission;
import com.zasa.superduper.Adapters.AdapterShowImages;
import com.zasa.superduper.MyFunctions;
import com.zasa.superduper.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class CaptureImageActivity extends AppCompatActivity {
    //    public static final int CAMERA_PERM_CODE = 101;
//    public static final int CAMERA_REQUEST_CODE = 102;
//    private static  TAG = "CaptureImageActivity";
    private Button btnAdd;
    private RecyclerView recyclerView;
    private final int REQUEST_CAMERA = 1234;
    private final int REQUEST_GALLERY = 5464;
    private static MyFunctions myFunctions;

    private static String imagePath = "";
    private ArrayList<String> arrayList = new ArrayList<>();
    static AdapterShowImages adapterShowImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_item);
        btnAdd = findViewById(R.id.btn_captureImage);
        recyclerView = findViewById(R.id.item_images_rcv);
        myFunctions = new MyFunctions(this);

//        btnAdd.setOnClickListener(view -> {
//            getImage();
//        });
        getImage();
        populateList();

    }

    private void getImage() {
        final CharSequence[] items;
        try {
            items = new CharSequence[]{"Take Photo", "Choose Image", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(CaptureImageActivity.this);
            builder.setCancelable(false);
            builder.setTitle("Select Image");
            builder.setItems(items, (dialogInterface, i) -> {
                if (items[i].equals("Take Photo")) {
                    RuntimePermission.askPermission(this)
                            .request(Manifest.permission.CAMERA)
                            .onAccepted(result -> {
                                takePicture();
                            })
                            .onDenied(result -> {
                                new android.app.AlertDialog.Builder(this)
                                        .setMessage("Please accept our permissions")
                                        .setPositiveButton("yes", (dialog1, which) -> result.askAgain()) // ask again
                                        .setNegativeButton("no", (dialog1, which) -> dialog1.dismiss())
                                        .show();
                            })
                            .ask();
                } else if (items[i].equals("Choose Image")) {
                    RuntimePermission.askPermission(this)
                            .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .onAccepted(result -> {
                                onClickGallery();
                            })
                            .onDenied(result -> {
                                new android.app.AlertDialog.Builder(this)
                                        .setMessage("Please accept our permissions")
                                        .setPositiveButton("yes", (dialog1, which) -> result.askAgain()) // ask again
                                        .setNegativeButton("no", (dialog1, which) -> dialog1.dismiss())
                                        .show();
                            })
                            .ask();
                } else {
                    dialogInterface.dismiss();
                }
            });
            builder.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        try {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, myFunctions.setImageUri());
            takePictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(takePictureIntent, REQUEST_CAMERA);
        } catch (Exception e) {
            Log.d("tag", "takeImageIssue " + e.toString());
        }
    }


    private void onClickGallery() {
        Intent intent = new Intent();
        intent.setType("image/jpg");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            new captureImageAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myFunctions.getImagePath());
        } else if (requestCode == REQUEST_GALLERY && resultCode == Activity.RESULT_OK) {
            new captureImageAsync1().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, myFunctions.getPath(data.getData(), this));
        }

    }

    @SuppressLint("StaticFieldLeak")
    public class captureImageAsync extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            imagePath = strings[0];
            try {
                return myFunctions.getRightAngleImage(imagePath);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return imagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // you can show progress bar here while image loading/fetching
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            getImagePathN(myFunctions.decodeFile(imagePath));

            //Close progress bar
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class captureImageAsync1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            imagePath = strings[0];
            try {
                return myFunctions.getRightAngleImage(imagePath);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return imagePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // you can show progress bar here
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // setImageUsingSwitch(PubFun.decodeFile(imagePath));
            getImagePathN(myFunctions.decodeFile(imagePath));

            //Close progress bar

        }
    }

    private void getImagePathN(Bitmap bitmap) {
        Uri tempUri = myFunctions.getImageUri(this, bitmap);
        String actualImagePath = myFunctions.getRealPathFromURI(tempUri, this);

        Log.d("tag", "image path : " + actualImagePath);

        adapterShowImages.updateList(actualImagePath);

    }

    private void populateList() {
//        adapterShowImages = new AdapterShowImages(CaptureImageActivity.this, arrayList);
//        recyclerView.setLayoutManager(new GridLayoutManager(CaptureImageActivity.this, 3));   //ctl+p to know parameters
//        recyclerView.setAdapter(adapterShowImages);
    }
}