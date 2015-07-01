package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class intro_customer extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
	    setContentView(R.layout.intro_customer);
		
		Button signin_btn =(Button) findViewById(R.id.signin_btn);
		signin_btn.setOnClickListener(new View.OnClickListener(){

	  		@Override
	  		public void onClick(View v) {
	  	        // TODO Auto-generated method stub
	      		Intent intent = new Intent(intro_customer.this,sign_in_customer.class);
	  		    startActivity(intent);
        	  	}
	    });

		Button signup_btn =(Button) findViewById(R.id.signup_btn);
		signup_btn.setOnClickListener(new View.OnClickListener(){

	  		@Override
	  		public void onClick(View v) {
	  	        // TODO Auto-generated method stub
	            Intent i = new Intent(intro_customer.this,sign_up_customer.class);
	  		    startActivity(i);
      		}
	    });
	}
}
