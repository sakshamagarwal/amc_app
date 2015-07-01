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

public class sign_in_customer extends Activity {
    EditText username, password;
    String uname, pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_customer);


        TextView forgotpass = (TextView) findViewById(R.id.forgotpass);
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
                if (uname.equals(pass)) {
                    startActivity(new Intent(getApplicationContext(),new_req1.class));
                }
                else {
                    Toast.makeText(getApplicationContext(), "Invalid Username/Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}