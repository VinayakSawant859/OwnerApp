package com.example.recyclerviewdemoproj;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.recyclerviewdemoproj.databinding.ActivityOwnerRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;
import java.util.regex.Pattern;

public class OwnerRegister extends AppCompatActivity {

    ActivityOwnerRegisterBinding registerBinding;
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
        registerBinding = ActivityOwnerRegisterBinding.inflate(getLayoutInflater());
        setContentView(registerBinding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();

        DatabaseClass dbclass = new DatabaseClass();

        registerBinding.nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isInputEmpty = validateTextFields();
                adminModel adminModel = new adminModel(registerBinding.dcLogo.getText().toString(),
                                                        registerBinding.dcName.getText().toString(),
                                                        registerBinding.dcAdminName.getText().toString(),
                                                        registerBinding.dcAdminEmail.getText().toString(),
                                                        registerBinding.dcNewPass.getText().toString(),
                                                        registerBinding.dcConfPass.getText().toString(),
                                                        registerBinding.dcAddress.getText().toString(),
                                                        registerBinding.dcPhone1.getText().toString(),
                                                        registerBinding.dcPhone2.getText().toString(),
                                                        registerBinding.dcOccupancy.getText().toString());

                if (!isInputEmpty){
                    if (validateEmail() | validatePassword()){
                        if (registerBinding.dcNewPass.getText().toString().equals(registerBinding.dcConfPass.getText().toString())){
                            String email = registerBinding.dcAdminEmail.getText().toString();
                            String password = registerBinding.dcNewPass.getText().toString();

                            mAuth = FirebaseAuth.getInstance();

                            mAuth.createUserWithEmailAndPassword(email,password)
                                    .addOnCompleteListener(OwnerRegister.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                registerBinding.dcAdminEmail.setText("");
                                                registerBinding.dcNewPass.setText("");
                                                Toast.makeText(OwnerRegister.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                            } else {
                                                registerBinding.dcAdminEmail.setText("");
                                                registerBinding.dcNewPass.setText("");
                                                Toast.makeText(OwnerRegister.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                            dbclass.add(adminModel).addOnSuccessListener(suc -> {
                                Toast.makeText(OwnerRegister.this, "Record Inserted", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(OwnerRegister.this, OwnerLogin.class));
                            }).addOnFailureListener(er -> {
                                Toast.makeText(OwnerRegister.this, "Record Not Inserted" + er.getMessage(), Toast.LENGTH_SHORT).show();
                            });


                        }else {
                            registerBinding.dcConfPassTB.setError("Confirm Password not matching!");
                        }
                    }
                }
            }
        });

    }

    private boolean validateEmail() {

        String emailInput = registerBinding.dcAdminEmailTB.getEditText().getText().toString().trim();

        if (emailInput.isEmpty()) {
            registerBinding.dcAdminEmailTB.setError("Field can not be empty");
            registerBinding.dcAdminEmailTB.requestFocus();
            return false;
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
            registerBinding.dcAdminEmailTB.setError("Please enter a valid email address");
            registerBinding.dcAdminEmailTB.requestFocus();
            return false;
        } else {
            registerBinding.dcAdminEmailTB.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String passwordInput = registerBinding.dcNewPassTB.getEditText().getText().toString().trim();
        if (passwordInput.isEmpty()) {
            registerBinding.dcNewPassTB.setError("Field can not be empty");
            registerBinding.dcNewPassTB.requestFocus();
            return false;
        }
        else if (!PASSWORD_PATTERN.matcher(passwordInput).matches()) {
            registerBinding.dcNewPassTB.setError("Password is too weak");
            registerBinding.dcNewPassTB.requestFocus();
            return false;
        } else {
            registerBinding.dcNewPassTB.setError(null);
            return true;
        }
    }

    private Boolean validateTextFields(){
        Boolean isEmptyFields = false;

        String dcID = registerBinding.dcLogoTB.getEditText().getText().toString().trim();
        String dcName = registerBinding.dcNameTB.getEditText().getText().toString().trim();
        String dcADMIN = registerBinding.dcAdminNameTB.getEditText().getText().toString().trim();
        String dcAddress = registerBinding.dcAddressTB.getEditText().getText().toString().trim();
        String dcP1 = registerBinding.dcPhone1TB.getEditText().getText().toString().trim();
        String dcP2 = registerBinding.dcPhone2TB.getEditText().getText().toString().trim();
        String dcOcc = registerBinding.dcOccupancyTB.getEditText().getText().toString().trim();

        if (dcID == null || dcID.equalsIgnoreCase("")){
            registerBinding.dcLogoTB.setError("Fields can't be empty");
            registerBinding.dcLogoTB.requestFocus();
            isEmptyFields = true;
        }
        else {
            registerBinding.dcLogoTB.setError(null);
            isEmptyFields = false;
        }

        if (dcName == null || dcName.equalsIgnoreCase("")){
            registerBinding.dcNameTB.setError("Fields can't be empty");
            registerBinding.dcNameTB.requestFocus();
            isEmptyFields = true;
        }
        else {
            registerBinding.dcNameTB.setError(null);
            isEmptyFields = false;
        }

        if (dcADMIN == null || dcADMIN.equalsIgnoreCase("")){
            registerBinding.dcAdminNameTB.setError("Fields can't be empty");
            registerBinding.dcAdminNameTB.requestFocus();
            isEmptyFields = true;
        }
        else {
            registerBinding.dcAdminNameTB.setError(null);
            isEmptyFields = false;
        }

        if (dcAddress == null || dcAddress.equalsIgnoreCase("")){
            registerBinding.dcAddressTB.setError("Fields can't be empty");
            registerBinding.dcAddressTB.requestFocus();
            isEmptyFields = true;
        }
        else {
            registerBinding.dcAddressTB.setError(null);
            isEmptyFields = false;
        }

        if (dcP1 == null || dcP1.equalsIgnoreCase("")){
            registerBinding.dcPhone1TB.setError("Fields can't be empty");
            registerBinding.dcPhone1TB.requestFocus();
            isEmptyFields = true;
        }
        else {
            registerBinding.dcPhone1TB.setError(null);
            isEmptyFields = false;
        }

        if (dcP2 == null || dcP2.equalsIgnoreCase("")){
            registerBinding.dcPhone2TB.setError("Fields can't be empty");
            registerBinding.dcPhone2TB.requestFocus();
            isEmptyFields = true;
        }
        else {
            registerBinding.dcPhone2TB.setError(null);
            isEmptyFields = false;
        }

        if (dcOcc == null || dcOcc.equalsIgnoreCase("")){
            registerBinding.dcOccupancyTB.setError("Fields can't be empty");
            registerBinding.dcOccupancyTB.requestFocus();
            isEmptyFields = true;
        }
        else {
            registerBinding.dcOccupancyTB.setError(null);
            isEmptyFields = false;
        }

        return isEmptyFields;
    }
}