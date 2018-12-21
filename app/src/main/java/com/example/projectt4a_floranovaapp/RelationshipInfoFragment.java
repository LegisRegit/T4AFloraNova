package com.example.projectt4a_floranovaapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RelationshipInfoFragment extends Fragment {

    private View view;
    private MenuActivityHandler activityHandler;

    private DatabaseReference mDatabase;

    private TextView relationName;
    private EditText internalCode;
    private Button changeStatus;
    private String status;

    private static String relationshipKey;


    public static RelationshipInfoFragment newInstance(String key) {
        relationshipKey = key;
        return new RelationshipInfoFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityHandler = (MenuActivityHandler) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.relationship_info_fragment, container, false);

        init();

        return view;
    }

    private void init() {

        mDatabase = FirebaseDatabase.getInstance().getReference();

        relationName = view.findViewById(R.id.relationText);
        internalCode = view.findViewById(R.id.internalCode);
        changeStatus = view.findViewById(R.id.statusChangeBtn);
        status = "1";

        DatabaseReference selectTransRef = mDatabase.child("Relationships").child(relationshipKey);
        selectTransRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                relationName.setText(dataSnapshot.child("CompanyA").getValue().toString());
                internalCode.setText(dataSnapshot.child("InternalCode").getValue().toString());
                if(dataSnapshot.child("Status").getValue().toString().equals("1")) {
                    changeStatus.setText("Terminate Relationship");
                    changeStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mDatabase.child("Relationships").child(relationshipKey).child("Status").setValue("0");
                            changeStatus.setText("Accept Relationship");
                        }
                    });
                }
                if(!dataSnapshot.child("Status").getValue().toString().equals("1")){
                    changeStatus.setText("Accept Relationship");
                    changeStatus.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            mDatabase.child("Relationships").child(relationshipKey).child("Status").setValue("1");
                            changeStatus.setText("Terminate Relationship");
                        }
                    });
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
