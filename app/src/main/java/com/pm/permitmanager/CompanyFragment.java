package com.pm.permitmanager;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.pm.permitmanager.ArrayAdapters.CompanyArrayAdapter;
import com.pm.permitmanager.Models.Company;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link ListFragment} subclass.
 */
public class CompanyFragment extends ListFragment {

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private ChildEventListener childEventListener;
    private FirebaseAuth.AuthStateListener stateListener;
    private ArrayAdapter<Company> companiesArrayAdapter;
    private static final int RC_SIGN_IN = 123;

    private ListView listView;

    public CompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_company, container, false);
//        listView = (ListView) view.findViewById(R.id.list);
//
//
//        String[] content = {
//                "Ford",
//                "Mazda",
//                "VW",
//                "Toyota"
//        };
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, content);
//        listView.setAdapter(arrayAdapter);
//        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        companiesArrayAdapter = new CompanyArrayAdapter(getContext(), android.R.layout.simple_list_item_1, new ArrayList<Company>());
        companiesArrayAdapter.clear();
        setListAdapter(companiesArrayAdapter);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    clean();
                    List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), new AuthUI.IdpConfig.GoogleBuilder().build());

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                } else {
                    databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("companies");
                    loadCompanies();
                }
            }
        };
    }

    private void loadCompanies() {
        if (childEventListener == null) {
            childEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Company company = dataSnapshot.getValue(Company.class);
                    companiesArrayAdapter.add(company);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };
        }

        databaseReference.addChildEventListener(childEventListener);
    }

    private void clean() {
        companiesArrayAdapter.clear();
        if (childEventListener != null) {
            databaseReference.removeEventListener(childEventListener);
            childEventListener = null;
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        firebaseAuth.removeAuthStateListener(stateListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (firebaseAuth != null) {
            firebaseAuth.addAuthStateListener(stateListener);
        }
    }
}
