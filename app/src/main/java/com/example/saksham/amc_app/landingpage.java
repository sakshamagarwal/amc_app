package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class landingpage extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.landingpage);
        Button cust = (Button)findViewById(R.id.customer_btn);
        cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(landingpage.this,intro_customer.class);
                startActivity(intent);
            }
        });

        Button vend = (Button)findViewById(R.id.vendor_btn);
        vend.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),intro_vendor.class);
                startActivity(i);
            }
        });
    }

}
