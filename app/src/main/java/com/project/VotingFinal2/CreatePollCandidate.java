package com.project.VotingFinal2;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CreatePollCandidate extends AppCompatActivity implements View.OnClickListener {

    private EditText CandidateName;
    private ImageView CandidateLogoImageView;
    private Button ChooseCandidateLogoButton, CreateandSaveCandidateButton;

    private Uri ImageUriCandidateLogo;
    private ProgressBar progressBar;

    private String uniquePollID;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    StorageTask uploadTask;

    private static final int IMAGE_REQUEST1 = 1; /// wondering what static does here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_poll_candidate);
        this.setTitle("Create New Candidate");


        CandidateName = findViewById(R.id.EnterCandidateNameID);
        CandidateLogoImageView = findViewById(R.id.CandidateLogoImageID);
        ChooseCandidateLogoButton = findViewById(R.id.ChooseCandidateLogoforPollID);
        CreateandSaveCandidateButton = findViewById(R.id.CreateandSaveCandidateID);


        ChooseCandidateLogoButton.setOnClickListener(this);
        CreateandSaveCandidateButton.setOnClickListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uniquePollID = bundle.getString("uniquePollID");
        }

        //        progressBar = findViewById(R.id.ProgressBarID);
//
        storageReference = FirebaseStorage.getInstance().getReference()
                .child(uniquePollID).child("candidate-list");

        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ChooseCandidateLogoforPollID:
                openFrontFileChooser();
                break;

            case R.id.CreateandSaveCandidateID:
                if (uploadTask != null && uploadTask.isInProgress()) {
                    Toast.makeText(getApplicationContext(), "Uploading in Progress", Toast.LENGTH_SHORT).show();
                    //To not repeat multiple upload clicks
                } else {
                    saveData();
                }
                break;
        }
    }

    //     getting image extension
    public String getFileExtension(Uri ImageUri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(ImageUri));
    }

    void openFrontFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, IMAGE_REQUEST1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (IMAGE_REQUEST1 == requestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUriCandidateLogo = data.getData();
            Picasso.get().load(ImageUriCandidateLogo).into(CandidateLogoImageView);
        }

    }

    private void saveData() {
        String candidateName = CandidateName.getText().toString().trim();

        if (candidateName.isEmpty()) {
            CandidateName.setError("Enter the voter name");
            CandidateName.requestFocus();
            return;
        }
//
        StorageReference stref1 = storageReference.child(candidateName).child(System.currentTimeMillis() + "." + getFileExtension(ImageUriCandidateLogo));

        final Uri[] downloadUrlFront = new Uri[1];

        stref1.putFile(ImageUriCandidateLogo)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Images Successfully Stored", Toast.LENGTH_LONG).show();

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!urlTask.isSuccessful()) ;

                        downloadUrlFront[0] = urlTask.getResult();

                        NewCandidateData newCandidateData = new NewCandidateData(candidateName, downloadUrlFront[0].toString(), 0);
                        databaseReference.child("Polls").child(uniquePollID).child("candidate-list").child(candidateName).setValue(newCandidateData)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        CandidateName.setText("");
                                        Toast.makeText(getApplicationContext(), "Candidate Data Saved", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(getApplicationContext(), EditCandidateList.class);
                                        intent.putExtra("uniquePollID", uniquePollID);
                                        startActivity(intent);
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Candidate Data Failed", Toast.LENGTH_LONG).show();
                            }
                        });


                    }

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Images Storing Failed", Toast.LENGTH_LONG).show();
                    }
                });

    }
}
