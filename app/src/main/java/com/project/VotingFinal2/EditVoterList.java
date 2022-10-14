package com.project.VotingFinal2;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

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

import java.util.ArrayList;
import java.util.List;

public class EditVoterList extends AppCompatActivity {

    private String uniquePollID, voterEmailsToSend, pollname;
    private StringBuffer voterEmailsBuffer = new StringBuffer();

    private List<NewVoterData> VoterDataList;
    private ListView listView;
    private VoterListCustomAdapter customAdapter;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_voter_list);
        this.setTitle("Voter List");  ///

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uniquePollID = bundle.getString("uniquePollID").trim();
//            pollName = bundle.getString("pollName").trim(); ///
        }

//        databaseReference = FirebaseDatabase.getInstance().getReference("Polls")
//                .child(uniquePollID).child("voter-list");

        databaseReference = FirebaseDatabase.getInstance().getReference("Polls")
                .child(uniquePollID);

        VoterDataList = new ArrayList<>();
        listView = findViewById(R.id.VoterListViewID);
        customAdapter = new VoterListCustomAdapter(EditVoterList.this, VoterDataList);

    }

    @Override
    public void onStart() {

        databaseReference.child("voter-list").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                VoterDataList.clear();
                for (DataSnapshot metadata1 : snapshot.getChildren()) {
                    NewVoterData voterData = metadata1.getValue(NewVoterData.class);
                    VoterDataList.add(voterData);

                    voterEmailsBuffer.append(voterData.getVoterEmail());
                    voterEmailsBuffer.append(", ");

                }
//
                voterEmailsToSend = voterEmailsBuffer.toString(); ///
//                Toast.makeText(getApplicationContext(), "voter emails " + voterEmailsToSend, Toast.LENGTH_SHORT).show();

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
                        Toast.makeText(EditVoterList.this, "Clicked on voter name", Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_layout_2, menu);

        MenuItem menuItem = menu.findItem(R.id.VoterListSearchViewID);
        SearchView searchView = (SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                customAdapter.getFilter().filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId()==R.id.signOutMenuItemId2)
        {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        if(item.getItemId()==R.id.SendEmailIconID)
        {

//            Task<DataSnapshot> metadata = databaseReference.child("poll-meta").child("pollname").get();

            Intent email = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
//                email.putExtra(Intent.EXTRA_EMAIL, new String[]{"fj@gmail.com, f@g.com, "});
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{voterEmailsToSend});
            email.putExtra(Intent.EXTRA_SUBJECT, "Password for Poll "); ///+ send poll name
            email.putExtra(Intent.EXTRA_TEXT, "The password for you is " + uniquePollID);

            //need this to prompts email client only
            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        }
        return super.onOptionsItemSelected(item);
    }

}
