package com.example.androidnote.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.shangyizhou.develop.helper.ToastUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DataFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DataFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DataFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DataFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DataFragment newInstance(String param1, String param2) {
        DataFragment fragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_data, container, false);
        initView(view);
        return view;
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
    private void initView(View view) {
        //找到图表控件
        topLeftlineChart = view.findViewById(R.id.chartTopLeft);
        chartTopRight = view.findViewById(R.id.chartTopRight);
        // chartBottomLeft = view.findViewById(R.id.chartBottomLeft);
        // chartBottomRight = view.findViewById(R.id.chartBottomRight);

        initLineChart();
        initRightTop();
        // initLeftBottom();
        // initRightBottom();
    }

    private void initLineChart() {
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
        topLeftlineChart.setDrawGridBackground(false);
        topLeftlineChart.setData(lineData);
        topLeftlineChart.setDragEnabled(false);
        topLeftlineChart.setScaleEnabled(false);
        topLeftlineChart.setScaleXEnabled(false);
        topLeftlineChart.setScaleYEnabled(false);
        //是否可以双指缩放--是
        topLeftlineChart.setPinchZoom(false);
        //是否可以双击放大--是
        topLeftlineChart.setDoubleTapToZoomEnabled(false);
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
        /**
         * x轴位置和y轴高度
         */
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