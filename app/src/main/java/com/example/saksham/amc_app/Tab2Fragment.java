package com.example.saksham.amc_app;

import android.app.Fragment;
import android.content.Intent;
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        String[] data = {
                "Device:\n" +
                        "\tLenovo Thinkpad Edge" +
                        "\nDevice Problem:\n" +
                        "\tKeypad Failure" +
                        "\nVendor:\n" +
                        "\tABC AMC Services" +
                        "\nCompleted on:\n" +
                        "\tdd/mm/yy\n\n",
                "\nDevice:\n" +
                        "\tMitsubishi Mr. Slim" +
                        "\nDevice Problem:\n" +
                        "\tRemote Not Working" +
                        "\nVendor:\n" +
                        "\tBEST AMCs" +
                        "\nCompleted On:\n" +
                        "\tdd2/mm2/yy2\n\n"
        };
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
