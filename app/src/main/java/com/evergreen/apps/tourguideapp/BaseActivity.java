package com.evergreen.apps.tourguideapp;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;

public class BaseActivity extends AppCompatActivity {
    public float screenDensity;
    private boolean isImmersive = false;
    public EventBus eventBus;
    public boolean registerEventBus = true;
    public boolean isInForeground = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if(registerEventBus)
//            eventBus = EventBus.getDefault();
//        screenDensity = getApplicationContext().getResources().getDisplayMetrics().density;
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        if(registerEventBus)
//            eventBus.register(this);
//    }
//
//    @Override
//    public void onStop() {
//        if(registerEventBus)
//            eventBus.unregister(this);
//        super.onStop();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        isInForeground = true;
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        isInForeground = false;
//    }
}
