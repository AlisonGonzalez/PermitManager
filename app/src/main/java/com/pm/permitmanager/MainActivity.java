package com.pm.permitmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {
    TabLayout tabLayout;
    ViewPager viewPager;
    PagerAdapter pagerAdapter;
    private static final int RC_SIGN_IN = 123;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener stateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Empresas"));
        tabLayout.addTab(tabLayout.newTab().setText("Ventas"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        stateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser != null) {
                    Toast.makeText(getApplicationContext(), "Log In Successful", Toast.LENGTH_LONG).show();
                    databaseReference = firebaseDatabase.getReference().child("companies").child(firebaseAuth.getCurrentUser().getUid());
                } else {
                    List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), new AuthUI.IdpConfig.GoogleBuilder().build());

                    startActivityForResult(
                            AuthUI.getInstance()
                                    .createSignInIntentBuilder()
                                    .setAvailableProviders(providers)
                                    .build(),
                            RC_SIGN_IN);
                }
            }
        };

        viewPager = (ViewPager) findViewById(R.id.pager);
        Pager adapter = new Pager(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            // get prompts.xml view
            LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);

            View promptView = layoutInflater.inflate(R.layout.prompts, null);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

            // set prompts.xml to be the layout file of the alertdialog builder
            alertDialogBuilder.setView(promptView);

            final TextInputLayout companyInput = (TextInputLayout) promptView.findViewById(R.id.companyInput);
            final TextInputLayout contactInput = (TextInputLayout) promptView.findViewById(R.id.contactInput);
            final TextInputLayout phoneInput = (TextInputLayout) promptView.findViewById(R.id.phoneInput);
            final TextInputLayout addressInput = (TextInputLayout) promptView.findViewById(R.id.addressInput);
            final TextInputLayout rfcInput = (TextInputLayout) promptView.findViewById(R.id.rfcInput);

            // setup a dialog window
            alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // get user companyInput and set it to result
                        if (!companyInput.getEditText().getText().toString().isEmpty()) {
                            companyInput.setError(null);
                            if (!contactInput.getEditText().getText().toString().isEmpty()){
                                contactInput.setError(null);
                                if (!phoneInput.getEditText().getText().toString().isEmpty()){
                                    phoneInput.setError(null);
                                    if (!addressInput.getEditText().getText().toString().isEmpty()) {
                                        addressInput.setError(null);
                                        if (!rfcInput.getEditText().getText().toString().isEmpty()) {
                                            rfcInput.setError(null);
                                            if (databaseReference == null) {
                                                if (firebaseAuth.getCurrentUser() != null) {
                                                    databaseReference = firebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("companies");
                                                    HashMap<String, String> companyData = new HashMap<>();
                                                    companyData.put("name", companyInput.getEditText().getText().toString());
                                                    companyData.put("contact", contactInput.getEditText().getText().toString());
                                                    companyData.put("phone", phoneInput.getEditText().getText().toString());
                                                    companyData.put("address", addressInput.getEditText().getText().toString());
                                                    databaseReference.push().setValue(companyData);
                                                }
                                            } else {
                                                HashMap<String, String> companyData = new HashMap<>();
                                                companyData.put("name", companyInput.getEditText().getText().toString());
                                                companyData.put("contact", contactInput.getEditText().getText().toString());
                                                companyData.put("phone", phoneInput.getEditText().getText().toString());
                                                companyData.put("address", addressInput.getEditText().getText().toString());
                                                databaseReference.child("companies").push().setValue(companyData);
                                            }
                                        }else{
                                            rfcInput.setError("RFC no válido");
                                        }
                                    }else{
                                        addressInput.setError("Dirección no válida");
                                    }
                                }else{
                                    phoneInput.setError("Teléfono no válido");
                                }
                            }else{
                                contactInput.setError("Contacto no válido");
                            }
                        }else{
                            companyInput.setError("Compañía no válida");
                        }
                    }
                })
                .setNegativeButton("Cancelar",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,	int id) {
                            dialog.cancel();
                        }
                    });

            // create an alert dialog
            AlertDialog alertD = alertDialogBuilder.create();

            alertD.show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.action_logout) {
            Toast.makeText(getApplicationContext(), "Logging out...", Toast.LENGTH_SHORT).show();
            AuthUI.getInstance()
                    .signOut(getApplicationContext())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        public void onComplete(@NonNull Task<Void> task) {
                            // ...
                            Toast.makeText(getApplicationContext(), "Session closed", Toast.LENGTH_SHORT).show();
                        }
                    });
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_permit) {
            // Handle the camera action
        } else if (id == R.id.nav_package) {
            Intent intent = new Intent(MainActivity.this, PackagesActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_data) {
            Intent intent = new Intent(MainActivity.this, InfoActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
