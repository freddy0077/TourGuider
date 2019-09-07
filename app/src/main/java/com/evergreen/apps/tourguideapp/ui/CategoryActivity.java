package com.evergreen.apps.tourguideapp.ui;

import android.Manifest;
import android.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.evergreen.apps.tourguideapp.data.viewModels.MainViewModel;
import com.android.example.tourguideapp.R;
import com.evergreen.apps.tourguideapp.adapters.CategoriesAdapter;
import com.android.example.tourguideapp.databinding.ActivityCategoryBinding;
import com.evergreen.apps.tourguideapp.models.Category;
import com.evergreen.apps.tourguideapp.sync.LocationsSyncUtils;
import com.evergreen.apps.tourguideapp.utils.GPSTracker;

import java.util.List;

public class CategoryActivity extends AppCompatActivity   {

    private static final String TAG = CategoryActivity.class.getSimpleName();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private ActivityCategoryBinding activityCategoryBinding;

    CategoriesAdapter mCategoriesAdapter;

    MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCategoryBinding = DataBindingUtil.setContentView(this,R.layout.activity_category);

        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        if (checkLocationPermission()) {
            gpsgetlocation();
        }

        getAllCategories();

        LocationsSyncUtils.initialize(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

//        checkGPS();
    }

    private void getAllCategories(){

        viewModel.getCategories().observe(this, new androidx.lifecycle.Observer<List<Category>>() {

            @Override
            public void onChanged(@Nullable List<Category> categoryList) {
                if(categoryList.size() != 0){
                    mCategoriesAdapter = new CategoriesAdapter(categoryList);

                    activityCategoryBinding.contentSearch
                            .categoryContent
                            .recyclerView
                            .setAdapter(mCategoriesAdapter);

                    int columnCount = getResources().getInteger(R.integer.list_column_count);
                    StaggeredGridLayoutManager sglm =
                            new StaggeredGridLayoutManager(columnCount, StaggeredGridLayoutManager.VERTICAL);
                    activityCategoryBinding.contentSearch
                            .categoryContent
                            .recyclerView.setLayoutManager(sglm);
                    activityCategoryBinding.contentSearch.categoryContent.loader.setVisibility(View.GONE);
                }else{
                    activityCategoryBinding.contentSearch.categoryContent.loader.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void gpsgetlocation() {
        GPSTracker gps = new GPSTracker(CategoryActivity.this);
        if (gps.canGetLocation()) {
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

        } else {
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }
    }

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {


                new AlertDialog.Builder(this)
                        .setTitle(R.string.alertmsg)
                        .setMessage(R.string.alert_msg2)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(CategoryActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.action_settings:
                //your code here
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        //Request location updates:
                        gpsgetlocation();
                    }

                } else {
                    checkLocationPermission();
                }
            }
        }
    }
}
