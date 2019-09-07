//package com.evergreen.apps.tourguideapp.ui.fragments;
//
//import android.content.Context;
//import android.location.Geocoder;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.AutoCompleteTextView;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.Toast;
//
//import androidx.annotation.NonNull;
//import androidx.core.app.ActivityCompat;
//
//import com.android.example.tourguideapp.R;
//import com.evergreen.apps.tourguideapp.utils.GPSTracker;
//import com.evergreen.apps.tourguideapp.utils.PermissionUtils;
//import com.google.android.gms.maps.CameraUpdate;
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.SupportMapFragment;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.Marker;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//
//import java.util.ArrayList;
//import java.util.Locale;
//
//
//public class MyMapsFragment extends Fragment implements OnMapReadyCallback,
//        GoogleMap.OnMyLocationButtonClickListener,
//        ActivityCompat.OnRequestPermissionsResultCallback, AdapterView.OnItemClickListener {
//
//    private GoogleMap mMap;
//    private CustomInfoWindowGoogleMap customInfoWindow;
//    private InfoWindowData infoWindowData;
//    private Geocoder geocoder;
//    private View mapView;
//    private MyMapUtils myMapUtils;
//    private ArrayList<Marker> markerList;
//    private ArrayList<String> phoneList;
//    private ArrayList<Store> allStorelList;
//    private ArrayList<String> allBranchNameList = null;
//    private boolean mPermissionDenied = false;
//    private double latitude, longitude;
//    private Context mContext;
//    private GPSTracker gps;
//    private AutoCompleteTextView autoCompleteTextView;
//    private RelativeLayout rlLayout;
//    private ImageView img_arrow;
//    private ArrayAdapter<String> adapter;
//    private ArrayList<LatLng> markerPoints;
//    private int coverArea = 0;
//
//    private MainActivity mainActivity;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View rootView = null;
//        rootView = inflater.inflate(R.layout.fragment_maps, container, false);
//        intVariable();
//        initView(rootView);
//        return rootView;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//
//    public void intVariable() {
//        mContext = getActivity();
//        infoWindowData = new InfoWindowData();
//        customInfoWindow = new CustomInfoWindowGoogleMap(mContext, "0");
//        markerPoints = new ArrayList<>();
//        allStorelList = new ArrayList<>();
//        markerList = new ArrayList<>();
//        allBranchNameList = new ArrayList<>();
//        phoneList = new ArrayList<>();
//
//        mainActivity = (MainActivity) getActivity();
//    }
//
//    private void initView(View rootView) {
//        rlLayout = rootView.findViewById(R.id.rlLayout);
//        autoCompleteTextView = rootView.findViewById(R.id.auto_complete_searchBox);
//        autoCompleteTextView.setThreshold(1);
//        img_arrow = rootView.findViewById(R.id.img_search_view_arrow);
//
//    }
//
//    private void initMapView() {
//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapView = mapFragment.getView();
//        mapFragment.getMapAsync(this);
//    }
//
//    @Override
//    public void onMapReady(GoogleMap map) {
//        mMap = map;
//        if (mMap != null) {
//            mMap.clear();
//        }
//        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//        getGpsLocation();
//        loadAllStoreData();
//        geocoder = new Geocoder(getActivity(), Locale.getDefault());
//        myMapUtils = new MyMapUtils(mContext, mMap);
//        initListener(map);
//
//        if (mapView != null && mapView.findViewById(Integer.parseInt("1")) != null) {
//            setMyLocationButtonPosition();
//        }
//        mMap.setMyLocationEnabled(true);
//    }
//
//
//    private void initListener(final GoogleMap googleMap) {
//
//        mMap.setOnMyLocationButtonClickListener(this);
//
//        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//            @Override
//            public boolean onMarkerClick(Marker marker) {
//
//                myMapUtils.getDirectionOnMap(marker.getPosition().latitude, marker.getPosition().longitude);
//                showInfoWindow(marker);
//                return false;
//            }
//        });
//
//        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
//            @Override
//            public void onInfoWindowClick(Marker marker) {
//                int number = (int) marker.getZIndex();
//                ActivityUtils.getInstance().invokePhoneCall(mContext, allStorelList.get(number).getContactNumber());
//            }
//        });
//
//
//        img_arrow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (mainActivity != null) {
//                    mainActivity.showFloatingSearchView();
//                }
//                rlLayout.setVisibility(View.GONE);
//                InputMethodManager imm = (InputMethodManager) mContext.getSystemService(
//                        Context.INPUT_METHOD_SERVICE);
//                imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
//            }
//        });
//        autoCompleteTextView.setOnItemClickListener(this);
//    }
//
//
//    private void showInfoWindow(Marker marker) {
//        String distance = myMapUtils.findDistanceFromMyLocation(marker);
//        infoWindowData.setDistance(distance);
//        customInfoWindow = new CustomInfoWindowGoogleMap(mContext, distance);
//        mMap.setInfoWindowAdapter(customInfoWindow);
//    }
//
//
//
//    private void enableMyLocation() {
//        if (PermissionUtils.isPermissionGranted(getActivity(), PermissionUtils.LOCATION_PERMISSION, PermissionUtils.REQUEST_LOCATION)) {
//            initMapView();
//        }
//    }
//
//    @Override
//    public boolean onMyLocationButtonClick() {
//        getGpsLocation();
//        return false;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch (requestCode) {
//            case PermissionUtils.REQUEST_LOCATION: {
//                if (PermissionUtils.isPermissionResultGranted(grantResults)) {
//                    enableMyLocation();
//                } else {
//                    mPermissionDenied = true;
//                    Toast.makeText(getActivity(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
//
//    private void addMarkersToMap(ArrayList<Store> storeListData) {
//        if (!markerList.isEmpty()) {
//            markerList.clear();
//        }
//        for (int i = 0; i < storeListData.size(); i++) {
//
//            if (!isProgressSet()) {
//                addMarkerByStoreList(i, storeListData);
//            } else {
//                double distance = myMapUtils.getDistanceFromLocationByLtdLng(storeListData.get(i).getLatitude()
//                        , storeListData.get(i).getLongitude());
//                if (distance <= coverArea) {
//                    addMarkerByStoreList(i, storeListData);
//                }
//            }
//
//        }
//
//    }
//
//
//    private void addMarkerByStoreList(int i, ArrayList<Store> storeListData) {
//        MarkerOptions markerOption = new MarkerOptions()
//                .position(new LatLng(storeListData.get(i).getLatitude(), storeListData.get(i).getLongitude()))
//                .title(storeListData.get(i).getBranchName())
//                .snippet(storeListData.get(i).getAddress())
//                .zIndex(i).icon(AppUtils.getMapMarker(getActivity()));
//
//        Marker marker = mMap.addMarker(markerOption);
//        phoneList.add(storeListData.get(i).getContactNumber());
//        markerList.add(marker);
//
//    }
//
//
//    private void getGpsLocation() {
//        gps = new GPSTracker(mContext);
//        if (gps.canGetLocation()) {
//
//            latitude = gps.getLatitude();
//            longitude = gps.getLongitude();
//
//            LatLng latLng = new LatLng(latitude, longitude);
//            AppConstant.MY_LOCATION_LATITUTE = latitude;
//            AppConstant.MY_LOCATION_LONGITUTE = longitude;
//            CameraUpdate cameraUpdate = null;
//            if (isProgressSet()) {
//                cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, AppPreference.getInstance(mContext).getInteger(PrefKey.ZOOM));
//            } else {
//                cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
//            }
//            mMap.animateCamera(cameraUpdate);
//        } else {
//
//            gps.showSettingsAlert();
//        }
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        if (mPermissionDenied) {
//            mPermissionDenied = false;
//        } else {
//            enableMyLocation();
//        }
//    }
//
//    public void loadAllStoreData() {
//        LoadStoreDataFromSheet loadStoreDataFromSheet = new LoadStoreDataFromSheet();
//        loadStoreDataFromSheet.loadAllStoreData();
//        loadStoreDataFromSheet.setClickListener(new RetrofitDataLoadListener() {
//            @Override
//            public void finishLoadData(ArrayList<Store> dataList, boolean isSuccessful) {
//                if (isSuccessful) {
//                    if (!allStorelList.isEmpty()) {
//                        allStorelList.clear();
//                    }
//                    allStorelList.addAll(dataList);
//                    addMarkersToMap(allStorelList);
//                }
//
//                for (int i = 0; i < dataList.size(); i++) {
//                    allBranchNameList.add(dataList.get(i).getBranchName());
//                }
//                adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_item, allBranchNameList);
//                autoCompleteTextView.setAdapter(adapter);
//            }
//        });
//    }
//
//    private void setMyLocationButtonPosition() {
//
//        View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)
//                locationButton.getLayoutParams();
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
//        layoutParams.setMargins(0, 0, 30, 180);
//    }
//
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        String branchName = adapter.getItem(position);
//        boolean isFoundBranch = false;
//        for (int i = 0; i < allBranchNameList.size(); i++) {
//            if (allBranchNameList.get(i) == branchName) {
//                for (int j = 0; j < markerList.size(); j++) {
//                    if (branchName.equalsIgnoreCase(markerList.get(j).getTitle())) {
//                        getLocationByBranchName(i);
//                        isFoundBranch = true;
//                        break;
//                    }
//
//                }
//
//            }
//        }
//
//        if (!isFoundBranch) {
//            Toast.makeText(mContext, branchName + getString(R.string.not_found), Toast.LENGTH_LONG).show();
//        }
//
//    }
//
//    private void getLocationByBranchName(int i) {
//        double ltd = allStorelList.get(i).getLatitude();
//        double lng = allStorelList.get(i).getLongitude();
//        LatLng latLng = new LatLng(ltd, lng);
//        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
//        mMap.animateCamera(cameraUpdate);
//        Marker marker = markerList.get(i);
//        myMapUtils.getDirectionOnMap(marker.getPosition().latitude, marker.getPosition().longitude);
//        showInfoWindow(marker);
//        marker.showInfoWindow();
//    }
//
//
//    private boolean isProgressSet() {
//        if (AppPreference.getInstance(mContext).getBoolean(PrefKey.IS_LOADED)) {
//            coverArea = AppPreference.getInstance(mContext).getInteger(PrefKey.SEEKBAR_VALUE);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public void showAutocompleteLayout() {
//        if (rlLayout != null) {
//            rlLayout.setVisibility(View.VISIBLE);
//        }
//    }
//
//}
//
