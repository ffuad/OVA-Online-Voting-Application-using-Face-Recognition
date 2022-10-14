package com.project.VotingFinal2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class AdminSignUp extends AppCompatActivity implements View.OnClickListener {

    private EditText adminSignUpEmailEditText, adminSignUpPasswordEditText;
    private Button signUpButtonButton;
    private TextView adminSignInTextView;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_up);
        this.setTitle("Admin Sign Up");

        mAuth = FirebaseAuth.getInstance();

        adminSignUpEmailEditText = (EditText) findViewById(R.id.adminSignUpEmail);
        adminSignUpPasswordEditText = (EditText) findViewById(R.id.adminSignUpPassword);
        signUpButtonButton = (Button) findViewById(R.id.signUpButton);
        adminSignInTextView = (TextView) findViewById(R.id.adminSignInText);

        adminSignInTextView.setOnClickListener(this);
        signUpButtonButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.signUpButton:
                adminRegistration();
                break;

            case R.id.adminSignInText:
                Intent goToSignUpPage = new Intent(getApplicationContext(), AdminSignIn.class);
                startActivity(goToSignUpPage);
                break;
        }
    }

    private void adminRegistration() {
        String adminEmail = adminSignUpEmailEditText.getText().toString().trim();
        String adminPassword = adminSignUpPasswordEditText.getText().toString().trim();

        //checking Email and Password Input validity:
        if(adminEmail.isEmpty())
        {
            adminSignUpEmailEditText.setError("Enter an email address");
            adminSignUpEmailEditText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(adminEmail).matches())
        {
            adminSignUpEmailEditText.setError("Enter a valid email address");
            adminSignUpEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(adminPassword.isEmpty())
        {
            adminSignUpPasswordEditText.setError("Enter a password");
            adminSignUpPasswordEditText.requestFocus();
            return;
        }
        if(adminPassword.length()<6) ////
        {
            adminSignUpPasswordEditText.setError("Password should be at least 6 characters long");
            adminSignUpPasswordEditText.requestFocus();
            return;
        }

        mAuth.createUserWithEmailAndPassword(adminEmail, adminPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), AdminFirstPage.class);
                    intent.putExtra("adminEmail", adminEmail);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "User is already registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Error " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }
}