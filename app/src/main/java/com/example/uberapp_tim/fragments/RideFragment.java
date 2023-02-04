package com.example.uberapp_tim.fragments;

import android.Manifest;
import android.annotation.SuppressLint;

import androidx.appcompat.app.AlertDialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim.BuildConfig;
import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.driver.DriverMainActivity;
import com.example.uberapp_tim.activities.driver.DriverRideActivity;
import com.example.uberapp_tim.connection.ServiceUtils;
import com.example.uberapp_tim.connection.WebSocket;
import com.example.uberapp_tim.dto.MessageDTO;
import com.example.uberapp_tim.dto.RideDTO;
import com.example.uberapp_tim.dto.VehicleLocatingDTO;
import com.example.uberapp_tim.model.message.Panic;
import com.example.uberapp_tim.receiver.NotificationReceiver;
import com.example.uberapp_tim.service.FragmentToActivity;
import com.example.uberapp_tim.service.OnMessageReceivedListener;
import com.example.uberapp_tim.service.WebSocketService;
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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RideFragment extends Fragment implements LocationListener, OnMapReadyCallback, OnMessageReceivedListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private LocationManager locationManager;
    private SupportMapFragment mMapFragment;
    DriverRideActivity activity;
    String provider;
    private GoogleMap map;
    private Marker home;
    private com.example.uberapp_tim.model.route.Location destination;
    private com.example.uberapp_tim.model.route.Location departure;
    private RideDTO ride;
    private Long id;

    private String estimation;
    private TextView estimationView;

    private Marker endMarker;
    private Polyline activeRoute;

    private static WebSocket webSocket = new WebSocket();
    private FragmentToActivity mCallback;

    private Handler mHandler;
    private Runnable mRunnable;


    public static RideFragment newInstance(){
        RideFragment f = new RideFragment();
        return f;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

        activity = (DriverRideActivity)getActivity();
        Bundle res = activity.getIdBundle();
        id = res.getLong("id");

        webSocket.stompClient.topic("/ride-panic/"+getActivity().getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null)).subscribe(
                topicMessage->{
                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            RideFragment.this.showPanicDialog();

                        }
                    });

                }
        );


        webSocket.stompClient.topic("/ride-cancel/"+getActivity().getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null)).subscribe(
                topicMessage->{

                    activity.runOnUiThread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            Toast.makeText(activity, "Pending ride was canceled", Toast.LENGTH_LONG).show();
                        }
                    });

                    Intent main = new Intent(activity, DriverMainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    getActivity().startActivity(main);
                }
        );

        webSocket.stompClient.topic("/message/"+id+"/"+getActivity().getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null)).subscribe(topicMessage ->{
            String message = topicMessage.getPayload();
            Log.d("MESSAGE", message);

            Gson g = null;

            g = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                    DateTimeFormatter format = DateTimeFormatter.ofPattern("yyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                    return LocalDateTime.parse(json.getAsJsonPrimitive().getAsString(), format);
                }
            }).create();

            MessageDTO messageDTO = g.fromJson(message, MessageDTO.class);
            Log.wtf("converted", messageDTO.toString());
            if(!String.valueOf(messageDTO.getSender()).equals(getActivity().getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null))){
                Intent i = new Intent(getActivity(), NotificationReceiver.class);
                i.putExtra("title", "In ride message");
                i.putExtra("text", messageDTO.getMessage());
                i.putExtra("channel", "in_ride_channel");
                i.putExtra("id", getActivity().getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", null));

                getActivity().sendBroadcast(i);
            }

        });
    }


    private void updateLocation(){

        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {

                if(home!= null){
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE);
                    Long id = Long.valueOf(sharedPreferences.getString("id", ""));
                    String jwt = "Bearer " + sharedPreferences.getString("accessToken", "");
                    Log.d("jwt " , jwt);
                    ServiceUtils.driverService.getDriverLocation(jwt, id).enqueue(new Callback<com.example.uberapp_tim.model.route.Location>() {
                        @Override
                        public void onResponse(Call<com.example.uberapp_tim.model.route.Location> call, Response<com.example.uberapp_tim.model.route.Location> response) {
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    com.example.uberapp_tim.model.route.Location current = response.body();
                                    if(current != null){
                                        home.setPosition(new LatLng(current.getLatitude(), current.getLongitude()));
                                    }

                                }
                            });
                        }

                        @Override
                        public void onFailure(Call<com.example.uberapp_tim.model.route.Location> call, Throwable t) {
                            Log.wtf("ERROR", t.getMessage());
                        }
                    });
                }

                mHandler.postDelayed(mRunnable, 2000);
            }
        };
        Log.wtf("BEFORE", "POSt");
        mHandler.post(mRunnable);
        Log.wtf("AFTER", "POST");
    }

    public void showPanicDialog(){
        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.panic_dialog_layout, null);
        builder.setView(dialogView);
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                Intent main = new Intent(activity, DriverMainActivity.class);
                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(main);
            }
        });
        dialog.show();

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
        mMapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment).commit();
        mMapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data){
        setHasOptionsMenu(true);
        activity = (DriverRideActivity) getActivity();
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        View view = inflater.inflate(R.layout.map_layout, vg, false);
        return view;
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
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(@NonNull String provider) {}

    @Override
    public void onProviderDisabled(@NonNull String provider) {}

    @SuppressWarnings("MissingPermission")
    @Override
    public void onPause() {
        super.onPause();

        locationManager.removeUpdates(this);
    }

    public void addRedMarker(com.example.uberapp_tim.model.route.Location location, String title){
        Log.d("RED MARKER", " ");
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        endMarker = map.addMarker(new MarkerOptions()
                        .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
                .position(latLng));
//        home.setFlat(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(latLng).zoom(14).build();

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void drawRoute(){
        List<LatLng> path = new ArrayList<>();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(BuildConfig.MAPS_API_KEY).build();

        DirectionsApiRequest req = DirectionsApi.getDirections(
                context,
                departure.getLatitude()+","+ departure.getLongitude(),
                destination.getLatitude()+","+ destination.getLongitude());
        try {
            DirectionsResult res = req.await();
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];
                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.parseColor("#f57804")).width(5);
            activeRoute = map.addPolyline(opts);
        }
        map.getUiSettings().setZoomControlsEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(departure.getLatitude(), departure.getLongitude()), 15));

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

            departure = new com.example.uberapp_tim.model.route.Location(location.getLongitude(), location.getLatitude());
            addMarker(location);
        }

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
                        destination = ride.getLocations().get(0).getDeparture();
                        addRedMarker(destination, "Departure");
                        drawRoute();
                        Location loc = new Location("");
                        loc.setLatitude(destination.getLatitude());
                        loc.setLongitude(destination.getLongitude());
                        mCallback.communicate(ride.getPassengers().get(0).getId());
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

    @Override
    public void onDetach(){
        mCallback = null;
        mHandler.removeCallbacks(mRunnable);
        super.onDetach();
    }

    @SuppressLint("CheckResult")
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        updateLocation();

        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MaterialButton startRideBtn = activity.findViewById(R.id.start_ride_btn);
        MaterialButton endRideBtn = activity.findViewById(R.id.end_ride_btn);
        MaterialButton panicBtn = activity.findViewById(R.id.panic_btn);
        estimationView = activity.findViewById(R.id.countdown_time);
        LinearLayout estLayout = activity.findViewById(R.id.ride_parametersLayout);
        estLayout.bringToFront();

        Gson g = new Gson();
        webSocket.stompClient.topic("/update-vehicle-location/").subscribe(topicMessage ->{
            String message = topicMessage.getPayload();
            Type listType = new TypeToken<ArrayList<VehicleLocatingDTO>>(){}.getType();
            List<VehicleLocatingDTO> vehicles= g.fromJson(message, listType);
            if(activity != null){
                for(VehicleLocatingDTO v : vehicles){
                    if(Objects.equals(v.getDriverId(), Long.valueOf(activity.getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("id", "")))){
                        this.estimation = v.getDuration();

                        if(this.estimation != null && this.getActivity()!=null){

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    estimationView.setText(estimation);
                                }
                            });
                        }

                    }
                }
            }

        });

        startRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jwt = activity.getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");
                ServiceUtils.rideService.startRide("Bearer " + jwt, ride.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                        departure = ride.getLocations().get(0).getDeparture();
                        destination = ride.getLocations().get(0).getDestination();

                        home.remove();
                        endMarker.remove();
                        activeRoute.remove();

                        Location current = new Location("");
                        current.setLatitude(departure.getLatitude());
                        current.setLongitude(departure.getLongitude());
                        addMarker(current);

                        addRedMarker(destination, "Departure");

                        drawRoute();

                        startRideBtn.setVisibility(View.GONE);
                        endRideBtn.setVisibility(View.VISIBLE);
                        panicBtn.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        });

        endRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String jwt = activity.getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");
                ServiceUtils.rideService.endRide("Bearer " + jwt, ride.getId()).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Intent main = new Intent(activity, DriverMainActivity.class);
                        main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(main);
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });

            }
        });

        panicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Please enter a reason");
                final EditText input = new EditText(getContext());
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                input.setPadding(30,30,30,30);
                builder.setView(input);

                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String reason = input.getText().toString();
                        Panic panic = new Panic();
                        panic.setReason(reason);

                        String jwt = activity.getSharedPreferences("AirRide_preferences", Context.MODE_PRIVATE).getString("accessToken", "");
                        ServiceUtils.rideService.panicRide("Bearer " + jwt, ride.getId(), panic).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                Toast.makeText(activity, "Panic was sent", Toast.LENGTH_SHORT).show();
                                Intent main = new Intent(activity, DriverMainActivity.class);
                                main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                                getActivity().finish();
                                startActivity(main);
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {

                            }
                        });
                    }
                });
                builder.create().show();
            }
        });
    }


    @Override
    public void onMessageReceived(String message) {

    }
}
