package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import java.util.Arrays;

public class sign_up_customer extends Activity {

    String[] uname_data, mobile_data, email_data;
    TextView t;
    EditText usernameEditText, emailidEditText, passwordEditText, confirmpassEditText, mobileEditText;
    String username, password, confirmpass, emailid, mobile;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_customer);
        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.sign_up_layout), getApplicationContext());
        Button next_btn =(Button)findViewById(R.id.next_btn);
        t = (TextView)findViewById(R.id.sign_up_text);
        usernameEditText = (EditText)findViewById(R.id.signup_username);
        emailidEditText = (EditText) findViewById(R.id.signup_emailid);
        passwordEditText = (EditText) findViewById(R.id.signup_pass);
        confirmpassEditText = (EditText) findViewById(R.id.signup_confirmpass);
        mobileEditText = (EditText) findViewById(R.id.signup_mobileno);
        ConnectivityManager connMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnected()) {
            next_btn.setEnabled(true);
        } else {
            next_btn.setEnabled(false);
        }
        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = usernameEditText.getText().toString();
                emailid =  emailidEditText.getText().toString();
                password = passwordEditText.getText().toString();
                confirmpass = confirmpassEditText.getText().toString();
                mobile = mobileEditText.getText().toString();

                new DownloadWebpageTask(new AsyncResult() {
                    @Override
                    public void onResult(JSONObject object) {
                        processJson(object);
                        if (Arrays.asList(uname_data).contains(username)) {
                            Toast.makeText(getApplicationContext(), "Sorry username already used", Toast.LENGTH_SHORT).show();
                        } else if (Arrays.asList(email_data).contains(emailid)) {
                            Toast.makeText(getApplicationContext(), "1 Email Id can be used with only 1 Account", Toast.LENGTH_SHORT).show();
                        } else if (Arrays.asList(mobile_data).contains(mobile)) {
                            Toast.makeText(getApplicationContext(), "1 mobile no. can be used with only 1 Account", Toast.LENGTH_SHORT).show();
                        } else if (password.equals(confirmpass) && password!="") {
                            Intent i = new Intent(getApplicationContext(),custsignup_addvend.class);
                            Bundle extras = new Bundle();
                            extras.putString("EXTRA_UNAME", username);
                            extras.putString("EXTRA_PASS", password);
                            extras.putString("EXTRA_EMAIL", emailid);
                            extras.putString("EXTRA_MOB", mobile);
                            i.putExtras(extras);
                            startActivity(i);
                        } else {
                            Toast.makeText(getApplicationContext(), "Passwords do not match", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).execute("https://spreadsheets.google.com/tq?key=16ssUOZJADeDbuJQIkaYahxoqERKhsw6aE1KmojuaA70");

            }
        });

	
        ImageButton back =(ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            Intent intent = new Intent(getApplicationContext(),intro_customer.class);
            startActivity(intent);
		}
		});
    }

    private void processJson(JSONObject object) {
        try {
            JSONArray rows = object.getJSONArray("rows");
            int len = rows.length();
            uname_data = new String[len];
            email_data = new String[len];
            mobile_data = new String[len];
            for (int r = 0; r < len; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                String name = columns.getJSONObject(1).getString("v");
                String e = columns.getJSONObject(2).getString("v");
                String m = columns.getJSONObject(3).getString("f");
                uname_data[r] = name;
                email_data[r] = e;
                mobile_data[r] = m;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            //t.setText("Hello Exception");
        }
        //unique_uname = true;
    }

}

