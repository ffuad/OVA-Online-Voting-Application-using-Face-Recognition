package com.project.VotingFinal2;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PollList extends AppCompatActivity {

    private String adminEmail;

    private List<PollMetaData> pollsListViewMembers;
    private ListView listView;
    private PollListCustomAdapter customAdapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_poll_list);
        this.setTitle("List of Created Polls");  ///

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            adminEmail = bundle.getString("adminEmail").trim();
            StringBuffer output = new StringBuffer();
            for (int i = 0; i < adminEmail.length(); i++) {
                if (adminEmail.charAt(i) == '@')
                    break;
                output.append(adminEmail.charAt(i));
            }
            adminEmail = output.toString();
        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Admins")
                .child(adminEmail).child("polls");

        pollsListViewMembers = new ArrayList<>();
        listView = findViewById(R.id.PollListViewID);
        customAdapter = new PollListCustomAdapter(PollList.this, pollsListViewMembers);

    }

    @Override
    public void onStart() {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                pollsListViewMembers.clear();
                for (DataSnapshot metadata1 : snapshot.getChildren()) {
                    PollMetaData pollMetaData = metadata1.getValue(PollMetaData.class);
                    pollsListViewMembers.add(pollMetaData);
                }
//                Toast.makeText(PollList.this, "From one " + pollsListViewMembers.size(), Toast.LENGTH_SHORT).show();
                listView.setAdapter(customAdapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        String value = pollsListViewMembers.get(position).getUniquePollID();
                        String pollName = pollsListViewMembers.get(position).getPollname();
                        Intent intent = new Intent(PollList.this, EditPoll.class);
                        intent.putExtra("uniquePollID", value);
                        intent.putExtra("pollName", pollName);
                        startActivity(intent);
//                        Toast.makeText(PollList.this, "Clicked on "+value , Toast.LENGTH_LONG).show();
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

}