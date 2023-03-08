package com.zasa.superduper.ApiManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.helpers.PreferencesData;
import com.zasa.superduper.retrofit.ApiEndpoints;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class stringAppManager {
    private static final String TAG = "SyncManager";



    private Activity parentActivity;

    //private WebServiceObjectClient client;

    private MyCallBack callBack;

    private JSONObject jRequestParam;

    public String selectedGridDate;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    public stringAppManager(Activity parentActivity, MyCallBack callBack) {
        this.parentActivity = parentActivity;
        this.callBack = callBack;
    }

    public void postLogin() {


        requestQueue = Volley.newRequestQueue(this.parentActivity);

        JSONObject jobj= new JSONObject();
        progressDialog = new ProgressDialog(this.parentActivity);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        try {
            jobj.put("request",jRequestParam);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        StringRequest postRequest = new StringRequest(Request.Method.GET, ApiEndpoints.CompaignURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {

                Log.d(TAG, "Success "+ s.toString());

                try {
                    JSONObject data = new JSONObject(s);
                    String dir = data.getString("dir");
//                    Log.d("dir", dir);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "Error response " + error.getMessage());
                    }
                }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String, String>();
//                params.put("tag", "login");
//                params.put("name", name);
//                params.put("password", password);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                String token = PreferencesData.getString(parentActivity, "token_id", "");

                params.put("Content-Type","application/x-www-form-urlencoded");
                params.put("Authorization", "Bearer " + token);

                return params;
            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);

        requestQueue.add(postRequest);

    }


}
