package org.tensorflow.lite.examples.classification.view.myplants;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.databinding.ItemMyplantsRecyclerviewBinding;
import org.tensorflow.lite.examples.classification.model.Plant;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyPlantsRecyclerViewAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private List<String> names = new ArrayList<String>();
    private List<String> imgUrls = new ArrayList<String>();

    private ImageView imgMyplatns;
    private TextView textMyplants;
    private LinearLayout panel;

    private List<Plant> myPlants;

    public MyPlantsRecyclerViewAdapter(Context ctx) {
        this.ctx = ctx;

        FirebaseAuth firebaseAuth;
        FirebaseDatabase database;

        database = FirebaseDatabase.getInstance();
        DatabaseReference myplantRef = database.getReference().child("MyPlant");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        myPlants = new ArrayList<>();
        myplantRef.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPlants.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Plant plant = snapshot.getValue(Plant.class);
                    myPlants.add(plant);
                    Log.i("plantSize",Integer.toString(myPlants.size()));
                }

                notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = LayoutInflater.from(ctx).inflate(R.layout.item_myplants_recyclerview, parent, false);
        return new ViewHolder(convertView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemMyplantsRecyclerviewBinding binding = ((ViewHolder) holder).getBinding();

        Picasso.get()   //임의로 이미지 넣어놨음. firebase 연결해서 이미지 받아오면 수정
                .load(myPlants.get(position).getImgUrl())
                .resize(400, 400)
                .centerCrop()
                .into(binding.imgMyplants);

        String name = myPlants.get(position).getName();
        binding.textMyplants.setText(name);

        int waterTime;
        if(myPlants.get(position).getWaterPeriod() == 0){
            binding.textWater.setText("");
        }else {
            waterTime = myPlants.get(position).getWaterTime();
            if (waterTime == 0) {
                binding.textWater.setText("물");
            } else {
                binding.textWater.setText(waterTime + "일 후 물");
            }
        }

        binding.panelMyplants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ctx, MyPlantDetailActivity.class);
                intent.putExtra("key", myPlants.get(position).getKey());
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myPlants.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ItemMyplantsRecyclerviewBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding = DataBindingUtil.bind(itemView);
        }

        ItemMyplantsRecyclerviewBinding getBinding() {
            return binding;
        }

    }
}
