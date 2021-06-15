package org.tensorflow.lite.examples.classification.view.myplants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.databinding.ActivityMyplantAddBinding;
import org.tensorflow.lite.examples.classification.model.Plant;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyPlantAddActivity extends AppCompatActivity {

    EditText plantName;
    EditText plantDesc;
    EditText plantWaterPeriod;
    TextView registerButton;
    ImageView plantImage;
    ImageView imageAdd;
    Switch switchAdd;

    private File tempFile = null;
    Uri photoUri = null;

    private ActivityMyplantAddBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_myplant_add);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_myplant_add);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = this;

//        plantName = findViewById(R.id.edit_plantName);
//        plantDesc = findViewById(R.id.edit_plantDesc);
//        registerButton = findViewById(R.id.registerButton);
//        plantImage = findViewById(R.id.edit_plantImage);
//        image_add = findViewById(R.id.image_add);

        plantName = binding.editPlantName;
        plantDesc = binding.editPlantDesc;
        plantWaterPeriod = binding.editPlantWaterPeriod;
        registerButton = binding.registerButton;
        plantImage = binding.editPlantImage;
        imageAdd = binding.imageAdd;
        switchAdd = binding.switchAdd;

        switchAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchAdd.isChecked()){
                    plantWaterPeriod.setEnabled(true);
                }else{
                    plantWaterPeriod.setEnabled(false);
                }
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = plantName.getText().toString().trim();
                String desc = plantDesc.getText().toString();

                int waterPeriod;
                if(switchAdd.isChecked()) {
                    if (plantWaterPeriod.getText() == null) {
                        waterPeriod = 0;
                    } else {
                        waterPeriod = Integer.parseInt(plantWaterPeriod.getText().toString());
                    }
                }else{
                    waterPeriod = 0;
                }

                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy-MM-dd");
                String regDate = simpleDate.format(date);

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference dbRef = database.getReference().child("MyPlant");

                FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                FirebaseUser user = firebaseAuth.getCurrentUser();
                String uid = user.getUid();

                //StorageReference storageRef = FirebaseStorage.getInstance().getReference();
                Uri file = Uri.fromFile(new File(getPath(photoUri))); // 절대경로uri를 file에 할당
                Log.d("이미지storage", "photo file : " + file);

                // 스토리지에 방생성 후 선택한 이미지 넣음
                StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                        .child("userMyPlantsImage").child(uid+"/"+file.getLastPathSegment());
                storageRef.putFile(photoUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            final Task<Uri> imageUrl = task.getResult().getStorage().getDownloadUrl();
                            imageUrl.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    //while (!imageUrl.isComplete()) ;

                                    Plant plant = new Plant();
                                    plant.setName(name);
                                    plant.setDesc(desc);
                                    plant.setWaterPeriod(waterPeriod);
                                    plant.setWaterTime(waterPeriod);
                                    plant.setImgUrl(imageUrl.getResult().toString());
                                    plant.setRegDate(regDate);

                                    // database에 저장
                                    DatabaseReference pushRef = dbRef.child(uid).push();
                                    String key = pushRef.getKey();
                                    plant.setKey(key);
                                    pushRef.setValue(plant).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            finish();
                                        }
                                    });
                                }
                            });
                        }
                    }
                });
            }
        });

        imageAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera_permission();
            }
        });

    }

    public String getPath(Uri uri){
        String [] proj = {MediaStore.Images.Media.DATA};
        CursorLoader cursorLoader = new CursorLoader(this,uri,proj,null,null,null);

        Cursor cursor = cursorLoader.loadInBackground();
        int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        cursor.moveToFirst();

        return cursor.getString(index);
    }

    private void camera_permission() {

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                image_picker();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {

            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                ).check();
    }

    //이미지피커
    private void image_picker() {
//        ImagePicker.create(this)
//                .returnMode(ReturnMode.ALL)
//                .toolbarImageTitle("Set Profile Image")
//                .toolbarArrowColor(Color.BLACK)
//                .includeVideo(false)
//                .onlyVideo(false)
//                .single()
//                .showCamera(true)
//                .imageDirectory("Camera")
//                .enableLog(true)
//                .start();
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 1);
    }



    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK) {

            Toast.makeText(this, "취소되었습니다.", Toast.LENGTH_SHORT).show();

            if (tempFile != null) {
                if (tempFile.exists()) {
                    if (tempFile.delete()) {
                        tempFile = null;
                    } else {
                    }
                } else {
                }
            } else {
            }
            return;
        }

        if(requestCode== 1 && resultCode==RESULT_OK && data!=null) {
            photoUri = data.getData();
            setImage();
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg";
        File storageDir = new File(Environment.getExternalStorageDirectory(), imageFileName);
        String mCurrentPhotoPath = storageDir.getAbsolutePath();
        return storageDir;
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    private void setImage() {
        Glide.with(this)
                .load(photoUri)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(plantImage);
    }
}