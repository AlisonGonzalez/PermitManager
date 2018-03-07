package com.pm.permitmanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends Activity {

    private static final int RC_SIGN_IN = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        List<AuthUI.IdpConfig> providers = Arrays.asList(new AuthUI.IdpConfig.EmailBuilder().build(), new AuthUI.IdpConfig.GoogleBuilder().build());

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                // Successfully signed in
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent it = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(it);
                } else {
                    Toast.makeText(getApplicationContext(), "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
                }
                // ...
            } else {
                // Sign in failed, check response for error code
                // ...
                Toast.makeText(getApplicationContext(), "Something went wrong. We'll have better error handling later.", Toast.LENGTH_LONG).show();
            }
        }
    }
}

