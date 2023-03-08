package com.zasa.superduper.ApiManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zasa.superduper.Models.Compaign_Module_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.R;
import com.zasa.superduper.helpers.PreferencesData;
import com.zasa.superduper.retrofit.ApiEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompaignAppManager {
    private static final String TAG = "SyncManager";


    private Activity parentActivity;

    //private WebServiceObjectClient client;

    private MyCallBack callBack;

    private JSONObject jRequestParam;

    public String selectedGridDate;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    public CompaignAppManager(Activity parentActivity, MyCallBack callBack) {
        this.parentActivity = parentActivity;
        this.callBack = callBack;
    }

    public void postCompaignLogin(final JSONObject requestParams) {

        jRequestParam = requestParams;
        requestQueue = Volley.newRequestQueue(this.parentActivity);

        JSONObject jobj = new JSONObject();
        progressDialog = new ProgressDialog(this.parentActivity);
        progressDialog.setTitle("Loading");
        progressDialog.show();

        try {
            jobj.put("request", jRequestParam);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, ApiEndpoints.CompaignURL, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.has("compaigns")) {
                                // Set Api's Data Here
                                JSONArray jCompaigns = response.getJSONArray("compaigns");
                                ArrayList<Compaign_Module_Model> moduleList = new ArrayList<>();

                                for (int i = 0; i < jCompaigns.length(); i++) {
                                    JSONObject jsonObject = jCompaigns.getJSONObject(i);
                                    Compaign_Module_Model compaign = new Compaign_Module_Model(R.drawable.bottles, jCompaigns.getJSONObject(i).getString("compaign_name"));
                                    if (jsonObject.has("compaign_image"))
                                        compaign.setImage_url(jsonObject.getString("campaign_image"));
                                    moduleList.add(compaign);
                                }
//                                moduleList.add(new Daily_OperationModule_Model(R.drawable.bottles, "SKU Availability"));
//                                moduleList.add(new Daily_OperationModule_Model(R.drawable.planogram, "Planogram"));
//                                moduleList.add(new Daily_OperationModule_Model(R.drawable.gps, "GPS Check in"));
//                                moduleList.add(new Daily_OperationModule_Model(R.drawable.expiry, "Expiry Tracking"));
//                                moduleList.add(new Daily_OperationModule_Model(R.drawable.osa, "OSA (Competitor)"));
//                                moduleList.add(new Daily_OperationModule_Model(R.drawable.sku, "Secondary Gondola Display"));
//                                moduleList.add(new Daily_OperationModule_Model(R.drawable.planogram, "Outlet Facia Picture"));
//                                moduleList.add(new Daily_OperationModule_Model(R.drawable.gps, "POSM Tracking"));
//                                moduleList.add(new Daily_OperationModule_Model(R.drawable.strike, "Share to Shelf"));

                                callBack.notify(moduleList, "questions");

                            } else {

                                if (!parentActivity.isFinishing())
                                    Toast.makeText(parentActivity, response.get("message").toString(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            if (!parentActivity.isFinishing())
                                Toast.makeText(parentActivity, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        if (!parentActivity.isFinishing())
                            progressDialog.dismiss();

                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (!parentActivity.isFinishing())
                            progressDialog.dismiss();
                        String em = "";
                        if (error.getMessage() != null)
                            em = error.getMessage();
                        ArrayList<Compaign_Module_Model> moduleList = new ArrayList<>();
                        moduleList.add(new Compaign_Module_Model(R.drawable.bottles, "SKU Availability"));
                        moduleList.add(new Compaign_Module_Model(R.drawable.planogram, "Planogram"));
                        moduleList.add(new Compaign_Module_Model(R.drawable.gps, "GPS Check in"));
                        moduleList.add(new Compaign_Module_Model(R.drawable.expiry, "Expiry Tracking"));
                        moduleList.add(new Compaign_Module_Model(R.drawable.osa, "OSA (Competitor)"));
                        moduleList.add(new Compaign_Module_Model(R.drawable.sku, "Secondary Gondola Display"));
                        moduleList.add(new Compaign_Module_Model(R.drawable.planogram, "Outlet Facia Picture"));
                        moduleList.add(new Compaign_Module_Model(R.drawable.gps, "POSM Tracking"));
                        moduleList.add(new Compaign_Module_Model(R.drawable.strike, "Share to Shelf"));

                        callBack.notify(moduleList, "questions");


//                        if(!parentActivity.isFinishing())
//                            Variables.showAlert(parentActivity,"Alert","Server Connectivity error. Slow or no Internet Connection.\n"+em);
                    }
                }
        ) {
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = PreferencesData.getString(parentActivity, "token_id", "");
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json; charset=UTF-8");
//				params.put("token", ACCESS_TOKEN);
                params.put("Authorization", "Bearer " + token);
                return params;

            }
        };
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsonObjectRequest.setRetryPolicy(policy);

        requestQueue.add(jsonObjectRequest);

    }
}
