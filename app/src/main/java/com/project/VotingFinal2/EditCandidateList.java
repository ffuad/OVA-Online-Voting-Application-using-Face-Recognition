package com.project.VotingFinal2;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class EditCandidateList extends AppCompatActivity {

    private String uniquePollID;

    private List<NewCandidateData> CandidateDataList;
    private ListView listView;
    private CandidateListCustomAdapter customAdapter;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_candidate_list);
        this.setTitle("Candidate List");  ///

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uniquePollID = bundle.getString("uniquePollID").trim();
//            pollName = bundle.getString("pollName").trim(); ///
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("Polls")
                .child(uniquePollID);

        CandidateDataList = new ArrayList<>();
        listView = findViewById(R.id.CandidateListViewID);
        customAdapter = new CandidateListCustomAdapter(EditCandidateList.this, CandidateDataList); ///

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
//
                listView.setAdapter(customAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                        String value = pollsListViewMembers.get(position).getUniquePollID();
//                        String pollName = pollsListViewMembers.get(position).getPollname();
//                        Intent intent = new Intent(PollList.this, EditPoll.class);
//                        intent.putExtra("uniquePollID", value);
//                        intent.putExtra("pollName", pollName);
//                        startActivity(intent);
                        Toast.makeText(getApplicationContext(), "Clicked on candidate" + position, Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        });
        super.onStart();
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

}
