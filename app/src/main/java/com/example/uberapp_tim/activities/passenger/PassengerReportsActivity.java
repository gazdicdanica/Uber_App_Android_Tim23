package com.example.uberapp_tim.activities.passenger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.uberapp_tim.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class PassengerReportsActivity extends AppCompatActivity {

    BottomNavigationView passengerNav;
    TextView selectedDates;
    MaterialButton pickBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.passenger_reports);

        selectedDates = findViewById(R.id.selected_date);
        pickBtn = findViewById(R.id.date_picker_btn);

        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select date range");

        final MaterialDatePicker datePicker = builder.build();

        pickBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
            }
        });

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                selectedDates.setText(datePicker.getHeaderText());
            }
        });


        BarChart barChart = findViewById(R.id.passenger_barchart);

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

        passengerNav = findViewById(R.id.passengerReportNav);
        passengerNav.setSelectedItemId(R.id.action_reports);
        passengerNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent i;
                switch (item.getItemId()) {

                    case (R.id.action_main):
                        i = new Intent(PassengerReportsActivity.this, PassengerMainActivity.class);
                        startActivity(i);
                        return true;
                    case (R.id.action_account):
                        i  = new Intent(PassengerReportsActivity.this, PassengerAccountActivity.class);
                        startActivity(i);
                    case (R.id.action_inbox):
                        i = new Intent(PassengerReportsActivity.this, PassengerInboxActivity.class);
                        startActivity(i);
                        return true;
                    case (R.id.action_reports):
                        return true;
                }
                return false;
            }
        });

    }

    @Override
    protected void onResume(){
        super.onResume();
        passengerNav.setSelectedItemId(R.id.action_reports);
    }
}