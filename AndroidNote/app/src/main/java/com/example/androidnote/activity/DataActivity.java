package com.example.androidnote.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.GridLayout;

import com.example.androidnote.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.shangyizhou.develop.base.BaseActivity;
import com.shangyizhou.develop.helper.MPChartUtil;
import com.shangyizhou.develop.helper.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class DataActivity extends BaseActivity {

    @Override
    protected void onCreateChildren(Bundle bundle) {
        setContentView(R.layout.activity_data);
        initView();
    }

    GridLayout gridLayout;

    CombinedChart mWeightChart;
    List<String> xLabels;
    float maxWeight;
    float minWeight;
    LineChart topLeftlineChart;
    BarChart chartTopRight;
    LineChart chartBottomLeft;
    LineChart chartBottomRight;
    private void initView() {
        //找到图表控件
        topLeftlineChart = findViewById(R.id.chartTopLeft);
        chartTopRight = findViewById(R.id.chartTopRight);
        chartBottomLeft = findViewById(R.id.chartBottomLeft);
        chartBottomRight = findViewById(R.id.chartBottomRight);

        initLeftTop();
        initRightTop();
        initLeftBottom();
        initRightBottom();

    }

    private void initLeftTop() {
        // 准备数据. 比如我们创建一个有10个数据点的图表
        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 50) + 3;
            values.add(new Entry(i, val));
        }

        // 创建数据集
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setFillAlpha(110);

        //使用RGB颜色填充
        set1.setColor(Color.RED);
        set1.setValueTextColor(Color.BLUE);

        // 创建数据集的一个包装器
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        // 创建一个数据对象，与图表对象关联
        LineData lineData = new LineData(dataSets);
        topLeftlineChart.setData(lineData);
        topLeftlineChart.setScaleEnabled(true);
        topLeftlineChart.setScaleXEnabled(true);
        topLeftlineChart.setScaleYEnabled(true);
        //是否可以双指缩放--是
        topLeftlineChart.setPinchZoom(true);
        //是否可以双击放大--是
        topLeftlineChart.setDoubleTapToZoomEnabled(true);
        topLeftlineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                ToastUtil.getInstance().showToast(e.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        //无数据时显示
        topLeftlineChart.setNoDataText("没有获取到数据哦~");

        // 更新图表
        topLeftlineChart.notifyDataSetChanged();
        topLeftlineChart.invalidate();
    }

    private void initRightTop() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 60f));
        entries.add(new BarEntry(3f, 50f));
        // add other entries as you want
        BarDataSet dataSet = new BarDataSet(entries, "Bar data");
        BarData barData = new BarData(dataSet);

        chartTopRight.setData(barData);
        chartTopRight.invalidate(); // refresh
    }

    private void initLeftBottom() {

    }

    private void initRightBottom() {

    }
}