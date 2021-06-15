package org.tensorflow.lite.examples.classification.view.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.tensorflow.lite.examples.classification.R;
import org.tensorflow.lite.examples.classification.view.MainActivity;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private FirebaseUser currentUser;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonLogIn;
    private Button buttonSignUp;
    Context loginContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        loginContext = this;

        firebaseAuth = FirebaseAuth.getInstance();

        editTextEmail = (EditText) findViewById(R.id.edittext_email);
        editTextPassword = (EditText) findViewById(R.id.edittext_password);

        buttonSignUp = (Button) findViewById(R.id.btn_signup);
        buttonSignUp.setPaintFlags(buttonSignUp.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SignUpActivity 연결
                Intent intent = new Intent(loginContext, SignUpActivity.class);
                startActivity(intent);
            }
        });

        buttonLogIn = (Button) findViewById(R.id.btn_login);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!editTextEmail.getText().toString().equals("") && !editTextPassword.getText().toString().equals("")) {
                    loginUser(editTextEmail.getText().toString().trim(), editTextPassword.getText().toString().trim());
                } else {
                    Toast.makeText(loginContext, "계정과 비밀번호를 입력하세요.", Toast.LENGTH_LONG).show();
                }
            }
        });

        firebaseAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    Intent intent = new Intent(loginContext, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                }
            }
        };
    }

    public void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            Toast.makeText(loginContext, "로그인 성공", Toast.LENGTH_SHORT).show();
                            firebaseAuth.addAuthStateListener(firebaseAuthListener);
                        } else {
                            // 로그인 실패
                            Toast.makeText(loginContext, "아이디 또는 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        currentUser = firebaseAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(loginContext, MainActivity.class);
            startActivity(intent);
            finish();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuthListener != null) {
            firebaseAuth.removeAuthStateListener(firebaseAuthListener);
        }
    }
}