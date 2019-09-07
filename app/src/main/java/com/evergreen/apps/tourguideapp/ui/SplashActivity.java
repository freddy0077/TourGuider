package com.evergreen.apps.tourguideapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.example.tourguideapp.BuildConfig;
import com.android.example.tourguideapp.R;
import com.android.example.tourguideapp.databinding.ActivitySplashBinding;
import com.evergreen.apps.tourguideapp.events.LoginClickedEvent;
import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 123 ;
    private ActivitySplashBinding activitySplashBinding;
    private FirebaseAuth mAuth;
    public EventBus eventBus;
    public boolean registerEventBus = true;
    private boolean isInForeground  = false;

    private View.OnClickListener onLoginClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            eventBus.post(new LoginClickedEvent());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySplashBinding = DataBindingUtil.setContentView(SplashActivity.this, R.layout.activity_splash);

//        FacebookSdk.sdkInitialize(this);
        mAuth = FirebaseAuth.getInstance();

        activitySplashBinding.loginButton.setOnClickListener(onLoginClicked);

        tryConnect(mAuth.getCurrentUser());


        if(registerEventBus)
            eventBus = EventBus.getDefault();
    }

    @Subscribe
    public void startAuthentication(LoginClickedEvent event){

        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
        );

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(!BuildConfig.DEBUG /* credentials */, true /* hints */)
                        .setLogo(R.drawable.ever_tourist_guide)

                        .build(),
                RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {

                tryConnect(mAuth.getCurrentUser());

                Log.i("SplashActivity", "onActivityResult: " + mAuth.getCurrentUser().getEmail());

//                IdpResponse idpResponse = IdpResponse.fromResultIntent(data);
//                    if (idpResponse != null) {
//                        email = idpResponse.getEmail();
//                        eventBus.post(new LoginEvent(email));
//                        return;
//                    }
            }
        }else{
//            AlerterHelper.showError(SplashActivity.this, getString(R.string.login_failed));
            tryConnect(null);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        tryConnect(currentUser);
        if(registerEventBus)
            eventBus.register(this);
    }

    @Override
    public void onStop() {
        if(registerEventBus)
            eventBus.unregister(this);
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        isInForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isInForeground = false;

    }

    public void tryConnect(FirebaseUser user){
        if (user != null) {
            startCategoryActivity(user.getEmail());
            Log.i("SplashActivity", "tryConnect: " + user.getEmail());
        } else {
            activitySplashBinding.loginButton.setVisibility(View.VISIBLE);
            activitySplashBinding.progressBar.setVisibility(View.GONE);
        }
    }

    private void signOut() {
        AuthUI.getInstance().signOut(this);
        tryConnect(null);
    }

    private void startCategoryActivity(String username) {
        Intent intent = new Intent(SplashActivity.this, CategoryActivity.class);
        startActivity(intent);
    }

}
