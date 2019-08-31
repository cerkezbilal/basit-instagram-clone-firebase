package com.bilal.karademir.basitinstagramclonefirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class feedActivity extends AppCompatActivity {

    ListView listView;
    postclass adapter;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference myRef;
    ArrayList<String> useremailFromFB;
    ArrayList<String> userimageFromFB;
    ArrayList<String> userzamanFromFB;
    ArrayList<String> usercommentFromFB;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_post,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.add_post){

            startActivity(new Intent(getApplicationContext(),uploadActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        listView = findViewById(R.id.listview);
        usercommentFromFB = new ArrayList<>();
        useremailFromFB = new ArrayList<>();
        userimageFromFB = new ArrayList<>();
        userzamanFromFB = new ArrayList<>();

        firebaseDatabase = FirebaseDatabase.getInstance();
        myRef = firebaseDatabase.getReference();

        adapter = new postclass(useremailFromFB,usercommentFromFB,userimageFromFB,userzamanFromFB,this);
        listView.setAdapter(adapter);
        getDataFirebase();
    }

    public void getDataFirebase(){

        DatabaseReference newReference = firebaseDatabase.getReference("Posts");
        newReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {




                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    //Log.e("deneme",ds.getValue().toString());
                    HashMap<String,String> hashMap = (HashMap<String, String>) ds.getValue();
                   // Log.e("FBV",hashMap.get("useremail"));
                    //Log.e("FBV",hashMap.get("comment"));

                    useremailFromFB.add(hashMap.get("useremail"));
                    usercommentFromFB.add(hashMap.get(("comment")));
                    userzamanFromFB.add(hashMap.get(("zaman")));
                    userimageFromFB.add(hashMap.get("downloadurl"));
                    adapter.notifyDataSetChanged();


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
