package com.example.androidnote.fragment.data;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidnote.R;
import com.example.androidnote.db.helper.MessageHelper;
import com.example.androidnote.db.helper.SessionHelper;
import com.example.androidnote.manager.BmobManager;
import com.example.androidnote.model.Session;
import com.example.androidnote.model.TagResponse;
import com.example.androidnote.view.OtherToServer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import java.util.ArrayList;
import java.util.List;

public class BarChartFragment extends Fragment {
    BarChart barChart;

    public BarChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bar_chart, container, false);
        initView(view);
        return view;
    }

    private void setScale() {
        // 禁止缩放
        barChart.setAutoScaleMinMaxEnabled(false);
    }

    private void initData() {
        List<Integer> numbers = new ArrayList<>();
        List<Session> sessionList = SessionHelper.getInstance().takeAllByUser(BmobManager.getInstance().getObjectId());
        for (Session session : sessionList) {
            numbers.add(MessageHelper.getInstance().getMessageListBySession(session.getSessionId()).size());
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        for (int i = 0; i < numbers.size(); i++) {
            entries.add(new BarEntry(i + 1, numbers.get(i).floatValue()));
        }

        BarDataSet dataSet = new BarDataSet(entries, "Bar data");

        BarData barData = new BarData(dataSet);
        barChart.setData(barData);
    }

    private void setStyle() {
        // Y轴
        YAxis yRightAxis = barChart.getAxisRight();
        yRightAxis.setEnabled(false); // 禁用右侧Y轴

        YAxis yLeftAxis = barChart.getAxisLeft();
        yLeftAxis.setEnabled(true);
        yLeftAxis.setAxisMinimum(0f);

        // X轴
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 将X轴的位置设置为底部
        xAxis.setDrawGridLines(false); // 去除X轴网格线
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f); // 设置 labels 之间的最小间隔

        // 图例
        Legend legend = barChart.getLegend(); // 获取图例
        legend.setEnabled(true); // 禁用或隐藏图例

        // 描述
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);


        barChart.setDragEnabled(true); // 允许用户通过在图表上做拖动手势来水平滚动图表
        barChart.setScaleYEnabled(true); // 仅允许水平方向滚动
        barChart.setDrawBarShadow(false); // 设置每个柱子的阴影不显示
        barChart.setDrawValueAboveBar(true); // 设置每个柱子的数值

        // 去除Y轴网格线
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.invalidate(); // refresh



    }

    private void initView(View view) {
        barChart = view.findViewById(R.id.bar_chart);

        initData();
        setScale();
        setStyle();
    }

}