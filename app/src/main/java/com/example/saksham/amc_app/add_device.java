package com.example.saksham.amc_app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class add_device extends Activity {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/d/1kmGcaEyEwyIbTweYSZrMEc1SJVEs1d-yjdwpRzNmaYk/formResponse";
    //input element ids found from the live form page
    public static final String VENDOR_KEY="entry_1604224892";
    public static final String USER_KEY="entry_1479064195";
    public static final String DEVICE_KEY="entry_1435849717";
    public static final String CAT_KEY="entry_1471235780";
    public static final String STATUS_KEY = "entry_1840874642";

    String[] data = new String[]{"Loading..."};
    String[] vid;
    Spinner vendors;
    EditText model;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ActionBar ab = getActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8bc34c")));
        final DBHelper amc_db = new DBHelper(add_device.this);
        amc_db.open();
        uid = amc_db.get_user();
        final Spinner category = (Spinner)findViewById(R.id.category);
        final ArrayAdapter<CharSequence> device_adapter = ArrayAdapter.createFromResource(this, R.array.device_choices, android.R.layout.simple_spinner_item);
        device_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        category.setAdapter(device_adapter);

        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.add_device_layout), add_device.this);

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                get_vendor_list(object);
                ArrayAdapter<String> problems_adapter = new ArrayAdapter<String>(add_device.this, android.R.layout.simple_spinner_item, data);
                problems_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                vendors.setAdapter(problems_adapter);
            }
        }, add_device.this).execute("https://spreadsheets.google.com/tq?key=1MoXjiGRLIOzjWOLO47nBslD7h0M9ODmNNvxQq6_41Dw");

        vendors = (Spinner)findViewById(R.id.vendor);
        ArrayAdapter<String> problems_adapter = new ArrayAdapter<String>(add_device.this, android.R.layout.simple_spinner_item, data);
        problems_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        vendors.setAdapter(problems_adapter);
        model = (EditText)findViewById(R.id.model_edit_text);
        Button request = (Button)findViewById(R.id.post_request);
        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amc_db.add_product(amc_db.get_user(), vendors.getSelectedItem().toString(), category.getSelectedItem().toString(), model.getText().toString());
                startActivity(new Intent(add_device.this, MaintenanceRequest.class));
            }
        });


        Button enquiry = (Button)findViewById(R.id.get_quotation);
        enquiry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDataTask postDataTask = new PostDataTask();
                postDataTask.execute(URL, vid[vendors.getSelectedItemPosition()], "\'" + uid, model.getText().toString(), category.getSelectedItem().toString(), "new");
            }
        });

    }

    private void get_vendor_list(JSONObject object) {
        try {
            JSONArray rows = object.getJSONArray("rows");
            String result = "";
            int len = rows.length();
            data = new String[len];
            vid = new String[len];
            for (int r = 0; r < len; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                vid[r] = columns.getJSONObject(1).getString("v");
                data[r] = columns.getJSONObject(2).getString("v");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_device, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... contactData) {
            Boolean result = true;
            String url = contactData[0];
            String uname = contactData[1];
            String email = contactData[2];
            String mob = contactData[3];
            String pass = contactData[4];
            String status = contactData[5];
            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = VENDOR_KEY+"=" + URLEncoder.encode(uname, "UTF-8") +
                        "&" + USER_KEY + "=" + URLEncoder.encode(email, "UTF-8") +
                        "&" + DEVICE_KEY + "=" + URLEncoder.encode(mob,"UTF-8") +
                        "&" + CAT_KEY + "=" + URLEncoder.encode(pass,"UTF-8") +
                        "&" + STATUS_KEY + "=" + URLEncoder.encode(status,"UTF-8");
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
                Toast.makeText(add_device.this, "Thank You For posting your enquiry", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(add_device.this, "There was a problem in posting, please try later", Toast.LENGTH_SHORT).show();
            }
        }

    }
}
