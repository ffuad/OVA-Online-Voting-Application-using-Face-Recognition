package com.project.VotingFinal2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class CandidateListForVoter extends AppCompatActivity {

    private String uniquePollID, voterName;

    private List<NewCandidateData> CandidateDataList;
    private ListView listView;
    private VoterSeeCandidateListCustomAdapter customAdapter;

    DatabaseReference databaseReference, secDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_candidate_list_for_voter);
        this.setTitle("Candidate List");  ///

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uniquePollID = bundle.getString("uniquePollID").trim();
            voterName = bundle.getString("voterName").trim();
//            pollName = bundle.getString("pollName").trim(); ///
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Polls")
                .child(uniquePollID);


        CandidateDataList = new ArrayList<>();
        listView = findViewById(R.id.CandidateListViewID);
        customAdapter = new VoterSeeCandidateListCustomAdapter(this, CandidateDataList); ///

    }

    @Override
    public void onStart() {

        databaseReference.child("candidate-list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                CandidateDataList.clear();
                for (DataSnapshot metadata1 : snapshot.getChildren()) {
                    NewCandidateData newCandidateData = metadata1.getValue(NewCandidateData.class);
                    CandidateDataList.add(newCandidateData);
                }

                listView.setAdapter(customAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                        Toast.makeText(getApplicationContext(), "Clicked on candidate" + position, Toast.LENGTH_LONG).show();
                        new AlertDialog.Builder(CandidateListForVoter.this)
                                .setTitle("Confirmation")
                                .setMessage("Submit your vote?")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton(R.string.Yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int whichButton) {
                                        //
                                        String candidateName = CandidateDataList.get(position).getCandidateName();
                                        databaseReference.child("candidate-list").child(candidateName).runTransaction(new Transaction.Handler() {
                                            @Override
                                            public Transaction.Result doTransaction(MutableData mutableData) {
                                                NewCandidateData p = mutableData.getValue(NewCandidateData.class);
                                                if (p == null) {
                                                    return Transaction.success(mutableData);
                                                }

//                                                Toast.makeText(getApplicationContext(), p.getCandidateVotes(), Toast.LENGTH_LONG).show();
                                                p.setCandidateVotes(p.getCandidateVotes() + 1);


                                                // Set value and report transaction success
                                                mutableData.setValue(p);
                                                return Transaction.success(mutableData);
                                            }

                                            @Override
                                            public void onComplete(DatabaseError databaseError, boolean committed,
                                                                   DataSnapshot currentData) {
                                                // Transaction completed

                                                secDB = FirebaseDatabase.getInstance().getReference("Voters")
                                                        .child(voterName).child(uniquePollID);
                                                secDB.setValue(1);
                                                Toast.makeText(getApplicationContext(), "Vote Submitted", Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(CandidateListForVoter.this, VoterFirstPage.class);
                                                intent.putExtra("uniquePollID", uniquePollID);
                                                intent.putExtra("voterEmail", voterName);
                                                startActivity(intent);

                                            }
                                        });
                                    }
                                })
                                .setNegativeButton(R.string.Discard, null).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
        super.

                onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_layout_3, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.signOutMenuItemId) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
