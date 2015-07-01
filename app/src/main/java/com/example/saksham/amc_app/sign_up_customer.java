package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class sign_up_customer extends Activity{
   @Override
   protected void onCreate(Bundle savedInstanceState){
       super.onCreate(savedInstanceState);
       setContentView(R.layout.sign_up_customer);
	
	Button next_btn =(Button)findViewById(R.id.next_btn);
	next_btn.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(getApplicationContext(),custsignup_addvend.class));
			
		}
	});
	
	 ImageButton back =(ImageButton) findViewById(R.id.back);
     back.setOnClickListener(new View.OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
            Intent intent = new Intent(getApplicationContext(),intro_customer.class);
            startActivity(intent);
			//Intent intent = new Intent(sign_up_customer.this,custsignup_addvend.class);
		 	//startActivity(intent);
		}
		});
}
}