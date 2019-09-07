package com.evergreen.apps.tourguideapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.example.tourguideapp.R;
import com.android.example.tourguideapp.databinding.ItemListBinding;
import com.evergreen.apps.tourguideapp.models.Category;
import com.evergreen.apps.tourguideapp.models.Location;
import com.evergreen.apps.tourguideapp.models.Place;
import com.evergreen.apps.tourguideapp.ui.DetailsActivity;
import com.evergreen.apps.tourguideapp.utils.Constants;
import com.evergreen.apps.tourguideapp.utils.LocationPreferences;
import com.evergreen.apps.tourguideapp.utils.LocationService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationAdapterViewHolder> {

    ItemListBinding itemListBinding;
    private Cursor mCursor;
    private Context mContext;

    public LocationAdapter(Context context){
        this.mContext = context;
    }


    @NonNull
    @Override
    public LocationAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        itemListBinding = DataBindingUtil.inflate(layoutInflater,R.layout.item_list, parent, false);

        final View itemView = itemListBinding.getRoot();

        final LocationAdapterViewHolder vh = new LocationAdapterViewHolder(itemView);
        return new LocationAdapterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final LocationAdapterViewHolder locationAdapterViewHolder, int i) {

        if (mCursor.moveToPosition(i)){

            final String id = mCursor.getString(mCursor.getColumnIndexOrThrow(Location.COLUMN_LOCATION_ID));
            final String name = mCursor.getString(mCursor.getColumnIndexOrThrow(Location.COLUMN_NAME));
            final String city = mCursor.getString(mCursor.getColumnIndexOrThrow(Location.COLUMN_CITY));
            final String country = mCursor.getString(mCursor.getColumnIndexOrThrow(Location.COLUMN_COUNTRY));
            final String categoryName = mCursor.getString(mCursor.getColumnIndexOrThrow(Location.COLUMN_CATEGORY_NAME));
            final String categoryIcon = mCursor.getString(mCursor.getColumnIndexOrThrow(Location.COLUMN_CATEGORY_ICON));
            final String latitude = mCursor.getString(mCursor.getColumnIndexOrThrow(Location.COLUMN_LAT));
            final String longitude = mCursor.getString(mCursor.getColumnIndexOrThrow(Location.COLUMN_LNG));
            final String address = mCursor.getString(mCursor.getColumnIndexOrThrow(Location.COLUMN_ADDRESS));

            if (LocationPreferences.isLocationLatLonAvailable(mContext)){

                double latitude_dbl = Double.parseDouble(latitude);
                double longitude_dbl = Double.parseDouble(longitude);

                android.location.Location endLocation = new android.location.Location("New Location");
                endLocation.setLatitude(latitude_dbl);
                endLocation.setLongitude(longitude_dbl);

                double locationServiceLastLocation = LocationService.getDistanceInKm(mContext, endLocation);

                locationAdapterViewHolder.placeMilesTextView.
                        setText(String.format("%skm away", String.format("%.2f", locationServiceLastLocation)));
            }

            locationAdapterViewHolder.titleView.setText(name);
            locationAdapterViewHolder.placeCityTextView.setText(city);
            locationAdapterViewHolder.categoryNameTextView.setText(categoryName);


            Picasso.with(mContext).load(Uri.parse(categoryIcon))
                    .into(locationAdapterViewHolder.placeImageView);

            locationAdapterViewHolder.imageProgress.setVisibility(View.GONE);

            locationAdapterViewHolder.frameLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Location locationObject = new Location();
                    locationObject.setName(name);
                    locationObject.setAddress(address);
                    locationObject.setCity(city);
                    locationObject.setCountry(country);
                    locationObject.setLat(latitude);
                    locationObject.setLng(longitude);

                    List<Category> categories = new ArrayList<>();
                    Place placeObject = new Place(id, name, locationObject, categories);

                    Intent detailsIntent = new Intent(mContext, DetailsActivity.class);
                    detailsIntent.putExtra(Constants.DETAILS_CONTENT, placeObject);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) mContext, locationAdapterViewHolder.placeImageView, Constants.SHARED_TRANSITION_NAME);

                    mContext.startActivity(detailsIntent, options.toBundle());

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return  mCursor == null ? 0 : mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    public class LocationAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView titleView;
        TextView placeCityTextView;
        TextView categoryNameTextView;
        ImageView placeImageView;
        TextView placeMilesTextView;
        FrameLayout frameLayout;
        ProgressBar imageProgress;

        public LocationAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            titleView = itemListBinding.placeName;
            placeCityTextView = itemListBinding.placeCity;
            categoryNameTextView = itemListBinding.categoryName;
            placeImageView       = itemListBinding.placeImage;
            placeMilesTextView       = itemListBinding.placeMiles;
            frameLayout              = itemListBinding.itemListFrame;
            imageProgress            = itemListBinding.imageProgress;
            mContext = itemView.getContext();
        }
    }
}

