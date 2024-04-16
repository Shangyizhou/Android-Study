package com.example.androidnote.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.androidnote.R;
import com.example.androidnote.manager.DataCollectionManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import java.util.Random;

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


    LineChart lineChart;
    BarChart barChart;

    private void initView(View view) {
        //找到图表控件
        lineChart = view.findViewById(R.id.line_chart);
        barChart = view.findViewById(R.id.bar_chart);

        initLineChart();
        initRightTop();
    }

    private void initLineChart() {
        // 准备数据. 比如我们创建一个有10个数据点的图表
        int[] invokeData = DataCollectionManager.getYesterdayInvokeInfo();
        Random random = new Random(); // 创建Random实例

        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            // values.add(new Entry(i, invokeData[i]));
            values.add(new Entry(i, random.nextInt(100)));
        }

        // 创建数据集
        LineDataSet set1 = new LineDataSet(values, "24h 调用情况");
        set1.setFillAlpha(110);

        //使用RGB颜色填充
        set1.setColor(Color.BLUE);
        set1.setValueTextColor(Color.BLUE);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置曲线模式
        set1.setDrawFilled(true); //设置曲线填充的颜色，这里以起点和终点为基准，进行渐变。

        // 创建数据集的一个包装器
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        // 创建一个数据对象，与图表对象关联
        LineData lineData = new LineData(dataSets);
        lineChart.setDrawGridBackground(false);
        lineChart.setData(lineData);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(false);
        lineChart.setScaleXEnabled(false);
        lineChart.setScaleYEnabled(false);
        //是否可以双指缩放--是
        lineChart.setPinchZoom(false);
        //是否可以双击放大--是
        lineChart.setDoubleTapToZoomEnabled(false);
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                ToastUtil.getInstance().showToast(e.toString());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        // 设置y轴左侧坐标
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        leftAxis.setGranularity(1f);
        leftAxis.setGranularityEnabled(true);
        leftAxis.setDrawGridLines(false);

        float axisInterval = 20f;      // 你需要的轴间隔

        float axisMinimum = 0f;    // 假设的最小值
        float axisMaximum = 100f;  // 假设的最大值

        lineChart.getAxisLeft().setAxisMinimum(axisMinimum);    // 左轴最小值
        lineChart.getAxisLeft().setAxisMaximum(axisMaximum);    // 左轴最大值

        int labelCount = (int) ((axisMaximum - axisMinimum) / axisInterval) + 1;
        lineChart.getAxisLeft().setLabelCount(labelCount, true);  // 设置左轴的标签数，true表示强制的标签数目，即使有时候不能够完美的均匀分布

        // 关闭右侧y轴和顶部x轴的显示
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);

        // 创建一个空的Description对象
        Description description = new Description();
        description.setText("");

        // 将空的Description对象设置到图表
        lineChart.setDescription(description);

        //无数据时显示
        lineChart.setNoDataText("没有获取到数据哦~");

        // 更新图表
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
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

        barChart.setData(barData);
        barChart.invalidate(); // refresh
    }
}