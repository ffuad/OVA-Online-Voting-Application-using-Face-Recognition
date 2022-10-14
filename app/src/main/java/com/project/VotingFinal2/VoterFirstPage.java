package com.project.VotingFinal2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class VoterFirstPage extends AppCompatActivity implements View.OnClickListener {

    private Button VoteButton, SeeResultButton;
    private String uniquePollID, voterEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_first_page);
        this.setTitle("Voter First Page");

        VoteButton = (Button) findViewById(R.id.VoteInThisPollID);
        SeeResultButton = (Button) findViewById(R.id.SeeThisPollResultID);

        VoteButton.setOnClickListener(this);
        SeeResultButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();

        if(bundle!=null) {
            uniquePollID = bundle.getString("uniquePollID");
            voterEmail = bundle.getString("voterEmail");

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.VoteInThisPollID:
                vote();
                break;

            case R.id.SeeThisPollResultID:
                Intent pollintent = new Intent(getApplicationContext(), VoterSeePollResult.class);
                pollintent.putExtra("uniquePollID", uniquePollID);
                startActivity(pollintent);

                break;

        }
    }

    private void vote() {

        StringBuffer output = new StringBuffer();
        for (int i = 0; i < voterEmail.length(); i++) {
            if (voterEmail.charAt(i) == '@')
                break;
            output.append(voterEmail.charAt(i));
        }
        voterEmail = output.toString();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Voters");

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int x  = dataSnapshot.child(voterEmail).child(uniquePollID).getValue(Integer.class);
                if (x==0) {
//                    Toast.makeText(getApplicationContext(), "Value is 0", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), CandidateListForVoter.class);
                    intent.putExtra("uniquePollID", uniquePollID);
                    intent.putExtra("voterName", voterEmail);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "You have already voted", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(), VoterFirstPage.class);
//                    intent.putExtra("uniquePollID", uniquePollID);
//                    intent.putExtra("voterName", voterEmail);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
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