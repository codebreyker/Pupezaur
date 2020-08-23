package com.example.pupezaur.PhoneConnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pupezaur.MainActivities.MainActivity;
import com.example.pupezaur.R;
import com.example.pupezaur.Utils.Message;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthSettings;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class AdminPhoneRegister extends AppCompatActivity {

    private String checker = "", phoneNumber = "", smsCode = "";
    String adminPhoneNumber;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendingToken;
    private ProgressDialog loadingBar;

    MaterialEditText username, admin_phone_number, codeText, phone_number;
    Button btn_register, btn_continue;
    TextView btnlogin;
    LinearLayout phoneAuth;
    LinearLayout llayout;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(AdminPhoneRegister.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_phone_register);

        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        username = findViewById(R.id.username);
        phone_number = findViewById(R.id.phone_number);
        admin_phone_number = findViewById(R.id.admin_phone_number);
        codeText = findViewById(R.id.codeText);
        btn_register = findViewById(R.id.btn_register);
        btn_continue = findViewById(R.id.btn_continue);
        phoneAuth = findViewById(R.id.phoneAuth);
        llayout = findViewById(R.id.llayout);

        auth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        FirebaseAuthSettings firebaseAuthSettings = auth.getFirebaseAuthSettings();
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);

        btnlogin = findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminPhoneRegister.this, PhoneSignin.class);
                startActivity(intent);
                finish();
            }
        });

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = phone_number.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(AdminPhoneRegister.this, "Please write a valid phone number", Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle("Phone number verification");
                    loadingBar.setMessage("Please wait for registration code");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

//                  llayout.setVisibility(View.INVISIBLE);
//                  phoneAuth.setVisibility(View.VISIBLE);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                    // Phone number to verify
                            60,                          // Timeout duration
                            TimeUnit.SECONDS,               // Unit of timeout
                            AdminPhoneRegister.this,// Activity (for callback binding)
                            mCallbacks);                    // OnVerificationStateChangedCallbacks
                    }
                }
        });

                mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                        signInWithPhoneAuthCredential(phoneAuthCredential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Toast.makeText(AdminPhoneRegister.this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
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

                        Toast.makeText(AdminPhoneRegister.this, "Code has been sent, please check", Toast.LENGTH_SHORT).show();
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
                            Toast.makeText(AdminPhoneRegister.this, "Please write verification code first", Toast.LENGTH_SHORT).show();
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

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//
//                            loadingBar.dismiss();
//                            Toast.makeText(PhoneRegister.this, "Register successful", Toast.LENGTH_SHORT).show();
//                            Intent intent = new Intent(PhoneRegister.this, MainActivity.class);
//                            startActivity(intent);
//                            finish();

                FirebaseUser firebaseUser = auth.getCurrentUser();
//                reference.child("Admin").orderByChild("AdminId").equalTo(adminPhoneNumber);
                String uid = firebaseUser.getPhoneNumber();
                reference = FirebaseDatabase.getInstance().getReference().child("Admin").child(uid); //admin id

                    HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("uid", uid);
                        hashMap.put("name", username.getText().toString());
                        hashMap.put("phoneNumber", phone_number.getText().toString());
                        hashMap.put("isAdmin", "1");
                        hashMap.put("Chats", "");
                        hashMap.put("Users", "");

                    reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(AdminPhoneRegister.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                loadingBar.dismiss();
                                String e = task.getException().toString();
                                Toast.makeText(AdminPhoneRegister.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

            }
        });
    }

}

