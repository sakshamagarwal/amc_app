package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class sign_in_customer extends Activity {
    EditText username, password;
    String uname, pass;
    TextView forgotpass;
    boolean correct_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_customer);

        forgotpass = (TextView) findViewById(R.id.forgotpass);
        forgotpass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(sign_in_customer.this, forgot_password.class);
                startActivity(intent);
            }
        });

        ImageButton back = (ImageButton) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(sign_in_customer.this, intro_customer.class);
                startActivity(intent);
            }
        });

        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.sign_in_layout), getApplicationContext());
        username = (EditText)findViewById(R.id.signin_username);
        password = (EditText)findViewById(R.id.signin_password);

        Button sign_in = (Button)findViewById(R.id.sign_in);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uname = username.getText().toString();
                pass = password.getText().toString();

                new DownloadWebpageTask(new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        correct_login = validate_login(object, uname, pass);
                        if (correct_login) {
                            Intent i = new Intent(getApplicationContext(), new_req1.class).putExtra(Intent.EXTRA_TEXT, uname);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid username/password", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute("https://spreadsheets.google.com/tq?key=16ssUOZJADeDbuJQIkaYahxoqERKhsw6aE1KmojuaA70");

            }
        });

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