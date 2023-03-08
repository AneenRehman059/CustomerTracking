package com.zasa.superduper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.zasa.superduper.Adapters.Routes_Adapter;
import com.zasa.superduper.ApiManager.RoutesAppManager;
import com.zasa.superduper.Login.LoginActivity;
import com.zasa.superduper.Models.Routes_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class RoutesActivity extends AppCompatActivity implements MyCallBack {
    SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "mypref";
    private static final String KEY_DATE = "date";
    private static final String PREF_DATE = "prf_date";
    ArrayList<Routes_Model> operationList = new ArrayList<>();
    RecyclerView operation_rv;

    private int layout = 1;
    String api_Token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);

        getToken();

        /////// get current date and shared to splash activity and save in shared prefrence ////////
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);
        String date = sharedPreferences.getString(KEY_DATE, null);
        String current_date = getCurrentDate();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_DATE, current_date);
        editor.apply();

        operation_rv = findViewById(R.id.rv_opeartion);

        //////// this get save date in splash activty and compare with current date ///////
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, MODE_PRIVATE);

        String sharedpref_date = sharedPreferences.getString(PREF_DATE, null);

        if (current_date.equals(sharedpref_date)){

            getOperationList();
        }
        else {
            Intent intent = new Intent(RoutesActivity.this, StartDay_Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void getToken() {
        SharedPreferences sharedPreferenc = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor1 = sharedPreferenc.edit();
        api_Token = String.valueOf(editor1.putString("token_id",""));
    }

    private void getOperationList() {

        RoutesAppManager routesAppManager = new RoutesAppManager(this,this);
        JSONObject routes_params = new JSONObject();

        try {
            routes_params.put("token",api_Token);
            routesAppManager.postRoutes(routes_params);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private String getCurrentDate() {
        return new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(RoutesActivity.this, HomeActivity.class));
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        finish();
    }

    @Override
    public void notify(Object obj, String type) {
        if (type.equalsIgnoreCase("routes")){
            operationList = (ArrayList<Routes_Model>)obj;

            Routes_Adapter operation_adapter = new Routes_Adapter(operationList,this);
            operation_rv.setAdapter(operation_adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            operation_rv.setLayoutManager(layoutManager);
        }
        else
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}