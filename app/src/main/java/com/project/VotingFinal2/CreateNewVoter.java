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

public class CreateNewVoter extends AppCompatActivity implements View.OnClickListener {

    private EditText VoterNameEditText, VoterEmailEditText;
    private ImageView FrontImageView, RightImageView, LeftImageView;
    private Button ChooseFrontImageButton, ChooseRightImageButton, ChooseLeftImageButton, CreateNewVoterButton;
    private Uri ImageUriFront, ImageUriRight, ImageUriLeft;
    private ProgressBar progressBar;

    private String uniquePollID;

    DatabaseReference databaseReference;
    StorageReference storageReference;

    StorageTask uploadTask;

    private static final int IMAGE_REQUEST1 = 1; /// wondering what static does here
//    private static final int IMAGE_REQUEST2 = 2; /// wondering what static does here
//    private static final int IMAGE_REQUEST3 = 3; /// wondering what static does here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_voter);
        this.setTitle("Create New Voter");

        VoterNameEditText = findViewById(R.id.EnterVoterNameID);
        VoterEmailEditText = findViewById(R.id.EnterVoterEmailID);

        FrontImageView = findViewById(R.id.FrontImageViewID);
//        RightImageView = findViewById(R.id.RightImageViewID);
//        LeftImageView = findViewById(R.id.LeftImageViewID);

        ChooseFrontImageButton = findViewById(R.id.ChooseFrontImageID);
//        ChooseRightImageButton = findViewById(R.id.ChooseRightImageID);
//        ChooseLeftImageButton = findViewById(R.id.ChooseLeftImageID);

        CreateNewVoterButton = findViewById(R.id.CreateNewVoterButtonID);

        ChooseFrontImageButton.setOnClickListener(this);
//        ChooseRightImageButton.setOnClickListener(this);
//        ChooseLeftImageButton.setOnClickListener(this);

        CreateNewVoterButton.setOnClickListener(this);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uniquePollID = bundle.getString("uniquePollID");
        }

        //        progressBar = findViewById(R.id.ProgressBarID);
//
        storageReference = FirebaseStorage.getInstance().getReference()
                .child(uniquePollID).child("Voters");

        databaseReference = FirebaseDatabase.getInstance().getReference();

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ChooseFrontImageID:
                openFrontFileChooser();
                break;

//            case R.id.ChooseRightImageID:
//                openRightFileChooser();
//                break;
//
//            case R.id.ChooseLeftImageID:
//                openLeftFileChooser();
//                break;

            case R.id.CreateNewVoterButtonID:
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

//    void openRightFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_REQUEST2);
//    }
//
//    void openLeftFileChooser() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, IMAGE_REQUEST3);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (IMAGE_REQUEST1 == requestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {
            ImageUriFront = data.getData();
            Picasso.get().load(ImageUriFront).into(FrontImageView);
        }

//        if (IMAGE_REQUEST2 == requestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            ImageUriRight = data.getData();
//            Picasso.get().load(ImageUriRight).into(RightImageView);
//        }
//
//        if (IMAGE_REQUEST3 == requestCode && resultCode == RESULT_OK && data != null && data.getData() != null) {
//            ImageUriLeft = data.getData();
//            Picasso.get().load(ImageUriLeft).into(LeftImageView);
//        }
    }


    private void saveData() {
        String voterName = VoterNameEditText.getText().toString().trim();
        String voterEmail = VoterEmailEditText.getText().toString().trim();

        if (voterName.isEmpty()) {
            VoterNameEditText.setError("Enter the voter name");
            VoterNameEditText.requestFocus();
            return;
        }
        if (voterEmail.isEmpty()) {
            VoterEmailEditText.setError("Enter the voter email");
            VoterEmailEditText.requestFocus();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(voterEmail).matches()) {
            VoterEmailEditText.setError("Enter a valid email address");
            VoterEmailEditText.requestFocus();
            return;
        }
//
        StorageReference stref1 = storageReference.child(voterEmail).child(System.currentTimeMillis() + "." + getFileExtension(ImageUriFront));
//        StorageReference stref2 = storageReference.child(voterEmail).child(System.currentTimeMillis() + "." + getFileExtension(ImageUriRight));
//        StorageReference stref3 = storageReference.child(voterEmail).child(System.currentTimeMillis() + "." + getFileExtension(ImageUriLeft));
////

        final Uri[] downloadUrlFront = new Uri[1];
//        final Uri[] downloadUrlRight = new Uri[1];
//        final Uri[] downloadUrlLeft = new Uri[1];

        String finalVoterEmail = voterEmail;

        StringBuffer output = new StringBuffer();
        for (int i = 0; i < voterEmail.length(); i++) {
            if (voterEmail.charAt(i) == '@')
                break;
            output.append(voterEmail.charAt(i));
        }
        voterEmail = output.toString();
        String finalVoterEmail2 = voterEmail;

        stref1.putFile(ImageUriFront)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getApplicationContext(), "Images Successfully Stored", Toast.LENGTH_LONG).show();

                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();

                        while (!urlTask.isSuccessful()) ;

                        downloadUrlFront[0] = urlTask.getResult();

                        NewVoterData newVoterData = new NewVoterData(finalVoterEmail, voterName, downloadUrlFront[0].toString());

                        Map<String, Object> childUpdates = new HashMap<>();

                        String key = databaseReference.child("Polls").child(uniquePollID)
                                .child("voter-list").push().getKey();

                        childUpdates.put("/Polls/" + uniquePollID + "/voter-list/" + key, newVoterData);

                        childUpdates.put("/Voters/" + finalVoterEmail2 + "/" + uniquePollID, 0);

                        databaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                VoterNameEditText.setText("");
                                VoterEmailEditText.setText("");
                                Toast.makeText(getApplicationContext(), "Voter Data Saved", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getApplicationContext(), EditVoterList.class);
                                intent.putExtra("uniquePollID", uniquePollID);
                                startActivity(intent);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Voter Data Failed", Toast.LENGTH_LONG).show();
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

//        stref2.putFile(ImageUriRight)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getApplicationContext(), "Right Image Successfully Stored", Toast.LENGTH_LONG).show();
//
//                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
//
//                        while(!urlTask.isSuccessful());
//
//                        downloadUrlRight[0] = urlTask.getResult();
//
//                    }
//
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Left Image Storing Failed", Toast.LENGTH_LONG).show();
//                    }
//                });
//
//        stref3.putFile(ImageUriLeft)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        Toast.makeText(getApplicationContext(), "Left Image Successfully Stored", Toast.LENGTH_LONG).show();
//
//                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
//
//                        while(!urlTask.isSuccessful());
//
//                        downloadUrlLeft[0] = urlTask.getResult();
//
//                    }
//
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(getApplicationContext(), "Left Image Storing Failed", Toast.LENGTH_LONG).show();
//                    }
//                });

//        Toast.makeText(this, "front image is "+downloadUrlFront[0].toString(), Toast.LENGTH_SHORT).show();

//        NewVoterData newVoterData = new NewVoterData(voterEmail, voterName, downloadUrlFront[0].toString(), downloadUrlRight[0].toString(), downloadUrlLeft[0].toString());
//
//        Map<String, Object> childUpdates = new HashMap<>();
//
//        String key = databaseReference.child("Polls").child(uniquePollID)
//                .child("voter-list").push().getKey();
//
//        childUpdates.put("/Polls/"+uniquePollID+"/voter-list/"+key, newVoterData);
//        childUpdates.put("/Voters/"+voterEmail+"/"+uniquePollID, 0);
//
//        databaseReference.updateChildren(childUpdates).addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                VoterNameEditText.setText("");
//                VoterEmailEditText.setText("");
//                Toast.makeText(getApplicationContext(), "Voter Data Saved", Toast.LENGTH_LONG).show();
////                Intent intent = new Intent(getApplicationContext(), PollList.class);
////                intent.putExtra("adminEmail", adminEmail);
////                startActivity(intent);
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(getApplicationContext(), "Voter Data Failed", Toast.LENGTH_LONG).show();
//            }
//        });

    }

}