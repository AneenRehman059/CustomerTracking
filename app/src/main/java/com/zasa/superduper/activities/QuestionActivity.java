package com.zasa.superduper.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.github.florent37.runtimepermission.RuntimePermission;
import com.zasa.superduper.Adapters.AdapterShowImages;
import com.zasa.superduper.Adapters.Question_Adapter;
import com.zasa.superduper.ApiManager.QuestionsAppManager;
import com.zasa.superduper.Login.LoginActivity;
import com.zasa.superduper.Models.Question_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.MyFunctions;
import com.zasa.superduper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity implements MyCallBack {
    public static  AdapterShowImages selectedRow;
    Button btn_submit;

    RecyclerView questionsRv;
    ArrayList<Question_Model> questionList = new ArrayList<>();

    private final int REQUEST_CAMERA = 1234;
    private final int REQUEST_GALLERY = 5464;
    private static MyFunctions myFunctions;
    String apiTokens;

    private static String imagePath = "";
    private ArrayList<String> arrayList = new ArrayList<>();
    static AdapterShowImages adapterShowImages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        SharedPreferences sharedPreferenc = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferenc.edit();
        apiTokens = String.valueOf(editor.putString("token_id",""));

        questionsRv = findViewById(R.id.rv_question);
        btn_submit = findViewById(R.id.btn_submit);


        getQuestionList();
    }

    private void getQuestionList() {

        QuestionsAppManager questionsAppManager = new QuestionsAppManager(this,this);
        JSONObject question_params = new JSONObject();
        try {
            question_params.put("token",apiTokens);
            questionsAppManager.postQuestions(question_params);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
        questionsAppManager.postQuestions(new JSONObject());

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ((Question_Adapter)questionsRv.getAdapter()).getQuestionList();
                Intent intent = new Intent(QuestionActivity.this,ShowQuestionListActivity.class);
                intent.putExtra("myList", ((Question_Adapter) questionsRv.getAdapter()).getQuestionList());
                startActivity(intent);
            }
        });

    }

//    public void onTopbuttonClick(View v){
//        ((Question_Adapter)questionsRv.getAdapter()).getQuestionList();
//        // yay agay activty main pass krain gay
//
//    }

    private void getImage() {
        myFunctions= new MyFunctions(this);
        final CharSequence[] items;
        try {
            items = new CharSequence[]{"Take Photo", "Choose Image", "Cancel"};
            AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
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

    @Override
    public void notify(Object obj, String type) {

        if(type.equalsIgnoreCase("questions")){

            questionList = (ArrayList<Question_Model>)obj;

            Question_Adapter question_adapter = new Question_Adapter(questionList, this,this);

            questionsRv.setAdapter(question_adapter);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            questionsRv.setLayoutManager(layoutManager);

        }
        else
        {
            getImage();
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

        selectedRow.updateList(actualImagePath);

    }

}