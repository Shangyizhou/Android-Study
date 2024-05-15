package com.example.androidnote.fragment;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.UiThread;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidnote.R;
import com.example.androidnote.db.helper.MessageHelper;
import com.example.androidnote.manager.DataCollectionManager;
import com.example.androidnote.manager.SessionManager;
import com.example.androidnote.model.Message;
import com.example.androidnote.model.Session;
import com.example.androidnote.model.TagResponse;
import com.example.androidnote.view.OtherToServer;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
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
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.gson.Gson;
import com.shangyizhou.develop.helper.ToastUtil;
import com.shangyizhou.develop.log.SLog;
import com.shangyizhou.develop.net.IResponse;

import java.util.ArrayList;
import java.util.List;
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
    private static final String TAG = "DataFragment";


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
    TextView keyWord;

    private void initView(View view) {
        // 找到图表控件
        lineChart = view.findViewById(R.id.line_chart);
        barChart = view.findViewById(R.id.bar_chart);
        keyWord = view.findViewById(R.id.tv_key_list);

        initLineChart();
        initBarChart();
        initKeyWord();
    }


    private void getLineChartData() {
        List<Session> sessionList = SessionManager.getInstance().getSessionList();
        for (Session session : sessionList) {
            MessageHelper.getInstance().getMessageListBySession(session.getSessionId());
        }
    }

    private void initLineChart() {
        // 准备数据. 比如我们创建一个有10个数据点的图表
        int[] invokeData = DataCollectionManager.getYesterdayInvokeInfo();
        Random random = new Random(); // 创建Random实例

        ArrayList<Entry> values = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            // values.add(new Entry(i, invokeData[i]));
            values.add(new Entry(i, random.nextInt(100)));
        }

        // 创建数据集
        LineDataSet set1 = new LineDataSet(values, "24h 调用情况");
        set1.setFillAlpha(110);

        // 使用RGB颜色填充
        set1.setColor(Color.BLUE);
        set1.setValueTextColor(Color.BLUE);
        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER); // 设置曲线模式
        set1.setDrawFilled(true); // 设置曲线填充的颜色，这里以起点和终点为基准，进行渐变。

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
        // 是否可以双指缩放--是
        lineChart.setPinchZoom(false);
        // 是否可以双击放大--是
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

        // 无数据时显示
        lineChart.setNoDataText("没有获取到数据哦~");

        // 更新图表
        lineChart.notifyDataSetChanged();
        lineChart.invalidate();
    }

    private void initBarChart() {
        ArrayList<BarEntry> entries = new ArrayList<>();
        /**
         * x轴位置和y轴高度
         */
        entries.add(new BarEntry(1f, 30f));
        entries.add(new BarEntry(2f, 80f));
        entries.add(new BarEntry(3f, 60f));

        // add other entries as you want
        BarDataSet dataSet = new BarDataSet(entries, "Bar data");
        BarData barData = new BarData(dataSet);

        /**
         * y轴
         */
        YAxis yAxis = barChart.getAxisRight();
        yAxis.setEnabled(false); // 禁用右侧Y轴

        /**
         * x轴
         */
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 将X轴的位置设置为底部
        xAxis.setDrawGridLines(false); // 去除X轴网格线
        // xAxis.setCenterAxisLabels(true); // 设置为 true 让 labels 居中
        // 禁止缩放
        barChart.setAutoScaleMinMaxEnabled(false);
        xAxis.setAxisMinimum(0f);
        xAxis.setGranularity(1f); // 设置 labels 之间的最小间隔

        // 设置x轴的标签
        List<String> labels = new ArrayList<>();
        labels.add("");
        labels.add("会话1");
        labels.add("会话2");
        labels.add("会话3");

        ValueFormatter formatter = new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int index = (int) value;
                if (index < 0 || index >= labels.size()) {
                    return "";
                }
                return labels.get(index);
            }
        };
        xAxis.setValueFormatter(formatter);

        Legend legend = barChart.getLegend(); // 获取图例
        legend.setEnabled(false); // 禁用或隐藏图例

        /**
         * 设置形容
         */
        Description description = new Description();
        description.setEnabled(false);
        barChart.setDescription(description);

        // 允许用户通过在图表上做拖动手势来水平滚动图表
        barChart.setDragEnabled(true);
        // 仅允许水平方向滚动
        barChart.setScaleYEnabled(false);

        // dataSet.setBarBorderWidth(0.9f);  //这里可以设置每个柱体相对大小的空闲部分（如果你有多个并列的 barEntry）
        // barData.setBarWidth(0.6f);  //这里可以设置每个柱体的宽度

        // 去除Y轴网格线
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.setData(barData);
        barChart.invalidate(); // refresh
    }

    private void initKeyWord() {
        OtherToServer.callKeyWords("", new IResponse() {
            @Override
            public void onSuccess(String originJson) {
                TagResponse tagResponse = new Gson().fromJson(originJson, TagResponse.class);
                SLog.i(TAG, "Gson after translate" + tagResponse);
                List<String> keyWords = new ArrayList<>();
                String key = "";
                for (TagResponse.Results res : tagResponse.getResults()) {
                    // keyWords.add(res.getWord());
                    key += res.getWord() + ", ";
                }
                String finalKey = key;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        keyWord.setText(finalKey);
                    }
                });
            }

            @Override
            public void onFailure(String errorMsg) {

            }
        });
    }
}