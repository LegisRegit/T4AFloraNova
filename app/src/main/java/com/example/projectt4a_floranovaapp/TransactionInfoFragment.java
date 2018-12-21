package com.example.projectt4a_floranovaapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TransactionInfoFragment extends Fragment {

    private View view;
    private MenuActivityHandler activityHandler;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private TextView mStatus;
    private TextView mCreator;
    private TextView mReceiver;
    private TextView mTransporter;
    //private TextView mCourier;
    //private TextView mDepartured;

    private static String thisTransactionID;
    //Set this to the ID of the transaction that was clicked

    public static TransactionInfoFragment newInstance(String transactionID) {
        thisTransactionID = transactionID;
        return new TransactionInfoFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityHandler = (MenuActivityHandler) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.trans_info_fragment, container, false);

        init();

        return view;
    }

    private void init() {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        mCreator = (TextView) view.findViewById(R.id.creator);
        mReceiver = (TextView) view.findViewById(R.id.receiver);
        mStatus = (TextView) view.findViewById(R.id.status);
        mTransporter = (TextView) view.findViewById(R.id.transporter);
        //mCourier = (TextView) view.findViewById(R.id.courier);
        //mDepartured = (TextView) view.findViewById(R.id.departured);

        DatabaseReference selectTransRef = mDatabase.child("Transactions").child(thisTransactionID);
        selectTransRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {
                mCreator.setText(dataSnapshot.child("Creator").getValue().toString());
                mReceiver.setText(dataSnapshot.child("Receiver").getValue().toString());
                mStatus.setText(dataSnapshot.child("Status").getValue().toString());
                mTransporter.setText(dataSnapshot.child("Transporter").getValue().toString());
                //mDepartured.setText(dataSnapshot.child("Departured").getValue().toString());

                view.findViewById(R.id.remarkBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), dataSnapshot.child("Remark").getValue().toString(), Toast.LENGTH_LONG).show();
                    }
                });

                view.findViewById(R.id.saveBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mDatabase.child("Transactions").child(thisTransactionID).child("Status").setValue(mStatus.getText().toString());
                    }
                });

                view.findViewById(R.id.viewPackagesBtn).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activityHandler.navigateToTransactionPackagesFragment(thisTransactionID);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
