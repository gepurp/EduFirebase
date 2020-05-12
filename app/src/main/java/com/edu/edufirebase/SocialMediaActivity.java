package com.edu.edufirebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.UUID;

public class SocialMediaActivity extends AppCompatActivity {

    // Firebase Auth declaration
    private FirebaseAuth mAuth;

    // UI Components declaration
    private ImageView imgViewPost;
    private Button btnPostImg;
    private EditText edtImgDescription;
    private ListView listViewUsers;

    // Bitmap declaration
    private Bitmap bitmap;

    // Unique id for each image
    private String imageIdentifier;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_social_media);

        // Firebase Auth initialization
        mAuth = FirebaseAuth.getInstance();

        // UI components initialization
        imgViewPost = findViewById(R.id.imgViewPost);
        btnPostImg = findViewById(R.id.btnPostImg);
        edtImgDescription = findViewById(R.id.edtImgDescription);
        listViewUsers = findViewById(R.id.listViewUsers);

        // Set click listener to the components
        imgViewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                selectImage();

            }
        });

        btnPostImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadImgToServer();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.signOutItem:
                signOut();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        signOut();
    }

    private void signOut() {
        mAuth.signOut();
        finish();
    }

    // Select image from gallery
    private void selectImage() {

        // Check the SDK version
        if (Build.VERSION.SDK_INT < 23) {

            // Access to the external storage
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 1000);

        } else if (Build.VERSION.SDK_INT >= 23) {

            // Check the permission was granted or not
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // Ask permission
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, 1000);

            } else {

                // Access to the external storage
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1000);

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            selectImage();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000
                && resultCode == RESULT_OK
                && data != null) {

            Uri chosenImage = data.getData();

            try {

                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), chosenImage);
                imgViewPost.setImageBitmap(bitmap);

            } catch (Exception e) {

                Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();

            }
        }
    }

    private void uploadImgToServer() {

        // Get the data from an imgViewPost as bytes
        imgViewPost.setDrawingCacheEnabled(true);
        imgViewPost.buildDrawingCache();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] data = byteArrayOutputStream.toByteArray();

        // Initializing unique id for each image
        imageIdentifier = UUID.randomUUID() + ".jpeg";

        UploadTask uploadTask = FirebaseStorage.getInstance().
                getReference().child("images").child(imageIdentifier).putBytes(data);

        // Set failure listener in case of unsuccessful uploads
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SocialMediaActivity.this, "Error: " + e, Toast.LENGTH_LONG).show();
            }
        });

        // Set success listener for doing something in case of success
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(SocialMediaActivity.this, "Success", Toast.LENGTH_LONG).show();
            }
        });

    }
}
