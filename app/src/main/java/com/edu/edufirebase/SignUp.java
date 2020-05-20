package com.edu.edufirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    // UI Components declaration
    private EditText edtEmailSignUp;
    private EditText edtUserNameSignUp;
    private EditText edtPasswordSignUp;
    private Button btnSignUp;

    // Firebase Auth declaration
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtEmailSignUp = findViewById(R.id.edtEmailSignUp);
        edtUserNameSignUp = findViewById(R.id.edtUserNameSignUp);
        edtPasswordSignUp = findViewById(R.id.edtPasswordSignUp);
        btnSignUp = findViewById(R.id.btnSignUp);

        // Firebase Auth initialization
        mAuth = FirebaseAuth.getInstance();

        // Call sing up method after clicking the sing up button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUp();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        menu.findItem(R.id.signOutItem).setVisible(false);
        menu.findItem(R.id.signUpItem).setVisible(false);
        menu.findItem(R.id.postsItem).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signInItem) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private void singUp() {
        try {
            // Sing Up new users
            mAuth.createUserWithEmailAndPassword(edtEmailSignUp.getText().toString(), edtPasswordSignUp.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                // Sing up success, update UI with the singed up user's information
                                Toast.makeText(SignUp.this,
                                        "Sing up successfully",
                                        Toast.LENGTH_LONG).show();

                                FirebaseDatabase.getInstance().getReference()
                                        .child("users").child(task.getResult().getUser().getUid())
                                        .child("username").setValue(edtUserNameSignUp.getText().toString());

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(edtUserNameSignUp.getText().toString())
                                        .build();

                                FirebaseAuth.getInstance().getCurrentUser().updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SignUp.this,
                                                            "Profile Updated",
                                                            Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });

                                switchToSocialMediaActivity();

                            } else {

                                Toast.makeText(SignUp.this,
                                        "Authentication Error",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
        }
    }

    private void switchToSocialMediaActivity() {
        Intent intent = new Intent(this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
