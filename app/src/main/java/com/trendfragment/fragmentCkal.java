package com.trendfragment;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.dietapp1.MainActivity;
import com.example.dietapp1.R;
import com.example.dietapp1.TrendActivity;
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
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;

public class fragmentCkal extends Fragment {

    LineChart mChart;
    ArrayList<String> daty;
    ArrayList<Integer> kcal;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View ckalView = inflater.inflate(R.layout.fragment_ckal,container, false);

        mChart = ckalView.findViewById(R.id.ckalChart);
        mChart.setTouchEnabled(true);
        mChart.setPinchZoom(true);
        mChart.getDescription().setEnabled(false);
        mChart.getLegend().setForm(Legend.LegendForm.NONE);
        mChart.getLegend().setTextColor(Color.WHITE);

        if(getArguments() != null)
        {
            daty = getArguments().getStringArrayList("daty");
            kcal = getArguments().getIntegerArrayList("kcal");

        }else
        {
            System.out.println("Puste jest!");
        }

        // ArrayList<String> weekdays = new ArrayList<>(Arrays.asList("3-6-2021","4-6-2021","5-6-2021","6-6-2021"));
        //weekdays.addAll(TrendActivity.arrayDaty);




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
        //values.add(new Entry(1, 100));
        //values.add(new Entry(2, 100));
        for(int i = 0; i<daty.size();i++)
        {
            values.add(new Entry(i,kcal.get(i)));
        }

        LineDataSet set1;
        if (mChart.getData() != null &&
                mChart.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) mChart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            mChart.getData().notifyDataChanged();
            mChart.notifyDataSetChanged();
        } else {
            LimitLine lmln = new LimitLine((float) MainActivity.loggedInProfil.getKalorie(),String.valueOf(MainActivity.loggedInProfil.getKalorie()));
            lmln.setLineWidth(2f);
            lmln.enableDashedLine(10f,5f,0);


            YAxis yaxis = mChart.getAxisLeft();
            yaxis.addLimitLine(lmln);
            yaxis.setAxisMinimum(0);
            yaxis.setAxisMaximum(getMax(MainActivity.loggedInProfil.getKalorie(),kcal));

            set1 = new LineDataSet(values, "Kalorie");
            set1.setDrawIcons(false);
            //set1.enableDashedLine(10f, 5f, 0f);
            //set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(Color.CYAN);
            set1.setCircleColor(Color.CYAN);
            set1.setLineWidth(1f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(12f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);


            set1.setFillColor(Color.CYAN);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            LineData data = new LineData(dataSets);
            mChart.setData(data);
        }


        return ckalView;
    }

    public static fragmentCkal newInstance(ArrayList<String> dates,ArrayList<Integer>kcal) {
        fragmentCkal fragment = new fragmentCkal();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("daty", dates);
        bundle.putIntegerArrayList("kcal",kcal);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static float getMax(double profilemax, ArrayList<Integer> list)
    {
        double max = 0;
        for(int i:list)
        {
            if(i>max)
                max = i;

        }
        if(max<profilemax)
            max = profilemax;

        return (float) ((float) 1.1*max);

    }
}
