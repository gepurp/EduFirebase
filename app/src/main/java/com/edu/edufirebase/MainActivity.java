package com.edu.edufirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    // UI Components declaration
    private EditText edtEmail;
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnSingUp;
    private Button btnSignIn;

    // Firebase Auth declaration
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Components initialization
        edtEmail = findViewById(R.id.edtEmail);
        edtUsername = findViewById(R.id.edtUsername);
        edtPassword = findViewById(R.id.edtPassword);
        btnSingUp = findViewById(R.id.btnSingUp);
        btnSignIn = findViewById(R.id.btnSignIn);

        // Firebase Auth initialization
        mAuth = FirebaseAuth.getInstance();

        // Call sing up method after clicking the sing up button
        btnSingUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singUp();
            }
        });

        // Call sign in method after clicking the sign in button
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is singed in and update UI
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // Update activity state or maybe switch to the another activity
        }
    }

    private void singUp() {

        // Check the empty fields for user's info
        if ((edtEmail.getText() != null) || (edtPassword.getText() != null)) {

            // Sing Up new users
            mAuth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                // Sing in success, update UI with the singed in user's information
                                //Log.i("INFO", "createUserWithEmail: success");
                                //FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(MainActivity.this,
                                        "Sing up successfully",
                                        Toast.LENGTH_LONG).show();

                                switchToSocialMediaActivity();

                            } else {
                                Toast.makeText(MainActivity.this,
                                        "Authentication Error",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        } else {
            // Show message if some filed is empty
            Toast.makeText(MainActivity.this, "All fields must be filled", Toast.LENGTH_LONG).show();
        }

    }

    private void signIn() {
        mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this,
                            "Sing in successfully",
                            Toast.LENGTH_LONG).show();

                    //FirebaseDatabase.getInstance().getReference().child("users").child();

                    switchToSocialMediaActivity();

                } else {
                    Toast.makeText(MainActivity.this,
                            "Authentication Error",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void switchToSocialMediaActivity() {
        Intent intent = new Intent(this, SocialMediaActivity.class);
        startActivity(intent);
    }
}
