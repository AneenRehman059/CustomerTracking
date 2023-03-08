package com.zasa.superduper.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.zasa.superduper.Adapters.Compaign_Adapter;
import com.zasa.superduper.ApiManager.CompaignAppManager;
import com.zasa.superduper.Login.LoginActivity;
import com.zasa.superduper.Models.Compaign_Module_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Compaign_Activity extends AppCompatActivity implements MyCallBack {
    RecyclerView rv_daily_operation_mod;
    int count = 0;
    String apiToken;
    ArrayList<Compaign_Module_Model> moduleList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);

        SharedPreferences sharedPreferenc = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferenc.edit();
        apiToken = String.valueOf(editor.putString("token_id",""));

        rv_daily_operation_mod = findViewById(R.id.rv_operation_module_items);

        setModuleItem();
    }

    private void setModuleItem() {


        CompaignAppManager checkOutAppManager = new CompaignAppManager(this,this);
        JSONObject compaign_params = new JSONObject();
        try {
            compaign_params.put("token",apiToken);
            checkOutAppManager.postCompaignLogin(compaign_params);
//            Toast.makeText(this, apiToken, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

//        stringAppManager checkOutAppManager = new stringAppManager(this,this);
//            checkOutAppManager.postLogin();


    }

    @Override
    public void notify(Object obj, String type) {
        if (type.equalsIgnoreCase("questions")){
            moduleList = (ArrayList<Compaign_Module_Model>)obj;

            Compaign_Adapter moduleAdapter = new Compaign_Adapter(moduleList, this);
            rv_daily_operation_mod.setAdapter(moduleAdapter);

            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
            rv_daily_operation_mod.setLayoutManager(gridLayoutManager);
        }
        else
        {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        }
    }
}