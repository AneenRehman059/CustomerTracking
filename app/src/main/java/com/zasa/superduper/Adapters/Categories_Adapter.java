package com.zasa.superduper.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zasa.superduper.Models.Category_Model;
import com.zasa.superduper.MyCallBack;
import com.zasa.superduper.R;
import com.zasa.superduper.activities.QuestionActivity;

import java.util.ArrayList;
import java.util.List;

public class Categories_Adapter extends RecyclerView.Adapter<Categories_Adapter.viewHolder> {
    ArrayList<Category_Model> catList;
    private List<String> list = new ArrayList<>();
    private MyCallBack myCallBack;
    Context context;

    public Categories_Adapter(ArrayList<Category_Model> catList, Context context, MyCallBack myCallBack) {
        this.catList = catList;
        this.context = context;
        this.myCallBack = myCallBack;
    }

    @NonNull
    @Override
    public Categories_Adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.each_item,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Categories_Adapter.viewHolder holder, int position) {
        Category_Model model = catList.get(position);

        holder.category_name.setText(model.getCategory_name());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, QuestionActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView category_name;
        RelativeLayout expandableLayot;
        RecyclerView nestedRecyclerView;
        LinearLayout linearLayout;
        ImageView mArrowImage;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            category_name = itemView.findViewById(R.id.txt_categories);
            linearLayout = itemView.findViewById(R.id.linear_layout);

        }
    }
}
