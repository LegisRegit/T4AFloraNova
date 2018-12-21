package com.example.projectt4a_floranovaapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MenuActivity extends AppCompatActivity implements MenuActivityHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);


        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, MenuFragment.newInstance()).commit();


        findViewById(R.id.lang_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO change lang
            }
        });


    }


    private void navigateTo(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        transaction.addToBackStack(null);
        transaction.replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void navigateToTransactionFragment() {
        navigateTo(TransactionFragment.newInstance());
    }

    @Override
    public void navigateToTransactionEditFragment() {
        navigateTo(TransactionEditFragment.newInstance());
    }

    @Override
    public void navigateToTransactionInfoFragment(String transactionID) {
        navigateTo(TransactionInfoFragment.newInstance(transactionID));
    }

    @Override
    public void navigateToTransactionPackagesFragment(String transactionID){
        navigateTo(TransactionPackagesFragment.newInstance(transactionID));
    }

    @Override
    public void navigateToRelationshipFragment(){
        navigateTo(RelationshipFragment.newInstance());
    }

    @Override
    public void navigateToRelationshipEditFragment(){
        navigateTo(RelationshipEditFragment.newInstance());
    }

    @Override
    public void  navigateToRelationshipInfoFragment(String key){
        navigateTo(RelationshipInfoFragment.newInstance(key));
    }
}
