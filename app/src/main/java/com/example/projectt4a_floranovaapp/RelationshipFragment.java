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

public class RelationshipFragment extends Fragment {

    private View view;
    private MenuActivityHandler activityHandler;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private String userCompanyID;
    private List<String> userCompanyRelationships;

    private LinearLayout layout;

    public static RelationshipFragment newInstance() {
        return new RelationshipFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityHandler = (MenuActivityHandler) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.relationship_fragment, container, false);

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


                layout = view.findViewById(R.id.relationshipsList);

                DatabaseReference relatRef = mDatabase.child("Relationships");
                relatRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        layout.removeAllViews();

                        for(final DataSnapshot relatSnapshot: dataSnapshot.getChildren()){

                            if (relatSnapshot.child("CompanyA").getValue().toString().equals(userCompanyID) || relatSnapshot.child("CompanyB").getValue().toString().equals(userCompanyID)){
                                TextView textView = new TextView(getContext());
                                textView.setText(relatSnapshot.child("CompanyAName").getValue().toString()  + "   " + relatSnapshot.child("CompanyBName").getValue().toString());

                                textView.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        activityHandler.navigateToRelationshipInfoFragment(relatSnapshot.getKey());
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






        view.findViewById(R.id.relationshipBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityHandler.navigateToRelationshipEditFragment();
            }
        });


    }

}
