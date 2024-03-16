package com.example.dairyfarm;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Home extends AppCompatActivity {

    FloatingActionButton fab;
    private List<MainModel> cowList = new ArrayList<>();
    private RecyclerView recyclerView;
    private Adapter adapter;
    private SearchView searchView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        searchView=findViewById(R.id.search);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

            private void filterList(String text) {
                List<MainModel> filteredList = new ArrayList<>();
                for (MainModel mainModel : cowList) {
                    if (mainModel.getCowid().toLowerCase().contains(text.toLowerCase())) {
                        filteredList.add(mainModel); // Add the matching item to the filtered list
                    }
                }
                if (filteredList.isEmpty()) {
                    Toast.makeText(Home.this, "No data found", Toast.LENGTH_SHORT).show(); // Corrected toast message
                } else {
                    adapter.setFilteredList(filteredList); // Use the adapter's method to set the filtered list
                }
            }
        });

        fab = findViewById(R.id.fab);

        recyclerView = findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter(cowList, new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(MainModel item) {
                // Handle item click here
                String cowId = item.getCowid();
                Intent cowinfoIntent = new Intent(Home.this, cowinfo.class);
                cowinfoIntent.putExtra("cowid", cowId);
                startActivity(cowinfoIntent);
            }
        });
        recyclerView.setAdapter(adapter);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("cows");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Set<String> uniqueCowIds = new HashSet<>(); // Use a set to store unique cow IDs
                cowList.clear(); // Clear the list before adding new data
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MainModel mainModel = snapshot.getValue(MainModel.class);
                    if (mainModel != null && !uniqueCowIds.contains(mainModel.getCowid())) {
                        cowList.add(mainModel);
                        uniqueCowIds.add(mainModel.getCowid()); // Add the cow ID to the set to ensure uniqueness
                        Log.d("CowData", "Cow ID: " + mainModel.getCowid());
                    }
                }
                adapter.notifyDataSetChanged(); // Notify the adapter that the data has changed
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("FirebaseError", "Error listening for data: ", databaseError.toException());
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cowIntent = new Intent(Home.this, Add_Cow.class);
                startActivity(cowIntent);
            }
        });
    }
}
