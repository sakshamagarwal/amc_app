package com.example.saksham.amc_app;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.database.Cursor;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class vendorTab2Fragment extends Fragment{

    String[] data = {"You don't have any completed requests"};

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
            int size = 0;
            while (!c.isAfterLast()) {
                if (c.getString(6).equals("completed") || c.getString(6).equals("rejected")) {
                    size++;
                }
                c.moveToNext();
            }
            if (size!=0) {
                data = new String[size];
            }
        }
        int i = 0;
        c.moveToFirst();
        while (!c.isAfterLast()) {
            if (c.getString(6).equals("completed") || c.getString(6).equals("rejected")) {
                data[i] = "Device: " + c.getString(2) + "\nProblem: " + c.getString(3) + "\nUser: " + c.getString(1) + "\nDates: " + c.getString(5) + "\nStatus: " + c.getString(6) + "\n\n";
                i++;
            }
            c.moveToNext();
        }


        List<String> completed_detail = new ArrayList<String>(Arrays.asList(data));

        ArrayAdapter<String> mDetails = new ArrayAdapter<String>(
                getActivity(),R.layout.vendortab2fragment,R.id.list_iem2_textview,completed_detail);

        View rootView = inflater.inflate(R.layout.vendenq_tab2,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.pending_list2);
        listView.setAdapter(mDetails);
        return rootView;
    }

}
