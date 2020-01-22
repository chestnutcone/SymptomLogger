package com.example.symptomlogger;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String NO_OPTION = "----------------";
    DatabaseHelper historyDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        DatabaseHelper.copyDatabaseFromAssets(this);
        historyDb = new DatabaseHelper(this);
        Cursor res = historyDb.getGeneralRegion();
        displayGeneralRegion(res);
        makeDatePicker();

    }

    public void displayGeneralRegion(Cursor res) {
        List<String> list = new ArrayList<String>();
        list.add(NO_OPTION);
        while (res.moveToNext()) {
            list.add(res.getString(0));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        Spinner generalRegionSpinner = (Spinner) findViewById(R.id.generalRegionSpinner);
        generalRegionSpinner.setAdapter(adapter);

        AdapterView.OnItemSelectedListener generalRegionListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                displaySpecificRegion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };
        generalRegionSpinner.setOnItemSelectedListener(generalRegionListener);
    }

    public void displaySpecificRegion() {
        // dependent on selection of general region
        Spinner generalRegionSpinner = (Spinner) findViewById(R.id.generalRegionSpinner);
        String choice = generalRegionSpinner.getSelectedItem().toString();
        Spinner specificRegionSpinner = (Spinner) findViewById(R.id.specificRegionSpinner);
        List<String> list = new ArrayList<String>();
        list.add(NO_OPTION);
        if (choice.equals(NO_OPTION)) {

        } else {
            // query for those
            String[] generalRegion = {choice};
            Cursor res = historyDb.getSpecificRegion(generalRegion);
            while (res.moveToNext()) {
                list.add(res.getString(0)+" ("+res.getString(1)+")");
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item, list);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        specificRegionSpinner.setAdapter(adapter);
    }

    public void makeDatePicker() {
        final TextView dateField = (TextView) findViewById(R.id.datePicker);
        final DatePickerDialog.OnDateSetListener mDateSetListener;

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = year+"/"+(month+1)+"/"+dayOfMonth;
                dateField.setText(date);
            }
        };
        dateField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal  = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        MainActivity.this,
                        mDateSetListener,
                        year, month, day
                );

                dialog.show();
            }
        });
    }


}
