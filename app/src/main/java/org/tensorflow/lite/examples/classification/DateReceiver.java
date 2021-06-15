package org.tensorflow.lite.examples.classification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.lite.examples.classification.model.Plant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;

public class DateReceiver extends BroadcastReceiver {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    @Override
    public void onReceive(Context context, Intent intent) {
       if(Intent.ACTION_DATE_CHANGED == intent.getAction()){
           firebaseAuth = FirebaseAuth.getInstance();
           FirebaseUser user = firebaseAuth.getCurrentUser();
           String uid = user.getUid();

           database = FirebaseDatabase.getInstance();
           DatabaseReference myplantRef = database.getReference().child("MyPlant");

           myplantRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
               @Override
               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                       Plant plant = snapshot.getValue(Plant.class);

                       int waterTime = plant.getWaterTime();
                       int waterPeriod = plant.getWaterPeriod();

                       Map<String, Object> childUpdates = new HashMap<>();

                       if(waterPeriod != 0) {
                           if (waterTime == 0) {
                               plant.setWaterTime(waterPeriod - 1);
                           } else {
                               plant.setWaterTime(waterTime - 1);
                           }
                       }

                       childUpdates.put(plant.getKey(), plant);
                       myplantRef.child(uid).updateChildren(childUpdates);
                   }
               }
               @Override
               public void onCancelled(@NonNull DatabaseError databaseError) { }
           });
       }
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}