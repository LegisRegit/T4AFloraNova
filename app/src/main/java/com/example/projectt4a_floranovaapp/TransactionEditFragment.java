package com.example.projectt4a_floranovaapp;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class TransactionEditFragment extends Fragment {

    private View view;
    private MenuActivityHandler activityHandler;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private HashMap<String, String> mPackagings;
    private TextView packaging;
    private TextView packagingAmount;
    private TextView receiverID;
    private TextView transporterID;
    private TextView remark;
    private String departured;
    private String creator;
    private String status;
    private String creatorCompany;
    private String creatorCompanyID;

    private HashMap<String, String> dataMap;

    private String transKey;


    public static TransactionEditFragment newInstance() {
        return new TransactionEditFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityHandler = (MenuActivityHandler) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.trans_edit_fragment, container, false);

        init();

        return view;
    }

    private void init() {

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        mPackagings = new HashMap<>();
        dataMap = new HashMap<>();

        receiverID = view.findViewById(R.id.receiverID);
        transporterID = view.findViewById(R.id.transporterID);
        remark = view.findViewById(R.id.remark);
        packaging = view.findViewById(R.id.packaging);
        packagingAmount = view.findViewById(R.id.packagingAmount);
        departured = "";
        status = "Created";
        creator = mAuth.getCurrentUser().getUid();
        creatorCompanyID = "0";
        creatorCompany = "0";


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



        view.findViewById(R.id.addPackagingBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPackagings.put(packaging.getText().toString(), packagingAmount.getText().toString());
            }
        });
        view.findViewById(R.id.createTransactionBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //Filling the datamap for the transaction entry
                dataMap.put("Creator", creator);
                dataMap.put("CreatorCompany", creatorCompany);
                dataMap.put("CreatorCompanyID", creatorCompanyID);
                dataMap.put("Receiver", receiverID.getText().toString());
                dataMap.put("Remark", remark.getText().toString());
                dataMap.put("Status", status);
                dataMap.put("Transporter", transporterID.getText().toString());
                dataMap.put("Departured", departured);

                mDatabase.child("Transactions").push().setValue(dataMap, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                transKey = databaseReference.getKey();


                                Iterator<Map.Entry<String, String>> it = mPackagings.entrySet().iterator();
                                while (it.hasNext()) {
                                    Map.Entry<String, String> pair = (Map.Entry<String, String>) it.next();
                                    mDatabase.child("Transactions").child(transKey).child("Packages").child(pair.getKey()).setValue(pair.getValue());
                                }
                            }
                        });

            }
        });

    }

}
