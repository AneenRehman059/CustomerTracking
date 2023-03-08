package com.zasa.superduper.ApiManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.retrofit.ApiEndpoints;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginAppManager {
    private static final String TAG = "SyncManager";



    private Activity parentActivity;

    //private WebServiceObjectClient client;

    private MyCallBack callBack;

    private JSONObject jRequestParam;

    public String selectedGridDate;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    public LoginAppManager(Activity parentActivity, MyCallBack callBack) {
        this.parentActivity = parentActivity;
        this.callBack = callBack;
    }

    public void postLogin(final JSONObject requestParams) {

        jRequestParam = requestParams;
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.POST, ApiEndpoints.SignInURL,jRequestParam,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response.has("token")){


//                                PreferencesData.saveString(parentActivity,"token",response.get("token").toString());
                                //PreferencesData.saveString(parentActivity,"password",jRequestParam.get("password").toString());

                                //response.getJSONObject("user_info");

                                callBack.notify(response.getString("token"),"login_token");
                            }else{

                                if(!parentActivity.isFinishing())
                                    Toast.makeText(parentActivity, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            if(!parentActivity.isFinishing())
                                Toast.makeText(parentActivity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if(!parentActivity.isFinishing())
                            progressDialog.dismiss();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(!parentActivity.isFinishing())
                            progressDialog.dismiss();
                        String em="";
                        if(error.getMessage()!=null)
                            em=error.getMessage();

                        if(!parentActivity.isFinishing())
                            Toast.makeText(parentActivity, "Server Connectivity Error", Toast.LENGTH_SHORT).show();
//                            ApiEndpoints.showAlert(parentActivity,"Alert","Server Connectivity error. Slow or no Internet Connection.\n"+em);
                    }
                }
        );
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        requestQueue.add(jsonObjectRequest);

    }


}
