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

/**
 * Created by saksham on 22/6/15.
 */
public class maintainance extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.maintainance);
        ActionBar ab = getActionBar();
        ab.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
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
                startActivity(new Intent(getApplicationContext(),enquiry.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(getApplicationContext(),new_req1.class));
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
}


