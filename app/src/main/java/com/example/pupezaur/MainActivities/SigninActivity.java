package com.example.pupezaur.MainActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pupezaur.PhoneConnection.UserPhoneRegister;
import com.example.pupezaur.Utils.AllMethods;
import com.example.pupezaur.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SigninActivity extends AppCompatActivity {
    MaterialEditText email, password, username;
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
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
            FirebaseUser currentUser = auth.getCurrentUser();
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            Intent intent = new Intent (SigninActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_signin);
            username = findViewById(R.id.username);
            email = findViewById(R.id.email);
            password = findViewById(R.id.password);
            btn_signin = findViewById(R.id.btn_signin);
            reference = FirebaseDatabase.getInstance().getReference().child("Users");
        }

        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        auth = FirebaseAuth.getInstance();

        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_signin = findViewById(R.id.btn_signin);
        btnregister = findViewById(R.id.btnregister);

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String txt_username = username.getText().toString();
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if (TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password) || TextUtils.isEmpty(txt_username)) {
                    Toast.makeText(SigninActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    auth.signInWithEmailAndPassword(txt_email, txt_password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                                        AllMethods.name = txt_username;
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(SigninActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

    btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
        Intent intent = new Intent(SigninActivity.this, UserPhoneRegister.class);
        startActivity(intent);
        finish();
            }
        });
    }

}