package com.example.pupezaur.PhoneConnection;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pupezaur.MainActivities.SigninActivity;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.MainActivities.MainActivity;
import com.example.pupezaur.MainActivities.RegisterActivity;
import com.example.pupezaur.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class PhoneSignin extends AppCompatActivity {

    private String verificationId, smsCode;

    MaterialEditText phone_number;
    Button btn_signin;
    TextView btnregister;

    FirebaseAuth auth;
    DatabaseReference reference;
    FirebaseUser firebaseUser;

    public String name;


    @Override
    protected void onStart() {
        super.onStart();
        if (firebaseUser != null) {
            Intent intent = new Intent(PhoneSignin.this, MainActivity.class);
            startActivity(intent);
            FirebaseUser currentUser = auth.getCurrentUser();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_signin);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(PhoneSignin.this, MainActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.phone_signin);
            reference = FirebaseDatabase.getInstance().getReference().child("Users");
        }

        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        auth = FirebaseAuth.getInstance();

        phone_number = findViewById(R.id.phone_number);
        btn_signin = findViewById(R.id.btn_signin);
        btnregister = findViewById(R.id.btnregister);

        btn_signin.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              String phoneNumber = phone_number.getText().toString();

                                              if (TextUtils.isEmpty(phoneNumber)) {
                                                  Toast.makeText(PhoneSignin.this, "All fields are required", Toast.LENGTH_SHORT).show();
                                              } else {
                                                 PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, smsCode);
                                                 signInWithPhoneAuthCredential(phoneAuthCredential);
                                              }
                                          }
                                      });


        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneSignin.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(PhoneSignin.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(PhoneSignin.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
