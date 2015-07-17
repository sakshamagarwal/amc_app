package com.example.saksham.amc_app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar ab = getActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8bc34c")));
        final DBHelper amc_db = new DBHelper(getApplicationContext());
        amc_db.open();
        ImageView homebtn = (ImageView) findViewById(R.id.btn_home);
        homebtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!amc_db.isLoggedIn()) {
                    startActivity(new Intent(getApplicationContext(), customer_login.class));
                } else {
                    String user = amc_db.get_user();
                    if (user.toCharArray()[0] == 'V' || user.toCharArray()[0] == 'v') {
                        startActivity(new Intent(getApplicationContext(), VendorRequests.class));
                    } else {
                        startActivity(new Intent(getApplicationContext(), MaintenanceRequest.class));
                    }
                }
            }
        });

    }

}
