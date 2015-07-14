package com.example.saksham.amc_app;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class MaintenanceRequest extends Activity {

    public static final MediaType FORM_DATA_TYPE
            = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    //URL derived from form URL
    public static final String URL="https://docs.google.com/forms/d/1OKyFOdR2IImzJyJ6KIpaPFmCf0k8Zh4GO0WpK1iBwTs/formResponse";
    //input element ids found from the live form page
    public static final String VENDOR_KEY="entry_1471383627";
    public static final String USER_KEY="entry_305999264";
    public static final String DEVICE_KEY="entry_2056691727";
    public static final String PROB_KEY="entry_1539963603";
    public static final String DESC_KEY="entry_1735846356";
    public static final String DATE_KEY="entry_971642410";
    public static final String STATUS_KEY="entry_856878217";


    String uid, global_device;
    HorizontalScrollView horizontalScrollView;
    LinearLayout linearLayout;
    DBHelper amc_db;
    Button button, submit;
    String[] problems_array;
    Spinner device_problems;
    EditText desc;
    int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_request);
        KeyboardManager km = new KeyboardManager((ViewGroup)findViewById(R.id.maintenance_request_layout), getApplicationContext());
        TextView add_new = (TextView)findViewById(R.id.add_device_text);
        add_new.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), add_device.class));
            }
        });

        amc_db = new DBHelper(getApplicationContext());
        amc_db.open();
        uid = amc_db.get_user();
        horizontalScrollView = (HorizontalScrollView)findViewById(R.id.device_scroll);
        linearLayout = (LinearLayout)findViewById(R.id.device_linear);
        device_problems = (Spinner)findViewById(R.id.problem_spinner);
        submit = (Button)findViewById(R.id.request_button);
        desc = (EditText)findViewById(R.id.description);
        if (!amc_db.isPresent("product_info", "user_id", uid)) {
            make_horizontal_view();
            update_problems_database();
        } else {
            Cursor c = amc_db.get_products(uid);
            c.moveToFirst();
            linearLayout.removeAllViews();
            while (!c.isAfterLast()) {
                button = new Button(MaintenanceRequest.this);
                button.setText(c.getString(3));
                linearLayout.addView(button);
                c.moveToNext();
            }
            setListeners(linearLayout);
        }

        Toast.makeText(getApplicationContext(), "Hi " + amc_db.get_name(uid), Toast.LENGTH_LONG).show();
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cur = amc_db.get_products(uid);
                cur.moveToFirst();
                while (!cur.getString(3).equals(global_device)) {
                    cur.moveToNext();
                }
                String vendor = cur.getString(1);
                String date = "";
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                CheckBox cb = (CheckBox)findViewById(R.id.asap);
                if (cb.isChecked()) {
                    date += "ASAP ";
                }
                cb = (CheckBox)findViewById(R.id.today);
                if (cb.isChecked()) {
                    date += df.format(c.getTime());
                }
                cb = (CheckBox)findViewById(R.id.tommorow);
                if (cb.isChecked()) {
                    c.add(Calendar.DAY_OF_YEAR, 1);
                    date += df.format(c.getTime());
                }
                PostDataTask postDataTask = new PostDataTask();
                //execute asynctask

                String problem = device_problems.getSelectedItem().toString();
                postDataTask.execute(URL,vendor, "\'" + uid, global_device, problem , desc.getText().toString() , date, "new");
                amc_db.add_request(vendor, uid, global_device, problem, desc.getText().toString(), date, "new");
            }
        });

    }

    private void update_problems_database() {
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                update_problems(object);
            }
        }, getApplicationContext()).execute("https://spreadsheets.google.com/tq?key=1iwnc9RwH0Af_zjCKWYlEBFuydPbizUO2tXlB4Epabt0");
    }

    private void update_problems(JSONObject object) {
        String category;
        String problem;

        try {
            JSONArray rows = object.getJSONArray("rows");
            String result = "";
            int len = rows.length();
            for (int r = 0; r < len; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                category = columns.getJSONObject(1).getString("v");
                problem = columns.getJSONObject(2).getString("v");
                amc_db.add_problems(category, problem);
            }

            //Log.i("id---", id+"");
            //t.setText(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setListeners(LinearLayout l) {
        int x = l.getChildCount();
        for (int i = 0; i < x; ++i) {
            Button b = (Button)linearLayout.getChildAt(i);
            final String device = b.getText().toString();
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    global_device = device;
                    String category = amc_db.get_category(amc_db.get_user(), device);
                    Cursor cur = amc_db.get_problems(category);
                    int problems_length = cur.getCount();
                    problems_array = new String[problems_length];
                    cur.moveToFirst();
                    int i = 0;
                    while (!cur.isAfterLast()) {
                        problems_array[i] = cur.getString(1);
                        cur.moveToNext();
                        i++;
                    }
                    ArrayAdapter<String> problems_adapter = new ArrayAdapter<String>(MaintenanceRequest.this, android.R.layout.simple_spinner_item, problems_array);
                    problems_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    device_problems.setAdapter(problems_adapter);

                }
            });
        }
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_maintenance_request, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.enquiry_button:
                startActivity(new Intent(getApplicationContext(),enquiry.class));
                return true;
            case R.id.history_button:
                startActivity(new Intent(getApplicationContext(), maintainance.class));
                return true;
            case R.id.action_logout:
                amc_db.logout();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                return true;
            case R.id.action_refresh:
                make_horizontal_view();
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void make_horizontal_view () {
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                update_products(object, uid);
                Cursor c = amc_db.get_products(uid);
                c.moveToFirst();
                linearLayout.removeAllViews();
                while (!c.isAfterLast()) {
                    button = new Button(MaintenanceRequest.this);
                    button.setText(c.getString(3));
                    linearLayout.addView(button);
                    c.moveToNext();
                }
                setListeners(linearLayout);
            }
        }, getApplicationContext()).execute("https://spreadsheets.google.com/tq?key=1dZOtZTU2_ilIzdQYXme30ISWTJnxdaGqapP6FeG_blk");

    }

    private void update_products(JSONObject object, String uid) {

        String user_id;
        String vendor;
        String cat;
        String prod;

        try {
            JSONArray rows = object.getJSONArray("rows");
            String result = "";
            int len = rows.length();
            for (int r = 0; r < len; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                user_id = columns.getJSONObject(1).getString("v");
                vendor = columns.getJSONObject(2).getString("v");
                cat = columns.getJSONObject(3).getString("v");
                prod = columns.getJSONObject(4).getString("v");

                if (user_id.equals(uid)) {
                    amc_db.add_product(user_id, vendor, cat, prod);
                }
            }


            //Log.i("id---", id+"");
            //t.setText(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            String vend = contactData[5];
            String reg = contactData[6];
            String status = contactData[7];
            String postBody="";

            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = VENDOR_KEY+"=" + URLEncoder.encode(uname, "UTF-8") +
                        "&" + USER_KEY + "=" + URLEncoder.encode(email, "UTF-8") +
                        "&" + DEVICE_KEY + "=" + URLEncoder.encode(mob,"UTF-8") +
                        "&" + PROB_KEY + "=" + URLEncoder.encode(pass,"UTF-8") +
                        "&" + DESC_KEY + "=" + URLEncoder.encode(vend,"UTF-8") +
                        "&" + DATE_KEY + "=" + URLEncoder.encode(reg,"UTF-8") +
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
                Toast.makeText(getApplicationContext(), "Thank You For Requesting", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "There was a problem in posting, please try later", Toast.LENGTH_SHORT).show();
            }
        }

    }

}