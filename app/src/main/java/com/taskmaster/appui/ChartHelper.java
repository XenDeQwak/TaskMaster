/*
package com.taskmaster.appui;

import android.graphics.Color;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.IBarDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class ChartHelper {

    /**
     * Initializes the given BarChart with the provided stat values.
     *
     * @param chart        The BarChart view to be updated.
     * @param strength     The value for Strength.
     * @param intelligence The value for Intelligence.
     * @param drawValues   Whether to display value labels on the bars.
     * @param drawXLabels  Whether to display x-axis labels.
     */

/*
    public static void initBarChart(BarChart chart, float strength, float intelligence, boolean drawValues, boolean drawXLabels) {
        // Create entries for the stats
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, strength));         // Strength
        entries.add(new BarEntry(1f, intelligence));       // Intelligence

        BarDataSet dataSet = new BarDataSet(entries, "");
        // Define colors for each stat
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.RED);     // Strength
        colors.add(Color.GREEN);   // Intelligence
        dataSet.setColors(colors);

        List<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);

        BarData data = new BarData(dataSets);
        data.setValueTextSize(10f);
        data.setDrawValues(drawValues);

        // Set data and styling on the chart
        chart.setData(data);
        chart.getDescription().setEnabled(false);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setDrawLabels(true);
        yAxis.setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);

        Legend legend = chart.getLegend();
        legend.setEnabled(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setDrawLabels(drawXLabels);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setXOffset(-25f);
        xAxis.setAxisMinimum(-0.5f);

        List<String> labels = new ArrayList<>();
        labels.add("Strength");
        labels.add("Intelligence");
        xAxis.setValueFormatter(new IndexAxisValueFormatter(labels));

        chart.setTouchEnabled(false);
        chart.invalidate();
    }
}
 */
