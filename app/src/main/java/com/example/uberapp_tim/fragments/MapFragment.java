package com.example.uberapp_tim.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Camera;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.passenger.PassengerMainActivity;
import com.example.uberapp_tim.connection.WebSocket;
import com.example.uberapp_tim.dialogs.LocationDialog;
import com.example.uberapp_tim.dto.MessageDTO;
import com.example.uberapp_tim.dto.VehicleLocatingDTO;
import com.example.uberapp_tim.model.ride.RideStatus;
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
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private Integer numberOfMarkers = 0;
    private LocationManager locationManager;
    private String provider;
    private SupportMapFragment mapFragment;
    private AlertDialog dialog;
    private Marker home;
    private GoogleMap map;
    private boolean isStart=false, isFinish=false;

    private List<Marker> activeDrivers = new ArrayList<>();
    private List<Marker> busyDrivers = new ArrayList<>();


    private PassengerMainActivity activity;

    private FragmentToActivity mCallback;

    public static MapFragment newInstance() {
        MapFragment mpf = new MapFragment();
        return mpf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        subscribeToWebsocket();
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
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

                    locationManager.requestLocationUpdates(provider, 1000, 50, this);
                } else if(ContextCompat.checkSelfPermission(getContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    locationManager.requestLocationUpdates(provider, 1000, 50, this);
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

    @SuppressLint("CheckResult")
    private void subscribeToWebsocket(){
        WebSocket webSocket = new WebSocket();
        webSocket.stompClient.topic("/update-vehicle-location/").subscribe(topicMessage -> {
            String message = topicMessage.getPayload();

            Gson g = new GsonBuilder().create();
            Type listType = new TypeToken<ArrayList<VehicleLocatingDTO>>(){}.getType();
            List<VehicleLocatingDTO> vehicles= g.fromJson(message, listType);

            if(getActivity() != null){
                for(VehicleLocatingDTO v : vehicles){
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
            Log.d("NE POSTOJI", "FINISHED");
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
            Log.d("NE POSTOJI", "ACTIVE");
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
            Log.d("AVAILABLE TO ", "BUSY");
            Marker m = this.getVehicleMarkerById(vehicle.getVehicle().getId());
            this.activeDrivers.remove(m);
            m.setTitle("Busy");
            m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            LatLng location = new LatLng(vehicle.getVehicle().getCurrentLocation().getLatitude(), vehicle.getVehicle().getCurrentLocation().getLongitude());
            m.setPosition(location);
            this.busyDrivers.add(m);
        } else if (this.checkPresentOnMap(vehicle) && vehicle.getRideStatus().equals(RideStatus.FINISHED)
                && this.getVehicleMarkerById(vehicle.getVehicle().getId()).getTitle().equals("Busy")) {
            Log.d("BUSY YO", "AVAILABLE");
            Marker m = this.getVehicleMarkerById(vehicle.getVehicle().getId());
            this.busyDrivers.remove(m);
            m.setTitle("Active");
            m.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            LatLng location = new LatLng(vehicle.getVehicle().getCurrentLocation().getLatitude(), vehicle.getVehicle().getCurrentLocation().getLongitude());
            m.setPosition(location);
            this.activeDrivers.add(m);
        }else{
            Log.d("PROMENA", "LOKACIJE");
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
                .bearing(0).build();
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

        View v =  inflater.inflate(R.layout.map_layout, vg, false);

        activity=(PassengerMainActivity) getActivity();

        return v;
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
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
        if(numberOfMarkers == 0) {
            LatLng gpsLocation = new LatLng(location.getLatitude(), location.getLongitude());
            String currLocation = "start,"+activity.getAddressFromLocation(gpsLocation).split(",")[0];
            mCallback.saveLatLng("s", gpsLocation);
            mCallback.communicate(currLocation);
            mCallback.sendStartLocation(new com.example.uberapp_tim.model.route.Location(location));
        }

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (numberOfMarkers < 2) {
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                            .position(latLng).title(resolveName()));
                    if ((numberOfMarkers == 0 && !isFinish) || (numberOfMarkers==1 && !isFinish)) {
                        Log.println(Log.ASSERT,"AAAA", activity.getAddressFromLocation(latLng));
                        String end = activity.getAddressFromLocation(latLng);
                        mCallback.saveLatLng("f", latLng);
                        isFinish = true;
                        mCallback.communicate("finish," + end);
                        mCallback.sendFinishLocation(new com.example.uberapp_tim.model.route.Location(latLng.longitude, latLng.latitude));
                    } else if (numberOfMarkers == 1 && !isStart){
                        isStart = true;
                        String start = activity.getAddressFromLocation(latLng);
                        mCallback.saveLatLng("s", latLng);
                        mCallback.communicate("start,"+start);
                        mCallback.sendStartLocation(new com.example.uberapp_tim.model.route.Location(latLng.longitude, latLng.latitude));
                    }
                    numberOfMarkers++;
                    home.setFlat(true);

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng).zoom(14).build();

                    map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }
            }
        });

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (marker.getTitle().equals("Start")){
                    mCallback.communicate("start, ");
                    isStart = false;
                } else if(marker.getTitle().equals("Finish")){
                    mCallback.communicate("finish, ");
                    isFinish = false;
                }
                if (numberOfMarkers != 0){ numberOfMarkers --; }
                marker.remove();
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

    private String resolveName() {
        if ((numberOfMarkers == 0 && !isFinish && !isStart) ||
                (numberOfMarkers == 1 && !isFinish && isStart)) {
            return "Finish";
        } else if (numberOfMarkers == 1 && isFinish && !isStart) {
            return "Start";
        }
        return "";
    }
}
