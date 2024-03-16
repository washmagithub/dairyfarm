package com.example.dairyfarm;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class updatecow extends AppCompatActivity {
    private EditText edtcow;
    private Button btnsave;
    private DatabaseReference databaseReference;
    private String cowid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_cow);

        // Initialize views and Realtime Database reference
        edtcow = findViewById(R.id.updatecowid);
        btnsave = findViewById(R.id.btnsave2);
        databaseReference = FirebaseDatabase.getInstance().getReference("cows");

        // Retrieve cowId from intent
        cowid = getIntent().getStringExtra("cowid");

        // Check if cowid is null
        if (cowid ==null ) {
            Toast.makeText(this, "Cowid is null", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity if cowid is null
            return;
        }

        // Set click listener for the save button
        btnsave.setOnClickListener(v -> editCow());

        // Fetch cow data
        fetchCowData();
    }

    // Method to fetch cow data from Realtime Database
    private void fetchCowData() {
        databaseReference.child(cowid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    MainModel mainModel = dataSnapshot.getValue(MainModel.class);
                    if (mainModel != null) {
                        edtcow.setText(mainModel.getCowid());
                    } else {
                        Toast.makeText(updatecow.this, "MainModel object is null", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(updatecow.this, "Cow not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(updatecow.this, "Failed to fetch cow data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to edit cow data in Realtime Database
    private void editCow() {
        String newCowId = edtcow.getText().toString();

        if (newCowId.isEmpty()) {
            Toast.makeText(this, "Cow ID cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        databaseReference.child(cowid).child("cowid").setValue(newCowId)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(updatecow.this, "Cow updated successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Finish the activity after successful update
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(updatecow.this, "Failed to update cow", Toast.LENGTH_SHORT).show();
                });
    }
}

