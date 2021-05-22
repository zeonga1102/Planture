package org.tensorflow.lite.examples.classification.view;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.model.Plant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyPlantsRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private List<String> names = new ArrayList<String>();
    private List<String> imgUrls = new ArrayList<String>();

    private ImageView imgMyplatns;
    private TextView textMyplants;
    private ConstraintLayout panel;

    private List<Plant> myPlants;

    public MyPlantsRecyclerViewAdapter(Context ctx) {
        this.ctx = ctx;
    }

    public void setMyPlants(List<Plant> myPlants){
        this.myPlants = myPlants;
        for(int i=0; i<myPlants.size(); i++){
            this.names.add(myPlants.get(i).getName());
            this.imgUrls.add(myPlants.get(i).getImgUrl());
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(ctx).inflate(R.layout.item_myplants_recyclerview, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Picasso.get()   //임의로 이미지 넣어놨음. firebase 연결해서 이미지 받아오면 수정
                .load(imgUrls.get(position))
                .resize(400, 400)
                .centerCrop()
                .into(imgMyplatns);
        //Glide.with(imgMyplatns.getContext()).load(imgUrls.get(position)).into(imgMyplatns);

        String name = names.get(position);
        textMyplants.setText(name);

        panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name == "add")
                    Toast.makeText(ctx, "추가", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(ctx, "상세정보", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return myPlants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgMyplatns = itemView.findViewById(R.id.img_myplants);
            textMyplants = itemView.findViewById(R.id.text_myplants);
            panel = itemView.findViewById(R.id.panel_myplants);
        }
    }
}
