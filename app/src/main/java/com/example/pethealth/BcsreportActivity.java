package com.example.pethealth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pethealth.fragments.PetAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BcsreportActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private RecyclerView recyclerView;
    private List<bcsgroup> bcsItems = new ArrayList<>();
    private List<String> uidList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcsreport);

        mDatabase = FirebaseDatabase.getInstance();
        mReference = mDatabase.getReference();
        recyclerView = findViewById(R.id.recyclerview3);


        Intent intent = getIntent();
        int position = intent.getIntExtra("position", 0);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String the_uid = user.getUid();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this){
            @Override
            public boolean canScrollVertically() {
                return false;//??????????????? ??????
            }
        };



        recyclerView.setLayoutManager(layoutManager);
        final bcsAdapter BcsAdapter = new bcsAdapter(bcsItems, uidList);
        recyclerView.setAdapter(BcsAdapter);//????????? ?????????






            mDatabase.getReference().child(the_uid).child("PetAccount").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        //????????? ?????? DataSnapshot ?????? ????????????.
                        //???????????? ????????? ?????????  clear()
                        //bcsItems.clear();
                        uidList.clear();
                        for(DataSnapshot ds : dataSnapshot.getChildren())           //?????? ?????? ????????? ?????????
                        {
                            bcsgroup bcsItem = ds.getValue(bcsgroup.class);
                            String uidKey = ds.getKey();

                            //bcsItems.add(bcsItem);
                            uidList.add(uidKey);
                        }

                        mDatabase.getReference().child(the_uid).child("PetAccount").child(uidList.get(position)).child("BcsReport").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {  //????????? ?????? DataSnapshot ?????? ????????????.
                                //???????????? ????????? ?????????  clear()
                                bcsItems.clear();
                                //uidList.clear();
                                for(DataSnapshot ds : dataSnapshot.getChildren())           //?????? ?????? ????????? ?????????
                                {
                                    bcsgroup bcsItem = ds.getValue(bcsgroup.class);
                                    String uidKey = ds.getKey();

                                    bcsItems.add(bcsItem);
                                    //uidList.add(uidKey);
                                }
                                BcsAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    } catch ( IndexOutOfBoundsException e) {

                    }

                    //BcsAdapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });





/*
        mReference = mDatabase.getReference("PetAccount" + the_uid); // ???????????? ????????? child ??????
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();

                for (DataSnapshot messageData : dataSnapshot.getChildren()) {
                    String msg2 = messageData.getValue().toString();
                    Array.add(msg2);
                    adapter.add(msg2);
                    // child ?????? ?????? ??????????????? ???????????????.

                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/








    }



}




