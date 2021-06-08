package org.tensorflow.lite.examples.classification.view.myplants;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.model.Plant;
import org.tensorflow.lite.examples.classification.model.User;

import java.util.HashMap;
import java.util.Map;

public class MyPlantDetailActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    private ImageView plantImage;
    private TextView regDateText;
    private EditText plantNameText;
    private EditText plantDescText;
    private EditText plantWaterText;
    private TextView modifyButton;
    private TextView deleteButton;

    Plant plant;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myplant_detail);

        context = this;

        Intent intent = getIntent();
        String key = (String)intent.getExtras().get("key");

        plantImage = findViewById(R.id.plantImage);
        regDateText = findViewById(R.id.registerDate);
        plantNameText = findViewById(R.id.plantName);
        plantDescText = findViewById(R.id.plantDesc);
        plantWaterText = findViewById(R.id.plantWaterTime);
        modifyButton = findViewById(R.id.modifyButton);
        deleteButton = findViewById(R.id.deleteButton);

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        DatabaseReference myplantRef = database.getReference().child("MyPlant");

        myplantRef.child(uid).child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                plant = dataSnapshot.getValue(Plant.class);
//                Picasso.get()   //임의로 이미지 넣어놨음. firebase 연결해서 이미지 받아오면 수정
//                        .load(plant.getImgUrl())
//                        .centerCrop()
//                        .into(plantImage);
                Glide.with(context)
                        .load(plant.getImgUrl())
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(plantImage);
                regDateText.setText(plant.getRegDate());
                plantNameText.setText(plant.getName());
                plantDescText.setText(plant.getDesc());
                //plantWaterText.setText(user.getName()+" 님");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(modifyButton.getText() == "수정"){
                    plantNameText.setEnabled(true);
                    plantDescText.setEnabled(true);
                    plantWaterText.setEnabled(true);

                    modifyButton.setText("완료");
                }else{
                    plantNameText.setEnabled(false);
                    plantDescText.setEnabled(false);
                    plantWaterText.setEnabled(false);

                    Map<String, Object> childUpdates = new HashMap<>();
                    plant.setName(plantNameText.getText().toString());
                    plant.setDesc(plantDescText.getText().toString());
                    childUpdates.put(key, plant);

                    myplantRef.child(uid).updateChildren(childUpdates);

                    modifyButton.setText("수정");
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("식물 삭제").setMessage("삭제하겠습니까?");
                builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myplantRef.child(uid).child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                finish();
                            }
                        });
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getApplicationContext(),"Try again!", Toast.LENGTH_LONG).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
    }
}