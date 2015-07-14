package com.example.saksham.amc_app;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


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

        public PlaceholderFragment() {
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            final View rootView = inflater.inflate(R.layout.fragment_process_request, container, false);
            Intent intent = getActivity().getIntent();
            if (intent!=null && intent.hasExtra(Intent.EXTRA_TEXT)) {
                String detailStr = intent.getStringExtra(Intent.EXTRA_TEXT);
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

            return rootView;
        }


    }

}
