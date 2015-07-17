package com.example.saksham.amc_app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Button;
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


public class ProcessRequest extends Activity {
    String[] engineers = new String[]{"Loading..."};
    DBHelper amc_db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_request);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_process_request, menu);
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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        String[] details;
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

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_process_request, container, false);
            Intent intent = getActivity().getIntent();
            if (intent!=null) {
                Bundle b = intent.getExtras();
                details = b.getStringArray("EXTRA_TEXT");
                String detailStr = "User: " + details[0] + "\nDevice:" +  details[2] + "\nProblem: " + details[3] + "\nDescription: " + details[4];
                ((TextView) rootView.findViewById(R.id.req_info)).setText(detailStr);
            }

            String[] engineers;
            DBHelper my_db = new DBHelper(getActivity());
            my_db.open();
            Cursor c = my_db.get_engineers(my_db.get_user());
            c.moveToFirst();
            int count = c.getCount();
            engineers = new String[count];
            int i = 0;
            if (count!=0) {
                while (!c.isAfterLast()) {
                    engineers[i] = c.getString(2);
                    i++;
                    c.moveToNext();
                }
            } else {
                engineers = new String[]{"Loading..."};
            }
            Spinner eng_spinner = (Spinner)rootView.findViewById(R.id.select_eng);
            ArrayAdapter<String> problems_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, engineers);
            problems_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            eng_spinner.setAdapter(problems_adapter);

            Button reject = (Button)rootView.findViewById(R.id.reject);
            reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostDataTask postDataTask = new PostDataTask();
                    postDataTask.execute(URL,details[0], "\'" + details[1], details[2], details[3] , details[4] , details[5], "rejected");
                }
            });
            return rootView;
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
                    Toast.makeText(getActivity(), "Thank You For processing", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "There was a problem in posting, please try later", Toast.LENGTH_SHORT).show();
                }
            }

        }

    }

}
