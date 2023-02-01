package com.example.uberapp_tim.fragments;

import android.content.Context;
import android.graphics.Color;
import android.location.Criteria;
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
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.uberapp_tim.BuildConfig;
import com.example.uberapp_tim.R;
import com.example.uberapp_tim.activities.passenger.PassengerMainActivity;
import com.example.uberapp_tim.dto.RideRequestDTO;
import com.example.uberapp_tim.model.route.Location;
import com.example.uberapp_tim.model.route.Route;
import com.example.uberapp_tim.service.FragmentToActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.Duration;
import com.google.maps.model.EncodedPolyline;
import com.google.maps.model.TravelMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DrawRouteFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private SupportMapFragment mMapFragment;
    LatLng start, end;
    private LocationManager locationManager;
    String provider;
    private FragmentToActivity mCallback;
    PassengerMainActivity activity;
    float distance;
    Duration duration;

    public static DrawRouteFragment newInstance() {
        DrawRouteFragment mpf = new DrawRouteFragment();
        return mpf;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        PassengerMainActivity activity = (PassengerMainActivity) getActivity();
        start = activity.rideBundle().getParcelable("start");
        end = activity.rideBundle().getParcelable("finish");
        Log.i("STARTARA: ", start.toString());
        Log.i("ENDARA: ", end.toString());
    }

    @Override
    public void onResume() {
        super.onResume();

        mMapFragment = SupportMapFragment.newInstance();

        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.replace(R.id.map_container, mMapFragment).commit();
       mMapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup vg, Bundle data) {
        setHasOptionsMenu(true);
        activity = (PassengerMainActivity) getActivity();
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, true);
        View view = inflater.inflate(R.layout.map_layout, vg, false);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FragmentToActivity) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement FragmentToActivity");
        }
    }

    @Override
    public void onDetach() {
        mCallback = null;
        super.onDetach();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        mMap.addMarker(new MarkerOptions().position(start).title("Start"));


        mMap.addMarker(new MarkerOptions().position(end).title("Finish"));

        List<LatLng> path = new ArrayList<>();
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(BuildConfig.MAPS_API_KEY).build();

        DirectionsApiRequest req = DirectionsApi.getDirections(
                context,
                start.latitude+","+start.longitude,
                end.latitude+","+ end.longitude);

        try {
            DirectionsResult res = req.await();
            resolveRespData(res);
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
            mMap.addPolyline(opts);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 15));
    }

    private void resolveRespData(DirectionsResult res) {
        DirectionsRoute route = res.routes[0];
        DirectionsLeg[] legs = route.legs;
        for (DirectionsLeg directionsLeg : legs) {
            distance += directionsLeg.distance.inMeters;
        }
        distance = distance/1000;

        DirectionsLeg leg = legs[0];
        duration = leg.duration;
        mCallback.sendRideData(distance, duration);
    }
}
