package com.example.saksham.amc_app;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by saksham on 22/6/15.
 */
public class Tab2Fragment extends Fragment{

    String[] data;
    String uid;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        DBHelper amc_db = new DBHelper(getActivity());
        amc_db.open();
        uid = amc_db.get_user();
        //Toast.makeText(getActivity(), uid, Toast.LENGTH_SHORT);
        Cursor c = amc_db.get_requests("user", uid);
        c.moveToFirst();
        int count = c.getCount();
        if (count!=0) {
            int size = 0;
            while (!c.isAfterLast()) {
                if (c.getString(6).equals("completed") || c.getString(6).equals("rejected")) {
                    size++;
                }
                c.moveToNext();
            }
            data = new String[size];
        }
        int i = 0;
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(6).equals("completed") || c.getString(6).equals("rejected")) {
                data[i] = "Device: " + c.getString(2) + "\nProblem: " + c.getString(3) + "\nVendor: " + c.getString(0) + "\nStatus: " + c.getString(6) + "\n\n";
                i++;
            }
            c.moveToNext();
        }

        if (i==0) {
            data = new String[]{"No Completed requests yet"};
        }

        List<String> pending_detail = new ArrayList<String>(Arrays.asList(data));

        final ArrayAdapter<String> mDetails = new ArrayAdapter<String>(
                getActivity(),R.layout.tab2_list_item,R.id.list_iem2_textview,pending_detail);

        View rootView = inflater.inflate(R.layout.maintainance_tab2,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.completed_list);
        listView.setAdapter(mDetails);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(getActivity(), Detail_and_Rate.class).putExtra(Intent.EXTRA_TEXT, mDetails.getItem(position));
                startActivity(i);
            }
        });
        return rootView;
    }
}
