package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
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
import android.widget.Toast;

/**
 * Created by saksham on 18/6/15.
 */
public class new_req1 extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_req1);
        DBHelper my_db = new DBHelper(getApplicationContext());
        my_db.open();
        Intent i = getIntent();
        String name = my_db.get_name(my_db.get_user());
        Toast.makeText(getApplicationContext(), "Hi " + name + "\nHope you got customer delight", Toast.LENGTH_LONG).show();
        my_db.close();
        ImageButton next_page = (ImageButton)findViewById(R.id.next1);
        next_page.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),maintainence_newreq2.class));
            }
        });

        EditText description = (EditText)findViewById(R.id.description);
        ViewGroup layout = (ViewGroup)findViewById(R.id.new_req1_layout);
        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.new_req1_layout), getApplicationContext());

        Spinner deviceChoices = (Spinner)findViewById(R.id.spinner_device);
        final ArrayAdapter<CharSequence> device_adapter = ArrayAdapter.createFromResource(this,R.array.device_choices,android.R.layout.simple_spinner_item);
        device_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deviceChoices.setAdapter(device_adapter);
        final Spinner deviceProblems = (Spinner)findViewById(R.id.spinner_problem);

        deviceChoices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayAdapter<CharSequence> problems_adapter;
                //problems_adapter = ArrayAdapter.createFromResource(getApplicationContext(), R.array.laptop_problems, android.R.layout.simple_spinner_item);
                //Toast.makeText(getApplicationContext(),"" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
                if (position==0) {
                    problems_adapter = ArrayAdapter.createFromResource(new_req1.this, R.array.laptop_problems, android.R.layout.simple_spinner_item);
                }
                else {
                    problems_adapter = ArrayAdapter.createFromResource(new_req1.this, R.array.tv_problems, android.R.layout.simple_spinner_item);
                }
                problems_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                deviceProblems.setAdapter(problems_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.new_req_actions,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.enquiry_button:
                startActivity(new Intent(getApplicationContext(),enquiry.class));
                return true;
            case R.id.history_button:
                startActivity(new Intent(getApplicationContext(), maintainance.class));
                return true;
            case R.id.logout_button:
                DBHelper amc_db = new DBHelper(getApplicationContext());
                amc_db.open();
                amc_db.logout();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                //Toast.makeText(getApplicationContext(), amc_db.get_user(), Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}