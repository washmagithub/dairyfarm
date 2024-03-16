package com.example.dairyfarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dairyfarm.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity"; // Add this line for logging

    private EditText e1, e2;
    private FirebaseAuth auth;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
        usersRef = FirebaseDatabase.getInstance().getReference().child("users");

        e1 = findViewById(R.id.email);
        e2 = findViewById(R.id.password);
        Button btn1 = findViewById(R.id.login_button);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = e1.getText().toString();
                String pass = e2.getText().toString();
                if (user.isEmpty()) {
                    e1.setError("Fill this field");
                }
                if (pass.isEmpty()) {
                    e2.setError("Please fill this field");
                } else {
                    auth.signInWithEmailAndPassword(user, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
                                FirebaseUser firebaseUser= auth.getCurrentUser();
                                firebaseUser.sendEmailVerification();
                                // Store user data in Realtime Database
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                String userEmail = e1.getText().toString();

                                Map<String, Object> userData = new HashMap<>();
                                userData.put("email", userEmail);

                                // Add the user data to Realtime Database
                                usersRef.child(userId).setValue(userData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d(TAG, "User data added to Realtime Database successfully");
                                                // Navigate to the Home activity
                                                Intent home = new Intent(getApplicationContext(), Home.class);
                                                startActivity(home);
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "Error adding user data to Realtime Database", e);
                                                // Handle the failure appropriately
                                            }
                                        });
                            } else {
                                Toast.makeText(MainActivity.this, "SignIn Failed" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        TextView signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign = new Intent(getApplicationContext(),sign_up.class);
                startActivity(sign);
            }
        });
    }
}
