package com.project.VotingFinal2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button AdminOptionButtonButton;
    private Button VoterOptionButtonButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setTitle("Welcome");


        AdminOptionButtonButton = (Button) findViewById(R.id.AdminOptionButton);
        VoterOptionButtonButton = (Button) findViewById(R.id.VoterOptionButton);

        AdminOptionButtonButton.setOnClickListener(this);
        VoterOptionButtonButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.AdminOptionButton:
                Intent goToAdminSignIn = new Intent(getApplicationContext(), AdminSignIn.class);
                startActivity(goToAdminSignIn);
                break;

            case R.id.VoterOptionButton:
                Intent intent = new Intent(getApplicationContext(), VoterSignIn.class);
                startActivity(intent);
//                finish();
                break;
        }
    }

}