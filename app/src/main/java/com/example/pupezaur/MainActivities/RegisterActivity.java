//package com.example.pupezaur.MainActivities;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.pupezaur.Days.Monday;
//import com.example.pupezaur.PhoneConnection.PhoneSignin;
//import com.example.pupezaur.R;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.rengwuxian.materialedittext.MaterialEditText;
//
//import java.util.HashMap;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    MaterialEditText username, email, password;
//    Button btn_register;
//    TextView btnlogin;
//
//    FirebaseAuth auth;
//    FirebaseUser firebaseUser;
//    DatabaseReference reference;
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (firebaseUser != null) {
//            Intent intent = new Intent (RegisterActivity.this, Monday.class);
//            startActivity(intent);
//            finish();
//        }
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        getSupportActionBar().setTitle("Register");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
//
//        username = findViewById(R.id.username);
//        email = findViewById(R.id.email);
//        password = findViewById(R.id.password);
//        btn_register = findViewById(R.id.btn_register);
//
//        auth = FirebaseAuth.getInstance();
//
//        btnlogin = findViewById(R.id.btnlogin);
//        btnlogin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this, PhoneSignin.class);
//                startActivity(intent);
//                finish();
//            }
//        });
//
//        btn_register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String txt_username = username.getText().toString();
//                String txt_email = email.getText().toString();
//                String txt_password = password.getText().toString();
//
//                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)) {
//                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
//                } else if (txt_password.length() < 6) {
//                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
//                } else {
//                    register(txt_username, txt_email, txt_password);
//                }
//            }
//        });
//    }
//
//    private void register(final String username, final String email, String password) {
//
//        auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            FirebaseUser firebaseUser = auth.getCurrentUser();
//                            String uid = firebaseUser.getUid();
//
//                            reference = FirebaseDatabase.getInstance().getReference("Users").child(uid);
//
//                            HashMap<String, String> hashMap = new HashMap<>();
//                            hashMap.put("uid", uid);
//                            hashMap.put("name", username);
//                            hashMap.put("email", email);
//                            hashMap.put("isAdmin","1");
//
//                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()) {
//                                        Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
//                                        finish();
//                                    }
//                                }
//                            });
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "You can't register with this email or password", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
//
//        }
//    }
//
