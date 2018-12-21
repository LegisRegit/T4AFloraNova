package com.example.projectt4a_floranovaapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TransactionFragment extends Fragment {

    private View view;
    private MenuActivityHandler activityHandler;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private String userCompanyID;
    private List<String> userCompanyRelationships;

    private LinearLayout layout;

    public static TransactionFragment newInstance() {
        return new TransactionFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityHandler = (MenuActivityHandler) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.trans_fragment, container, false);

        init();

        return view;
    }

    private void init() {


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        userCompanyRelationships = new ArrayList<>();


        //Load company of the user:
        DatabaseReference userCompRef = mDatabase.child("Users").child(mAuth.getCurrentUser().getUid().toString()).child("Company");
        userCompRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userCompanyID = dataSnapshot.getValue().toString();


                //Fill in the layout
                layout = view.findViewById(R.id.transactionsList);

                DatabaseReference transRef = mDatabase.child("Transactions");
                transRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        layout.removeAllViews();

                        for(final DataSnapshot transSnapshot: dataSnapshot.getChildren()){

                            if (transSnapshot.child("CreatorCompanyID").getValue().toString().equals(userCompanyID) || transSnapshot.child("Receiver").getValue().toString().equals(userCompanyID)){
                                TextView textView = new TextView(getContext());
                                textView.setText(transSnapshot.child("CreatorCompany").getValue().toString()  + "   " + transSnapshot.child("Status").getValue().toString());

                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        activityHandler.navigateToTransactionInfoFragment(transSnapshot.getKey());
                                    }
                                });

                                layout.addView(textView);

                            }
                        }


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



        view.findViewById(R.id.transactionBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityHandler.navigateToTransactionEditFragment();
            }
        });


    }

}
