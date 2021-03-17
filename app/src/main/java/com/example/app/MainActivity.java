package com.example.app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public Button btnSignIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (Button)findViewById(R.id.btnLogin);
        btnSignUp = (Button)findViewById(R.id.btnRegister);

        btnSignIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int idx = v.getId();
        if (idx == R.id.btnLogin) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        } else if (idx == R.id.btnRegister) {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));
        }
    }
}