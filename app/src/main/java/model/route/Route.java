package model.route;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Route {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private float distance;
    private double estimatedTime;   // Za sada stavljeno u sekundama, promeniti po potrebi
    private float estimatedPrice;
    private Location startLocation;
    private Location endLocation;
    private ArrayList<Location> stops;

    public Route(int id, LocalDateTime startTime, LocalDateTime endTime, float distance,
                 double estimatedTime, float estimatedPrice, Location startLocation,
                 Location endLocation, ArrayList<Location> stops) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.distance = distance;
        this.estimatedTime = estimatedTime;
        this.estimatedPrice = estimatedPrice;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.stops = stops;
    }

}
