package org.tensorflow.lite.examples.classification.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.tensorflow.lite.examples.classification.ClassifierActivity;
import org.tensorflow.lite.examples.classification.R;

public class MainActivity extends AppCompatActivity {
    Context context;

    private Animation fab_open, fab_close;
    private Boolean isFabOpen = false;
    private FloatingActionButton mainFloating, cameraFloating, galleryFloting, textFloating;

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //가로화면 안됨.

        context = this;

        //floating action button==================================================
        fab_open = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
        fab_close = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);

        mainFloating = (FloatingActionButton)findViewById(R.id.mainFloating);
        cameraFloating = (FloatingActionButton)findViewById(R.id.cameraFloating);
        galleryFloting =(FloatingActionButton)findViewById(R.id.galleryFloating);
        textFloating = (FloatingActionButton)findViewById(R.id.textFloating);

        mainFloating.setOnClickListener(onClickListener);
        cameraFloating.setOnClickListener(onClickListener);
        galleryFloting.setOnClickListener(onClickListener);
        textFloating.setOnClickListener(onClickListener);

        //바텀네비게이션 설정==============================================================
        bottomNavigationView = findViewById(R.id.nav_view);

        getSupportFragmentManager().beginTransaction().add(R.id.main_frame, new HomeFragment()).commit(); //FrameLayout에 fragment.xml 띄우기

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) { //item을 클릭시 id값을 가져와 FrameLayout에 fragment.xml띄우기
                    case R.id.navigation_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new HomeFragment()).commit();
                        break;
                    case R.id.navigation_community:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new CommunityFragment()).commit();
                        break;
                    case R.id.navigation_myplants:
                        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, new MyplantsFragment()).commit();
                        break;
                }
                return true;
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.mainFloating: anim(); break;
                case R.id.cameraFloating:
                    anim();
                    Intent intent = new Intent(context, ClassifierActivity.class);
                    startActivityForResult(intent, 0);
                    break;
                case R.id.galleryFloating: anim(); break;
                case R.id.textFloating: anim(); break;
            }
        }
    };

    public void anim() {
        if (isFabOpen) {
            cameraFloating.startAnimation(fab_close);
            galleryFloting.startAnimation(fab_close);
            textFloating.startAnimation(fab_close);

            cameraFloating.setClickable(false);
            galleryFloting.setClickable(false);
            textFloating.setClickable(false);

            isFabOpen = false;
        } else {
            cameraFloating.startAnimation(fab_open);
            galleryFloting.startAnimation(fab_open);
            textFloating.startAnimation(fab_open);

            cameraFloating.setClickable(true);
            galleryFloting.setClickable(true);
            textFloating.setClickable(true);
            isFabOpen = true;
        }
    }


}