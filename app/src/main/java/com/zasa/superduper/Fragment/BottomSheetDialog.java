package com.zasa.superduper.Fragment;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.zasa.superduper.Adapters.ShopList_Adapter;
import com.zasa.superduper.ApiManager.ShopAppManager;
import com.zasa.superduper.Login.LoginActivity;
import com.zasa.superduper.Models.ShopList_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.R;
import com.zasa.superduper.activities.TrackOperationPlaceActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class BottomSheetDialog extends BottomSheetDialogFragment implements MyCallBack {
    RecyclerView recyclerView_shopList;
    ArrayList<ShopList_Model> shopList = new ArrayList<>();
    String api_Token;
    public BottomSheetDialog() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);

        recyclerView_shopList = view.findViewById(R.id.rv_shopList);

        SharedPreferences sharedPreferenc = getActivity().getSharedPreferences(LoginActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor1 = sharedPreferenc.edit();
        api_Token = String.valueOf(editor1.putString("token_id",""));

        getShopList();

        return view;
    }

    private void getShopList() {
        ShopAppManager shopAppManager = new ShopAppManager(getActivity(),this);
        JSONObject shop_params = new JSONObject();

//        ShopList_Adapter shopList_adapter = new ShopList_Adapter(shopList,getContext());
//        recyclerView_shopList.setAdapter(shopList_adapter);

        try {
//            TrackOperationPlaceActivity act = (TrackOperationPlaceActivity) getActivity();
            shop_params.put("route_id", TrackOperationPlaceActivity.routeId);
            shopAppManager.postRoutes(shop_params);
        }
        catch (JSONException e) {
            throw new RuntimeException(e);
        }

//        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
//        recyclerView_shopList.setLayoutManager(layoutManager);

//        shopList_adapter.notifyDataSetChanged();

    }

    @Override
    public void notify(Object obj, String type) {
        if (type.equalsIgnoreCase("shops")){
            shopList = (ArrayList<ShopList_Model>)obj;

            ShopList_Adapter shopList_adapter = new ShopList_Adapter(shopList,getContext());
            recyclerView_shopList.setAdapter(shopList_adapter);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            recyclerView_shopList.setLayoutManager(layoutManager);

            shopList_adapter.notifyDataSetChanged();
        }
        else
        {
            Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
        }
    }
}