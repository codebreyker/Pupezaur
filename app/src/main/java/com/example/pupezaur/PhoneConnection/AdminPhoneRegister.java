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
import android.widget.Toast;

import com.example.pupezaur.Days.MondayAdmin;
import com.example.pupezaur.R;
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

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    private PhoneAuthProvider.ForceResendingToken resendingToken;

    private String checker = "", phoneNumber = "", smsCode = "";
    private String verificationId;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference reference;

    MaterialEditText username, admin_phone_number, codeText, phone_number;
    Button btn_register, btn_continue;
    LinearLayout codeVerifLayout, registerLayout;

    private ProgressDialog loadingBar;

    @Override
    public void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            Intent intent = new Intent(AdminPhoneRegister.this, MondayAdmin.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_phone_register);

        String register = getResources().getString(R.string.registerLogin);
        final String invalidNumber = getResources().getString(R.string.invalidNumber);
        final String checkCode = getResources().getString(R.string.checkCode);
        final String emptyCode = getResources().getString(R.string.emptyCode);
        final String codeVerif = getResources().getString(R.string.codeVerif);
        final String pleaseWaitVerif = getResources().getString(R.string.pleaseWaitVerif);
        final String validNumber = getResources().getString(R.string.validNumber);
        final String checkNumber = getResources().getString(R.string.checkNumber);
        final String waitNumber = getResources().getString(R.string.waitNumber);
        final String codeSent = getResources().getString(R.string.codeSent);

        getSupportActionBar().setTitle(register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        FirebaseAuthSettings firebaseAuthSettings = auth.getFirebaseAuthSettings();
        firebaseAuthSettings.setAutoRetrievedSmsCodeForPhoneNumber(phoneNumber, smsCode);

        username = findViewById(R.id.username);
        phone_number = findViewById(R.id.phone_number);
        admin_phone_number = findViewById(R.id.admin_phone_number);
        codeText = findViewById(R.id.codeText);

        codeVerifLayout = findViewById(R.id.codeVerifLayout);
        registerLayout = findViewById(R.id.registerLayout);

        auth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        btn_continue = findViewById(R.id.btn_continue);
        btn_register = findViewById(R.id.btn_register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = phone_number.getText().toString();

                if (TextUtils.isEmpty(phoneNumber)) {
                    Toast.makeText(AdminPhoneRegister.this, validNumber, Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle(checkNumber);
                    loadingBar.setMessage(waitNumber);
                    loadingBar.setCanceledOnTouchOutside(false);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            phoneNumber,                    // Phone number to verify
                            60,                          // Timeout duration
                            TimeUnit.SECONDS,               // Unit of timeout
                            AdminPhoneRegister.this,// Activity (for callback binding)
                            callbacks);                     // OnVerificationStateChangedCallbacks

                    loadingBar.show();
                }
            }
        });

        callbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(AdminPhoneRegister.this, invalidNumber, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                verificationId = s;
                resendingToken = forceResendingToken;

                registerLayout.setVisibility(View.GONE);
                codeVerifLayout.setVisibility(View.VISIBLE);
                checker = codeSent;
                loadingBar.dismiss();

                Toast.makeText(AdminPhoneRegister.this, checkCode, Toast.LENGTH_SHORT).show();
            }
        };

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String verificationCode = codeText.getText().toString();

                if (TextUtils.isEmpty(verificationCode)) {
                    Toast.makeText(AdminPhoneRegister.this, emptyCode, Toast.LENGTH_SHORT).show();
                } else {
                    loadingBar.setTitle(codeVerif);
                    loadingBar.setMessage(pleaseWaitVerif);
                    loadingBar.setCanceledOnTouchOutside(false);

                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
                    signInWithPhoneAuthCredential(credential);

                    loadingBar.show();
                }
            }
        });

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                firebaseUser = auth.getCurrentUser();
                String uid = firebaseUser.getPhoneNumber();
                reference = FirebaseDatabase.getInstance().getReference().child("Admin").child(uid); //admin id

                HashMap<String, String> hashMap = new HashMap<>();
                hashMap.put("uid", uid);
                hashMap.put("name", username.getText().toString());
                hashMap.put("phoneNumber", phone_number.getText().toString());
                hashMap.put("isAdmin", "1");

                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Intent intent = new Intent(AdminPhoneRegister.this, MondayAdmin.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        } else {
                            loadingBar.dismiss();
                            String e = task.getException().toString();
                            final String error = getResources().getString(R.string.error);
                            Toast.makeText(AdminPhoneRegister.this, error + ": " + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }

}

