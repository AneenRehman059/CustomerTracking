package com.zasa.superduper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.zasa.superduper.activities.Survey_Activity;
import com.zasa.superduper.Models.Compaign_Module_Model;
import com.zasa.superduper.R;

import java.util.ArrayList;

public class Compaign_Adapter extends RecyclerView.Adapter<Compaign_Adapter.viewHolder> {
    ArrayList<Compaign_Module_Model> moduleList;
    Context context;

    public Compaign_Adapter(ArrayList<Compaign_Module_Model> moduleList, Context context) {
        this.moduleList = moduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public Compaign_Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_operation_module_design,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Compaign_Adapter.viewHolder holder, int position) {
        Compaign_Module_Model model = moduleList.get(position);

        holder.iv_module.setImageResource(model.getModuledPic());
        holder.txt_module.setText(model.getModule_name());

        Picasso.get().load(model.getImage_url()).placeholder(R.drawable.bottles)
                .into(holder.iv_module);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Survey_Activity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return moduleList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView iv_module;
        TextView txt_module;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            iv_module = itemView.findViewById(R.id.iv_single_mod);
            txt_module = itemView.findViewById(R.id.tv_single_mod);
        }
    }
}
