package com.example.selftrainer.Login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.selftrainer.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class Signup extends AppCompatActivity {
    FirebaseFirestore db;
    FirebaseAuth mAuth;
TextInputEditText name_text,email_text,password_text;
Button Sigup,toLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_singup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        name_text = findViewById(R.id.SignupName);
        email_text = findViewById(R.id.Signupemail);
        password_text = findViewById(R.id.SignupPassword);
        Sigup = findViewById(R.id.Signupbtrn);
        toLogin = findViewById(R.id.toLogin);
        mAuth = FirebaseAuth.getInstance();

        db = FirebaseFirestore.getInstance();
        Sigup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = name_text.getText().toString();
                String email = email_text.getText().toString();
                String password = password_text.getText().toString();

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String UserId = user.getUid().toString();

                                    HashMap<String,String> map = new HashMap<>();
                                    map.put("UserName",name);
                                    map.put("email",email);
                                    map.put("Password",password);
                                    map.put("UserId",UserId);
                                    db.collection("Users").document(UserId).set(map);
                                    Intent i = new Intent(Signup.this, Login.class);
                                    startActivity(i);
                                } else {
                                    // If sign in fails, display a message to the user.

                                }
                            }
                        });
            }
        });
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Signup.this, Login.class);
                startActivity(i);
            }
        });
    }
}