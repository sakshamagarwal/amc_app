package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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


public class customer_login extends Activity {

    EditText id;
    String id_text;
    TextView error, forgot_id;
    Button login_button;
    String[] data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_login);
        final DBHelper amc_db = new DBHelper(getApplicationContext());
        amc_db.open();
        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.login_layout), getApplicationContext());
        id = (EditText)findViewById(R.id.customer_uid);
        error = (TextView)findViewById(R.id.error_msg);
        forgot_id = (TextView)findViewById(R.id.lost_id);
        login_button = (Button)findViewById(R.id.login_submit);
        id.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                id_text = id.getText().toString();
                if (id_text.length()!=8 && id_text.length()!=0) {
                    error.setText("Id must be 8 characters long");
                } else {
                    error.setText("");
                }
            }
        });

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            login_button.setEnabled(true);
        } else {
            Toast.makeText(getApplicationContext(), "Could not connect to the internet", Toast.LENGTH_LONG).show();
            login_button.setEnabled(false);
        }

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_text = id.getText().toString();
                if (id_text.length()!=5) {
                    error.setText("Id must be 8 characters long");
                } else {
                    if (amc_db.isPresent("customer_info", "user_id", id_text)) {
                        amc_db.login(id_text);
                        Toast.makeText(getApplicationContext(), "From DB", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MaintenanceRequest.class));
                    } else {
                        new DownloadWebpageTask(new AsyncResult() {
                            @Override
                            public void onResult(JSONObject object) {
                                if (validate_login(object, id_text)) {
                                    amc_db.insert(data[0], data[1], data[2], data[3], data[4]);
                                    amc_db.login(id_text);
                                    Intent i = new Intent(getApplicationContext(), MaintenanceRequest.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Invalid Id", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }, getApplicationContext()).execute("https://spreadsheets.google.com/tq?key=1WVBAP1zx_JmnvhLSRzKj4B-F19Y4XIWpws5C0WGrE3o");
                    }
                }
            }
        });

        forgot_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), forgot_id.class));
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.vendor_login) {
            startActivity(new Intent(getApplicationContext(), VendorLogin.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validate_login(JSONObject object, String uid) {
        try {
            JSONArray rows = object.getJSONArray("rows");
            int len = rows.length();
            data = new String[5];

            for (int r = 0; r < len; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String cust_id = columns.getJSONObject(1).getString("v");
                if (cust_id.equals(uid)) {
                    data[0] = cust_id;
                    data[1] = columns.getJSONObject(2).getString("v");
                    data[2] = columns.getJSONObject(3).getString("v");
                    data[3] = columns.getJSONObject(4).getString("v");
                    data[4] = columns.getJSONObject(5).getString("v");
                    return true;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //t.setText("Hello Exception");
        }

        return false;
    }

}
