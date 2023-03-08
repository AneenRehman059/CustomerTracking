package com.zasa.superduper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zasa.superduper.Models.Routes_Model;
import com.zasa.superduper.R;
import com.zasa.superduper.activities.TrackOperationPlaceActivity;

import java.util.ArrayList;

public class Routes_Adapter extends RecyclerView.Adapter<Routes_Adapter.viewHolder> {
    ArrayList<Routes_Model> operationList;
    Context context;
    String route_name,route_id;

    public Routes_Adapter(ArrayList<Routes_Model> operationList, Context context) {
        this.operationList = operationList;
        this.context = context;
    }

    @NonNull
    @Override
    public Routes_Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_operation_list_rcv,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Routes_Adapter.viewHolder holder, int position) {
        Routes_Model model = operationList.get(position);
        holder.areaName.setText(model.getAssign_area());


        route_name = model.getAssign_area();
        route_id = model.getRoute_id();

//        holder.shopName.setText(model.getAssign_shop());
//        holder.lineAddresss.setText(model.getAssign_line());
//        holder.lastPurchase.setText(model.getLast_purchase());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TrackOperationPlaceActivity.class);
                TrackOperationPlaceActivity.route =route_name;
                TrackOperationPlaceActivity.routeId = route_id;
                intent.putExtra(route_name,"route_name");
                intent.putExtra(route_id,"routeID");
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return operationList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView areaName,shopName,lineAddresss,lastPurchase;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            areaName  = itemView.findViewById(R.id.area_name);
//            shopName  = itemView.findViewById(R.id.area_shop);
//            lineAddresss  = itemView.findViewById(R.id.line_address);
//            lastPurchase = itemView.findViewById(R.id.last_purchase);
        }
    }
}
