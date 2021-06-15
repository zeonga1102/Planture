package org.tensorflow.lite.examples.classification.view.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.model.User;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    private Button buttonJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.editText_email);
        editTextPassword = (EditText) findViewById(R.id.editText_passWord);
        editTextName = (EditText) findViewById(R.id.editText_name);

        buttonJoin = (Button) findViewById(R.id.btn_join);
        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {
                    // 이메일과 비밀번호가 공백이 아닌 경우
                    createUser(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim(), editTextName.getText().toString().trim());
                } else {
                    // 이메일과 비밀번호가 공백인 경우
                    Toast.makeText(SignUpActivity.this, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void createUser(String email, String password, String name) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            //회원가입 성공시
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            String name = editTextName.getText().toString().trim();

                            //user정보를 파이어베이스 데이터베이스에 저장
                            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("User");
                            databaseReference.child(uid).setValue(new User(uid,email, name));
                            databaseReference.keepSynced(true);
                            
                            Toast.makeText(SignUpActivity.this, "회원가입 성공", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            // 계정이 중복된 경우
                            Log.w("계정생성오류", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(SignUpActivity.this, "잘못된 형식이거나\n이미 존재하는 계정입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}