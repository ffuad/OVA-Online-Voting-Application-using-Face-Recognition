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
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class VoterSeePollResult extends AppCompatActivity {

    private String uniquePollID, voterName;

    private List<NewCandidateData> CandidateDataList;
    private ListView listView;
    private CandidateListCustomAdapter customAdapter;

    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_see_poll_result);
        this.setTitle("Poll Result");  ///

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uniquePollID = bundle.getString("uniquePollID").trim();
//            pollName = bundle.getString("pollName").trim(); ///
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Polls")
                .child(uniquePollID);


        CandidateDataList = new ArrayList<>();
        listView = findViewById(R.id.CandidateListViewID);
        customAdapter = new CandidateListCustomAdapter(this, CandidateDataList); ///

    }

    @Override
    public void onStart() {

        Query query = databaseReference.child("candidate-list").orderByChild("candidateVotes");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                CandidateDataList.clear();
                for (DataSnapshot metadata1 : snapshot.getChildren()) {
                    NewCandidateData newCandidateData = metadata1.getValue(NewCandidateData.class);
                    CandidateDataList.add(newCandidateData);
                }

                listView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError dataerror) {
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
        super.onStart();

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
