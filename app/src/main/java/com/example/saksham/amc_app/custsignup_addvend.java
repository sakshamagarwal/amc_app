package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class custsignup_addvend extends Activity {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/d/148FIuoO2ddLjvzi68xJkxo-NVmaMq0K6_8_lo5YmcTw/formResponse";
    //input element ids found from the live form page
    public static final String UNAME_KEY="entry_1721088693";
    public static final String EMAIL_KEY="entry_139574038";
    public static final String MOBILE_KEY="entry_340976024";
    public static final String PASS_KEY="entry_1377524012";
    public static final String VEND_KEY="entry_1909718012";
    public static final String REG_KEY="entry_685105578";

    String uname, pass, email, mob, vend, reg;
    EditText vendor_id, reg_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custsignup_addvend);
        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.add_vend_layout), getApplicationContext());
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        uname = extras.getString("EXTRA_UNAME");
        pass = extras.getString("EXTRA_PASS");
        mob = extras.getString("EXTRA_MOB");
        email = extras.getString("EXTRA_EMAIL");

        vendor_id = (EditText)findViewById(R.id.signup_vendorid);
        reg_no = (EditText)findViewById(R.id.signup_regno);


        Button sign_up = (Button)findViewById(R.id.add_vend_btn);
        sign_up.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                vend = vendor_id.getText().toString();
                reg = reg_no.getText().toString();
                PostDataTask postDataTask = new PostDataTask();
                //execute asynctask
                postDataTask.execute(URL,uname, email, mob, pass, vend, reg);
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

    //AsyncTask to send data as a http POST request
    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String uname = contactData[1];
            String email = contactData[2];
            String mob = contactData[3];
            String pass = contactData[4];
            String vend = contactData[5];
            String reg = contactData[6];
            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = UNAME_KEY+"=" + URLEncoder.encode(uname, "UTF-8") +
                        "&" + EMAIL_KEY + "=" + URLEncoder.encode(email, "UTF-8") +
                        "&" + MOBILE_KEY + "=" + URLEncoder.encode(mob,"UTF-8") +
                        "&" + PASS_KEY + "=" + URLEncoder.encode(pass,"UTF-8") +
                        "&" + VEND_KEY + "=" + URLEncoder.encode(vend,"UTF-8") +
                        "&" + REG_KEY + "=" + URLEncoder.encode(reg,"UTF-8");
                //t.setText(postBody);

            } catch (UnsupportedEncodingException ex) {
                result=false;
            }

            /*
            //If you want to use HttpRequest class from http://stackoverflow.com/a/2253280/1261816
            try {
			HttpRequest httpRequest = new HttpRequest();
			httpRequest.sendPost(url, postBody);
		}catch (Exception exception){
			result = false;
		}
            */

            try{
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
                //Toast.makeText(getApplicationContext(),"Hello World",Toast.LENGTH_SHORT).show();
                //t.setText("Hello Saksham");
                result = true;
            }catch (IOException exception){
                result=false;
            }
            return result;
        }

        @Override
        protected void onPostExecute(Boolean result){
            //Print Success or failure message accordingly
            if (result) {
                Toast.makeText(getApplicationContext(), "Thank You For SIGN UP", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),sign_in_customer.class));
            } else {
                Toast.makeText(getApplicationContext(), "There was a problem in sign_up, please try later", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
