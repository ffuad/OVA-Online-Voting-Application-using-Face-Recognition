package com.project.VotingFinal2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VoterSignIn extends AppCompatActivity implements View.OnClickListener {

    private EditText VoterSignInEmailEditText, VoterSignInPasswordEditText;
    private Button VoterSignInButtonButton;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_sign_in);
        this.setTitle("Voter Sign In");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Voters");

        VoterSignInEmailEditText = (EditText) findViewById(R.id.VoterSignInEmailID);
        VoterSignInPasswordEditText = (EditText) findViewById(R.id.VoterSignInPasswordID);
        VoterSignInButtonButton = (Button) findViewById(R.id.VoterSignInButtonID);

        VoterSignInButtonButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.VoterSignInButtonID) {
            VoterSignIn();
        }
    }

    private void VoterSignIn() {
        String voterEmail = VoterSignInEmailEditText.getText().toString().trim();
        String voterPassword = VoterSignInPasswordEditText.getText().toString().trim();

        //checking Email and Password Input validity:
        if (voterEmail.isEmpty()) {
            VoterSignInEmailEditText.setError("Enter an email address");
            VoterSignInEmailEditText.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(voterEmail).matches()) {
            VoterSignInEmailEditText.setError("Enter a valid email address");
            VoterSignInEmailEditText.requestFocus();
            return;
        }

        //checking the validity of the password
        if (voterPassword.isEmpty()) {
            VoterSignInPasswordEditText.setError("Enter a password");
            VoterSignInPasswordEditText.requestFocus();
            return;
        }
        if (voterPassword.length() < 6) {
            VoterSignInPasswordEditText.setError("Password should be at least 6 characters long");
            VoterSignInPasswordEditText.requestFocus();
            return;
        }

        String finalVoterEmail2 = voterEmail; /// sent untrimmed email name to the next page

        StringBuffer output = new StringBuffer();
        for (int i = 0; i < voterEmail.length(); i++) {
            if (voterEmail.charAt(i) == '@')
                break;
            output.append(voterEmail.charAt(i));
        }
        voterEmail = output.toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Voters");

        String finalVoterEmail = voterEmail;


        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(finalVoterEmail)) {
                    // run some code
                    if (dataSnapshot.child(finalVoterEmail).hasChild(voterPassword)) {
//                        Intent intent = new Intent(getApplicationContext(), VoterFirstPage.class);
                        Intent intent = new Intent(getApplicationContext(), VoterRecognitionInput.class);
                        intent.putExtra("uniquePollID", voterPassword);
                        intent.putExtra("voterEmail", finalVoterEmail2);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        databaseReference.removeEventListener(this);
                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(), "Voter is not allowed", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(), "Voter does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Database Read Error", Toast.LENGTH_SHORT).show();
            }
        };
        databaseReference.addValueEventListener(valueEventListener);

    }
}