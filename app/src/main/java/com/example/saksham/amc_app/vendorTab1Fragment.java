package com.example.saksham.amc_app;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class vendorTab1Fragment extends Fragment {

    String[] data = {"You don't have any pending requests"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DBHelper amc_db = new DBHelper(getActivity());
        amc_db.open();
        String uid = amc_db.get_user();
        //Toast.makeText(getActivity(), uid, Toast.LENGTH_SHORT);
        Cursor c = amc_db.get_requests("vendor", uid);
        c.moveToFirst();
        int count = c.getCount();
        if (count!=0) {
            data = new String[count];
        }
        int i = 0;
        while (!c.isAfterLast()) {
            if (!c.getString(6).equals("completed")) {
                data[i] = "Device: " + c.getString(2) + "\nProblem: " + c.getString(3) + "\nUser: " + c.getString(1) + "\nDates: " + c.getString(5) + "\nStatus: " + c.getString(6) + "\n\n";
                i++;
            }
            c.moveToNext();
        }

        while (i < count) {
            data[i] = "\n";
            i++;
        }

        if (i==0) {
            data = new String[]{"You don't have any pending requests"};
        }

        List<String> completed_detail = new ArrayList<String>(Arrays.asList(data));

        final ArrayAdapter<String> mDetails = new ArrayAdapter<String>(
                getActivity(),R.layout.vendortab1fragment,R.id.list_iem1_textview,completed_detail);

        View rootView = inflater.inflate(R.layout.vendenq_tab1,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.pending_list1);
        listView.setAdapter(mDetails);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), ProcessRequest.class).putExtra(Intent.EXTRA_TEXT, mDetails.getItem(position));
                startActivity(i);
            }
        });
        return rootView;
    }



}
