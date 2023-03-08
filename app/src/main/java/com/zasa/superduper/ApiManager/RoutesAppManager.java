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
import com.zasa.superduper.Models.Routes_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.helpers.PreferencesData;
import com.zasa.superduper.retrofit.ApiEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoutesAppManager {
    private static final String TAG = "SyncManager";


    private Activity parentActivity;

    //private WebServiceObjectClient client;

    private MyCallBack callBack;

    private JSONObject jRequestParam;

    public String selectedGridDate;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    public RoutesAppManager(Activity parentActivity, MyCallBack callBack) {
        this.parentActivity = parentActivity;
        this.callBack = callBack;
    }

    public void postRoutes(final JSONObject requestParams) {

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(com.android.volley.Request.Method.GET, ApiEndpoints.GetRoutesURL, null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response.has("routes")){
                                // Set Api's Data Here
                                JSONArray jSurveys = response.getJSONArray("routes");
                                ArrayList<Routes_Model> operationList = new ArrayList<>();
                                for (int i = 0; i < jSurveys.length(); i++) {
                                    Routes_Model routes_model = new Routes_Model(jSurveys.getJSONObject(i).getString("route_name"));
                                    routes_model.setRoute_id(jSurveys.getJSONObject(i).getString("route_id"));
//                                    JSONObject jsonObject =
//                                    jCompaigns.getJSONObject(i).getString("compaign_name");
                                    operationList.add(routes_model);



                                }

                                callBack.notify(operationList,"routes");
//                                callBack.notify(response.getJSONObject("user_info"),"login");
                            }else{

                                if(!parentActivity.isFinishing())
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
                        ArrayList<Routes_Model> operationList = new ArrayList<>();
                        operationList.add(new Routes_Model("Gulberg"));
                        operationList.add(new Routes_Model("Johar Town "));
                        operationList.add(new Routes_Model("Defense Phase I"));
                        operationList.add(new Routes_Model("Mughal Pura"));
                        operationList.add(new Routes_Model("BaghbanPura"));
                        operationList.add(new Routes_Model("Paragon City"));
                        operationList.add(new Routes_Model("Lahore Cant"));
                        operationList.add(new Routes_Model("Model Town"));

                        callBack.notify(operationList, "routes");


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
