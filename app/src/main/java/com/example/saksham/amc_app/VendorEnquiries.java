package com.example.saksham.amc_app;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VendorEnquiries extends Activity {

    DBHelper amc_db;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_vendor_enquiries);

        amc_db = new DBHelper(VendorEnquiries.this);
        amc_db.open();
        uid = amc_db.get_user();

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                update_enquiries(object, uid);
            }
        }, VendorEnquiries.this).execute("https://spreadsheets.google.com/tq?key=1LexGBch7rDXbyK0h_KqdEp27KsN-77p4W8m5NEvWWuM");

        ActionBar ab = getActionBar();
        ab.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#8bc34c")));
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        String label1 = "Pending Enquiries";
        ActionBar.Tab tab = ab.newTab();
        tab.setText(label1);
        TabListener<v_e_tab1> t1 = new TabListener<v_e_tab1>(this,label1,v_e_tab1.class);
        tab.setTabListener(t1);
        ab.addTab(tab);
        String label2 = "Completed Enquiries";
        tab = ab.newTab();
        tab.setText(label2);
        TabListener<v_e_tab2> t2 = new TabListener<v_e_tab2>(this,label2,v_e_tab2.class);
        tab.setTabListener(t2);
        ab.addTab(tab);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vendor_enquiries, menu);
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

    private void update_enquiries(JSONObject object, String user) {
        String user_id;
        String vendor;
        String device;
        String category;
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
                category = columns.getJSONObject(4).getString("v");
                //description = columns.getJSONObject(5).getString("v");
                //dates = columns.getJSONObject(6).getString("v");
                status = columns.getJSONObject(5).getString("v");
                if (vendor.equals(uid)) {
                    amc_db.add_enquiry(vendor, user_id, device, category, status);
                }
            }


            //Log.i("id---", id+"");
            //t.setText(result);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
