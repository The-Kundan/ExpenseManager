package com.example.emanager.views.activites;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.emanager.R;

public class LoginAcivity extends AppCompatActivity {

    EditText edUsername, edPassword;
    Button btn;
    TextView tv;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_acivity);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", null);

        if (username != null) {
            // User is already logged in, redirect to MainActivity
            startActivity(new Intent(LoginAcivity.this, MainActivity.class));
            finish(); // Close LoginActivity
            return; // Avoid executing further code
        }

        setContentView(R.layout.activity_login_acivity);


        edUsername =findViewById(R.id.editTextLoginUsername);
        edPassword =findViewById(R.id.editTextLoginPassword);
        btn =findViewById(R.id.buttonLogin);
        tv =findViewById(R.id.textViewNewUser);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username =edUsername.getText().toString();
                String password =edPassword.getText().toString();
                Database db = new Database(getApplicationContext(),"healthcare",null,1);
                if (username.length() == 0 || password.length()==0) {
                    Toast.makeText(getApplicationContext() , "Please fill the details to log in ", Toast.LENGTH_SHORT).show();
                }else {
                    if (db.login(username,password)==1){
                        Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
                        SharedPreferences sharedPreferences =getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        // saving our data with key and value
                        editor.apply();
                        startActivity(new Intent(LoginAcivity.this,MainActivity.class));
                        finish();
                    }else {
                        Toast.makeText(getApplicationContext(), "Invalid Username and Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                This is explicit intent which is used for user defined activity
                startActivity(new Intent(LoginAcivity.this,RegisterActivity.class));
            }
        });

    }
}