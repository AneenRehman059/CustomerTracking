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
import com.zasa.superduper.Models.Question_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.helpers.PreferencesData;
import com.zasa.superduper.retrofit.ApiEndpoints;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionsAppManager {

    private static final String TAG = "SyncManager";



    private Activity parentActivity;

    //private WebServiceObjectClient client;

    private MyCallBack callBack;

    private JSONObject jRequestParam;

    public String selectedGridDate;
    RequestQueue requestQueue;
    ProgressDialog progressDialog;

    public QuestionsAppManager(Activity parentActivity, MyCallBack callBack) {
        this.parentActivity = parentActivity;
        this.callBack = callBack;
    }

    public void postQuestions(final JSONObject requestParams) {

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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, ApiEndpoints.QuestionURL,null,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if(response.has("questions")){

                                // Set Api's Data Here
                                JSONArray jQuestions = response.getJSONArray("questions");
                                ArrayList<Question_Model> questionList = new ArrayList<>();
                                for (int i = 0; i < jQuestions.length(); i++) {
//                                    JSONObject jsonObject =
//                                    jCompaigns.getJSONObject(i).getString("compaign_name");
                                    questionList.add(new Question_Model(jQuestions.getJSONObject(i).getString("question_name"),jQuestions.getJSONObject(i).getString("question_type")));

                                }

                                callBack.notify(questionList,"questions");
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
                        ArrayList<Question_Model> questionList = new ArrayList<>();
                        questionList.add(new Question_Model("Rose Patel","text"));
                        questionList.add(new Question_Model("Tulip","picture"));
                        questionList.add(new Question_Model("Maxob","qr"));
                        questionList.add(new Question_Model("Competitors","text"));
                        questionList.add(new Question_Model("Competitors","picture"));
                        questionList.add(new Question_Model("Competitors","qr"));
                        questionList.add(new Question_Model("Rose Patel","text"));
                        questionList.add(new Question_Model("Rose Patel","picture"));
                        questionList.add(new Question_Model("Rose Patel","qr"));
                        callBack.notify(questionList,"questions");


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
