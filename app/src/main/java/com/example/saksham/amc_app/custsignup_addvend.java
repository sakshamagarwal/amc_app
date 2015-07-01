package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class custsignup_addvend extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custsignup_addvend);
        Button sign_up = (Button)findViewById(R.id.add_vend_btn);
        sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Thank You For SIGN UP", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),sign_in_customer.class));
            }
        });
        ImageButton back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),sign_up_customer.class);
                startActivity(i);
            }
        });
    }
}
