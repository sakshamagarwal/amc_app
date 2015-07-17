package com.example.saksham.amc_app;

import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by saksham on 22/6/15.
 */
public class Tab1Fragment extends Fragment {

    String[] data = {"You have not made any requests yet"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DBHelper amc_db = new DBHelper(getActivity());
        amc_db.open();
        String uid = amc_db.get_user();
        //Toast.makeText(getActivity(), uid, Toast.LENGTH_SHORT);
        Cursor c = amc_db.get_requests("user", uid);
        c.moveToFirst();
        int count = c.getCount();
        if (count!=0) {
            int size = 0;
            while (!c.isAfterLast()) {
                if (c.getString(6).equals("new") || c.getString(6).equals("engineer assigned")) {
                    size++;
                }
                c.moveToNext();
            }
            data = new String[size];
        }
        c.moveToFirst();
        int i = 0;
        while (!c.isAfterLast()) {
            if (c.getString(6).equals("new") || c.getString(6).equals("engineer assigned")) {
                data[i] = "Device: " + c.getString(2) + "\nProblem: " + c.getString(3) + "\nVendor: " + c.getString(0) + "\nStatus: " + c.getString(6) + "\n\n";
                i++;
            }
            c.moveToNext();
        }


        List<String> completed_detail = new ArrayList<String>(Arrays.asList(data));

        ArrayAdapter<String> mDetails = new ArrayAdapter<String>(
                getActivity(),R.layout.tab1_list_item,R.id.list_iem1_textview,completed_detail);

        View rootView = inflater.inflate(R.layout.maintainance_tab1,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.pending_list);
        listView.setAdapter(mDetails);
        return rootView;
    }


}
