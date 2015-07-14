package com.example.saksham.amc_app;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
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
public class enquiry extends Activity {
    DBHelper amc_db;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        amc_db = new DBHelper(getApplicationContext());
        amc_db.open();
        uid = amc_db.get_user();

        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                update_enquiries(object, uid);

            }
        }, getApplicationContext()).execute("https://spreadsheets.google.com/tq?key=1LexGBch7rDXbyK0h_KqdEp27KsN-77p4W8m5NEvWWuM");


        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.maintainance);
        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        String label1 = "Pending Enquiries";
        Tab tab = ab.newTab();
        tab.setText(label1);
        TabListener<E_Tab1Fragment> t1 = new TabListener<E_Tab1Fragment>(this,label1,E_Tab1Fragment.class);
        tab.setTabListener(t1);
        ab.addTab(tab);
        String label2 = "Completed Enquiries";
        tab = ab.newTab();
        tab.setText(label2);
        TabListener<E_Tab2Fragment> t2 = new TabListener<E_Tab2Fragment>(this,label2,E_Tab2Fragment.class);
        tab.setTabListener(t2);
        ab.addTab(tab);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.enquiry_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.maintainence_mode:
                startActivity(new Intent(getApplicationContext(), maintainance.class));
                return true;
            case R.id.add_enquiry:
                startActivity(new Intent(getApplicationContext(),new_enquiry1.class));
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
                status = columns.getJSONObject(5).getString("v");
                if (user_id.equals(uid)) {
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


