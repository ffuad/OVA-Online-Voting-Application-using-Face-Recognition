package com.project.VotingFinal2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class AdminFirstPage extends AppCompatActivity implements View.OnClickListener {

    private Button CreateNewPoll, EditExistingPolls, PollResults;
    private String adminEmail;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_first_page);
        this.setTitle("Admin First Page");

        mAuth = FirebaseAuth.getInstance();


        CreateNewPoll = (Button) findViewById(R.id.CreateNewPollID);
        EditExistingPolls = (Button) findViewById(R.id.EditExistingPollsID);
        PollResults = (Button) findViewById(R.id.PollResultsforAdminID);

        CreateNewPoll.setOnClickListener(this);
        EditExistingPolls.setOnClickListener(this);
        PollResults.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null) {
            adminEmail = bundle.getString("adminEmail");

            StringBuffer output = new StringBuffer();
            for (int i = 0; i <adminEmail.length(); i++) {
                if(adminEmail.charAt(i)=='@')
                    break;
                output.append(adminEmail.charAt(i));
            }
            adminEmail=output.toString();
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.CreateNewPollID:
                Intent intent = new Intent(getApplicationContext(), CreatePoll.class);
                startActivity(intent);
                intent.putExtra("adminEmail", adminEmail);
                break;

            case R.id.EditExistingPollsID:
                Intent pollintent = new Intent(getApplicationContext(), PollList.class);
                pollintent.putExtra("adminEmail", adminEmail);
                startActivity(pollintent);
                break;

            case R.id.PollResultsforAdminID:

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

        if(item.getItemId()==R.id.signOutMenuItemId)
        {
            FirebaseAuth.getInstance().signOut();
            finish();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}