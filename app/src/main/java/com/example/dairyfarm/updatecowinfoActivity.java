package com.example.dairyfarm;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;

public class updatecowinfoActivity extends AppCompatActivity {

    final Calendar myCalender = Calendar.getInstance();
    private DatabaseReference mDatabase;
    private Spinner drname, drno, dss;
    private ArrayAdapter<String> drnameAdapter, drnoAdapter, dssAdapter;
    private ProgressDialog progressDialog;
    private String mainCowId = "";

    Button btnedit;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatecowinfo);

        mainCowId = getIntent().getStringExtra("cowid");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("cows");

        EditText b1 = findViewById(R.id.cid);
        EditText b4 = findViewById(R.id.date);
        Button b6 = findViewById(R.id.createNow);

        // Spinner Initialization
        drname = findViewById(R.id.dn);
        drno = findViewById(R.id.dno);
        dss = findViewById(R.id.dss);
        drnameAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.doctor_names));
        drnoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.doctor_numbers));
        dssAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.diseases_list));
        drnameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drnoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dssAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drname.setAdapter(drnameAdapter);
        drno.setAdapter(drnoAdapter);
        dss.setAdapter(dssAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving data...");

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(updatecowinfoActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        myCalender.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        myCalender.set(Calendar.MONTH, month);
                        myCalender.set(Calendar.YEAR, year);
                        // Update EditText with selected date
                        b4.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
                    }
                }, myCalender.get(Calendar.YEAR), myCalender.get(Calendar.MONTH), myCalender.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        b6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                String cowId = b1.getText().toString();
                String displayName = drname.getSelectedItem().toString();
                String displayNumber = drno.getSelectedItem().toString();
                String date = b4.getText().toString(); // Get selected date from EditText
                String description = dss.getSelectedItem().toString();

                // Check if any field is empty
                if (cowId.isEmpty() || displayName.isEmpty() || displayNumber.isEmpty() || date.isEmpty() || description.isEmpty()) {
                    // Show error message if any field is empty
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                // Validate cow ID
                if (!isValidCowId(cowId)) {
                    // Cow ID is invalid, show error message
                    Toast.makeText(getApplicationContext(), "Please enter a valid numeric cow ID", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                // Validate display number
                if (!isValidDisplayNumber(displayNumber)) {
                    // Display number is invalid, show error message
                    Toast.makeText(getApplicationContext(), "Please enter a valid 11-digit display number", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    return;
                }

                // Check if cow ID already exists
                mDatabase.orderByChild("cid").equalTo(cowId).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // Cow ID already exists, show error message
                            Toast.makeText(updatecowinfoActivity.this, "Index already exists", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else {
                            // Cow ID is unique, proceed with saving the data
                            //Model model = new Model(cowId, displayName, displayNumber, date, description);

                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("dname", displayName);
                            hashMap.put("dr", displayNumber);
                            hashMap.put("dt", date);
                            hashMap.put("dis", description);

                            mDatabase.child(mainCowId).child("info").child(cowId).updateChildren(hashMap).addOnCompleteListener(task -> {
                                progressDialog.dismiss(); // Dismiss progress dialog
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Data saved", Toast.LENGTH_LONG).show();
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra("cowid", cowId); // Pass cow ID back to Home activity
                                    setResult(RESULT_OK, resultIntent);
                                    finish(); // Finish current activity
                                } else {
                                    Toast.makeText(getApplicationContext(), "Failed to save data", Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle database error
                    }
                });
            }
        });
    }

    private boolean isValidCowId(String cowId) {
        // Regular expression to match only digits
        String regex = "\\d+";
        return cowId.matches(regex);
    }

    private boolean isValidDisplayNumber(String displayNumber) {
        // Check if displayNumber contains only digits and has a length of 11
        return displayNumber.matches("\\d{11}");
    }
}
