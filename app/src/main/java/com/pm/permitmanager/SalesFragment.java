package com.pm.permitmanager;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class SalesFragment extends Fragment {
    private int totalSale, totalPaid, totalFactured;

    private String saleString, paidString, facturedString;

    private Button addSold, addPaid, addFac, removeSold, removePaid, removeFac;
    private TextView soldText, paidText, facText;

    private BarChart chart;

    public SalesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales, container, false);

        addSold = (Button) view.findViewById(R.id.addSold);
        addPaid = (Button) view.findViewById(R.id.addPaid);
        addFac = (Button) view.findViewById(R.id.addFact);

        removeSold = (Button) view.findViewById(R.id.removeSold);
        removePaid = (Button) view.findViewById(R.id.removePaid);
        removeFac = (Button) view.findViewById(R.id.removeFac);

        soldText = (TextView) view.findViewById(R.id.soldText);
        paidText = (TextView) view.findViewById(R.id.paidText);
        facText = (TextView) view.findViewById(R.id.facText);

        chart = (BarChart) view.findViewById(R.id.barchart);

        totalSale = SharedPrefrenceUtils.getInt(getContext(), "totalSale");
        if (totalSale >= 0){
            saleString = "Vendidos: "+totalSale;
        }else{
            SharedPrefrenceUtils.putInt(getContext(), "totalSale", 0);
            totalSale = 0;
            saleString = "Vendidos: "+totalSale;
        }
        soldText.setText(saleString);

        totalPaid = SharedPrefrenceUtils.getInt(getContext(), "totalPaid");
        if (totalPaid >= 0){
            paidString = "Pagados: "+totalPaid;
        }else{
            SharedPrefrenceUtils.putInt(getContext(), "totalPaid", 0);
            totalPaid = 0;
            paidString = "Pagados: "+totalPaid;
        }
        paidText.setText(paidString);

        totalFactured = SharedPrefrenceUtils.getInt(getContext(), "totalFactured");
        if (totalFactured >= 0){
            facturedString = "Facturados: "+totalFactured;
        }else{
            SharedPrefrenceUtils.putInt(getContext(), "totalFactured", 0);
            totalFactured = 0;
            facturedString = "Facturados: "+totalFactured;
        }
        facText.setText(facturedString);

        addSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                totalSale++;
                SharedPrefrenceUtils.putInt(getContext(), "totalSale", totalSale);
                updateText(soldText, "Vendidos: ", totalSale);
                updateChart();
            }
        });
        removeSold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalSale > 0){
                    totalSale--;
                    SharedPrefrenceUtils.putInt(getContext(), "totalSale", totalSale);
                    updateText(soldText, "Vendidos: ", totalSale);

                    if (totalPaid == totalSale+1) {
                        totalPaid--;
                        SharedPrefrenceUtils.putInt(getContext(), "totalPaid", totalPaid);
                        updateText(paidText, "Pagados: ", totalPaid);

                        if (totalFactured == totalPaid+1) {
                            totalFactured--;
                            SharedPrefrenceUtils.putInt(getContext(), "totalFactured", totalFactured);
                            updateText(facText, "Facturados: ", totalFactured);

                        }

                    }

                    updateChart();

                }
            }
        });

        addPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(totalPaid < totalSale){
                    totalPaid++;
                    SharedPrefrenceUtils.putInt(getContext(), "totalPaid", totalPaid);
                    updateText(paidText, "Pagados: ", totalPaid);
                    updateChart();
                }
            }
        });
        removePaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalPaid > 0){
                    totalPaid--;
                    SharedPrefrenceUtils.putInt(getContext(), "totalPaid", totalPaid);
                    updateText(paidText, "Pagados: ", totalPaid);

                    if (totalFactured == totalPaid+1) {
                        totalFactured--;
                        SharedPrefrenceUtils.putInt(getContext(), "totalFactured", totalFactured);
                        updateText(facText, "Facturados: ", totalFactured);

                    }

                    updateChart();
                }
            }
        });

        addFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalFactured < totalPaid){
                    totalFactured++;
                    SharedPrefrenceUtils.putInt(getContext(), "totalFactured", totalFactured);
                    updateText(facText, "Facturados: ", totalFactured);
                    updateChart();
                }
            }
        });
        removeFac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (totalFactured > 0){
                    totalFactured--;
                    SharedPrefrenceUtils.putInt(getContext(), "totalFactured", totalFactured);
                    updateText(facText, "Facturados: ", totalFactured);
                    updateChart();
                }
            }
        });

        updateChart();

        return view;
    }

    private void updateText(TextView textView, String prefix, int value){
        textView.setText(prefix+value);
    }

    private void updateChart(){
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry((float) totalSale, 0));
        barEntries.add(new BarEntry((float) totalPaid, 1));
        barEntries.add(new BarEntry((float) totalFactured, 2));
        BarDataSet dataSet = new BarDataSet(barEntries, "Permisos");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);

        ArrayList<String> labels = new ArrayList<>();
        labels.add("Vendidos");
        labels.add("Pagados");
        labels.add("Facturados");

        BarData barData =  new BarData(labels, dataSet);

        chart.setData(barData);
        chart.invalidate();
    }

}
