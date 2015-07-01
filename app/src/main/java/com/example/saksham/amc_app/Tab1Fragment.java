package com.example.saksham.amc_app;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by saksham on 22/6/15.
 */
public class Tab1Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        String[] data = {
                "Device:\n" +
                        "\tLenovo Thinkpad Edge" +
                        "\nDevice Problem:\n" +
                        "\tKeypad Failure" +
                        "\nVendor:\n" +
                        "\tABC AMC Services" +
                        "\nStatus:\n" +
                        "\tEngineer Assigned\n\n",
                "\nDevice:\n" +
                        "\tMitsubishi Mr. Slim" +
                        "\nDevice Problem:\n" +
                        "\tRemote Not Working" +
                        "\nVendor:\n" +
                        "\tBEST AMCs" +
                        "\nStatus:\n" +
                        "\tRequest Recieved\n\n"
        };
        List<String> completed_detail = new ArrayList<String>(Arrays.asList(data));

        ArrayAdapter<String> mDetails = new ArrayAdapter<String>(
                getActivity(),R.layout.tab1_list_item,R.id.list_iem1_textview,completed_detail);

        View rootView = inflater.inflate(R.layout.maintainance_tab1,container,false);
        ListView listView = (ListView)rootView.findViewById(R.id.pending_list);
        listView.setAdapter(mDetails);
        return rootView;
    }
}