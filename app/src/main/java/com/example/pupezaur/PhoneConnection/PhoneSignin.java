package com.example.pupezaur.PhoneConnection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pupezaur.MainActivities.MainActivity;
import com.example.pupezaur.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class PhoneSignin extends AppCompatActivity {

    private String checker = "", phoneNumber = "", smsCode = "";
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private ProgressDialog loadingBar;

    MaterialEditText phone_number, username, codeText;
    Button btn_signin, btn_continue;
    TextView btnregister;
    LinearLayout phoneAuth, llayout;

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

        phone_number = findViewById(R.id.phone_number);
        phoneNumber = phone_number.getText().toString();
        username = findViewById(R.id.username);
        name = username.getText().toString();

        btn_signin = findViewById(R.id.btn_signin);
        btnregister = findViewById(R.id.btnregister);
        phoneAuth = findViewById(R.id.phoneAuth);
        llayout = findViewById(R.id.llayout);
        btn_continue = findViewById(R.id.btn_continue);
        codeText = findViewById(R.id.codeText);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        if (auth.getCurrentUser() != null) {
            Intent intent = new Intent(PhoneSignin.this, MainActivity.class);
            startActivity(intent);
        } else {
            setContentView(R.layout.phone_signin);
            FirebaseDatabase.getInstance().getReference("Admin").orderByChild("Users");
        }

        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        auth = FirebaseAuth.getInstance();



        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//          String aid = firebaseUser.getUid();
//          String aid = adminPhoneNumber;
                String phoneNumber = phone_number.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(PhoneSignin.this, "Please write a valid phone number", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase.getInstance().getReference().orderByChild("phoneNumber").equalTo(phone_number.getText().toString()).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                String phoneNumber = phone_number.getText().toString();

                                loadingBar.setTitle("Phone number verification");
                                loadingBar.setMessage("Please wait for registration code");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();

//                              llayout.setVisibility(View.INVISIBLE);
//                              phoneAuth.setVisibility(View.VISIBLE);

                                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                        phoneNumber,                    // Phone number to verify
                                        60,                          // Timeout duration
                                        TimeUnit.SECONDS,               // Unit of timeout
                                        PhoneSignin.this, // Activity (for callback binding)
                                        mCallbacks);                    // OnVerificationStateChangedCallbacks
                            } else {
                                Toast.makeText(PhoneSignin.this, "Please write a valid admin phone number", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                    mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                        @Override
                        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                            signInWithPhoneAuthCredential(phoneAuthCredential);
                        }

                        @Override
                        public void onVerificationFailed(@NonNull FirebaseException e) {

                            Toast.makeText(PhoneSignin.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                        @Override
                        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                            super.onCodeSent(s, forceResendingToken);

                            verificationId = s;
                            resendingToken = forceResendingToken;

                            llayout.setVisibility(View.GONE);
                            phoneAuth.setVisibility(View.VISIBLE);
                            checker = "Code Sent";

                            loadingBar.dismiss();
                            Toast.makeText(PhoneSignin.this, "Code has been sent, please check", Toast.LENGTH_SHORT).show();
                        }
                    };

                    btn_continue.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
//                        btn_register.setVisibility(View.INVISIBLE);
//                        phone_number.setVisibility(View.INVISIBLE);
//                        username.setVisibility(View.INVISIBLE);
//                        llayout.setVisibility(View.INVISIBLE);

                            String verificationCode = codeText.getText().toString();
                            if (TextUtils.isEmpty(verificationCode)) {
                                Toast.makeText(PhoneSignin.this, "Please write verification code first", Toast.LENGTH_SHORT).show();
                            } else {
                                loadingBar.setTitle("Code verification");
                                loadingBar.setMessage("Please wait to verify code number");
                                loadingBar.setCanceledOnTouchOutside(false);
                                loadingBar.show();

                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
                                signInWithPhoneAuthCredential(credential);
                            }
                        }
                    });
                }
            }
        });
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(PhoneSignin.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            loadingBar.dismiss();
                            String e = task.getException().toString();
                            Toast.makeText(PhoneSignin.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PhoneSignin.this, AdminPhoneRegister.class);
                startActivity(intent);
                finish();
            }
        });
    }
//    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential){
//        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if (task.isSuccessful()) {
//                    Intent intent = new Intent(PhoneSignin.this, MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
//                } else {
//                    Toast.makeText(PhoneSignin.this, "Authentication failed", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }

//    private void verifyCode (final String code) {
//        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
//        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    final String num = getIntent().getStringExtra(phone_number.getText().toString());
//                    FirebaseDatabase.getInstance().getReference("Users").orderByChild("PhoneNumber").equalTo(num).addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()){
//                                Intent intent = new Intent(PhoneSignin.this, MainActivity.class);
//                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                startActivity(intent);
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//                    phoneNumber = auth.getCurrentUser().getPhoneNumber();
//                    PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code);
//                    signInWithPhoneAuthCredential(phoneAuthCredential);
//                }
//            }
//        });
//    }
}
