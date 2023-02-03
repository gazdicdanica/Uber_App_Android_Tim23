package com.example.uberapp_tim.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.connection.WebSocket;
import com.example.uberapp_tim.dialogs.LocationDialog;
import com.example.uberapp_tim.dto.VehicleLocatingDTO;
import com.example.uberapp_tim.model.ride.RideStatus;
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
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DriverMapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationManager locationManager;
    private String provider;
    private SupportMapFragment mapFragment;
    private AlertDialog dialog;
    private Marker home;
    private GoogleMap map;

    private List<Marker> activeDrivers = new ArrayList<>();
    private List<Marker> busyDrivers = new ArrayList<>();

    public static DriverMapFragment newInstance() {
        DriverMapFragment mpf = new DriverMapFragment();
        return mpf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
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

    private void createMapFragmentAndInflate() {
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

    @SuppressWarnings("MissingPermission")
    @Override
    public void onResume() {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        subscribeToWebsocket();
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        Location location = null;

        if(provider == null) {
            Log.i("Nope", "No provider");
        } else {
            if(checkLocationPermission()) {
                Log.i("ASD", "str"+provider);

                if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    location = locationManager.getLastKnownLocation(provider);
                } else if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    location = locationManager.getLastKnownLocation(provider);
                }
            }
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getActivity(), marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Log.i("ASD", "ASDASDASDSA");
            }
        });

        if (location != null) {
            addMarker(location);
        }
    }

    @SuppressLint("CheckResult")
    private void subscribeToWebsocket(){
        Log.d("subscribe to websocket", "");
        WebSocket webSocket = new WebSocket();
        webSocket.stompClient.topic("/update-vehicle-location/").subscribe(topicMessage -> {
            String message = topicMessage.getPayload();

            Gson g = new GsonBuilder().create();
            Type listType = new TypeToken<ArrayList<VehicleLocatingDTO>>(){}.getType();
            List<VehicleLocatingDTO> vehicles= g.fromJson(message, listType);

            for(VehicleLocatingDTO v : vehicles){
                if(getActivity() != null){
                    if(v.getDriverId().equals(Long.valueOf(getContext().getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", "")))){
                        continue;
                    }
                    getActivity().runOnUiThread(
                            new Runnable() {
                                @Override
                                public void run() {
                                    addVehicle(v);
                                }
                            }
                    );
                }


            }
        });
    }

    private void addVehicle(VehicleLocatingDTO vehicle) {
        if (vehicle.getRideStatus() == RideStatus.FINISHED && !checkPresentOnMap(vehicle)) {
            LatLng location = new LatLng(vehicle.getVehicle().getCurrentLocation().getLatitude(), vehicle.getVehicle().getCurrentLocation().getLongitude());
            Marker m = map.addMarker(
                    new MarkerOptions()
                            .title("Available")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                            .position(location)
            );
            m.setTag(vehicle.getVehicle().getId());
            this.activeDrivers.add(m);
        } else if (vehicle.getRideStatus().equals(RideStatus.ACTIVE) && !this.checkPresentOnMap(vehicle)) {
            LatLng location = new LatLng(vehicle.getVehicle().getCurrentLocation().getLatitude(), vehicle.getVehicle().getCurrentLocation().getLongitude());
            Marker m = map.addMarker(new MarkerOptions()
                    .title("Busy")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .position(location)
            );
            m.setTag(vehicle.getVehicle().getId());
            this.busyDrivers.add(m);
        } else if (this.checkPresentOnMap(vehicle) && vehicle.getRideStatus().equals(RideStatus.ACTIVE)
                && this.getVehicleMarkerById(vehicle.getVehicle().getId()).getTitle().equals("Available")) {
            Marker m = this.getVehicleMarkerById(vehicle.getVehicle().getId());
            m.remove();
            this.activeDrivers.remove(m);
            LatLng location = new LatLng(vehicle.getVehicle().getCurrentLocation().getLatitude(), vehicle.getVehicle().getCurrentLocation().getLongitude());
            m.setPosition(location);
            this.busyDrivers.add(m);
        } else if (this.checkPresentOnMap(vehicle) && vehicle.getRideStatus().equals(RideStatus.FINISHED)
                && this.getVehicleMarkerById(vehicle.getVehicle().getId()).getTitle().equals("Busy")) {
            Marker m = this.getVehicleMarkerById(vehicle.getVehicle().getId());
            m.remove();
            this.busyDrivers.remove(m);
            m.setTitle("Active");
            LatLng location = new LatLng(vehicle.getVehicle().getCurrentLocation().getLatitude(), vehicle.getVehicle().getCurrentLocation().getLongitude());
            m.setPosition(location);
            this.activeDrivers.add(m);
        }else{
            LatLng location = new LatLng(vehicle.getVehicle().getCurrentLocation().getLatitude(), vehicle.getVehicle().getCurrentLocation().getLongitude());
            this.getVehicleMarkerById(vehicle.getVehicle().getId()).setPosition(location);
        }
    }

    private Marker getVehicleMarkerById(Long id){
        for(Marker driverMarker : this.activeDrivers){
            if(driverMarker.getTag().equals(id)){
                return driverMarker;
            }
        }

        for(Marker driverMarker : this.busyDrivers){
            if(driverMarker.getTag().equals(id)){
                return driverMarker;
            }
        }
        return null;
    }

    private boolean checkPresentOnMap(VehicleLocatingDTO vehicle){
        for(Marker driverMarker : this.activeDrivers){
            if(Objects.equals(driverMarker.getTag(), vehicle.getVehicle().getId())){
                return true;
            }
        }
        for(Marker driverMarker : this.activeDrivers){
            if(Objects.equals(driverMarker.getTag(), vehicle.getVehicle().getId())){
                return true;
            }
        }
        return false;
    }
}