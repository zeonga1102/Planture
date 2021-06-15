package org.tensorflow.lite.examples.classification.view.myplants;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.Button;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.squareup.picasso.Picasso;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.model.Plant;
import org.tensorflow.lite.examples.classification.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyPlantDetailActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference myplantRef;
    FirebaseUser user;
    String uid;
    String key;

    private File tempFile = null;
    Uri photoUri = null;

    private ImageView plantImage;
    private ImageView imageModi;
    private TextView regDateText;
    private EditText plantNameText;
    private EditText plantDescText;
    private EditText plantWaterText;
    private TextView modifyButton;
    private TextView deleteButton;
    private Switch switchDetail;

    Plant plant;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myplant_detail);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        context = this;

        Intent intent = getIntent();
        key = (String)intent.getExtras().get("key");

        plantImage = findViewById(R.id.plantImage);
        imageModi = findViewById(R.id.imageModi);
        regDateText = findViewById(R.id.registerDate);
        plantNameText = findViewById(R.id.plantName);
        plantDescText = findViewById(R.id.plantDesc);
        plantWaterText = findViewById(R.id.plantWaterPeriod);
        modifyButton = findViewById(R.id.modifyButton);
        deleteButton = findViewById(R.id.deleteButton);
        switchDetail = findViewById(R.id.switch_detail);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        uid = user.getUid();

        database = FirebaseDatabase.getInstance();
        myplantRef = database.getReference().child("MyPlant");

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
                plantWaterText.setText(Integer.toString(plant.getWaterPeriod()));
                if(plant.getWaterPeriod() == 0){
                    switchDetail.setChecked(false);
                }else{
                    switchDetail.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        switchDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchDetail.isChecked()){
                    plantWaterText.setEnabled(true);
                }else{
                    plantWaterText.setEnabled(false);
                }
            }
        });

        modifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("정보 수정").setMessage("입력한 정보로 수정하겠습니까?");
                builder.setPositiveButton("수정", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int waterPeriod;
                        if(switchDetail.isChecked()) {
                            if (plantWaterText.getText() == null) {
                                waterPeriod = 0;
                            } else {
                                waterPeriod = Integer.parseInt(plantWaterText.getText().toString());
                            }
                        }else{
                            waterPeriod = 0;
                        }

                        Map<String, Object> childUpdates = new HashMap<>();
                        plant.setName(plantNameText.getText().toString());
                        plant.setDesc(plantDescText.getText().toString());
                        plant.setWaterPeriod(waterPeriod);
                        plant.setWaterTime(waterPeriod);
                        childUpdates.put(key, plant);

                        myplantRef.child(uid).updateChildren(childUpdates);
                        Toast.makeText(context, "수정되었습니다.", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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

        imageModi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("사진 변경").setMessage("사진을 변경하겠습니까?");
                builder.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        myplantRef.child(uid).child(key).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                camera_permission();
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

                                Map<String, Object> childUpdates = new HashMap<>();
                                plant.setImgUrl(imageUrl.getResult().toString());
                                childUpdates.put(key, plant);

                                myplantRef.child(uid).updateChildren(childUpdates);
                                Toast.makeText(context, "사진이 변경되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
            });
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