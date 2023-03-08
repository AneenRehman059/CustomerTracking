package com.zasa.superduper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zasa.superduper.activities.Compaign_Activity;
import com.zasa.superduper.Models.ShopList_Model;
import com.zasa.superduper.R;

import java.util.ArrayList;


public class ShopList_Adapter extends RecyclerView.Adapter<ShopList_Adapter.viewHolder> {
    ArrayList<ShopList_Model> shopList;
    Context context;

    public ShopList_Adapter(ArrayList<ShopList_Model> shopList, Context context) {
        this.shopList = shopList;
        this.context = context;
    }

    @NonNull
    @Override
    public ShopList_Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.shop_list_rcv_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopList_Adapter.viewHolder holder, int position) {
        ShopList_Model model = shopList.get(position);

        holder.txt_shop_name.setText(model.getShop_name());
        holder.txt_shop_description.setText(model.getShop_description());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Compaign_Activity.class);
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txt_shop_name,txt_shop_description;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txt_shop_name = itemView.findViewById(R.id.store_name);
            txt_shop_description = itemView.findViewById(R.id.shop_description);
        }
    }
}