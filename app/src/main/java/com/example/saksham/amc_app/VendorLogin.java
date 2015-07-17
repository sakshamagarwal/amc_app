package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
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


public class VendorLogin extends Activity {

    EditText username, password;
    String uname, pass;
    DBHelper amc_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_login);
        TextView forgotpass = (TextView) findViewById(R.id.forgotpass);
        forgotpass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Intent in = new Intent(signin_vend.this, frogotpassvendor.class);
                //startActivity(in);

            }
        });
        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.vendor_login_layout) ,VendorLogin.this);
        amc_db = new DBHelper(VendorLogin.this);
        amc_db.open();
        username = (EditText)findViewById(R.id.signin_username);
        password = (EditText)findViewById(R.id.signin_pass);


        Button signin_btnv =(Button) findViewById(R.id.signin_btnv);
        signin_btnv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                uname = username.getText().toString();
                pass = password.getText().toString();

                new DownloadWebpageTask(new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        boolean correct_login = validate_login(object, uname, pass);
                        if (correct_login) {
                            amc_db.login(uname);
                            Intent i = new Intent(VendorLogin.this, VendorRequests.class).putExtra(Intent.EXTRA_TEXT, uname);
                            startActivity(i);
                        } else {
                            Toast.makeText(VendorLogin.this, "Invalid username/password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, VendorLogin.this).execute("https://spreadsheets.google.com/tq?key=1MoXjiGRLIOzjWOLO47nBslD7h0M9ODmNNvxQq6_41Dw");

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vendor_login, menu);
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

    private boolean validate_login(JSONObject object, String uname, String pass) {
        try {
            JSONArray rows = object.getJSONArray("rows");
            int len = rows.length();
            String uname_iter, pass_iter;
            for (int r = 0; r < len; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                uname_iter = columns.getJSONObject(1).getString("v");
                if (uname_iter.equals(uname)) {
                    pass_iter = columns.getJSONObject(4).getString("v");
                    //forgotpass.setText(uname_iter);
                    if (pass.equals(pass_iter)) {
                        return true;
                    }
                    else {
                        return false;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }
}
