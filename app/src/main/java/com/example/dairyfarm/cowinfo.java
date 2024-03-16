package com.example.dairyfarm;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class cowinfo extends AppCompatActivity {
    private RecyclerView recyclerView;
    private DatabaseReference dbRef;
    private rvadapter rvAdapter;
    private Button addDataTableButton, b2, b3, b4;
    private Toolbar toolbar;
    private String cowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cowinfo); // Initialize toolbar
        toolbar = findViewById(R.id.toolbarcowinfo);
        setSupportActionBar(toolbar);
        b3 = findViewById(R.id.deltable);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent delDataTableIntent = new Intent(cowinfo.this, deletecowinfodatatable.class);
                startActivity(delDataTableIntent);
            }
        });
        b2 = findViewById(R.id.edittable);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editDataTableIntent = new Intent(cowinfo.this, updatecowinfoActivity.class);
                editDataTableIntent.putExtra("cowid", cowId);
                startActivity(editDataTableIntent);
            }
        });

        // Get cow ID from Intent extra
        cowId = getIntent().getStringExtra("cowid");

        // Set cow ID as the title of the toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(" " + cowId);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        // Initialize views
        recyclerView = findViewById(R.id.rv1);
        addDataTableButton = findViewById(R.id.adddatatable);

        // Set click listener to handle clicks on addDataTableButton
        addDataTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addDataTableIntent = new Intent(cowinfo.this, cowaddinfodatatable.class);
                addDataTableIntent.putExtra("cowid", cowId);
                startActivity(addDataTableIntent);
            }
        });

        // Set up RecyclerView
        setRecyclerView();
    }

    private void setRecyclerView() {
        List<Model> models = new ArrayList<>();
        rvAdapter = new rvadapter(this, models);
        recyclerView.setAdapter(rvAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        getList();
    }

    private static final String TAG = "Visha";

    private void getList() {
        dbRef = FirebaseDatabase.getInstance().getReference().child("cows").child(cowId);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    MainModel cow = null;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        cow = new MainModel();

                        if (Objects.equals(snapshot.getKey(), "cowid")) {
                            cow.setCowid(snapshot.getKey());
                        } else {
                            List<Model> infoList = new ArrayList<>();
                            for (DataSnapshot infoSnapshot : snapshot.getChildren()) {
                                Model info = infoSnapshot.getValue(Model.class); // Convert dataSnapshot to Info object
                                infoList.add(info);
                            }
                            cow.setInfo(infoList);
                        }
                    }

                    if (cow != null) {
                        rvAdapter.setRv_list(cow.getInfo()); // Set data to adapter
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                Log.d(TAG, "onCancelled: Error: " + databaseError.getMessage());
            }
        });
    }
}
