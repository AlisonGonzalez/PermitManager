package com.pm.permitmanager.ArrayAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.pm.permitmanager.Models.Company;

import java.util.List;
import java.util.Locale;

/**
 * Created by Lalo on 3/6/18.
 */

public class CompanyArrayAdapter extends ArrayAdapter<Company> {

    private Context context;

    public CompanyArrayAdapter(@NonNull Context context, int resource, @NonNull List<Company> objects) {
        super(context, resource, objects);
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Company company = getItem(position);
        if (company != null) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(android.R.id.text1);
            textView.setText(company.getName());
            return convertView;
        }

        return super.getView(position, convertView, parent);
    }
}
