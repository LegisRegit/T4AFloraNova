package com.example.projectt4a_floranovaapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class MenuFragment extends Fragment {

    private View view;
    private MenuActivityHandler activityHandler;

    public static MenuFragment newInstance() {
        return new MenuFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activityHandler = (MenuActivityHandler) context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.menu_fragment, container, false);

        init();

        return view;
    }

    private void init() {


        view.findViewById(R.id.transactionBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activityHandler.navigateToTransactionFragment();
            }
        });

        view.findViewById(R.id.logoutBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Objects.requireNonNull(getActivity()).startActivity(new Intent(getContext(), MainActivity.class));
                activityHandler.finish();
            }
        });

        view.findViewById(R.id.relationshipBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityHandler.navigateToRelationshipFragment();
            }
        });

    }

}
