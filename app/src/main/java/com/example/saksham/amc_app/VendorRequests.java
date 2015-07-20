package com.example.saksham.amc_app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.app.ActionBar.Tab;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VendorRequests extends Activity {

    DBHelper amc_db;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_vendor_requests);
        amc_db = new DBHelper(VendorRequests.this);
        amc_db.open();
        uid = amc_db.get_user();

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                update_requests(object, uid);
            }
        }, VendorRequests.this).execute("https://spreadsheets.google.com/tq?key=1vi4YuASm8Pn9w3TXZrdsD6zsaHHVRAc1I5STzC8ix7M");

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                get_engineer_list(object, uid);
            }
        },VendorRequests.this).execute("https://spreadsheets.google.com/tq?key=1ktZNgZJOR2BD0TliTd2oaYe7WhmZhvtG_gZXAEE6APM");

        ActionBar ab = getActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8bc34c")));
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        String label1 = "Pending Requests";
        Tab tab = ab.newTab();
        tab.setText(label1);
        TabListener<vendorTab1Fragment> t1 = new TabListener<vendorTab1Fragment>(this,label1,vendorTab1Fragment.class);
        tab.setTabListener(t1);
        ab.addTab(tab);
        String label2 = "Completed Requests";
        tab = ab.newTab();
        tab.setText(label2);
        TabListener<vendorTab2Fragment> t2 = new TabListener<vendorTab2Fragment>(this,label2,vendorTab2Fragment.class);
        tab.setTabListener(t2);
        ab.addTab(tab);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vendor_requests, menu);
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
        } else if (id == R.id.action_logout) {
            amc_db.logout();
            startActivity(new Intent(VendorRequests.this, MainActivity.class));
        } else if (id == R.id.to_enquiry) {
            startActivity(new Intent(VendorRequests.this, VendorEnquiries.class));
        }

        return super.onOptionsItemSelected(item);
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
                if (vendor.equals(uid)) {
                    amc_db.add_request(vendor, user_id, device, problem,description,dates, status);
                }
            }


            //Log.i("id---", id+"");
            //t.setText(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void get_engineer_list(JSONObject object, String user) {
        String vendor;
        String eng;
        String name;
        String[] temp;
        try {
            JSONArray rows = object.getJSONArray("rows");
            String result = "";
            int len = rows.length();
            int i = 0;
            for (int r = 0; r < len; ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");
                vendor = columns.getJSONObject(1).getString("v");
                eng = columns.getJSONObject(2).getString("v");
                name = columns.getJSONObject(3).getString("v");
                    /*description = columns.getJSONObject(5).getString("v");
                    dates = columns.getJSONObject(6).getString("v");
                    status = columns.getJSONObject(7).getString("v");*/
                if (vendor.equals(user)) {
                    amc_db.add_engineer(vendor, eng, name);
                }
            }
            /*temp = new String[i];
            for (int j= 0; j < len; j++) {
                if (engineers[j]!=null) {
                    temp[j] = engineers[j];
                }
            }

            engineers = new String[i];
            for (int j = 0; j < i; j++ ) {
                engineers[j] = temp[j];
            }*/

            //Log.i("id---", id+"");
            //t.setText(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
