package org.tensorflow.lite.examples.classification.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.tensorflow.lite.examples.classification.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MyPlantsRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private List<String> names;
    private List<String> imgUrls;

    private ImageView imgMyplatns;
    private TextView textMyplants;
    private ConstraintLayout panel;

    public MyPlantsRecyclerViewAdapter(Context ctx, List<String> names, List<String> imgUrls) {
        this.ctx = ctx;
        this.names = names;
        this.imgUrls = imgUrls;
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
        return names.size();
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
