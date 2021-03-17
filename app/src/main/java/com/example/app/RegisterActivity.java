package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.CycleInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.app.components.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{

    public EditText email, password, firstName, lastName;
    public Button btnSignUp;

    public FirebaseAuth mAuth;
    public FirebaseDatabase mDatabase;
    public DatabaseReference userDatabase;

    @Override
    public void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_register);

        initFirebase();

        email = (EditText)findViewById(R.id.regEmail);
        password = (EditText)findViewById(R.id.regPass);
        firstName = (EditText)findViewById(R.id.regFirstName);
        lastName = (EditText)findViewById(R.id.regLastName);

        btnSignUp = (Button)findViewById(R.id.btnReg);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int idx = v.getId();
        if (idx == R.id.btnReg) {
            signUp(email.getText().toString(), password.getText().toString());
        }
    }

    public void initFirebase() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        userDatabase = mDatabase.getReference();
    }

    /*

    Users ->
            Id ->
                    Nume
                    Prenume
                    Email
     */

    public void storeUserInDB() {
        final User user = new User(firstName.getText().toString(), lastName.getText().toString(), email.getText().toString());
        final String id = mAuth.getCurrentUser().getUid();
        userDatabase.child("Users").child(id).setValue(user).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            } else {
                String error = task.getException().getMessage();
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void signUp(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email!", Toast.LENGTH_SHORT).show();
            this.email.startAnimation(shakeError());
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter your password!", Toast.LENGTH_SHORT).show();
            this.password.startAnimation(shakeError());
            return;
        }
        if (TextUtils.isEmpty(firstName.getText().toString())) {
            Toast.makeText(this, "Please enter your first name!", Toast.LENGTH_SHORT).show();
            this.firstName.startAnimation(shakeError());
            return;
        }
        if (TextUtils.isEmpty(lastName.getText().toString())) {
            Toast.makeText(this, "Please enter your last name!", Toast.LENGTH_SHORT).show();
            this.lastName.startAnimation(shakeError());
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, task -> {
            if (task.isSuccessful()) {
                storeUserInDB();
            } else {
                String error = task.getException().getMessage();
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public TranslateAnimation shakeError() {
        TranslateAnimation shake = new TranslateAnimation(0, 100, 0, 100);
        shake.setDuration(500);
        shake.setInterpolator(new CycleInterpolator(7));
        return shake;
    }
}
