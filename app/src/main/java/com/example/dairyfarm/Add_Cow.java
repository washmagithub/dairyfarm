package com.example.dairyfarm;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Add_Cow extends AppCompatActivity {
    private EditText edtName;
    private Button save;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cow);

        save = findViewById(R.id.save);
        edtName = findViewById(R.id.cowid);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("cows");

        save.setOnClickListener(v -> saveCow());
    }

    private void saveCow() {
        String cowid = edtName.getText().toString();

        if (TextUtils.isEmpty(cowid)) {
            Toast.makeText(this, "All fields must be filled", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the cow ID already exists
        databaseReference.orderByChild("cowid").equalTo(cowid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Toast.makeText(Add_Cow.this, "This cow ID already exists", Toast.LENGTH_SHORT).show();
                } else {
                    // Generate a unique key for the new cow
                    //String cowKey = databaseReference.push().getKey();

                    MainModel cowModel = new MainModel();
                    cowModel.setCowid(cowid);
                    cowModel.setInfo(new ArrayList<>());

                    // Use the generated key to set the value in the database
                    databaseReference.child(cowid).setValue(cowModel)
                            .addOnSuccessListener(aVoid -> {
                                showNotification("Success", "Cow saved successfully");
                                finish();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(Add_Cow.this, "Error saving cow", Toast.LENGTH_SHORT).show();
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error checking cow ID: ", databaseError.toException());
            }
        });
    }


    private void showNotification(String title, String message) {
        // Implement notification functionality here if needed
    }
}

