package com.example.projectt4a_floranovaapp;

public interface MenuActivityHandler {

    void navigateToTransactionFragment();

    void navigateToTransactionEditFragment();

    void navigateToTransactionInfoFragment(String transactionID);

    void navigateToTransactionPackagesFragment(String transactionID);

    void navigateToRelationshipFragment();

    void navigateToRelationshipEditFragment();

    void navigateToRelationshipInfoFragment(String key);

    void finish();
}
