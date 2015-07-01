package com.example.saksham.amc_app;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.saksham.amc_app.R;

public class forgot_password extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.forgot_password);
	
        ImageButton back =(ImageButton) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener(){

  	        @Override
  	        public void onClick(View v) {
  	    	    // TODO Auto-generated method stub
  	    	    Intent intent = new Intent(forgot_password.this,sign_in_customer.class);
  	    	    startActivity(intent);
            }
        });

        Button reset = (Button)findViewById(R.id.send);
        reset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Check email to reset password",Toast.LENGTH_LONG).show();
                startActivity(new Intent(getApplicationContext(),sign_in_customer.class));
            }
        });

    }
}
