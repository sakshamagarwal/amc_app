package com.example.saksham.amc_app;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by saksham on 22/6/15.
 */
public class maintainance extends Activity {

    String uid;
    DBHelper amc_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.maintainance);
        amc_db = new DBHelper(maintainance.this);
        amc_db.open();
        uid = amc_db.get_user();

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                update_requests(object, uid);

            }
        }, maintainance.this).execute("https://spreadsheets.google.com/tq?key=1vi4YuASm8Pn9w3TXZrdsD6zsaHHVRAc1I5STzC8ix7M");


        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8bc34c")));
        String label1 = "Pending Requests";
        Tab tab = ab.newTab();
        tab.setText(label1);
        TabListener<Tab1Fragment> t1 = new TabListener<Tab1Fragment>(this,label1,Tab1Fragment.class);
        tab.setTabListener(t1);
        ab.addTab(tab);
        String label2 = "Completed Requests";
        tab = ab.newTab();
        tab.setText(label2);
        TabListener<Tab2Fragment> t2 = new TabListener<Tab2Fragment>(this,label2,Tab2Fragment.class);
        tab.setTabListener(t2);
        ab.addTab(tab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.enquiry_mode:
                startActivity(new Intent(maintainance.this,enquiry.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(maintainance.this,MaintenanceRequest.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class TabListener<T extends Fragment> implements ActionBar.TabListener {
        private Fragment mFragment;
        private final Activity mActivity;
        private final String mTag;
        private final Class<T> mClass;

        public TabListener(Activity activity, String tag, Class<T> clz) {
            mActivity = activity;
            mTag = tag;
            mClass = clz;
        }
        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(mFragment == null)
            {
                mFragment = Fragment.instantiate(mActivity, mClass.getName());
                ft.add(android.R.id.content, mFragment, mTag);
            } else {
                ft.attach(mFragment);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if(mFragment != null)
            {
                ft.detach(mFragment);
            }
        }
    }

    private void update_requests(JSONObject object, String user) {
        String user_id;
        String vendor;
        String device;
        String problem;
        String description;
        String dates;
        String status;
        try {
            JSONArray rows = object.getJSONArray("rows");
            String result = "";
            int len = rows.length();
            for (int r = 0; r < len; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                user_id = columns.getJSONObject(2).getString("v");
                vendor = columns.getJSONObject(1).getString("v");
                device = columns.getJSONObject(3).getString("v");
                problem = columns.getJSONObject(4).getString("v");
                description = columns.getJSONObject(5).getString("v");
                dates = columns.getJSONObject(6).getString("v");
                status = columns.getJSONObject(7).getString("v");
                if (user_id.equals(uid)) {
                    amc_db.add_request(vendor, user_id, device, problem,description,dates, status);
                }
            }


            //Log.i("id---", id+"");
            //t.setText(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}


