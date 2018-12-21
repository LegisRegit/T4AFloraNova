package com.example.projectt4a_floranovaapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmailField = findViewById(R.id.emailField);
        mPasswordField = findViewById(R.id.passwordField);

        Button mLoginBtn = findViewById(R.id.loginBtn);

        mAuth = FirebaseAuth.getInstance();

        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    // User is signed in
                    Intent mainIntent = new Intent(MainActivity.this, MenuActivity.class);

                    startActivity(mainIntent);


                } else {
                    // User is signed out
                    Log.d("User Sign Out", "onAuthStateChanged:signed_out");
                    //sign out
                }

            }
        };

        mLoginBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Toast toast = Toast.makeText(MainActivity.this, "Logging In", Toast.LENGTH_LONG);
                toast.show();
                signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
            }

        });


    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }

    private void signIn(String email, String password) {


        Log.d("Signing_In", "signIn:" + email);


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Log-In_Success", " Verification : signIn With Email:onComplete:" + task.isSuccessful());


                // If sign in fails, display a message to the user.
                if (!task.isSuccessful()) {
                    Toast toast = Toast.makeText(MainActivity.this, "Login Failed. Please check your credentials", Toast.LENGTH_LONG);
                    toast.show();
                }

            }
            });

    }

}

