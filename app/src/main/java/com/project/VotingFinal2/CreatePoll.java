package com.project.VotingFinal2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class CreatePoll extends AppCompatActivity {

    String adminEmail, uniquePollID;
    EditText pollName;
    Button createPollButton;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll);
        this.setTitle("Create New Poll");



        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            adminEmail = bundle.getString("adminEmail").trim();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        pollName = (EditText) findViewById(R.id.EnterPollNameID);
        createPollButton = (Button) findViewById(R.id.CreatePollButtonID);

        createPollButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });

    }

    @Override
    protected void onStart() {
        uniquePollID = UUID.randomUUID().toString().substring(0,8);
        Toast.makeText(this, uniquePollID, Toast.LENGTH_LONG).show();

        super.onStart();
    }

    private void saveData() {
        String pollNamestr = pollName.getText().toString().trim();

        PollMetaData pollMetaData = new PollMetaData(pollNamestr, uniquePollID, adminEmail, 0);

        Map<String, Object> childUpdates = new HashMap<>();

        String key = databaseReference.child("Admins").child(adminEmail).child("polls").push().getKey();
        childUpdates.put("/Polls/"+uniquePollID+"/poll-meta", pollMetaData);
        childUpdates.put("/Admins/"+adminEmail+"/polls/"+key, pollMetaData);

        databaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pollName.setText("");
                Toast.makeText(getApplicationContext(), "Poll Creation Successful", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), PollList.class);
                intent.putExtra("adminEmail", adminEmail);
                startActivity(intent);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Poll Creation Failed", Toast.LENGTH_LONG).show();
            }
        });

    }
}
