package com.zasa.superduper.Login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.zasa.superduper.ApiManager.LoginAppManager;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.SharedPrefManager;
import com.zasa.superduper.activities.HomeActivity;
import com.zasa.superduper.R;
import com.zasa.superduper.helpers.PreferencesData;
import com.zasa.superduper.retrofit.ApiEndpoints;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements MyCallBack {
    public static String PREFS_NAME = "MyPrefsFile";
    TextInputEditText et_Phone, et_pass;
    CheckBox cb_RememberPass;
    String token;
    String st_Phone, st_pass;
    String selectDate;

    ProgressDialog progressDialog;
    Context context;
    SharedPreferences sharedPreferences;
    String sp_memberID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = LoginActivity.this;

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Please Wait...");

        cb_RememberPass = findViewById(R.id.login_check_box);
        et_Phone = findViewById(R.id.loginPhone);
        et_pass = findViewById(R.id.loginPass);

        sharedPreferences = context.getSharedPreferences("sharedPref", MODE_PRIVATE);
        sp_memberID = sharedPreferences.getString("Member_Unique", "");
//        sp_Member_Types = sharedPreferences.getInt("Member_Type", 1);

        SharedPreferences sp_RememberMe = getSharedPreferences("RememberMe", MODE_PRIVATE);
        String phone = sp_RememberMe.getString("uPhone", "");
        String pass = sp_RememberMe.getString("uPass", "");
        if (!phone.isEmpty() && !pass.isEmpty()) {
            et_Phone.setText(phone);
            et_pass.setText(pass);
        }

        loginPrefrence();
    }

    private void loginPrefrence() {
        SharedPreferences sharedPreferenc = getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = sharedPreferenc.edit();

        editor.putBoolean("hasLoggedIn", true);
        editor.commit();
    }

    public void Login(View view) {

        st_Phone = et_Phone.getText().toString().trim();
        st_pass = et_pass.getText().toString().trim();
//        if (st_Phone.length() <= 15) {
//            et_Phone.requestFocus();
//            et_Phone.setError("Enter correct mobile number");
//            return;
//        }

        if (st_pass.length() <= 4) {
            et_pass.requestFocus();
            et_pass.setError("Password must contains 4-digits");
            return;
        }


        LoginAppManager loginAppManager = new LoginAppManager(this,this);
        JSONObject params = new JSONObject();
        try {
            params.put("email",st_Phone);
            params.put("password",st_pass);
            loginAppManager.postLogin(params);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


//        progressDialog.show();

//        finish();

    }

    public void forgetPassword(View view) {
//        startActivity(new Intent(context, ForgetPassActivity.class));
    }

    @Override
    public void notify(Object obj, String type) {

        if(type.equalsIgnoreCase("login_token")){

            token = (String)obj;
            PreferencesData.saveString(LoginActivity.this, "token_id",token);

            Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
            startActivity(intent);
        }
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//        if (sharedPrefManager.isLoggedIn()) {
//            //   Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
//            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
//            finish();
//        }
//    }


}