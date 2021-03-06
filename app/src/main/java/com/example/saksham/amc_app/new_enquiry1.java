package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by saksham on 18/6/15.
 */
public class new_enquiry1 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_enquiry);

        EditText comments = (EditText)findViewById(R.id.comments);

        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.new_enquiry_layout), getApplicationContext());

        Spinner deviceChoices = (Spinner)findViewById(R.id.spinner_device);
        final ArrayAdapter<CharSequence> device_adapter = ArrayAdapter.createFromResource(this,R.array.device_choices,android.R.layout.simple_spinner_item);
        device_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceChoices.setAdapter(device_adapter);
        final Spinner deviceProblems = (Spinner)findViewById(R.id.spinner_brand);

        deviceChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> problems_adapter;
                //problems_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.laptop_problems, android.R.layout.simple_spinner_item);
                //Toast.makeText(getApplicationContext(),"" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                if (position==0) {
                    problems_adapter = ArrayAdapter.createFromResource(new_enquiry1.this, R.array.laptop_models, android.R.layout.simple_spinner_item);
                }
                else {
                    problems_adapter = ArrayAdapter.createFromResource(new_enquiry1.this, R.array.tv_models, android.R.layout.simple_spinner_item);
                }
                problems_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                deviceProblems.setAdapter(problems_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        Button submit = (Button)findViewById(R.id.enquity_submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Your vendor will reply soon", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),enquiry.class));
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_enquiry_actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.maintainence_button:
                startActivity(new Intent(getApplicationContext(),maintainance.class));
                return true;
            case R.id.enquiry_history_button:
                startActivity(new Intent(getApplicationContext(), enquiry.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}