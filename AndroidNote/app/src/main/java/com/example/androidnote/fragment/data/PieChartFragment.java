package com.example.androidnote.fragment.data;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidnote.R;
import com.example.androidnote.activity.DataActivity;
import com.example.androidnote.manager.SessionManager;
import com.example.androidnote.model.Message;
import com.example.androidnote.model.MessageExtern;
import com.example.androidnote.model.Session;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.shangyizhou.develop.log.SLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PieChartFragment extends Fragment {
    private static final String TAG = PieChartFragment.class.getSimpleName();

    public PieChartFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);
        initView(view);
        return view;
    }

    PieChart pieChart;
    private void initView(View view) {
        getIntentData();
        SLog.i(TAG, "initPieChart");

        pieChart = view.findViewById(R.id.pie_chart);

        // 禁用图表上半部分的绘制
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);

        ArrayList<PieEntry> yValues = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : intentMap.entrySet()) {
            if (entry == null || TextUtils.isEmpty(entry.getKey())) {
                continue;
            }
            System.out.println(entry.getKey() + ": " + entry.getValue());
            yValues.add(new PieEntry(entry.getValue(), entry.getKey())); // 添加数据和对应的标签
        }

        PieDataSet dataSet = new PieDataSet(yValues, "");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);

        // 自定义文本标签的样式
        ValueFormatter percentFormatter = new PercentFormatter();
        dataSet.setValueFormatter(percentFormatter);
        dataSet.setValueTextSize(11f);
        dataSet.setValueTextColor(Color.BLACK);

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(ColorTemplate.VORDIPLOM_COLORS[0]);
        colors.add(ColorTemplate.VORDIPLOM_COLORS[1]);
        colors.add(ColorTemplate.VORDIPLOM_COLORS[2]);
        colors.add(ColorTemplate.VORDIPLOM_COLORS[3]);

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        pieChart.setData(data);

        // 刷新图表
        pieChart.invalidate();
    }

    private HashMap<String, Integer> intentMap = new HashMap<>();
    private void getIntentData() {
        Gson json = new Gson();
        for (Session session : SessionManager.getInstance().getSessionList()) {
            List<Message> messageList = SessionManager.getInstance().getSessionMessages(session);
            for (Message message : messageList) {
                String ext = message.getExt();
                MessageExtern messageExtern = json.fromJson(ext, MessageExtern.class);
                if (messageExtern != null) {
                    String intent = messageExtern.getIntent();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        intentMap.put(intent, intentMap.getOrDefault(intent, 0) + 1);
                    }
                }
            }
        }
    }
}