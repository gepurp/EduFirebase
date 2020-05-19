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
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    // UI Components declaration
    private EditText edtEmail;
    private EditText edtUsername;
    private EditText edtPassword;
    private Button btnSignIn;

    // Firebase Auth declaration
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UI Components initialization
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);

        // Firebase Auth initialization
        mAuth = FirebaseAuth.getInstance();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_menu, menu);
        menu.findItem(R.id.signOutItem).setVisible(false);
        menu.findItem(R.id.postsItem).setVisible(false);
        menu.findItem(R.id.signInItem).setVisible(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.signUpItem) {
            Intent intent = new Intent(this, SignUp.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }



    private void signIn() {
        try {
            mAuth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                Toast.makeText(MainActivity.this,
                                        "Sing in successfully",
                                        Toast.LENGTH_LONG).show();

                                switchToSocialMediaActivity();

                            } else {

                                Toast.makeText(MainActivity.this,
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
