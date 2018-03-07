package com.pm.permitmanager;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CompanyFragment extends Fragment {


    public CompanyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list);
        String[] content = {
                "Ford",
                "Mazda",
                "VW",
                "Toyota"
        };
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, content);
        listView.setAdapter(arrayAdapter);
        return view;
    }

}
