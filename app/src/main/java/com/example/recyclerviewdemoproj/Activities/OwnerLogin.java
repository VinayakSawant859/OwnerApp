package com.example.recyclerviewdemoproj.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.recyclerviewdemoproj.databinding.ActivityOwnerLoginBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Pattern;

public class OwnerLogin extends AppCompatActivity {

    ActivityOwnerLoginBinding loginBinding;
    private FirebaseAuth mAuth;

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[@#$%^&+=])" +     // at least 1 special character
                    "(?=\\S+$)" +            // no white spaces
                    ".{4,}" +                // at least 4 characters
                    "$");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginBinding = ActivityOwnerLoginBinding.inflate(getLayoutInflater());
        setContentView(loginBinding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();


        loginBinding.adminLogBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validatePassword() | validateEmail()){
                    loginBinding.bar.setVisibility(View.VISIBLE);

                    String email = loginBinding.adminLogInTB.getEditText().getText().toString();
                    String password = loginBinding.adminLogPassTB.getEditText().getText().toString();

                    mAuth.signInWithEmailAndPassword(email,password)
                            .addOnCompleteListener(OwnerLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        loginBinding.bar.setVisibility(View.INVISIBLE);
                                        Intent intent = new Intent(OwnerLogin.this, HomeRecycle.class);
                                        intent.putExtra("email", mAuth.getCurrentUser().getEmail());
                                        Toast.makeText(OwnerLogin.this, "Logged in Successfully!", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                        finishAffinity();
                                    }
                                    else {
                                        loginBinding.bar.setVisibility(View.INVISIBLE);
                                        loginBinding.adminLogID.setText("");
                                        loginBinding.adminLogPass.setText("");
                                        Toast.makeText(OwnerLogin.this, "Invalid User details!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        loginBinding.regforowner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OwnerLogin.this, OwnerRegister.class);
                startActivity(i);
            }
        });
    }

    private boolean validateEmail() {

        String emailInput = loginBinding.adminLogInTB.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            loginBinding.adminLogInTB.setError("Field can not be empty");
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            loginBinding.adminLogInTB.setError("Please enter a valid email address");
            return false;
        } else {
            loginBinding.adminLogInTB.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = loginBinding.adminLogPassTB.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            loginBinding.adminLogPassTB.setError("Field can not be empty");
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            loginBinding.adminLogPassTB.setError("Password is too weak");
            return false;
        } else {
            loginBinding.adminLogPassTB.setError(null);
            return true;
        }
    }
}
