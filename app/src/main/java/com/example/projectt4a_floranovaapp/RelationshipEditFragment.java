package com.example.projectt4a_floranovaapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RelationshipEditFragment extends Fragment {

    private View view;
    private MenuActivityHandler activityHandler;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private TextView relation;
    private Button createRelationship;

    private HashMap<String, String> dataMap;

    private String creatorCompanyID;
    private String creatorCompany;
    private String creator;
    private String relationID;
    private String relationName;
    private String status;

    private Boolean run;

    private String relationshipKey;


    public static RelationshipEditFragment newInstance() {
        return new RelationshipEditFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityHandler = (MenuActivityHandler) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.relationship_edit_fragment, container, false);

        init();

        return view;
    }

    private void init() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        relation = view.findViewById(R.id.relationCompanyName);

        dataMap = new HashMap<>();

        creator = mAuth.getCurrentUser().getUid();
        creatorCompanyID = "0";
        creatorCompany = "0";
        relationID = "0";
        relationName = "0";
        status = "1";


        DatabaseReference creaCompIDREF = mDatabase.child("Users").child(creator);
        creaCompIDREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                creatorCompanyID = dataSnapshot.child("Company").getValue().toString();

                DatabaseReference creaCompREF = mDatabase.child("Companies").child(creatorCompanyID);
                creaCompREF.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        creatorCompany = dataSnapshot.child("CompanyName").getValue().toString();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        view.findViewById(R.id.createRelationshipBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                relationName = relation.getText().toString();
                run = true;

                DatabaseReference compNameREF = mDatabase.child("Companies");
                compNameREF.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {

                        if(!run){
                            return;
                        }
                        run = false;

                        for(final DataSnapshot relatSnapshot2: dataSnapshot2.getChildren()){


                            if (relatSnapshot2.child("CompanyName").getValue().equals(relationName)){
                                relationID = relatSnapshot2.getKey();
                            }
                        }

                        //Filling the datamap for the relationship entry
                        dataMap.put("CompanyA", creatorCompanyID);
                        dataMap.put("CompanyAName", creatorCompany);
                        dataMap.put("CompanyB", relationID);
                        dataMap.put("CompanyBName", relationName);
                        dataMap.put("Status", status);

                        mDatabase.child("Relationships").push().setValue(dataMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                relationshipKey = databaseReference.getKey();

                                mDatabase.child("Companies").child(creatorCompanyID).child("Relationships").child(relationshipKey).child("Name").setValue(relationName);
                                mDatabase.child("Companies").child(relationID).child("Relationships").child(relationshipKey).child("Name").setValue(creatorCompany);

                            }
                        });


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });





            }
        });

    }



}
