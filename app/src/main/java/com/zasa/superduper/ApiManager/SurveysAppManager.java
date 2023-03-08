package com.zasa.superduper.ApiManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.zasa.superduper.Models.Category_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.helpers.PreferencesData;
import com.zasa.superduper.retrofit.ApiEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SurveysAppManager {
    private static final String TAG = "SyncManager";



    private Activity parentActivity;

    //private WebServiceObjectClient client;

    private MyCallBack callBack;

    private JSONObject jRequestParam;

    public String selectedGridDate;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    public SurveysAppManager(Activity parentActivity, MyCallBack callBack) {
        this.parentActivity = parentActivity;
        this.callBack = callBack;
    }

    public void postSurveys(final JSONObject requestParams) {

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiEndpoints.SurveysURL,null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response.has("surveys")){
                                // Set Api's Data Here
                                JSONArray jSurveys = response.getJSONArray("surveys");
                                ArrayList<Category_Model> catList = new ArrayList<>();
                                for (int i = 0; i < jSurveys.length(); i++) {
//                                    JSONObject jsonObject =
//                                    jCompaigns.getJSONObject(i).getString("compaign_name");
                                    catList.add(new Category_Model(jSurveys.getJSONObject(i).getString("survey_name")));
                                }

                                callBack.notify(catList,"surveys");
//                                callBack.notify(response.getJSONObject("user_info"),"login");
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
                        ArrayList<Category_Model> catList = new ArrayList<>();
                        catList.add(new Category_Model("Rose Patel"));
                        catList.add(new Category_Model("Tulip"));
                        catList.add(new Category_Model("Maxob"));
                        catList.add(new Category_Model("Competitors"));
                        catList.add(new Category_Model("Competitors"));
                        catList.add(new Category_Model("Competitors"));
                        catList.add(new Category_Model("Rose Patel"));
                        callBack.notify(catList,"surveys");


//                        if(!parentActivity.isFinishing())
//                            Variables.showAlert(parentActivity,"Alert","Server Connectivity error. Slow or no Internet Connection.\n"+em);
                    }
                }
        ){
            //This is for Headers If You Needed
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token  = PreferencesData.getString(parentActivity, "token_id","");
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
