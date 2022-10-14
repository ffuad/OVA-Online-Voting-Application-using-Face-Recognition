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

public class AdminSignIn extends AppCompatActivity implements View.OnClickListener {

    private EditText adminSignInEmailEditText, adminSignInPasswordEditText;
    private Button signInButtonButton;
    private TextView adminSignUpTextView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sign_in);
        this.setTitle("Admin Sign In");

        mAuth = FirebaseAuth.getInstance();

        adminSignInEmailEditText = (EditText) findViewById(R.id.adminSignInEmail);
        adminSignInPasswordEditText = (EditText) findViewById(R.id.adminSignInPassword);
        signInButtonButton = (Button) findViewById(R.id.adminSignInButton);
        adminSignUpTextView = (TextView) findViewById(R.id.adminSignUpText);

        adminSignUpTextView.setOnClickListener(this);
        signInButtonButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.adminSignInButton:
                adminSignIn();
                break;

            case R.id.adminSignUpText:
                Intent goToSignUpPage = new Intent(getApplicationContext(), AdminSignUp.class);
                startActivity(goToSignUpPage);
                break;
        }
    }

    private void adminSignIn() {
        String adminEmail = adminSignInEmailEditText.getText().toString().trim();
        String adminPassword = adminSignInPasswordEditText.getText().toString().trim();

        //checking Email and Password Input validity:
        if(adminEmail.isEmpty())
        {
            adminSignInEmailEditText.setError("Enter an email address");
            adminSignInEmailEditText.requestFocus();
            return;
        }

        if(!android.util.Patterns.EMAIL_ADDRESS.matcher(adminEmail).matches())
        {
            adminSignInEmailEditText.setError("Enter a valid email address");
            adminSignInEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if(adminPassword.isEmpty())
        {
            adminSignInPasswordEditText.setError("Enter a password");
            adminSignInPasswordEditText.requestFocus();
            return;
        }
        if(adminPassword.length()<6)
        {
            adminSignInPasswordEditText.setError("Password should be at least 6 characters long");
            adminSignInPasswordEditText.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(adminEmail, adminPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(getApplicationContext(), AdminFirstPage.class);
                    intent.putExtra("adminEmail", adminEmail);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Sign in failed", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}