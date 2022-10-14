package com.project.VotingFinal2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;


public class EditPoll extends AppCompatActivity implements View.OnClickListener {

    private Button EditCandidateList, CreateNewCandidate, EditVoterList, CreateNewVoter;
    private TextView PollNameTextView;
    private String uniquePollID, pollName;

    DatabaseReference databaseReference;

    private Switch aSwitch;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_poll);
        this.setTitle("Edit Poll Page");

        mAuth = FirebaseAuth.getInstance();


        CreateNewVoter = (Button) findViewById(R.id.CreateNewVoterID);
        EditVoterList = (Button) findViewById(R.id.EditVoterListID);
        CreateNewCandidate = (Button) findViewById(R.id.CreateNewCandidateID);
        EditCandidateList = (Button) findViewById(R.id.EditCandidateListID);
        PollNameTextView = (TextView) findViewById(R.id.PollNameonEditPollID);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uniquePollID = bundle.getString("uniquePollID");
            pollName = bundle.getString("pollName");
        }

        PollNameTextView.setText(pollName);

        aSwitch = (Switch) findViewById(R.id.switchID);

        CreateNewVoter.setOnClickListener(this);
        EditVoterList.setOnClickListener(this);
        CreateNewCandidate.setOnClickListener(this);
        EditCandidateList.setOnClickListener(this);

        databaseReference = FirebaseDatabase.getInstance().getReference()
                .child("Polls")
                .child(uniquePollID)
                .child("poll-meta");

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    databaseReference.child("poll_switch").setValue(0); // turned 0 i.e. from off to on
                } else
                    databaseReference.child("poll_switch").setValue(1); // turned 1 i.e. from on to off
            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.CreateNewVoterID:
                Intent intent = new Intent(EditPoll.this, CreateNewVoter.class);
                intent.putExtra("uniquePollID", uniquePollID);
                startActivity(intent);
                break;

            case R.id.EditVoterListID:
                Intent pollintent = new Intent(EditPoll.this, EditVoterList.class);
                pollintent.putExtra("uniquePollID", uniquePollID);
//                pollintent.putExtra("pollName", pollName);
                startActivity(pollintent);
                break;

            case R.id.CreateNewCandidateID:
                Intent candiintent = new Intent(getApplicationContext(), CreatePollCandidate.class);
                candiintent.putExtra("uniquePollID", uniquePollID);
                startActivity(candiintent);
                break;
            case R.id.EditCandidateListID:
                Intent editcandiintent = new Intent(getApplicationContext(), EditCandidateList.class);
                editcandiintent.putExtra("uniquePollID", uniquePollID);
                startActivity(editcandiintent);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_layout_1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.signOutMenuItemId) {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int x = snapshot.child("poll_switch").getValue(Integer.class);
                if(x==0) {
                    aSwitch.setChecked(true); /// On
                }
                else {
                    aSwitch.setChecked(false); /// Off
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditPoll.this, "Switch Toggling Failed", Toast.LENGTH_SHORT).show();
            }
        });
        super.onStart();
    }
}