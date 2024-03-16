package com.example.dairyfarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dairyfarm.MainActivity;
import com.example.dairyfarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class sign_up extends AppCompatActivity {
    EditText e1, e2, e3, e4;

    Button b1;
    TextView t1;
    FirebaseAuth nAuth;
    DatabaseReference usersRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        nAuth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        e1 = findViewById(R.id.username);
        e2 = findViewById(R.id.email);
        e3 = findViewById(R.id.password);
        e4 = findViewById(R.id.repassword);
        t1 = findViewById(R.id.signup1);
        b1 = findViewById(R.id.register);

        t1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login = new Intent(sign_up.this, MainActivity.class);
                startActivity(login);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name, email1, password1, repassword1;
                name = e1.getText().toString();
                String namet = e1.getText().toString();
                if (!namet.matches("[a-zA-Z]+")) {
                    Toast.makeText(sign_up.this, "Only Alphabets are acceptable for Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                email1 = e2.getText().toString();
                if (!isValidEmail(email1)) {
                    Toast.makeText(sign_up.this, "Invalid email format", Toast.LENGTH_SHORT).show();
                    return;
                }

                password1 = e3.getText().toString();
                repassword1 = e4.getText().toString();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email1) || TextUtils.isEmpty(password1) || TextUtils.isEmpty(repassword1)) {
                    Toast.makeText(sign_up.this, "Fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password1.equals(repassword1)) {
                    Toast.makeText(sign_up.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    return;
                }

                nAuth.createUserWithEmailAndPassword(email1, password1)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Account created successfully
                                    Toast.makeText(sign_up.this, "Account created", Toast.LENGTH_SHORT).show();

                                    // Store user data in Realtime Database
                                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    Map<String, Object> userData = new HashMap<>();
                                    userData.put("username", name);
                                    userData.put("email", email1);
                                    userData.put("password",password1);
                                    userData.put("repassword",repassword1);

                                    usersRef.child(userId).setValue(userData)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.d("sign_up", "User data added to Realtime Database successfully");
                                                    } else {
                                                        Log.e("sign_up", "Error adding user data to Realtime Database", task.getException());
                                                    }
                                                }
                                            });

                                    Intent login = new Intent(sign_up.this, MainActivity.class);
                                    startActivity(login);
                                } else {
                                    // Authentication failed
                                    Toast.makeText(sign_up.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
