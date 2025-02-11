package org.tensorflow.lite.examples.classification.view;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import org.tensorflow.lite.examples.classification.ClassifierActivity;
import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.tflite.Classifier;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static org.tensorflow.lite.examples.classification.view.MainActivity.mainContext;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;

    View rootView;
    public Bitmap galleryBitmap = null;

    LinearLayout cameraButton;
    LinearLayout albumButton;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        cameraButton = rootView.findViewById(R.id.layout_camera);
        albumButton = rootView.findViewById(R.id.layout_album);

        cameraButton.setOnClickListener(onClickListener);
        albumButton.setOnClickListener(onClickListener);
        return rootView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int id = v.getId();
            Intent intent;
            switch (id) {
                case R.id.layout_camera:
                    intent = new Intent(rootView.getContext(), ClassifierActivity.class);
                    startActivityForResult(intent, 0);
                    break;
                case R.id.layout_album:
                    intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 1);
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //이미지 뷰를 클릭하면 시작되는 함수

        if(requestCode== 1 && resultCode==RESULT_OK && data!=null) {
            //response에 getData , return data 부분 추가해주어야 한다
            HashMap<String,String> map = new HashMap<>();
            map.put("0", "스투키");
            map.put("1", "나팔꽃");
            map.put("2", "개나리");
            map.put("3", "튤립");
            map.put("4", "장미");
            map.put("5", "해바라기");
            map.put("6", "백합");
            map.put("7", "수국");
            map.put("8", "철쭉");
            map.put("9", "무궁화");
            map.put("10", "아이비");
            map.put("11", "코스모스");

            Uri photoUri = data.getData();
            //bitmap 이용
            try {
                galleryBitmap = MediaStore.Images.Media.getBitmap(rootView.getContext().getContentResolver(),photoUri);
                Classifier classifier = Classifier.create(getActivity(), Classifier.Model.QUANTIZED_MOBILENET, Classifier.Device.CPU, 1);
                final List<Classifier.Recognition> results = classifier.recognizeImage(galleryBitmap, 0);
                Intent intent = new Intent(mainContext, ResultActivity.class);
                intent.putExtra("result", map.get(results.get(0).getTitle()));
                startActivityForResult(intent, 0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

}