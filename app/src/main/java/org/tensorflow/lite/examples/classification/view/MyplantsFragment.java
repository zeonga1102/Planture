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

import org.tensorflow.lite.examples.classification.R;

import java.util.ArrayList;

public class MyplantsFragment extends Fragment {

    private MyplantsViewModel mViewModel;
    private RecyclerView recyclerView;
    private MyPlantsRecyclerViewAdapter adapter;
    private ArrayList<String> names = new ArrayList<>();
    private ArrayList<String> imgUrls = new ArrayList<>();


    public static MyplantsFragment newInstance() {
        return new MyplantsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d("jalee", "onCreateView");
        View view = inflater.inflate(R.layout.fragment_myplants, container, false);

        //firebase 연결하면 수정
        names.add("planture");
        names.add("planture");
        names.add("planture");
        names.add("planture");
        names.add("add");

        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/View_from_the_Window_at_Le_Gras%2C_Joseph_Nic%C3%A9phore_Ni%C3%A9pce%2C_uncompressed_UMN_source.png/270px-View_from_the_Window_at_Le_Gras%2C_Joseph_Nic%C3%A9phore_Ni%C3%A9pce%2C_uncompressed_UMN_source.png");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/View_from_the_Window_at_Le_Gras%2C_Joseph_Nic%C3%A9phore_Ni%C3%A9pce%2C_uncompressed_UMN_source.png/270px-View_from_the_Window_at_Le_Gras%2C_Joseph_Nic%C3%A9phore_Ni%C3%A9pce%2C_uncompressed_UMN_source.png");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/View_from_the_Window_at_Le_Gras%2C_Joseph_Nic%C3%A9phore_Ni%C3%A9pce%2C_uncompressed_UMN_source.png/270px-View_from_the_Window_at_Le_Gras%2C_Joseph_Nic%C3%A9phore_Ni%C3%A9pce%2C_uncompressed_UMN_source.png");
        imgUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/0/0e/View_from_the_Window_at_Le_Gras%2C_Joseph_Nic%C3%A9phore_Ni%C3%A9pce%2C_uncompressed_UMN_source.png/270px-View_from_the_Window_at_Le_Gras%2C_Joseph_Nic%C3%A9phore_Ni%C3%A9pce%2C_uncompressed_UMN_source.png");
        imgUrls.add("https://helpx.adobe.com/content/dam/help/en/photoshop/using/convert-color-image-black-white/jcr_content/main-pars/before_and_after/image-before/Landscape-Color.jpg");

        recyclerView = view.findViewById(R.id.recycler_myplants);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addItemDecoration(new MyPlantsRecyclerViewDecoration(getActivity()));

        adapter = new MyPlantsRecyclerViewAdapter(getActivity(), names, imgUrls);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MyplantsViewModel.class);
        // TODO: Use the ViewModel
    }

}