package org.tensorflow.lite.examples.classification.view.myplants;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//import org.tensorflow.lite.examples.classification.FirebaseBroker;
import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.model.Plant;
import org.tensorflow.lite.examples.classification.model.User;
import org.tensorflow.lite.examples.classification.view.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class MyplantsFragment extends Fragment {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;

    private MyplantsViewModel mViewModel;
    private RecyclerView recyclerView;
    private MyPlantsRecyclerViewAdapter adapter;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imgUrls = new ArrayList<>();
    private Button addButton;
    private ImageButton logoutButton;
    private TextView userNameText;
    private TextView userEmailText;

    List<Plant> myPlants = new ArrayList<>();

    public static MyplantsFragment newInstance() {
        return new MyplantsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_myplants, container, false);

        userNameText = (TextView)rootView.findViewById(R.id.userNameText);
        userEmailText = (TextView)rootView.findViewById(R.id.userEmailText);

        logoutButton = (ImageButton)rootView.findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(rootView.getContext(), LoginActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });

        addButton = (Button)rootView.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(rootView.getContext(), MyPlantAddActivity.class);
                startActivity(intent);
            }
        });

        recyclerView = rootView.findViewById(R.id.recycler_myplants);
        recyclerView.setHasFixedSize(true);
        adapter = new MyPlantsRecyclerViewAdapter(getActivity());

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyPlantsRecyclerViewDecoration(getActivity()));
        recyclerView.setAdapter(adapter);

        //init(rootView);
        initData();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        //initData();
    }

    public void initData(){
        database = FirebaseDatabase.getInstance();
        DatabaseReference myplantRef = database.getReference().child("MyPlant");
        DatabaseReference userRef = database.getReference().child("User");

        firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        String uid = user.getUid();

        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                userNameText.setText(user.getName()+" 님");
                userEmailText.setText(user.getEmail());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void init(View view){
        recyclerView = view.findViewById(R.id.recycler_myplants);
        recyclerView.setHasFixedSize(true);
        adapter = new MyPlantsRecyclerViewAdapter(getActivity());
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyplantsViewModel.class);
        // TODO: Use the ViewModel
    }

}