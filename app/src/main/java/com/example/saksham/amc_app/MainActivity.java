package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final DBHelper amc_db = new DBHelper(getApplicationContext());
        amc_db.open();
        ImageButton homebtn = (ImageButton) findViewById(R.id.btn_home);
        homebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!amc_db.isLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), customer_login.class));
                } else {
                    startActivity(new Intent(getApplicationContext(), MaintenanceRequest.class));
                }
            }
        });

    }

}
