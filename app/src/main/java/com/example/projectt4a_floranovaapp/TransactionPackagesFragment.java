package com.example.projectt4a_floranovaapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;


public class TransactionPackagesFragment extends Fragment {
    private View view;
    private MenuActivityHandler activityHandler;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private LinearLayout layout;


    private static String thisTransactionID;
    //Set this to the ID of the transaction that was clicked

    public static TransactionPackagesFragment newInstance(String transactionID) {
        thisTransactionID = transactionID;
        return new TransactionPackagesFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityHandler = (MenuActivityHandler) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.trans_packages_fragment, container, false);

        init();

        return view;
    }

    private void init() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        layout = view.findViewById(R.id.packagesView);

        final DatabaseReference selectTransRef = mDatabase.child("Transactions").child(thisTransactionID).child("Packages");
        selectTransRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                layout.removeAllViews();

                for(final DataSnapshot transSnapshot: dataSnapshot.getChildren()){

                    TextView packages = new TextView(getContext());
                    EditText amounts = new EditText(getContext());
                    packages.setText(transSnapshot.getKey().toString());
                    amounts.setText(transSnapshot.getValue().toString());

                    layout.addView(packages);
                    layout.addView(amounts);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        view.findViewById(R.id.savePackagesBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, String> packageMap = new HashMap<>();

                int i = 0;

                while (i < layout.getChildCount()){
                    TextView text1 = (TextView) layout.getChildAt(i);
                    TextView text2 = (TextView) layout.getChildAt(i+1);
                    packageMap.put(text1.getText().toString(), text2.getText().toString());
                    i = i+2;
                }

                selectTransRef.setValue(packageMap);
            }
        });

    }
}
