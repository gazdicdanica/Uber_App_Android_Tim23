package com.example.uberapp_tim.activities.passenger;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.example.uberapp_tim.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PassengerReportsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_reports);

        BarChart barChart = findViewById(R.id.rideNum_barchart);

        ArrayList<BarEntry> rides = new ArrayList<>();
        rides.add(new BarEntry(2013, 240));
        rides.add(new BarEntry(2014, 493));
        rides.add(new BarEntry(2015, 153));
        rides.add(new BarEntry(2016, 525));
        rides.add(new BarEntry(2017, 984));
        rides.add(new BarEntry(2018, 362));
        rides.add(new BarEntry(2019, 436));
        rides.add(new BarEntry(2020, 438));

        BarDataSet barDataSet = new BarDataSet(rides, "Rides");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        barDataSet.setValueTextColor(Color.BLACK);
        barDataSet.setValueTextSize(16f);

        BarData barData = new BarData(barDataSet);

        barChart.setFitBars(true);
        barChart.setData(barData);
        barChart.getDescription().setText("Example");
        barChart.animateY(2000);

    }
}