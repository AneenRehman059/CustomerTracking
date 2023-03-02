package com.zasa.superduper.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zasa.superduper.R;

import java.util.ArrayList;

public class AdapterShowImages extends RecyclerView.Adapter<AdapterShowImages.viewHolder> {
    private Context context;
    public int row;
    private ArrayList<String> arrayList;

    public AdapterShowImages(Context context, ArrayList<String> arrayList,int row) {
        this.context = context;
        this.arrayList = arrayList;
        this.row = row;
    }

    @NonNull
    @Override
    public AdapterShowImages.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new viewHolder(LayoutInflater.from(context).inflate(R.layout.image_res,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterShowImages.viewHolder holder, int position) {
        Glide.with(context)
                .load(arrayList.get(position))
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void updateList(String imagePath){
        arrayList.add(imagePath);
        notifyDataSetChanged();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.item_iv);
        }
    }
}
