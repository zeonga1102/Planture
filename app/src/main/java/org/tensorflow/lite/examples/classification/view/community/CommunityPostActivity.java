package org.tensorflow.lite.examples.classification.view.community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.loader.content.CursorLoader;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.databinding.ActivityCommunityPostBinding;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommunityPostActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityCommunityPostBinding binding;

    private String imgUrl;

    private FirebaseStorage firebaseStorage;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseAuth firebaseAuth;

    boolean selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_community_post);

        binding.buttonPost.setOnClickListener(this);

        //권한 요청
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        //앨범 오픈
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);

        // Firebase storage
        firebaseStorage = FirebaseStorage.getInstance();

        // Firebase Database
        firebaseDatabase = FirebaseDatabase.getInstance();

        // Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        binding.imgPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader cursorLoader = new CursorLoader(this, data.getData(), proj,
                    null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();

            //이미지 경로
            imgUrl = cursor.getString(column_index);

            binding.imgPost.setImageURI(data.getData());

            selected = true;
        }
        else if(!selected) {
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button_post && imgUrl != null) {
            binding.progressBarPost.setVisibility(View.VISIBLE);

            File file = new File(imgUrl);
            Uri contentUri = Uri.fromFile(file);
            StorageReference storageRef = firebaseStorage.getReference().child("images").child(contentUri.getLastPathSegment());
            UploadTask uploadTask = storageRef.putFile(contentUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return storageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        storageRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        //디비에 바인딩 할 위치 생성 및 컬렉션(테이블)에 데이터 집합 생성
                                        DatabaseReference images = firebaseDatabase.getReference().child("images").push();

                                        //시간 생성
                                        Date date = new Date();
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                        CommunityDTO communityDTO = new CommunityDTO();

                                        //이미지 파일 이름
                                        communityDTO.imgFileName = contentUri.getLastPathSegment();
                                        //이미지 주소
                                        communityDTO.imageUrl = uri.toString();
                                        //유저의 UID
                                        communityDTO.uid = firebaseAuth.getCurrentUser().getUid();
                                        //게시물의 설명
                                        communityDTO.explain = binding.textPost.getText().toString();
                                        //유저의 아이디
                                        communityDTO.userId = firebaseAuth.getCurrentUser().getEmail();
                                        //게시물 업로드 시간
                                        communityDTO.timestamp = simpleDateFormat.format(date);

                                        //게시물 데이터를 생성 및 엑티비티 종료
                                        images.setValue(communityDTO);

                                        binding.progressBarPost.setVisibility(View.GONE);
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                });
                        Toast.makeText(CommunityPostActivity.this, "업로드 완료", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        binding.progressBarPost.setVisibility(View.GONE);

                        Toast.makeText(CommunityPostActivity.this, "업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}