package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class forgot_id extends Activity {

    Button button;
    String[] data;
    String mobile_no;
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_id);
        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.forgot_id_layout), getApplicationContext());
        button = (Button)findViewById(R.id.get_id_button);
        textView = (TextView)findViewById(R.id.id_result);
        final EditText editText = (EditText)findViewById(R.id.mobile);

        final DBHelper amc_db = new DBHelper(getApplicationContext());
        amc_db.open();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile_no = editText.getText().toString();
                if (mobile_no.length()!=10) {
                    textView.setText("Invalid Mobile Number");
                }
                new DownloadWebpageTask(new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        if (get_id(object, mobile_no)!="") {
                            amc_db.insert(data[0], data[1], data[2], data[3], data[4]);
                            //amc_db.login(id_text);
                            //Intent i = new Intent(getApplicationContext(), new_req1.class);
                            //startActivity(i);
                            textView.setText("Hi " + data[1] + ".\nYour id is " + data[0]);
                            textView.setTextColor(Color.parseColor("#0000FF"));
                        } else {
                            textView.setText("Please contact your AMC vendor and get registered");
                        }
                    }
                }, getApplicationContext()).execute("https://spreadsheets.google.com/tq?key=1WVBAP1zx_JmnvhLSRzKj4B-F19Y4XIWpws5C0WGrE3o");
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_forgot_id, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String get_id(JSONObject object, String mob) {
        try {
            JSONArray rows = object.getJSONArray("rows");
            int len = rows.length();
            data = new String[5];

            for (int r = 0; r < len; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String mob_iter = columns.getJSONObject(4).getString("v");
                if (mob_iter.equals(mob)) {
                    data[0] = columns.getJSONObject(1).getString("v");
                    data[1] = columns.getJSONObject(2).getString("v");
                    data[2] = columns.getJSONObject(3).getString("v");
                    data[3] = columns.getJSONObject(4).getString("v");
                    data[4] = columns.getJSONObject(5).getString("v");
                    return data[0];
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //t.setText("Hello Exception");
        }

        return "";
    }
}
