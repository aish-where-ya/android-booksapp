package com.example.user.chatyt;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FilterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerFilter;
    Spinner spinnerMoreInfo;
    String option,choice;

    Button Submit;
    DatabaseReference databaseAllBooks;
    StorageReference mStorageRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        databaseAllBooks = FirebaseDatabase.getInstance().getReference("AllBooks");

        spinnerFilter = (Spinner) findViewById(R.id.spinner1);
        spinnerFilter.setOnItemSelectedListener(this);

        Submit = (Button) findViewById(R.id.Submit);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice=spinnerMoreInfo.getSelectedItem().toString();
                Intent intent = new Intent(FilterActivity.this,AfterFilter.class);
                intent.putExtra("key1",option);
                intent.putExtra("key2",choice);
                startActivity(intent);

                //Toast.makeText(getApplicationContext(),""+choice,Toast.LENGTH_SHORT).show();
            }
        });


    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        option = parent.getItemAtPosition(position).toString();
        if(option.equals("department"))
        {
            spinnerMoreInfo = (Spinner) findViewById(R.id.spinner2);
            List<String> list = new ArrayList<String>();
            list.add("Comp");
            list.add("It");
            list.add("EnTC");
            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMoreInfo.setAdapter(dataAdapter);


        }
        else if(option.equals("year"))
        {
            spinnerMoreInfo = (Spinner) findViewById(R.id.spinner2);
            List<String> list = new ArrayList<String>();
            list.add("F.E");
            list.add("S.E");
            list.add("T.E");
            list.add("B.E");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMoreInfo.setAdapter(dataAdapter);

        }
        else
        {
            spinnerMoreInfo = (Spinner) findViewById(R.id.spinner2);
            List<String> list = new ArrayList<String>();
            list.add("First");
            list.add("Second");

            ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, list);
            dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMoreInfo.setAdapter(dataAdapter);

        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
