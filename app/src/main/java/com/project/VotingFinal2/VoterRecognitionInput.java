package com.project.VotingFinal2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.ByteArrayOutputStream;
import java.util.Base64;

public class VoterRecognitionInput extends AppCompatActivity {

    public static final int CAMERA_PERM_CODE = 101;
    public static final int CAMERA_PERM_CODE2 = 103;
    public static final int CAMERA_REQUEST_CODE = 102;
    public static final int CAMERA_REQUEST_CODE2 = 104;

    ImageView selectedImage, selectedImage2;
    Button Camerabtn, Submitbtn, Camerabtn2;

    BitmapDrawable bitmapDrawable, bitmapDrawable2;
    Bitmap bitmap, bitmap2;
    String RegistrationImageString = "", checkingImageString = "";
    Bitmap image;

    String uniquePollID, voterEmail;
    DatabaseReference databaseReference;

    public Target target;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_recognition_input);
        this.setTitle("Face Recognition");


        selectedImage = findViewById(R.id.imageView);
        selectedImage2 = findViewById(R.id.imageView1);

        selectedImage2 .setVisibility(View.GONE);

        Camerabtn = findViewById(R.id.camerabtn1);

        Submitbtn = findViewById(R.id.submitbtn);

//        Camerabtn2 = findViewById(R.id.camerabtn2);


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uniquePollID = bundle.getString("uniquePollID").trim();
            voterEmail = bundle.getString("voterEmail");
//            pollName = bundle.getString("pollName").trim(); ///
        }

        target = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                selectedImage2.setImageBitmap(bitmap);
//                checkingImageString = getStringImage(bitmap);
//////////////////////////////////
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };

//        @Override
//        public void onDestroy() {  // could be in onPause or onStop
//            Picasso.with(this).cancelRequest(target);
//            super.onDestroy();
//        }

        databaseReference = FirebaseDatabase.getInstance().getReference("Polls")
                .child(uniquePollID);


        Camerabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VoterRecognitionInput.this, "Camera Button is clicked", Toast.LENGTH_SHORT).show();
                askCameraPermission();
            }
        });
//        Camerabtn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(VoterRecognitionInput.this,"Camera Button 2 is clicked",Toast.LENGTH_SHORT).show();
//                askCameraPermission1();
//            }
//        });

        if (!Python.isStarted()) {               //checking python
            Python.start(new AndroidPlatform(this));
        }

        final Python py = Python.getInstance();

        Submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //by clicking get image from image view
                // this image from the camera. At first I will take image from it camera then
                // convert into bitmapDrawable, then bitmap array then string
                bitmapDrawable = (BitmapDrawable) selectedImage.getDrawable();
                bitmap = bitmapDrawable.getBitmap();
                RegistrationImageString = getStringImage(bitmap);

                ////checkingImageString = RegistrationImageString;
//////////////////////////////////////////////////////


                //Here the prev registration image will take from firebase into a bitmap then convert into string.
                bitmapDrawable2 = (BitmapDrawable) selectedImage2.getDrawable();
                bitmap2 = bitmapDrawable2.getBitmap();
                checkingImageString = getStringImage(bitmap2);

                //in imageString we get encoded image
                //passing the string into python script
                PyObject pyObject = py.getModule("scripts");
                PyObject obj = pyObject.callAttr("main", RegistrationImageString, checkingImageString);

                int check = obj.toInt();

                if (check == 1) {
                    Toast.makeText(VoterRecognitionInput.this, "Faces are same", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), VoterFirstPage.class);
                    intent.putExtra("uniquePollID", uniquePollID);
                    intent.putExtra("voterEmail", voterEmail);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(VoterRecognitionInput.this, "Faces are different", Toast.LENGTH_LONG).show();
                }

                //converted to string
                //String string = obj.toString();
                //converted to byte array
                //byte data[] = android.util.Base64.decode(string,0);
                //converted to bitmap
                //Bitmap returnBitmap = BitmapFactory.decodeByteArray(data,0,data.length);

                //now set bitmap to image view
                //selectedImage.setImageBitmap(returnBitmap);

            }
        });

    }

//    private void openActivity2() {
//        Intent intent = new Intent(this, Activity2nd.class);
//        startActivity(intent);
//    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        //storing in byte array
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        //encoding string
        String encodedImage = android.util.Base64.encodeToString(imageBytes, 0);
        return encodedImage;
    }

    private void askCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }

//    private void askCameraPermission1() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERM_CODE2);
//        } else {
//            openCamera1();
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERM_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "Camera Permission Required.", Toast.LENGTH_SHORT).show();
            }
        }
//        if (requestCode == CAMERA_PERM_CODE2) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                openCamera1();
//            } else {
//                Toast.makeText(this, "Camera Permission Required.", Toast.LENGTH_SHORT).show();
//            }
//        }
    }

    private void openCamera() {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(camera, CAMERA_REQUEST_CODE);
    }

//    private void openCamera1() {
//        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(camera, CAMERA_REQUEST_CODE2);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            image = (Bitmap) data.getExtras().get("data");
            selectedImage.setImageBitmap(image);
        }
//        if (requestCode == CAMERA_REQUEST_CODE2) {
//            image = (Bitmap) data.getExtras().get("data");
//            selectedImage2.setImageBitmap(image);
//        }
    }


        @Override
        public void onStart() {
            databaseReference.child("voter-list").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

//                VoterDataList.clear();
                    for (DataSnapshot metadata1 : snapshot.getChildren()) {
                        NewVoterData voterData = metadata1.getValue(NewVoterData.class);
//                    VoterDataList.add(voterData);
                        if (voterData.getVoterEmail().equals(voterEmail)) {

                            Picasso.get()
                                    .load(voterData.getVoterFrontImage())
                                    .placeholder(R.mipmap.ic_launcher_round)
                                    .rotate(270) /// use rotate function
                                    .into(target);

                            Submitbtn.setVisibility(View.VISIBLE);

                            databaseReference.child("voter-list").removeEventListener(this);
                            break;
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
//                Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            });

            super.onStart();
        }

}




