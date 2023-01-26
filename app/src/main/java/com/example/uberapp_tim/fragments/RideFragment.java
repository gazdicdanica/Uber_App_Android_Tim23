package com.example.uberapp_tim.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverRideActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.dialogs.LocationDialog;
import com.example.uberapp_tim.dto.RideDTO;
import com.example.uberapp_tim.service.FragmentToActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationManager locationManager;
    private String provider;
    private SupportMapFragment mapFragment;
    private GoogleMap map;
    private AlertDialog dialog;
    private Marker home;
    private com.example.uberapp_tim.model.route.Location destination;
    private com.example.uberapp_tim.model.route.Location departure;
    private RideDTO ride;
    private Long id;

    private FragmentToActivity mCallback;

    public static RideFragment newInstance(){
        RideFragment f = new RideFragment();
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        DriverRideActivity activity = (DriverRideActivity)getActivity();
        Bundle res = activity.getIdBundle();
        id = res.getLong("id");
        String jwt = getActivity().getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");
        ServiceUtils.rideService.getRide("Bearer " + jwt, id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.code() == 200){
                    try {
                        String rideMessage = response.body().string();

                        Gson g = null;

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                                @Override
                                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                                    return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                                }
                            }).create();
                        }
                        ride = g.fromJson(rideMessage, RideDTO.class);

                        mCallback.communicate(ride.getPassengers().get(0).getId());

                        if(map != null){
                            addRedMarker(ride.getLocations().get(0).getDeparture(), "Departure");
                            addRedMarker(ride.getLocations().get(0).getDestination(), "Destination");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void showLocationDialog() {
        if(dialog == null) {
            dialog = new LocationDialog(getActivity()).prepareDialog();
        } else {
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        dialog.show();
    }

    private void createMapFragmentAndInflate(){
        Criteria criteria = new Criteria();

        provider = locationManager.getBestProvider(criteria, true);

        mapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mapFragment).commit();

        mapFragment.getMapAsync(this);
    }

    public boolean checkLocationPermission() {
        if(ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                new AlertDialog.Builder(getActivity())
                        .setTitle("Allow user location")
                        .setMessage("To continue working we need your location...Allow now?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{
                                                Manifest.permission.ACCESS_FINE_LOCATION,
                                                Manifest.permission.ACCESS_COARSE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION
                                );
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume(){
        super.onResume();

        createMapFragmentAndInflate();

        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean wifi = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.i("gps", String.valueOf(gps));
        Log.i("wifi", String.valueOf(wifi));
        if(!gps && !wifi) {
            Log.i("Nema Konekcije", "!");
            showLocationDialog();
        } else {
            if(checkLocationPermission()) {
                if(ContextCompat.checkSelfPermission(requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(provider, 2000, 0, this);
                } else if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(provider, 2000, 0, this);
                }
            }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(map != null) {
            addMarker(location);
        }
    }

    private void addMarker(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());

        if(home != null) {
            home.remove();
        }

        home = map.addMarker(new MarkerOptions()
                .title("Your Position")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).position(loc));

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(location.getLatitude(), location.getLongitude()))
                .zoom(15)
                .bearing(location.getBearing()).build();
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(update);
        home.setFlat(true);

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(@NonNull String provider) {}

    @Override
    public void onProviderDisabled(@NonNull String provider) {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.map_layout, vg, false);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
    }

    public void addRedMarker(com.example.uberapp_tim.model.route.Location location, String title){
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        map.addMarker(new MarkerOptions()
                        .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(latLng));
        home.setFlat(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap){
        map = googleMap;
        Location location = null;

        if(provider == null) {
            Log.i("Nope", "No provider");
        } else {
            if(checkLocationPermission()) {

                if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    location = locationManager.getLastKnownLocation(provider);
                } else if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    location = locationManager.getLastKnownLocation(provider);
                }
            }
        }

        if (location != null) {
            addMarker(location);
        }

        if(ride != null){
            addRedMarker(ride.getLocations().get(0).getDeparture(), "Departure");
            addRedMarker(ride.getLocations().get(0).getDestination(), "Destination");
        }
    }

    @Override
    public void onDetach(){
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }
}
