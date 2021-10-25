package com.trendfragment;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.dietapp1.MainActivity;
import com.example.dietapp1.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

public class fragmentProt extends Fragment {

    LineChart mChart;
    ArrayList<String> daty;
    ArrayList<Integer> prot;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View protView = inflater.inflate(R.layout.fragment_prot,container, false);

        mChart = protView.findViewById(R.id.protChart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setForm(Legend.LegendForm.NONE);
        mChart.getLegend().setTextColor(Color.WHITE);

        if(getArguments() != null)
        {
            daty = getArguments().getStringArrayList("daty");
            prot = getArguments().getIntegerArrayList("prot");

        }else
        {
            System.out.println("Puste jest!");
        }


        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(-10);

        xAxis.setEnabled(true);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mChart.getXAxis().setValueFormatter(new com.github.mikephil.charting.formatter.IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                //return weekdays.get((int) value);
                return daty.get((int) value);
            }
        });


        ArrayList<Entry> values = new ArrayList<>();
        for(int i = 0; i<daty.size();i++)
        {
            values.add(new Entry(i,prot.get(i)));
        }

        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            LimitLine lmln = new LimitLine((float) MainActivity.loggedInProfil.getBialko(),String.valueOf(MainActivity.loggedInProfil.getBialko()));
            lmln.setLineWidth(2f);
            lmln.enableDashedLine(10f,5f,0);


            YAxis yaxis = mChart.getAxisLeft();
            yaxis.addLimitLine(lmln);
            yaxis.setAxisMinimum(0);
            yaxis.setAxisMaximum(fragmentCkal.getMax(MainActivity.loggedInProfil.getBialko(),prot));



            set1 = new LineDataSet(values, "Białko");
            set1.setDrawIcons(false);
            //set1.enableDashedLine(10f, 5f, 0f);
            //set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.RED);
            set1.setCircleColor(Color.RED);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(12f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            set1.setFillColor(Color.RED);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }


        return protView;
    }


    public static fragmentProt newInstance(ArrayList<String> dates,ArrayList<Integer>prot) {
        fragmentProt fragment = new fragmentProt();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("daty", dates);
        bundle.putIntegerArrayList("prot",prot);
        fragment.setArguments(bundle);
        return fragment;
    }
}
