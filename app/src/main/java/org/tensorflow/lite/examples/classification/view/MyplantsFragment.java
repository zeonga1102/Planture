package org.tensorflow.lite.examples.classification.view;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import org.tensorflow.lite.examples.classification.FirebaseBroker;
import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.model.Plant;

import java.util.ArrayList;
import java.util.List;

public class MyplantsFragment extends Fragment {

    private MyplantsViewModel mViewModel;
    private RecyclerView recyclerView;
    private MyPlantsRecyclerViewAdapter adapter;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imgUrls = new ArrayList<>();
    List<Plant> myPlants = new ArrayList<>();

    public static MyplantsFragment newInstance() {
        return new MyplantsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("jalee", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_myplants, container, false);

        init(view);
        initData();

        return view;
    }

    private void initData(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference dbRef = database.getReference().child("myPlant");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myPlants.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Plant plant = snapshot.getValue(Plant.class);
                    myPlants.add(plant);
                    Log.i("plantSize",Integer.toString(myPlants.size()));
                }
                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                recyclerView.setLayoutManager(layoutManager);

                recyclerView.addItemDecoration(new MyPlantsRecyclerViewDecoration(getActivity()));

                adapter.setMyPlants(myPlants);
                adapter.notifyDataSetChanged();

                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void init(View view){
        recyclerView = view.findViewById(R.id.recycler_myplants);
        adapter = new MyPlantsRecyclerViewAdapter(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyplantsViewModel.class);
        // TODO: Use the ViewModel
    }

}