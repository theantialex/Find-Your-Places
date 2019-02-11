package com.example.alexandra.expendablechecklist;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    double origin_latitude;
    double origin_longitude;
    private int num_of_places;


    private GoogleMap mMap;
    private int radius;
    private TextView Show;
    private List<String> TYPES = new ArrayList<>();

    private CameraPosition mCameraPosition;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private Location mLastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        Show = findViewById(R.id.show);

        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        radius = Integer.valueOf(getIntent().getStringExtra("radius")) + 2000;
        //TYPES.add("grocery_or_supermarket");



        for (int i = 0; i < MyCategoriesExpandableListAdapter.parentItems.size(); i++) {

            for (int j = 0; j < MyCategoriesExpandableListAdapter.childItems.get(i).size(); j++) {

                String isChildChecked = MyCategoriesExpandableListAdapter.childItems.get(i).get(j).get(ConstantManager.Parameter.IS_CHECKED);

                if (isChildChecked.equalsIgnoreCase(ConstantManager.CHECK_BOX_CHECKED_TRUE)) {
                    // FIND OUT WHAT WAS CHOSEN HERE
                    String tvChild = MyCategoriesExpandableListAdapter.parentItems.get(i).get(ConstantManager.Parameter.CATEGORY_NAME) + " " + (j + 1);
                    TYPES.add(getCheckedItems(i, j));

                }

            }

        }


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }


    private String getCheckedItems(int parent, int child) {
        List<String> Active = new ArrayList<>();
        Active.add("park");
        Active.add("zoo");
        Active.add("amusement_park");
        Active.add("aquarium");

        List<String> Cul = new ArrayList<>();
        Cul.add("museum");
        Cul.add("art_gallery");
        Cul.add("church");
        Cul.add("mosque");
        Cul.add("synagogue");
        Cul.add("library");
        Cul.add("hindu_temple");

        List<String> food = new ArrayList<>();
        food.add("restaurant");
        food.add("cafe");
        food.add("bar");
        food.add("grocery_or_supermarket");

        if (parent == 0) {
            return Active.get(child);
        } else if (parent == 1) {
            return Cul.get(child);
        } else if (parent == 2) {
            return food.get(child);
        }
        return "";
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        Button btn = (Button) findViewById(R.id.btnRestaurant);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main(TYPES, radius);

            }
        });

    }
    public void main(final List<String> TYP, final int radius) {
        num_of_places = 0;
        final Polyline[] line = new Polyline[1];

        String url = "https://maps.googleapis.com/maps/";

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final RetrofitMaps service = retrofit.create(RetrofitMaps.class);

        Call<Example> call = service.getNearbyPlaces(origin_latitude + "," + origin_longitude, radius);

        call.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(final Call<Example> call, Response<Example> response) {
                try {
                    // CODE HERE
                    String waypoints1 = "";
                    int a = 0;
                    try {
                        String page = response.body().getNextPageToken();
                    } catch (NullPointerException e) {
                        Log.d("NextPageToken", "There is an error");
                    }
                    for (int i = 0; i < response.body().getResults().size(); i++) {

                        for (String tp : TYP) {
                            if ((response.body().getResults().get(i).getTypes().contains(tp)) && (a < 10)) {
                                waypoints1 += "place_id:" + response.body().getResults().get(i).getPlaceId() + '|';
                                a++;
                                String inf = "";
                                Double lat = response.body().getResults().get(i).getGeometry().getLocation().getLat();
                                Double lng = response.body().getResults().get(i).getGeometry().getLocation().getLng();
                                String placeName = response.body().getResults().get(i).getName();

                                try {
                                    if (response.body().getResults().get(i).getOpeningHours().getOpenNow().equals(true)) {
                                        inf = "Открыто сейчас";
                                    } else {
                                        inf = "Закрыто сейчас";
                                    }
                                } catch (NullPointerException e) {
                                    Log.d("OpeningHours", "There is an error");
                                }
                                LatLng place = new LatLng(lat, lng);
                                mMap.addMarker(new MarkerOptions().position(place)
                                        .title(placeName).snippet(inf));
                            }

                        }
                    }
                    TimeUnit.SECONDS.sleep(4);
                    try {
                        String page = response.body().getNextPageToken();
                        String url = "https://maps.googleapis.com/maps/";
                        final String waypoints12 = waypoints1;
                        final int a2 = a;


                        Retrofit retrofit3 = new Retrofit.Builder()
                                .baseUrl(url)
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        RetrofitMaps service3 = retrofit3.create(RetrofitMaps.class);

                        Call<Example> call3 = service3.getNearbyPlaces2(origin_latitude + "," + origin_longitude, radius, page);

                        call3.enqueue(new Callback<Example>() {
                            @Override
                            public void onResponse(Call<Example> call3, Response<Example> response3) {
                                try {
                                    int a3 = a2;
                                    String waypoints2 = "";

                                    for (int i = 0; i < response3.body().getResults().size(); i++) {

                                        for (String tp : TYP) {
                                            if ((response3.body().getResults().get(i).getTypes().contains(tp)) && (a3 < 10)) {
                                                waypoints2 += "place_id:" + response3.body().getResults().get(i).getPlaceId() + '|';
                                                a3++;
                                                String inf = "";
                                                Double lat = response3.body().getResults().get(i).getGeometry().getLocation().getLat();
                                                Double lng = response3.body().getResults().get(i).getGeometry().getLocation().getLng();
                                                String placeName = response3.body().getResults().get(i).getName();


                                                try {
                                                    if (response3.body().getResults().get(i).getOpeningHours().getOpenNow().equals(true)) {
                                                        inf = "Открыто сейчас";
                                                    } else {
                                                        inf = "Закрыто сейчас";
                                                    }
                                                } catch (NullPointerException e) {
                                                    Log.d("OpeningHours", "There is an error");
                                                }
                                                LatLng place = new LatLng(lat, lng);
                                                mMap.addMarker(new MarkerOptions().position(place)
                                                        .title(placeName).snippet(inf));
                                            }

                                        }

                                    }
                                    String waypoints = waypoints12 + waypoints2;

                                   // waypoints = waypoints.substring(0, waypoints.length() - 1);

                                    TimeUnit.SECONDS.sleep(4);
                                    try {
                                        String page = response3.body().getNextPageToken();
                                        String url = "https://maps.googleapis.com/maps/";
                                        final String waypoints24 = waypoints;
                                        final int a4 = a3;


                                        Retrofit retrofit4 = new Retrofit.Builder()
                                                .baseUrl(url)
                                                .addConverterFactory(GsonConverterFactory.create())
                                                .build();

                                        RetrofitMaps service4 = retrofit4.create(RetrofitMaps.class);

                                        Call<Example> call4 = service4.getNearbyPlaces2(origin_latitude + "," + origin_longitude, radius, page);

                                        call4.enqueue(new Callback<Example>() {
                                            @Override
                                            public void onResponse(Call<Example> call4, Response<Example> response4) {
                                                try {
                                                    int a5 = a4;
                                                    String waypoints4 = "";
                                                    for (int i = 0; i < response4.body().getResults().size(); i++) {

                                                        for (String tp : TYP) {
                                                            if ((response4.body().getResults().get(i).getTypes().contains(tp)) && (a5 < 10)) {
                                                                waypoints4 += "place_id:" + response4.body().getResults().get(i).getPlaceId() + '|';
                                                                a5++;
                                                                String inf = "";
                                                                Double lat = response4.body().getResults().get(i).getGeometry().getLocation().getLat();
                                                                Double lng = response4.body().getResults().get(i).getGeometry().getLocation().getLng();
                                                                String placeName = response4.body().getResults().get(i).getName();


                                                                try {
                                                                    if (response4.body().getResults().get(i).getOpeningHours().getOpenNow().equals(true)) {
                                                                        inf = "Открыто сейчас";
                                                                    } else {
                                                                        inf = "Закрыто сейчас";
                                                                    }
                                                                } catch (NullPointerException e) {
                                                                    Log.d("OpeningHours", "There is an error");
                                                                }
                                                                LatLng place = new LatLng(lat, lng);
                                                                mMap.addMarker(new MarkerOptions().position(place)
                                                                        .title(placeName).snippet(inf));

                                                            }

                                                        }

                                                    }
                                                    String fin_waypoints = waypoints24 + waypoints4;
                                                    try {
                                                        fin_waypoints = fin_waypoints.substring(0, fin_waypoints.length() - 1);
                                                    } catch (Exception e){
                                                        Toast toast = Toast.makeText(getApplicationContext(),
                                                                "Не найдено мест поблизости", Toast.LENGTH_LONG);
                                                        toast.show();
                                                    }

                                                    String url2 = "https://maps.googleapis.com/maps/";

                                                    Retrofit retrofit2 = new Retrofit.Builder()
                                                            .baseUrl(url2)
                                                            .addConverterFactory(GsonConverterFactory.create())
                                                            .build();

                                                    RetrofitMaps service2 = retrofit2.create(RetrofitMaps.class);

                                                    Call<Example2> call2 = service2.getDistanceDuration(origin_latitude + "," + origin_longitude, origin_latitude + "," + origin_longitude, "optimize:true|" + fin_waypoints);
                                                    // Call<Example2> call2 = service2.getDistanceDuration( origin_latitude + "," + origin_longitude, or);

                                                    call2.enqueue(new Callback<Example2>() {
                                                        @Override
                                                        public void onResponse(Call<Example2> call2, Response<Example2> response2) {
                                                            try {
                                                                //Remove previous line from map
                                                                if (line[0] != null) {
                                                                    line[0].remove();
                                                                }
                                                                // This loop will go through all the results and add marker on each location.
                                                                for (int i = 0; i < response2.body().getRoutes().size(); i++) {
                                                                    // String distance = response2.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                                                                    // String time = response2.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                                                                    String encodedString = response2.body().getRoutes().get(0).getOverviewPolyline().getPoints();

                                                                    // DECODE HERE
                                                                    List<LatLng> poly = new ArrayList<LatLng>();
                                                                    int index = 0, len = encodedString.length();
                                                                    int lat = 0, lng = 0;

                                                                    while (index < len) {
                                                                        int b, shift = 0, result = 0;
                                                                        do {
                                                                            b = encodedString.charAt(index++) - 63;
                                                                            result |= (b & 0x1f) << shift;
                                                                            shift += 5;
                                                                        } while (b >= 0x20);
                                                                        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                                                                        lat += dlat;

                                                                        shift = 0;
                                                                        result = 0;
                                                                        do {
                                                                            b = encodedString.charAt(index++) - 63;
                                                                            result |= (b & 0x1f) << shift;
                                                                            shift += 5;
                                                                        } while (b >= 0x20);
                                                                        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                                                                        lng += dlng;

                                                                        LatLng p = new LatLng((((double) lat / 1E5)),
                                                                                (((double) lng / 1E5)));
                                                                        poly.add(p);
                                                                    }

                                                                    line[0] = mMap.addPolyline(new PolylineOptions()
                                                                            .addAll(poly)
                                                                            .width(20)
                                                                            .color(Color.parseColor("#076AFC"))
                                                                            .geodesic(true)
                                                                    );

                                                                }

                                                            } catch (Exception e){
                                                                Log.d("error", "Something went wrong");

                                                            }

                                                }

                                                        @Override
                                                        public void onFailure(Call<Example2> call, Throwable t) {
                                                            Log.d("onFailure", t.toString());
                                                        }
                                                    });



                                            // MAKE ROUTE HERE
                                            // optimize:true перед waypoints

                                    /*String url2 = "https://maps.googleapis.com/maps/";

                                    Retrofit retrofit2 = new Retrofit.Builder()
                                            .baseUrl(url2)
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();

                                    RetrofitMaps service2 = retrofit2.create(RetrofitMaps.class);

                                    Call<Example2> call2 = service2.getDistanceDuration(origin_latitude + "," + origin_longitude, origin_latitude + "," + origin_longitude, "optimize:true|" + waypoints);
                                    // Call<Example2> call2 = service2.getDistanceDuration( origin_latitude + "," + origin_longitude, or);

                                    call2.enqueue(new Callback<Example2>() {
                                        @Override
                                        public void onResponse(Call<Example2> call2, Response<Example2> response2) {
                                            try {
                                                //Remove previous line from map
                                                if (line[0] != null) {
                                                    line[0].remove();
                                                }
                                                // This loop will go through all the results and add marker on each location.
                                                for (int i = 0; i < response2.body().getRoutes().size(); i++) {
                                                    // String distance = response2.body().getRoutes().get(i).getLegs().get(i).getDistance().getText();
                                                    // String time = response2.body().getRoutes().get(i).getLegs().get(i).getDuration().getText();
                                                    String encodedString = response2.body().getRoutes().get(0).getOverviewPolyline().getPoints();

                                                    // DECODE HERE
                                                    List<LatLng> poly = new ArrayList<LatLng>();
                                                    int index = 0, len = encodedString.length();
                                                    int lat = 0, lng = 0;

                                                    while (index < len) {
                                                        int b, shift = 0, result = 0;
                                                        do {
                                                            b = encodedString.charAt(index++) - 63;
                                                            result |= (b & 0x1f) << shift;
                                                            shift += 5;
                                                        } while (b >= 0x20);
                                                        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                                                        lat += dlat;

                                                        shift = 0;
                                                        result = 0;
                                                        do {
                                                            b = encodedString.charAt(index++) - 63;
                                                            result |= (b & 0x1f) << shift;
                                                            shift += 5;
                                                        } while (b >= 0x20);
                                                        int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
                                                        lng += dlng;

                                                        LatLng p = new LatLng((((double) lat / 1E5)),
                                                                (((double) lng / 1E5)));
                                                        poly.add(p);
                                                    }

                                                    line[0] = mMap.addPolyline(new PolylineOptions()
                                                            .addAll(poly)
                                                            .width(20)
                                                            .color(Color.parseColor("#076AFC"))
                                                            .geodesic(true)
                                                    );

                                                }

                                            } catch (Exception e) {
                                                Log.d("error", "Something went wrong");
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Example2> call, Throwable t) {
                                            Log.d("onFailure", t.toString());
                                        }

                                    });
                                } catch (Exception e) {
                                    Log.d("error", "Something went wrong");
                                }
                            }
                            @Override
                            public void onFailure(Call<Example> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                            } */
                                    } catch (Exception e) {
                                        Log.d("error", "Something went wrong");
                                    }


                            }


                            @Override
                            public void onFailure(Call<Example> call, Throwable t) {
                                Log.d("onFailure", t.toString());
                            }
                        });
                    } catch (Exception e) {
                        Log.d("error", "Something went wrong");
                    }
                } catch (Exception e) {
                    Log.d("error", "Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    } catch (Exception e) {
               Log.d("error", "Something went wrong");
                    }
                } catch (Exception e) {
                    Log.d("error", "Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Log.d("onFailure", t.toString());
            }
        });
    }





    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            origin_latitude = mLastKnownLocation.getLatitude();
                            origin_longitude = mLastKnownLocation.getLongitude();

                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }


    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }
}